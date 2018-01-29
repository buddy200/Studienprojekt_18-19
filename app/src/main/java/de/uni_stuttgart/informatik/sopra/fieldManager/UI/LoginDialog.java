package de.uni_stuttgart.informatik.sopra.fieldManager.UI;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.nio.charset.StandardCharsets;

import de.uni_stuttgart.informatik.sopra.fieldManager.FragmentInteractionListener;
import de.uni_stuttgart.informatik.sopra.fieldManager.GlobalConstants;
import de.uni_stuttgart.informatik.sopra.fieldManager.R;
import de.uni_stuttgart.informatik.sopra.fieldManager.Util.SimpleCrypto;
import de.uni_stuttgart.informatik.sopra.fieldManager.data.managers.AppDataManager;

/**
 * sopra_priv
 * Created by Felix B on 10.01.18.
 * Mail: felix.burk@gmail.com
 */

public class LoginDialog extends Dialog implements android.view.View.OnClickListener {
    private static final String TAG = "LoginDialog";
    private EditText username;
    private EditText password;
    private Button loginBtn;
    private RadioGroup userPrivileges;
    private FragmentInteractionListener mListener;
    private Context context;
    private AppDataManager dataManager;

    private static final int RB_ADMIN_ID = 1000;//first radio button id
    private static final int RB_FARMER_ID = 1001;//second radio button id

    public LoginDialog(@NonNull Context context, AppDataManager dataManager) {
        super(context, R.style.Login_Dialog);

        this.dataManager = dataManager;
        this.context = context;
        //prevent user from cancelling
        this.setCanceledOnTouchOutside(false);
        this.setCancelable(false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_login);
        username = findViewById(R.id.login_dialog_editText_username);
        password = findViewById(R.id.login_dialog_editText_password);
        loginBtn = findViewById(R.id.login_dialog_btn_login);
        userPrivileges = findViewById(R.id.login_dialog_usr_privileges);

        RadioButton admRadio = findViewById(R.id.setAdmin);
        RadioButton farmerRadio = findViewById(R.id.setFarmer);
        admRadio.setId(RB_ADMIN_ID);
        farmerRadio.setId(RB_FARMER_ID);

        loginBtn.setOnClickListener(this);
        if (context instanceof FragmentInteractionListener) {
            mListener = (FragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement FragmentInteractionListener");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_dialog_btn_login:
                String usr = username.getText().toString();
                String pw = password.getText().toString();

                if(!usr.equals("") &&  !pw.equals("") && userPrivileges.getCheckedRadioButtonId() != -1
                        && dataManager.checkLogin(usr, sha(pw))){

                    saveLogin(usr, sha(pw), userPrivileges.getCheckedRadioButtonId());

                    this.dismiss();

                }else{
                    Animation shake = AnimationUtils.loadAnimation(getContext(), R.anim.shake);

                    //start shake animation on the missing view
                    if(username.getText().toString().equals("")) username.startAnimation(shake);
                    if(userPrivileges.getCheckedRadioButtonId() == -1) userPrivileges.startAnimation(shake);
                    if(password.getText().toString().equals("")) password.startAnimation(shake);

                    if(!dataManager.checkLogin(usr, sha(pw))){
                        username.startAnimation(shake);
                        password.startAnimation(shake);
                        TextInputLayout pass = (TextInputLayout) findViewById(R.id.etPasswordLayout);
                        TextInputLayout usrInput = (TextInputLayout) findViewById(R.id.etUsernameLayout);
                        usrInput.setError("wrong username");
                        pass.setError("wrong password");
                    }
                }
                break;
        }
    }

    /**
     * save login to shared preferences
     */
    public void saveLogin(String username, String password, int radioButtonId){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        SharedPreferences.Editor edit = prefs.edit();
        edit.putBoolean(getContext().getString(R.string.pref_previously_started), Boolean.TRUE);
        edit.putString(getContext().getString(R.string.pref_username), username);

        if(radioButtonId == RB_ADMIN_ID ){
            if(!password.equals("292C9515973C024858D535B4DB786ACB") && !username.equals("Adm")){
                Toast.makeText(getContext(), R.string.no_privileges, Toast.LENGTH_LONG).show();
            }else {
                edit.putBoolean(getContext().getString(R.string.pref_admin_bool), Boolean.TRUE);
                GlobalConstants.isAdmin = true;
            }
        }else {
            edit.putBoolean(getContext().getString(R.string.pref_admin_bool), Boolean.FALSE);
            GlobalConstants.isAdmin = false;
        }

        edit.apply();
        mListener.onFragmentMessage(TAG, "complete", null );
    }

    private String sha(String password){

        byte[] b = {
                103, 32, 56, 99, 116, 97, 51, 57, 57, 69, 88, 121, 50, 81, 65, 106, 109, 52, 69, 77, 120,
                114, 32, 109, 109, 67, 112, 66, 88, 112, 107, 50, 97, 116, 102, 102, 73, 73, 53, 65, 72,
                56, 56, 32, 77, 118, 119, 79, 89, 49, 75, 80, 50, 78, 68, 70, 111, 54, 70, 50, 57, 83, 54,
                109, 32, 118, 86, 85, 65, 66, 121, 79, 113, 111, 121, 89, 117, 114, 108, 71, 111, 52, 102,
                97, 79, 32, 107, 71, 112, 76, 74, 56, 83, 48, 51, 88, 51, 54, 101, 75, 71, 48, 113, 121,
                120, 89, 32, 83, 75, 55, 109, 52, 76, 116, 56, 54, 54, 100, 108, 65, 56, 67, 48, 71, 76, 73,
                57, 32, 73, 97, 80, 65, 118, 78, 54, 84, 85, 49, 87, 105, 55, 77, 116, 103, 82, 75, 71, 67,
                32, 75, 87, 121, 122, 114, 104, 117, 86, 119, 74, 112, 116, 106, 54, 89, 49, 68, 121, 71,
                110, 32, 99, 70, 97, 72, 102, 100, 103, 55, 52, 90, 75, 72, 86, 108, 70, 79, 88, 97, 112, 68
        };

        String str = new String(b, StandardCharsets.UTF_8);
        String s = "";

        try {
            s = SimpleCrypto.encrypt(str, password);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return s;
    }
}
