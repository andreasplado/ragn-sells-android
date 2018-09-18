package utils;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

/**
 * Created by Andreas on 06.04.2017.
 */

public class GPSManager {

    private Activity activity;
    private Context context;
    private TextView tvGPSError;

    public GPSManager(Activity activity, Context context, TextView tvGPSError) {
        this.activity = activity;
        this.context = context;
        this.tvGPSError = tvGPSError;
    }

    public static boolean isGPSEnabled(Context context) {
        final LocationManager manager = (LocationManager) context.getSystemService( Context.LOCATION_SERVICE );

        if ( !manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
            return false;

        }else {
            return  true;
        }
    }

    public BroadcastReceiver gpsBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            final LocationManager manager = (LocationManager) context.getSystemService( Context.LOCATION_SERVICE );
            if (manager.isProviderEnabled(LocationManager.GPS_PROVIDER) ) {
                if (tvGPSError != null) {
                    tvGPSError.setVisibility(View.GONE);
                }
            } else {
                if (tvGPSError != null) {
                    tvGPSError.setVisibility(View.VISIBLE);
                }else{
                    Alert.info(context, "Viga", "Palun lüitage GPS sisse");
                }
            }
        }
    };

    public void handleGPSConnectionAlert(){
        if(!isGPSEnabled(context)){
            Alert.turnOnGPSAlert(activity, context);
        }else{
            Toast.makeText(context, "Gps lisati", Toast.LENGTH_SHORT).show();
        }
    }



}
