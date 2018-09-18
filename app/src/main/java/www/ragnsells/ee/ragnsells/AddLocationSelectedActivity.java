package www.ragnsells.ee.ragnsells;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import presenter.AddLocationSelectedActivityPresenter;

public class AddLocationSelectedActivity extends AppCompatActivity {

    private AddLocationSelectedActivityPresenter addLocationSelectedActivityPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_location_selected);
        addLocationSelectedActivityPresenter = new AddLocationSelectedActivityPresenter(this, this);
        addLocationSelectedActivityPresenter.init();
    }
    @Override
    protected void onSaveInstanceState(Bundle SavedInstanceState)
    {
        super.onSaveInstanceState(SavedInstanceState);
    }
}
