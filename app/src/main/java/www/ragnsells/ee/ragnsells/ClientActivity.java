package www.ragnsells.ee.ragnsells;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import presenter.ClientActivityPresenter;

public class ClientActivity extends AppCompatActivity {


    private ClientActivityPresenter clientActivityPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);
        clientActivityPresenter = new ClientActivityPresenter(this, this);
        clientActivityPresenter.init();
    }

    @Override
    protected void onSaveInstanceState(Bundle SavedInstanceState)
    {
        super.onSaveInstanceState(SavedInstanceState);
    }

    @Override
    protected void onStop() {
        clientActivityPresenter.unregisterRecievers();
        super.onStop();
    }
}
