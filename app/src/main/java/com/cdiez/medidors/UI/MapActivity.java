package com.cdiez.medidors.UI;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;

import com.cdiez.medidors.Data.Centro;
import com.cdiez.medidors.Other.ParseConstants;
import com.cdiez.medidors.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseQuery;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MapActivity extends AppCompatActivity implements LocationListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener{

    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    private static final int MILLISECONDS_PER_SECOND = 1000;
    private static final int UPDATE_INTERVAL_IN_SECONDS = 10;
    private static final int FAST_CEILING_IN_SECONDS = 1;
    private static final long UPDATE_INTERVAL_IN_MILLISECONDS = MILLISECONDS_PER_SECOND
            * UPDATE_INTERVAL_IN_SECONDS;
    private static final long FAST_INTERVAL_CEILING_IN_MILLISECONDS = MILLISECONDS_PER_SECOND
            * FAST_CEILING_IN_SECONDS;
    private static final int MAX_CENTRO_SEARCH_DISTANCE = 100;

    private SupportMapFragment mapFragment;
    private float mRadius;
    private final Map<String, Marker> mMapMarkers = new HashMap<>();
    private String mSelectedCentroObjectId;
    private Location mLastLocation;
    private Location mCurrentLocation;
    private LocationRequest mLocationRequest;
    private GoogleApiClient mLocationClient;

    @Bind(R.id.toolbar) Toolbar mToolbar;
    @Bind(R.id.relative) RelativeLayout mRelativeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        ButterKnife.bind(this);
        mRadius = 25;

        setSupportActionBar(mToolbar);

        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mLocationRequest = LocationRequest.create();
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setFastestInterval(FAST_INTERVAL_CEILING_IN_MILLISECONDS);

        mLocationClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        mapFragment = (SupportMapFragment) this.getSupportFragmentManager().findFragmentById(R.id.map_fragment);
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                // Enable the current location "blue dot"
                if (ActivityCompat.checkSelfPermission(MapActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MapActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    //ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION}, );
                    return;
                }
                googleMap.setMyLocationEnabled(true);
                // Set up the camera change handler
                googleMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
                    public void onCameraChange(CameraPosition position) {
                        doMapQuery();
                    }
                });
            }
        });

        isLocationEnabled();
    }

    private void doMapQuery() {
        Location myLoc = (mCurrentLocation == null) ? mLastLocation : mCurrentLocation;
        if (myLoc == null) {
            cleanUpMarkers(new HashSet<String>());
            return;
        }
        final ParseGeoPoint myPoint = geoPointFromLocation(myLoc);
        ParseQuery<Centro> mapQuery = Centro.getQuery();
        mapQuery.whereWithinKilometers(ParseConstants.KEY_LOCATION, myPoint, MAX_CENTRO_SEARCH_DISTANCE);
        mapQuery.findInBackground(new FindCallback<Centro>() {
            @Override
            public void done(List<Centro> objects, ParseException e) {
                // Handle the results
                Set<String> toKeep = new HashSet<>();
                for (final Centro centro : objects) {
                    toKeep.add(centro.getObjectId());
                    Marker oldMarker = mMapMarkers.get(centro.getObjectId());
                    MarkerOptions markerOpts =
                            new MarkerOptions().position(new LatLng(centro.getLocation().getLatitude(), centro
                                    .getLocation().getLongitude()));
                    if (centro.getLocation().distanceInKilometersTo(myPoint) > mRadius) {
                        // Set up an out-of-range marker
                        if (oldMarker != null) {
                            if (oldMarker.getSnippet() == null) {
                                continue;
                            } else {
                                oldMarker.remove();
                            }
                        }
                        markerOpts =
                                markerOpts.title(centro.getName())
                                        .icon(BitmapDescriptorFactory.defaultMarker(
                                                BitmapDescriptorFactory.HUE_RED));
                    } else {
                        // Set up an in-range marker
                        if (oldMarker != null) {
                            if (oldMarker.getSnippet() != null) {
                                continue;
                            } else {
                                oldMarker.remove();
                            }
                        }
                        markerOpts =
                                markerOpts.title(centro.getName())
                                        .snippet(centro.getAddress())
                                        .icon(BitmapDescriptorFactory.defaultMarker(
                                                BitmapDescriptorFactory.HUE_GREEN));
                    }
                    final MarkerOptions finalMarkerOpts = markerOpts;
                    mapFragment.getMapAsync(new OnMapReadyCallback() {
                        @Override
                        public void onMapReady(GoogleMap googleMap) {
                            Marker marker = googleMap.addMarker(finalMarkerOpts);
                            mMapMarkers.put(centro.getObjectId(), marker);
                            if (centro.getObjectId().equals(mSelectedCentroObjectId)) {
                                marker.showInfoWindow();
                                mSelectedCentroObjectId = null;
                            }

                            googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                                @Override
                                public boolean onMarkerClick(Marker marker) {
                                    return false;
                                }
                            });
                        }
                    });
                }
                cleanUpMarkers(toKeep);
            }
        });
    }

    @Override
    public void onConnected(Bundle bundle) {
        mCurrentLocation = getLocation();
        startPeriodicUpdates();

        goToLocationOnMap();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(Location location) {
        mCurrentLocation = location;
        if (mLastLocation != null
                && geoPointFromLocation(location)
                .distanceInKilometersTo(geoPointFromLocation(mLastLocation)) < 0.01) {
            // If the location hasn't changed by more than 10 meters, ignore it.
            return;
        }
        mLastLocation = location;

        doMapQuery();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        if (connectionResult.hasResolution()) {
            try {
                connectionResult.startResolutionForResult(this, CONNECTION_FAILURE_RESOLUTION_REQUEST);
            } catch (IntentSender.SendIntentException e) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AppCompatAlertDialogStyle);
                builder.setTitle("Connection Failed!");
                builder.setPositiveButton("ACCEPT", null);
                builder.setMessage("Could not resolve the location connection please try again later.");
                builder.show();
            }
        } else {
            //showErrorDialog(connectionResult.getErrorCode());
            AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AppCompatAlertDialogStyle);
            builder.setTitle("Connection Failed!");
            builder.setPositiveButton("ACCEPT", null);
            builder.setMessage("Could not connect to Google Play services.");
            builder.show();
        }
    }

    private void goToLocationOnMap() {
        final Location myLoc = (mCurrentLocation == null) ? mLastLocation : mCurrentLocation;
        if (myLoc != null) {
            //Go to Location
            mapFragment.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap googleMap) {
                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                            new LatLng(myLoc.getLatitude(), myLoc.getLongitude()), 13));

                    CameraPosition cameraPosition = new CameraPosition.Builder()
                            .target(new LatLng(myLoc.getLatitude(), myLoc.getLongitude()))      // Sets the center of the map to location user
                            .zoom(15)                   // Sets the zoom
                            .bearing(0)                // Sets the orientation of the camera to east
                            .tilt(0)                   // Sets the tilt of the camera to 30 degrees
                            .build();                   // Creates a CameraPosition from the builder

                    googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                }
            });
        }
    }

    private Location getLocation() {
        if (servicesConnected()) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return null;
            }
            return LocationServices.FusedLocationApi.getLastLocation(mLocationClient);
        } else {
            return null;
        }
    }

    private boolean servicesConnected() {
        int resultCode = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this);

        if (ConnectionResult.SUCCESS == resultCode) {
            return true;
        } else {
            //Show error dialog
            AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AppCompatAlertDialogStyle);
            builder.setTitle("Connection Failed!");
            builder.setPositiveButton("ACCEPT", null);
            builder.setMessage("Could not connect to Google Play services.");
            builder.show();
            return false;
        }
    }

    /*
   * Helper method to clean up old markers
   */
    private void cleanUpMarkers(Set<String> markersToKeep) {
        for (String objId : new HashSet<>(mMapMarkers.keySet())) {
            if (!markersToKeep.contains(objId)) {
                Marker marker = mMapMarkers.get(objId);
                marker.remove();
                mMapMarkers.get(objId).remove();
                mMapMarkers.remove(objId);
            }
        }
    }

    /*
   * Helper method to get the Parse GEO point representation of a location
   */
    private ParseGeoPoint geoPointFromLocation(Location loc) {
        return new ParseGeoPoint(loc.getLatitude(), loc.getLongitude());
    }

    private void startPeriodicUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mLocationClient, mLocationRequest, this);
    }

    private void stopPeriodicUpdates() {
        mLocationClient.disconnect();
    }

    private void isLocationEnabled() {
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
            return;
        }

        // getting GPS status
        isGPSEnabled = locationManager
                .isProviderEnabled(LocationManager.GPS_PROVIDER);

        // getting network status
        isNetworkEnabled = locationManager
                .isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        if (!isGPSEnabled && !isNetworkEnabled) {
            // no network provider is enabled
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
    }

    @Override
    public void onStop() {
        if (mLocationClient.isConnected()) {
            stopPeriodicUpdates();
        }
        mLocationClient.disconnect();

        super.onStop();
    }

    @Override
    public void onStart() {
        super.onStart();

        mLocationClient.connect();
    }
}
