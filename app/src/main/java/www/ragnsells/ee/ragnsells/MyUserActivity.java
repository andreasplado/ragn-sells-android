package www.ragnsells.ee.ragnsells;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import presenter.MyUserActivityPresenter;

public class MyUserActivity extends AppCompatActivity {

    private MyUserActivityPresenter myUserActivityPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_user);
        myUserActivityPresenter = new MyUserActivityPresenter(this, this);
        myUserActivityPresenter.init();
    }

    @Override
    protected void onSaveInstanceState(Bundle SavedInstanceState)
    {
        super.onSaveInstanceState(SavedInstanceState);
    }
}