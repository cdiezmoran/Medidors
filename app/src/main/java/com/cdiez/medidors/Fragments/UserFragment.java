package com.cdiez.medidors.Fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cdiez.medidors.Other.ParseConstants;
import com.cdiez.medidors.R;
import com.parse.ParseUser;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Carlos Diez
 * on 18/01/2016.
 */
public class UserFragment extends Fragment {

    @Bind(R.id.tarifa) TextView mTarifa;
    @Bind(R.id.lecutra_anterior) TextView mLecturaAnterior;
    @Bind(R.id.fecha_lectura) TextView mFechaLectura;
    @Bind(R.id.ultimo_pago) TextView mUltimoPago;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user, container, false);
        ButterKnife.bind(this, view);

        ParseUser user = ParseUser.getCurrentUser();

        if (user != null) {
            String tarifa = user.getNumber(ParseConstants.KEY_TARIFA) + "";
            String lecturaAnterior = user.getNumber(ParseConstants.KEY_LECTURA_ANTERIOR) + " KW/h";
            String ultimoPago = user.getNumber(ParseConstants.KEY_ULTIMO_PAGO) + "$";

            SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
            Date fechaLectura = user.getDate(ParseConstants.KEY_FECHA_LECTURA);

            //TODO: Enumerar tarifas y ligarlas con su nombre
            mTarifa.setText(tarifa);
            mLecturaAnterior.setText(lecturaAnterior);
            mFechaLectura.setText(dateFormatter.format(fechaLectura));
            mUltimoPago.setText(ultimoPago);
        }

        return view;
    }
}