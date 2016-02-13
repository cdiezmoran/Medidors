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
import com.cdiez.medidors.UI.EditProfile;
import com.parse.ParseException;
import com.parse.ParseUser;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Carlos Diez
 * on 18/01/2016.
 */
public class UserFragment extends Fragment {

    @Bind(R.id.name) TextView mName;
    @Bind(R.id.email) TextView mEmail;
    @Bind(R.id.username) TextView mUsername;
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
            String name = user.getString(ParseConstants.KEY_NAME) + " " + user.getString(ParseConstants.KEY_APELLIDOS);
            String email = user.getEmail();
            String username = user.getUsername();

            Municipio municipio = null;

            try {
                municipio = user.getParseObject(ParseConstants.KEY_MUNICIPIO).fetchIfNeeded();
            } catch (ParseException e) {
                e.printStackTrace();
            }
            assert municipio != null;

            mName.setText(name);
            mEmail.setText(email);
            mUsername.setText(username);
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
                Intent intent = new Intent(v.getContext(), EditProfile.class);
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