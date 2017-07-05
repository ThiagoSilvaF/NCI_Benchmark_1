package ie.aomonitor;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.v4.util.Pair;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import ie.aomonitor.endpoints.AsyncResponse;
import ie.aomonitor.endpoints.GetConfigServletAsyncTask;
import ie.aomonitor.endpoints.PersistLogsServletAsyncTask;
import ie.aomonitor.endpoints.SyncDeviceServletAsyncTask;
import ie.aomonitor.utils.Connectivity;
import ie.aomonitor.utils.SharedPreferencesUtils;

import static ie.aomonitor.Constants.*;

/**
 * Created by silvat on 16/05/2017.
 */
public abstract class Monitor extends Thread {

    private ConnectivityManager cm;
    private NetworkInfo activeNetwork;
    private boolean isConnected = false;
    private boolean isWiFi = false;
    private String urlEndPoint;
    private static DatabaseHandler db;

    private SharedPreferencesUtils sharedPreferences;
    private Context context;
    private String methodName;
    private String appId;

    private static DeviceLog deviceLog;

    public Monitor(String appId, String methodName, final Context context){
        this.methodName = methodName;
        this.appId      = appId;

        sharedPreferences = new SharedPreferencesUtils(context);
        this.context      = context;



    }

    public static final void initMonitor(final Context context, final String appId){
        //Retrieve specs from the devices
        String memory = null;
        try {
            HashMap<String, String> map = getMemSpecs();
            memory = map.get("MemTotal");
        } catch (IOException e) {
            e.printStackTrace();
        }
        String processor = null;
        try {
            processor = getCPUSpecs();
        } catch (IOException e) {
            e.printStackTrace();
        }

        deviceLog = new DeviceLog(Build.BRAND,Build.DEVICE, Build.HARDWARE, Build.MODEL,
                Build.PRODUCT, Build.MANUFACTURER, Build.BOARD, memory, processor
        );

        db                = new DatabaseHandler(context);

        if (Connectivity.isConnected(context)) {
            //sync to the cloud all the latest info into the device
            syncDeviceProfile(context, appId+","+Build.DEVICE);


        }

    }

    private static String getCPUSpecs() throws IOException {
        ProcessBuilder processBuilder;
        Process process ;
        InputStream inputStream;
        String[] DATA = {"/system/bin/cat", "/proc/cpuinfo"};
        byte[] byteArry = new byte[1024];
        String Holder = "";
        try{
            processBuilder = new ProcessBuilder(DATA);
            process = processBuilder.start();
            inputStream = process.getInputStream();
            while(inputStream.read(byteArry) != -1){
                Holder = Holder + new String(byteArry);
            }
            inputStream.close();
        } catch(IOException ex){
            ex.printStackTrace();
        }
        String ret = null;
        for(String r : Holder.split("\n")){
            if(r.contains("Processor")){
                ret = r.split(":")[1].trim();
            }
        }
        return ret;

    }

    private static HashMap<String,String> getMemSpecs() throws IOException {

        ProcessBuilder processBuilder;
        Process process ;
        InputStream inputStream;

        String[] DATA = {"/system/bin/cat", "/proc/meminfo"};
        byte[] byteArry = new byte[1024];
        String Holder = "";
        try{
            processBuilder = new ProcessBuilder(DATA);
            process = processBuilder.start();
            inputStream = process.getInputStream();
            while(inputStream.read(byteArry) != -1){
                Holder = Holder + new String(byteArry);
            }
            inputStream.close();
        } catch(IOException ex){
            ex.printStackTrace();
        }

        HashMap<String, String> ret = new HashMap<String,String>();
        for(String r : Holder.split("\n")){
            if(r.contains("MemTotal")){
                ret.put("MemTotal",  r.split(":")[1].trim());
            }else if(r.contains("MemFree")){
                ret.put("MemFree",  r.split(":")[1].trim());
            }
        }

        return ret;

    }

    public static DatabaseHandler getDbInstance(){
        return db;
    }

    public static void syncDeviceProfile(Context context, String json) {
        SyncDeviceServletAsyncTask asyncTask = new SyncDeviceServletAsyncTask(new AsyncResponse() {

            @Override
            public void processFinish(String output) {
                System.out.print("chegando aqui: " + output);
            }

        });
        asyncTask.execute(new Pair<Context, String>(context, json));

        PersistLogsServletAsyncTask persistTask = new PersistLogsServletAsyncTask(new AsyncResponse() {

            @Override
            public void processFinish(String output) {
                System.out.print("chegando aqui 2: " + output);
            }

        }, context);
        persistTask.execute(new Pair<Context, String>(context, json));
    }

    @Override
    public void run(){
        long startTime = System.nanoTime();

        if(Connectivity.isConnected(context)){
            int i = isNewScenario();
            if(i == DEVICE_OFFLINE){
                taskLocal();
            }else if( i == DEVICE_SLOW_CONNECTED){
                taskRemote();
            }else if( i == DEVICE_FAST_CONNECTED){
                taskRemote();
            }else if( i == 0) {
                if (getConfig(appId, methodName)) {
                    taskRemote();
                } else {
                    taskLocal();
                }
            }
        }else{
            taskLocal();
        }

        long endTime = System.nanoTime();
        deviceLog.setStartTime(startTime);
        deviceLog.setEndTime(endTime);
        deviceLog.setAppKey(appId);
        deviceLog.setMethodName(methodName);

        saveWorkloadDetails(deviceLog);


    }

    private void saveWorkloadDetails(DeviceLog deviceLog){
        db.addProcessInfo(deviceLog);
    }

    /**
     *
     * @param methodName
     * @return
     *
     * true = run online
     * false = run locally
     * This method will decide where to run the workload
     */
    private boolean getConfig(String appId, String methodName) {
        GetConfigServletAsyncTask asyncTask = new GetConfigServletAsyncTask(deviceLog, new AsyncResponse() {

            @Override
            public void processFinish(String output) {
                System.out.print("chegando aqui: " + output);
            }

        });
        asyncTask.execute(new Pair<String, String>(appId,methodName));

        return false;
    }

    /**
     *
     * DEFINE SCENARIOS
     *
     * 1 for offline
     * 2 for slow conn
     * 3 for fast conn
     */
    private int isNewScenario(){

        if(!sharedPreferences.getPreferenceBoolean(OFF_LINE_TESTED)){
            return DEVICE_OFFLINE;
        }else if (!sharedPreferences.getPreferenceBoolean(SLOW_CONNECTED)){
            return DEVICE_SLOW_CONNECTED;
        }else if (!sharedPreferences.getPreferenceBoolean(FAST_CONNECTED)){
            return DEVICE_FAST_CONNECTED;
        }
        return 0;
    }

    public abstract void taskLocal();
    public abstract void taskRemote();
}