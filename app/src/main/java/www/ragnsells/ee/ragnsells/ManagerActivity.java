package www.ragnsells.ee.ragnsells;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

import constant.ResultCode;
import presenter.ManagerActivityPresenter;
import www.ragnsells.ee.ragnsells.R;

public class ManagerActivity extends AppCompatActivity {

    private ManagerActivityPresenter managerActivityPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager);

        managerActivityPresenter = new ManagerActivityPresenter(this, this);
        managerActivityPresenter.init();

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case ResultCode.NETWORK_ENABLED_RESULT_CODE:
                managerActivityPresenter.askUserToTurnOnMobileData();
                break;

        }
    }

    @Override
    protected void onSaveInstanceState(Bundle SavedInstanceState)
    {
        super.onSaveInstanceState(SavedInstanceState);
    }

}
