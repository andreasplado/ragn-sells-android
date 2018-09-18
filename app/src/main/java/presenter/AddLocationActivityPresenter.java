package presenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.util.Log;
import android.widget.Toast;

//import com.google.android.gms.location.places.AutocompleteFilter;
//import com.google.android.gms.location.places.Place;
//import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
//import com.google.android.gms.location.places.ui.PlaceSelectionListener;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.model.LatLng;

import data.TrashcanLocation;
import utils.carrier.TrashcanManager;
import www.ragnsells.ee.ragnsells.AddLocationSelectedActivity;
import www.ragnsells.ee.ragnsells.R;

/**
 * Created by Andreas on 09.05.2017.
 */

public class AddLocationActivityPresenter implements Presenter {

    private Activity activity;
    private Context context;

    private PlaceAutocompleteFragment autocompleteFragment;
    private TrashcanManager trashcanManager;


    public AddLocationActivityPresenter(Activity activity, Context context){
        this.activity = activity;
        this.context = context;
    }
    @Override
    public void init() {
        requestPermissions();
        mapUiItems();
        addData();
        addClicks();
        addListeners();
        registerReceivers();
    }

    @Override
    public void initFileds() {

    }

    @Override
    public void addListeners() {
        AutocompleteFilter typeFilter = new AutocompleteFilter.Builder()
                .setCountry("EE")
                .build();

        autocompleteFragment.setFilter(typeFilter);
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                Log.i("Lol", "Place: " + place.getName());
                LatLng latLng = place.getLatLng();
                Location containerLocation = new Location("");
                containerLocation.setLatitude(latLng.latitude);
                containerLocation.setLongitude(latLng.longitude);
                TrashcanLocation.setLocation(containerLocation);
                TrashcanLocation.setAddress(place.getName());
                Intent i = new Intent(context, AddLocationSelectedActivity.class);
                activity.startActivity(i);
                activity.finish();

            }

            @Override
            public void onError(Status status) {
                Toast.makeText(context, "Viga: "+status, Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void addClicks() {

    }

    @Override
    public void mapUiItems() {
        autocompleteFragment = (PlaceAutocompleteFragment)
                activity.getFragmentManager().findFragmentById(R.id.fragment_place_autocomplete);
    }

    @Override
    public void addData() {

    }

    @Override
    public void requestPermissions() {

    }

    @Override
    public void registerReceivers() {

    }
}
