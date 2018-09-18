package www.ragnsells.ee.ragnsells;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import presenter.ClientActivityLogPresenter;

public class ClientActivityLog extends AppCompatActivity {

    private ClientActivityLogPresenter clientActivityLogPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_log);
        clientActivityLogPresenter = new ClientActivityLogPresenter(this, this);
        clientActivityLogPresenter.init();
    }
}
