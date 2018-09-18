package utils.login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import java.util.regex.Pattern;

import constant.PrefNames;
import data.ResponseData;
import data.UserData;
import model.auth.AuthResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import utils.Alert;
import utils.retrofit.Constants;
import utils.retrofit.RetrofitBuilder;
import utils.retrofit.UsersEndpoint;
import www.ragnsells.ee.ragnsells.LoadingActivity;
import www.ragnsells.ee.ragnsells.LoginActivity;
import www.ragnsells.ee.ragnsells.R;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Andreas on 05.05.2017.
 */

public class LoginManager {

    private Activity activity;
    private Context context;
    private static RetrofitBuilder retrofitBuilder = new RetrofitBuilder(Constants.URL_BASE);

    public LoginManager(Activity activity, Context context) {
        this.activity = activity;
        this.context = context;
    }

    public boolean areInputsCorrect(AutoCompleteTextView actvEmail, EditText etPassword){
        Pattern emailPattern = Pattern.compile("[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}");
        String email = actvEmail.getText().toString();
        String password = etPassword.getText().toString();
        boolean isCorrect = true;
        if(email.equals("")){
            actvEmail.setError("Email ei tohi olla tühi");
            isCorrect = false;
        }else if(!emailPattern.matcher(email).matches()){
            actvEmail.setError("Emaili formaat on ebakorrektne");
            isCorrect = false;
        }
        if(password.equals("")){
            etPassword.setError("Parool ei tohi olla tühi");
            isCorrect = false;
        }else if(password.length() < 4){
            etPassword.setError("Teie parool on liiga lühike");
            isCorrect = false;
        }
        return isCorrect;
    }

    public void getToken(final String username, final String password){
        new Thread(new Runnable() {
            public void run() {
                UsersEndpoint apiService = RetrofitBuilder.getClient().create(UsersEndpoint.class);
                retrofitBuilder.build();
                Call<AuthResponse> call = apiService.getToken(activity.getString(R.string.grant_type),
                        activity.getString(R.string.client_id),
                        activity.getString(R.string.secret),
                        username,
                        password);
                call.enqueue(new Callback<AuthResponse>() {
                    @Override
                    public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                        //Log.e("resp", response.body().toString());
                        if(response.body() != null) {
                            AuthResponse authResponse = response.body();
                            UserData.setAuthResponse(authResponse);
                            //save token to sharedpref
                            String token = response.body().getAccess_token();
                            writeLoginDataToSharedPrefs(context, token, username, password);
                            //PreferenceManager.getDefaultSharedPreferences(context).edit().putString("token", authResponse.getAccessToken()).commit();
                            //PreferenceManager.getDefaultSharedPreferences(context).edit().putString("email", username).commit();
                            Intent i = new Intent(context, LoadingActivity.class);
                            activity.startActivity(i);

                        }else{
                            Alert.loginIncorrectAlert(activity, context);
                        }

                    }

                    @Override
                    public void onFailure(Call<AuthResponse> call, Throwable t) {
                        Alert.loginFailedAlert(activity, context);
                    }
                });
            }
        }).start();
    }
    
    public static void writeLoginDataToSharedPrefs(Context context, String token, String username, String password){
        // Writing data to SharedPreferences
        SharedPreferences tokesSharedPreferences = context.getSharedPreferences(PrefNames.TOKEN, MODE_PRIVATE);

        // Writing data to SharedPreferences
        SharedPreferences.Editor editor = tokesSharedPreferences.edit();
        editor.putString("username", username);
        editor.putString("password", password);
        editor.putString("token", token);
        editor.commit();

    }

    public static String retrieveToken(Context context){
        SharedPreferences tokenSharedPreferences = context.getSharedPreferences(PrefNames.TOKEN, MODE_PRIVATE);
        return tokenSharedPreferences.getString("token", "");
    }

    public static String retrieveUsername(Context context){
        SharedPreferences tokenSharedPreferences = context.getSharedPreferences(PrefNames.TOKEN, MODE_PRIVATE);
        return tokenSharedPreferences.getString("username", "");
    }

    public static String retrievePassword(Context context){
        SharedPreferences tokenSharedPreferences = context.getSharedPreferences(PrefNames.TOKEN, MODE_PRIVATE);
        return tokenSharedPreferences.getString("password", "");
    }


    public void destroyToken(){
        new Thread(new Runnable() {
            public void run() {
                UsersEndpoint apiService = RetrofitBuilder.getClient().create(UsersEndpoint.class);
                retrofitBuilder.build();
                Call<ResponseData> call = apiService.logout(retrieveToken(context));
                call.enqueue(new Callback<ResponseData>() {
                    @Override
                    public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                        if (response.body() != null) {
                            ResponseData authResponse = response.body();
                            UserData.setResponseData(authResponse);
                            //save token to sharedpref
                            writeLoginDataToSharedPrefs(context, "", "", "");
                            Intent i = new Intent(context, LoginActivity.class);
                            activity.startActivity(i);
                        } else {
                            Alert.logoutUnsuccessfulAlert(context);
                        }

                    }

                    @Override
                    public void onFailure(Call<ResponseData> call, Throwable t) {
                        Alert.logoutUnsuccessfulAlert(context);
                    }
                });
            }
        }).start();
    }
}
