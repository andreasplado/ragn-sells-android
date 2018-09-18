package utils.carrier;

import android.app.Activity;
import android.os.AsyncTask;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import org.w3c.dom.Document;

import java.util.ArrayList;

/**
 * Created by Andreas on 28.04.2017.
 */

public class GetGoogleMapDirectionsAsync extends AsyncTask<ArrayList<LatLng>, Object, ArrayList<LatLng>> {

    private Activity activity;
    private Exception exception;
    private PolylineManager polylineManager;

    public GetGoogleMapDirectionsAsync(Activity activity, PolylineManager polylineManager)
    {
        super();
        this.activity = activity;
        this.polylineManager = polylineManager;
    }



    public void onPreExecute() {

    }

    @Override
    public void onPostExecute(ArrayList<LatLng> result) {
        if (exception == null) {
            polylineManager.handleGetDirectionsResult(result);
        } else {
            processException();
        }
    }

    @Override
    protected ArrayList<LatLng> doInBackground(ArrayList<LatLng> ... userCurrent) {
        try{
            GoogleMapDirectionsQuery googleMapDirectionsQuery = new GoogleMapDirectionsQuery();
            Document doc = googleMapDirectionsQuery.getDocument();
            ArrayList<LatLng> directionPoints = googleMapDirectionsQuery.getDirection(doc);
            return directionPoints;

        }
        catch (Exception e) {
            exception = e;
            return null;
        }
    }

    private void processException() {
        Toast.makeText(activity, "Teejuhiseid ei leitud!\nHetkel pole konteinereid, või on registreeritud konteineri asukoht ebaselge", Toast.LENGTH_LONG).show();
    }

}
