package com.cdiez.medidors.UI;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.cdiez.medidors.Data.Estado;
import com.cdiez.medidors.Data.Municipio;
import com.cdiez.medidors.Other.ParseConstants;
import com.cdiez.medidors.R;
import com.github.rahatarmanahmed.cpv.CircularProgressView;
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

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
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
    private List<Estado> mEstados;
    private boolean mFlag = false;
    private Location mLocation;
    private AlertDialog mAlertDialog;

    private final LocationListener mLocationListener = new LocationListener() {

        @Override
        public void onLocationChanged(final Location location) {
            mLocation = location;
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };

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

        mEstadosSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (mEstados != null) {
                    mEstado = mEstados.get(position);
                    if (!mFlag) {
                        doMunicipiosQuery(null);
                    } else {
                        mFlag = false;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void doEstadosQuery() {
        ParseQuery<Estado> estadoQuery = Estado.getQuery();
        estadoQuery.findInBackground(new FindCallback<Estado>() {
            @Override
            public void done(List<Estado> list, ParseException e) {
                if (e == null) {
                    List<String> estados = new ArrayList<>();
                    mEstados = list;

                    for (Estado estado : list)
                        estados.add(estado.getName());

                    mEstadosAdapter = new ArrayAdapter<>(EditLocation.this, android.R.layout.simple_spinner_item, estados);
                    mEstadosAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                    mEstadosSpinner.setAdapter(mEstadosAdapter);

                    if (mMunicipio != null) {
                        try {
                            mEstado = (Estado) mMunicipio.fetchIfNeeded().getParseObject(ParseConstants.KEY_ESTADO);
                        } catch (ParseException e1) {
                            e1.printStackTrace();
                        }
                        mFlag = true;
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
                    mFlag = false;
                    mMunicipios = list;
                    List<String> municipios = new ArrayList<>();

                    for (Municipio municipio : list)
                        municipios.add(municipio.getName());


                    mMunicipiosAdapter = new ArrayAdapter<>(EditLocation.this, android.R.layout.simple_spinner_item, municipios);
                    mMunicipiosAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                    mMunicipiosSpinner.setAdapter(mMunicipiosAdapter);

                    if (municipioName == null) {
                        mMunicipiosSpinner.setSelection(mMunicipiosAdapter.getPosition(list.get(0).getName()));
                    } else {
                        mMunicipiosSpinner.setSelection(mMunicipiosAdapter.getPosition(municipioName));
                    }

                    if (mAlertDialog != null && mAlertDialog.isShowing()) {
                        mAlertDialog.dismiss();
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

        showProgressView();

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
        showProgressView();
        mLocation = getLocation();

        if (mLocation != null) {
            double longitude = mLocation.getLongitude();
            double latitude = mLocation.getLatitude();

            String url = "https://maps.googleapis.com/maps/api/geocode/json?latlng=" + latitude + ","+ longitude + "&key=AIzaSyAlEnwOmGFqUMT1RmOskanU57wRPe8nw2Y";

            try {
                okHttpCall(url, new Callback() {
                    Handler mainHandler = new Handler(Looper.getMainLooper());
                    @Override
                    public void onFailure(Call call, IOException e) {

                    }

                    @Override
                    public void onResponse(Call call, final Response response) throws IOException {
                        final String jsonData = response.body().string();
                        mainHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                if (response.isSuccessful()) {
                                    try {
                                        JSONObject object = new JSONObject(jsonData);
                                        JSONObject secondObject = object.getJSONArray("results").getJSONObject(0);
                                        JSONArray address_components = secondObject.getJSONArray("address_components");

                                        String estado = "";
                                        String municipio = null;

                                        for (int i = 0; i < address_components.length(); i++) {
                                            JSONObject jsonObject = address_components.getJSONObject(i);
                                            String long_name = jsonObject.getString("long_name");
                                            JSONArray types = jsonObject.getJSONArray("types");
                                            String type = types.getString(0);

                                            if (type.equalsIgnoreCase("administrative_area_level_1")) {
                                                estado = long_name;
                                            } else if (type.equalsIgnoreCase("administrative_area_level_2")
                                                    || type.equalsIgnoreCase("administrative_area_level_3")) {
                                                municipio = long_name;
                                            }
                                        }

                                        mEstadosSpinner.setSelection(mEstadosAdapter.getPosition(estado));

                                        if (mEstados != null)
                                            for (Estado edo : mEstados)
                                                if (edo.getName().equals(estado))
                                                    mEstado = edo;

                                        mFlag = true;
                                        doMunicipiosQuery(municipio);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        });
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else {
            showLocationError();
            mAlertDialog.dismiss();
        }
    }
    private Call okHttpCall(String url, Callback callback) throws IOException{
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();
        Call call = client.newCall(request);
        call.enqueue(callback);
        return call;
    }

    public Location getLocation() {
        Location location = null;
        try {
            boolean isGPSEnabled;
            boolean isNetworkEnabled;

            LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return null;
            }

            // getting GPS status
            isGPSEnabled = locationManager
                    .isProviderEnabled(LocationManager.GPS_PROVIDER);

            // getting network status
            isNetworkEnabled = locationManager
                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!isGPSEnabled && !isNetworkEnabled) {
                // no network provider is enabled
                showLocationError();
            } else {
                // First get location from Network Provider
                if (isNetworkEnabled) {
                    locationManager.requestLocationUpdates(
                            LocationManager.NETWORK_PROVIDER,
                            1000 * 60,
                            10, mLocationListener);
                    if (locationManager != null) {
                        location = locationManager
                                .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    }
                }
                // if GPS Enabled get lat/long using GPS Services
                if (isGPSEnabled) {
                    if (location == null) {
                        locationManager.requestLocationUpdates(
                                LocationManager.GPS_PROVIDER,
                                1000 * 60,
                                10, mLocationListener);
                        if (locationManager != null) {
                            location = locationManager
                                    .getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return location;
    }

    private void showLocationError() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AppCompatAlertDialogStyle);
        builder.setTitle("No se encontro su localización!");
        builder.setMessage("Asegurese de tener sus servicios de localización activados y estar conectado a internet.");
        builder.setPositiveButton("CONFIGURACIÓN", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        });
        builder.setNegativeButton("CANCELAR", null);
        builder.show();
    }

    private void showProgressView() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.TransparentAlertDialog);
        LayoutInflater inflater = getLayoutInflater();
        View convertView = inflater.inflate(R.layout.circular_progress_view, null);
        builder.setView(convertView);
        CircularProgressView cpv = (CircularProgressView) convertView.findViewById(R.id.cpv);
        cpv.startAnimation();
        builder.setCancelable(false);
        mAlertDialog = builder.show();
    }
}
