package utils.myuser;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.jaredrummler.materialspinner.MaterialSpinner;
import constant.PrefNames;
import data.ResponseData;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import utils.dataconverter.DateConverter;
import utils.login.LoginManager;
import utils.retrofit.Constants;
import utils.retrofit.RetrofitBuilder;
import utils.retrofit.UsersEndpoint;
import www.ragnsells.ee.ragnsells.LoginActivity;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Andreas on 13.05.2017.
 */

public class EditUserManager {

    private Activity activity;
    public Context context;
    private SharedPreferences tokenSharedPreferences;
    private static RetrofitBuilder retrofitBuilder = new RetrofitBuilder(Constants.URL_BASE);



    public EditUserManager(Activity activity, Context context){
        this.activity = activity;
        this.context = context;
    }

    public void setUserInfo(final TextView tvEmail, final TextView tvName, final TextView tvPhone, final TextView tvRole,
                            final TextView txtUserCreated, final TextView txtUserUpdated){
        tokenSharedPreferences = context.getSharedPreferences(PrefNames.TOKEN, MODE_PRIVATE);
        tvEmail.setText(tokenSharedPreferences.getString(UserSharedPrefConstants.SESSION_EMAIL, ""));
        tvName.setText(tokenSharedPreferences.getString(UserSharedPrefConstants.SESSION_NAME, ""));
        tvPhone.setText(tokenSharedPreferences.getString(UserSharedPrefConstants.SESSION_PHONE_NR, ""));

        String role = tokenSharedPreferences.getString(UserSharedPrefConstants.SESSION_ROLE, "");
        if(role.equals("client")){
            role = "Klient";
        }else if(role.equals("manager")){
            role = "Mänedžer";
        }else if(role.equals("carrier")){
            role = "Vedaja";
        }
        tvRole.setText(role);
        txtUserCreated.setText(DateConverter.convertDateTimeToReadableFormat(tokenSharedPreferences.getString(UserSharedPrefConstants.SESSION_USER_CREATED_DATE, "")));
        txtUserUpdated.setText(DateConverter.convertDateTimeToReadableFormat(tokenSharedPreferences.getString(UserSharedPrefConstants.SESSION_USER_UDPATED_DATE, "")));
    }

    public void updateName(EditText etName, final TextView tvName){
        final int id = tokenSharedPreferences.getInt(UserSharedPrefConstants.SESSION_ID, 0);
        final String name = etName.getText().toString();
        new Thread(new Runnable() {
            public void run() {
                UsersEndpoint apiService = RetrofitBuilder.getClient().create(UsersEndpoint.class);
                retrofitBuilder.build();
                Call<ResponseData> call = apiService.updateName(id, name, LoginManager.retrieveToken(context));
                call.enqueue(new Callback<ResponseData>() {
                    @Override
                    public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                        if(response.body() != null) {

                            if(response.body().isError() == true){
                                Toast.makeText(context, response.body().getMsg(), Toast.LENGTH_LONG).show();
                            }else{
                                Toast.makeText(context, "Muutsite nime edukalt!", Toast.LENGTH_LONG).show();
                                tvName.setText(name);
                            }
                        }else{
                            Toast.makeText(context, "Server error!", Toast.LENGTH_LONG).show();
                        }

                    }

                    @Override
                    public void onFailure(Call<ResponseData> call, Throwable t) {
                        Toast.makeText(context, "Palun lülitage oma võrguühendus sisse!", Toast.LENGTH_LONG).show();
                    }
                });
            }
        }).start();
    }

    public void updatePhoneNr(EditText etPhoneNr, final TextView tvPhoneNr){
        final int id = tokenSharedPreferences.getInt(UserSharedPrefConstants.SESSION_ID, 0);
        final String phoneNr = etPhoneNr.getText().toString();
        new Thread(new Runnable() {
            public void run() {
                UsersEndpoint apiService = RetrofitBuilder.getClient().create(UsersEndpoint.class);
                retrofitBuilder.build();
                Call<ResponseData> call = apiService.updateUserPhone(id, phoneNr, LoginManager.retrieveToken(context));
                call.enqueue(new Callback<ResponseData>() {
                    @Override
                    public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                        if(response.body() != null) {
                            if(response.body().isError() == true){
                                Toast.makeText(context, response.body().getMsg(), Toast.LENGTH_LONG).show();
                            }else {
                                Toast.makeText(context, "Muutsite telefon numbrit edukalt!", Toast.LENGTH_LONG).show();
                                tvPhoneNr.setText(phoneNr);
                            }
                        }else{
                            Toast.makeText(context, "Server error!", Toast.LENGTH_LONG).show();
                        }

                    }

                    @Override
                    public void onFailure(Call<ResponseData> call, Throwable t) {
                        Toast.makeText(context, "Palun lülitage oma võrguühendus sisse!", Toast.LENGTH_LONG).show();
                    }
                });
            }
        }).start();
    }

    public void updatePassword(EditText etPassword){
        final int id = tokenSharedPreferences.getInt(UserSharedPrefConstants.SESSION_ID, 0);
        final String password = etPassword.getText().toString();
        new Thread(new Runnable() {
            public void run() {
                UsersEndpoint apiService = RetrofitBuilder.getClient().create(UsersEndpoint.class);
                retrofitBuilder.build();
                Call<ResponseData> call = apiService.updateUserPass(id, password, LoginManager.retrieveToken(EditUserManager.this.context));
                call.enqueue(new Callback<ResponseData>() {
                    @Override
                    public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                        if(response.body() != null) {
                            if(response.body().isError() == true){
                                Toast.makeText(EditUserManager.this.context, response.body().getMsg(), Toast.LENGTH_LONG).show();
                            }else{
                                //put new string to sharedpref
                                SharedPreferences.Editor editor = tokenSharedPreferences.edit();
                                editor.putString("password", password);
                                editor.commit();
                                Toast.makeText(EditUserManager.this.context, "Muutsite parooli edukalt!", Toast.LENGTH_LONG).show();
                            }
                        }else{
                            Toast.makeText(EditUserManager.this.context, "Server error!", Toast.LENGTH_LONG).show();
                        }

                    }

                    @Override
                    public void onFailure(Call<ResponseData> call, Throwable t) {
                        Toast.makeText(EditUserManager.this.context, "Palun lülitage oma võrguühendus sisse!", Toast.LENGTH_LONG).show();
                    }
                });
            }
        }).start();
    }

    public void updateRole(final MaterialSpinner msUserRole, final TextView tvRole){
        final int id = tokenSharedPreferences.getInt(UserSharedPrefConstants.SESSION_ID, 0);
        String role = "client";
        if(msUserRole.getSelectedIndex() == 1){
            role = "carrier";
        }
        if(msUserRole.getSelectedIndex() == 2){
            role = "manager";
        }
        final String finalRole = role;
        new Thread(new Runnable() {
            public void run() {
                UsersEndpoint apiService = RetrofitBuilder.getClient().create(UsersEndpoint.class);
                retrofitBuilder.build();
                Call<ResponseData> call = apiService.updateUserRole(id, finalRole, LoginManager.retrieveToken(context));
                call.enqueue(new Callback<ResponseData>() {
                    @Override
                    public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                        if(response.body() != null) {
                            if(response.body().isError() == true){
                                Toast.makeText(context, response.body().getMsg(), Toast.LENGTH_LONG).show();
                            }else{
                                Toast.makeText(context, "Muutsite rolli edukalt!", Toast.LENGTH_LONG).show();
                                if(finalRole.equals("client")){
                                    tvRole.setText("Klient");
                                }else if(finalRole.equals("manager")){
                                    tvRole.setText("Mänedžer");
                                }else if(finalRole.equals("carrier")){
                                    tvRole.setText("Vedaja");
                                }

                            }
                        }else{
                            Toast.makeText(context, "Server error!", Toast.LENGTH_LONG).show();
                        }

                    }

                    @Override
                    public void onFailure(Call<ResponseData> call, Throwable t) {
                        Toast.makeText(context, "Palun lülitage oma võrguühendus sisse!", Toast.LENGTH_LONG).show();
                    }
                });
            }
        }).start();
    }

    public void deleteUser(final int id){
        new Thread(new Runnable() {
            public void run() {
                UsersEndpoint apiService = RetrofitBuilder.getClient().create(UsersEndpoint.class);
                retrofitBuilder.build();
                Call<ResponseData> call = apiService.deleteUser(id, LoginManager.retrieveToken(context));
                call.enqueue(new Callback<ResponseData>() {
                    @Override
                    public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                        if(response.body() != null) {
                            if(response.body().isError() == true){
                                Toast.makeText(context, response.body().getMsg(), Toast.LENGTH_LONG).show();
                            }else{
                                Toast.makeText(context, "Kasutaja edukalt kustutatud!", Toast.LENGTH_LONG).show();
                                LoginManager.writeLoginDataToSharedPrefs(context, "", "", "");
                                Intent i = new Intent(context, LoginActivity.class);
                                activity.startActivity(i);
                            }
                        }else{
                            Toast.makeText(context, "Server error!", Toast.LENGTH_LONG).show();
                        }

                    }

                    @Override
                    public void onFailure(Call<ResponseData> call, Throwable t) {
                        Toast.makeText(context, "Palun lülitage oma võrguühendus sisse!", Toast.LENGTH_LONG).show();
                    }
                });
            }
        }).start();
    }



}
