package com.cdiez.medidors.UI;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;

import com.cdiez.medidors.R;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SignUpActivity extends AppCompatActivity {

    @Bind(R.id.email_field) EditText mEmailField;
    @Bind(R.id.password_field) EditText mPasswordField;
    @Bind(R.id.password_confirm_field) EditText mConfirmPasswordField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.sign_up_button)
    public void onClickSignUp() {
        String email = mEmailField.getText().toString().trim();
        String password = mPasswordField.getText().toString().trim();
        String confirmPassword = mConfirmPasswordField.getText().toString().trim();

        mEmailField.setError(null);
        mPasswordField.setError(null);
        mConfirmPasswordField.setError(null);

        if (!doChecks(email, password, confirmPassword)) {
            return;
        }

        String emailParts[] = email.split("@"); //Split the string at the @ to get username

        ParseUser newUser = new ParseUser();
        newUser.setUsername(emailParts[0]);
        newUser.setEmail(email);
        newUser.setPassword(password);

        newUser.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
                else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(SignUpActivity.this, R.style.AppCompatAlertDialogStyle);
                    builder.setTitle("Algo salio mal!");
                    builder.setPositiveButton("OK", null);
                    builder.setMessage("No se pudo conectar con el servidor! Porfavor intente de nuevo mas tarde.");
                    builder.show();
                }
            }
        });
    }

    private boolean doChecks(String email, String password, String confirmPassword) {
        if (email.isEmpty()) {
            mEmailField.setError(getResources().getString(R.string.error_field_required));
            mEmailField.requestFocus();
            return false;
        }
        else if (password.isEmpty()) {
            mPasswordField.setError(getResources().getString(R.string.error_field_required));
            mPasswordField.requestFocus();
            return false;
        }
        else if (confirmPassword.isEmpty()) {
            mConfirmPasswordField.setError(getResources().getString(R.string.error_field_required));
            mConfirmPasswordField.requestFocus();
            return false;
        }

        if (!isEmailValid(email)) {
            mEmailField.setError(getResources().getString(R.string.error_invalid_email));
            mEmailField.requestFocus();
            return false;
        }

        if (!isPasswordValid(password)) {
            mPasswordField.setError(getResources().getString(R.string.error_invalid_password));
            mPasswordField.requestFocus();
            return false;
        }

        if (!confirmPassword.equals(password)) {
            mConfirmPasswordField.setError(getResources().getString(R.string.error_incorrect_confrim));
            mConfirmPasswordField.requestFocus();
            return false;
        }

        return true;
    }

    public boolean isEmailValid(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public boolean isPasswordValid(String password) {
        return password.length() > 2 && password.length() < 16;
    }
}