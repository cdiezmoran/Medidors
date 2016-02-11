package com.cdiez.medidors.UI;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.cdiez.medidors.Other.ParseConstants;
import com.cdiez.medidors.R;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.wdullaer.materialdatetimepicker.Utils;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EditRecibo extends AppCompatActivity {

    @Bind(R.id.toolbar) Toolbar mToolbar;
    @Bind(R.id.tarifas) Spinner mSpinner;
    @Bind(R.id.lecutra_anterior) EditText mUltimaLectura;
    @Bind(R.id.fecha_anterior) EditText mFechaAnterior;
    @Bind(R.id.ultimo_pago) EditText mUltimoPago;
    @Bind(R.id.ultimo_consumo) EditText mUltimoConsumo;

    private SimpleDateFormat mDateFormatter;
    private ParseUser mUser = ParseUser.getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_recibo);
        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);

        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String[] tarifas = {"2", "3", "5", "5-A", "6", "9", "9M", "9-CU", "etc.."};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, tarifas);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        mSpinner.setAdapter(adapter);

        mDateFormatter = new SimpleDateFormat("dd/MM/yyyy", Locale.US);

        mSpinner.setSelection(0);
        mUltimaLectura.setText(mUser.getNumber(ParseConstants.KEY_LECTURA_ANTERIOR).toString());
        mUltimoPago.setText(mUser.getNumber(ParseConstants.KEY_ULTIMO_PAGO).toString());
        mFechaAnterior.setText(mDateFormatter.format(mUser.getDate(ParseConstants.KEY_FECHA_LECTURA)));

        mUltimaLectura.requestFocus();

        setDateField();
    }

    @OnClick(R.id.save)
    public void onClickSave() {
        String lecturaString = mUltimaLectura.getText().toString().trim();
        String fechaString = mFechaAnterior.getText().toString().trim();
        String pagoString = mUltimoPago.getText().toString().trim();
        String consumoString = mUltimoConsumo.getText().toString().trim();

        mUser = ParseUser.getCurrentUser();

        if (lecturaString.isEmpty()) {
            mUltimaLectura.setError(getResources().getString(R.string.error_field_required));
            mUltimaLectura.requestFocus();
            return;
        }

        mUser.put(ParseConstants.KEY_LECTURA_ANTERIOR, Integer.parseInt(lecturaString));
        mUser.put(ParseConstants.KEY_TARIFA, mSpinner.getSelectedItemPosition());
        if (!pagoString.isEmpty())
            mUser.put(ParseConstants.KEY_ULTIMO_PAGO, Float.parseFloat(pagoString));
        if (!consumoString.isEmpty())
            mUser.put(ParseConstants.KEY_ULTIMO_CONSUMO, Integer.parseInt(consumoString));

        try {
            Date fecha = mDateFormatter.parse(fechaString);
            mUser.put(ParseConstants.KEY_FECHA_LECTURA, fecha);
        } catch (ParseException e) {
            //TODO:send error
        }

        mUser.saveInBackground(new SaveCallback() {
            @Override
            public void done(com.parse.ParseException e) {
                if (e == null) {
                    NavUtils.navigateUpFromSameTask(EditRecibo.this);
                }
            }
        });
    }

    @OnClick(R.id.cancel)
    public void onClickCancel() {
        NavUtils.navigateUpFromSameTask(this);
    }

    private void setDateField() {
        Calendar now = Calendar.getInstance();
        final DatePickerDialog dpd = DatePickerDialog.newInstance(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                mFechaAnterior.setText(mDateFormatter.format(newDate.getTime()));
            }
        }, now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH));

        mFechaAnterior.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus)
                    dpd.show(getFragmentManager(), "DatePickerDialog");
            }
        });

        dpd.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                mUltimoPago.requestFocus();
            }
        });
    }
}
