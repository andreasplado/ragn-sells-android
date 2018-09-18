package utils;

import android.content.Context;
import android.content.IntentFilter;
import android.location.LocationManager;
import android.net.ConnectivityManager;

/**
 * RagnSells
 * Created by Andreas on 24.05.2017.
 */

public class Receivers {

    public static void registerMobileDataReciver(Context context, MobileDataManager mobileDataManager) {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        context.registerReceiver(mobileDataManager.mobileDataBroadcastReceiver, intentFilter);
    }

    public static void registerGPSReciver(Context context, GPSManager gpsManager) {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(LocationManager.PROVIDERS_CHANGED_ACTION);
        context.registerReceiver(gpsManager.gpsBroadcastReceiver, intentFilter);
    }
}
