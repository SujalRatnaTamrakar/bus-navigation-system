package com.project.of.busnavigationsystem.MainActivities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PointF;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.mapbox.android.core.location.LocationEngine;
import com.mapbox.android.core.location.LocationEngineCallback;
import com.mapbox.android.core.location.LocationEngineProvider;
import com.mapbox.android.core.location.LocationEngineRequest;
import com.mapbox.android.core.location.LocationEngineResult;
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.api.directions.v5.MapboxDirections;
import com.mapbox.api.directions.v5.models.DirectionsRoute;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.location.LocationComponent;
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions;
import com.mapbox.mapboxsdk.location.modes.CameraMode;
import com.mapbox.mapboxsdk.location.modes.RenderMode;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.onesignal.OneSignal;
import com.project.of.busnavigationsystem.MainActivities.AlertDialog.AlertRoutesAdapter;
import com.project.of.busnavigationsystem.MainActivities.AlertDialog.CustomAlertDialog;
import com.project.of.busnavigationsystem.MainActivities.DriverRouteOperations.GetRoute;
import com.project.of.busnavigationsystem.MainActivities.DriverSpeed.CLocation;
import com.project.of.busnavigationsystem.MainActivities.DriverSpeed.IBaseGpsListener;
import com.project.of.busnavigationsystem.NavBar.Profile.Driver;
import com.project.of.busnavigationsystem.NavBar.Routes.BusListActivity;
import com.project.of.busnavigationsystem.Login.LoginActivity;
import com.project.of.busnavigationsystem.R;
import com.project.of.busnavigationsystem.NavBar.SettingsActivity;
import com.project.of.busnavigationsystem.NavBar.Profile.readdriver;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconAllowOverlap;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconIgnorePlacement;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconImage;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconOffset;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineCap;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineColor;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineJoin;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineWidth;
import static com.project.of.busnavigationsystem.MainActivities.DriverRouteOperations.GetRoute.currentRoute;

/**
 * Include a map fragment within your app using Android support library.
 */
public class MainActivityy_driver extends AppCompatActivity implements PermissionsListener, IBaseGpsListener {
    LocationManager locationManager;
    LocationListener locationListener;
    FirebaseAuth myFirebaseAuth;
    private FirebaseAuth.AuthStateListener myAuthStateListener;
    private DrawerLayout dl;
    private ActionBarDrawerToggle t;
    private NavigationView nv;
    private PermissionsManager permissionsManager;
    private MapboxMap mMap;
    private LocationEngine locationEngine;
    private Location originLocation;
    private MapView mapView;
    private CustomAlertDialog customAlertDialog;
    //MapboxNavigation navigation = new MapboxNavigation(MainActivityy.this,getString(R.string.mapbox_key));

    //drawing route
    private static final String ROUTE_LAYER_ID = "route-layer-id";
    private static final String ROUTE_SOURCE_ID = "route-source-id";
    private static final String ICON_LAYER_ID = "icon-layer-id";
    private static final String ICON_SOURCE_ID = "icon-source-id";
    private static final String RED_PIN_ICON_ID = "red-pin-icon-id";
    private DirectionsRoute currentRout;
    private MapboxDirections client;
    private Point origin;
    private Point destination;
    private FloatingActionButton share, ns;
    private List<String> driverRoutes = new ArrayList<>();
    String[] routes;
    public static String driverSelectedRoute;
    public static LocationListener listener;

    //location
    private long DEFAULT_INTERVAL_IN_MILLISECONDS = 1000L;
    private long DEFAULT_MAX_WAIT_TIME = DEFAULT_INTERVAL_IN_MILLISECONDS * 5;
    // Variables needed to listen to location updates
    private MainActivityLocationCallback callback = new MainActivityLocationCallback(this);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Mapbox access token is configured here. This needs to be called either in your application
        // object or in the same activity which contains the mapview.
        Mapbox.getInstance(this, getString(R.string.mapbox_key));
        setContentView(R.layout.activity_bus);

        //pushNoti
        OneSignal.startInit(this)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .unsubscribeWhenNotificationsAreDisabled(true)
                .init();


        //to update speed and give permission
        final LocationManager[] locationManager = {(LocationManager) this.getSystemService(Context.LOCATION_SERVICE)};
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
        locationManager[0].requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, (LocationListener) this);
        this.updateSpeed(null);

        CheckBox chkUseMetricUntis = (CheckBox) this.findViewById(R.id.chkMetricUnits);
        chkUseMetricUntis.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // TODO Auto-generated method stub
                MainActivityy_driver.this.updateSpeed(null);
            }

        });

        //options for selecting route
        myFirebaseAuth = FirebaseAuth.getInstance();
        final DatabaseReference routereff = FirebaseDatabase.getInstance().getReference().child("Driver");
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser drivertest = auth.getCurrentUser();
        if (drivertest != null) {
            String email = drivertest.getEmail();
            DatabaseReference dbreff = FirebaseDatabase.getInstance().getReference();
            Query query = dbreff.child("Driver").orderByChild("email").equalTo(email);
            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Map<String, Object> td = (Map<String, Object>) dataSnapshot.getValue();
                    if (td != null) {
                        routereff.child(td.keySet().iterator().next()).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                Driver driver = dataSnapshot.getValue(Driver.class);
                                routereff.child(td.keySet().iterator().next() + "/busRoute").addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        GenericTypeIndicator<List<String>> t = new GenericTypeIndicator<List<String>>() {
                                        };
                                        List<String> routesList = dataSnapshot.getValue(t);
                                        driverRoutes.addAll(routesList);
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }


                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

        dl = (DrawerLayout) findViewById(R.id.activity_main);
        t = new ActionBarDrawerToggle(this, dl, R.string.Open, R.string.Close);
        dl.addDrawerListener(t);
        t.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Navigation Bar
        nv = (NavigationView) findViewById(R.id.nv);
        nv.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch (id) {
                    case R.id.myprofile:
                        //Toast.makeText(MainActivityy.this, "My Profile", Toast.LENGTH_SHORT).show();

                        Intent intent1 = new Intent(MainActivityy_driver.this, readdriver.class);
                        startActivity(intent1);

                        break;
                    /*case R.id.destination:
                        Toast.makeText(MainActivityy_driver.this, "Destination Driver activity", Toast.LENGTH_SHORT).show();
                        break;*/
                    case R.id.Routes:
                        //Toast.makeText(MainActivityy.this, "Bus", Toast.LENGTH_SHORT).show();
                        Intent intent3 = new Intent(MainActivityy_driver.this, BusListActivity.class);
                        startActivity(intent3);
                        break;
                    case R.id.settings:
                        //Toast.makeText(MainActivityy.this, "Settings", Toast.LENGTH_SHORT).show();
                        Intent intent4 = new Intent(MainActivityy_driver.this, SettingsActivity.class);
                        startActivity(intent4);
                        break;
                    case R.id.logout:
                        FirebaseAuth.getInstance().signOut();
                        Intent intToLogIn = new Intent(MainActivityy_driver.this, LoginActivity.class);
                        startActivity(intToLogIn);
                        finish();
                    default:
                        return true;
                }
                return true;


            }
        });


        mapView = (MapView) findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull MapboxMap mapboxMap) {
                MainActivityy_driver.this.mMap = mapboxMap;

                mapboxMap.getUiSettings().setAttributionEnabled(false);
                mapboxMap.getUiSettings().setLogoEnabled(false);
                mapboxMap.setStyle(Style.MAPBOX_STREETS, new Style.OnStyleLoaded() {
                    @Override
                    public void onStyleLoaded(@NonNull Style style) {
                        // Map is set up and the style has loaded. Now you can add data or make other map adjustments
                        enableLocationComponent(style);
                        Location lastLocation = mMap.getLocationComponent().getLastKnownLocation();
                        if (!PermissionsManager.areLocationPermissionsGranted(MainActivityy_driver.this)) {
                            permissionsManager = new PermissionsManager(MainActivityy_driver.this);
                            permissionsManager.requestLocationPermissions(MainActivityy_driver.this);

                        } else if (lastLocation == null) {
                            if (ActivityCompat.checkSelfPermission(MainActivityy_driver.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MainActivityy_driver.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                // TODO: Consider calling
                                //    ActivityCompat#requestPermissions
                                // here to request the missing permissions, and then overriding
                                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                //                                          int[] grantResults)
                                // to handle the case where the user grants the permission. See the documentation
                                // for ActivityCompat#requestPermissions for more details.
                                return;
                            }
                            locationEngine.getLastLocation(new LocationEngineCallback<LocationEngineResult>() {
                                @Override
                                public void onSuccess(LocationEngineResult result) {
                                    Location location = result.getLastLocation();
                                    if (location != null) {
                                        CameraPosition position = new CameraPosition.Builder()
                                                .target(new LatLng(location))
                                                .zoom(15)
                                                .tilt(40)
                                                .bearing(90)
                                                .build();

                                        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(position), 3000);
                                    } else {
                                        new AlertDialog.Builder(MainActivityy_driver.this)
                                                .setTitle("Location Services turned off!")
                                                .setMessage("Please turn on location service on your device!")
                                                .show();
                                    }
                                }

                                @Override
                                public void onFailure(@NonNull Exception exception) {
                                    new AlertDialog.Builder(MainActivityy_driver.this)
                                            .setTitle("Location Services turned off!")
                                            .setMessage("Please turn on location service on your device!")
                                            .show();
                                }
                            });

                        } else {

                            CameraPosition position = new CameraPosition.Builder()
                                    .target(new LatLng(lastLocation))
                                    .zoom(15)
                                    .tilt(40)
                                    .bearing(90)
                                    .build();

                            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(position), 3000);
                        }

                    }
                });

                mapboxMap.addOnMapClickListener(new MapboxMap.OnMapClickListener() {
                    @Override
                    public boolean onMapClick(@NonNull LatLng point) {
                        PointF screenPoint = mapboxMap.getProjection().toScreenLocation(point);
                        List<Feature> features = mapboxMap.queryRenderedFeatures(screenPoint, ROUTE_LAYER_ID);
                        if (features.isEmpty()) {
                            mapboxMap.getStyle(new Style.OnStyleLoaded() {
                                @Override
                                public void onStyleLoaded(@NonNull Style style) {
                                    if (style.getSource(ROUTE_SOURCE_ID) != null) {
                                        style.removeImage(RED_PIN_ICON_ID);
                                        style.removeLayer(ICON_LAYER_ID);
                                        style.removeSource(ICON_SOURCE_ID);
                                        style.removeLayer(ROUTE_LAYER_ID);
                                        style.removeSource(ROUTE_SOURCE_ID);
                                    }
                                }
                            });
                        }
                        return true;
                    }
                });

                //GPS floating button
                FloatingActionButton fab = findViewById(R.id.floatingActionButton);
                fab.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Location lastLocation = mMap.getLocationComponent().getLastKnownLocation();
                        if (lastLocation != null) {
                            CameraPosition position = new CameraPosition.Builder()
                                    .target(new LatLng(lastLocation))
                                    .zoom(15)
                                    .tilt(40)
                                    .bearing(90)
                                    .build();
                            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(position), 3000);
                        } else {
                            new AlertDialog.Builder(MainActivityy_driver.this)
                                    .setTitle("Location Services turned off!")
                                    .setMessage("Please turn on location service on your device!")
                                    .show();
                        }

                    }
                });

                //sharing location and stopping sharing location
                share = findViewById(R.id.shr);
                ns = findViewById(R.id.stop);

                share.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        //Asking to select the route
                        routes = driverRoutes.toArray(new String[driverRoutes.size()]);

                        DialogInterface.OnDismissListener onDismissListenerShowShare = new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialog) {
                                share.setEnabled(true);
                                share.setVisibility(View.VISIBLE);
                                ns.setVisibility(View.INVISIBLE);
                                ns.setEnabled(false);
                            }
                        };
                        DialogInterface.OnDismissListener onDismissListenerHideShare = new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialog) {
                                share.setEnabled(false);
                                share.setVisibility(View.INVISIBLE);
                                ns.setVisibility(View.VISIBLE);
                                ns.setEnabled(true);
                            }
                        };

                       AlertRoutesAdapter alertRoutesAdapter = new AlertRoutesAdapter(routes, new AlertRoutesAdapter.RecyclerViewItemClickListener() {
                            @RequiresApi(api = Build.VERSION_CODES.Q)
                            @Override
                            public void clickOnItem(String data) {
                                Toast.makeText(MainActivityy_driver.this, "Started Sharing", Toast.LENGTH_SHORT).show();
                                switch (data) {
                                    case "Attarkhel-Purano Buspark":
                                        driverSelectedRoute = "Attarkhel-Purano Buspark";
                                        GetRoute.getRoute(mapboxMap,
                                                Point.fromLngLat(Double.parseDouble(getString(R.string.Attarkhel_long)), Double.parseDouble(getString(R.string.Attarkhel_lat))),
                                                Point.fromLngLat(Double.parseDouble(getString(R.string.PuranoBuspark_long)), Double.parseDouble(getString(R.string.PuranoBuspark_lat))), getApplicationContext(), MainActivityy_driver.this, "MainActivityy_driver",data);
                                        break;
                                    case "Lagankhel-Jawalakhel-Kupondol-Tripureshor":
                                        driverSelectedRoute = "Lagankhel-Jawalakhel-Kupondol-Tripureshor";
                                        GetRoute.getRoute(mapboxMap,
                                                Point.fromLngLat(Double.parseDouble(getString(R.string.Lagankhel_long)), Double.parseDouble(getString(R.string.Lagankhel_lat))),
                                                Point.fromLngLat(Double.parseDouble(getString(R.string.Tripureshor_long)), Double.parseDouble(getString(R.string.Tripureshor_lat))), getApplicationContext(), MainActivityy_driver.this, "MainActivityy_driver",data);
                                        break;
                                    case "Lagankhel-Kupondol-Thapathali":
                                        driverSelectedRoute = "Lagankhel-Kupondol-Thapathali";
                                        GetRoute.getRoute(mapboxMap,
                                                Point.fromLngLat(Double.parseDouble(getString(R.string.Lagankhel_long)), Double.parseDouble(getString(R.string.Lagankhel_lat))),
                                                Point.fromLngLat(Double.parseDouble(getString(R.string.Thapathali_long)), Double.parseDouble(getString(R.string.Thapathali_lat))), getApplicationContext(), MainActivityy_driver.this, "MainActivityy_driver",data);
                                        break;
                                    case "Bungamati-Bhaisipati-Charghare":
                                        driverSelectedRoute = "Bungamati-Bhaisipati-Charghare";
                                        GetRoute.getRoute(mapboxMap,
                                                Point.fromLngLat(Double.parseDouble(getString(R.string.Bungamati_long)), Double.parseDouble(getString(R.string.Bungamati_lat))),
                                                Point.fromLngLat(Double.parseDouble(getString(R.string.Charghare_long)), Double.parseDouble(getString(R.string.Charghare_lat))), getApplicationContext(), MainActivityy_driver.this, "MainActivityy_driver",data);
                                        break;
                                    case "Bagbazaar-Ratnapark":
                                        driverSelectedRoute = "Bagbazaar-Ratnapark";
                                        GetRoute.getRoute(mapboxMap,
                                                Point.fromLngLat(Double.parseDouble(getString(R.string.Bagbazaar_long)), Double.parseDouble(getString(R.string.Bagbazaar_lat))),
                                                Point.fromLngLat(Double.parseDouble(getString(R.string.Ratnapark_long)), Double.parseDouble(getString(R.string.Ratnapark_lat))), getApplicationContext(), MainActivityy_driver.this, "MainActivityy_driver",data);
                                        break;
                                    case "Bagbazaar-Kamalbinayak":
                                        driverSelectedRoute = "Bagbazaar-Kamalbinayak";
                                        GetRoute.getRoute(mapboxMap,
                                                Point.fromLngLat(Double.parseDouble(getString(R.string.Bagbazaar_long)), Double.parseDouble(getString(R.string.Bagbazaar_lat))),
                                                Point.fromLngLat(Double.parseDouble(getString(R.string.Kamalbinayak_long)), Double.parseDouble(getString(R.string.Kamalbinayak_lat))), getApplicationContext(), MainActivityy_driver.this, "MainActivityy_driver",data);
                                        break;
                                    case "Balaju-Ratnapark":
                                        driverSelectedRoute = "Balaju-Ratnapark";
                                        GetRoute.getRoute(mapboxMap,
                                                Point.fromLngLat(Double.parseDouble(getString(R.string.Balaju_long)), Double.parseDouble(getString(R.string.Balaju_lat))),
                                                Point.fromLngLat(Double.parseDouble(getString(R.string.Ratnapark_long)), Double.parseDouble(getString(R.string.Ratnapark_lat))), getApplicationContext(), MainActivityy_driver.this, "MainActivityy_driver",data);
                                        break;
                                    case "Balkhu-NayaBaneshwor-Sankhamul":
                                        driverSelectedRoute = "Balkhu-NayaBaneshwor-Sankhamul";
                                        GetRoute.getRoute(mapboxMap,
                                                Point.fromLngLat(Double.parseDouble(getString(R.string.Balkhu_long)), Double.parseDouble(getString(R.string.Balkhu_lat))),
                                                Point.fromLngLat(Double.parseDouble(getString(R.string.Sankhamul_long)), Double.parseDouble(getString(R.string.Sankhamul_lat))), getApplicationContext(), MainActivityy_driver.this, "MainActivityy_driver",data);
                                        break;
                                    case "Balkhu-NayaBaneshwor-Shantinagar-Bhatkya Pul":
                                        driverSelectedRoute = "Balkhu-NayaBaneshwor-Shantinagar-Bhatkya Pul";
                                        GetRoute.getRoute(mapboxMap,
                                                Point.fromLngLat(Double.parseDouble(getString(R.string.Balkhu_long)), Double.parseDouble(getString(R.string.Balkhu_lat))),
                                                Point.fromLngLat(Double.parseDouble(getString(R.string.BhatkyaPul_long)), Double.parseDouble(getString(R.string.BhatkyaPul_lat))), getApplicationContext(), MainActivityy_driver.this, "MainActivityy_driver",data);
                                        break;
                                    case "Balkumari-Gopi Krishna":
                                        driverSelectedRoute = "Balkumari-Gopi Krishna";
                                        GetRoute.getRoute(mapboxMap,
                                                Point.fromLngLat(Double.parseDouble(getString(R.string.Balkumari_long)), Double.parseDouble(getString(R.string.Balkumari_lat))),
                                                Point.fromLngLat(Double.parseDouble(getString(R.string.GopiKrishna_long)), Double.parseDouble(getString(R.string.GopiKrishna_lat))), getApplicationContext(), MainActivityy_driver.this, "MainActivityy_driver",data);
                                        break;
                                    case "Bhaktapur-Purano Thimi-Purano Bus Park":
                                        driverSelectedRoute = "Bhaktapur-Purano Thimi-Purano Bus Park";
                                        GetRoute.getRoute(mapboxMap,
                                                Point.fromLngLat(Double.parseDouble(getString(R.string.Bhaktapur_long)), Double.parseDouble(getString(R.string.Bhaktapur_lat))),
                                                Point.fromLngLat(Double.parseDouble(getString(R.string.PuranoBuspark_long)), Double.parseDouble(getString(R.string.PuranoBuspark_lat))), getApplicationContext(), MainActivityy_driver.this, "MainActivityy_driver",data);
                                        break;
                                    case "Budhanilkantha School-Ratnapark":
                                        driverSelectedRoute = "Budhanilkantha School-Ratna Park";
                                        GetRoute.getRoute(mapboxMap,
                                                Point.fromLngLat(Double.parseDouble(getString(R.string.BudhanilkanthaSchool_long)), Double.parseDouble(getString(R.string.BudhanilkanthaSchool_lat))),
                                                Point.fromLngLat(Double.parseDouble(getString(R.string.Ratnapark_long)), Double.parseDouble(getString(R.string.Ratnapark_lat))), getApplicationContext(), MainActivityy_driver.this, "MainActivityy_driver",data);
                                        break;
                                    case "Changu-Ratnapark":
                                        driverSelectedRoute = "Changu-Ratnapark";
                                        GetRoute.getRoute(mapboxMap,
                                                Point.fromLngLat(Double.parseDouble(getString(R.string.Changu_long)), Double.parseDouble(getString(R.string.Changu_lat))),
                                                Point.fromLngLat(Double.parseDouble(getString(R.string.Ratnapark_long)), Double.parseDouble(getString(R.string.Ratnapark_lat))), getApplicationContext(), MainActivityy_driver.this, "MainActivityy_driver",data);
                                        break;
                                    case "Ratnapark-Samakhusi Chowk":
                                        driverSelectedRoute = "Ratnapark-Samakhusi Chowk";
                                        GetRoute.getRoute(mapboxMap,
                                                Point.fromLngLat(Double.parseDouble(getString(R.string.Ratnapark_long)), Double.parseDouble(getString(R.string.Ratnapark_lat))),
                                                Point.fromLngLat(Double.parseDouble(getString(R.string.SamakhusiChowk_long)), Double.parseDouble(getString(R.string.SamakhusiChowk_lat))), getApplicationContext(), MainActivityy_driver.this, "MainActivityy_driver",data);
                                        break;
                                    case "Boudha-Dillibazaar":
                                        driverSelectedRoute = "Boudha-Dillibazaar";
                                        GetRoute.getRoute(mapboxMap,
                                                Point.fromLngLat(Double.parseDouble(getString(R.string.Boudha_long)), Double.parseDouble(getString(R.string.Boudha_lat))),
                                                Point.fromLngLat(Double.parseDouble(getString(R.string.Dillibazaar_long)), Double.parseDouble(getString(R.string.Dillibazaar_lat))), getApplicationContext(), MainActivityy_driver.this, "MainActivityy_driver",data);
                                        break;
                                    case "Putalisadak-Pulchowk":
                                        driverSelectedRoute = "Putalisadak-Pulchowk";
                                        GetRoute.getRoute(mapboxMap,
                                                Point.fromLngLat(Double.parseDouble(getString(R.string.Putalisadak_long)), Double.parseDouble(getString(R.string.Putalisadak_lat))),
                                                Point.fromLngLat(Double.parseDouble(getString(R.string.Pulchowk_long)), Double.parseDouble(getString(R.string.Pulchowk_lat))), getApplicationContext(), MainActivityy_driver.this, "MainActivityy_driver",data);
                                        break;
                                }
                                customAlertDialog.setOnDismissListener(onDismissListenerHideShare);
                                customAlertDialog.dismiss();

                            }

                        });
                        customAlertDialog = new CustomAlertDialog(MainActivityy_driver.this,alertRoutesAdapter);
                        customAlertDialog.show();
                        customAlertDialog.setCanceledOnTouchOutside(false);
                        customAlertDialog.setOnDismissListener(onDismissListenerShowShare);

                      /*OLDALERDIALOG
                        AlertDialog.Builder window = new AlertDialog.Builder(MainActivityy_driver.this);
                        window.setCancelable(false);
                        window.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                share.setEnabled(true);
                                share.setVisibility(View.VISIBLE);
                                ns.setVisibility(View.INVISIBLE);
                                ns.setEnabled(false);
                            };
                        });
                        window.setTitle("Select a route and start sharing your location!");
                        window.setItems(routes, new DialogInterface.OnClickListener() {
                            @RequiresApi(api = Build.VERSION_CODES.Q)
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(MainActivityy_driver.this, "Started Sharing", Toast.LENGTH_SHORT).show();
                                switch (routes[which]) {
                                    case "Attarkhel-Purano Buspark":
                                        driverSelectedRoute = "Attarkhel-Purano Buspark";
                                        GetRoute.getRoute(mapboxMap,
                                                Point.fromLngLat(Double.parseDouble(getString(R.string.Attarkhel_long)), Double.parseDouble(getString(R.string.Attarkhel_lat))),
                                                Point.fromLngLat(Double.parseDouble(getString(R.string.PuranoBuspark_long)), Double.parseDouble(getString(R.string.PuranoBuspark_lat))), getApplicationContext(), MainActivityy_driver.this, "MainActivityy_driver",mapView);
                                        break;
                                    case "Lagankhel-Jawalakhel-Kupondol-Tripureshor":
                                        driverSelectedRoute = "Lagankhel-Jawalakhel-Kupondol-Tripureshor";
                                        GetRoute.getRoute(mapboxMap,
                                                Point.fromLngLat(Double.parseDouble(getString(R.string.Lagankhel_long)), Double.parseDouble(getString(R.string.Lagankhel_lat))),
                                                Point.fromLngLat(Double.parseDouble(getString(R.string.Tripureshor_long)), Double.parseDouble(getString(R.string.Tripureshor_lat))), getApplicationContext(), MainActivityy_driver.this, "MainActivityy_driver",mapView);
                                        break;
                                    case "Lagankhel-Kupondol-Thapathali":
                                        driverSelectedRoute = "Lagankhel-Kupondol-Thapathali";
                                        GetRoute.getRoute(mapboxMap,
                                                Point.fromLngLat(Double.parseDouble(getString(R.string.Lagankhel_long)), Double.parseDouble(getString(R.string.Lagankhel_lat))),
                                                Point.fromLngLat(Double.parseDouble(getString(R.string.Thapathali_long)), Double.parseDouble(getString(R.string.Thapathali_lat))), getApplicationContext(), MainActivityy_driver.this, "MainActivityy_driver",mapView);
                                        break;
                                    case "Bungamati-Bhaisipati-Charghare":
                                        driverSelectedRoute = "Bungamati-Bhaisipati-Charghare";
                                        GetRoute.getRoute(mapboxMap,
                                                Point.fromLngLat(Double.parseDouble(getString(R.string.Bungamati_long)), Double.parseDouble(getString(R.string.Bungamati_lat))),
                                                Point.fromLngLat(Double.parseDouble(getString(R.string.Charghare_long)), Double.parseDouble(getString(R.string.Charghare_lat))), getApplicationContext(), MainActivityy_driver.this, "MainActivityy_driver",mapView);
                                        break;
                                    case "Bagbazaar-Ratnapark":
                                        driverSelectedRoute = "Bagbazaar-Ratnapark";
                                        GetRoute.getRoute(mapboxMap,
                                                Point.fromLngLat(Double.parseDouble(getString(R.string.Bagbazaar_long)), Double.parseDouble(getString(R.string.Bagbazaar_lat))),
                                                Point.fromLngLat(Double.parseDouble(getString(R.string.Ratnapark_long)), Double.parseDouble(getString(R.string.Ratnapark_lat))), getApplicationContext(), MainActivityy_driver.this, "MainActivityy_driver",mapView);
                                        break;
                                    case "Bagbazaar-Kamalbinayak":
                                        driverSelectedRoute = "Bagbazaar-Kamalbinayak";
                                        GetRoute.getRoute(mapboxMap,
                                                Point.fromLngLat(Double.parseDouble(getString(R.string.Bagbazaar_long)), Double.parseDouble(getString(R.string.Bagbazaar_lat))),
                                                Point.fromLngLat(Double.parseDouble(getString(R.string.Kamalbinayak_long)), Double.parseDouble(getString(R.string.Kamalbinayak_lat))), getApplicationContext(), MainActivityy_driver.this, "MainActivityy_driver",mapView);
                                        break;
                                    case "Balaju-Ratnapark":
                                        driverSelectedRoute = "Balaju-Ratnapark";
                                        GetRoute.getRoute(mapboxMap,
                                                Point.fromLngLat(Double.parseDouble(getString(R.string.Balaju_long)), Double.parseDouble(getString(R.string.Balaju_lat))),
                                                Point.fromLngLat(Double.parseDouble(getString(R.string.Ratnapark_long)), Double.parseDouble(getString(R.string.Ratnapark_lat))), getApplicationContext(), MainActivityy_driver.this, "MainActivityy_driver",mapView);
                                        break;
                                    case "Balkhu-NayaBaneshwor-Sankhamul":
                                        driverSelectedRoute = "Balkhu-NayaBaneshwor-Sankhamul";
                                        GetRoute.getRoute(mapboxMap,
                                                Point.fromLngLat(Double.parseDouble(getString(R.string.Balkhu_long)), Double.parseDouble(getString(R.string.Balkhu_lat))),
                                                Point.fromLngLat(Double.parseDouble(getString(R.string.Sankhamul_long)), Double.parseDouble(getString(R.string.Sankhamul_lat))), getApplicationContext(), MainActivityy_driver.this, "MainActivityy_driver",mapView);
                                        break;
                                    case "Balkhu-NayaBaneshwor-Shantinagar-Bhatkya Pul":
                                        driverSelectedRoute = "Balkhu-NayaBaneshwor-Shantinagar-Bhatkya Pul";
                                        GetRoute.getRoute(mapboxMap,
                                                Point.fromLngLat(Double.parseDouble(getString(R.string.Balkhu_long)), Double.parseDouble(getString(R.string.Balkhu_lat))),
                                                Point.fromLngLat(Double.parseDouble(getString(R.string.BhatkyaPul_long)), Double.parseDouble(getString(R.string.BhatkyaPul_lat))), getApplicationContext(), MainActivityy_driver.this, "MainActivityy_driver",mapView);
                                        break;
                                    case "Balkumari-Gopi Krishna":
                                        driverSelectedRoute = "Balkumari-Gopi Krishna";
                                        GetRoute.getRoute(mapboxMap,
                                                Point.fromLngLat(Double.parseDouble(getString(R.string.Balkumari_long)), Double.parseDouble(getString(R.string.Balkumari_lat))),
                                                Point.fromLngLat(Double.parseDouble(getString(R.string.GopiKrishna_long)), Double.parseDouble(getString(R.string.GopiKrishna_lat))), getApplicationContext(), MainActivityy_driver.this, "MainActivityy_driver",mapView);
                                        break;
                                    case "Bhaktapur-Purano Thimi-Purano Bus Park":
                                        driverSelectedRoute = "Bhaktapur-Purano Thimi-Purano Bus Park";
                                        GetRoute.getRoute(mapboxMap,
                                                Point.fromLngLat(Double.parseDouble(getString(R.string.Bhaktapur_long)), Double.parseDouble(getString(R.string.Bhaktapur_lat))),
                                                Point.fromLngLat(Double.parseDouble(getString(R.string.PuranoBuspark_long)), Double.parseDouble(getString(R.string.PuranoBuspark_lat))), getApplicationContext(), MainActivityy_driver.this, "MainActivityy_driver",mapView);
                                        break;
                                    case "Budhanilkantha School-Ratnapark":
                                        driverSelectedRoute = "Budhanilkantha School-Ratna Park";
                                        GetRoute.getRoute(mapboxMap,
                                                Point.fromLngLat(Double.parseDouble(getString(R.string.BudhanilkanthaSchool_long)), Double.parseDouble(getString(R.string.BudhanilkanthaSchool_lat))),
                                                Point.fromLngLat(Double.parseDouble(getString(R.string.Ratnapark_long)), Double.parseDouble(getString(R.string.Ratnapark_lat))), getApplicationContext(), MainActivityy_driver.this, "MainActivityy_driver",mapView);
                                        break;
                                    case "Changu-Ratnapark":
                                        driverSelectedRoute = "Changu-Ratnapark";
                                        GetRoute.getRoute(mapboxMap,
                                                Point.fromLngLat(Double.parseDouble(getString(R.string.Changu_long)), Double.parseDouble(getString(R.string.Changu_lat))),
                                                Point.fromLngLat(Double.parseDouble(getString(R.string.Ratnapark_long)), Double.parseDouble(getString(R.string.Ratnapark_lat))), getApplicationContext(), MainActivityy_driver.this, "MainActivityy_driver",mapView);
                                        break;
                                    case "Ratnapark-Samakhusi Chowk":
                                        driverSelectedRoute = "Ratnapark-Samakhusi Chowk";
                                        GetRoute.getRoute(mapboxMap,
                                                Point.fromLngLat(Double.parseDouble(getString(R.string.Ratnapark_long)), Double.parseDouble(getString(R.string.Ratnapark_lat))),
                                                Point.fromLngLat(Double.parseDouble(getString(R.string.SamakhusiChowk_long)), Double.parseDouble(getString(R.string.SamakhusiChowk_lat))), getApplicationContext(), MainActivityy_driver.this, "MainActivityy_driver",mapView);
                                        break;
                                    case "Boudha-Dillibazaar":
                                        driverSelectedRoute = "Boudha-Dillibazaar";
                                        GetRoute.getRoute(mapboxMap,
                                                Point.fromLngLat(Double.parseDouble(getString(R.string.Boudha_long)), Double.parseDouble(getString(R.string.Boudha_lat))),
                                                Point.fromLngLat(Double.parseDouble(getString(R.string.Dillibazaar_long)), Double.parseDouble(getString(R.string.Dillibazaar_lat))), getApplicationContext(), MainActivityy_driver.this, "MainActivityy_driver",mapView);
                                        break;
                                    case "Putalisadak-Pulchowk":
                                        driverSelectedRoute = "Putalisadak-Pulchowk";
                                        GetRoute.getRoute(mapboxMap,
                                                Point.fromLngLat(Double.parseDouble(getString(R.string.Putalisadak_long)), Double.parseDouble(getString(R.string.Putalisadak_lat))),
                                                Point.fromLngLat(Double.parseDouble(getString(R.string.Pulchowk_long)), Double.parseDouble(getString(R.string.Pulchowk_lat))), getApplicationContext(), MainActivityy_driver.this, "MainActivityy_driver",mapView);
                                        break;
                                }

                            }


                        });
                        window.show();*/


                        listener = new LocationListener() {
                            @Override
                            public void onLocationChanged(Location location) {
                                if (getApplicationContext() != null && driverSelectedRoute != null) {
                                    location = mMap.getLocationComponent().getLastKnownLocation();
                                    LatLng latlng = new LatLng(location.getLatitude(), location.getLongitude());
                                    mMap.moveCamera(CameraUpdateFactory.newLatLng(latlng));


                                    String userid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                    DatabaseReference reff = FirebaseDatabase.getInstance().getReference("driversavailable/" + driverSelectedRoute);

                                    GeoFire geofire = new GeoFire(reff);
                                    geofire.setLocation(userid, new GeoLocation(location.getLatitude(), location.getLongitude()));
                                }
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
                        locationManager[0] = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
                        if (ActivityCompat.checkSelfPermission(MainActivityy_driver.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MainActivityy_driver.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            // TODO: Consider calling
                            //    ActivityCompat#requestPermissions
                            // here to request the missing permissions, and then overriding
                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                            //                                          int[] grantResults)
                            // to handle the case where the user grants the permission. See the documentation
                            // for ActivityCompat#requestPermissions for more details.
                            return;
                        }
                        locationManager[0].requestLocationUpdates(LocationManager.GPS_PROVIDER, 3, 2, listener);


                        share.setEnabled(false);
                        share.setVisibility(View.INVISIBLE);

                        ns.setEnabled(true);
                        ns.setVisibility(View.VISIBLE);

                        ns.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {


                                String userid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                DatabaseReference reff = FirebaseDatabase.getInstance().getReference("driversavailable/" + driverSelectedRoute);
                                GeoFire geofire = new GeoFire(reff);
                                geofire.removeLocation(userid);


                                ns.setEnabled(false);
                                share.setEnabled(true);
                                Toast.makeText(MainActivityy_driver.this, "Stopped Sharing", Toast.LENGTH_SHORT).show();
                                if (locationManager[0] != null) {
                                    if (GetRoute.navigation != null) {
                                        GetRoute.navigation.onDestroy();
                                    }
                                    //noinspection MissingPermission
                                    locationManager[0].removeUpdates(listener);
                                    locationManager[0].removeUpdates(MainActivityy_driver.this);
                                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
                                    if (GetRoute.progressChangeListener != null) {
                                        GetRoute.navigation.removeProgressChangeListener(GetRoute.progressChangeListener);
                                    }
                                    if (GetRoute.valueEventListener != null) {
                                        reference.child("routeDetails").child(driverSelectedRoute).child(userid).removeEventListener(GetRoute.valueEventListener);
                                        driverSelectedRoute = null;
                                    }
                                    reference.child("routeProgress").removeValue();


                                    //reff.removeEventListener(GetRoute.childEventListener);

                                }
                                share.setEnabled(true);
                                share.setVisibility(View.VISIBLE);
                                ns.setVisibility(View.INVISIBLE);
                                ns.setEnabled(false);
                            }
                        });

                    }

                });


            }
        });


    }


    // function to update speed
    private void updateSpeed(CLocation location) {
        // TODO Auto-generated method stub
        float nCurrentSpeed = 0;

        if (location != null) {
            location.setUseMetricunits(this.useMetricUnits());
            nCurrentSpeed = location.getSpeed();
        }

        Formatter fmt = new Formatter(new StringBuilder());
        fmt.format(Locale.US, "%5.1f", nCurrentSpeed);
        String strCurrentSpeed = fmt.toString();
        strCurrentSpeed = strCurrentSpeed.replace(' ', '0');

        String strUnits = "miles/hour";
        if (this.useMetricUnits()) {
            strUnits = "meters/second";
        }

        TextView txtCurrentSpeed = (TextView) this.findViewById(R.id.txtCurrentSpeed);
        txtCurrentSpeed.setText(strCurrentSpeed + " " + strUnits);
    }

    private boolean useMetricUnits() {
        // TODO Auto-generated method stub
        CheckBox chkUseMetricUnits = (CheckBox) this.findViewById(R.id.chkMetricUnits);
        return chkUseMetricUnits.isChecked();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (t.onOptionsItemSelected(item))
            return true;

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings({"MissingPermission"})
    private void enableLocationComponent(@NonNull Style loadedMapStyle) {
// Check if permissions are enabled and if not request
        if (PermissionsManager.areLocationPermissionsGranted(this)) {

// Get an instance of the component
            LocationComponent locationComponent = mMap.getLocationComponent();

// Set the LocationComponent activation options
            LocationComponentActivationOptions locationComponentActivationOptions =
                    LocationComponentActivationOptions.builder(this, loadedMapStyle)
                            .useDefaultLocationEngine(false)
                            .build();

// Activate with the LocationComponentActivationOptions object
            locationComponent.activateLocationComponent(locationComponentActivationOptions);

// Enable to make component visible
            locationComponent.setLocationComponentEnabled(true);

// Set the component's camera mode
            locationComponent.setCameraMode(CameraMode.TRACKING);

// Set the component's render mode
            locationComponent.setRenderMode(RenderMode.COMPASS);
            initLocationEngine();
        } else {
            permissionsManager = new PermissionsManager(this);
            permissionsManager.requestLocationPermissions(this);
        }


    }

    @SuppressLint("MissingPermission")
    private void initLocationEngine() {
        locationEngine = LocationEngineProvider.getBestLocationEngine(this);

        LocationEngineRequest request = new LocationEngineRequest.Builder(DEFAULT_INTERVAL_IN_MILLISECONDS)
                .setPriority(LocationEngineRequest.PRIORITY_HIGH_ACCURACY)
                .setMaxWaitTime(DEFAULT_MAX_WAIT_TIME).build();

        locationEngine.requestLocationUpdates(request, callback, getMainLooper());
        locationEngine.getLastLocation(callback);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        permissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onExplanationNeeded(List<String> permissionsToExplain) {
        Toast.makeText(this, R.string.user_location_permission_explanation, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onPermissionResult(boolean granted) {
        if (granted) {
            mMap.getStyle(new Style.OnStyleLoaded() {
                @Override
                public void onStyleLoaded(@NonNull Style style) {
                    enableLocationComponent(style);
                }
            });
        } else {
            Toast.makeText(this, R.string.user_location_permission_not_granted, Toast.LENGTH_LONG).show();
            finish();
        }
    }

    private static class MainActivityLocationCallback
            implements LocationEngineCallback<LocationEngineResult> {

        private final WeakReference<MainActivityy_driver> activityWeakReference;

        MainActivityLocationCallback(MainActivityy_driver activity) {
            this.activityWeakReference = new WeakReference<>(activity);
        }

        /**
         * The LocationEngineCallback interface's method which fires when the device's location has changed.
         *
         * @param result the LocationEngineResult object which has the last known location within it.
         */
        @Override
        public void onSuccess(LocationEngineResult result) {
            MainActivityy_driver activity = activityWeakReference.get();

            if (activity != null) {
                Location location = result.getLastLocation();

                if (location == null) {
                    return;
                }

// Pass the new location to the Maps SDK's LocationComponent
                if (activity.mMap != null && result.getLastLocation() != null) {
                    activity.mMap.getLocationComponent().forceLocationUpdate(result.getLastLocation());
                }
            }
        }

        /**
         * The LocationEngineCallback interface's method which fires when the device's location can not be captured
         *
         * @param exception the exception message
         */
        @Override
        public void onFailure(@NonNull Exception exception) {
            Log.d("LocationChangeActivity", exception.getLocalizedMessage());
            MainActivityy_driver activity = activityWeakReference.get();
            if (activity != null) {
                Toast.makeText(activity, exception.getLocalizedMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        // TODO Auto-generated method stub
        if (location != null) {
            CLocation myLocation = new CLocation(location, this.useMetricUnits());
            this.updateSpeed(myLocation);
        }
    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onGpsStatusChanged(int event) {

    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();

    }

    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
        if (GetRoute.navigation != null) {
            GetRoute.navigation.onDestroy();
        }
        if (locationManager != null) {
            //noinspection MissingPermission
            locationManager.removeUpdates(listener);
            locationManager.removeUpdates(MainActivityy_driver.this);
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
            GetRoute.navigation.removeProgressChangeListener(GetRoute.progressChangeListener);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
        if (GetRoute.navigation != null) {
            GetRoute.navigation.onDestroy();
        }
        if (locationManager != null) {
            //noinspection MissingPermission
            locationManager.removeUpdates(listener);
            locationManager.removeUpdates(MainActivityy_driver.this);
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
            GetRoute.navigation.removeProgressChangeListener(GetRoute.progressChangeListener);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }


}





