package ie.aomonitor;

/**
 * Created by Administrativo on 18/05/2017.
 */

public interface Constants {


    String PREFS_NAME = "AOMonitor";

    String WIFI_TESTED = "WIFI_TESTED";
    String MOBILE_TESTED = "MOBILE_TESTED";
    String FAST_CONNECTED = "FAST_CONNECTED";
    String SLOW_CONNECTED = "SLOW_CONNECTED";
    String OFF_LINE_TESTED = "OFF_LINE_TESTED";

    int DEVICE_OFFLINE = 1;
    int DEVICE_SLOW_CONNECTED = 2;
    int DEVICE_FAST_CONNECTED = 3;

    String GAE_ENDPOINT = "https://maximal-sphere-169913.appspot.com/";
   // String GAE_ENDPOINT = "https://10.0.2.2:8080/";
}
