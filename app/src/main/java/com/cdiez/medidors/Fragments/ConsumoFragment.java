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

import com.cdiez.medidors.R;
import com.cdiez.medidors.UI.CalculateActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Carlos Diez
 * on 13/02/2016.
 */
public class ConsumoFragment extends Fragment {

    @Bind(R.id.consumo) EditText mConsumo;

    private Context mContext;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_consumo, container, false);
        ButterKnife.bind(this, view);

        mContext = view.getContext();

        return view;
    }

    @OnClick(R.id.calculate)
    public void onClickCalculate() {
        String consumoString = mConsumo.getText().toString().trim();

        if (consumoString.isEmpty()) {
            mConsumo.setError(getResources().getString(R.string.error_field_required));
            mConsumo.requestFocus();
            return;
        }

        Intent intent = new Intent(mContext, CalculateActivity.class);
        intent.putExtra("consumo", Integer.parseInt(consumoString));
        startActivity(intent);
    }
}
