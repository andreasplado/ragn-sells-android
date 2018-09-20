package presenter;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import data.UserInfoData;
import mehdi.sakout.fancybuttons.FancyButton;
import utils.login.LoginManager;
import utils.userinfo.UserInfoManager;
import www.ragnsells.ee.ragnsells.LoadingActivity;
import www.ragnsells.ee.ragnsells.MyUserActivity;
import www.ragnsells.ee.ragnsells.R;

/**
 * Created by Andreas on 18.05.2017.
 */

public class UserInfoActivityPresenter implements Presenter {

    private Activity activity;
    private Context context;
    private UserInfoManager userInfoManager;
    private TextView tvEmail;
    private TextView tvName;
    private TextView tvPhone;
    private TextView tvRole;
    private TextView tvUserUpdateDate;
    private TextView tvUserCreated;
    private Button btnBack;
    private FancyButton btnCallUser;

    public UserInfoActivityPresenter(Activity activity, Context context) {
        this.activity = activity;
        this.context = context;
    }

    @Override
    public void init() {
        addListeners();
        requestPermissions();
        initFileds();
        mapUiItems();
        addClicks();
        addData();
        registerReceivers();
    }

    @Override
    public void initFileds() {
        userInfoManager = new UserInfoManager(activity, context);
    }

    @Override
    public void addListeners() {

    }

    @Override
    public void requestPermissions() {
        ActivityCompat.requestPermissions(activity,
                new String[]{Manifest.permission.CALL_PHONE},
                1);
    }

    @Override
    public void mapUiItems() {
        btnBack = (Button) activity.findViewById(R.id.back_to_user_view);
        tvEmail = (TextView) activity.findViewById(R.id.tv_email);
        tvName = (TextView) activity.findViewById(R.id.tv_name);
        tvPhone = (TextView) activity.findViewById(R.id.tv_phone);
        tvRole = (TextView) activity.findViewById(R.id.tv_role);
        tvUserUpdateDate = (TextView) activity.findViewById(R.id.tv_user_update_date);
        tvUserCreated = (TextView) activity.findViewById(R.id.tv_user_created);
        btnCallUser = (FancyButton) activity.findViewById(R.id.btn_call_user);

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
        btnCallUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + tvPhone.getText().toString()));
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                    activity.startActivity(intent);
                }else{
                    Toast.makeText(context, "Te peate lubama rakendusel helistada", Toast.LENGTH_LONG).show();
                }

            }
        });


    }

    @Override
    public void addData() {
        userInfoManager.getUserInfo(UserInfoData.getEmail(), tvEmail, tvName, tvPhone, tvRole, tvUserUpdateDate, tvUserCreated);
        //If it is your username then go to edit page
        if(UserInfoData.getEmail().equals(LoginManager.retrieveUsername(context))){
            Intent i = new Intent(context, MyUserActivity.class);
            activity.startActivity(i);
        }
    }

    @Override
    public void registerReceivers() {

    }
}
