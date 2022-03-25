package com.project.of.busnavigationsystem.MainActivities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PointF;
import android.graphics.Typeface;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryDataEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mapbox.android.core.location.LocationEngine;
import com.mapbox.android.core.location.LocationEngineCallback;
import com.mapbox.android.core.location.LocationEngineProvider;
import com.mapbox.android.core.location.LocationEngineRequest;
import com.mapbox.android.core.location.LocationEngineResult;
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.api.directions.v5.DirectionsCriteria;
import com.mapbox.api.directions.v5.MapboxDirections;
import com.mapbox.api.directions.v5.models.DirectionsResponse;
import com.mapbox.api.directions.v5.models.DirectionsRoute;
import com.mapbox.api.geocoding.v5.models.CarmenFeature;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.LineString;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.geometry.LatLngBounds;
import com.mapbox.mapboxsdk.location.LocationComponent;
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions;
import com.mapbox.mapboxsdk.location.modes.CameraMode;
import com.mapbox.mapboxsdk.location.modes.RenderMode;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.plugins.markerview.MarkerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.mapbox.mapboxsdk.plugins.annotation.Symbol;
import com.mapbox.mapboxsdk.plugins.annotation.SymbolManager;
import com.mapbox.mapboxsdk.plugins.annotation.SymbolOptions;
import com.mapbox.mapboxsdk.plugins.markerview.MarkerViewManager;
import com.mapbox.mapboxsdk.plugins.places.autocomplete.PlaceAutocomplete;
import com.mapbox.mapboxsdk.plugins.places.autocomplete.model.PlaceOptions;
import com.mapbox.mapboxsdk.style.layers.LineLayer;
import com.mapbox.mapboxsdk.style.layers.Property;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;
import com.mapbox.mapboxsdk.utils.BitmapUtils;
import com.mapbox.services.android.navigation.v5.navigation.MapboxNavigation;
import com.mapbox.services.android.navigation.v5.navigation.NavigationRoute;
import com.mapbox.services.android.navigation.v5.routeprogress.ProgressChangeListener;
import com.mapbox.services.android.navigation.v5.routeprogress.RouteProgress;
import com.onesignal.OneSignal;
import com.project.of.busnavigationsystem.NavBar.Routes.BusListActivity;
import com.project.of.busnavigationsystem.Login.LoginActivity;
import com.project.of.busnavigationsystem.R;
import com.project.of.busnavigationsystem.NavBar.SettingsActivity;
import com.project.of.busnavigationsystem.NavBar.Profile.readuser;
import com.project.of.busnavigationsystem.Popups.route_detail_popup;
import com.project.of.busnavigationsystem.Popups.search_popup;

import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

import static com.mapbox.core.constants.Constants.PRECISION_6;
import static com.mapbox.mapboxsdk.style.layers.Property.ICON_ANCHOR_BOTTOM;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconAllowOverlap;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconIgnorePlacement;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconImage;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconOffset;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconSize;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineCap;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineColor;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineJoin;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineWidth;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.visibility;
import static com.mapbox.mapboxsdk.style.layers.Property.LINE_CAP_ROUND;
import static com.mapbox.mapboxsdk.style.layers.Property.LINE_JOIN_ROUND;
import static com.mapbox.mapboxsdk.style.layers.Property.NONE;

/**
 * Include a map fragment within your app using Android support library.
 */
public class MainActivityy extends AppCompatActivity implements PermissionsListener {
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
    private Boolean driverFound = false;
    private String driverFoundID;
    private MapView mapView;


    //drawing route
    MapboxNavigation navigation;
    private static final String ROUTE_LAYER_ID = "route-layer-id";
    private static final String ROUTE_SOURCE_ID = "route-source-id";
    private static final String ICON_LAYER_ID = "icon-layer-id";
    private static final String ICON_SOURCE_ID = "icon-source-id";
    private static final String RED_PIN_ICON_ID = "red-pin-icon-id";
    String busStopOrigin, busStopDestination;

    private DirectionsRoute currentRoute, previousRoute;
    private MapboxDirections client;
    private Point origin;
    private Point destination;
    private Point testpoint;
    private LineLayer backgroundLineLayer;
    Intent routeDetailPopup;
    MutableLiveData<String> nearestStop = new MutableLiveData<>();
    public static MutableLiveData<String> clickedroute = new MutableLiveData<>();
    private NavigationRoute.Builder builder;

    //places api
    private static final int REQUEST_CODE_AUTOCOMPLETE = 1;
    private static final int RESPOND_CODE_SEARCH_POPUP = 2;

    //locationOfDriver
    SymbolManager driverSymbolManager;
    Symbol driverMarkerSymbol;
    //location
    private long DEFAULT_INTERVAL_IN_MILLISECONDS = 1000L;
    private long DEFAULT_MAX_WAIT_TIME = DEFAULT_INTERVAL_IN_MILLISECONDS * 5;
    // Variables needed to listen to location updates
    private MainActivityy.MainActivityLocationCallback callback = new MainActivityy.MainActivityLocationCallback(this);

    //for geoquery
    GeoQuery geoQuery;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Mapbox access token is configured here. This needs to be called either in your application
        // object or in the same activity which contains the mapview.
        Mapbox.getInstance(this, getString(R.string.mapbox_key));
        setContentView(R.layout.activity_main2);

        OneSignal.startInit(this)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .unsubscribeWhenNotificationsAreDisabled(true)
                .init();

        //navDrawer
        dl = (DrawerLayout) findViewById(R.id.activity_main);
        t = new ActionBarDrawerToggle(this, dl, R.string.Open, R.string.Close);
        dl.addDrawerListener(t);
        t.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        routeDetailPopup = new Intent(MainActivityy.this, route_detail_popup.class);
        nv = (NavigationView) findViewById(R.id.nv);
        nv.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch (id) {
                    case R.id.myprofile:
                        Intent intent1 = new Intent(MainActivityy.this, readuser.class);
                        startActivity(intent1);
                        break;

                    case R.id.Routes:
                        Intent routeDetailPopup = new Intent(MainActivityy.this, BusListActivity.class);
                        startActivity(routeDetailPopup);
                        break;
                    case R.id.settings:
                        Intent intent4 = new Intent(MainActivityy.this, SettingsActivity.class);
                        startActivity(intent4);
                        break;
                    case R.id.logout:
                        PlaceAutocomplete.clearRecentHistory(MainActivityy.this);
                        FirebaseAuth.getInstance().signOut();
                        OneSignal.deleteTag("Route");
                        Intent intToLogIn = new Intent(MainActivityy.this, LoginActivity.class);
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
            public void onMapReady(@NonNull final MapboxMap mapboxMap) {
                MainActivityy.this.mMap = mapboxMap;
                //OnClickListenerfortheroute
                mapboxMap.addOnMapClickListener(new MapboxMap.OnMapClickListener() {
                    @Override
                    public boolean onMapClick(@NonNull LatLng point) {

                        PointF screenPoint = mapboxMap.getProjection().toScreenLocation(point);
                        List<Feature> features = mapboxMap.queryRenderedFeatures(screenPoint, ROUTE_LAYER_ID);
                        if (!features.isEmpty()) {
                            Feature selectedFeature = features.get(0);
                            String title = selectedFeature.getStringProperty("title");
                            routeDetailPopup.putExtra("distance", new DecimalFormat("##.#").format((currentRoute.distance() / 1000)) + " km");
                            routeDetailPopup.putExtra("duration", ((int) (currentRoute.duration() / 50)) + " min");
                            startActivity(routeDetailPopup);
                            getClosestDriver();


                        } else {
                            mMap.getStyle(new Style.OnStyleLoaded() {
                                @Override
                                public void onStyleLoaded(@NonNull Style style) {
                                    if (style.getSource(ROUTE_SOURCE_ID) != null) {
                                        for (int i = 0; i < currentRoute.routeOptions().coordinates().size(); i++) {
                                            style.removeImage(currentRoute.routeOptions().waypointNamesList().get(i));
                                        }
                                        //style.removeImage(RED_PIN_ICON_ID);
                                        style.removeLayer(backgroundLineLayer);
                                        style.removeLayer(ICON_LAYER_ID);
                                        style.removeSource(ICON_SOURCE_ID);
                                        style.removeLayer(ROUTE_LAYER_ID);
                                        style.removeSource(ROUTE_SOURCE_ID);

                                    }

                                    if (geoQuery != null) {
                                        geoQuery.removeAllListeners();
                                    }


                                }
                            });

                        }

                        return true;
                    }

                    private void getClosestDriver() {
                        //geoquery initialization
                        DatabaseReference reff = FirebaseDatabase.getInstance().getReference("driversavailable/" + clickedroute.getValue().toString());
                        Location lastLocation = mMap.getLocationComponent().getLastKnownLocation();
                        GeoFire geoFire = new GeoFire(reff);
                        geoQuery = geoFire.queryAtLocation(new GeoLocation(lastLocation.getLatitude(), lastLocation.getLongitude()), 50);

                        geoQuery.removeAllListeners();
                        geoQuery.addGeoQueryDataEventListener(new GeoQueryDataEventListener() {
                            @Override
                            public void onDataEntered(DataSnapshot dataSnapshot, GeoLocation location) {
                                Log.d("DRIVERFINDING", "onDataEntered: "+dataSnapshot.toString());
                                if (!driverFound) {
                                    driverFound = true;
                                    //for driver location location.latitude and location.longitude


                                    if (mapboxMap.getStyle() != null) {

                                        driverSymbolManager = new SymbolManager(mapView, mapboxMap, mapboxMap.getStyle());

                                        driverSymbolManager.setIconAllowOverlap(true);
                                        driverSymbolManager.setIconIgnorePlacement(true);
                                        mapboxMap.getStyle().addImage("driverSymbolMarker", BitmapUtils.getBitmapFromDrawable(getResources().getDrawable(R.drawable.ic_bus_marker)));

                                        driverMarkerSymbol = driverSymbolManager.create(new SymbolOptions()
                                                .withLatLng(new LatLng(location.latitude, location.longitude))
                                                .withIconImage("driverSymbolMarker")
                                                .withIconSize(0.08f)
                                                .withIconOffset(new Float[]{0f, 0f})
                                                .withIconAnchor(ICON_ANCHOR_BOTTOM)
                                        );


                                    }
                                }


                            }

                            @Override
                            public void onDataExited(DataSnapshot dataSnapshot) {
                                Log.d("DRIVERFINDING", "onDataExited: "+dataSnapshot.toString());
                                if (dataSnapshot.exists() && driverMarkerSymbol!=null){
                                    driverSymbolManager.delete(driverMarkerSymbol);
                                }
                            }

                            @Override
                            public void onDataMoved(DataSnapshot dataSnapshot, final GeoLocation location) {
                                //for updates on driver location use this location
                                Log.d("DRIVERFINDING", "onDataMoved: "+dataSnapshot.toString());
                                if (driverMarkerSymbol != null) {
                                    driverSymbolManager.delete(driverMarkerSymbol);
                                }
                                driverMarkerSymbol = driverSymbolManager.create(new SymbolOptions()
                                        .withLatLng(new LatLng(location.latitude, location.longitude))
                                        .withIconImage("driverSymbolMarker")
                                        .withIconOffset(new Float[]{0f, 0f})
                                        .withIconSize(0.08f)
                                        .withIconAnchor(ICON_ANCHOR_BOTTOM)
                                );

                            }

                            @Override
                            public void onDataChanged(DataSnapshot dataSnapshot, GeoLocation location) {
                                Log.d("DRIVERFINDING", "onDataChanged: "+dataSnapshot.toString());
                                // ...
                            }

                            @Override
                            public void onGeoQueryReady() {
                                if (!driverFound) {
                                    Toast toast = Toast.makeText(MainActivityy.this, "No active bus on the route!", Toast.LENGTH_SHORT);
                                    toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 0);
                                    toast.show();

                                    if (driverMarkerSymbol!=null){
                                        driverSymbolManager.delete(driverMarkerSymbol);
                                    }
                                }

                            }

                            @Override
                            public void onGeoQueryError(DatabaseError error) {
                            }
                        });

                    }


                });
                Log.d("onmapready", "onStyleLoaded: ONMAPREADY1");

                //Removing mapbox attribution
                mapboxMap.getUiSettings().setAttributionEnabled(false);
                mapboxMap.getUiSettings().setLogoEnabled(false);

                mapboxMap.setStyle(Style.MAPBOX_STREETS, new Style.OnStyleLoaded() {
                    @Override
                    public void onStyleLoaded(@NonNull final Style style) {

                        // Map is set up and the style has loaded. Now you can add data or make other map adjustments
                        enableLocationComponent(style);

                        Location lastLocation = mapboxMap.getLocationComponent().getLastKnownLocation();
                        if (lastLocation == null) {
                            if (ActivityCompat.checkSelfPermission(MainActivityy.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MainActivityy.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
                                        new AlertDialog.Builder(MainActivityy.this)
                                                .setTitle("Location Services turned off!")
                                                .setMessage("Please turn on location service on your device!")
                                                .show();
                                    }
                                }

                                @Override
                                public void onFailure(@NonNull Exception exception) {
                                    new AlertDialog.Builder(MainActivityy.this)
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


                        //places api function call
                        initSearchFab();

                        //Send nearestBusStop to the window popup as input
                        nearestStop.observe(MainActivityy.this, new Observer<String>() {
                            @Override
                            public void onChanged(String busStop) {
                                Intent z = new Intent(getApplicationContext(), search_popup.class);
                                z.putExtra("busstop", busStop);
                                startActivityForResult(z, 1);
                            }
                        });


                        //Route result onclick action
                        clickedroute.observe(MainActivityy.this, new Observer<String>() {
                            @RequiresApi(api = Build.VERSION_CODES.Q)
                            @Override
                            public void onChanged(String s) {

                                if (s.equals("Ratnapark-Samakhusi Chowk")) {
                                    Toast.makeText(MainActivityy.this, "CLICK ON ROUTE FOR INFO", Toast.LENGTH_LONG).show();


                                    //remove any layer if it exists
                                    if (style.getSource(ROUTE_SOURCE_ID) != null) {
                                        if (currentRoute != null) {
                                            previousRoute = currentRoute;
                                            for (int i = 0; i < previousRoute.routeOptions().coordinates().size(); i++) {
                                                style.removeImage(previousRoute.routeOptions().waypointNamesList().get(i));
                                            }

                                        }

                                        //style.removeImage(RED_PIN_ICON_ID);
                                        style.removeLayer(backgroundLineLayer);
                                        style.removeLayer(ICON_LAYER_ID);
                                        style.removeSource(ICON_SOURCE_ID);
                                        style.removeLayer(ROUTE_LAYER_ID);
                                        style.removeSource(ROUTE_SOURCE_ID);
                                    }
                                    //give the routes coordinates
                                    busStopOrigin = "Ratnapark";
                                    busStopDestination = "Samakhusi Chowk";
                                    origin = Point.fromLngLat(Double.parseDouble(getString(R.string.Ratnapark_long)), Double.parseDouble(getString(R.string.Ratnapark_lat)));
                                    destination = Point.fromLngLat(Double.parseDouble(getString(R.string.SamakhusiChowk_long)), Double.parseDouble(getString(R.string.SamakhusiChowk_lat)));
                                    Log.d("BEFORE GET ROUTE", "onChanged: " + origin + destination);

                                    getRoute(mMap, origin, destination);
                                    initSource(style);
                                    initLayers(style);
                                    LatLngBounds latLngBounds = new LatLngBounds.Builder()
                                            .include(new LatLng(Double.parseDouble(getString(R.string.Ratnapark_lat)), Double.parseDouble(getString(R.string.Ratnapark_long))))
                                            .include(new LatLng(Double.parseDouble(getString(R.string.SamakhusiChowk_lat)), Double.parseDouble(getString(R.string.SamakhusiChowk_long))))
                                            .build();

                                    mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, 20));

                                    routeDetailPopup.putExtra("route", "Route 16");
                                } else if (s.equals("Putalisadak-Pulchowk")) {
                                    Toast.makeText(MainActivityy.this, "CLICK ON ROUTE FOR INFO", Toast.LENGTH_LONG).show();

                                    //remove any layer if it exists
                                    if (style.getSource(ROUTE_SOURCE_ID) != null) {

                                        //style.removeImage(RED_PIN_ICON_ID);
                                        style.removeLayer(backgroundLineLayer);
                                        style.removeLayer(ICON_LAYER_ID);
                                        style.removeSource(ICON_SOURCE_ID);
                                        style.removeLayer(ROUTE_LAYER_ID);
                                        style.removeSource(ROUTE_SOURCE_ID);
                                    }
                                    //give the routes coordinates
                                    busStopOrigin = "Putalisadak";
                                    busStopDestination = "Pulchowk";
                                    origin = Point.fromLngLat(Double.parseDouble(getString(R.string.Putalisadak_long)), Double.parseDouble(getString(R.string.Putalisadak_lat)));
                                    destination = Point.fromLngLat(Double.parseDouble(getString(R.string.Pulchowk_long)), Double.parseDouble(getString(R.string.Pulchowk_lat)));
                                    getRoute(mMap, origin, destination);
                                    initSource(style);
                                    initLayers(style);
                                    LatLngBounds latLngBounds = new LatLngBounds.Builder()
                                            .include(new LatLng(Double.parseDouble(getString(R.string.Putalisadak_lat)), Double.parseDouble(getString(R.string.Putalisadak_long))))
                                            .include(new LatLng(Double.parseDouble(getString(R.string.Pulchowk_lat)), Double.parseDouble(getString(R.string.Pulchowk_long))))
                                            .build();

                                    mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, 20));

                                    routeDetailPopup.putExtra("route", "Route 15");

                                } else if (s.equals("Boudha-Dillibazaar")) {
                                    Toast.makeText(MainActivityy.this, "CLICK ON ROUTE FOR INFO", Toast.LENGTH_LONG).show();

                                    //remove any layer if it exists
                                    if (style.getSource(ROUTE_SOURCE_ID) != null) {
                                        if (currentRoute != null) {
                                            previousRoute = currentRoute;
                                            for (int i = 0; i < previousRoute.routeOptions().coordinates().size(); i++) {
                                                style.removeImage(previousRoute.routeOptions().waypointNamesList().get(i));
                                            }

                                        }

                                        //style.removeImage(RED_PIN_ICON_ID);
                                        style.removeLayer(backgroundLineLayer);
                                        style.removeLayer(ICON_LAYER_ID);
                                        style.removeSource(ICON_SOURCE_ID);
                                        style.removeLayer(ROUTE_LAYER_ID);
                                        style.removeSource(ROUTE_SOURCE_ID);
                                    }
                                    //give the routes coordinates
                                    busStopOrigin = "Boudha";
                                    busStopDestination = "Dillibazaar";
                                    origin = Point.fromLngLat(Double.parseDouble(getString(R.string.Boudha_long)), Double.parseDouble(getString(R.string.Boudha_lat)));
                                    destination = Point.fromLngLat(Double.parseDouble(getString(R.string.Dillibazaar_long)), Double.parseDouble(getString(R.string.Dillibazaar_lat)));
                                    getRoute(mMap, origin, destination);
                                    initSource(style);
                                    initLayers(style);
                                    LatLngBounds latLngBounds = new LatLngBounds.Builder()
                                            .include(new LatLng(Double.parseDouble(getString(R.string.Boudha_lat)), Double.parseDouble(getString(R.string.Boudha_long))))
                                            .include(new LatLng(Double.parseDouble(getString(R.string.Dillibazaar_lat)), Double.parseDouble(getString(R.string.Dillibazaar_long))))
                                            .build();

                                    mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, 20));

                                    routeDetailPopup.putExtra("route", "Route 9");

                                } else if (s.equals("Lagankhel-Jawalakhel-Kupondol-Tripureshor")) {
                                    Toast.makeText(MainActivityy.this, "CLICK ON ROUTE FOR INFO", Toast.LENGTH_LONG).show();

                                    //remove any layer if it exists
                                    if (style.getSource(ROUTE_SOURCE_ID) != null) {
                                        if (currentRoute != null) {
                                            previousRoute = currentRoute;
                                            for (int i = 0; i < previousRoute.routeOptions().coordinates().size(); i++) {
                                                style.removeImage(previousRoute.routeOptions().waypointNamesList().get(i));
                                            }

                                        }

                                        //style.removeImage(RED_PIN_ICON_ID);
                                        style.removeLayer(backgroundLineLayer);
                                        style.removeLayer(ICON_LAYER_ID);
                                        style.removeSource(ICON_SOURCE_ID);
                                        style.removeLayer(ROUTE_LAYER_ID);
                                        style.removeSource(ROUTE_SOURCE_ID);
                                    }
                                    //give the routes coordinates
                                    origin = Point.fromLngLat(Double.parseDouble(getString(R.string.Lagankhel_long)), Double.parseDouble(getString(R.string.Lagankhel_lat)));
                                    destination = Point.fromLngLat(Double.parseDouble(getString(R.string.Tripureshor_long)), Double.parseDouble(getString(R.string.Tripureshor_lat)));
                                    getRoute(mMap, origin, destination);
                                    initSource(style);
                                    initLayers(style);
                                    LatLngBounds latLngBounds = new LatLngBounds.Builder()
                                            .include(new LatLng(Double.parseDouble(getString(R.string.Lagankhel_lat)), Double.parseDouble(getString(R.string.Lagankhel_long))))
                                            .include(new LatLng(Double.parseDouble(getString(R.string.Tripureshor_lat)), Double.parseDouble(getString(R.string.Tripureshor_long))))
                                            .build();

                                    mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, 25));

                                    routeDetailPopup.putExtra("route", "Route 13");

                                } else if (s.equals("Lagankhel-Kupondol-Thapathali")) {
                                    Toast.makeText(MainActivityy.this, "CLICK ON ROUTE FOR INFO", Toast.LENGTH_LONG).show();

                                    //remove any layer if it exists
                                    if (style.getSource(ROUTE_SOURCE_ID) != null) {
                                        if (currentRoute != null) {
                                            previousRoute = currentRoute;
                                            for (int i = 0; i < previousRoute.routeOptions().coordinates().size(); i++) {
                                                style.removeImage(previousRoute.routeOptions().waypointNamesList().get(i));
                                            }

                                        }

                                        //style.removeImage(RED_PIN_ICON_ID);
                                        style.removeLayer(backgroundLineLayer);
                                        style.removeLayer(ICON_LAYER_ID);
                                        style.removeSource(ICON_SOURCE_ID);
                                        style.removeLayer(ROUTE_LAYER_ID);
                                        style.removeSource(ROUTE_SOURCE_ID);
                                    }
                                    //give the routes coordinates
                                    origin = Point.fromLngLat(Double.parseDouble(getString(R.string.Lagankhel_long)), Double.parseDouble(getString(R.string.Lagankhel_lat)));
                                    destination = Point.fromLngLat(Double.parseDouble(getString(R.string.Thapathali_long)), Double.parseDouble(getString(R.string.Thapathali_lat)));
                                    getRoute(mMap, origin, destination);
                                    initSource(style);
                                    initLayers(style);
                                    LatLngBounds latLngBounds = new LatLngBounds.Builder()
                                            .include(new LatLng(Double.parseDouble(getString(R.string.Lagankhel_lat)), Double.parseDouble(getString(R.string.Lagankhel_long))))
                                            .include(new LatLng(Double.parseDouble(getString(R.string.Thapathali_lat)), Double.parseDouble(getString(R.string.Thapathali_long))))
                                            .build();

                                    mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, 25));

                                    routeDetailPopup.putExtra("route", "Route 14");

                                } else if (s.equals("Bungamati-Bhaisipati-Charghare")) {
                                    Toast.makeText(MainActivityy.this, "CLICK ON ROUTE FOR INFO", Toast.LENGTH_LONG).show();

                                    //remove any layer if it exists
                                    if (style.getSource(ROUTE_SOURCE_ID) != null) {
                                        if (currentRoute != null) {
                                            previousRoute = currentRoute;
                                            for (int i = 0; i < previousRoute.routeOptions().coordinates().size(); i++) {
                                                style.removeImage(previousRoute.routeOptions().waypointNamesList().get(i));
                                            }

                                        }

                                        //style.removeImage(RED_PIN_ICON_ID);
                                        style.removeLayer(backgroundLineLayer);
                                        style.removeLayer(ICON_LAYER_ID);
                                        style.removeSource(ICON_SOURCE_ID);
                                        style.removeLayer(ROUTE_LAYER_ID);
                                        style.removeSource(ROUTE_SOURCE_ID);
                                    }
                                    //give the routes coordinates
                                    origin = Point.fromLngLat(Double.parseDouble(getString(R.string.Bungamati_long)), Double.parseDouble(getString(R.string.Bungamati_lat)));
                                    destination = Point.fromLngLat(Double.parseDouble(getString(R.string.Charghare_long)), Double.parseDouble(getString(R.string.Charghare_lat)));
                                    getRoute(mMap, origin, destination);
                                    initSource(style);
                                    initLayers(style);
                                    LatLngBounds latLngBounds = new LatLngBounds.Builder()
                                            .include(new LatLng(Double.parseDouble(getString(R.string.Bungamati_lat)), Double.parseDouble(getString(R.string.Bungamati_long))))
                                            .include(new LatLng(Double.parseDouble(getString(R.string.Charghare_lat)), Double.parseDouble(getString(R.string.Charghare_long))))
                                            .build();

                                    mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, 25));

                                    routeDetailPopup.putExtra("route", "Route 11");

                                } else if (s.equals("Attarkhel-Purano Buspark")) {
                                    Toast.makeText(MainActivityy.this, "CLICK ON ROUTE FOR INFO", Toast.LENGTH_LONG).show();

                                    //remove any layer if it exists
                                    if (style.getSource(ROUTE_SOURCE_ID) != null) {
                                        if (currentRoute != null) {
                                            previousRoute = currentRoute;
                                            for (int i = 0; i < previousRoute.routeOptions().coordinates().size(); i++) {
                                                style.removeImage(previousRoute.routeOptions().waypointNamesList().get(i));
                                            }

                                        }

                                        //style.removeImage(RED_PIN_ICON_ID);
                                        style.removeLayer(backgroundLineLayer);
                                        style.removeLayer(ICON_LAYER_ID);
                                        style.removeSource(ICON_SOURCE_ID);
                                        style.removeLayer(ROUTE_LAYER_ID);
                                        style.removeSource(ROUTE_SOURCE_ID);
                                    }
                                    //give the routes coordinates
                                    busStopOrigin = "Attarkhel";
                                    busStopDestination = "Purano Buspark";
                                    origin = Point.fromLngLat(Double.parseDouble(getString(R.string.Attarkhel_long)), Double.parseDouble(getString(R.string.Attarkhel_lat)));
                                    destination = Point.fromLngLat(Double.parseDouble(getString(R.string.PuranoBuspark_long)), Double.parseDouble(getString(R.string.PuranoBuspark_lat)));
                                    getRoute(mMap, origin, destination);
                                    initSource(style);
                                    initLayers(style);
                                    LatLngBounds latLngBounds = new LatLngBounds.Builder()
                                            .include(new LatLng(Double.parseDouble(getString(R.string.Attarkhel_lat)), Double.parseDouble(getString(R.string.Attarkhel_long))))
                                            .include(new LatLng(Double.parseDouble(getString(R.string.PuranoBuspark_lat)), Double.parseDouble(getString(R.string.PuranoBuspark_long))))
                                            .build();

                                    mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, 25));

                                    routeDetailPopup.putExtra("route", "Route 1");

                                } else if (s.equals("Bagbazaar-Ratnapark")) {
                                    Toast.makeText(MainActivityy.this, "CLICK ON ROUTE FOR INFO", Toast.LENGTH_LONG).show();

                                    //remove any layer if it exists
                                    if (style.getSource(ROUTE_SOURCE_ID) != null) {
                                        if (currentRoute != null) {
                                            previousRoute = currentRoute;
                                            for (int i = 0; i < previousRoute.routeOptions().coordinates().size(); i++) {
                                                style.removeImage(previousRoute.routeOptions().waypointNamesList().get(i));
                                            }

                                        }

                                        //style.removeImage(RED_PIN_ICON_ID);
                                        style.removeLayer(backgroundLineLayer);
                                        style.removeLayer(ICON_LAYER_ID);
                                        style.removeSource(ICON_SOURCE_ID);
                                        style.removeLayer(ROUTE_LAYER_ID);
                                        style.removeSource(ROUTE_SOURCE_ID);
                                    }
                                    //give the routes coordinates
                                    busStopOrigin = "Bagbazaar";
                                    busStopDestination = "Ratnapark";
                                    origin = Point.fromLngLat(Double.parseDouble(getString(R.string.Bagbazaar_long)), Double.parseDouble(getString(R.string.Bagbazaar_lat)));
                                    destination = Point.fromLngLat(Double.parseDouble(getString(R.string.Ratnapark_long)), Double.parseDouble(getString(R.string.Ratnapark_lat)));
                                    getRoute(mMap, origin, destination);
                                    initSource(style);
                                    initLayers(style);
                                    LatLngBounds latLngBounds = new LatLngBounds.Builder()
                                            .include(new LatLng(Double.parseDouble(getString(R.string.Bagbazaar_lat)), Double.parseDouble(getString(R.string.Bagbazaar_long))))
                                            .include(new LatLng(Double.parseDouble(getString(R.string.Ratnapark_lat)), Double.parseDouble(getString(R.string.Ratnapark_long))))
                                            .build();

                                    mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, 25));

                                    routeDetailPopup.putExtra("route", "Route 3");

                                } else if (s.equals("Bagbazaar-Kamalbinayak")) {
                                    Toast.makeText(MainActivityy.this, "CLICK ON ROUTE FOR INFO", Toast.LENGTH_LONG).show();

                                    //remove any layer if it exists
                                    if (style.getSource(ROUTE_SOURCE_ID) != null) {
                                        if (currentRoute != null) {
                                            previousRoute = currentRoute;
                                            for (int i = 0; i < previousRoute.routeOptions().coordinates().size(); i++) {
                                                style.removeImage(previousRoute.routeOptions().waypointNamesList().get(i));
                                            }

                                        }

                                        //style.removeImage(RED_PIN_ICON_ID);
                                        style.removeLayer(backgroundLineLayer);
                                        style.removeLayer(ICON_LAYER_ID);
                                        style.removeSource(ICON_SOURCE_ID);
                                        style.removeLayer(ROUTE_LAYER_ID);
                                        style.removeSource(ROUTE_SOURCE_ID);
                                    }
                                    //give the routes coordinates
                                    busStopOrigin = "Bagbazaar";
                                    busStopDestination = "Kamalbinayak";
                                    origin = Point.fromLngLat(Double.parseDouble(getString(R.string.Bagbazaar_long)), Double.parseDouble(getString(R.string.Bagbazaar_lat)));
                                    destination = Point.fromLngLat(Double.parseDouble(getString(R.string.Kamalbinayak_long)), Double.parseDouble(getString(R.string.Kamalbinayak_lat)));
                                    getRoute(mMap, origin, destination);
                                    initSource(style);
                                    initLayers(style);
                                    LatLngBounds latLngBounds = new LatLngBounds.Builder()
                                            .include(new LatLng(Double.parseDouble(getString(R.string.Bagbazaar_lat)), Double.parseDouble(getString(R.string.Bagbazaar_long))))
                                            .include(new LatLng(Double.parseDouble(getString(R.string.Kamalbinayak_lat)), Double.parseDouble(getString(R.string.Kamalbinayak_long))))
                                            .build();

                                    mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, 25));

                                    routeDetailPopup.putExtra("route", "Route 2");

                                } else if (s.equals("Balaju-Ratnapark")) {
                                    Toast.makeText(MainActivityy.this, "CLICK ON ROUTE FOR INFO", Toast.LENGTH_LONG).show();

                                    //remove any layer if it exists
                                    if (style.getSource(ROUTE_SOURCE_ID) != null) {
                                        if (currentRoute != null) {
                                            previousRoute = currentRoute;
                                            for (int i = 0; i < previousRoute.routeOptions().coordinates().size(); i++) {
                                                style.removeImage(previousRoute.routeOptions().waypointNamesList().get(i));
                                            }

                                        }

                                        //style.removeImage(RED_PIN_ICON_ID);
                                        style.removeLayer(backgroundLineLayer);
                                        style.removeLayer(ICON_LAYER_ID);
                                        style.removeSource(ICON_SOURCE_ID);
                                        style.removeLayer(ROUTE_LAYER_ID);
                                        style.removeSource(ROUTE_SOURCE_ID);
                                    }
                                    //give the routes coordinates
                                    busStopOrigin = "Balaju";
                                    busStopDestination = "Ratnapark";
                                    origin = Point.fromLngLat(Double.parseDouble(getString(R.string.Balaju_long)), Double.parseDouble(getString(R.string.Balaju_lat)));
                                    destination = Point.fromLngLat(Double.parseDouble(getString(R.string.Ratnapark_long)), Double.parseDouble(getString(R.string.Ratnapark_lat)));
                                    getRoute(mMap, origin, destination);
                                    initSource(style);
                                    initLayers(style);
                                    LatLngBounds latLngBounds = new LatLngBounds.Builder()
                                            .include(new LatLng(Double.parseDouble(getString(R.string.Balaju_lat)), Double.parseDouble(getString(R.string.Balaju_long))))
                                            .include(new LatLng(Double.parseDouble(getString(R.string.Ratnapark_lat)), Double.parseDouble(getString(R.string.Ratnapark_long))))
                                            .build();

                                    mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, 25));

                                    routeDetailPopup.putExtra("route", "Route 4");

                                } else if (s.equals("Balkhu-NayaBaneshwor-Sankhamul")) {
                                    Toast.makeText(MainActivityy.this, "CLICK ON ROUTE FOR INFO", Toast.LENGTH_LONG).show();

                                    //remove any layer if it exists
                                    if (style.getSource(ROUTE_SOURCE_ID) != null) {
                                        if (currentRoute != null) {
                                            previousRoute = currentRoute;
                                            for (int i = 0; i < previousRoute.routeOptions().coordinates().size(); i++) {
                                                style.removeImage(previousRoute.routeOptions().waypointNamesList().get(i));
                                            }

                                        }

                                        //style.removeImage(RED_PIN_ICON_ID);
                                        style.removeLayer(backgroundLineLayer);
                                        style.removeLayer(ICON_LAYER_ID);
                                        style.removeSource(ICON_SOURCE_ID);
                                        style.removeLayer(ROUTE_LAYER_ID);
                                        style.removeSource(ROUTE_SOURCE_ID);
                                    }
                                    //give the routes coordinates
                                    origin = Point.fromLngLat(Double.parseDouble(getString(R.string.Balkhu_long)), Double.parseDouble(getString(R.string.Balkhu_lat)));
                                    destination = Point.fromLngLat(Double.parseDouble(getString(R.string.Sankhamul_long)), Double.parseDouble(getString(R.string.Sankhamul_lat)));
                                    getRoute(mMap, origin, destination);
                                    initSource(style);
                                    initLayers(style);
                                    LatLngBounds latLngBounds = new LatLngBounds.Builder()
                                            .include(new LatLng(Double.parseDouble(getString(R.string.Balkhu_lat)), Double.parseDouble(getString(R.string.Balkhu_long))))
                                            .include(new LatLng(Double.parseDouble(getString(R.string.Sankhamul_lat)), Double.parseDouble(getString(R.string.Sankhamul_long))))
                                            .build();

                                    mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, 25));

                                    routeDetailPopup.putExtra("route", "Route 5");

                                } else if (s.equals("Balkhu-NayaBaneshwor-Shantinagar-Bhatkya Pul")) {
                                    Toast.makeText(MainActivityy.this, "CLICK ON ROUTE FOR INFO", Toast.LENGTH_LONG).show();

                                    //remove any layer if it exists
                                    if (style.getSource(ROUTE_SOURCE_ID) != null) {
                                        if (currentRoute != null) {
                                            previousRoute = currentRoute;
                                            for (int i = 0; i < previousRoute.routeOptions().coordinates().size(); i++) {
                                                style.removeImage(previousRoute.routeOptions().waypointNamesList().get(i));
                                            }

                                        }

                                        //style.removeImage(RED_PIN_ICON_ID);
                                        style.removeLayer(backgroundLineLayer);
                                        style.removeLayer(ICON_LAYER_ID);
                                        style.removeSource(ICON_SOURCE_ID);
                                        style.removeLayer(ROUTE_LAYER_ID);
                                        style.removeSource(ROUTE_SOURCE_ID);
                                    }
                                    //give the routes coordinates
                                    origin = Point.fromLngLat(Double.parseDouble(getString(R.string.Balkhu_long)), Double.parseDouble(getString(R.string.Balkhu_lat)));
                                    destination = Point.fromLngLat(Double.parseDouble(getString(R.string.BhatkyaPul_long)), Double.parseDouble(getString(R.string.BhatkyaPul_lat)));
                                    getRoute(mMap, origin, destination);
                                    initSource(style);
                                    initLayers(style);
                                    LatLngBounds latLngBounds = new LatLngBounds.Builder()
                                            .include(new LatLng(Double.parseDouble(getString(R.string.Balkhu_lat)), Double.parseDouble(getString(R.string.Balkhu_long))))
                                            .include(new LatLng(Double.parseDouble(getString(R.string.BhatkyaPul_lat)), Double.parseDouble(getString(R.string.BhatkyaPul_long))))
                                            .build();

                                    mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, 25));

                                    routeDetailPopup.putExtra("route", "Route 6");

                                } else if (s.equals("Balkumari-Gopi Krishna")) {
                                    Toast.makeText(MainActivityy.this, "CLICK ON ROUTE FOR INFO", Toast.LENGTH_LONG).show();

                                    //remove any layer if it exists
                                    if (style.getSource(ROUTE_SOURCE_ID) != null) {
                                        if (currentRoute != null) {
                                            previousRoute = currentRoute;
                                            for (int i = 0; i < previousRoute.routeOptions().coordinates().size(); i++) {
                                                style.removeImage(previousRoute.routeOptions().waypointNamesList().get(i));
                                            }

                                        }

                                        //style.removeImage(RED_PIN_ICON_ID);
                                        style.removeLayer(backgroundLineLayer);
                                        style.removeLayer(ICON_LAYER_ID);
                                        style.removeSource(ICON_SOURCE_ID);
                                        style.removeLayer(ROUTE_LAYER_ID);
                                        style.removeSource(ROUTE_SOURCE_ID);
                                    }
                                    //give the routes coordinates
                                    busStopOrigin = "Balkumari";
                                    busStopDestination = "Gopi Krishna";
                                    origin = Point.fromLngLat(Double.parseDouble(getString(R.string.Balkumari_long)), Double.parseDouble(getString(R.string.Balkumari_lat)));
                                    destination = Point.fromLngLat(Double.parseDouble(getString(R.string.GopiKrishna_long)), Double.parseDouble(getString(R.string.GopiKrishna_lat)));
                                    getRoute(mMap, origin, destination);
                                    initSource(style);
                                    initLayers(style);
                                    LatLngBounds latLngBounds = new LatLngBounds.Builder()
                                            .include(new LatLng(Double.parseDouble(getString(R.string.Balkumari_lat)), Double.parseDouble(getString(R.string.Balkumari_long))))
                                            .include(new LatLng(Double.parseDouble(getString(R.string.GopiKrishna_lat)), Double.parseDouble(getString(R.string.GopiKrishna_long))))
                                            .build();

                                    mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, 25));

                                    routeDetailPopup.putExtra("route", "Route 7");

                                } else if (s.equals("Bhaktapur-Purano Thimi-Purano Bus Park")) {
                                    Toast.makeText(MainActivityy.this, "CLICK ON ROUTE FOR INFO", Toast.LENGTH_LONG).show();

                                    //remove any layer if it exists
                                    if (style.getSource(ROUTE_SOURCE_ID) != null) {
                                        if (currentRoute != null) {
                                            previousRoute = currentRoute;
                                            for (int i = 0; i < previousRoute.routeOptions().coordinates().size(); i++) {
                                                style.removeImage(previousRoute.routeOptions().waypointNamesList().get(i));
                                            }

                                        }

                                        //style.removeImage(RED_PIN_ICON_ID);
                                        style.removeLayer(backgroundLineLayer);
                                        style.removeLayer(ICON_LAYER_ID);
                                        style.removeSource(ICON_SOURCE_ID);
                                        style.removeLayer(ROUTE_LAYER_ID);
                                        style.removeSource(ROUTE_SOURCE_ID);
                                    }
                                    //give the routes coordinates
                                    origin = Point.fromLngLat(Double.parseDouble(getString(R.string.Bhaktapur_long)), Double.parseDouble(getString(R.string.Bhaktapur_lat)));
                                    destination = Point.fromLngLat(Double.parseDouble(getString(R.string.PuranoBuspark_long)), Double.parseDouble(getString(R.string.PuranoBuspark_lat)));
                                    getRoute(mMap, origin, destination);
                                    initSource(style);
                                    initLayers(style);
                                    LatLngBounds latLngBounds = new LatLngBounds.Builder()
                                            .include(new LatLng(Double.parseDouble(getString(R.string.Bhaktapur_lat)), Double.parseDouble(getString(R.string.Bhaktapur_long))))
                                            .include(new LatLng(Double.parseDouble(getString(R.string.PuranoBuspark_lat)), Double.parseDouble(getString(R.string.PuranoBuspark_long))))
                                            .build();

                                    mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, 25));

                                    routeDetailPopup.putExtra("route", "Route 8");

                                } else if (s.equals("Budhanilkantha School-Ratnapark")) {
                                    Toast.makeText(MainActivityy.this, "CLICK ON ROUTE FOR INFO", Toast.LENGTH_LONG).show();

                                    //remove any layer if it exists
                                    if (style.getSource(ROUTE_SOURCE_ID) != null) {
                                        if (currentRoute != null) {
                                            previousRoute = currentRoute;
                                            for (int i = 0; i < previousRoute.routeOptions().coordinates().size(); i++) {
                                                style.removeImage(previousRoute.routeOptions().waypointNamesList().get(i));
                                            }

                                        }

                                        //style.removeImage(RED_PIN_ICON_ID);
                                        style.removeLayer(backgroundLineLayer);
                                        style.removeLayer(ICON_LAYER_ID);
                                        style.removeSource(ICON_SOURCE_ID);
                                        style.removeLayer(ROUTE_LAYER_ID);
                                        style.removeSource(ROUTE_SOURCE_ID);
                                    }
                                    //give the routes coordinates
                                    busStopOrigin = "Budhanilkantha School";
                                    busStopDestination = "Ratnapark";
                                    origin = Point.fromLngLat(Double.parseDouble(getString(R.string.BudhanilkanthaSchool_long)), Double.parseDouble(getString(R.string.BudhanilkanthaSchool_lat)));
                                    destination = Point.fromLngLat(Double.parseDouble(getString(R.string.Ratnapark_long)), Double.parseDouble(getString(R.string.Ratnapark_lat)));
                                    getRoute(mMap, origin, destination);
                                    initSource(style);
                                    initLayers(style);
                                    LatLngBounds latLngBounds = new LatLngBounds.Builder()
                                            .include(new LatLng(Double.parseDouble(getString(R.string.BudhanilkanthaSchool_lat)), Double.parseDouble(getString(R.string.BudhanilkanthaSchool_long))))
                                            .include(new LatLng(Double.parseDouble(getString(R.string.Ratnapark_lat)), Double.parseDouble(getString(R.string.Ratnapark_long))))
                                            .build();

                                    mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, 25));

                                    routeDetailPopup.putExtra("route", "Route 10");

                                }
                                        /*else if (s.equals("Chakrapath Parikrama")){
                                            Toast.makeText(MainActivityy.this, "CLICK ON ROUTE FOR INFO", Toast.LENGTH_LONG).show();

                                            //remove any layer if it exists
                                            if (style.getSource(ROUTE_SOURCE_ID) != null) {
                                                if (currentRoute!=null){
                                                    previousRoute=currentRoute;
                                                    for (int i = 0; i < previousRoute.routeOptions().coordinates().size(); i++) {
                                                        style.removeImage(previousRoute.routeOptions().waypointNamesList().get(i));
                                                    }
                                                    
                                                }
                                                
                                                //style.removeImage(RED_PIN_ICON_ID);
                                                style.removeLayer(backgroundLineLayer);
                                                style.removeLayer(ICON_LAYER_ID);
                                                style.removeSource(ICON_SOURCE_ID);
                                                style.removeLayer(ROUTE_LAYER_ID);
                                                style.removeSource(ROUTE_SOURCE_ID);
                                            }
                                            //give the routes coordinates
                                            origin = Point.fromLngLat(Double.parseDouble(getString(R.string.Lagankhel_long)),Double.parseDouble(getString(R.string.Lagankhel_lat)));
                                            destination = Point.fromLngLat(Double.parseDouble(getString(R.string.Tripureshor_long)), Double.parseDouble(getString(R.string.Tripureshor_lat)));
                                            getRoute(mMap, origin, destination);
                                            initSource(style);
                                            initLayers(style);
                                            LatLngBounds latLngBounds = new LatLngBounds.Builder()
                                                    .include(new LatLng(Double.parseDouble(getString(R.string.Lagankhel_lat)),Double.parseDouble(getString(R.string.Lagankhel_long))))
                                                    .include(new LatLng(Double.parseDouble(getString(R.string.Tripureshor_lat)),Double.parseDouble(getString(R.string.Tripureshor_long))))
                                                    .build();

                                            mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, 25));

                                            routeDetailPopup.putExtra("route","Route 4");

                                        }*/
                                else if (s.equals("Changu-Ratnapark")) {
                                    Toast.makeText(MainActivityy.this, "CLICK ON ROUTE FOR INFO", Toast.LENGTH_LONG).show();

                                    //remove any layer if it exists
                                    if (style.getSource(ROUTE_SOURCE_ID) != null) {
                                        if (currentRoute != null) {
                                            previousRoute = currentRoute;
                                            for (int i = 0; i < previousRoute.routeOptions().coordinates().size(); i++) {
                                                style.removeImage(previousRoute.routeOptions().waypointNamesList().get(i));
                                            }

                                        }

                                        //style.removeImage(RED_PIN_ICON_ID);
                                        style.removeLayer(backgroundLineLayer);
                                        style.removeLayer(ICON_LAYER_ID);
                                        style.removeSource(ICON_SOURCE_ID);
                                        style.removeLayer(ROUTE_LAYER_ID);
                                        style.removeSource(ROUTE_SOURCE_ID);
                                    }
                                    //give the routes coordinates
                                    busStopOrigin = "Changu";
                                    busStopDestination = "Ratnapark";
                                    origin = Point.fromLngLat(Double.parseDouble(getString(R.string.Changu_long)), Double.parseDouble(getString(R.string.Changu_lat)));
                                    destination = Point.fromLngLat(Double.parseDouble(getString(R.string.Ratnapark_long)), Double.parseDouble(getString(R.string.Ratnapark_lat)));
                                    getRoute(mMap, origin, destination);
                                    initSource(style);
                                    initLayers(style);
                                    LatLngBounds latLngBounds = new LatLngBounds.Builder()
                                            .include(new LatLng(Double.parseDouble(getString(R.string.Changu_lat)), Double.parseDouble(getString(R.string.Changu_long))))
                                            .include(new LatLng(Double.parseDouble(getString(R.string.Ratnapark_lat)), Double.parseDouble(getString(R.string.Ratnapark_long))))
                                            .build();

                                    mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, 25));

                                    routeDetailPopup.putExtra("route", "Route 12");

                                }

                                AlertDialog alertDialog = new AlertDialog.Builder(MainActivityy.this).create();
                                alertDialog.setTitle("Notification");
                                alertDialog.setMessage("Do you want to receive push notifications about the bus arrivals?");
                                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Yes",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                OneSignal.sendTag("Route", s);
                                                dialog.dismiss();
                                            }
                                        });
                                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        OneSignal.deleteTag("Route");
                                        dialog.dismiss();
                                    }
                                });
                                alertDialog.show();

                            }
                        });

                    }

                    //the function for the places api
                    private void initSearchFab() {

                        findViewById(R.id.search_fab).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                Intent intent = new PlaceAutocomplete.IntentBuilder().accessToken(Mapbox.getAccessToken() != null ? Mapbox.getAccessToken() : getString(R.string.mapbox_key))
                                        .placeOptions(PlaceOptions.builder().backgroundColor(Color.parseColor("#EEEEEE"))
                                                .limit(10).country("NP")
                                                .build(PlaceOptions.MODE_CARDS)).build(MainActivityy.this);
                                startActivityForResult(intent, 1);

                            }

                        });


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
                                    .bearing(90)
                                    .build();
                            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(position), 3000);
                        } else {
                            new AlertDialog.Builder(MainActivityy.this)
                                    .setTitle("Location Services turned off!")
                                    .setMessage("Please turn on location service on your device!")
                                    .show();
                        }

                    }
                });


            }
        });


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

        private final WeakReference<MainActivityy> activityWeakReference;

        MainActivityLocationCallback(MainActivityy activity) {
            this.activityWeakReference = new WeakReference<>(activity);
        }

        /**
         * The LocationEngineCallback interface's method which fires when the device's location has changed.
         *
         * @param result the LocationEngineResult object which has the last known location within it.
         */
        @Override
        public void onSuccess(LocationEngineResult result) {
            MainActivityy activity = activityWeakReference.get();

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
            MainActivityy activity = activityWeakReference.get();
            if (activity != null) {
                Toast.makeText(activity, exception.getLocalizedMessage(),
                        Toast.LENGTH_SHORT).show();
                //For future : add alert window to turn on location services for the app

            }
        }
    }


    /**
     * Add the route and marker sources to the map
     */
    private void initSource(@NonNull Style loadedMapStyle) {
        loadedMapStyle.addSource(new GeoJsonSource(ROUTE_SOURCE_ID));
        if ((origin.equals(Point.fromLngLat(Double.parseDouble(getString(R.string.Lagankhel_long)), Double.parseDouble(getString(R.string.Lagankhel_lat)))) && destination.equals(Point.fromLngLat(Double.parseDouble(getString(R.string.Tripureshor_long)), Double.parseDouble(getString(R.string.Tripureshor_lat)))))) {
            GeoJsonSource iconGeoJsonSource = new GeoJsonSource(ICON_SOURCE_ID, FeatureCollection.fromFeatures(new Feature[]{
                    Feature.fromGeometry(Point.fromLngLat(origin.longitude(), origin.latitude())),
                    Feature.fromGeometry(Point.fromLngLat(destination.longitude(), destination.latitude())),
                    Feature.fromGeometry(Point.fromLngLat(Double.parseDouble(getString(R.string.Jawalakhel_long)), Double.parseDouble(getString(R.string.Jawalakhel_lat)))),
                    Feature.fromGeometry(Point.fromLngLat(Double.parseDouble(getString(R.string.Kupondol_long)), Double.parseDouble(getString(R.string.Kupondol_lat))))


            }));
            loadedMapStyle.addSource(iconGeoJsonSource);
        } else if (origin.equals(Point.fromLngLat(Double.parseDouble(getString(R.string.Lagankhel_long)), Double.parseDouble(getString(R.string.Lagankhel_lat)))) && destination.equals(Point.fromLngLat(Double.parseDouble(getString(R.string.Thapathali_long)), Double.parseDouble(getString(R.string.Thapathali_lat))))) {
            GeoJsonSource iconGeoJsonSource = new GeoJsonSource(ICON_SOURCE_ID, FeatureCollection.fromFeatures(new Feature[]{
                    Feature.fromGeometry(Point.fromLngLat(origin.longitude(), origin.latitude())),
                    Feature.fromGeometry(Point.fromLngLat(destination.longitude(), destination.latitude())),
                    Feature.fromGeometry(Point.fromLngLat(Double.parseDouble(getString(R.string.Kupondol_long)), Double.parseDouble(getString(R.string.Kupondol_lat))))


            }));
            loadedMapStyle.addSource(iconGeoJsonSource);
        } else if (origin.equals(Point.fromLngLat(Double.parseDouble(getString(R.string.Bungamati_long)), Double.parseDouble(getString(R.string.Bungamati_lat)))) && destination.equals(Point.fromLngLat(Double.parseDouble(getString(R.string.Charghare_long)), Double.parseDouble(getString(R.string.Charghare_lat))))) {
            GeoJsonSource iconGeoJsonSource = new GeoJsonSource(ICON_SOURCE_ID, FeatureCollection.fromFeatures(new Feature[]{
                    Feature.fromGeometry(Point.fromLngLat(origin.longitude(), origin.latitude())),
                    Feature.fromGeometry(Point.fromLngLat(destination.longitude(), destination.latitude())),
                    Feature.fromGeometry(Point.fromLngLat(Double.parseDouble(getString(R.string.Bhaisipati_long)), Double.parseDouble(getString(R.string.Bhaisipati_lat))))


            }));
            loadedMapStyle.addSource(iconGeoJsonSource);
        } else if (origin.equals(Point.fromLngLat(Double.parseDouble(getString(R.string.Balkhu_long)), Double.parseDouble(getString(R.string.Balkhu_lat)))) && destination.equals(Point.fromLngLat(Double.parseDouble(getString(R.string.Sankhamul_long)), Double.parseDouble(getString(R.string.Sankhamul_lat))))) {
            GeoJsonSource iconGeoJsonSource = new GeoJsonSource(ICON_SOURCE_ID, FeatureCollection.fromFeatures(new Feature[]{
                    Feature.fromGeometry(Point.fromLngLat(origin.longitude(), origin.latitude())),
                    Feature.fromGeometry(Point.fromLngLat(destination.longitude(), destination.latitude())),
                    Feature.fromGeometry(Point.fromLngLat(Double.parseDouble(getString(R.string.NayaBaneshwor_long)), Double.parseDouble(getString(R.string.NayaBaneshwor_lat))))


            }));
            loadedMapStyle.addSource(iconGeoJsonSource);
        } else if (origin.equals(Point.fromLngLat(Double.parseDouble(getString(R.string.Balkhu_long)), Double.parseDouble(getString(R.string.Balkhu_lat)))) && destination.equals(Point.fromLngLat(Double.parseDouble(getString(R.string.BhatkyaPul_long)), Double.parseDouble(getString(R.string.BhatkyaPul_lat))))) {
            GeoJsonSource iconGeoJsonSource = new GeoJsonSource(ICON_SOURCE_ID, FeatureCollection.fromFeatures(new Feature[]{
                    Feature.fromGeometry(Point.fromLngLat(origin.longitude(), origin.latitude())),
                    Feature.fromGeometry(Point.fromLngLat(destination.longitude(), destination.latitude())),
                    Feature.fromGeometry(Point.fromLngLat(Double.parseDouble(getString(R.string.NayaBaneshwor_long)), Double.parseDouble(getString(R.string.NayaBaneshwor_lat)))),
                    Feature.fromGeometry(Point.fromLngLat(Double.parseDouble(getString(R.string.Shantinagar_long)), Double.parseDouble(getString(R.string.Shantinagar_lat))))


            }));
            loadedMapStyle.addSource(iconGeoJsonSource);
        } else if (origin.equals(Point.fromLngLat(Double.parseDouble(getString(R.string.Bhaktapur_long)), Double.parseDouble(getString(R.string.Bhaktapur_lat)))) && destination.equals(Point.fromLngLat(Double.parseDouble(getString(R.string.PuranoBuspark_long)), Double.parseDouble(getString(R.string.PuranoBuspark_lat))))) {
            GeoJsonSource iconGeoJsonSource = new GeoJsonSource(ICON_SOURCE_ID, FeatureCollection.fromFeatures(new Feature[]{
                    Feature.fromGeometry(Point.fromLngLat(origin.longitude(), origin.latitude())),
                    Feature.fromGeometry(Point.fromLngLat(destination.longitude(), destination.latitude())),
                    Feature.fromGeometry(Point.fromLngLat(Double.parseDouble(getString(R.string.PuranoThimi_long)), Double.parseDouble(getString(R.string.PuranoThimi_lat))))

            }));
            Log.d("INSIDE", "initSource: INSIDE ");
            loadedMapStyle.addSource(iconGeoJsonSource);
        } else {
            GeoJsonSource iconGeoJsonSource = new GeoJsonSource(ICON_SOURCE_ID, FeatureCollection.fromFeatures(new Feature[]{
                    Feature.fromGeometry(Point.fromLngLat(origin.longitude(), origin.latitude())),
                    Feature.fromGeometry(Point.fromLngLat(destination.longitude(), destination.latitude()))
            }));
            loadedMapStyle.addSource(iconGeoJsonSource);
        }
    }

    /**
     * Add the route and marker icon layers to the map
     */
    private void initLayers(@NonNull Style loadedMapStyle) {
        LineLayer routeLayer = new LineLayer(ROUTE_LAYER_ID, ROUTE_SOURCE_ID);

// Add the LineLayer to the map. This layer will display the directions route.
        routeLayer.setProperties(
                lineCap(Property.LINE_CAP_ROUND),
                lineJoin(Property.LINE_JOIN_ROUND),
                iconAllowOverlap(true),
                lineWidth(4f),
                lineColor(Color.parseColor("#009688"))
        );
        loadedMapStyle.addLayer(routeLayer);

        // Add the background LineLayer that will act as the highlighting effect
        backgroundLineLayer = new LineLayer("background-line-layer-id",
                ICON_SOURCE_ID);
        backgroundLineLayer.setProperties(
                lineWidth(routeLayer.getLineWidth().value + 8),
                lineColor(Color.parseColor("#ff8402")),
                lineCap(LINE_CAP_ROUND),
                lineJoin(LINE_JOIN_ROUND),
                visibility(NONE)
        );
        loadedMapStyle.addLayerBelow(backgroundLineLayer, ROUTE_LAYER_ID);
/*
// Add the red marker icon image to the map
        loadedMapStyle.addImage(RED_PIN_ICON_ID, BitmapUtils.getBitmapFromDrawable(
                getResources().getDrawable(R.drawable.busstopmarker)));

// Add the red marker icon SymbolLayer to the map
        loadedMapStyle.addLayer(new SymbolLayer(ICON_LAYER_ID, ICON_SOURCE_ID).withProperties(
                iconImage(RED_PIN_ICON_ID),
                iconIgnorePlacement(true),
                iconAllowOverlap(true),
                iconOffset(new Float[]{0f, -9f})));
        Log.d("INITLAYER", "initLayers: INITLAYER");*/
    }


    /**
     * Make a request to the Mapbox Directions API. Once successful, pass the route to the
     * route layer.
     *
     * @param mapboxMap   the Mapbox map object that the route will be drawn on
     * @param origin      the starting point of the route
     * @param destination the desired finish point of the route
     */

    @RequiresApi(api = Build.VERSION_CODES.Q)
    private void getRoute(MapboxMap mapboxMap, Point origin, Point destination) {

        if (origin.equals(Point.fromLngLat(Double.parseDouble(getString(R.string.Lagankhel_long)), Double.parseDouble(getString(R.string.Lagankhel_lat)))) && destination.equals(Point.fromLngLat(Double.parseDouble(getString(R.string.Tripureshor_long)), Double.parseDouble(getString(R.string.Tripureshor_lat))))) {
            NavigationRoute.builder(this)
                    .accessToken(getString(R.string.mapbox_key))
                    .origin(origin)
                    .destination(destination)
                    .addWaypointNames("Lagankhel", "Jawalakhel", "Kupondol", "Tripureshor")
                    .addWaypoint(Point.fromLngLat(Double.parseDouble(getString(R.string.Jawalakhel_long)), Double.parseDouble(getString(R.string.Jawalakhel_lat))))
                    .addWaypoint(Point.fromLngLat(Double.parseDouble(getString(R.string.Kupondol_long)), Double.parseDouble(getString(R.string.Kupondol_lat))))
                    .profile(DirectionsCriteria.PROFILE_DRIVING)
                    .build()
                    .getRoute(new Callback<DirectionsResponse>() {
                        @Override
                        public void onResponse(Call<DirectionsResponse> call, Response<DirectionsResponse> response) {
                            // You can get the generic HTTP info about the response
                            Timber.d("Response code: " + response.code());
                            if (response.body() == null) {
                                Timber.e("No routes found, make sure you set the right user and access token.");
                                Log.d("Route", "onResponse: No route error" + response);
                                return;
                            } else if (response.body().routes().size() < 1) {
                                Timber.e("No routes found");
                                Log.d("DISTANCE REMAINING", "onResponse: ");
                                return;
                            }

                            currentRoute = response.body().routes().get(0);
                            getAnnotations(mapboxMap);


                            if (mMap != null) {
                                mMap.getStyle(new Style.OnStyleLoaded() {
                                    @Override
                                    public void onStyleLoaded(@NonNull Style style) {

// Retrieve and update the source designated for showing the directions route
                                        GeoJsonSource source = style.getSourceAs(ROUTE_SOURCE_ID);

// Create a LineString with the directions route's geometry and
// reset the GeoJSON source for the route LineLayer source
                                        if (source != null) {
                                            source.setGeoJson(LineString.fromPolyline(currentRoute.geometry(), PRECISION_6));


                                        }
                                    }
                                });
                            }
                        }

                        @Override
                        public void onFailure(Call<DirectionsResponse> call, Throwable t) {

                        }
                    });


        } else if (origin.equals(Point.fromLngLat(Double.parseDouble(getString(R.string.Lagankhel_long)), Double.parseDouble(getString(R.string.Lagankhel_lat)))) && destination.equals(Point.fromLngLat(Double.parseDouble(getString(R.string.Thapathali_long)), Double.parseDouble(getString(R.string.Thapathali_lat))))) {
            NavigationRoute.builder(this)
                    .accessToken(getString(R.string.mapbox_key))
                    .origin(origin)
                    .destination(destination)
                    .addWaypoint(Point.fromLngLat(Double.parseDouble(getString(R.string.Kupondol_long)), Double.parseDouble(getString(R.string.Kupondol_lat))))
                    .addWaypointNames("Lagankhel", "Kupondol", "Thapathali")
                    .profile(DirectionsCriteria.PROFILE_DRIVING)
                    .build()
                    .getRoute(new Callback<DirectionsResponse>() {
                        @Override
                        public void onResponse(Call<DirectionsResponse> call, Response<DirectionsResponse> response) {
                            // You can get the generic HTTP info about the response
                            Timber.d("Response code: " + response.code());
                            if (response.body() == null) {
                                Timber.e("No routes found, make sure you set the right user and access token.");
                                Log.d("Route", "onResponse: No route error" + response);
                                return;
                            } else if (response.body().routes().size() < 1) {
                                Timber.e("No routes found");
                                Log.d("Route", "onResponse: No route" + response);
                                return;
                            }

                            currentRoute = response.body().routes().get(0);
                            getAnnotations(mapboxMap);

                            if (mMap != null) {
                                mMap.getStyle(new Style.OnStyleLoaded() {
                                    @Override
                                    public void onStyleLoaded(@NonNull Style style) {

// Retrieve and update the source designated for showing the directions route
                                        GeoJsonSource source = style.getSourceAs(ROUTE_SOURCE_ID);

// Create a LineString with the directions route's geometry and
// reset the GeoJSON source for the route LineLayer source
                                        if (source != null) {
                                            source.setGeoJson(LineString.fromPolyline(currentRoute.geometry(), PRECISION_6));


                                        }
                                    }
                                });
                            }
                        }

                        @Override
                        public void onFailure(Call<DirectionsResponse> call, Throwable t) {

                        }
                    });

        } else if (origin.equals(Point.fromLngLat(Double.parseDouble(getString(R.string.Bungamati_long)), Double.parseDouble(getString(R.string.Bungamati_lat)))) && destination.equals(Point.fromLngLat(Double.parseDouble(getString(R.string.Charghare_long)), Double.parseDouble(getString(R.string.Charghare_lat))))) {
            NavigationRoute.builder(this)
                    .accessToken(getString(R.string.mapbox_key))
                    .origin(origin)
                    .destination(destination)
                    .addWaypoint(Point.fromLngLat(Double.parseDouble(getString(R.string.Bhaisipati_long)), Double.parseDouble(getString(R.string.Bhaisipati_lat))))
                    .addWaypointNames("Bungamati", "Bhaisipati", "Charghare")
                    .profile(DirectionsCriteria.PROFILE_DRIVING)
                    .build()
                    .getRoute(new Callback<DirectionsResponse>() {
                        @Override
                        public void onResponse(Call<DirectionsResponse> call, Response<DirectionsResponse> response) {
                            // You can get the generic HTTP info about the response
                            Timber.d("Response code: " + response.code());
                            if (response.body() == null) {
                                Timber.e("No routes found, make sure you set the right user and access token.");
                                Log.d("Route", "onResponse: No route error" + response);
                                return;
                            } else if (response.body().routes().size() < 1) {
                                Timber.e("No routes found");
                                Log.d("Route", "onResponse: No route" + response);
                                return;
                            }

                            currentRoute = response.body().routes().get(0);
                            getAnnotations(mapboxMap);

                            if (mMap != null) {
                                mMap.getStyle(new Style.OnStyleLoaded() {
                                    @Override
                                    public void onStyleLoaded(@NonNull Style style) {

// Retrieve and update the source designated for showing the directions route
                                        GeoJsonSource source = style.getSourceAs(ROUTE_SOURCE_ID);

// Create a LineString with the directions route's geometry and
// reset the GeoJSON source for the route LineLayer source
                                        if (source != null) {
                                            source.setGeoJson(LineString.fromPolyline(currentRoute.geometry(), PRECISION_6));


                                        }
                                    }
                                });
                            }
                        }

                        @Override
                        public void onFailure(Call<DirectionsResponse> call, Throwable t) {

                        }
                    });

        } else if (origin.equals(Point.fromLngLat(Double.parseDouble(getString(R.string.Balkhu_long)), Double.parseDouble(getString(R.string.Balkhu_lat)))) && destination.equals(Point.fromLngLat(Double.parseDouble(getString(R.string.Sankhamul_long)), Double.parseDouble(getString(R.string.Sankhamul_lat))))) {
            NavigationRoute.builder(this)
                    .accessToken(getString(R.string.mapbox_key))
                    .origin(origin)
                    .destination(destination)
                    .addWaypoint(Point.fromLngLat(Double.parseDouble(getString(R.string.NayaBaneshwor_long)), Double.parseDouble(getString(R.string.NayaBaneshwor_lat))))
                    .addWaypointNames("Balkhu", "NayaBaneshwor", "Sankhamul")
                    .profile(DirectionsCriteria.PROFILE_DRIVING)
                    .build()
                    .getRoute(new Callback<DirectionsResponse>() {
                        @Override
                        public void onResponse(Call<DirectionsResponse> call, Response<DirectionsResponse> response) {
                            // You can get the generic HTTP info about the response
                            Timber.d("Response code: " + response.code());
                            if (response.body() == null) {
                                Timber.e("No routes found, make sure you set the right user and access token.");
                                Log.d("Route", "onResponse: No route error" + response);
                                return;
                            } else if (response.body().routes().size() < 1) {
                                Timber.e("No routes found");
                                Log.d("Route", "onResponse: No route" + response);
                                return;
                            }

                            currentRoute = response.body().routes().get(0);
                            getAnnotations(mapboxMap);

                            if (mMap != null) {
                                mMap.getStyle(new Style.OnStyleLoaded() {
                                    @Override
                                    public void onStyleLoaded(@NonNull Style style) {

// Retrieve and update the source designated for showing the directions route
                                        GeoJsonSource source = style.getSourceAs(ROUTE_SOURCE_ID);

// Create a LineString with the directions route's geometry and
// reset the GeoJSON source for the route LineLayer source
                                        if (source != null) {
                                            source.setGeoJson(LineString.fromPolyline(currentRoute.geometry(), PRECISION_6));


                                        }
                                    }
                                });
                            }
                        }

                        @Override
                        public void onFailure(Call<DirectionsResponse> call, Throwable t) {

                        }
                    });

        } else if (origin.equals(Point.fromLngLat(Double.parseDouble(getString(R.string.Balkhu_long)), Double.parseDouble(getString(R.string.Balkhu_lat)))) && destination.equals(Point.fromLngLat(Double.parseDouble(getString(R.string.BhatkyaPul_long)), Double.parseDouble(getString(R.string.BhatkyaPul_lat))))) {
            NavigationRoute.builder(this)
                    .accessToken(getString(R.string.mapbox_key))
                    .origin(origin)
                    .destination(destination)
                    .addWaypoint(Point.fromLngLat(Double.parseDouble(getString(R.string.NayaBaneshwor_long)), Double.parseDouble(getString(R.string.NayaBaneshwor_lat))))
                    .addWaypoint(Point.fromLngLat(Double.parseDouble(getString(R.string.Shantinagar_long)), Double.parseDouble(getString(R.string.Shantinagar_lat))))
                    .addWaypointNames("Balkhu", "NayaBaneshwor", "Shantinagar", "Bhatkya Pul")
                    .profile(DirectionsCriteria.PROFILE_DRIVING)
                    .build()
                    .getRoute(new Callback<DirectionsResponse>() {
                        @Override
                        public void onResponse(Call<DirectionsResponse> call, Response<DirectionsResponse> response) {
                            // You can get the generic HTTP info about the response
                            Timber.d("Response code: " + response.code());
                            if (response.body() == null) {
                                Timber.e("No routes found, make sure you set the right user and access token.");
                                Log.d("Route", "onResponse: No route error" + response);
                                return;
                            } else if (response.body().routes().size() < 1) {
                                Timber.e("No routes found");
                                Log.d("Route", "onResponse: No route" + response);
                                return;
                            }

                            currentRoute = response.body().routes().get(0);
                            getAnnotations(mapboxMap);

                            if (mMap != null) {
                                mMap.getStyle(new Style.OnStyleLoaded() {
                                    @Override
                                    public void onStyleLoaded(@NonNull Style style) {

// Retrieve and update the source designated for showing the directions route
                                        GeoJsonSource source = style.getSourceAs(ROUTE_SOURCE_ID);

// Create a LineString with the directions route's geometry and
// reset the GeoJSON source for the route LineLayer source
                                        if (source != null) {
                                            source.setGeoJson(LineString.fromPolyline(currentRoute.geometry(), PRECISION_6));


                                        }
                                    }
                                });
                            }
                        }

                        @Override
                        public void onFailure(Call<DirectionsResponse> call, Throwable t) {

                        }
                    });

        } else if (origin.equals(Point.fromLngLat(Double.parseDouble(getString(R.string.Bhaktapur_long)), Double.parseDouble(getString(R.string.Bhaktapur_lat)))) && destination.equals(Point.fromLngLat(Double.parseDouble(getString(R.string.PuranoBuspark_long)), Double.parseDouble(getString(R.string.PuranoBuspark_lat))))) {
            NavigationRoute.builder(this)
                    .accessToken(getString(R.string.mapbox_key))
                    .origin(origin)
                    .destination(destination)
                    .addWaypoint(Point.fromLngLat(Double.parseDouble(getString(R.string.PuranoThimi_long)), Double.parseDouble(getString(R.string.PuranoThimi_lat))))
                    .addWaypointNames("Bhaktapur", "Purano Thimi", "Purano Bus Park")
                    .profile(DirectionsCriteria.PROFILE_DRIVING)
                    .build()
                    .getRoute(new Callback<DirectionsResponse>() {
                        @Override
                        public void onResponse(Call<DirectionsResponse> call, Response<DirectionsResponse> response) {
                            // You can get the generic HTTP info about the response
                            Timber.d("Response code: " + response.code());
                            if (response.body() == null) {
                                Timber.e("No routes found, make sure you set the right user and access token.");
                                Log.d("Route", "onResponse: No route error" + response);
                                return;
                            } else if (response.body().routes().size() < 1) {
                                Timber.e("No routes found");
                                Log.d("Route", "onResponse: No route" + response);
                                return;
                            }

                            currentRoute = response.body().routes().get(0);
                            getAnnotations(mapboxMap);

                            if (mMap != null) {
                                mMap.getStyle(new Style.OnStyleLoaded() {
                                    @Override
                                    public void onStyleLoaded(@NonNull Style style) {

// Retrieve and update the source designated for showing the directions route
                                        GeoJsonSource source = style.getSourceAs(ROUTE_SOURCE_ID);

// Create a LineString with the directions route's geometry and
// reset the GeoJSON source for the route LineLayer source
                                        if (source != null) {
                                            source.setGeoJson(LineString.fromPolyline(currentRoute.geometry(), PRECISION_6));


                                        }
                                    }
                                });
                            }
                        }

                        @Override
                        public void onFailure(Call<DirectionsResponse> call, Throwable t) {

                        }
                    });

        } else {
            Log.d("DEBUG", "getRoute: "+clickedroute.toString());
            NavigationRoute.builder(this)
                    .accessToken(getString(R.string.mapbox_key))
                    .origin(origin)
                    .destination(destination)
                    .addWaypointNames(busStopOrigin, busStopDestination)
                    .profile(DirectionsCriteria.PROFILE_DRIVING)
                    .build()
                    .getRoute(new Callback<DirectionsResponse>() {
                        @Override
                        public void onResponse(Call<DirectionsResponse> call, Response<DirectionsResponse> response) {
                            // You can get the generic HTTP info about the response
                            Timber.d("Response code: " + response.code());
                            if (response.body() == null) {
                                Timber.e("No routes found, make sure you set the right user and access token.");
                                Log.d("Route", "onResponse: No route error" + response);
                                return;
                            } else if (response.body().routes().size() < 1) {
                                Timber.e("No routes found");
                                Log.d("Route", "onResponse: No route" + response);
                                return;
                            }

                            currentRoute = response.body().routes().get(0);
                            getAnnotations(mapboxMap);

                            if (mMap != null) {
                                mMap.getStyle(new Style.OnStyleLoaded() {
                                    @Override
                                    public void onStyleLoaded(@NonNull Style style) {

// Retrieve and update the source designated for showing the directions route
                                        GeoJsonSource source = style.getSourceAs(ROUTE_SOURCE_ID);

// Create a LineString with the directions route's geometry and
// reset the GeoJSON source for the route LineLayer source
                                        if (source != null) {
                                            source.setGeoJson(LineString.fromPolyline(currentRoute.geometry(), PRECISION_6));


                                        }
                                    }
                                });
                            }
                        }

                        @Override
                        public void onFailure(Call<DirectionsResponse> call, Throwable t) {

                        }
                    });

        }


    }

    //result from search
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESPOND_CODE_SEARCH_POPUP) {

            clickedroute.setValue(data.getStringExtra("Route"));

        }

        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_AUTOCOMPLETE) {

            // Retrieve selected location's CarmenFeature
            CarmenFeature selectedCarmenFeature = PlaceAutocomplete.getPlace(data);

            // Move map camera to the selected location
            LatLng latLng = new LatLng(((Point) selectedCarmenFeature.geometry()).latitude(),
                    ((Point) selectedCarmenFeature.geometry()).longitude());

            double lagankhel = meterDistanceBetweenPoints(latLng.getLatitude(), latLng.getLongitude(), Double.parseDouble(getString(R.string.Lagankhel_lat)), Double.parseDouble(getString(R.string.Lagankhel_long)));
            double jawalakhel = meterDistanceBetweenPoints(latLng.getLatitude(), latLng.getLongitude(), Double.parseDouble(getString(R.string.Jawalakhel_lat)), Double.parseDouble(getString(R.string.Jawalakhel_long)));
            double kupondol = meterDistanceBetweenPoints(latLng.getLatitude(), latLng.getLongitude(), Double.parseDouble(getString(R.string.Kupondol_lat)), Double.parseDouble(getString(R.string.Kupondol_long)));
            double tripureswor = meterDistanceBetweenPoints(latLng.getLatitude(), latLng.getLongitude(), Double.parseDouble(getString(R.string.Tripureshor_lat)), Double.parseDouble(getString(R.string.Tripureshor_long)));
            double thapathali = meterDistanceBetweenPoints(latLng.getLatitude(), latLng.getLongitude(), Double.parseDouble(getString(R.string.Thapathali_lat)), Double.parseDouble(getString(R.string.Thapathali_long)));
            double bungamati = meterDistanceBetweenPoints(latLng.getLatitude(), latLng.getLongitude(), Double.parseDouble(getString(R.string.Bungamati_lat)), Double.parseDouble(getString(R.string.Bungamati_long)));
            double bhaisepati = meterDistanceBetweenPoints(latLng.getLatitude(), latLng.getLongitude(), Double.parseDouble(getString(R.string.Bhaisipati_lat)), Double.parseDouble(getString(R.string.Bhaisipati_long)));
            double charghare = meterDistanceBetweenPoints(latLng.getLatitude(), latLng.getLongitude(), Double.parseDouble(getString(R.string.Charghare_lat)), Double.parseDouble(getString(R.string.Charghare_long)));
            double attarkhel = meterDistanceBetweenPoints(latLng.getLatitude(), latLng.getLongitude(), Double.parseDouble(getString(R.string.Attarkhel_lat)), Double.parseDouble(getString(R.string.Attarkhel_long)));
            double puranobuspark = meterDistanceBetweenPoints(latLng.getLatitude(), latLng.getLongitude(), Double.parseDouble(getString(R.string.PuranoBuspark_lat)), Double.parseDouble(getString(R.string.Pulchowk_long)));
            double bagbazar = meterDistanceBetweenPoints(latLng.getLatitude(), latLng.getLongitude(), Double.parseDouble(getString(R.string.Bagbazaar_lat)), Double.parseDouble(getString(R.string.Bagbazaar_long)));
            double ratnapark = meterDistanceBetweenPoints(latLng.getLatitude(), latLng.getLongitude(), Double.parseDouble(getString(R.string.Ratnapark_lat)), Double.parseDouble(getString(R.string.Ratnapark_long)));
            double kamalbinayak = meterDistanceBetweenPoints(latLng.getLatitude(), latLng.getLongitude(), Double.parseDouble(getString(R.string.Kamalbinayak_lat)), Double.parseDouble(getString(R.string.Kamalbinayak_long)));
            double balaju = meterDistanceBetweenPoints(latLng.getLatitude(), latLng.getLongitude(), Double.parseDouble(getString(R.string.Balaju_lat)), Double.parseDouble(getString(R.string.Balaju_long)));
            double sankhamul = meterDistanceBetweenPoints(latLng.getLatitude(), latLng.getLongitude(), Double.parseDouble(getString(R.string.SamakhusiChowk_lat)), Double.parseDouble(getString(R.string.SamakhusiChowk_long)));
            double balkhu = meterDistanceBetweenPoints(latLng.getLatitude(), latLng.getLongitude(), Double.parseDouble(getString(R.string.Balkhu_lat)), Double.parseDouble(getString(R.string.Balkhu_long)));
            double nayabaneswor = meterDistanceBetweenPoints(latLng.getLatitude(), latLng.getLongitude(), Double.parseDouble(getString(R.string.NayaBaneshwor_lat)), Double.parseDouble(getString(R.string.NayaBaneshwor_long)));
            double shantinagar = meterDistanceBetweenPoints(latLng.getLatitude(), latLng.getLongitude(), Double.parseDouble(getString(R.string.Shantinagar_lat)), Double.parseDouble(getString(R.string.Shantinagar_long)));
            double bhatkyapul = meterDistanceBetweenPoints(latLng.getLatitude(), latLng.getLongitude(), Double.parseDouble(getString(R.string.BhatkyaPul_lat)), Double.parseDouble(getString(R.string.BhatkyaPul_long)));
            double balkumari = meterDistanceBetweenPoints(latLng.getLatitude(), latLng.getLongitude(), Double.parseDouble(getString(R.string.Balkumari_lat)), Double.parseDouble(getString(R.string.Balkumari_long)));
            double gopikrishna = meterDistanceBetweenPoints(latLng.getLatitude(), latLng.getLongitude(), Double.parseDouble(getString(R.string.GopiKrishna_lat)), Double.parseDouble(getString(R.string.GopiKrishna_long)));
            double bhaktapur = meterDistanceBetweenPoints(latLng.getLatitude(), latLng.getLongitude(), Double.parseDouble(getString(R.string.Bhaktapur_lat)), Double.parseDouble(getString(R.string.Bhaktapur_long)));
            double puranothimi = meterDistanceBetweenPoints(latLng.getLatitude(), latLng.getLongitude(), Double.parseDouble(getString(R.string.PuranoThimi_lat)), Double.parseDouble(getString(R.string.PuranoThimi_long)));
            double budanilkantha = meterDistanceBetweenPoints(latLng.getLatitude(), latLng.getLongitude(), Double.parseDouble(getString(R.string.BudhanilkanthaSchool_lat)), Double.parseDouble(getString(R.string.BudhanilkanthaSchool_long)));
            double changu = meterDistanceBetweenPoints(latLng.getLatitude(), latLng.getLongitude(), Double.parseDouble(getString(R.string.Changu_lat)), Double.parseDouble(getString(R.string.Changu_long)));
            double samakhusichowk = meterDistanceBetweenPoints(latLng.getLatitude(), latLng.getLongitude(), Double.parseDouble(getString(R.string.SamakhusiChowk_lat)), Double.parseDouble(getString(R.string.SamakhusiChowk_long)));
            double boudha = meterDistanceBetweenPoints(latLng.getLatitude(), latLng.getLongitude(), Double.parseDouble(getString(R.string.Boudha_lat)), Double.parseDouble(getString(R.string.Boudha_long)));
            double dilibazar = meterDistanceBetweenPoints(latLng.getLatitude(), latLng.getLongitude(), Double.parseDouble(getString(R.string.Dillibazaar_lat)), Double.parseDouble(getString(R.string.Dillibazaar_long)));
            double putalisadak = meterDistanceBetweenPoints(latLng.getLatitude(), latLng.getLongitude(), Double.parseDouble(getString(R.string.Putalisadak_lat)), Double.parseDouble(getString(R.string.Putalisadak_long)));
            double pulchowk = meterDistanceBetweenPoints(latLng.getLatitude(), latLng.getLongitude(), Double.parseDouble(getString(R.string.Pulchowk_lat)), Double.parseDouble(getString(R.string.Pulchowk_long)));

            double[] busStops = {lagankhel, jawalakhel, kupondol, tripureswor, thapathali, bungamati, bhaisepati, charghare, attarkhel, puranobuspark, bagbazar,
                    ratnapark, kamalbinayak, balaju, sankhamul, balkhu, nayabaneswor, shantinagar, bhatkyapul, balkumari, gopikrishna, bhaktapur, puranothimi, budanilkantha,
                    changu, samakhusichowk, boudha, dilibazar, putalisadak, pulchowk};

            HashMap<Double, String> busStopMap = new HashMap<>();
            busStopMap.put(lagankhel, "Lagankhel");
            busStopMap.put(jawalakhel, "Jawalakhel");
            busStopMap.put(kupondol, "Kupondol");
            busStopMap.put(tripureswor, "Tripureshor");
            busStopMap.put(thapathali, "Thapathali");
            busStopMap.put(bungamati, "Bungamati");
            busStopMap.put(bhaisepati, "Bhaisipati");
            busStopMap.put(charghare, "Charghare");
            busStopMap.put(attarkhel, "Attarkhel");
            busStopMap.put(puranobuspark, "Purano Buspark");
            busStopMap.put(bagbazar, "Bagbazaar");
            busStopMap.put(ratnapark, "Ratnapark");
            busStopMap.put(kamalbinayak, "Kamalbinayak");
            busStopMap.put(balaju, "Balaju");
            busStopMap.put(sankhamul, "Sankhamul");
            busStopMap.put(balkhu, "Balkhu");
            busStopMap.put(nayabaneswor, "NayaBaneshwor");
            busStopMap.put(shantinagar, "Shantinagar");
            busStopMap.put(bhatkyapul, "Bhatkya Pul");
            busStopMap.put(balkumari, "Balkumari");
            busStopMap.put(gopikrishna, "Gopi Krishna");
            busStopMap.put(bhaktapur, "Bhaktapur");
            busStopMap.put(puranothimi, "Purano Thimi");
            busStopMap.put(budanilkantha, "Budhanilkantha School");
            busStopMap.put(changu, "Changu");
            busStopMap.put(samakhusichowk, "Samakhusi Chowk");
            busStopMap.put(boudha, "Boudha");
            busStopMap.put(dilibazar, "Dillibazaar");
            busStopMap.put(putalisadak, "Putalisadak");
            busStopMap.put(pulchowk, "Pulchowk");


            double temp, size;
            size = busStops.length;
            for (int i = 0; i < size; i++) {
                for (int j = i + 1; j < size; j++) {
                    if (busStops[i] > busStops[j]) {
                        temp = busStops[i];
                        busStops[i] = busStops[j];
                        busStops[j] = temp;
                    }
                }
            }
            nearestStop.setValue(busStopMap.get(busStops[0]));

        }


    }


    private double meterDistanceBetweenPoints(double latitude, double longitude, double parseDouble, double parseDouble1) {
        float pk = (float) (180.f / Math.PI);
        float a1 = (float) latitude / pk;
        float a2 = (float) longitude / pk;
        float b1 = (float) parseDouble / pk;
        float b2 = (float) parseDouble1 / pk;

        double t1 = Math.cos(a1) * Math.cos(a2) * Math.cos(b1) * Math.cos(b2);
        double t2 = Math.cos(a1) * Math.sin(a2) * Math.cos(b1) * Math.sin(b2);
        double t3 = Math.sin(a1) * Math.sin(b1);
        double tt = Math.acos(t1 + t2 + t3);

        return 6366000 * tt;
    }

    public void getAnnotations(MapboxMap mapboxMap) {
        //trying to add annotations
        if (currentRoute != null) {
            List<Point> waypoints = currentRoute.routeOptions().coordinates();
            List<String> waypointNames = currentRoute.routeOptions().waypointNamesList();
            View[] markerAnnotation = new View[waypoints.size()];
            TextView[] busStopName = new TextView[waypoints.size()];
            Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/abel.ttf");
            Bitmap[] bitmaps = new Bitmap[waypoints.size()];
            mapboxMap.getStyle(new Style.OnStyleLoaded() {
                @Override
                public void onStyleLoaded(@NonNull Style style) {
                    for (int i = 0; i < waypoints.size(); i++) {
                        markerAnnotation[i] = LayoutInflater.from(MainActivityy.this).inflate(R.layout.marker_annotation, null);
                        markerAnnotation[i].setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                        busStopName[i] = markerAnnotation[i].findViewById(R.id.busStopName);
                        busStopName[i].setText(waypointNames.get(i).trim());
                        busStopName[i].setTypeface(typeface);
                        markerAnnotation[i].setDrawingCacheEnabled(true);
                        markerAnnotation[i].measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
                        markerAnnotation[i].layout(0, 0, markerAnnotation[i].getMeasuredWidth(), markerAnnotation[i].getMeasuredHeight());
                        markerAnnotation[i].buildDrawingCache(true);
                        bitmaps[i] = Bitmap.createBitmap(markerAnnotation[i].getDrawingCache());
                        markerAnnotation[i].setDrawingCacheEnabled(false);
                        style.addImage(waypointNames.get(i), bitmaps[i]);

                        SymbolManager symbolManager = new SymbolManager(mapView, mapboxMap, style);
                        symbolManager.setIconAllowOverlap(true);
                        symbolManager.setIconIgnorePlacement(true);
                        Symbol symbol = symbolManager.create(new SymbolOptions()
                                .withLatLng(new LatLng(waypoints.get(i).latitude(), waypoints.get(i).longitude()))
                                .withIconImage(waypointNames.get(i))
                                .withIconSize(0.6f)
                                .withIconOffset(new Float[]{0f, -40f}));
                    }
                }
            });
        }

        //end of trying
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
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }


}








