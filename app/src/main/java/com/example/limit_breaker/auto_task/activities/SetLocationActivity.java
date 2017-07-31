package com.example.limit_breaker.auto_task.activities;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.limit_breaker.auto_task.R;
import com.example.limit_breaker.auto_task.services.GeofenceTrasitionService;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class SetLocationActivity extends AppCompatActivity
        implements
        LocationListener,
        PlaceSelectionListener,
        GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks,
        ResultCallback<Status>,
        OnMapReadyCallback,
        GoogleMap.OnMapClickListener,
        GoogleMap.OnMarkerClickListener{

    private TextView tvLatlang;
    private Location lastLocation;
    private LocationRequest locationRequest;
    private SupportMapFragment mapFragment;
    private GoogleMap map;
    private GoogleApiClient mGoogleApiClient;
    private Marker locationMarker,geoFencemarker;
    private boolean firstCameraZoom;
    private Circle geoFenceLimits;
    private PlaceAutocompleteFragment autocompleteFragment;
    private PendingIntent geoFencePendingIntent;
    public static final String TAG="MAP-PUI";
    private final int UPDATE_INTERVAL =  1000;
    private final int FASTEST_INTERVAL = 900;
    private final int REQ_PERM_CODE=111;
    private static final long GEO_DURATION = 60 * 60 * 1000;
    private static final String GEOFENCE_REQ_ID = "My Geofence";
    private static final float GEOFENCE_RADIUS = 500.0f; // in meters
    private final int GEOFENCE_REQ_CODE = 0;
    private final int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;
    private final int PLACE_PICKER_REQUEST = 2;

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: ");
        mGoogleApiClient.connect();

    }

    @Override
    protected void onStop() {
        Log.d(TAG, "onStop: ");
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_location);
        firstCameraZoom=true;
        createGoogleAPIClient();
        initFields();

    }

    private void initFields(){
        tvLatlang=(TextView) findViewById(R.id.tvLatlng);
        mapFragment=(SupportMapFragment)getSupportFragmentManager()
                .findFragmentById(R.id.MapContainer);
        autocompleteFragment=(PlaceAutocompleteFragment)getFragmentManager()
                .findFragmentById(R.id.place_autocomplete_fragment);
        autocompleteFragment.setOnPlaceSelectedListener(this);
        Log.i(TAG, "initFields: mapsfrag"+mapFragment);
        mapFragment.getMapAsync(this);

    }

    private void createGoogleAPIClient(){
        Log.d(TAG, "createGoogleAPIClient: ");
        if(mGoogleApiClient==null){
            mGoogleApiClient=new GoogleApiClient.Builder(this)
                    .enableAutoManage(this,this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
        Log.d(TAG, "createGoogleAPIClient: "+mGoogleApiClient);
    }

    private void getlastKnownLocation(){
        Log.d(TAG, "getlastKnownLocation: ");
        if(checkPermission()){
            lastLocation=LocationServices.
                    FusedLocationApi.
                    getLastLocation(mGoogleApiClient);
            if(lastLocation!=null){
                Log.i(TAG, "getlastKnownLocation: lat :"
                        +lastLocation.getLatitude()
                +"long :"+lastLocation.getLongitude());
                writeToScreen();
                startLocationUpdates();
            }else{
                Log.i(TAG, "getlastKnownLocation: Location NUll");
                startLocationUpdates();
            }
        }else{
            askPermission();
        }

    }

    private void askPermission() {
        Log.d(TAG, "askPermission()");
        ActivityCompat.requestPermissions(
                this,
                new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                REQ_PERM_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        Log.d(TAG, "onRequestPermissionsResult: ");
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case REQ_PERM_CODE:
                if(grantResults.length>0 &&
                        grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    Log.d(TAG, "onRequestPermissionsResult: permission granted ");
                    getlastKnownLocation();
                }else{
                    Log.d(TAG, "onRequestPermissionsResult: permission denied");
                }
                break;
        }
    }

    private void startLocationUpdates() {
        Log.d(TAG, "startLocationUpdates: ");
        locationRequest=LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(UPDATE_INTERVAL)
                .setFastestInterval(FASTEST_INTERVAL);
        if(checkPermission()){

            LocationServices.
                    FusedLocationApi.
                    requestLocationUpdates(mGoogleApiClient,locationRequest,this);
        }
    }


    private void writeToScreen() {
        Log.d(TAG, "writeToScreen: ");
        writeActuallocation(lastLocation);

    }


    private void writeActuallocation(Location lastLocation){
        Log.d(TAG, "writeActuallocation: ");
        if(lastLocation!=null) {
            tvLatlang.setText("Lat :" + lastLocation.getLatitude()
                    + "Long :" + lastLocation.getLongitude());
        }
        markerLocation(new LatLng(lastLocation.getLatitude(),lastLocation.getLongitude()));
    }

    private void markerLocation(LatLng latLng){
        Log.i(TAG, "markerLocation: "+latLng);
        String title ="lat:"+latLng.latitude+"long :"+latLng.longitude;
        MarkerOptions markerOptions= new MarkerOptions()
                .position(latLng)
                .title(title);
        if(map!=null){
            if(locationMarker!=null){
                locationMarker.remove();
            }
            locationMarker= map.addMarker(markerOptions);
            if(firstCameraZoom) {
                firstCameraZoom=false;
                float zoom = 14f;
                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, zoom);
                map.animateCamera(cameraUpdate);
            }
        }
    }
    private void markerForGeofence(LatLng latLng){
        Log.i(TAG, "markerForGeofence: ");
        String title ="lat:"+latLng.latitude+"long :"+latLng.longitude;
        MarkerOptions markerOptions= new MarkerOptions()
                .position(latLng)
                .title(title);
        if(map!=null) {
            if (geoFencemarker!= null) {
                geoFencemarker.remove();
            }
            geoFencemarker = map.addMarker(markerOptions);
        }
    }

    private boolean checkPermission() {
        Log.d(TAG, "checkPermission()");

        return (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED );
    }

    @Override
    public void onMapClick(LatLng latLng) {
        Log.d(TAG, "onMapClick: "+latLng);
        markerForGeofence(latLng);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        Log.d(TAG, "onMarkerClick: ");
        return false;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map=googleMap;
        map.setOnMapClickListener(this);
        map.setOnMarkerClickListener(this);
    }
    

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.i(TAG, "onConnected: "+mGoogleApiClient);
        getlastKnownLocation();
        writeToScreen();
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.w(TAG, "onConnectionSuspended: " );
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.w(TAG, "onConnectionFailed: " );
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.i(TAG, "onLocationChanged: "+location);
        lastLocation=location;
        //writeActuallocation(location);
    }

    private Geofence createGeofence(LatLng latLng, float radius ) {
        Log.d(TAG, "createGeofence");
        return new Geofence.Builder()
                .setRequestId(GEOFENCE_REQ_ID)
                .setCircularRegion( latLng.latitude, latLng.longitude, radius)
                .setExpirationDuration( GEO_DURATION )
                .setTransitionTypes( Geofence.GEOFENCE_TRANSITION_ENTER
                        | Geofence.GEOFENCE_TRANSITION_EXIT )
                .build();
    }

    private GeofencingRequest createGeofenceRequest(Geofence geofence ) {
        Log.d(TAG, "createGeofenceRequest");
        return new GeofencingRequest.Builder()
                .setInitialTrigger( GeofencingRequest.INITIAL_TRIGGER_ENTER )
                .addGeofence( geofence )
                .build();
    }

    private PendingIntent createGeofencePendingIntent() {
        Log.d(TAG, "createGeofencePendingIntent");
        if ( geoFencePendingIntent != null )
            return geoFencePendingIntent;

        Intent intent = new Intent( this, GeofenceTrasitionService.class);
        return PendingIntent.getService(
                this, GEOFENCE_REQ_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT );
    }

    // Add the created GeofenceRequest to the device's monitoring list
    private void addGeofence(GeofencingRequest request) {
        Log.d(TAG, "addGeofence");
        if (checkPermission()) {
            PendingResult<Status> result = LocationServices.GeofencingApi.addGeofences(
                    mGoogleApiClient,
                    request,
                    createGeofencePendingIntent()
            );
            result.setResultCallback(this);
        }
    }



    // Draw Geofence circle on GoogleMap
    private void drawGeofence() {
        Log.d(TAG, "drawGeofence()");

        if ( geoFenceLimits != null )
            geoFenceLimits.remove();

        CircleOptions circleOptions = new CircleOptions()
                .center( geoFencemarker.getPosition())
                .strokeColor(Color.argb(50, 70,70,70))
                .fillColor( Color.argb(100, 150,150,150) )
                .radius( GEOFENCE_RADIUS );
        geoFenceLimits = map.addCircle( circleOptions );
    }

    @Override
    public void onResult(@NonNull Status status) {
        Log.i(TAG, "onResult: " + status);
        if ( status.isSuccess() ) {
            drawGeofence();
        } else {
            // inform about fail
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch ( item.getItemId() ) {
            case R.id.StartGeoFence: {
                startGeofence();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    // Start Geofence creation process
    private void startGeofence() {
        Log.i(TAG, "startGeofence()");
        if( geoFencemarker != null ) {
            Geofence geofence = createGeofence( geoFencemarker.getPosition(), GEOFENCE_RADIUS );
            GeofencingRequest geofenceRequest = createGeofenceRequest( geofence );
            addGeofence( geofenceRequest );
        } else {
            Log.e(TAG, "Geofence marker is null");
        }
    }

    @Override
    public void onPlaceSelected(Place place) {
        float zoom = 14f;
        LatLng selLatlng = place.getLatLng();
        markerForGeofence(place.getLatLng());
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(selLatlng, zoom);
        map.animateCamera(cameraUpdate);
    }

    @Override
    public void onError(Status status) {

    }

    private void createPlaceAutoComplete(){

        try {
            Intent intent =
                    new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY)
                            .build(this);
            startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
        } catch (GooglePlayServicesRepairableException e) {
            // TODO: Handle the error.
        } catch (GooglePlayServicesNotAvailableException e) {
            // TODO: Handle the error.
        }
    }

    private void createPlacePicker(){

        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

        try {
            startActivityForResult(builder.build(this), PLACE_PICKER_REQUEST);
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(this, data);
                Log.i(TAG, "Place: " + place.getName());
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                // TODO: Handle the error.
                Log.i(TAG, status.getStatusMessage()+"Place AutoComplete");

            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }

            if (requestCode == PLACE_PICKER_REQUEST) {
                if (resultCode == RESULT_OK) {
                    Place place = PlacePicker.getPlace(data, this);
                    String toastMsg = String.format("Place: %s", place.getName());
                    Toast.makeText(this, toastMsg, Toast.LENGTH_LONG).show();
                }
            }
        }
    }
}
