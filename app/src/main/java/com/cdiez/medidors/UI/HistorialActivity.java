package com.cdiez.medidors.UI;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.cdiez.medidors.Adapters.HistorialAdapter;
import com.cdiez.medidors.Data.Lectura;
import com.cdiez.medidors.Other.ParseConstants;
import com.cdiez.medidors.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class HistorialActivity extends AppCompatActivity {

    @Bind(R.id.toolbar) Toolbar mToolbar;
    @Bind(R.id.recycler_view) RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historial);
        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);

        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        doLecturaQuery();
    }

    private void doLecturaQuery() {
        ParseQuery<Lectura> query = Lectura.getQuery();
        query.whereEqualTo(ParseConstants.KEY_USER, ParseUser.getCurrentUser());
        query.findInBackground(new FindCallback<Lectura>() {
            @Override
            public void done(List<Lectura> list, ParseException e) {
                if (e == null) {
                    HistorialAdapter adapter = new HistorialAdapter(HistorialActivity.this, list);
                    mRecyclerView.setAdapter(adapter);
                }
            }
        });
    }
}
