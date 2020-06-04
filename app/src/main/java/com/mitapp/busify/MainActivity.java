package com.mitapp.busify;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import android.Manifest;
import android.app.ActionBar;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Camera;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.annotations.Nullable;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;
import java.lang.Object;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback{

    private ActionBarDrawerToggle nToggle;
    private static final int LOCATION_PERMISSION_CODE = 1337;
    private static final String TAG = "MainActivity";
    private static final int ERROR_DIALOG_REQUEST = 9001;
    private static final int PERMISSIONS_REQUEST_ENABLE_GPS = 9002;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 9003;
    private boolean isLocationGranted = false;
    private static final String USER_DETAILS = "User Details";
    private static final String SELECTED_BUS= "Selected Bus";
    GoogleMap mMap;
    FusedLocationProviderClient fusedLocationProviderClient;
    FirebaseDatabase database;
    DatabaseReference reference;
    FirebaseFirestore mDB = FirebaseFirestore.getInstance();
    String UId = FirebaseAuth.getInstance().getUid();
    RadioButton checkbox_do_not_show_buses, checkbox_show_my_buses, checkbox_show_all_buses;
    Map<String,String> UID_Bus_map = new HashMap<>();
    Map<String,Marker> driverMarkers = new HashMap<>();
    List<Marker> driverMarker = new ArrayList<>();





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        UID_Bus_map.put("G","c6j3SIAX38W1uSWGBjDGeXmHBVw2");
        UID_Bus_map.put("H","SOzNZgTpoNZj14OC2F97vqrqI2k1");
        final SharedPreferences sharedPreferences = getSharedPreferences(USER_DETAILS,MODE_PRIVATE);
        final String selectedBus = sharedPreferences.getString(SELECTED_BUS,null);
        //getDriverLocationLive(selectedBus,1);


        //Functions for check boxes
        RadioGroup main_menu_radio_group = findViewById(R.id.main_menu_radio_group);
        main_menu_radio_group.bringToFront();

        checkbox_do_not_show_buses = findViewById(R.id.checkbox_do_not_show_buses);
        checkbox_do_not_show_buses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for(Marker marker : driverMarker)
                {
                    marker.setVisible(false);
                }
                checkbox_do_not_show_buses.setChecked(true);
            }
        });
        checkbox_show_my_buses = findViewById(R.id.checkbox_show_my_buses);
        checkbox_show_my_buses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendCoordinateToFirebase();
                getDriverLocationLive();
                getMyDriverLocationLive(selectedBus);
                checkbox_show_my_buses.setChecked(true);
            }
        });
        checkbox_show_all_buses = findViewById(R.id.checkbox_show_all_buses);
        checkbox_show_all_buses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkbox_show_all_buses.setChecked(true);
                sendCoordinateToFirebase();
                getDriverLocationLive();
            }
        });



        //This is for UI
        View status_bar_tint = findViewById(R.id.passenger_statusBar_spacer);
        status_bar_tint.bringToFront();
        status_bar_tint.getLayoutParams().height = getResources().getDimensionPixelSize(getResources().
                getIdentifier("status_bar_height", "dimen", "android"));
        status_bar_tint.requestLayout();

        View nav_bar = findViewById(R.id.passenger_navigationBar_spacer);
        nav_bar.bringToFront();
        nav_bar.getLayoutParams().height = getResources().getDimensionPixelSize(getResources().
                getIdentifier("navigation_bar_height", "dimen", "android"));
        nav_bar.requestLayout();

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION,
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

        Toolbar my_toolbar = findViewById(R.id.passenger_actionBar);
        my_toolbar.setTitle("");
        setSupportActionBar(my_toolbar);

        DrawerLayout nDrawerLayout = findViewById(R.id.passenger_navigationMenu);
        nToggle = new ActionBarDrawerToggle(this, nDrawerLayout, R.string.open, R.string.close);


        nDrawerLayout.addDrawerListener(nToggle);
        nToggle.syncState();
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        NavigationView navigationView = findViewById(R.id.passenger_navView);
        navigationView.setNavigationItemSelectedListener(this);


        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        fusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                LatLng latlng1 = new LatLng(location.getLatitude(),location.getLongitude());
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng1,14.0f));
            }
        });


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (nToggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.change_details){
            startActivity(new Intent(getApplicationContext(), change_details.class));
        }
        if (id == R.id.settings){
            startActivity(new Intent(getApplicationContext(), Settings.class));
        }
        if (id == R.id.logout_nav){
            FirebaseAuth.getInstance().signOut();
            GoogleSignIn.getClient(this, new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .build()).signOut().addOnSuccessListener(new OnSuccessListener<Void>(){
                @Override
                public void onSuccess(Void aVoid){
                    startActivity(new Intent(getApplicationContext(), login_activity.class));
                }});
        }
        if (id == R.id.app_info){
            startActivity(new Intent(getApplicationContext(), app_info.class));
        }
        return false;
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        initmap();
        if(checkMapServices())
        {
            if(isLocationGranted)
            {
                initmap();
            }
            else{
                getLocationPermission();
            }
        }
    }


    private boolean checkMapServices(){
        if(isServicesOK()){
            if(isMapsEnabled()){
                return true;
            }
        }
        return false;
    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("This application requires GPS to work properly, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        Intent enableGpsIntent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivityForResult(enableGpsIntent, PERMISSIONS_REQUEST_ENABLE_GPS);
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    public boolean isMapsEnabled(){
        final LocationManager manager = (LocationManager) getSystemService( Context.LOCATION_SERVICE );

        if ( !manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
            buildAlertMessageNoGps();
            return false;
        }
        return true;
    }

    private void getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         *
         */
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            isLocationGranted = true;
            initmap();
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    public boolean isServicesOK(){
        Log.d(TAG, "isServicesOK: checking google services version");

        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(MainActivity.this);

        if(available == ConnectionResult.SUCCESS){
            //everything is fine and the user can make map requests
            Log.d(TAG, "isServicesOK: Google Play Services is working");
            return true;
        }
        else if(GoogleApiAvailability.getInstance().isUserResolvableError(available)){
            //an error occured but we can resolve it
            Log.d(TAG, "isServicesOK: an error occured but we can fix it");
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(MainActivity.this, available, ERROR_DIALOG_REQUEST);
            dialog.show();
        }else{
            Toast.makeText(this, "You can't make map requests", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        isLocationGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    isLocationGranted = true;
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult: called.");
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ENABLE_GPS: {
                if(isLocationGranted){
                    initmap();
                }
                else{
                    getLocationPermission();
                }
            }
        }

    }

    private void initmap()
    {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.passenger_mapView);
        mapFragment.getMapAsync(MainActivity.this);

    }

    private void sendlastknownlocation()
    {


        Task<Location> locationtask = fusedLocationProviderClient.getLastLocation();
        locationtask.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                GeoPoint geoPoint = new GeoPoint(location.getLatitude(),location.getLongitude());
                Map<String,Object> data = new HashMap<>();
                data.put("UID",UId);
                data.put("Location",geoPoint);
                //Toast.makeText(MainActivity.this, "point" + data, Toast.LENGTH_SHORT).show();
                mDB.collection("locations").document(UId).set(data).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        //Toast.makeText(MainActivity.this, "saved", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this, "not saved", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        //Toast.makeText(getApplicationContext(), "running", Toast.LENGTH_SHORT).show();

        mMap = googleMap;
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.passenger_mapView);


        //moving location button to bottom right
        View locationButton = ((View) mapFragment.getView().findViewById(Integer.parseInt("1")).
                getParent()).findViewById(Integer.parseInt("2"));
        RelativeLayout.LayoutParams rlp = (RelativeLayout.LayoutParams) locationButton.getLayoutParams();
        rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
        rlp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
        rlp.setMargins(0, 0, 200, 200);

        //setting map theme

        SharedPreferences sharedPreferences = getSharedPreferences("Map Themes",MODE_PRIVATE);
        int selectedTheme = sharedPreferences.getInt("theme",1);
        // 1:light 2:dark 3:white 4:black
        if (selectedTheme == 1) mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(MainActivity.this, R.raw.standardmap));
        else if (selectedTheme == 2) mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(MainActivity.this, R.raw.night));
        else if (selectedTheme == 3) mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(MainActivity.this, R.raw.lightmap));
        else if (selectedTheme == 4) mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(MainActivity.this, R.raw.dark));
        else if (selectedTheme/100 == 5){
            selectedTheme%=100;
            switch (getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) {
                case Configuration.UI_MODE_NIGHT_NO:
                    if (selectedTheme/10 == 1) mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(MainActivity.this, R.raw.standardmap));
                    else if (selectedTheme/10 == 3) mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(MainActivity.this, R.raw.lightmap));
                    break;
            case Configuration.UI_MODE_NIGHT_YES:
                selectedTheme%=10;
                if (selectedTheme == 2) mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(MainActivity.this, R.raw.night));
                else if (selectedTheme == 4) mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(MainActivity.this, R.raw.dark));
                break;
            }
        }

        //getting my location
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            isLocationGranted = true;
            initmap();
            googleMap.setMyLocationEnabled(true);
            //Toast.makeText(this, "constant", Toast.LENGTH_SHORT).show();
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }


    }

    public void sendCoordinateToFirebase()
    {


        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            isLocationGranted = true;

            int delay = 1000;   // delay for 5 sec.
            int interval = 3000;  // iterate every sec.
            Timer timer = new Timer();

            timer.scheduleAtFixedRate(new TimerTask() {
                public void run() {
                    sendlastknownlocation();
                }
            }, delay, interval);

        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }

    }
    public void gototest(View view){
        startActivity(new Intent(getApplicationContext(),FirebaseGetDataTest.class));
    }


    /*@Override
    public void onBackPressed() {
        SharedPreferences sharedPreferences = getSharedPreferences("system global variables",MODE_PRIVATE);
        Boolean regbool = sharedPreferences.getBoolean("Registered",false);
        Boolean loginbool = sharedPreferences.getBoolean("LoggedIn",false);
        if (regbool==loginbool&&loginbool==true)
        {
            moveTaskToBack(true);
        }
        else {
            super.onBackPressed();
        }
    }*/

    public void getDriverLocationLive()
    {

        final Marker marker1 = mMap.addMarker(new MarkerOptions().position(new LatLng(30,30)).title("Vedank").icon(BitmapDescriptorFactory.fromResource((R.drawable.busify_g))).visible(true));
        final Marker marker2 = mMap.addMarker(new MarkerOptions().position(new LatLng(60,60)).title("Shantanu").icon(BitmapDescriptorFactory.fromResource(R.drawable.busifyh)).visible(true));
        driverMarker.add(marker1);
        driverMarker.add(marker2);
        driverMarkers.put("c6j3SIAX38W1uSWGBjDGeXmHBVw2",marker1);
        driverMarkers.put("SOzNZgTpoNZj14OC2F97vqrqI2k1",marker2);
            for(Marker marker:driverMarker)
            {
                marker.setVisible(true);
            }

        mDB.collection("locations")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value,
                                        @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.w(TAG, "Listen failed.", e);
                            return;
                        }
                        for (QueryDocumentSnapshot doc : value) {
                            if (doc.getGeoPoint("Location") != null) {

                                GeoPoint geoPoint = doc.getGeoPoint("Location");
                                LatLng latLng = new LatLng(geoPoint.getLatitude(),geoPoint.getLongitude());
                                    if(doc.getId().equals("c6j3SIAX38W1uSWGBjDGeXmHBVw2"))
                                    {
                                        marker1.setPosition(latLng);
                                    }
                                    if(doc.getId().equals("SOzNZgTpoNZj14OC2F97vqrqI2k1"))
                                    {
                                        marker2.setPosition(latLng);
                                    }

                            }
                        }
                    }
                });
    }

    public void getMyDriverLocationLive(String selectedBus)
    {
        for (Marker markers : driverMarker)
        {
            markers.setVisible(false); //setting all markers to hidden
        }
        final String myBus = UID_Bus_map.get(selectedBus); //gets UID from users bus letter
        final Marker myMarker = driverMarkers.get(myBus);  // gets marker for the users bus letter
        myMarker.setVisible(true); //make users driver marker visible

        mDB.collection("locations")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value,
                                        @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.w(TAG, "Listen failed.", e);
                            return;
                        }
                        for (QueryDocumentSnapshot doc : value) {
                            if (doc.getGeoPoint("Location") != null) {
                                GeoPoint geoPoint = doc.getGeoPoint("Location");
                                LatLng latLng = new LatLng(geoPoint.getLatitude(),geoPoint.getLongitude());
                                if(doc.getId().equals(myBus))
                                {
                                    myMarker.setPosition(latLng); //set position of only users driver marker
                                }

                            }
                        }
                    }
                });
    }




    }


