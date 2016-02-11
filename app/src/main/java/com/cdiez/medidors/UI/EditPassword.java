package com.cdiez.medidors.UI;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

import com.cdiez.medidors.Other.ParseConstants;
import com.cdiez.medidors.R;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EditPassword extends AppCompatActivity {

    @Bind(R.id.toolbar) Toolbar mToolbar;
    @Bind(R.id.password) EditText mPasswordField;
    @Bind(R.id.password_new) EditText mPasswordNewField;
    @Bind(R.id.password_confirm) EditText mPasswordConfirmField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_password);
        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);

        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @OnClick(R.id.save)
    public void onClickSave() {
        String password = mPasswordField.getText().toString().trim();
        String passwordNew = mPasswordNewField.getText().toString().trim();
        String passwordConfirm = mPasswordConfirmField.getText().toString().trim();

        ParseUser user = ParseUser.getCurrentUser();

        if (!password.equals(user.getString(ParseConstants.KEY_PASSWORD))) {
            mPasswordField.setError(getResources().getString(R.string.error_incorrect_password));
            mPasswordField.requestFocus();
            return;
        }

        if (!isPasswordValid(passwordNew)) {
            mPasswordNewField.setError(getResources().getString(R.string.error_invalid_password));
            mPasswordNewField.requestFocus();
            return;
        }

        if (!passwordConfirm.equals(passwordNew)) {
            mPasswordConfirmField.setError(getResources().getString(R.string.error_incorrect_confrim));
            mPasswordConfirmField.requestFocus();
            return;
        }

        user.put(ParseConstants.KEY_PASSWORD, passwordNew);
        user.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    NavUtils.navigateUpFromSameTask(EditPassword.this);
                }
            }
        });
    }

    @OnClick(R.id.cancel)
    public void onClickCancel() {
        NavUtils.navigateUpFromSameTask(this);
    }

    public boolean isPasswordValid(String password) {
        return password.length() > 2 && password.length() < 16;
    }
}
