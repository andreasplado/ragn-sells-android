package presenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import utils.GPSManager;
import utils.MobileDataManager;
import utils.Receivers;
import utils.client.ClientManager;
import www.ragnsells.ee.ragnsells.LoadingActivity;
import www.ragnsells.ee.ragnsells.R;

/**
 * Created by Andreas on 18.05.2017.
 */

public class ClientActivityLogPresenter implements Presenter{

    private Activity activity;
    private Context context;
    private Button btnBack;
    private ListView clientLogListView;
    private ClientManager clientManager;
    private SwipeRefreshLayout swipeRefreshLayout;
    private MobileDataManager mobileDataManager;
    private GPSManager gpsManager;
    private TextView tvNetworkError;
    private TextView tvGpsError;

    public ClientActivityLogPresenter(Activity activity, Context context){
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

    }

    @Override
    public void addClicks() {
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(context, R.anim.toggle_back);
                btnBack.startAnimation(hyperspaceJumpAnimation);
                Intent i = new Intent(context, LoadingActivity.class);
                activity.startActivity(i);
            }
        });
    }

    @Override
    public void mapUiItems() {
        btnBack = (Button)activity.findViewById(R.id.back_to_user_view);
        clientLogListView = (ListView)activity.findViewById(R.id.lv_client_log);
        clientLogListView.setDivider(null);
        swipeRefreshLayout = (SwipeRefreshLayout) activity.findViewById(R.id.client_log_swipe_refresh_layout);
        mapClientLogListView(clientLogListView);
        tvNetworkError = (TextView) activity.findViewById(R.id.networ_error);
        tvGpsError = (TextView)activity.findViewById(R.id.gps_error);
    }

    private void mapClientLogListView(final ListView clientLogListView) {
        clientManager = new ClientManager(activity, context);
        clientManager.getAllClientTrashcans(clientLogListView, swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                clientManager.getAllClientTrashcans(clientLogListView, swipeRefreshLayout);
            }
        });

    }

    @Override
    public void addData() {

    }

    @Override
    public void requestPermissions() {

    }

    @Override
    public void registerReceivers() {
        this.mobileDataManager = new MobileDataManager(activity, context, tvNetworkError);
        Receivers.registerMobileDataReciver(context, mobileDataManager);
        this.gpsManager = new GPSManager(activity, context, tvGpsError);
        Receivers.registerGPSReciver(context, gpsManager);
    }
}
