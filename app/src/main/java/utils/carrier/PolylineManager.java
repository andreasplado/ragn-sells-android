package utils.carrier;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.location.Location;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import data.UserData;
import utils.UserLocationManager;
import utils.carrier.GetGoogleMapDirectionsAsync;
import utils.carrier.GoogleMapDirectionsQuery;

/**
 * Created by Andreas on 29.04.2017.
 */

public class PolylineManager {

    private Context context;
    private Activity activity;
    private GoogleMap googleMap;
    private Location currentLocation;
    private PolylineOptions rectLine;
    public Polyline polyline;
    private UserLocationManager userLocationManager;
    List<Polyline> polylines = new ArrayList<Polyline>();

    public PolylineManager(Activity activity,
                           Context context,
                           GoogleMap googleMap,
                           UserLocationManager userLocationManager,
                           Location currentLocation){
        this.activity = activity;
        this.context = context;
        this.googleMap = googleMap;
        this.userLocationManager = userLocationManager;
        this.currentLocation = currentLocation;

    }


    public void findDirection() {

        Location endLocation = new Location("");
        endLocation.setLongitude(24.7536);
        endLocation.setLatitude(59.43457);
        //TODO:
        directionsQuery(UserData.getUserLocation().getLatitude(),
                UserData.getUserLocation().getLongitude(),
                endLocation.getLatitude(), endLocation.getLongitude(), GoogleMapDirectionsQuery.MODE_DRIVING );
    }


    public void handleGetDirectionsResult(ArrayList<LatLng> directionPoints)
    {

        if(polyline != null){
            polyline.remove();
        }
        rectLine = new PolylineOptions().width(7).color(Color.RED);

        for(int i = 0 ; i < directionPoints.size() ; i++)
        {
            rectLine.add(directionPoints.get(i));

        }
        polyline = googleMap.addPolyline(rectLine);
    }




    private void directionsQuery(final double fromPositionDoubleLat, final double fromPositionDoubleLong, final double toPositionDoubleLat, final double toPositionDoubleLong, final String mode)
    {
        final GetGoogleMapDirectionsAsync asyncTask = new GetGoogleMapDirectionsAsync(activity, this);
        new Thread(new Runnable() {
            public void run() {
                asyncTask.execute();
            }
        }).start();

    }

    public void removePolyLine(){
        polyline.remove();
    }

    //TODO: This might work!
    public void removeSpecificPolyline(LatLng latLng){
        for(int i=0; i< rectLine.getPoints().size(); i++){
            if(rectLine.getPoints().get(i).longitude == latLng.longitude && rectLine.getPoints().get(i).latitude == latLng.latitude){
                rectLine.getPoints().remove(i);
            }
        }
    }

}
