package ie.aomonitor;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import ie.aomonitor.utils.Connectivity;
import ie.aomonitor.utils.SharedPreferencesUtils;

import static ie.aomonitor.Constants.*;

/**
 * Created by silvat on 16/05/2017.
 */
public abstract class Monitor extends Thread{

    private ConnectivityManager cm;
    private NetworkInfo activeNetwork;
    private boolean isConnected = false;
    private boolean isWiFi = false;
    private String urlEndPoint;

    private SharedPreferencesUtils sharedPreferences;
    private Context context;
    private String methodName;

    public Monitor(String url, String methodName, Context context){
        this.urlEndPoint = url;
        this.methodName = methodName;
        sharedPreferences = new SharedPreferencesUtils(context);
        this.context = context;
    }

    public static final void initMonitor(){
        //TODO implement the initMonitor
        //sync the information with the Cloud
        synchronizeInfo();
        //Retrieve specs from the devices
        try {
            getDeviceSpecs();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Log.i("Display : ", Build.BRAND);
        Log.i("Display : ", Build.DEVICE);
        Log.i("Display : ", Build.HARDWARE);
        Log.i("Display : ", Build.MODEL);
        Log.i("Display : ", Build.PRODUCT);

        Log.i("ManuFacturer :", Build.MANUFACTURER);
        Log.i("Board : ", Build.BOARD);
        Log.i("Display : ", Build.DISPLAY);
    }

    public static String getDeviceSpecs() throws IOException {

        String str1 = "/proc/meminfo";
        String str2;
        String[] arrayOfString;
        long initial_memory = 0;
        FileReader localFileReader = null;
        BufferedReader localBufferedReader = null;

        try {
            localFileReader = new FileReader(str1);
            localBufferedReader = new BufferedReader(localFileReader, 8192);
            str2 = localBufferedReader.readLine();//meminfo
            arrayOfString = str2.split("\\s+");
            for (String num : arrayOfString) {
                Log.i(str2, num + "\t");
            }
            //total Memory
            initial_memory = Integer.valueOf(arrayOfString[1]).intValue() * 1024;

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            localBufferedReader.close();
        }

        return Long.toString(initial_memory);
    }

    public static void syncDeviceProfile(){
        // TODO here the application will retrieve an existent profile from the cloud or collect data for this one.

    }

    private static void synchronizeInfo() {
        // TODO define when to sync the information to the cloud

    }

    @Override
    public void run(){

        long startTime = System.nanoTime();

        NetworkInfo ni = Connectivity.getNetworkInfo(context);
        boolean isNewScenario = isNewScenario();

        if(isNewScenario) {
            if(!ni.isConnected()){
                task();
            }else{
                //TODO call the method remotelly
                System.out.print(this.urlEndPoint);
            }
        }else {

            boolean runOnTheCloud = getConfig(methodName);
            if(!runOnTheCloud){
                task();
            }else{
                //TODO call the method remotelly
                System.out.print(this.urlEndPoint);
            }
        }

        long endTime = System.nanoTime();

        //TODO log this info locally for synchronize it later
        System.out.println("That took " + (endTime - startTime) + " milliseconds");

    }

    private boolean getConfig(String methodName) {
        //TODO implement method to decide where to run the workload
        return false;
    }

    /**
     *
     * DEFINE SCENARIOS
     *
     */
    private boolean isNewScenario(){

        if(!Connectivity.isConnected(context)){
            return sharedPreferences.getPreferenceBoolean(OFF_LINE_TESTED);
        }else if(Connectivity.isConnectedWifi(context)){
            return sharedPreferences.getPreferenceBoolean(WIFI_TESTED);
        }else if(Connectivity.isConnectedMobile(context)){
            return sharedPreferences.getPreferenceBoolean(MOBILE_TESTED);
        }else if(!Connectivity.isConnectedFast(context)){
            return sharedPreferences.getPreferenceBoolean(SLOW_CONNECTED);
        }else if(Connectivity.isConnectedFast(context)){
            return sharedPreferences.getPreferenceBoolean(FAST_CONNECTED);
        }

        return true;
    }

    public abstract void task();
}

