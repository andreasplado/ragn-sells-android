package www.ragnsells.ee.ragnsells;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import presenter.UserInfoActivityPresenter;

public class UserInfoActivity extends AppCompatActivity {

    private UserInfoActivityPresenter userInfoActivityPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        userInfoActivityPresenter = new UserInfoActivityPresenter(this, this);
        userInfoActivityPresenter.init();
    }

    @Override
    protected void onSaveInstanceState(Bundle SavedInstanceState)
    {
        super.onSaveInstanceState(SavedInstanceState);
    }
}
