package utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.provider.Settings;
import android.support.design.widget.TextInputEditText;
import android.text.util.Linkify;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.Marker;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import constant.ResultCode;
import data.TrashcanLocation;
import data.UserInfoData;
import mehdi.sakout.fancybuttons.FancyButton;
import model.Trashcan;
import model.Trashcans;
import utils.carrier.TrashcanLocationManager;
import utils.client.ClientManager;
import utils.manager.ManagerManager;
import www.ragnsells.ee.ragnsells.LoadingActivity;
import www.ragnsells.ee.ragnsells.MyUserActivity;
import www.ragnsells.ee.ragnsells.R;
import www.ragnsells.ee.ragnsells.UserInfoActivity;

/**
 * Created by Andreas on 29.04.2017.
 */

public class Alert {

    private static AlertDialog.Builder cannotGetContainersAlert,
            cannotGetGPSAlert, cannotTurnOnDataConnectionAlert, deleteTrashcan, cannotGetTokenAlert;

    /**
     * Adds the opportunity to turn on GPS
     * @param activity
     * @param context
     */
    public static void turnOnGPSAlert(final Activity activity, final Context context) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context, R.style.AlertDialogTheme);
        alertDialog.setTitle("Hoiatus");
        alertDialog.setMessage("GPS ei tööta. Soovite seda tööle panna?");
        alertDialog.setPositiveButton("Seaded", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                activity.startActivityForResult(intent, ResultCode.GPS_ENABLED_RESULT_CODE);
            }
        });
        alertDialog.setNegativeButton("Tühista", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alertDialog.setCancelable(false);
        alertDialog.show();
        setCannotTurnOnGPSAlert(alertDialog);
    }

    /**
     * Cannot get token from the specified server.
     */
    public static void loginFailedAlert(final Activity activity, final Context context){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context, R.style.AlertDialogTheme);
        alertDialog.setTitle("Viga");
        alertDialog.setMessage("Sisselogimine ebaõnnestus!\nPalun lülitage andmeside sisse uuesti!");
        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent i = new Intent(context, LoadingActivity.class);
                activity.startActivity(i);
            }
        });
        alertDialog.setCancelable(false);
        alertDialog.show();
        setCannotGetTokenAlert(alertDialog);
    }

    public static void registrationFailedAlert(final Activity activity, final Context context){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context, R.style.AlertDialogTheme);
        alertDialog.setTitle("Viga");
        alertDialog.setMessage("Registreerumine ebaõnnestus!\nPalun lülitage andmeside sisse!");
        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alertDialog.setCancelable(false);
        alertDialog.show();
    }

    public static void registrationFailedServerErrorAlert(final Activity activity, final Context context){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context, R.style.AlertDialogTheme);
        alertDialog.setTitle("Viga");
        alertDialog.setMessage("Registreerumine ebaõnnestus!\nPalun proovige uuesti!");
        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alertDialog.setCancelable(false);
        alertDialog.show();
    }

    public static void loginIncorrectAlert(final Activity activity, final Context context){
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(context, R.style.AlertDialogTheme);
        alertDialog.setTitle("Viga");
        alertDialog.setMessage("Vale kasutajatunnus või parool!\nPalun proovige uuesti!");
        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alertDialog.setCancelable(false);
        alertDialog.show();
        setCannotGetTokenAlert(alertDialog);
    }


    public static void logoutUnsuccessfulAlert(Context context){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context, R.style.AlertDialogTheme);
        alertDialog.setTitle("Viga");
        alertDialog.setMessage("Välja logimine ebaõnnestus. Te ei pruugi olla aktiivses võrguühenduses!\nPalun proovige uuesti!");
        alertDialog.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alertDialog.show();
        setCannotGetTokenAlert(alertDialog);
    }

    public static void cannotGetContainersAlert(Context context, final TrashcanLocationManager trashcanLocationManager){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context, R.style.AlertDialogTheme);
        alertDialog.setTitle("Prügikasite asukohad teadmata");
        alertDialog.setMessage("Ei saadud prügikonteinerite asukohta serverist!\n" +
                "Kas soovite uuesti proovida?");
        alertDialog.setPositiveButton("Jah", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                trashcanLocationManager.getAllTrashcansOnMap(true);
            }
        });
        alertDialog.setNegativeButton("Ei", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alertDialog.show();
        setCannotGetContainersAlert(alertDialog);
    }

    public static void turnOnDataConnectionAlert(final Activity activity, final Context context) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context, R.style.AlertDialogTheme);
        alertDialog.setTitle("Hoiatus");
        alertDialog.setMessage("Andmeside ei tööta. Soovite seda tööle panna?");
        alertDialog.setPositiveButton("Seaded", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
                activity.startActivityForResult(intent, ResultCode.NETWORK_ENABLED_RESULT_CODE);
            }
        });
        alertDialog.setNegativeButton("Tühista", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alertDialog.setCancelable(false);
        alertDialog.show();
        setCannotTurnOnDataConnectionAlert(alertDialog);
    }

    public void traschanDetailViewAlert(final Context context, final int id, final Marker marker, final TrashcanLocationManager trashcanLocationManager) {
        View view = View.inflate(trashcanLocationManager.context, R.layout.alert_edit_trashcan, null);
        final FancyButton btnAddIssue = (FancyButton) view.findViewById(R.id.btn_add_issue);
        final FancyButton btnDeleteIssue = (FancyButton) view.findViewById(R.id.btn_delete_issue);
        final TextView tvEmail = (TextView)view.findViewById(R.id.tv_email);
        final TextView tvPlaceAddress = (TextView)view.findViewById(R.id.tv_place_name);
        final TextInputEditText etIssue = (TextInputEditText)view.findViewById(R.id.et_issue);
        trashcanLocationManager.addTrashcanInfoText(id, etIssue, tvEmail, tvPlaceAddress);

        if(etIssue.getText().toString().equals("")){
            btnDeleteIssue.setVisibility(View.INVISIBLE);
        }

        tvEmail.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                String email = tvEmail.getText().toString();
                UserInfoData.setEmail(tvEmail.getText().toString());
                Intent i = new Intent(trashcanLocationManager.context, UserInfoActivity.class);
                trashcanLocationManager.activity.startActivity(i);
                return false;
            }
        });

        btnAddIssue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!etIssue.getText().toString().equals("")) {
                    trashcanLocationManager.updateTrashcan(id, etIssue.getText().toString(), etIssue, btnAddIssue, btnDeleteIssue, marker);
                    TrashcanLocationManager tlm = TrashcanLocation.getTrashcanLocationManager();
                    if (tlm != null) {
                        tlm.getAllTrashcansOnMap(false);
                    } else {
                        Toast.makeText(context, "Konteineri info uuendamine ebaõnnestus", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(context, "Palun lisage probleem", Toast.LENGTH_LONG).show();
                }
            }
        });
        btnDeleteIssue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                trashcanLocationManager.updateTrashcan(id, "", etIssue, btnAddIssue, btnDeleteIssue, marker);
                TrashcanLocationManager tlm = TrashcanLocation.getTrashcanLocationManager();
                if(tlm != null){
                    tlm.getAllTrashcansOnMap(false);
                }else{
                    Toast.makeText(context, "Konteineri info uuendamine ebaõnnestus", Toast.LENGTH_SHORT).show();
                }
            }
        });



        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(trashcanLocationManager.context, R.style.AlertDialogTheme);
        alertDialog.setTitle("Konteineri andmed");
        //alertDialog.setMessage("Muutke konteineri andmeid");
        alertDialog.setView(view);

        tvEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        alertDialog.setPositiveButton("Kustuta konteiner", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                trashcanLocationManager.deleteTrashcan(id);
            }
        });
        alertDialog.setNegativeButton("Tühista", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alertDialog.setCancelable(false);
        alertDialog.show();
    }

    public static void trashcanIssueDetailViewAlert(final int id, final int position, final ListView managerListView, final ManagerManager managerManager, final ListAdapter clientTrashcansAdatper ){
        final View view = View.inflate(managerManager.context, R.layout.alert_trashcan_issue, null);
        final TextView tvPlaceName = (TextView)view.findViewById(R.id.tv_place_name);
        final TextView tvName = (TextView) view.findViewById(R.id.tv_name);
        final TextView tvIssue = (TextView) view.findViewById(R.id.tv_issue);
        final TextView tvEmail = (TextView) view.findViewById(R.id.tv_email);



        tvName.setText(Trashcans.getTrashcans().get(position).getName());
        tvIssue.setText(Trashcans.getTrashcans().get(position).getIssue());
        tvEmail.setText(Trashcans.getTrashcans().get(position).getEmail());
        tvPlaceName.setText(Trashcans.getTrashcans().get(position).getPlace_name());

        //add link

        tvEmail.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Intent i = new Intent(managerManager.context, UserInfoActivity.class);
                UserInfoData.setEmail(Trashcans.getTrashcans().get(position).getEmail());
                managerManager.context.startActivity(i);
                return false;
            }
        });



        AlertDialog.Builder alertDialog = new AlertDialog.Builder(managerManager.context, R.style.AlertDialogTheme);
        alertDialog.setTitle("Probleem");
        alertDialog.setMessage("Probleemi kirjeldus");
        alertDialog.setView(view);


        alertDialog.setPositiveButton("Probleem lahendatud", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                managerManager.deleteTrashcanIssue(id, position, managerListView, clientTrashcansAdatper);
            }
        });
        alertDialog.setNegativeButton("Tühista", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alertDialog.setCancelable(false);
        alertDialog.show();
    }

    private static void setIssueAddress(ManagerManager managerManager, int position, TextView txtAddress) {
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(managerManager.context, Locale.getDefault());

        Trashcan trashcan = Trashcans.getTrashcans().get(position);
        try {
            addresses = geocoder.getFromLocation(trashcan.getLatitude(), trashcan.getLongitude(), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            String city = addresses.get(0).getLocality();
            String state = addresses.get(0).getAdminArea();
            String country = addresses.get(0).getCountryName();
            String postalCode = addresses.get(0).getPostalCode();
            String knownName = addresses.get(0).getFeatureName(); // Only if available else return NULL
            txtAddress.setText(address);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void cannotDeleteTrashcan(final int id, final TrashcanLocationManager trashcanLocationManager) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(trashcanLocationManager.context, R.style.AlertDialogTheme);
        alertDialog.setTitle("Konteineri tühjendamine");
        alertDialog.setMessage("Konteineri kustutamine ebaõnnestus!\nKas soovite uuesti proovida?");
        alertDialog.setPositiveButton("Jah", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                trashcanLocationManager.deleteTrashcan(id);
            }
        });
        alertDialog.setNegativeButton("Ei", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alertDialog.setCancelable(false);
        alertDialog.show();
    }

    public static void deleteTrashcanLogAlert(final int id, final int position, final ListView managerListView, final ClientManager clientManager, final ListAdapter clientTrashcansAdatper){
        final View view = View.inflate(clientManager.context, R.layout.alert_client_activity_info, null);
        final TextView tvIssue = (TextView) view.findViewById(R.id.tv_issue);
        final TextView tvPlaceName = (TextView)view.findViewById(R.id.tv_place_name);
        final TextView tvIssueDescription = (TextView)view.findViewById(R.id.tv_issue_description);
        final TextView tvEmail = (TextView)view.findViewById(R.id.tv_email);

        tvEmail.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                tvEmail.setTextColor(Color.GRAY);
                Intent i = new Intent(clientManager.context, MyUserActivity.class);
                clientManager.activity.startActivity(i);
                return false;
            }
        });




        int idTest = id;
        if(Trashcans.getTrashcans().get(position).getIssue().equals("")){
            tvIssueDescription.setText("");
            tvIssue.setText("Probleemid äraveoga puuduvad");
        }else {
            tvIssue.setText(Trashcans.getTrashcans().get(position).getIssue());
        }
        tvPlaceName.setText(Trashcans.getTrashcans().get(position).getPlace_name());



        AlertDialog.Builder alertDialog = new AlertDialog.Builder(clientManager.context, R.style.AlertDialogTheme);
        alertDialog.setTitle("Tellimus");
        alertDialog.setMessage("Detailne kirjeldus");
        alertDialog.setView(view);


        alertDialog.setPositiveButton("Tühista tellimus", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                clientManager.deleteClientTrashcan(id, position, clientTrashcansAdatper, managerListView);
            }
        });
        alertDialog.setNegativeButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alertDialog.setCancelable(false);
        alertDialog.show();
    }


    public static void info(Context context, String title, String message) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context, R.style.AlertDialogTheme);
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alertDialog.setCancelable(false);
        alertDialog.show();
    }

    public static AlertDialog.Builder getCannotGetContainersAlert() {
        return cannotGetContainersAlert;
    }

    public static void setCannotGetContainersAlert(AlertDialog.Builder cannotGetContainersAlert) {
        Alert.cannotGetContainersAlert = cannotGetContainersAlert;
    }

    public static AlertDialog.Builder getCannotGetGPSAlert() {
        return cannotGetGPSAlert;
    }

    public static void setCannotTurnOnGPSAlert(AlertDialog.Builder cannotGetGPSAlert) {
        Alert.cannotGetGPSAlert = cannotGetGPSAlert;
    }

    public static void setCannotTurnOnDataConnectionAlert(AlertDialog.Builder cannotTurnOnDataConnectionAlert) {
        Alert.cannotTurnOnDataConnectionAlert = cannotTurnOnDataConnectionAlert;
    }

    public static AlertDialog.Builder getCannotTurnOnDataConnectionAlert() {
        return cannotTurnOnDataConnectionAlert;
    }

    public static AlertDialog.Builder getDeleteTrashcan() {
        return deleteTrashcan;
    }

    public static void setDeleteTrashcan(AlertDialog.Builder deleteTrashcan) {
        Alert.deleteTrashcan = deleteTrashcan;
    }

    public static AlertDialog.Builder getCannotGetTokenAlert() {
        return cannotGetTokenAlert;
    }

    public static void setCannotGetTokenAlert(AlertDialog.Builder cannotGetTokenAlert) {
        Alert.cannotGetTokenAlert = cannotGetTokenAlert;
    }
}
