package com.mhyr.instagramclone;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.shashank.sony.fancytoastlib.FancyToast;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    // Ui Component
    private EditText edtLoginEmail , edtLoginPassword;
    private Button btnLogin;
    private TextView txtCreateAnAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setTitle("Log In");

        edtLoginEmail = findViewById(R.id.edtLoginEmail);
        edtLoginPassword = findViewById(R.id.edtLoginPassword);
        btnLogin = findViewById(R.id.btnLogin);
        txtCreateAnAccount = findViewById(R.id.txtCreateAnAccount);

        edtLoginPassword.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {

                if (keyCode == KeyEvent.KEYCODE_ENTER && keyEvent.getAction() == KeyEvent.ACTION_DOWN) {

                    onClick(btnLogin);

                }
                return false;
            }
        });

        btnLogin.setOnClickListener(this);
        txtCreateAnAccount.setOnClickListener(this);

        if (ParseUser.getCurrentUser() != null) {

            ParseUser.logOut();

        }

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.btnLogin:

                try {

                    if (edtLoginEmail.getText().toString().equals("") ||
                            edtLoginPassword.getText().toString().equals("")) {

                        FancyToast.makeText(LoginActivity.this,
                                "Email & Password are required!",
                                Toast.LENGTH_SHORT, FancyToast.INFO,
                                false).show();

                    } else {

                        ParseUser.logInInBackground(edtLoginEmail.getText().toString(),
                                edtLoginPassword.getText().toString(),
                                new LogInCallback() {
                                    @Override
                                    public void done(ParseUser user, ParseException e) {

                                        if (user != null && e == null) {

                                            FancyToast.makeText(LoginActivity.this,
                                                    user.getUsername() + " is Logged in",
                                                    Toast.LENGTH_SHORT, FancyToast.SUCCESS,
                                                    false).show();

                                            transitionToSocialMediaActivity();

                                        } else {

                                            FancyToast.makeText(LoginActivity.this,
                                                    "There was an error: " + e.getMessage(),
                                                    Toast.LENGTH_SHORT, FancyToast.ERROR,
                                                    false).show();

                                        }

                                    }
                                });
                    }
                } catch (Exception e) {

                    e.printStackTrace();

                }

                break;

            case R.id.txtCreateAnAccount:

                finish();

                break;

        }

    }

    public void rootLayoutTapped(View view) {

        try {

            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken() , 0);

        } catch (Exception e) {

            e.printStackTrace();

        }

    }

    private void transitionToSocialMediaActivity() {

        Intent intent = new Intent(LoginActivity.this , SocialMediaActivity.class);
        startActivity(intent);
        finish();

    }

}