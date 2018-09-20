package presenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.github.clans.fab.FloatingActionButton;

import data.UserData;
import utils.Receivers;
import utils.manager.ManagerManager;
import utils.login.LoginManager;
import utils.MobileDataManager;
import www.ragnsells.ee.ragnsells.AddLocationActivity;
import www.ragnsells.ee.ragnsells.MyUserActivity;
import www.ragnsells.ee.ragnsells.R;

/**
 * Created by Andreas on 09.05.2017.
 */

public class ManagerActivityPresenter implements Presenter {

    private Context context;
    private Activity activity;
    private DrawerLayout drawerLayout;
    private Button mainMenuBtn;
    private NavigationView navigationView;
    private boolean isNavDrawerOpened;
    private FloatingActionButton floatingActionButton;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ListView managerListView;
    private ManagerManager managerManager;
    private TextView tvNetworkError;
    private MobileDataManager mobileDataManager;

    public ManagerActivityPresenter(Activity activity, Context context){
        this.activity = activity;
        this.context = context;
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

    }

    @Override
    public void addListeners() {
        drawerLayout.addDrawerListener(mdrawerListener);
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

    @Override
    public void addClicks() {
        addButtonClicks();
        addNavigationViewClicks();
    }

    private void addButtonClicks() {
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
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, AddLocationActivity.class);
                activity.startActivity(i);
            }
        });
    }

    @Override
    public void mapUiItems() {
        drawerLayout = (DrawerLayout)activity.findViewById(R.id.dl_side_menu);
        mainMenuBtn = (Button)activity.findViewById(R.id.main_menu);
        navigationView = (NavigationView)activity.findViewById(R.id.navigation_view);
        floatingActionButton = (FloatingActionButton)activity.findViewById(R.id.add_location);
        swipeRefreshLayout = (SwipeRefreshLayout) activity.findViewById(R.id.client_log_swipe_refresh_layout);
        managerListView = (ListView)activity.findViewById(R.id.lv_client_log);
        mapManagerListView(managerListView);
        tvNetworkError = (TextView)activity.findViewById(R.id.networ_error);
    }

    @Override
    public void addData() {
        if(UserData.isOnline()){
            tvNetworkError.setVisibility(View.GONE);
        }else {
            tvNetworkError.setVisibility(View.VISIBLE);

        }
    }

    private void mapManagerListView(final ListView managerListView) {
        managerManager = new ManagerManager(activity, context);
        managerManager.getAllTrashcansWithIssues(managerListView, swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                managerManager.getAllTrashcansWithIssues(managerListView, swipeRefreshLayout);
            }
        });
        managerListView.setDivider(null);

    }

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
                    case R.id.manager_my_account:
                        Intent i = new Intent(context, MyUserActivity.class);
                        activity.startActivity(i);
                        break;
                    case R.id.manager_log_out:
                        LoginManager loginManager = new LoginManager(activity, context);
                        loginManager.destroyToken();
                        break;
                }
                return false;
            }

        });
    }

    @Override
    public void requestPermissions() {

    }

    @Override
    public void registerReceivers() {
        this.mobileDataManager = new MobileDataManager(activity, context, tvNetworkError);
        Receivers.registerMobileDataReciver(context, mobileDataManager);
    }

    public void askUserToTurnOnMobileData() {
        mobileDataManager.handleMobileDataConnectionAlert();
    }
}
