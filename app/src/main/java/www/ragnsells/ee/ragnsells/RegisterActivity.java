package www.ragnsells.ee.ragnsells;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import presenter.RegisterActivityPresenter;

public class RegisterActivity extends AppCompatActivity {

    private RegisterActivityPresenter registerActivityPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        registerActivityPresenter = new RegisterActivityPresenter(this, this);
        registerActivityPresenter.init();
    }

    @Override
    protected void onSaveInstanceState(Bundle SavedInstanceState)
    {
        super.onSaveInstanceState(SavedInstanceState);
    }
}
