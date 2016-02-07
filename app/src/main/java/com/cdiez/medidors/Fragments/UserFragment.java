package com.cdiez.medidors.Fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cdiez.medidors.Data.Municipio;
import com.cdiez.medidors.Other.ParseConstants;
import com.cdiez.medidors.R;
import com.cdiez.medidors.UI.EditLocation;
import com.cdiez.medidors.UI.EditRecibo;
import com.parse.ParseException;
import com.parse.ParseUser;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Carlos Diez
 * on 18/01/2016.
 */
public class UserFragment extends Fragment {

    @Bind(R.id.tarifa) TextView mTarifa;
    @Bind(R.id.lecutra_anterior) TextView mLecturaAnterior;
    @Bind(R.id.fecha_lectura) TextView mFechaLectura;
    @Bind(R.id.ultimo_pago) TextView mUltimoPago;
    @Bind(R.id.estado) TextView mEstado;
    @Bind(R.id.municipio) TextView mMunicipio;
    @Bind(R.id.card_view_info) CardView mCardInfo;
    @Bind(R.id.card_view_location) CardView mCardLocation;

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

            Municipio municipio = null;

            try {
                municipio = user.getParseObject(ParseConstants.KEY_MUNICIPIO).fetchIfNeeded();
            } catch (ParseException e) {
                e.printStackTrace();
            }
            assert municipio != null;

            mTarifa.setText(tarifa);
            mLecturaAnterior.setText(lecturaAnterior);
            mFechaLectura.setText(dateFormatter.format(fechaLectura));
            mUltimoPago.setText(ultimoPago);
            mMunicipio.setText(municipio.getName());
            mEstado.setText(municipio.getEstado().getName());

            setOnClickListeners();
        }

        return view;
    }

    private void setOnClickListeners() {
        mCardInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), EditRecibo.class);
                v.getContext().startActivity(intent);
            }
        });

        mCardLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), EditLocation.class);
                v.getContext().startActivity(intent);
            }
        });
    }
}