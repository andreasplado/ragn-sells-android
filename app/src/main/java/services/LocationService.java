package services;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;

/**
 * Created by Andreas on 21.05.2017.
 */

public class LocationService extends Service implements LocationListener {

    private LocationManager locationManager;
    private Location location;

    @Override
    public int onStartCommand(final Intent intent, final int flags, final int startId) {


        if (intent.getAction().equals("startListening")) {
            locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
            }
        }
        else {
            if (intent.getAction().equals("stopListening")) {
                locationManager.removeUpdates(this);
                locationManager = null;
            }
        }

        return START_STICKY;

    }

    @Override
    public IBinder onBind(final Intent intent) {
        return null;
    }

    public void onLocationChanged(final Location location) {
        this.location = location;
        // TODO this is where you'd do something like context.sendBroadcast()
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

}
