package presenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import data.TrashcanLocation;
import mehdi.sakout.fancybuttons.FancyButton;
import model.Trashcan;
import utils.carrier.TrashcanManager;
import utils.client.MyBounceInterpolator;
import utils.dataconverter.PlaceConverter;
import www.ragnsells.ee.ragnsells.AddLocationActivity;
import www.ragnsells.ee.ragnsells.R;

/**
 * Created by Andreas on 09.05.2017.
 */

public class AddLocationSelectedActivityPresenter implements Presenter {

    private Activity activity;
    private Context context;
    private FancyButton btnAddContainer;
    private TrashcanManager trashcanManager;
    private TextView txtAddress;
    private EditText etIssue;

    public AddLocationSelectedActivityPresenter(Activity activity, Context context){
        this.activity = activity;
        this.context = context;
    }

    @Override
    public void init() {
        registerReceivers();
        requestPermissions();
        mapUiItems();
        addData();
        addClicks();
        addListeners();
    }

    @Override
    public void initFileds() {

    }

    @Override
    public void addData() {
        txtAddress.setText("Asukoht: " + TrashcanLocation.getAddress());
    }

    @Override
    public void addListeners() {

    }

    @Override
    public void addClicks() {
        btnAddContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Animate.
                Animation myAnim = AnimationUtils.loadAnimation(activity, R.anim.bounce);
                MyBounceInterpolator interpolator = new MyBounceInterpolator(0.2, 20);
                myAnim.setInterpolator(interpolator);
                btnAddContainer.startAnimation(myAnim);

                //Register container.
                trashcanManager = new TrashcanManager(activity, context);
                trashcanManager.registerContainer(etIssue.getText().toString());
                TrashcanLocation.setLocation(null);
                Intent i = new Intent(context, AddLocationActivity.class);
                activity.startActivity(i);
            }
        });
    }

    @Override
    public void mapUiItems() {
        btnAddContainer = (FancyButton)activity.findViewById(R.id.btn_add_container);
        txtAddress = (TextView)activity.findViewById(R.id.tv_address);
        etIssue = (EditText)activity.findViewById(R.id.et_issue);
    }

    @Override
    public void requestPermissions() {

    }

    @Override
    public void registerReceivers() {

    }
}
