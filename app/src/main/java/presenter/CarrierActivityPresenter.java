package presenter;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.github.clans.fab.FloatingActionButton;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.MapStyleOptions;

import data.TrashcanLocation;
import utils.GPSManager;
import utils.Receivers;
import utils.carrier.TrashcanManager;
import utils.dataconverter.DateConverter;
import utils.login.LoginManager;
import utils.MobileDataManager;
import utils.carrier.TrashcanLocationManager;
import utils.UserLocationManager;
import www.ragnsells.ee.ragnsells.MyUserActivity;
import www.ragnsells.ee.ragnsells.R;

/**
 * Created by Andreas on 04.04.2017.
 */

public class CarrierActivityPresenter implements Presenter {

    private Activity activity;
    private Context context;

    private Button mainMenuBtn;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private FloatingActionButton fABStartTravelingMode,fABExitTravelingMode;
    private GoogleMap googleMap;

    private GPSManager gpsManager;
    private MobileDataManager mobileDataManager;
    private UserLocationManager userLocationManager;

    private boolean isNavDrawerOpened = false;
    private Button refreshBtn;
    public TextView tvLastRefreshTime;
    private TextView tvNetworkError;
    private TextView tvGpsError;


    public CarrierActivityPresenter(Activity activity, Context context, GoogleMap googleMap){
        this.activity = activity;
        this.context = context;
        this.googleMap = googleMap;
    }
    @Override
    public void init() {
        initFileds();
        requestPermissions();
        mapUiItems();
        addData();
        addClicks();
        addListeners();
        registerReceivers();
    }

    @Override
    public void initFileds() {
        this.userLocationManager = new UserLocationManager(activity, context, googleMap);

    }

    @Override
    public void registerReceivers(){
        this.mobileDataManager = new MobileDataManager(activity, context, tvNetworkError);
        Receivers.registerMobileDataReciver(context, mobileDataManager);
        this.gpsManager = new GPSManager(activity, context, tvGpsError);
        Receivers.registerGPSReciver(context, gpsManager);
    }

    /**
     * Adds the listeners to ui items.
     */
    @Override
    public void addListeners() {
        drawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(View drawerView) {
                Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(context, R.anim.toggle_menu_on);
                mainMenuBtn.startAnimation(hyperspaceJumpAnimation);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(context, R.anim.toggle_menu_off);
                mainMenuBtn.startAnimation(hyperspaceJumpAnimation);
            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });
    }

    /**
     * Maps the user interface.
     */
    @Override
    public void mapUiItems() {
        mainMenuBtn = (Button)activity.findViewById(R.id.main_menu);
        drawerLayout =(DrawerLayout) activity.findViewById(R.id.dl_side_menu);
        navigationView = (NavigationView)activity.findViewById(R.id.navigation_view);
        fABStartTravelingMode = (FloatingActionButton)activity.findViewById(R.id.fab_start_travelling);
        fABExitTravelingMode = (FloatingActionButton)activity.findViewById(R.id.fab_exit_travelling_mode);
        refreshBtn = (Button)activity.findViewById(R.id.btn_refresh_trashcans);
        tvLastRefreshTime = (TextView)activity.findViewById(R.id.tv_last_refresh_time_seconds);
        tvNetworkError = (TextView)activity.findViewById(R.id.networ_error);
        tvGpsError = (TextView)activity.findViewById(R.id.gps_error);
    }

    @Override
    public void addData() {

    }


    /**
     * Add clicks to navigation view.
     */
    private void addNavigationViewClicks() {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (isNavDrawerOpened) {
                    isNavDrawerOpened = false;
                    drawerLayout.closeDrawer(Gravity.LEFT);
                } else {
                    isNavDrawerOpened = true;
                    drawerLayout.openDrawer(Gravity.LEFT);
                }
                switch (item.getItemId()) {
                    case R.id.map_my_account:
                        Intent i = new Intent(context, MyUserActivity.class);
                        activity.startActivity(i);
                        break;
                    case R.id.map_log_out:
                        LoginManager loginManager = new LoginManager(activity, context);
                        loginManager.destroyToken();
                        break;
                }
                return false;
            }

        });
    }


    private void addButtonClicks() {
        new Thread(new Runnable() {
            public void run() {
                mainMenuBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (isNavDrawerOpened) {
                            isNavDrawerOpened = false;
                            drawerLayout.closeDrawer(Gravity.LEFT);
                        } else {
                            isNavDrawerOpened = true;
                            drawerLayout.openDrawer(Gravity.LEFT);
                        }

                    }
                });
                fABStartTravelingMode.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (googleMap != null) {
                            new Thread(new Runnable() {
                                public void run() {
                                    userLocationManager.enterRideMode(fABStartTravelingMode, fABExitTravelingMode);
                                }
                            }).run();

                        } else {
                            Toast.makeText(context, "Kahjuks pole veel map suutnud ära laadida", Toast.LENGTH_LONG).show();
                        }
                    }
                });
                fABExitTravelingMode.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (googleMap != null) {
                            new Thread(new Runnable() {
                                public void run() {
                                    userLocationManager.exitRideMode(fABExitTravelingMode, fABStartTravelingMode);
                                }
                            }).run();

                        } else {
                            Toast.makeText(context, "Kahjuks pole veel map suutnud ära laadida", Toast.LENGTH_LONG).show();
                        }
                    }
                });
                refreshBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(context, R.anim.toggle_back);
                        refreshBtn.startAnimation(hyperspaceJumpAnimation);
                        TrashcanLocationManager tlm = TrashcanLocation.getTrashcanLocationManager();
                        if(tlm != null){
                            Toast.makeText(context, "Konteinerite info värksendatud", Toast.LENGTH_SHORT).show();
                            tlm.getAllTrashcansOnMap(true);
                            postRequestTime();
                        }else{
                            Toast.makeText(context, "Palun oodake veidi, kuni konteinerite infot uuendatakse", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }).start();
    }

    private void postRequestTime() {
        String currentDateTimeString = DateConverter.convertCurrentTimeToReadableFormat();
        tvLastRefreshTime.setText("Viimati värskendatud:" + currentDateTimeString);
    }

    /**
     * Adds clicks to UI items.
     */
    @Override
    public void addClicks(){
        addNavigationViewClicks();
        addButtonClicks();
    }
    /**
     * Asks user to turn on GPS and network.
     */
    public void askUserToTurnOnGPS() {
        gpsManager.handleGPSConnectionAlert();
    }

    public void askUserToTurnOnMobileData() {
        mobileDataManager.handleMobileDataConnectionAlert();
    }


    /**
     * Gets the user current location and starts
     * requesting a location updates of a user.
     * @param googleMap
     */
    public void setUserOnMap(GoogleMap googleMap){
        userLocationManager = new UserLocationManager(activity, context, googleMap);
        userLocationManager.init();
    }



    /**
     * Request for permissions(location permission, wifi permission and so on)
     */
    @Override
    public void requestPermissions(){
        ActivityCompat.requestPermissions(activity,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                1);
    }


    public void setGoogleMapstyle(GoogleMap googleMap){
        googleMap.getUiSettings().setMapToolbarEnabled(false);
        googleMap.getUiSettings().setMyLocationButtonEnabled(false);
        googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(context,
                R.raw.green_map_style));
        this.googleMap = googleMap;
    }


    public void unregisterRecievers() {
        context.unregisterReceiver(mobileDataManager.mobileDataBroadcastReceiver);
        context.unregisterReceiver(gpsManager.gpsBroadcastReceiver);
    }

    public void setTrashcansOnMap() {
        TrashcanLocationManager tlm = new TrashcanLocationManager(activity, context, googleMap);
        TrashcanLocation.setTrashcanLocationManager(tlm);
        tlm.getAllTrashcansOnMap(false);
        tlm.setMapBounds();
    }
}
