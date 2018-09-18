package www.ragnsells.ee.ragnsells;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import presenter.AddLocationActivityPresenter;

public class AddLocationActivity extends AppCompatActivity {

    private AddLocationActivityPresenter addLocationActivityPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_location);
        addLocationActivityPresenter = new AddLocationActivityPresenter(this, this);
        addLocationActivityPresenter.init();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i=new Intent(this,ManagerActivity.class);
        startActivity(i);
    }

    @Override
    protected void onSaveInstanceState(Bundle SavedInstanceState)
    {
        super.onSaveInstanceState(SavedInstanceState);
    }

}
