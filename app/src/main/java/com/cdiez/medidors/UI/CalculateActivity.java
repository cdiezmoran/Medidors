package com.cdiez.medidors.UI;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.cdiez.medidors.Data.Tarifa;
import com.cdiez.medidors.Other.ParseConstants;
import com.cdiez.medidors.R;
import com.parse.ParseUser;

import butterknife.Bind;
import butterknife.ButterKnife;

public class CalculateActivity extends AppCompatActivity {

    @Bind(R.id.toolbar) Toolbar mToolbar;
    @Bind(R.id.pago) TextView mPagoText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculate);
        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);

        calculate();
    }

    private void calculate() {
        Intent intent = getIntent();
        float consumo = intent.getFloatExtra("consumo", 0);
        float pago = 0;

        Tarifa tarifa = (Tarifa) ParseUser.getCurrentUser().getParseObject(ParseConstants.KEY_TARIFA);

        switch (tarifa.getName()) {
            case "1":
                pago = getPagoUnoDos(consumo, tarifa);
                break;
            case "2":
                pago = getPagoUnoDos(consumo, tarifa);
                pago += (tarifa.getCargoFijo() * 2);
                break;
            case "DAC":
                break;
        }
        String pagoString = "A pagar: $" + pago;
        mPagoText.setText(pagoString);
    }

    private float getPagoUnoDos(float consumo, Tarifa tarifa) {
        float pago;
        int primerMaximo = tarifa.getPrimerMaximo();
        int segundoMaximo = tarifa.getSegundoMaximo();
        float primerCargo = tarifa.getPrimerCargo();
        float segundoCargo = tarifa.getSegundoCargo();
        float excedente = tarifa.getExcedente();

        if (consumo <= primerMaximo) {
            pago = consumo * primerCargo;
        }
        else{
            pago = primerMaximo * primerCargo;
            consumo -= primerMaximo;

            if (consumo <= segundoMaximo) {
                pago += (consumo * segundoCargo);
            }
            else {
                pago += (segundoMaximo * segundoCargo);
                consumo -= segundoMaximo;
                pago += (consumo * excedente);
            }
        }
        return pago;
    }
}
