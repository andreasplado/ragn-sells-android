package www.ragnsells.ee.ragnsells;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import presenter.LoadingActivityPresenter;
import www.ragnsells.ee.ragnsells.R;

public class LoadingActivity extends AppCompatActivity {

    private LoadingActivityPresenter loadingActivityPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        loadingActivityPresenter = new LoadingActivityPresenter(this, this);
    }

    @Override
    protected void onSaveInstanceState(Bundle SavedInstanceState)
    {
        super.onSaveInstanceState(SavedInstanceState);
    }
}
