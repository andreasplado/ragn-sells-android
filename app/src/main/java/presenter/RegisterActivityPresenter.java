package presenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.jaredrummler.materialspinner.MaterialSpinner;

import mehdi.sakout.fancybuttons.FancyButton;
import utils.register.RegisterManager;
import www.ragnsells.ee.ragnsells.LoginActivity;
import www.ragnsells.ee.ragnsells.R;

/**
 * Created by Andreas on 06.05.2017.
 */

public class RegisterActivityPresenter implements Presenter {

    private Activity activity;
    private Context context;

    private RegisterManager registerManager;

    private EditText etName, etEmail, etPassword, etPhoneNr;
    private MaterialSpinner rolematerialSpinner;
    private FancyButton btnRegister;
    private Button btnBack;

    public RegisterActivityPresenter(Activity activity, Context context){
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

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, LoginActivity.class);
                activity.startActivity(i);
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerManager = new RegisterManager(activity, context);
                boolean areInputsCorrect = registerManager.areInputsCorrect(etName,etEmail,etPassword,etPhoneNr);
                String role = "client";
                if(rolematerialSpinner.getSelectedIndex() == 1){
                    role = "carrier";
                }
                if(rolematerialSpinner.getSelectedIndex() == 2){
                    role = "manager";
                }
                if(areInputsCorrect) {
                    registerManager.registerUser(etName.getText().toString(),
                            etEmail.getText().toString(),
                            etPassword.getText().toString(),
                            role,
                            etPhoneNr.getText().toString()
                    );
                }
            }
        });
    }

    @Override
    public void mapUiItems() {
        btnBack = (Button) activity.findViewById(R.id.back_to_user_view);
        etName =(EditText)activity.findViewById(R.id.et_name_register);
        etEmail = (EditText)activity.findViewById(R.id.et_email_register);
        etPassword = (EditText)activity.findViewById(R.id.et_password_register);
        etPhoneNr = (EditText)activity.findViewById(R.id.et_phone_number_register);
        rolematerialSpinner = (MaterialSpinner)activity.findViewById(R.id.sp_role);
        rolematerialSpinner.setItems("Klient", "Vedaja", "Mänedžer");
        btnRegister = (FancyButton)activity.findViewById(R.id.btn_register);

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
