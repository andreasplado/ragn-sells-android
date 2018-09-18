package utils.userinfo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import data.ResponseData;
import model.User;
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
 * Created by Andreas on 18.05.2017.
 */

public class UserInfoManager {

    private Activity activity;
    private Context context;

    public UserInfoManager(Activity activity, Context context){
        this.activity = activity;
        this.context = context;
    }

    private static RetrofitBuilder retrofitBuilder = new RetrofitBuilder(Constants.URL_BASE);


    public void getUserInfo(final String email, final TextView tvEmail, final TextView tvName, final TextView tvPhone, final TextView tvRole, final TextView tvUserUpdateDate, final TextView tvUserCreated){
        UsersEndpoint apiService = RetrofitBuilder.getClient().create(UsersEndpoint.class);
        retrofitBuilder.build();
        Call<User> call = apiService.getUser(email, LoginManager.retrieveToken(context));
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                Log.e("resp", response.body().toString());
                if(response.body() != null) {
                    User user = response.body();
                    tvEmail.setText(user.getEmail());
                    tvName.setText(user.getName());
                    tvPhone.setText(user.getPhone_nr());
                    tvRole.setText(user.getRole());
                    tvUserUpdateDate.setText(user.getUpdated_at());
                    tvUserCreated.setText(user.getCreated_at());

                }
                else{
                    Alert.registrationFailedServerErrorAlert(activity, context);
                }

            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Alert.registrationFailedAlert(activity, context);
            }
        });
    }
}
