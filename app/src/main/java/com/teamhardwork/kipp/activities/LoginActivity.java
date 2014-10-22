package com.teamhardwork.kipp.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.teamhardwork.kipp.KippApplication;
import com.teamhardwork.kipp.R;
import com.teamhardwork.kipp.utilities.NetworkUtils;

public class LoginActivity extends Activity {
    private EditText etUsername;
    private EditText etPassword;
    private Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        setupViews();
        Parse.initialize(this, KippApplication.PARSE_APPLICATION_ID, KippApplication.PARSE_CLIENT_KEY);
    }

    private void setupViews() {
        etUsername = (EditText) findViewById(R.id.etUsername);
        etPassword = (EditText) findViewById(R.id.etPassword);
        btnLogin = (Button) findViewById(R.id.btnLogin);

        if (ParseUser.getCurrentUser() != null) {
            onLoginSuccess();
        }

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (NetworkUtils.isNetworkAvailable(LoginActivity.this) == false) {
                    Toast.makeText(LoginActivity.this, "network not available, try again later", Toast.LENGTH_SHORT).show();
                    return;
                }
                ParseUser.logInInBackground(etUsername.getText().toString(), etPassword.getText().toString(), new LogInCallback() {
                    public void done(ParseUser user, ParseException e) {
                        if (user != null) {
                            onLoginSuccess();
                        } else {
                            Toast.makeText(LoginActivity.this, "Failed to log in!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    void onLoginSuccess() {
        ((KippApplication) getApplication()).setTeacher();
        ((KippApplication) getApplication()).setSchoolClass();
        Intent i = new Intent(this, RosterActivity.class);
        startActivity(i);
    }
}
