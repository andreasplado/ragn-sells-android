package utils;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

/**
 * Created by Andreas on 30.04.2017.
 */

public class MobileDataManager {

    private Activity activity;
    private Context context;
    private TextView tvNetworkError;

    public MobileDataManager(Activity activity, Context context, TextView tvNetworkError){
        this.activity = activity;
        this.context = context;
        this.tvNetworkError = tvNetworkError;
    }

    public static boolean isInternetConnected (Context context) {
        ConnectivityManager cm =
                (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

        assert cm != null;
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
    }

    public BroadcastReceiver mobileDataBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if(connectivity != null) {
                NetworkInfo activeNetwork = connectivity.getActiveNetworkInfo();
                if(activeNetwork != null) {
                    if (tvNetworkError != null) {
                        tvNetworkError.setVisibility(View.GONE);
                    }
                }else{
                    if(tvNetworkError != null) {
                        tvNetworkError.setVisibility(View.VISIBLE);
                    }else{
                        Alert.info(context, "Viga", "Palun lülitage oma andmeside sisse");
                    }
                }
            }

            handleMobileDataConnectionAlert();
            /*
            if (intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
                handleMobileDataConnectionAlert();
            }*/
        }
    };

    public void handleMobileDataConnectionAlert() {
        if(!isInternetConnected(context)){
            //Alert.turnOnDataConnectionAlert(activity, context);
        }else{
            if(Alert.getCannotGetContainersAlert() != null){
                Alert.getCannotGetContainersAlert().show().dismiss();
            }
        }
    }
}
