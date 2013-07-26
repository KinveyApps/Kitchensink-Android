/** 
 * Copyright (c) 2013, Kinvey, Inc. All rights reserved.
 *
 * This software contains valuable confidential and proprietary information of
 * KINVEY, INC and is subject to applicable licensing agreements.
 * Unauthorized reproduction, transmission or distribution of this file and its
 * contents is a violation of applicable laws.
 * 
 */
package com.kinvey.sample.kitchensink.account;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;

import com.kinvey.android.Client;
import com.kinvey.android.callback.KinveyUserCallback;
import com.kinvey.java.User;
import com.kinvey.sample.kitchensink.*;

/**
 * @author edwardf
 * @since 2.0
 */
public class LoginActivity extends Activity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private EditText eUserName;
    private EditText ePassword;
    private Button bLogin;
    private CheckBox cImplicit;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        bindViews();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    private void bindViews() {
        eUserName = (EditText) findViewById(R.id.login_user_name);
        ePassword = (EditText) findViewById(R.id.login_user_password);
        bLogin = (Button) findViewById(R.id.login_button);
        cImplicit = (CheckBox) findViewById(R.id.login_anon_checkbox);

        bLogin.setOnClickListener(this);
        cImplicit.setOnCheckedChangeListener(this);

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        eUserName.setText("");
        eUserName.setEnabled(!isChecked);
        ePassword.setText("");
        ePassword.setEnabled(!isChecked);

    }

    @Override
    public void onClick(View v) {

        if (!isUserLoggedIn()) {

            if (cImplicit.isChecked()) {
                getClient().user().login(new KinveyUserCallback() {
                    @Override
                    public void onSuccess(User result) {
                        Intent feature = new Intent(LoginActivity.this, KitchenSink.class);
                        startActivity(feature);
                    }

                    @Override
                    public void onFailure(Throwable error) {
                        AndroidUtil.toast(LoginActivity.this, "couldn't login -> " + error.getMessage());
                    }

                });

            } else {
                getClient().user().login(eUserName.getText().toString(), ePassword.getText().toString(), new KinveyUserCallback() {
                    @Override
                    public void onSuccess(User result) {
                        Intent feature = new Intent(LoginActivity.this, KitchenSink.class);
                        startActivity(feature);
                    }

                    @Override
                    public void onFailure(Throwable error) {
                        AndroidUtil.toast(LoginActivity.this, "couldn't login -> " + error.getMessage());
                    }
                });

            }
        }

    }

    private boolean isUserLoggedIn() {
        return getClient().user().isUserLoggedIn();
    }

    private Client getClient() {
        return ((KitchenSinkApplication) getApplicationContext()).getClient();
    }
}
