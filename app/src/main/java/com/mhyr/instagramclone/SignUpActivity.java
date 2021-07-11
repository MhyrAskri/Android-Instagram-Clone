package com.mhyr.instagramclone;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    // Ui Component
    private EditText edtSignUpEmail , edtSignUpUsername , edtSignUpPassword;
    private Button btnSignUp;
    private TextView txtHaveAnAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setTitle("Sign Up");

        edtSignUpEmail = findViewById(R.id.edtSignUpEmail);
        edtSignUpUsername = findViewById(R.id.edtSignUpUsername);
        edtSignUpPassword = findViewById(R.id.edtSignUpPassword);
        btnSignUp = findViewById(R.id.btnSignUp);
        txtHaveAnAccount = findViewById(R.id.txtHaveAnAccount);

        edtSignUpPassword.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {

                if (keyCode == KeyEvent.KEYCODE_ENTER && keyEvent.getAction() == KeyEvent.ACTION_DOWN) {

                    onClick(btnSignUp);

                }
                return false;
            }
        });

        btnSignUp.setOnClickListener(this);
        txtHaveAnAccount.setOnClickListener(this);

        if (ParseUser.getCurrentUser() != null) {

//            ParseUser.logOut();
            transitionToSocialMediaActivity();

        }

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){

            case R.id.btnSignUp:

                try {

                    if (edtSignUpEmail.getText().toString().equals("") ||
                            edtSignUpUsername.getText().toString().equals("") ||
                            edtSignUpPassword.getText().toString().equals("")) {

                        FancyToast.makeText(SignUpActivity.this,
                                "Email , Username & Password are required!",
                                Toast.LENGTH_SHORT, FancyToast.INFO,
                                false).show();

                    } else {

                        final ParseUser appUser = new ParseUser();
                        appUser.setEmail(edtSignUpEmail.getText().toString());
                        appUser.setUsername(edtSignUpUsername.getText().toString());
                        appUser.setPassword(edtSignUpPassword.getText().toString());

                        ProgressDialog progressDialog = new ProgressDialog(this);
                        progressDialog.setMessage("Signing up " + edtSignUpUsername.getText().toString());
                        progressDialog.show();

                        appUser.signUpInBackground(new SignUpCallback() {
                            @Override
                            public void done(ParseException e) {

                                if (e == null) {

                                    FancyToast.makeText(SignUpActivity.this,
                                            appUser.getUsername() + " is Signed up",
                                            Toast.LENGTH_SHORT, FancyToast.SUCCESS,
                                            false).show();

                                    transitionToSocialMediaActivity();

                                } else {

                                    FancyToast.makeText(SignUpActivity.this,
                                            "There was an error: " + e.getMessage(),
                                            Toast.LENGTH_SHORT, FancyToast.ERROR,
                                            false).show();

                                }

                                progressDialog.dismiss();

                            }
                        });

                    }
                } catch (Exception e) {

                    e.printStackTrace();

                }

                break;

            case R.id.txtHaveAnAccount:

                Intent intent = new Intent(SignUpActivity.this , LoginActivity.class);
                startActivity(intent);

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

        Intent intent = new Intent(SignUpActivity.this , SocialMediaActivity.class);
        startActivity(intent);
        finish();

    }

}