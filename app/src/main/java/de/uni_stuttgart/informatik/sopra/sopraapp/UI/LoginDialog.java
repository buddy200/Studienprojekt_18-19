package de.uni_stuttgart.informatik.sopra.sopraapp.UI;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import de.uni_stuttgart.informatik.sopra.sopraapp.GlobalConstants;
import de.uni_stuttgart.informatik.sopra.sopraapp.R;

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

    private static final int RB_ADMIN_ID = 1000;//first radio button id
    private static final int RB_FARMER_ID = 1001;//second radio button id

    public LoginDialog(@NonNull Context context) {
        super(context, R.style.Login_Dialog);

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
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_dialog_btn_login:
                if(!username.getText().toString().equals("") && userPrivileges.getCheckedRadioButtonId() != -1){
                    saveLogin(username.getText().toString(), password.getText().toString(), userPrivileges.getCheckedRadioButtonId());
                    Toast.makeText(getContext(), "Logged in as " + username.getText().toString(), Toast.LENGTH_SHORT);
                    this.dismiss();

                }else{
                    Animation shake = AnimationUtils.loadAnimation(getContext(), R.anim.shake);

                    //start shake animation on the missing view
                    if(username.getText().toString().equals("")) username.startAnimation(shake);
                    if(userPrivileges.getCheckedRadioButtonId() == -1) userPrivileges.startAnimation(shake);
                    if(password.getText().toString().equals("")) password.startAnimation(shake);

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

        Log.e(TAG, "checked radio button: " + radioButtonId);
        if(radioButtonId == RB_ADMIN_ID){
            edit.putBoolean(getContext().getString(R.string.pref_admin_bool), Boolean.TRUE);
            GlobalConstants.isAdmin = true;
            Log.e(TAG, "whdioahdo: " + GlobalConstants.isAdmin);
        }else {
            edit.putBoolean(getContext().getString(R.string.pref_admin_bool), Boolean.FALSE);
            GlobalConstants.isAdmin = false;
            Log.e(TAG, "whdioahdo: " + GlobalConstants.isAdmin);
        }

        //password not saved yet.. would be unsave

        edit.apply();
    }


}
