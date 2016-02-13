package com.cdiez.medidors.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.cdiez.medidors.Other.ParseConstants;
import com.cdiez.medidors.R;
import com.cdiez.medidors.UI.CalculateActivity;
import com.parse.ParseUser;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Carlos Diez
 * on 13/02/2016.
 */
public class LecturaFragment extends Fragment {

    @Bind(R.id.lectura) EditText mLecturaField;
    @Bind(R.id.lectura_anterior) TextView mLecturaAnterior;

    private Context mContext;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lectura, container, false);
        ButterKnife.bind(this, view);
        mContext = view.getContext();

        String ultimaLectura = "Ultima lectura: " + ParseUser.getCurrentUser().getNumber(ParseConstants.KEY_LECTURA_ANTERIOR);
        mLecturaAnterior.setText(ultimaLectura);

        return view;
    }

    @OnClick(R.id.calculate)
    public void onClickCalculate() {
        String lecturaString = mLecturaField.getText().toString().trim();

        if (lecturaString.isEmpty()) {
            mLecturaField.setError(getResources().getString(R.string.error_field_required));
            mLecturaField.requestFocus();
            return;
        }

        int consumo = Integer.parseInt(lecturaString) - ParseUser.getCurrentUser().getNumber(ParseConstants.KEY_LECTURA_ANTERIOR).intValue();

        Intent intent = new Intent(mContext, CalculateActivity.class);
        intent.putExtra("consumo", consumo);
        intent.putExtra("lectura", lecturaString);
        startActivity(intent);
    }
}
