package presenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.widget.DrawerLayout;
import android.widget.Button;

import utils.loading.LoadingManager;
import utils.login.LoginManager;
import www.ragnsells.ee.ragnsells.LoginActivity;

/**
 * Created by Andreas on 05.05.2017.
 */

public class LoadingActivityPresenter{

    private Activity activity;
    private Context context;
    private LoadingManager loadingManager;

    public LoadingActivityPresenter(Activity activity, Context context){
        this.activity = activity;
        this.context = context;
        userCheck();
    }

    private void userCheck() {
        loadingManager = new LoadingManager(activity, context);
        if(!LoginManager.retrieveToken(context).equals("") && !LoginManager.retrieveUsername(context).equals("") ){
            loadingManager.getActivityState();
        }else{
            Intent i = new Intent(context, LoginActivity.class);
            activity.startActivity(i);
        }
    }

}
