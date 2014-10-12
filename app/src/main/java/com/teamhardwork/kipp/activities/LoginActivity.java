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

public class LoginActivity extends Activity {
    private EditText etUsername;
    private EditText etPassword;
    private Button btnLogin;
    private ParseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Parse.initialize(this, KippApplication.PARSE_APPLICATION_ID, KippApplication.PARSE_CLIENT_KEY);

        setupViews();
    }

    private void setupViews() {
        etUsername = (EditText) findViewById(R.id.etUsername);
        etPassword = (EditText) findViewById(R.id.etPassword);
        btnLogin = (Button) findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ParseUser.logInInBackground(etUsername.getText().toString(), etPassword.getText().toString(), new LogInCallback() {
                    public void done(ParseUser user, ParseException e) {
                        if (user != null) {
                            currentUser = user;
                            onLoginSuccess(currentUser);
                        } else {
                            Toast.makeText(LoginActivity.this, "Failed to log in!", Toast.LENGTH_SHORT).show();
                            if (e != null) {
                                e.printStackTrace();
                            }
                        }
                    }
                });

            }
        });
    }

    public void onLoginSuccess(ParseUser user) {
        Intent i = new Intent(this, MainActivity.class);
        i.putExtra("user", user.getUsername());
        i.putExtra("email", user.getEmail());
        i.putExtra("session", user.getSessionToken());
        startActivity(i);
    }
}
