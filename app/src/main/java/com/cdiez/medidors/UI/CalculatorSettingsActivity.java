package com.cdiez.medidors.UI;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.cdiez.medidors.Adapters.SettingsAdapter;
import com.cdiez.medidors.Other.FragmentConstants;
import com.cdiez.medidors.R;

import butterknife.Bind;
import butterknife.ButterKnife;

public class CalculatorSettingsActivity extends AppCompatActivity {

    @Bind(R.id.toolbar) Toolbar mToolbar;
    @Bind(R.id.list_view) ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator_settings);
        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);

        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        int[] icons = {R.drawable.ic_receipt_grey600_24dp, R.drawable.ic_map_marker_grey600_24dp};
        final String[] titles = {FragmentConstants.SETTINGS_RECIBO, FragmentConstants.SETTINGS_LOCATION};

        SettingsAdapter adapter = new SettingsAdapter(this, titles, icons);
        mListView.setAdapter(adapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (titles[position]) {
                    case FragmentConstants.SETTINGS_RECIBO:
                        goToActivity(EditRecibo.class);
                        break;
                    case FragmentConstants.SETTINGS_LOCATION:
                        goToActivity(EditLocation.class);
                        break;
                }
            }
        });
    }

    private void goToActivity(Class<?> targetClass) {
        Intent intent = new Intent(this, targetClass);
        startActivity(intent);
    }
}
