package utils.dataconverter;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import data.TrashcanLocation;
import model.Trashcan;

/**
 * Created by Andreas on 18.05.2017.
 */

public class PlaceConverter {

    public static void setTrashcanAddress(Context context, TextView txtAddress, Trashcan trashcan) {
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(context, Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(trashcan.getLatitude(), trashcan.getLongitude(), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            String city = addresses.get(0).getLocality();
            String state = addresses.get(0).getAdminArea();
            String country = addresses.get(0).getCountryName();
            String postalCode = addresses.get(0).getPostalCode();
            String knownName = addresses.get(0).getFeatureName(); // Only if available else return NULL
            txtAddress.setText(address);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getTrashcanAddress(Context context) {
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(context, Locale.getDefault());
        String address = "";
        if(TrashcanLocation.getLocation() != null) {
            try {
                addresses = geocoder.getFromLocation(TrashcanLocation.getLocation().getLatitude(), TrashcanLocation.getLocation().getLongitude(), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                String city = addresses.get(0).getLocality();
                String state = addresses.get(0).getAdminArea();
                String country = addresses.get(0).getCountryName();
                String postalCode = addresses.get(0).getPostalCode();
                String knownName = addresses.get(0).getFeatureName(); // Only if available else return NULL
                return address;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return address;
    }
}
