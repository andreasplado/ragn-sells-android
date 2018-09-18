package www.ragnsells.ee.ragnsells;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

import presenter.CarrierActivityPresenter;
import constant.ResultCode;
import services.ProcessingService;
import utils.Alert;

public class CarrierActivity extends FragmentActivity implements OnMapReadyCallback {

    private CarrierActivityPresenter carrierActivityPresenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carrier);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fragment_map);
        mapFragment.getMapAsync(this);

    }


    @Override
    public void onMapReady(final GoogleMap googleMap) {
        //start the service
        Intent i = new Intent(this, ProcessingService.class);
        startService(i);

        carrierActivityPresenter = new CarrierActivityPresenter(this, this, googleMap);
        carrierActivityPresenter.init();
        carrierActivityPresenter.setTrashcansOnMap();
        carrierActivityPresenter.setUserOnMap(googleMap);
        carrierActivityPresenter.setGoogleMapstyle(googleMap);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case ResultCode.GPS_ENABLED_RESULT_CODE:
                carrierActivityPresenter.askUserToTurnOnGPS();
                break;
            case ResultCode.NETWORK_ENABLED_RESULT_CODE:
                carrierActivityPresenter.askUserToTurnOnMobileData();
                break;

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            //GPS is off
            case 1: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                } else {
                    Alert.info(this, "Viga", "Juurdepääs GPS asukohale keelatud");
                }
                return;
            }
            //Phonie dial through app is off
            case 2: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                } else {
                    Alert.info(this, "Viga", "Telefoniga ei saa läbi rakenduse helistada");
                }
            }
        }
    }

    @Override
    protected void onStop()
    {
        carrierActivityPresenter.unregisterRecievers();
        super.onStop();
    }

    @Override
    protected void onSaveInstanceState(Bundle SavedInstanceState)
    {
        super.onSaveInstanceState(SavedInstanceState);
    }
}

