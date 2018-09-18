package utils.register;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import java.util.regex.Pattern;

import data.ResponseData;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import utils.Alert;
import utils.login.LoginManager;
import utils.retrofit.Constants;
import utils.retrofit.RetrofitBuilder;
import utils.retrofit.UsersEndpoint;
import www.ragnsells.ee.ragnsells.LoadingActivity;

/**
 * Created by Andreas on 06.05.2017.
 */

public class RegisterManager {

    private Activity activity;
    private Context context;
    private static RetrofitBuilder retrofitBuilder = new RetrofitBuilder(Constants.URL_BASE);

    public RegisterManager(Activity activity, Context context){
        this.activity = activity;
        this.context = context;
    }

    public boolean areInputsCorrect(EditText etName, EditText etEmail, EditText etPassword, EditText etPhoneNr){
        Pattern pattern = Pattern.compile("[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}");
        String fullNameExpression = "^[a-zA-Z\\s]+";
        String onlyNumbersExpression = "[0-9]+";
        String name = etName.getText().toString();
        String email = etEmail.getText().toString();
        String password = etPassword.getText().toString();
        String mobileNr = etPhoneNr.getText().toString();
        boolean isCorrect = true;
        if(name.equals("")){
            etName.setError("Nimi ei tohi olla tühi");
            isCorrect = false;
        }else if(name.length() < 5){
            etName.setError("Nimi ei saa olla nii lühike");
            isCorrect = false;
        }else if(!name.matches(fullNameExpression)){
            etName.setError("Nimi peab olema formadis \'Eesnimi Perekonnanimi\'");
            isCorrect = false;
        }
        if(email.equals("")) {
            etEmail.setError("Email ei tohi olla tühi");
            isCorrect = false;
        } else if(!pattern.matcher(email).matches()){
            etEmail.setError("Emaili formaat on ebakorrektne");
            isCorrect = false;
        }
        if(password.equals("")){
            etPassword.setError("Parool ei tohi olla tühi");
            isCorrect = false;
        }else if(password.length() < 4){
            etPassword.setError("Teie parool on liiga lühike");
            isCorrect = false;
        }
        if(mobileNr.length() < 5){
            etPhoneNr.setError("Telefoni number ei saa olla nii lühike");
            isCorrect = false;
        }else if(!mobileNr.matches(onlyNumbersExpression)){
            etPhoneNr.setError("Peate sistama ainult numbri ilma suunakoodita");
            isCorrect = false;
        }
        return isCorrect;
    }

    public void registerUser(final String name, final String email, final String password,
                             final String role, final String phoneNr){
        UsersEndpoint apiService = RetrofitBuilder.getClient().create(UsersEndpoint.class);
        retrofitBuilder.build();
        Call<ResponseData> call = apiService.registerUser(name, email,
                password, role, phoneNr);
        call.enqueue(new Callback<ResponseData>() {
            @Override
            public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                Log.e("resp", response.body().toString());
                if(!response.body().isError()) {
                    LoginManager.writeLoginDataToSharedPrefs(context, "", "", "");
                    ResponseData authResponse = response.body();
                    Toast.makeText(context, "Te registreerusite edukalt!", Toast.LENGTH_LONG).show();
                    Intent i = new Intent(context, LoadingActivity.class);
                    activity.startActivity(i);

                }else if(response.body().isError()){
                    Toast.makeText(context, response.body().getMsg(), Toast.LENGTH_LONG).show();
                }
                else{
                    Alert.registrationFailedServerErrorAlert(activity, context);
                }

            }

            @Override
            public void onFailure(Call<ResponseData> call, Throwable t) {
                Alert.registrationFailedAlert(activity, context);
            }
        });
    }
}
