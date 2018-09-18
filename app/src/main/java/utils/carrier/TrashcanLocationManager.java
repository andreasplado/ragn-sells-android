package utils.carrier;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.NotificationCompat;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

import data.ResponseData;
import mehdi.sakout.fancybuttons.FancyButton;
import model.Trashcan;
import model.Trashcans;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import utils.Alert;
import utils.dataconverter.PlaceConverter;
import utils.login.LoginManager;
import utils.retrofit.Constants;
import utils.retrofit.RetrofitBuilder;
import utils.retrofit.TrashcansEndpoint;
import www.ragnsells.ee.ragnsells.R;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Created by Andreas on 15.03.2017.
 */

public class TrashcanLocationManager {

    public Activity activity;
    public Context context;
    public GoogleMap googleMap;
    private ArrayList<Marker> markers = new ArrayList<>();


    private static RetrofitBuilder retrofitBuilder = new RetrofitBuilder(Constants.URL_BASE);


    public TrashcanLocationManager(Activity activity, Context context, GoogleMap googleMap){
        this.activity = activity;
        this.context = context;
        this.googleMap = googleMap;
    }

    /**
     * Gets all trashcans on the map
     * @param notifyUser if the user is needs to get notified about the new trashcans or not.
     */
    public void getAllTrashcansOnMap(final boolean notifyUser){
        new Thread(new Runnable() {
            public void run() {
                TrashcansEndpoint apiService = RetrofitBuilder.getClient().create(TrashcansEndpoint.class);
                retrofitBuilder.build();
                Call<ArrayList<Trashcan>> call = apiService.getAllTrashcans(LoginManager.retrieveToken(context));
                call.enqueue(new Callback<ArrayList<Trashcan>>() {
                    @Override
                    public void onResponse(Call<ArrayList<Trashcan>> call, Response<ArrayList<Trashcan>> response) {
                        if(response.body() != null) {
                            ArrayList<Trashcan> newData = response.body();
                            ArrayList<Trashcan> oldData = Trashcans.getTrashcans();
                            if(oldData != null && newData != null) {
                                removeTrashCans();
                                for (int i = 0; i < newData.size(); i++) {
                                    addTrashcanInfoMareker(newData.get(i).getLongitude(), newData.get(i).getLatitude(), newData.get(i).getId(), newData.get(i).getIssue());
                                }
                                    if (oldData.size() < newData.size() || oldData.size() > newData.size()
                                            || isTrashcanIssueChanged(oldData, newData)) {
                                        if(notifyUser) {
                                            notifyUsers(oldData, newData);
                                        }
                                        Trashcans.setTrashcans(newData);
                                        removeTrashCans();
                                        for (int i = 0; i < newData.size(); i++) {
                                            addTrashcanInfoMareker(newData.get(i).getLongitude(), newData.get(i).getLatitude(), newData.get(i).getId(), newData.get(i).getIssue());
                                        }
                                    }
                            }else{
                                removeTrashCans();
                                for (int i = 0; i < newData.size(); i++) {
                                    addTrashcanInfoMareker(newData.get(i).getLongitude(), newData.get(i).getLatitude(), newData.get(i).getId(), newData.get(i).getIssue());
                                }
                                Trashcans.setTrashcans(newData);
                            }
                        }else{
                            Alert.cannotGetContainersAlert(context, TrashcanLocationManager.this);
                        }

                    }

                    @Override
                    public void onFailure(Call<ArrayList<Trashcan>> call, Throwable t) {
                        Alert.cannotGetContainersAlert(context, TrashcanLocationManager.this);
                    }
                });
            }
        }).start();
    }

    /**
     */
    public void addTrashcanInfoMareker(final double longitude, final double latitude, final int id, final String issue){
        LatLng markerLocation = new LatLng(latitude, longitude);
        //if trashcan has no issue
        if(issue.equals("")){
            Bitmap markerImage = MarkerManager.getBitmapFromVectorDrawable(context, R.drawable.marker_no_issues);
            MarkerOptions trashcanInfo = new MarkerOptions()
                    .icon(BitmapDescriptorFactory.fromBitmap(markerImage))
                    .position(markerLocation);
            Marker marker = googleMap.addMarker(trashcanInfo);
            marker.setTag(id);
            marker.setDraggable(true);
            markers.add(marker);
            addTrashCanInfoMarkerActions();
        }else{
            Bitmap markerImage = MarkerManager.getBitmapFromVectorDrawable(context, R.drawable.marker_issues);
            MarkerOptions trashcanInfo = new MarkerOptions()
                    .icon(BitmapDescriptorFactory.fromBitmap(markerImage))
                    .position(markerLocation);
            Marker marker = googleMap.addMarker(trashcanInfo);
            marker.setTag(id);
            marker.setDraggable(true);
            markers.add(marker);
            addTrashCanInfoMarkerActions();
        }
    }

    /**
     * Adds the trashcan info marker options.
     */
    private void addTrashCanInfoMarkerActions(){
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {

                googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker marker) {
                        Alert alert = new Alert();
                        int markserId = (int) marker.getTag();
                        alert.traschanDetailViewAlert(context, markserId, marker, TrashcanLocationManager.this);
                        return true;
                    }
                });
            }
        });

    }

    public void setMapBounds(){
        final LatLngBounds ESTONIA = new LatLngBounds(
                new LatLng(57.480403, 21.489258), new LatLng(59.778522, 28.608398));


        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {

                googleMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
                    @Override
                    public void onMapLoaded() {
                        googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(ESTONIA, 0));
                    }
                });
            }
        });

    }

    private boolean isTrashcanIssueChanged(ArrayList<Trashcan> oldData, ArrayList<Trashcan> newData){
        boolean isChanged = false;
        if (newData.size() == oldData.size()) {
            for(int i = 0; i< newData.size(); i++) {
                if (oldData.get(i).getIssue().length() > newData.get(i).getIssue().length() ||
                        oldData.get(i).getIssue().length() < newData.get(i).getIssue().length()) {
                    isChanged = true;
                }
            }
        }
        return isChanged;
    }


    private void notifyUsers(ArrayList<Trashcan> oldTrashcans, ArrayList<Trashcan> newTrashcans) {

        Notification n = null;
        //DELLETON OF TRASHCAN
        if(oldTrashcans.size() > newTrashcans.size()) {
            n = new NotificationCompat.Builder(activity)
                    .setContentTitle("Ragnsells")
                    .setContentText("Konteiner kustutati")
                    .setSmallIcon(R.drawable.ic_recycle_bin)
                    .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setAutoCancel(true).build();
        }
        //INSERTION OF TRASHCAN
        if(oldTrashcans.size() < newTrashcans.size()){
             n = new NotificationCompat.Builder(activity)
                     .setContentTitle("Ragnsells")
                     .setContentText("Konteiner lisati")
                     .setSmallIcon(R.drawable.ic_recycle_bin)
                     .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                     .setPriority(NotificationCompat.PRIORITY_HIGH)
                     .setDefaults(Notification.DEFAULT_ALL)
                     .setAutoCancel(true).build();
        }
        //UPDATING THE TRASHCAN ISSUE
        if(oldTrashcans.size() == newTrashcans.size()){

            for(int i= 0; i< newTrashcans.size(); i++){
                if(!oldTrashcans.get(i).getIssue().equals("") && newTrashcans.get(i).getIssue().equals("")){
                    n = new NotificationCompat.Builder(activity)
                            .setContentTitle("Ragnsells")
                            .setContentText("Konteineri probleem lahendati")
                            .setSmallIcon(R.drawable.ic_recycle_bin)
                            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                            .setPriority(NotificationCompat.PRIORITY_HIGH)
                            .setDefaults(Notification.DEFAULT_ALL)
                            .setAutoCancel(true).build();
                    break;
                }
            }
            for(int i= 0; i< newTrashcans.size(); i++){
                if(oldTrashcans.get(i).getIssue().equals("") && !newTrashcans.get(i).getIssue().equals("")){
                    n = new NotificationCompat.Builder(activity)
                            .setContentTitle("Ragnsells")
                            .setContentText("Konteineri äraveoga tekkis probleem")
                            .setSmallIcon(R.drawable.ic_recycle_bin)
                            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                            .setPriority(NotificationCompat.PRIORITY_HIGH)
                            .setDefaults(Notification.DEFAULT_ALL)
                            .setAutoCancel(true).build();
                    break;
                }
            }

        }


        NotificationManager notificationManager =
                (NotificationManager)activity.getSystemService(NOTIFICATION_SERVICE);

        if(n!= null) {
            notificationManager.notify(0, n);
        }
    }

    public void deleteTrashcan(final int id){
        TrashcansEndpoint apiService = RetrofitBuilder.getClient().create(TrashcansEndpoint.class);
        retrofitBuilder.build();
        Call<ResponseData> call = apiService.deleteTrashcan(id, LoginManager.retrieveToken(context));
        call.enqueue(new Callback<ResponseData>() {
            @Override
            public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                if (response.body() != null) {
                    for(int i = 0; i< markers.size(); i++){
                        if(markers.get(i).getTag() != null){
                            if(Integer.parseInt(markers.get(i).getTag().toString()) == id){
                                markers.get(i).remove();
                            }
                        }
                    }
                } else {
                    Alert.cannotDeleteTrashcan(id, TrashcanLocationManager.this);
                }

            }

            @Override
            public void onFailure(Call<ResponseData> call, Throwable t) {
                Alert.cannotGetContainersAlert(context, TrashcanLocationManager.this);
            }
        });
    }

    private void removeTrashCans(){
        for(int i = 0; i< markers.size(); i++){
            markers.get(i).remove();
        }
    }

    public void updateTrashcan(final int id, final String issue, final TextInputEditText etIssue, final FancyButton btnAddIssue, final FancyButton btnDeleteIssue, final Marker marker){
        TrashcansEndpoint apiService = RetrofitBuilder.getClient().create(TrashcansEndpoint.class);
        retrofitBuilder.build();
        Call<ResponseData> call = apiService.updateTrashcan(id, issue, LoginManager.retrieveToken(context));
        call.enqueue(new Callback<ResponseData>() {
            @Override
            public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                if (response.body() != null) {
                    Toast.makeText(context, "Konteineri probleemsed kohad kirjeldatud", Toast.LENGTH_LONG).show();

                    if(issue.equals("")){
                        /*MarkerOptions markerOptions = new MarkerOptions().icon(BitmapDescriptorFactory.fromBitmap(MarkerManager.getBitmapFromVectorDrawable(context, R.drawable.marker_no_issues)));
                        marker.setIcon(markerOptions.getIcon());*/
                        btnDeleteIssue.setVisibility(View.INVISIBLE);
                        etIssue.setText("");
                        marker.setAlpha(0.5f);
                    }else {
                        /*MarkerOptions markerOptions = new MarkerOptions().icon(BitmapDescriptorFactory.fromBitmap(MarkerManager.getBitmapFromVectorDrawable(context, R.drawable.marker_issues)));
                        marker.setIcon(markerOptions.getIcon());*/
                        btnDeleteIssue.setVisibility(View.VISIBLE);
                        btnAddIssue.setText("MUUDA PROBLEEMI");
                        marker.setAlpha(1.0f);
                    }
                } else {
                    Alert.cannotDeleteTrashcan(id, TrashcanLocationManager.this);

                }

            }

            @Override
            public void onFailure(Call<ResponseData> call, Throwable t) {
                Alert.cannotGetContainersAlert(context, TrashcanLocationManager.this);
            }
        });
    }

    public void addTrashcanInfoText(int trascanId, TextInputEditText txtIssue,
                                    TextView tvEmail, TextView tvPlaceAddress){
        ArrayList<Trashcan> trashcans = Trashcans.getTrashcans();
        for(int i= 0; i< trashcans.size(); i++){
            if(trashcans.get(i).getId() == trascanId){
                txtIssue.setText(trashcans.get(i).getIssue());
                tvEmail.setText(trashcans.get(i).getEmail());
                tvPlaceAddress.setText(trashcans.get(i).getPlace_name());
            }
        }
    }


}
