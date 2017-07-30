package ie.aomonitor;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.v4.util.Pair;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
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
        db                = new DatabaseHandler(context);
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

        GetConfigServletAsyncTask getConfig = new GetConfigServletAsyncTask( json , new AsyncResponse() {

            @Override
            public void processFinish(String output) {
                System.out.print(output);
            }

        });
        getConfig.execute(new Pair<Context, String>(context, json));

        PersistLogsServletAsyncTask persistTask = new PersistLogsServletAsyncTask(new AsyncResponse() {

            @Override
            public void processFinish(String output) {
                System.out.print(output);
            }

        }, context);
        persistTask.execute(new Pair<Context, String>(context, json));

        SyncDeviceServletAsyncTask asyncTask = new SyncDeviceServletAsyncTask(new AsyncResponse() {

            @Override
            public void processFinish(String output) {

                ConfigLog conf = new com.google.gson.Gson().fromJson(output, ConfigLog.class);
                if(conf != null) {

                    if (db.getConfigByMethodName(conf.getName()) == null) {
                        db.addConfigInfo(conf.getName(), conf.getValue());
                    } else {
                        db.updateConfigInfo(conf.getName(), conf.getValue());
                    }
                }
            }

        });
        asyncTask.execute(new android.util.Pair<Context, String>(context, json));

    }

    @Override
    public void run(){
        long startTime = System.nanoTime();
        boolean isTaskLocal = false;
        if(Connectivity.isConnected(context)){
            int i = isNewScenario();
            if(i == DEVICE_OFFLINE){
                taskLocal();
                isTaskLocal = true;
            }else if( i == DEVICE_SLOW_CONNECTED){
                taskRemote();
            }else if( i == DEVICE_FAST_CONNECTED){
                taskRemote();
            }else if( i == 0) {
                if (getConfig(appId, methodName)) {
                    taskRemote();
                } else {
                    taskLocal();
                    isTaskLocal = true;
                }
            }
        }else{
            taskLocal();
            isTaskLocal = true;
        }

        long endTime = System.nanoTime();
        deviceLog.setStartTime(startTime);
        deviceLog.setEndTime(endTime);
        deviceLog.setAppKey(appId);
        deviceLog.setMethodName(methodName);
        if(isTaskLocal) {
            deviceLog.setEnvironment("LOCAL");
        }else{
            deviceLog.setEnvironment("REMOTE");
        }
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
        String ret = db.getConfigByMethodName(methodName);
        if(ret == null){
            return false;
        }else if (ret.equals("0")) {
            return false;

            //}else if (ret.equals("1")){
        }else{
            return true;
        }
        //return false;




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
        if(sharedPreferences.getPreferenceString(OFF_LINE_TESTED) ==  null){
            sharedPreferences.setPreference(OFF_LINE_TESTED, "true");
            return DEVICE_OFFLINE;
        }else if(!Connectivity.isConnectedFast(context)){
            if(sharedPreferences.getPreferenceString(SLOW_CONNECTED) ==  null){
                sharedPreferences.setPreference(SLOW_CONNECTED, "true");
                return DEVICE_SLOW_CONNECTED;
            }
        } if(Connectivity.isConnectedFast(context)){
            if(sharedPreferences.getPreferenceString(FAST_CONNECTED) ==  null){
                sharedPreferences.setPreference(FAST_CONNECTED, "true");
                return DEVICE_FAST_CONNECTED;
            }
        }else{
            return 0;
        }
        return 0;
        /**
        if((sharedPreferences.getPreferenceString(OFF_LINE_TESTED) != null)
                && (!sharedPreferences.getPreferenceString(OFF_LINE_TESTED).equals("true"))){
            sharedPreferences.setPreference(OFF_LINE_TESTED, "true");
            return DEVICE_OFFLINE;
        }else if ((sharedPreferences.getPreferenceString(SLOW_CONNECTED) != null)
                &&(!sharedPreferences.getPreferenceString(SLOW_CONNECTED).equals("true"))){
            sharedPreferences.setPreference(SLOW_CONNECTED, "true");
            return DEVICE_SLOW_CONNECTED;
        }else if ((sharedPreferences.getPreferenceString(FAST_CONNECTED) != null)
                &&(!sharedPreferences.getPreferenceString(FAST_CONNECTED).equals("true"))){
            sharedPreferences.setPreference(FAST_CONNECTED, "true");
            return DEVICE_FAST_CONNECTED;
        }else{
            return 0;
        }*/
    }

    public abstract void taskLocal();
    public abstract void taskRemote();
}