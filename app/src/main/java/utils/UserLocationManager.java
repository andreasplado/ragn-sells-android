package utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.Toast;
import com.github.clans.fab.FloatingActionButton;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MapStyleOptions;

import data.UserData;
import services.LocationService;
import services.ProcessingService;
import utils.carrier.PolylineManager;
import www.ragnsells.ee.ragnsells.R;

import static android.content.Context.LOCATION_SERVICE;

/**
 * Created by Andreas on 24.04.2017.
 */

public class UserLocationManager {


    private Context context;
    private Activity activity;

    private PolylineManager polylineManager;

    private GoogleMap googleMap;


    private static LocationManager locationManager;
    public static Location location;
    private boolean userIsTrackingRoad = false;

    /**
     * Gets user location and handles.
     * @param activity
     * @param context
     * @param googleMap
     */
    public UserLocationManager(Activity activity, Context context, GoogleMap googleMap) {
        this.activity = activity;
        this.context = context;
        this.googleMap = googleMap;

    }

    public void init() {
        initServices();

    }

    private void initServices() {
        locationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);
    }

    public void updateCamera() {
        if ( googleMap == null) return;
        if(UserData.getUserLocation() != null) {
            LatLng userLocation = new LatLng(UserData.getUserLocation().getLatitude(),
                    UserData.getUserLocation().getLongitude());
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(userLocation)
                    .zoom(14.0f)
                    .tilt(40)
                    .build();
            CameraUpdate cameraUpdate = CameraUpdateFactory
                    .newCameraPosition(cameraPosition);
            googleMap.animateCamera(cameraUpdate);
        }else {
            Toast.makeText(context, "Kasutaja asukoht on veel teadmata", Toast.LENGTH_LONG).show();
        }
    }



    /**
     * Moves the camera to main position.
     * @param location Main position {@link Location}
     */
    private void moveCameraToMainPosition(Location location) {
        final LatLngBounds ESTONIA = new LatLngBounds(
                new LatLng(57.480403, 21.489258), new LatLng(59.778522, 28.608398));

        googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(ESTONIA, 0));
    }




    /**
     * Enters the ride mode
     */
    public void enterRideMode(FloatingActionButton fABStartTravelingMode, FloatingActionButton fABExitTravelingMode) {
        getUserLocation();

        //Draw Ppolyline
        polylineManager = new PolylineManager(activity, context, googleMap, UserLocationManager.this, location);

        //Manage UI items
        if (UserData.getUserLocation() != null) {
            //Update damera
            updateCamera();
            fABStartTravelingMode.setVisibility(View.INVISIBLE);
            fABExitTravelingMode.setVisibility(View.VISIBLE);
            setDarkMapStyle();
            userIsTrackingRoad = true;
            polylineManager.findDirection();

        } else{
            fABStartTravelingMode.setVisibility(View.VISIBLE);
            fABExitTravelingMode.setVisibility(View.INVISIBLE);
            Toast.makeText(context, "Asukoht pole veel saadaval", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Exits the ride mode.
     * It is important to know what UI Elements you need to show or hide,
     * for example the start FAB button goes away from the screen
     * And starts showing exit from ride mode fab button.
     *  @param fABExitTravelingMode
     * @param fabStartTravellingMode
     */
    public void exitRideMode(FloatingActionButton fABExitTravelingMode, FloatingActionButton fabStartTravellingMode) {
        if (UserData.getUserLocation() != null) {
            fABExitTravelingMode.setVisibility(View.INVISIBLE);
            fabStartTravellingMode.setVisibility(View.VISIBLE);
            userIsTrackingRoad = false;
            setGreenMapStyle();
            moveCameraToMainPosition(location);
            polylineManager.removePolyLine();


        } else{
            fABExitTravelingMode.setVisibility(View.VISIBLE);
            fabStartTravellingMode.setVisibility(View.INVISIBLE);
            Toast.makeText(context, "Asukoht pole veel saadaval", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Sets the googlemap style to green.
     */
    public void setGreenMapStyle() {
        googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(context,
                R.raw.green_map_style));
    }

    /**
     * Sets the google map style to dark.
     */
    private void setDarkMapStyle() {
        googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(context,
                R.raw.dark_map_style));
    }

    public void getUserLocation(){
        if (ActivityCompat.checkSelfPermission
                (activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    1);
        }else{
            googleMap.setMyLocationEnabled(true);
            // enable location buttons
            googleMap.getUiSettings().setMyLocationButtonEnabled(false);

            // fetch last location if any from provider - GPS.
            final LocationManager locationManager = (LocationManager) activity.getSystemService(LOCATION_SERVICE);
            final Location loc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    UserData.setUserLocation(location);
                    updateCamera();

                }
                @Override
                public void onProviderDisabled(String provider) {
                    // TODO Auto-generated method stub
                }
                @Override
                public void onProviderEnabled(String provider) {
                    // TODO Auto-generated method stub
                }
                @Override
                public void onStatusChanged(String provider, int status,
                                            Bundle extras) {
                    // TODO Auto-generated method stub
                }
            });
            //if last known location is not available
            if (loc == null) {

                final LocationListener locationListener = new LocationListener() {
                    @Override
                    public void onLocationChanged(final Location location) {

                        // getting location of user
                        final double latitude = location.getLatitude();
                        final double longitude = location.getLongitude();
                        //do something with Lat and Lng
                    }

                    @Override
                    public void onStatusChanged(String provider, int status, Bundle extras) {
                    }

                    @Override
                    public void onProviderEnabled(String provider) {
                        //when user enables the GPS setting, this method is triggered.
                    }

                    @Override
                    public void onProviderDisabled(String provider) {
                        //when no provider is available in this case GPS provider, trigger your gpsDialog here.
                    }
                };

                //update location every 10sec in 500m radius with both provider GPS and Network.

                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10*1000, 500, locationListener);
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 10000, 500, locationListener);
                UserData.setUserLocation(loc);
                updateCamera();
            }
            else {
                //do something with last known location.
                // getting location of user
                UserData.setUserLocation(loc);
                updateCamera();

            }
        }
    }

}
