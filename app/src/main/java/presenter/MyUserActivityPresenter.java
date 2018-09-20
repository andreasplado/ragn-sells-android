package presenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

import mehdi.sakout.fancybuttons.FancyButton;
import utils.GPSManager;
import utils.MobileDataManager;
import utils.Receivers;
import utils.myuser.EditUserAlerts;
import utils.myuser.EditUserManager;
import www.ragnsells.ee.ragnsells.LoadingActivity;
import www.ragnsells.ee.ragnsells.R;

/**
 * Created by Andreas on 09.05.2017.
 */

public class MyUserActivityPresenter implements Presenter {

    private Activity activity;
    private Context context;
    private Button btnBack;
    private FancyButton btnName, btnPhoneNr, btnPassword, btnRole, btnDeleteAccount;
    private TextView tvName, tvPhone, tvRole, tvUserCreated, tvUserUpdated, tvEmail;
    private EditUserManager editUserManager;
    private TextView tvNetworkError;
    private MobileDataManager mobileDataManager;
    private TextView tvGPSError;
    private GPSManager gpsManager;

    public MyUserActivityPresenter(Activity activity, Context context){
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
        btnName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditUserAlerts.editName(activity, context, editUserManager, tvName);
            }
        });
        btnPhoneNr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditUserAlerts.editPhoneNr(activity, context, editUserManager, tvPhone);
            }
        });
        btnPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditUserAlerts.editPassword(activity, context, editUserManager);
            }
        });
        btnRole.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditUserAlerts.editRole(activity, context, editUserManager, tvRole);
            }
        });
        btnDeleteAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditUserAlerts.deleteUser(activity, context, editUserManager);
            }
        });
    }

    @Override
    public void mapUiItems() {
        btnBack = (Button)activity.findViewById(R.id.back_to_user_view);
        btnName = (FancyButton)activity.findViewById(R.id.btn_name);
        btnPhoneNr = (FancyButton)activity.findViewById(R.id.btn_phone_nr);
        btnPassword = (FancyButton)activity.findViewById(R.id.btn_password);
        btnRole = (FancyButton)activity.findViewById(R.id.btn_role);
        btnDeleteAccount = (FancyButton)activity.findViewById(R.id.btn_delete_account);
        tvEmail = (TextView)activity.findViewById(R.id.tv_email);
        tvName = (TextView)activity.findViewById(R.id.tv_name);
        tvPhone = (TextView)activity.findViewById(R.id.tv_phone);
        tvRole = (TextView)activity.findViewById(R.id.tv_role);
        tvUserCreated = (TextView)activity.findViewById(R.id.tv_user_created);
        tvUserUpdated = (TextView)activity.findViewById(R.id.tv_user_update_date);
        tvNetworkError = (TextView)activity.findViewById(R.id.networ_error);
        tvGPSError = (TextView)activity.findViewById(R.id.gps_error);
    }

    public void addData() {
        editUserManager = new EditUserManager(activity, context);
        editUserManager.setUserInfo(tvEmail, tvName, tvPhone, tvRole, tvUserCreated, tvUserUpdated);
    }

    @Override
    public void requestPermissions() {

    }

    @Override
    public void registerReceivers() {
        this.mobileDataManager = new MobileDataManager(activity, context, tvNetworkError);
        Receivers.registerMobileDataReciver(context, mobileDataManager);
    }
}
