package utils.myuser;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.jaredrummler.materialspinner.MaterialSpinner;

import java.util.regex.Pattern;

import constant.PrefNames;
import utils.login.LoginManager;
import www.ragnsells.ee.ragnsells.R;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Andreas on 09.05.2017.
 */

public class EditUserAlerts {


    private static SharedPreferences tokenSharedPreferences;

    public static void editName(Activity activity, final Context context, final EditUserManager editUserManager, final TextView tvOldName){
        final View view = View.inflate(context, R.layout.alert_edit_user_name, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AlertDialogTheme);
        builder.setTitle("Nimi");
        builder.setView(view);
        final EditText etName = (EditText)view.findViewById(R.id.et_user_name);
        final TextView tvName = (TextView)activity.findViewById(R.id.tv_name);
        etName.setText(tvOldName.getText().toString());



        builder.setPositiveButton("Muuda", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(isUserNameEditValid(context, etName)){
                    editUserManager.updateName(etName, tvName);
                }
            }
        });
        builder.setNegativeButton("Tühista", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });



        builder.show();
    }

    private static boolean isUserNameEditValid(Context context, EditText etUserName){
        boolean isValid = true;
        Pattern namePattern = Pattern.compile("^[a-zA-Z\\s]+");
        String userName = etUserName.getText().toString();
        if(userName.equals("")){
            Toast.makeText(context, "Palun sisetsage ees ja perekonnanimi", Toast.LENGTH_LONG).show();
            isValid = false;
        }
        if(!namePattern.matcher(userName).matches()){
            Toast.makeText(context, "Palun sisetsage ees ja perekonnanimi", Toast.LENGTH_LONG).show();
            isValid = false;
        }
        return isValid;
    }


    public static void editPhoneNr(Activity activity, final Context context, final EditUserManager editUserManager, TextView tvPhoneOld) {
        final View view = View.inflate(context, R.layout.alert_edit_user_phone_nr, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AlertDialogTheme);

        final EditText etUserPhone = (EditText)view.findViewById(R.id.et_user_phone);
        final TextView tvUserPhone = (TextView)activity.findViewById(R.id.tv_phone);
        etUserPhone.setText(tvPhoneOld.getText().toString());

        builder.setTitle("Telefoni number");
        builder.setView(view);

        builder.setPositiveButton("Muuda", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(isPhoneNrEditValid(context, etUserPhone)){
                    editUserManager.updatePhoneNr(etUserPhone, tvUserPhone);
                }

            }
        });
        builder.setNegativeButton("Tühista", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }

    private static boolean isPhoneNrEditValid(Context context, EditText etUserPhone){
        boolean isValid = true;
        String phone = etUserPhone.getText().toString();
        if(phone.length() < 5){
            etUserPhone.setError("Number peab olema pikem kui 5 ühikut");
            isValid = false;
        }
        return isValid;
    }

    public static void editPassword(Activity activity, final Context context, final EditUserManager editUserManager) {
        final View view = View.inflate(context, R.layout.alert_user_password, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AlertDialogTheme);

        final EditText etOldPassword = (EditText)view.findViewById(R.id.et_old_password);
        final EditText etNewPassword = (EditText)view.findViewById(R.id.et_new_password);
        final EditText etNewPasswordRepeat = (EditText)view.findViewById(R.id.et_new_password_repeat);

        builder.setTitle("Parool");
        builder.setView(view);

        builder.setPositiveButton("Muuda", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(isPasswordValid(context, etOldPassword, etNewPassword, etNewPasswordRepeat)){
                    editUserManager.updatePassword(etNewPassword);
                }

            }
        });
        builder.setNegativeButton("Tühista", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    private static boolean isPasswordValid(Context context, EditText etOldPassword, EditText etNewPassword, EditText etNewPasswordRepeat){
        boolean isValid = true;
        String oldPassword = etOldPassword.getText().toString();
        String newPassword = etNewPassword.getText().toString();
        String newPasswordRepeat = etNewPasswordRepeat.getText().toString();
        String currentPassword = LoginManager.retrievePassword(context);
        if(!oldPassword.equals(currentPassword)){
            Toast.makeText(context, "Teie vana parool on vale", Toast.LENGTH_LONG).show();
            isValid = false;
        }
        if(!newPassword.equals(newPasswordRepeat)){
            Toast.makeText(context, "Teie uus parool peab kattuma", Toast.LENGTH_LONG).show();
            isValid = false;
        }
        if(oldPassword.equals(newPassword)){
            Toast.makeText(context, "Teie parool ei tohi vana parooliga olla sama", Toast.LENGTH_LONG).show();
            isValid = false;
        }
        if(newPassword.length()<5 && newPasswordRepeat.length() < 5){
            Toast.makeText(context, "Teie parool peab olema vähemalt 6 tähemärki pikk", Toast.LENGTH_LONG).show();
            isValid = false;
        }
        return isValid;
    }

    public static void editRole(Activity activity, Context context, final EditUserManager editUserManager, TextView tvOldRole) {
        final View view = View.inflate(context, R.layout.alert_edit_user_role, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AlertDialogTheme);
        final MaterialSpinner msUserRole = (MaterialSpinner)view.findViewById(R.id.ms_edit_user_role);
        final TextView tvRole = (TextView)activity.findViewById(R.id.tv_role);
        msUserRole.setItems("Klient", "Vedaja", "Mänedžer");
        if(tvOldRole.getText().toString().equals("Klient")){
            msUserRole.setSelectedIndex(0);
        }
        else if(tvOldRole.getText().toString().equals("Vedaja")){
            msUserRole.setSelectedIndex(1);
        }else{
            msUserRole.setSelectedIndex(2);
        }
        builder.setTitle("Roll");
        builder.setView(view);

        builder.setPositiveButton("Muuda", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                editUserManager.updateRole(msUserRole, tvRole);
            }
        });
        builder.setNegativeButton("Tühista", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    public static void deleteUser(Activity activity, final Context context, final EditUserManager editUserManager) {
        final View view = View.inflate(context, R.layout.alert_edit_user_delete_user, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AlertDialogTheme);

        final EditText etOldPassword = (EditText)view.findViewById(R.id.et_user_password);

        builder.setTitle("Kustatge kasutaja");
        builder.setView(view);

        builder.setPositiveButton("Kustuta", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                tokenSharedPreferences = context.getSharedPreferences(PrefNames.TOKEN, MODE_PRIVATE);
                if(isUserValid(context, etOldPassword)){
                    editUserManager.deleteUser(tokenSharedPreferences.getInt(UserSharedPrefConstants.SESSION_ID, 0));
                }

            }
        });
        builder.setNegativeButton("Tühista", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    private static boolean isUserValid(Context context, EditText etOldPassword) {
        boolean isValid = true;
        String retrevedPassword = LoginManager.retrievePassword(context);
        if (!etOldPassword.getText().toString().equals(retrevedPassword)){
            Toast.makeText(context, "Sisestatud parool on vale", Toast.LENGTH_LONG).show();
            isValid = false;
        }
        return  isValid;
    }
}
