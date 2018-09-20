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
        final EditText etName = (EditText)view.findViewById(R.id.et_user_name);
        final TextView tvName = (TextView)activity.findViewById(R.id.tv_name);
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AlertDialogTheme);

        builder.setTitle(context.getResources().getString(R.string.name));
        builder.setView(view);
        etName.setText(tvOldName.getText().toString());
        builder.setPositiveButton(context.getResources().getString(R.string.change), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(isUserNameEditValid(context, etName)){
                    editUserManager.updateName(etName, tvName);
                }
            }
        });
        builder.setNegativeButton(context.getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
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
            Toast.makeText(context, context.getString(R.string.entr_fst_and_lstname), Toast.LENGTH_LONG).show();
            isValid = false;
        }
        if(!namePattern.matcher(userName).matches()){
            Toast.makeText(context, R.string.entr_fst_and_lstname, Toast.LENGTH_LONG).show();
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
        builder.setTitle(context.getResources().getString(R.string.phone_number));
        builder.setView(view);
        builder.setPositiveButton(context.getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(isPhoneNrEditValid(context, etUserPhone)){
                    editUserManager.updatePhoneNr(etUserPhone, tvUserPhone);
                }

            }
        });
        builder.setNegativeButton(context.getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
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
            etUserPhone.setError(context.getResources().getString(R.string.nmber_must_be_more_than_5_units));
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

        builder.setTitle(context.getResources().getString(R.string.password));
        builder.setView(view);

        builder.setPositiveButton(context.getResources().getString(R.string.change), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(isPasswordValid(context, etOldPassword, etNewPassword, etNewPasswordRepeat)){
                    editUserManager.updatePassword(etNewPassword);
                }

            }
        });
        builder.setNegativeButton(context.getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
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
            Toast.makeText(context, context.getResources().getString(R.string.your_old_password_is_wrong), Toast.LENGTH_LONG).show();
            isValid = false;
        }
        if(!newPassword.equals(newPasswordRepeat)){
            Toast.makeText(context, context.getResources().getString(R.string.your_password_must_match) , Toast.LENGTH_LONG).show();
            isValid = false;
        }
        if(oldPassword.equals(newPassword)){
            Toast.makeText(context, context.getResources().getString(R.string.your_new_password_must_not_be_equal_with_old_password), Toast.LENGTH_LONG).show();
            isValid = false;
        }
        if(newPassword.length()<5 && newPasswordRepeat.length() < 5){
            Toast.makeText(context,  context.getResources().getString(R.string.your_new_password_must_be_at_least_6_chars_long), Toast.LENGTH_LONG).show();
            isValid = false;
        }
        return isValid;
    }

    public static void editRole(Activity activity, Context context, final EditUserManager editUserManager, TextView tvOldRole) {
        final View view = View.inflate(context, R.layout.alert_edit_user_role, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AlertDialogTheme);
        final MaterialSpinner msUserRole = (MaterialSpinner)view.findViewById(R.id.ms_edit_user_role);
        final TextView tvRole = (TextView)activity.findViewById(R.id.tv_role);
        msUserRole.setItems(context.getResources().getString(R.string.client), context.getResources().getString(R.string.carrier), context.getResources().getString(R.string.manager));
        if(tvOldRole.getText().toString().equals(context.getResources().getString(R.string.client))){
            msUserRole.setSelectedIndex(0);
        }
        else if(tvOldRole.getText().toString().equals(context.getResources().getString(R.string.carrier))){
            msUserRole.setSelectedIndex(1);
        }else{
            msUserRole.setSelectedIndex(2);
        }
        builder.setTitle(context.getResources().getString(R.string.role));
        builder.setView(view);

        builder.setPositiveButton(context.getResources().getString(R.string.change), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                editUserManager.updateRole(msUserRole, tvRole);
            }
        });
        builder.setNegativeButton(context.getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
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

        builder.setTitle(context.getResources().getString(R.string.delete_user));
        builder.setView(view);

        builder.setPositiveButton(context.getResources().getString(R.string.delete), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                tokenSharedPreferences = context.getSharedPreferences(PrefNames.TOKEN, MODE_PRIVATE);
                if(isUserValid(context, etOldPassword)){
                    editUserManager.deleteUser(tokenSharedPreferences.getInt(UserSharedPrefConstants.SESSION_ID, 0));
                }

            }
        });
        builder.setNegativeButton(context.getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
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
            Toast.makeText(context, context.getResources().getString(R.string.entered_password_is_wrong), Toast.LENGTH_LONG).show();
            isValid = false;
        }
        return  isValid;
    }
}
