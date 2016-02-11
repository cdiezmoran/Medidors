package com.cdiez.medidors.UI;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.cdiez.medidors.Other.ParseConstants;
import com.cdiez.medidors.R;
import com.parse.ParseUser;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LecturaManual extends AppCompatActivity {

    @Bind(R.id.toolbar) Toolbar mToolbar;
    @Bind(R.id.lectura) EditText mLecturaField;
    @Bind(R.id.lectura_anterior) TextView mLecturaAnterior;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lectura_manual);
        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);

        assert getSupportActionBar()!= null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String ultimaLectura = "Ultima lectura: " + ParseUser.getCurrentUser().getNumber(ParseConstants.KEY_LECTURA_ANTERIOR);
        mLecturaAnterior.setText(ultimaLectura);
    }

    @OnClick(R.id.calculate)
    public void onClickCalculate() {
        String lecturaString = mLecturaField.getText().toString().trim();

        if (lecturaString.isEmpty()) {
            mLecturaField.setError(getResources().getString(R.string.error_field_required));
            mLecturaField.requestFocus();
            return;
        }

        float consumo = Float.parseFloat(lecturaString) - ParseUser.getCurrentUser().getNumber(ParseConstants.KEY_LECTURA_ANTERIOR).floatValue();

        Intent intent = new Intent(this, CalculateActivity.class);
        intent.putExtra("consumo", consumo);
        intent.putExtra("lectura", lecturaString);
        startActivity(intent);
    }
}