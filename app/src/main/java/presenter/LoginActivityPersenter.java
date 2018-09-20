package presenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;

import java.util.HashMap;

import mehdi.sakout.fancybuttons.FancyButton;
import utils.login.BannerAdapter;
import utils.login.LoginManager;
import www.ragnsells.ee.ragnsells.R;
import www.ragnsells.ee.ragnsells.RegisterActivity;

/**
 * Created by Andreas on 05.05.2017.
 */

public class LoginActivityPersenter implements Presenter{

    private Activity activity;
    private Context context;
    private AutoCompleteTextView etEmail;
    private EditText etPassword;
    private FancyButton btnLogin, btnRegister;
    private LoginManager loginManager;
    private SliderLayout mDemoSlider;

    public LoginActivityPersenter(Activity activity, Context context){
        this.activity = activity;
        this.context = context;
    }

    @Override
    public void init() {
        registerReceivers();
        requestPermissions();
        mapUiItems();
        addData();
        addClicks();
        addListeners();
    }

    @Override
    public void initFileds() {

    }

    @Override
    public void addListeners() {

    }

    @Override
    public void addClicks() {
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginManager = new LoginManager(activity, context);
                boolean areInputsCorrect= loginManager.areInputsCorrect(etEmail,
                        etPassword);
                if(areInputsCorrect) {
                    loginManager.getToken(etEmail.getText().toString(), etPassword.getText().toString());
                }
            }
        });
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, RegisterActivity.class);
                activity.startActivity(i);
            }
        });

    }

    @Override
    public void mapUiItems() {
        etEmail = (AutoCompleteTextView) activity.findViewById(R.id.et_email);
        etPassword = (EditText) activity.findViewById(R.id.et_password);
        btnLogin = (FancyButton)activity.findViewById(R.id.btn_login);
        btnRegister = (FancyButton) activity.findViewById(R.id.btn_register);
        mDemoSlider = (SliderLayout)activity.findViewById(R.id.slider);
        manageSlider();

    }

    private void manageSlider(){
        HashMap<String,String> url_maps = new HashMap<String, String>();
        url_maps.put("Süvamahutid", "http://ec2-35-162-160-209.us-west-2.compute.amazonaws.com:8080/static_files/1.png");
        url_maps.put("Osta big bag", "http://ec2-35-162-160-209.us-west-2.compute.amazonaws.com:8080/static_files/2.png");
        for(String name : url_maps.keySet()){
            TextSliderView textSliderView = new TextSliderView(context);
            // initialize a SliderLayout
            textSliderView
                    .description(name)
                    .image(url_maps.get(name))
                    .setScaleType(BaseSliderView.ScaleType.Fit)
                    .setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
                        @Override
                        public void onSliderClick(BaseSliderView slider) {
                            Toast.makeText(context,slider.getBundle().get("extra") + "",Toast.LENGTH_SHORT).show();
                        }
                    });
            mDemoSlider.addSlider(textSliderView);

            //add your extra information
            textSliderView.bundle(new Bundle());
            textSliderView.getBundle()
                    .putString("extra",name);
            mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Accordion);
            mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
            mDemoSlider.setCustomAnimation(new DescriptionAnimation());
            mDemoSlider.setDuration(4000);


        }
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
