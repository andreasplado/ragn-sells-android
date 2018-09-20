package utils.carrier;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import data.TrashcanLocation;
import data.ResponseData;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import utils.Alert;
import utils.dataconverter.PlaceConverter;
import utils.loading.LoadingManager;
import utils.login.LoginManager;
import utils.retrofit.Constants;
import utils.retrofit.RetrofitBuilder;
import utils.retrofit.TrashcansEndpoint;

/**
 * Created by Andreas on 09.05.2017.
 */

public class TrashcanManager {

    private Activity activity;
    private Context context;
    private static RetrofitBuilder retrofitBuilder = new RetrofitBuilder(Constants.URL_BASE);

    public TrashcanManager(Activity activity, Context context){
        this.activity = activity;
        this.context = context;
    }

    public void getContainerLocation() {
        LocationManager locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);

        // Define a listener that responds to location updates
        LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                // Called when a new location is found by the network location provider.
                TrashcanLocation.setLocation(location);
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            public void onProviderEnabled(String provider) {
            }

            public void onProviderDisabled(String provider) {
            }
        };

        // Register the listener with the Location Manager to receive location updates
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        }else{
            Toast.makeText(context, "Asukohta ei suudetud leida", Toast.LENGTH_SHORT).show();
        }
    }

    public void registerContainer(String issue){

        if(TrashcanLocation.getLocation() != null) {
            double longitude = TrashcanLocation.getLocation().getLongitude();
            double latitude = TrashcanLocation.getLocation().getLatitude();
            String placeName = PlaceConverter.getTrashcanAddress(context);
            String token = LoginManager.retrieveToken(context);
            int userId = LoadingManager.retrieveUserId(context);
            TrashcansEndpoint apiService = RetrofitBuilder.getClient().create(TrashcansEndpoint.class);
            retrofitBuilder.build();

            Call<ResponseData> call = apiService.postTrashcan(longitude, latitude, issue, userId, placeName, token);
            call.enqueue(new Callback<ResponseData>() {
                @Override
                public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                    ResponseData responseData= response.body();
                    Log.e("TAG", response.body().toString());
                    if (response.body() != null) {
                        Toast.makeText(context, "Te tellisite konteineri asukoha", Toast.LENGTH_LONG).show();
                    } else {
                        Alert.info(context, "Viga", "Teie sessioon on aegunud\nPalun logige uuesti sisse!");
                    }

                }

                @Override
                public void onFailure(Call<ResponseData> call, Throwable t) {
                    //Alert.loginFailedAlert(activity, context);
                    //Log.e("Failure", t.getMessage());
                }
            });
        }else {
            Toast.makeText(context, "Teie asukoht pole veel saadaval", Toast.LENGTH_SHORT).show();
        }
    }
}
