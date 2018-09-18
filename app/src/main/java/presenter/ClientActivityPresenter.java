package presenter;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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

import mehdi.sakout.fancybuttons.FancyButton;
import utils.GPSManager;
import utils.MobileDataManager;
import utils.Receivers;
import utils.carrier.TrashcanManager;
import utils.client.MyBounceInterpolator;
import utils.dataconverter.PlaceConverter;
import utils.login.LoginManager;
import www.ragnsells.ee.ragnsells.ClientActivityLog;
import www.ragnsells.ee.ragnsells.MyUserActivity;
import www.ragnsells.ee.ragnsells.R;

/**
 * Created by Andreas on 02.05.2017.
 */

public class ClientActivityPresenter implements Presenter {

    private Activity activity;
    private Context context;

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private FancyButton btnRegisterTrashcan;
    private Button btnShowClientAtivity;
    private Button mainMenuBtn;
    private TextView tvNetworkError;
    private TextView tvGpsError;

    private boolean isNavDrawerOpened;

    private TrashcanManager trashcanManager;
    private MobileDataManager mobileDataManager;
    private GPSManager gpsManager;




    public ClientActivityPresenter(Activity activity, Context context){
        this.activity = activity;
        this.context = context;
        trashcanManager = new TrashcanManager(activity, context);
    }

    @Override
    public void init(){
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

    public void addListeners() {
        drawerLayout.addDrawerListener(mdrawerListener);
        trashcanManager.getContainerLocation();
    }

    public void addClicks() {
        addButtonClicks();
        addNavigationViewClicks();
    }

    private void addButtonClicks(){
        mainMenuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isNavDrawerOpened){
                    isNavDrawerOpened = false;
                    drawerLayout.closeDrawer(Gravity.LEFT);
                }else{
                    isNavDrawerOpened = true;
                    drawerLayout.openDrawer(Gravity.LEFT);
                }
            }
        });
        btnRegisterTrashcan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation myAnim = AnimationUtils.loadAnimation(activity, R.anim.bounce);
                MyBounceInterpolator interpolator = new MyBounceInterpolator(0.2, 20);
                myAnim.setInterpolator(interpolator);

                btnRegisterTrashcan.startAnimation(myAnim);
                trashcanManager.registerContainer("");
            }
        });

        btnShowClientAtivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, ClientActivityLog.class);
                activity.startActivity(i);

            }
        });

    }

    private DrawerLayout.DrawerListener mdrawerListener = new DrawerLayout.DrawerListener() {
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
    };

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
                    case R.id.register_container_my_account:
                        Intent i = new Intent(context, MyUserActivity.class);
                        activity.startActivity(i);
                        break;
                    case R.id.register_container_log_out:
                        LoginManager loginManager = new LoginManager(activity, context);
                        loginManager.destroyToken();
                        break;
                }
                return false;
            }

        });
    }

    public void mapUiItems() {
        drawerLayout = (DrawerLayout)activity.findViewById(R.id.dl_side_menu);
        mainMenuBtn = (Button)activity.findViewById(R.id.main_menu);
        navigationView = (NavigationView)activity.findViewById(R.id.navigation_view);
        btnRegisterTrashcan = (FancyButton)activity.findViewById(R.id.btn_register_trashcan);
        btnShowClientAtivity = (Button)activity.findViewById(R.id.btn_show_client_activity);
        tvNetworkError = (TextView)activity.findViewById(R.id.networ_error);
        tvGpsError = (TextView)activity.findViewById(R.id.gps_error);
    }

    @Override
    public void addData() {
        if(!GPSManager.isGPSEnabled(context)){
            tvGpsError.setVisibility(View.VISIBLE);
        }
    }

    public void requestPermissions() {
        ActivityCompat.requestPermissions(activity,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                1);
    }

    public void registerReceivers() {
        this.mobileDataManager = new MobileDataManager(activity, context, tvNetworkError);
        Receivers.registerMobileDataReciver(context, mobileDataManager);
        this.gpsManager = new GPSManager(activity, context, tvGpsError);
        Receivers.registerGPSReciver(context, gpsManager);
    }

    public void unregisterRecievers() {
        context.unregisterReceiver(mobileDataManager.mobileDataBroadcastReceiver);
        context.unregisterReceiver(gpsManager.gpsBroadcastReceiver);
    }
}
