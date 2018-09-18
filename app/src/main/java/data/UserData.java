package data;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

import model.User;
import model.auth.AuthResponse;

/**
 * Created by Andreas on 02.05.2017.
 */

public class UserData {

    private static Location userLocation;
    private static AuthResponse authResponse;
    private static ResponseData responseData;
    private static User user;
    private static boolean isOnline;
    private static boolean isGPSOn;

    public static Location getUserLocation() {
        return userLocation;
    }

    public static void setUserLocation(Location userLocation) {
        UserData.userLocation = userLocation;
    }

    public static AuthResponse getAuthResponse() {
        return authResponse;
    }

    public static void setAuthResponse(AuthResponse authResponse) {
        UserData.authResponse = authResponse;
    }

    public static ResponseData getResponseData() {
        return responseData;
    }

    public static void setResponseData(ResponseData responseData) {
        UserData.responseData = responseData;
    }

    public static User getUser() {
        return user;
    }

    public static void setUser(User user) {
        UserData.user = user;
    }

    public static boolean isOnline() {
        return isOnline;
    }

    public static void setIsOnline(boolean isOnline) {
        UserData.isOnline = isOnline;
    }
}
