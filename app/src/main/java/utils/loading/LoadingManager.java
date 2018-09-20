package utils.loading;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import constant.PrefNames;
import data.UserData;
import model.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import utils.Alert;
import utils.login.LoginManager;
import utils.myuser.UserSharedPrefConstants;
import utils.retrofit.Constants;
import utils.retrofit.RetrofitBuilder;
import utils.retrofit.UsersEndpoint;
import utils.sqlite.UserHandler;
import www.ragnsells.ee.ragnsells.CarrierActivity;
import www.ragnsells.ee.ragnsells.ClientActivity;
import www.ragnsells.ee.ragnsells.ManagerActivity;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Andreas on 05.05.2017.
 */

public class LoadingManager {

    private Activity activity;
    private Context context;
    private static RetrofitBuilder retrofitBuilder = new RetrofitBuilder(Constants.URL_BASE);

    public LoadingManager(Activity activity, Context context){
        this.activity = activity;
        this.context = context;
    }

    /**
     * Gets the activity state of a user.
     * If user role is client, start {@link ClientActivity}
     * If user role is carrier, start {@link CarrierActivity}
     * If user role is manager, start {@link ManagerActivity}
     */
    public void getActivityState(){
        UsersEndpoint apiService = RetrofitBuilder.getClient().create(UsersEndpoint.class);

        retrofitBuilder.build();
        Call<User> call = apiService.getUser(LoginManager.retrieveUsername(context),LoginManager.retrieveToken(context));
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(response.body() != null) {
                    UserHandler userHandler = new UserHandler(context);
                    userHandler.removeUsers();
                    userHandler.addUser(response.body());
                    UserData.setUser(response.body());
                    UserData.setIsOnline(true);
                    saveUserDataToSharedPrefs(response.body().getId(), response.body().getName(), response.body().getEmail(),
                            response.body().getRole(), response.body().getPhone_nr(), response.body().getCreated_at(), response.body().getUpdated_at());
                    switch(UserData.getUser().getRole()){
                        case "client":
                            Intent registerContainerActivityIntent = new Intent(context, ClientActivity.class);
                            activity.startActivity(registerContainerActivityIntent);
                            break;
                        case "carrier":
                            Intent mapsActivityIntent = new Intent(context, CarrierActivity.class);
                            activity.startActivity(mapsActivityIntent);
                            break;
                        case "manager":
                            Intent managerActivityIntent = new Intent(context, ManagerActivity.class);
                            activity.startActivity(managerActivityIntent);
                    }
                }else{
                    Alert.loginFailedAlert(activity, context);
                }

            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                //Alert.loginFailedAlert(activity, context);
                UserHandler userHandler = new UserHandler(context);
                User user = userHandler.getUser(LoginManager.retrieveUsername(context));

                UserData.setUser(user);
                UserData.setIsOnline(false);
                switch(UserData.getUser().getRole()){
                    case "client":
                        Intent registerContainerActivityIntent = new Intent(context, ClientActivity.class);
                        activity.startActivity(registerContainerActivityIntent);
                        break;
                    case "carrier":
                        Intent mapsActivityIntent = new Intent(context, CarrierActivity.class);
                        activity.startActivity(mapsActivityIntent);
                        break;
                    case "manager":
                        Intent managerActivityIntent = new Intent(context, ManagerActivity.class);
                        activity.startActivity(managerActivityIntent);
                }

            }
        });
    }

    private void saveUserDataToSharedPrefs(int id, String name, String email, String role, String phoneNr, String userCreatedDate, String userUpdatedDate){
        // Writing data to SharedPreferences
        SharedPreferences tokesSharedPreferences = context.getSharedPreferences(PrefNames.TOKEN, MODE_PRIVATE);

        // Writing data to SharedPreferences
        SharedPreferences.Editor editor = tokesSharedPreferences.edit();
        editor.putInt(UserSharedPrefConstants.SESSION_ID, id);
        editor.putString(UserSharedPrefConstants.SESSION_NAME, name);
        editor.putString(UserSharedPrefConstants.SESSION_EMAIL, email);
        editor.putString(UserSharedPrefConstants.SESSION_ROLE, role);
        editor.putString(UserSharedPrefConstants.SESSION_PHONE_NR, phoneNr);
        editor.putString(UserSharedPrefConstants.SESSION_USER_CREATED_DATE, userCreatedDate);
        editor.putString(UserSharedPrefConstants.SESSION_USER_UDPATED_DATE, userUpdatedDate);
        editor.commit();
    }

    public static int retrieveUserId(Context context){
        SharedPreferences tokesSharedPreferences = context.getSharedPreferences(PrefNames.TOKEN, MODE_PRIVATE);
        return  tokesSharedPreferences.getInt(UserSharedPrefConstants.SESSION_ID, 0);
    }

}
