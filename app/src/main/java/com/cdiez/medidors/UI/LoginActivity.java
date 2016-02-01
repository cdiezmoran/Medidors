package com.cdiez.medidors.UI;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.TextView;

import com.cdiez.medidors.Other.ParseConstants;
import com.cdiez.medidors.R;
import com.parse.GetCallback;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity {

    @Bind(R.id.email_field) EditText mEmailField;
    @Bind(R.id.password_field) EditText mPasswordField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.login_button)
    public void onLoginClick() {
        String email = mEmailField.getText().toString().trim();
        final String password = mPasswordField.getText().toString().trim();

        mEmailField.setError(null);
        mPasswordField.setError(null);

        if (!isEmailValid(email)) {
            mEmailField.setError(getResources().getString(R.string.error_invalid_email));
            mEmailField.requestFocus();
            return;
        }

        if (!isPasswordValid(password)) {
            mPasswordField.setError(getResources().getString(R.string.error_invalid_password));
            mPasswordField.requestFocus();
            return;
        }

        if (!email.contains("@")) {
            ParseUser.logInInBackground(email, password, new LogInCallback() {
                @Override
                public void done(ParseUser parseUser, ParseException e) {
                    isValidUser(parseUser);
                }
            });
        }
        else {
            ParseQuery<ParseUser> userQuery = ParseUser.getQuery();
            userQuery.whereEqualTo(ParseConstants.KEY_EMAIL, email);
            userQuery.getFirstInBackground(new GetCallback<ParseUser>() {
                @Override
                public void done(ParseUser object, ParseException e) {
                    String emailUsername = object.getUsername();
                    ParseUser.logInInBackground(emailUsername, password, new LogInCallback() {
                        @Override
                        public void done(ParseUser user, ParseException e) {
                            isValidUser(user);
                        }
                    });
                }
            });
        }
    }

    private void isValidUser(ParseUser parseUser) {
        if (parseUser != null) {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this, R.style.AppCompatAlertDialogStyle);
            builder.setTitle("Invalid credentials!");
            builder.setPositiveButton("OK", null);
            builder.setMessage("Please make sure your email and password are correct!");
            builder.show();
        }
    }

    public boolean isEmailValid(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public boolean isPasswordValid(String password) {
        return password.length() > 3;
    }

    @OnClick(R.id.sign_up_button)
    public void onSignUpClick() {
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
    }
}