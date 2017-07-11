package ie.aomonitor.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

/**
 * Check device's network connectivity and speed 
 * @author emil http://stackoverflow.com/users/220710/emil
 *
 */
public class Connectivity {

    /**
     * Get the network info
     * @param context
     * @return
     */
    public static NetworkInfo getNetworkInfo(Context context){
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo();
    }

    /**
     * Check if there is any connectivity
     * @param context
     * @return
     */
    public static boolean isConnected(Context context){
        NetworkInfo info = Connectivity.getNetworkInfo(context);
        return (info != null && info.isConnected());
    }

    /**
     * Check if there is any connectivity to a Wifi network
     * @param context
     * @param type
     * @return
     */
    public static boolean isConnectedWifi(Context context){
        NetworkInfo info = Connectivity.getNetworkInfo(context);
        return (info != null && info.isConnected() && info.getType() == ConnectivityManager.TYPE_WIFI);
    }

    /**
     * Check if there is any connectivity to a mobile network
     * @param context
     * @param type
     * @return
     */
    public static boolean isConnectedMobile(Context context){
        NetworkInfo info = Connectivity.getNetworkInfo(context);
        return (info != null && info.isConnected() && info.getType() == ConnectivityManager.TYPE_MOBILE);
    }

    /**
     * Check if there is fast connectivity
     * @param context
     * @return
     */
    public static boolean isConnectedFast(Context context){
        NetworkInfo info = Connectivity.getNetworkInfo(context);
        return (info != null && info.isConnected() && Connectivity.isConnectionFast(info.getType(),info.getSubtype()));
    }

    /**
     * Check if the connection is fast
     * @param type
     * @param subType
     * @return
     */
    public static boolean isConnectionFast(int type, int subType){
        boolean ret = false;
        if(type==ConnectivityManager.TYPE_WIFI){
            ret = true;
        }else if(type==ConnectivityManager.TYPE_MOBILE){
            switch(subType){
                case TelephonyManager.NETWORK_TYPE_1xRTT:
                    ret = false;
                    break;// ~ 50-100 kbps
                case TelephonyManager.NETWORK_TYPE_CDMA:
                    ret = false;
                    break; // ~ 14-64 kbps
                case TelephonyManager.NETWORK_TYPE_EDGE:
                    ret = false; // ~ 50-100 kbps
                    break;
                case TelephonyManager.NETWORK_TYPE_EVDO_0:
                    ret = true; // ~ 400-1000 kbps
                    break;
                case TelephonyManager.NETWORK_TYPE_EVDO_A:
                    ret = true; // ~ 600-1400 kbps
                    break;
                case TelephonyManager.NETWORK_TYPE_GPRS:
                    ret = false; // ~ 100 kbps
                    break;
                case TelephonyManager.NETWORK_TYPE_HSDPA:
                    ret = true; // ~ 2-14 Mbps
                    break;
                case TelephonyManager.NETWORK_TYPE_HSPA:
                    ret = true; // ~ 700-1700 kbps
                    break;
                case TelephonyManager.NETWORK_TYPE_HSUPA:
                    ret = true; // ~ 1-23 Mbps
                    break;
                case TelephonyManager.NETWORK_TYPE_UMTS:
                    ret = true; // ~ 400-7000 kbps
                    break;
                /*
                 * Above API level 7, make sure to set android:targetSdkVersion
                 * to appropriate level to use these
                 */
                case TelephonyManager.NETWORK_TYPE_EHRPD: // API level 11
                    ret = true; // ~ 1-2 Mbps
                    break;
                case TelephonyManager.NETWORK_TYPE_EVDO_B: // API level 9
                    ret = true; // ~ 5 Mbps
                    break;
                case TelephonyManager.NETWORK_TYPE_HSPAP: // API level 13
                    ret = true; // ~ 10-20 Mbps
                    break;
                case TelephonyManager.NETWORK_TYPE_IDEN: // API level 8
                    ret = false; // ~25 kbps
                    break;
                case TelephonyManager.NETWORK_TYPE_LTE: // API level 11
                    ret = true; // ~ 10+ Mbps
                    break;
                // Unknown
                case TelephonyManager.NETWORK_TYPE_UNKNOWN:
                default:
                    ret = false;
                    break;
            }
        }else{
            ret = false;
        }
        return ret;
    }

}