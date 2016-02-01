package com.cdiez.medidors.UI;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.cdiez.medidors.Data.Estado;
import com.cdiez.medidors.Data.Municipio;
import com.cdiez.medidors.Other.ParseConstants;
import com.cdiez.medidors.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class EditLocation extends AppCompatActivity {

    @Bind(R.id.toolbar) Toolbar mToolbar;
    @Bind(R.id.estados) Spinner mEstadosSpinner;
    @Bind(R.id.municipios) Spinner mMunicipiosSpinner;

    private Municipio mMunicipio;
    private Estado mEstado;
    private List<Municipio> mMunicipios = new ArrayList<>();
    private ArrayAdapter<String> mMunicipiosAdapter;
    private ArrayAdapter<String> mEstadosAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_location);
        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);

        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mMunicipio = (Municipio) ParseUser.getCurrentUser().getParseObject(ParseConstants.KEY_MUNICIPIO);

        doEstadosQuery();
    }

    private void doEstadosQuery() {
        ParseQuery<Estado> estadoQuery = Estado.getQuery();
        estadoQuery.findInBackground(new FindCallback<Estado>() {
            @Override
            public void done(List<Estado> list, ParseException e) {
                if (e == null) {
                    List<String> estados = new ArrayList<>();

                    for (Estado estado : list)
                        estados.add(estado.getName());

                    mEstadosAdapter = new ArrayAdapter<>(EditLocation.this, android.R.layout.simple_spinner_item, estados);
                    mEstadosAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                    mEstadosSpinner.setAdapter(mEstadosAdapter);

                    if (mMunicipio != null) {
                        mEstado = mMunicipio.getEstado();
                        doMunicipiosQuery(mMunicipio.getName());
                    } else {
                        mEstado = list.get(0);
                        doMunicipiosQuery(null);
                    }

                    mEstadosSpinner.setSelection(mEstadosAdapter.getPosition(mEstado.getName()));
                }
            }
        });
    }

    private void doMunicipiosQuery(final String municipioName) {
        ParseQuery<Municipio> municipioQuery = Municipio.getQuery();
        municipioQuery.whereEqualTo(ParseConstants.KEY_ESTADO, mEstado);
        municipioQuery.findInBackground(new FindCallback<Municipio>() {
            @Override
            public void done(List<Municipio> list, ParseException e) {
                if (e == null) {
                    mMunicipios = list;
                    List<String> municipios = new ArrayList<>();

                    for (Municipio municipio : list)
                        municipios.add(municipio.getName());


                    mMunicipiosAdapter = new ArrayAdapter<>(EditLocation.this, android.R.layout.simple_spinner_item, municipios);
                    mMunicipiosAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                    mMunicipiosSpinner.setAdapter(mMunicipiosAdapter);

                    if (municipioName == null) {
                        mMunicipiosSpinner.setSelection(mMunicipiosAdapter.getPosition(list.get(0).getName()));
                    }
                    else {
                        mMunicipiosSpinner.setSelection(mMunicipiosAdapter.getPosition(municipioName));
                    }

                }
            }
        });
    }

    @OnClick(R.id.save)
    public void onSaveClick() {
        int municipioPosition = mMunicipiosSpinner.getSelectedItemPosition();
        Municipio municipio = mMunicipios.get(municipioPosition);

        ParseUser user = ParseUser.getCurrentUser();
        user.put(ParseConstants.KEY_MUNICIPIO, municipio);

        user.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    NavUtils.navigateUpFromSameTask(EditLocation.this);
                }
            }
        });
    }

    @OnClick(R.id.cancel)
    public void onCancelClick() {
        NavUtils.navigateUpFromSameTask(this);
    }

    @OnClick(R.id.auto_fill)
    public void onAutoFillClick() {
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (location != null) {
            double longitude = location.getLongitude();
            double latitude = location.getLatitude();

            String url = "http://maps.googleapis.com/maps/api/geocode/json?latlng=" + latitude + ","+ longitude + "&sensor=false";

            try {
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url(url)
                        .build();
                Response responses = null;

                try {
                    responses = client.newCall(request).execute();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                assert responses != null;
                String jsonData = responses.body().toString();
                JSONObject object = new JSONObject(jsonData);
                JSONArray address_components = object.getJSONArray("address_components");

                String estado = "";
                String municipio = null;

                for (int i = 0; i < address_components.length(); i++) {
                    JSONObject jsonObject = address_components.getJSONObject(i);
                    String long_name = jsonObject.getString("long_name");
                    JSONArray types = jsonObject.getJSONArray("types");
                    String type = types.getString(0);

                    if (type.equalsIgnoreCase("administrative_area_level_1")) {
                        estado = long_name;
                    }
                    else if (type.equalsIgnoreCase("administrative_area_level_2") || type.equalsIgnoreCase("administrative_area_level_3")){
                        municipio = long_name;
                    }
                }

                mEstadosSpinner.setSelection(mEstadosAdapter.getPosition(estado));
                doMunicipiosQuery(municipio);

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
}
