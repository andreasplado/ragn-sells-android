package www.ragnsells.ee.ragnsells;

import android.app.Activity;
import android.os.Bundle;
import presenter.LoginActivityPersenter;


/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends Activity {

    private LoginActivityPersenter loginActivityPersenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginActivityPersenter = new LoginActivityPersenter(this, this);
        loginActivityPersenter.init();
    }

    @Override
    protected void onSaveInstanceState(Bundle SavedInstanceState)
    {
        super.onSaveInstanceState(SavedInstanceState);
    }
}

