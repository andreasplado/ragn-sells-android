package services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;

import data.TrashcanLocation;
import utils.carrier.TrashcanLocationManager;

/**
 * Created by Andreas on 15.05.2017.
 */

public class ProcessingService extends Service {

    private Timer timer = new Timer();


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                TrashcanLocationManager tlm = TrashcanLocation.getTrashcanLocationManager();
                if(tlm == null){
                    tlm = TrashcanLocation.getTrashcanLocationManager();
                }
                if(tlm != null) {
                    tlm.getAllTrashcansOnMap(true);
                    Log.e("Tag", "Uuendati mappi");
                }
            }
        }, 0, 900000);//900 Seconds = 15 minutes
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //shutdownService();

    }

}
