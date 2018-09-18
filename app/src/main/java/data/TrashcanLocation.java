package data;

import android.location.Location;

import utils.carrier.TrashcanLocationManager;

/**
 * Created by Andreas on 09.05.2017.
 */

public class TrashcanLocation {

    private static TrashcanLocationManager trashcanLocationManager;

    private static Location location;

    private static CharSequence address;

    public static Location getLocation() {
        return location;
    }

    public static void setLocation(Location location) {
        TrashcanLocation.location = location;
    }

    public static CharSequence getAddress() {
        return address;
    }

    public static void setAddress(CharSequence address) {
        TrashcanLocation.address = address;
    }

    public static TrashcanLocationManager getTrashcanLocationManager() {
        return trashcanLocationManager;
    }

    public static void setTrashcanLocationManager(TrashcanLocationManager trashcanLocationManager) {
        TrashcanLocation.trashcanLocationManager = trashcanLocationManager;
    }
}
