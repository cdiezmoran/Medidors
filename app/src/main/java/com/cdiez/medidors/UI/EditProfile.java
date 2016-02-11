package com.cdiez.medidors.UI;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.CircularPropagation;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.cdiez.medidors.Other.ParseConstants;
import com.cdiez.medidors.R;
import com.github.rahatarmanahmed.cpv.CircularProgressView;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EditProfile extends AppCompatActivity {

    @Bind(R.id.toolbar) Toolbar mToolbar;
    @Bind(R.id.email_field) EditText mEmailField;
    @Bind(R.id.user_field) EditText mUserField;

    private ParseUser mUser = ParseUser.getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);

        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mEmailField.setText(mUser.getEmail());
        mUserField.setText(mUser.getUsername());
    }

    @OnClick(R.id.save)
    public void onClickSave() {
        String email = mEmailField.getText().toString().trim();
        String username = mUserField.getText().toString().trim();

        if (!isEmailValid(email)) {
            mEmailField.setError(getResources().getString(R.string.error_invalid_email));
            mEmailField.requestFocus();
            return;
        }

        if (!isUsernameValid(username)) {
            mUserField.setError(getResources().getString(R.string.error_invalid_user));
            mUserField.requestFocus();
            return;
        }

        showProgressView();

        if (!email.equals(mUser.getEmail()) && !username.equals(mUser.getEmail())) {
            doEmailQuery(email, username, true);
        }
        else if (!email.equals(mUser.getEmail()) && username.equals(mUser.getUsername())) {
            doEmailQuery(email, username, false);
        }
        else if (email.equals(mUser.getEmail()) && !username.equals(mUser.getUsername())) {
            doUsernameQuery(username);
        }
        else {
            NavUtils.navigateUpFromSameTask(this);
        }
    }

    private void showProgressView() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.transparentAlertDialog);
        LayoutInflater inflater = getLayoutInflater();
        View convertView = inflater.inflate(R.layout.circular_progress_view, null);
        builder.setView(convertView);
        CircularProgressView cpv = (CircularProgressView) convertView.findViewById(R.id.cpv);
        cpv.startAnimation();
        builder.setCancelable(false);
        builder.show();
    }

    private void doEmailQuery(final String email, final String username, final boolean isUsernameDifferent) {
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereEqualTo(ParseConstants.KEY_EMAIL, email);
        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> list, ParseException e) {
                if (e == null) {
                    if (list.isEmpty()) {
                        mUser.setEmail(email);

                        if (isUsernameDifferent) {
                            doUsernameQuery(username);
                        }
                        else {
                            saveUser();
                        }
                    }
                    else {
                        mEmailField.setError(getResources().getString(R.string.error_used_email));
                        mEmailField.requestFocus();
                    }
                }
            }
        });
    }

    private void doUsernameQuery(final String username) {
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereEqualTo(ParseConstants.KEY_USERNAME, username);
        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> list, ParseException e) {
                if (e == null) {
                    if (list.isEmpty()) {
                        mUser.setUsername(username);
                        saveUser();
                    }
                    else {
                        mUserField.setError(getResources().getString(R.string.error_used_username));
                        mUserField.requestFocus();
                    }
                }
            }
        });
    }

    private void saveUser() {
        mUser.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    NavUtils.navigateUpFromSameTask(EditProfile.this);
                }
            }
        });
    }

    @OnClick(R.id.cancel)
    public void onClickCancel() {
        NavUtils.navigateUpFromSameTask(this);
    }

    public boolean isEmailValid(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean isUsernameValid(CharSequence username){
        Pattern pattern = Pattern.compile("[a-zA-Z0-9_]{1,15}");
        Matcher matcher = pattern.matcher(username);
        return matcher.matches();
    }

    @OnClick(R.id.change_password)
    public void onClickChangePassword() {
        Intent intent = new Intent(this, EditPassword.class);
        startActivity(intent);
    }
}