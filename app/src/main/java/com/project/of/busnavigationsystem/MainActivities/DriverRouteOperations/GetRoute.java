package com.project.of.busnavigationsystem.MainActivities.DriverRouteOperations;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mapbox.api.directions.v5.DirectionsCriteria;
import com.mapbox.api.directions.v5.models.DirectionsResponse;
import com.mapbox.api.directions.v5.models.DirectionsRoute;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.LineString;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.geometry.LatLngBounds;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.plugins.annotation.Symbol;
import com.mapbox.mapboxsdk.plugins.annotation.SymbolManager;
import com.mapbox.mapboxsdk.plugins.annotation.SymbolOptions;
import com.mapbox.mapboxsdk.style.expressions.Expression;
import com.mapbox.mapboxsdk.style.layers.LineLayer;
import com.mapbox.mapboxsdk.style.layers.Property;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;
import com.mapbox.mapboxsdk.utils.BitmapUtils;
import com.mapbox.services.android.navigation.v5.location.replay.ReplayRouteLocationEngine;
import com.mapbox.services.android.navigation.v5.navigation.MapboxNavigation;
import com.mapbox.services.android.navigation.v5.navigation.NavigationRoute;
import com.mapbox.services.android.navigation.v5.offroute.OffRouteListener;
import com.mapbox.services.android.navigation.v5.routeprogress.ProgressChangeListener;
import com.mapbox.services.android.navigation.v5.routeprogress.RouteProgress;
import com.project.of.busnavigationsystem.MainActivities.MainActivityy_driver;
import com.project.of.busnavigationsystem.R;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

import static com.mapbox.core.constants.Constants.PRECISION_6;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconAllowOverlap;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconIgnorePlacement;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconImage;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconOffset;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconSize;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineCap;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineColor;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineJoin;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineWidth;

public class GetRoute extends AppCompatActivity {
    public static MapboxMap mMap;
    public static DirectionsRoute currentRoute;
    private static final String ROUTE_LAYER_ID = "route-layer-id";
    private static final String ROUTE_SOURCE_ID = "route-source-id";
    private static final String ICON_LAYER_ID = "icon-layer-id";
    private static final String ICON_SOURCE_ID = "icon-source-id";
    private static final String RED_PIN_ICON_ID = "red-pin-icon-id";
    public static MapView mapView;
    private static GetAnnotations getAnnotations;

    public static MapboxNavigation navigation;
    public static ProgressChangeListener progressChangeListener;

    static Double distanceRemaining;
    static Double distanceTraveled;
    static Double durationRemaining;
    public static int remainingWaypoints;
    public static int totalremainingWaypoints;


    public static boolean executedonce, executedValueEventListener = false;
    public static boolean[] executed, executed0, executed5, executed15, executed30;
    public static DatabaseReference reff;
    public static String userid;
    public static ValueEventListener valueEventListener, sendNoti;
    public static ChildEventListener childEventListener;
    public static Map<String, Object> map;
    public static Double[] distanceToWaypoint = new Double[5];
    public static Double[] durationToWaypoint = new Double[5];
    public static Double tempWaypointDistance = 0.0;
    public static Double tempWaypointDuration = 0.0;


    //notification
    final private static String FCM_API = "https://fcm.googleapis.com/fcm/send";
    final private static String serverKey = "key=" + "AAAAlmTH0Z8:APA91bG6ykJNVJoUcKNqnGUqlGGirvbhTqPcbuK3z4BPpm9qK0qrEax34_C6OhcHQ5ovlYMZS7J3B6pTpvsTSLc-lmcDoB0tTIdcrwpQK_pwSvMGs2jAvTZO04yhFD5dgLih2chtE5YV";
    final private static String contentType = "application/json";
    final String TAG = "NOTIFICATION TAG";

    static String NOTIFICATION_TITLE;
    static String NOTIFICATION_MESSAGE;
    static String TOPIC;


    /**
     * Add the route and marker sources to the map
     */
    public static void initSource(@NonNull Style loadedMapStyle, Context context, Point origin, Point destination) {
        loadedMapStyle.addSource(new GeoJsonSource(ROUTE_SOURCE_ID));
        if ((origin.equals(Point.fromLngLat(Double.parseDouble(context.getString(R.string.Lagankhel_long)), Double.parseDouble(context.getString(R.string.Lagankhel_lat)))) && destination.equals(Point.fromLngLat(Double.parseDouble(context.getString(R.string.Tripureshor_long)), Double.parseDouble(context.getString(R.string.Tripureshor_lat)))))) {
            GeoJsonSource iconGeoJsonSource = new GeoJsonSource(ICON_SOURCE_ID, FeatureCollection.fromFeatures(new Feature[]{
                    Feature.fromGeometry(Point.fromLngLat(origin.longitude(), origin.latitude())),
                    Feature.fromGeometry(Point.fromLngLat(destination.longitude(), destination.latitude())),
                    Feature.fromGeometry(Point.fromLngLat(Double.parseDouble(context.getString(R.string.Jawalakhel_long)), Double.parseDouble(context.getString(R.string.Jawalakhel_lat)))),
                    Feature.fromGeometry(Point.fromLngLat(Double.parseDouble(context.getString(R.string.Kupondol_long)), Double.parseDouble(context.getString(R.string.Kupondol_lat))))


            }));
            loadedMapStyle.addSource(iconGeoJsonSource);
        } else if (origin.equals(Point.fromLngLat(Double.parseDouble(context.getString(R.string.Lagankhel_long)), Double.parseDouble(context.getString(R.string.Lagankhel_lat)))) && destination.equals(Point.fromLngLat(Double.parseDouble(context.getString(R.string.Thapathali_long)), Double.parseDouble(context.getString(R.string.Thapathali_lat))))) {
            GeoJsonSource iconGeoJsonSource = new GeoJsonSource(ICON_SOURCE_ID, FeatureCollection.fromFeatures(new Feature[]{
                    Feature.fromGeometry(Point.fromLngLat(origin.longitude(), origin.latitude())),
                    Feature.fromGeometry(Point.fromLngLat(destination.longitude(), destination.latitude())),
                    Feature.fromGeometry(Point.fromLngLat(Double.parseDouble(context.getString(R.string.Kupondol_long)), Double.parseDouble(context.getString(R.string.Kupondol_lat))))


            }));
            loadedMapStyle.addSource(iconGeoJsonSource);
        } else if (origin.equals(Point.fromLngLat(Double.parseDouble(context.getString(R.string.Bungamati_long)), Double.parseDouble(context.getString(R.string.Bungamati_lat)))) && destination.equals(Point.fromLngLat(Double.parseDouble(context.getString(R.string.Charghare_long)), Double.parseDouble(context.getString(R.string.Charghare_lat))))) {
            GeoJsonSource iconGeoJsonSource = new GeoJsonSource(ICON_SOURCE_ID, FeatureCollection.fromFeatures(new Feature[]{
                    Feature.fromGeometry(Point.fromLngLat(origin.longitude(), origin.latitude())),
                    Feature.fromGeometry(Point.fromLngLat(destination.longitude(), destination.latitude())),
                    Feature.fromGeometry(Point.fromLngLat(Double.parseDouble(context.getString(R.string.Bhaisipati_long)), Double.parseDouble(context.getString(R.string.Bhaisipati_lat))))


            }));
            loadedMapStyle.addSource(iconGeoJsonSource);
        } else if (origin.equals(Point.fromLngLat(Double.parseDouble(context.getString(R.string.Balkhu_long)), Double.parseDouble(context.getString(R.string.Balkhu_lat)))) && destination.equals(Point.fromLngLat(Double.parseDouble(context.getString(R.string.Sankhamul_long)), Double.parseDouble(context.getString(R.string.Sankhamul_lat))))) {
            GeoJsonSource iconGeoJsonSource = new GeoJsonSource(ICON_SOURCE_ID, FeatureCollection.fromFeatures(new Feature[]{
                    Feature.fromGeometry(Point.fromLngLat(origin.longitude(), origin.latitude())),
                    Feature.fromGeometry(Point.fromLngLat(destination.longitude(), destination.latitude())),
                    Feature.fromGeometry(Point.fromLngLat(Double.parseDouble(context.getString(R.string.NayaBaneshwor_long)), Double.parseDouble(context.getString(R.string.NayaBaneshwor_lat))))


            }));
            loadedMapStyle.addSource(iconGeoJsonSource);
        } else if (origin.equals(Point.fromLngLat(Double.parseDouble(context.getString(R.string.Balkhu_long)), Double.parseDouble(context.getString(R.string.Balkhu_lat)))) && destination.equals(Point.fromLngLat(Double.parseDouble(context.getString(R.string.BhatkyaPul_long)), Double.parseDouble(context.getString(R.string.BhatkyaPul_lat))))) {
            GeoJsonSource iconGeoJsonSource = new GeoJsonSource(ICON_SOURCE_ID, FeatureCollection.fromFeatures(new Feature[]{
                    Feature.fromGeometry(Point.fromLngLat(origin.longitude(), origin.latitude())),
                    Feature.fromGeometry(Point.fromLngLat(destination.longitude(), destination.latitude())),
                    Feature.fromGeometry(Point.fromLngLat(Double.parseDouble(context.getString(R.string.NayaBaneshwor_long)), Double.parseDouble(context.getString(R.string.NayaBaneshwor_lat)))),
                    Feature.fromGeometry(Point.fromLngLat(Double.parseDouble(context.getString(R.string.Shantinagar_long)), Double.parseDouble(context.getString(R.string.Shantinagar_lat))))


            }));
            loadedMapStyle.addSource(iconGeoJsonSource);
        } else if (origin.equals(Point.fromLngLat(Double.parseDouble(context.getString(R.string.Bhaktapur_long)), Double.parseDouble(context.getString(R.string.Bhaktapur_lat)))) && destination.equals(Point.fromLngLat(Double.parseDouble(context.getString(R.string.PuranoBuspark_long)), Double.parseDouble(context.getString(R.string.PuranoBuspark_lat))))) {
            GeoJsonSource iconGeoJsonSource = new GeoJsonSource(ICON_SOURCE_ID, FeatureCollection.fromFeatures(new Feature[]{
                    Feature.fromGeometry(Point.fromLngLat(origin.longitude(), origin.latitude())),
                    Feature.fromGeometry(Point.fromLngLat(destination.longitude(), destination.latitude())),
                    Feature.fromGeometry(Point.fromLngLat(Double.parseDouble(context.getString(R.string.PuranoThimi_long)), Double.parseDouble(context.getString(R.string.PuranoThimi_lat))))

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
    public static void initLayers(@NonNull Style loadedMapStyle, Context context) {
        LineLayer routeLayer = new LineLayer(ROUTE_LAYER_ID, ROUTE_SOURCE_ID);

// Add the LineLayer to the map. This layer will display the directions route.
        routeLayer.setProperties(
                lineCap(Property.LINE_CAP_ROUND),
                lineJoin(Property.LINE_JOIN_ROUND),
                lineWidth(4.5f),
                iconAllowOverlap(true),
                lineColor(Color.parseColor("#674BC5"))
        );
        loadedMapStyle.addLayer(routeLayer);

        // Add the background LineLayer that will act as the highlighting effect
        /*backgroundLineLayer = new LineLayer("background-line-layer-id",
                ICON_SOURCE_ID);
        backgroundLineLayer.setProperties(
                lineWidth(routeLayer.getLineWidth().value + 8),
                lineColor(Color.parseColor("#ff8402")),
                lineCap(LINE_CAP_ROUND),
                lineJoin(LINE_JOIN_ROUND),
                visibility(NONE)
        );
        loadedMapStyle.addLayerBelow(backgroundLineLayer, ROUTE_LAYER_ID);*/

// Add the red marker icon image to the map
        loadedMapStyle.addImage(RED_PIN_ICON_ID, BitmapUtils.getBitmapFromDrawable(
                context.getResources().getDrawable(R.drawable.busstopmarker)));

// Add the red marker icon SymbolLayer to the map
        loadedMapStyle.addLayer(new SymbolLayer(ICON_LAYER_ID, ICON_SOURCE_ID).withProperties(
                iconImage(RED_PIN_ICON_ID),
                iconIgnorePlacement(true),
                iconAllowOverlap(true),
                iconSize((Float)0.2f),
                iconOffset(new Float[]{0f, -9f})));
        Log.d("INITLAYER", "initLayers: INITLAYER");

    }

@RequiresApi(api = Build.VERSION_CODES.Q)
public static void getRoute(MapboxMap mapboxMap, Point origin, Point destination, Context context, Activity activity, String callingActivity,String driverSelectedRoute) {
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
                initSource(style, context, origin, destination);
                initLayers(style, context);

            }
        });


        if (origin.equals(Point.fromLngLat(Double.parseDouble(context.getString(R.string.Lagankhel_long)), Double.parseDouble(context.getString(R.string.Lagankhel_lat)))) && destination.equals(Point.fromLngLat(Double.parseDouble(context.getString(R.string.Tripureshor_long)), Double.parseDouble(context.getString(R.string.Tripureshor_lat))))) {
            NavigationRoute.builder(context)
                    .accessToken(context.getString(R.string.mapbox_key))
                    .origin(origin)
                    .destination(destination)
                    .addWaypointNames("Lagankhel", "Jawalakhel", "Kupondol", "Tripureshor")
                    .addWaypoint(Point.fromLngLat(Double.parseDouble(context.getString(R.string.Jawalakhel_long)), Double.parseDouble(context.getString(R.string.Jawalakhel_lat))))
                    .addWaypoint(Point.fromLngLat(Double.parseDouble(context.getString(R.string.Kupondol_long)), Double.parseDouble(context.getString(R.string.Kupondol_lat))))
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


                            if (mapboxMap != null) {
                                mapboxMap.getStyle(new Style.OnStyleLoaded() {
                                    @Override
                                    public void onStyleLoaded(@NonNull Style style) {

// Retrieve and update the source designated for showing the directions route
                                        GeoJsonSource source = style.getSourceAs(ROUTE_SOURCE_ID);

// Create a LineString with the directions route's geometry and
// reset the GeoJSON source for the route LineLayer source
                                        if (source != null) {
                                            source.setGeoJson(LineString.fromPolyline(currentRoute.geometry(), PRECISION_6));
                                            if (callingActivity.equals("MainActivityy_driver")) {
                                                getNav(currentRoute, activity, context,driverSelectedRoute);
                                            }

                                        }
                                    }
                                });
                            }
                        }

                        @Override
                        public void onFailure(Call<DirectionsResponse> call, Throwable t) {

                        }
                    });
            LatLngBounds latLngBounds = new LatLngBounds.Builder()
                    .include(new LatLng(origin.latitude(), origin.longitude()))
                    .include(new LatLng(destination.latitude(), destination.longitude()))
                    .build();

            mapboxMap.animateCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, 20));


        } else if (origin.equals(Point.fromLngLat(Double.parseDouble(context.getString(R.string.Lagankhel_long)), Double.parseDouble(context.getString(R.string.Lagankhel_lat)))) && destination.equals(Point.fromLngLat(Double.parseDouble(context.getString(R.string.Thapathali_long)), Double.parseDouble(context.getString(R.string.Thapathali_lat))))) {
            NavigationRoute.builder(context)
                    .accessToken(context.getString(R.string.mapbox_key))
                    .origin(origin)
                    .destination(destination)
                    .addWaypoint(Point.fromLngLat(Double.parseDouble(context.getString(R.string.Kupondol_long)), Double.parseDouble(context.getString(R.string.Kupondol_lat))))
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


                            if (mapboxMap != null) {
                                mapboxMap.getStyle(new Style.OnStyleLoaded() {
                                    @Override
                                    public void onStyleLoaded(@NonNull Style style) {

// Retrieve and update the source designated for showing the directions route
                                        GeoJsonSource source = style.getSourceAs(ROUTE_SOURCE_ID);

// Create a LineString with the directions route's geometry and
// reset the GeoJSON source for the route LineLayer source
                                        if (source != null) {
                                            source.setGeoJson(LineString.fromPolyline(currentRoute.geometry(), PRECISION_6));
                                            if (callingActivity.equals("MainActivityy_driver")) {
                                                getNav(currentRoute, activity, context,driverSelectedRoute);
                                            }


                                        }
                                    }
                                });
                            }
                        }

                        @Override
                        public void onFailure(Call<DirectionsResponse> call, Throwable t) {

                        }
                    });
            LatLngBounds latLngBounds = new LatLngBounds.Builder()
                    .include(new LatLng(origin.latitude(), origin.longitude()))
                    .include(new LatLng(destination.latitude(), destination.longitude()))
                    .build();

            mapboxMap.animateCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, 20));

        } else if (origin.equals(Point.fromLngLat(Double.parseDouble(context.getString(R.string.Bungamati_long)), Double.parseDouble(context.getString(R.string.Bungamati_lat)))) && destination.equals(Point.fromLngLat(Double.parseDouble(context.getString(R.string.Charghare_long)), Double.parseDouble(context.getString(R.string.Charghare_lat))))) {
            NavigationRoute.builder(context)
                    .accessToken(context.getString(R.string.mapbox_key))
                    .origin(origin)
                    .destination(destination)
                    .addWaypoint(Point.fromLngLat(Double.parseDouble(context.getString(R.string.Bhaisipati_long)), Double.parseDouble(context.getString(R.string.Bhaisipati_lat))))
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


                            if (mapboxMap != null) {
                                mapboxMap.getStyle(new Style.OnStyleLoaded() {
                                    @Override
                                    public void onStyleLoaded(@NonNull Style style) {

// Retrieve and update the source designated for showing the directions route
                                        GeoJsonSource source = style.getSourceAs(ROUTE_SOURCE_ID);

// Create a LineString with the directions route's geometry and
// reset the GeoJSON source for the route LineLayer source
                                        if (source != null) {
                                            source.setGeoJson(LineString.fromPolyline(currentRoute.geometry(), PRECISION_6));
                                            if (callingActivity.equals("MainActivityy_driver")) {
                                                getNav(currentRoute, activity, context,driverSelectedRoute);
                                            }


                                        }
                                    }
                                });
                            }
                        }

                        @Override
                        public void onFailure(Call<DirectionsResponse> call, Throwable t) {

                        }
                    });
            LatLngBounds latLngBounds = new LatLngBounds.Builder()
                    .include(new LatLng(origin.latitude(), origin.longitude()))
                    .include(new LatLng(destination.latitude(), destination.longitude()))
                    .build();

            mapboxMap.animateCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, 20));

        } else if (origin.equals(Point.fromLngLat(Double.parseDouble(context.getString(R.string.Balkhu_long)), Double.parseDouble(context.getString(R.string.Balkhu_lat)))) && destination.equals(Point.fromLngLat(Double.parseDouble(context.getString(R.string.Sankhamul_long)), Double.parseDouble(context.getString(R.string.Sankhamul_lat))))) {
            NavigationRoute.builder(context)
                    .accessToken(context.getString(R.string.mapbox_key))
                    .origin(origin)
                    .destination(destination)
                    .addWaypoint(Point.fromLngLat(Double.parseDouble(context.getString(R.string.NayaBaneshwor_long)), Double.parseDouble(context.getString(R.string.NayaBaneshwor_lat))))
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


                            if (mapboxMap != null) {
                                mapboxMap.getStyle(new Style.OnStyleLoaded() {
                                    @Override
                                    public void onStyleLoaded(@NonNull Style style) {

// Retrieve and update the source designated for showing the directions route
                                        GeoJsonSource source = style.getSourceAs(ROUTE_SOURCE_ID);

// Create a LineString with the directions route's geometry and
// reset the GeoJSON source for the route LineLayer source
                                        if (source != null) {
                                            source.setGeoJson(LineString.fromPolyline(currentRoute.geometry(), PRECISION_6));
                                            if (callingActivity.equals("MainActivityy_driver")) {
                                                getNav(currentRoute, activity, context,driverSelectedRoute);
                                            }


                                        }
                                    }
                                });
                            }

                        }

                        @Override
                        public void onFailure(Call<DirectionsResponse> call, Throwable t) {

                        }
                    });
            LatLngBounds latLngBounds = new LatLngBounds.Builder()
                    .include(new LatLng(origin.latitude(), origin.longitude()))
                    .include(new LatLng(destination.latitude(), destination.longitude()))
                    .build();

            mapboxMap.animateCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, 20));

        } else if (origin.equals(Point.fromLngLat(Double.parseDouble(context.getString(R.string.Balkhu_long)), Double.parseDouble(context.getString(R.string.Balkhu_lat)))) && destination.equals(Point.fromLngLat(Double.parseDouble(context.getString(R.string.BhatkyaPul_long)), Double.parseDouble(context.getString(R.string.BhatkyaPul_lat))))) {
            NavigationRoute.builder(context)
                    .accessToken(context.getString(R.string.mapbox_key))
                    .origin(origin)
                    .destination(destination)
                    .addWaypoint(Point.fromLngLat(Double.parseDouble(context.getString(R.string.NayaBaneshwor_long)), Double.parseDouble(context.getString(R.string.NayaBaneshwor_lat))))
                    .addWaypoint(Point.fromLngLat(Double.parseDouble(context.getString(R.string.Shantinagar_long)), Double.parseDouble(context.getString(R.string.Shantinagar_lat))))
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


                            if (mapboxMap != null) {
                                mapboxMap.getStyle(new Style.OnStyleLoaded() {
                                    @Override
                                    public void onStyleLoaded(@NonNull Style style) {

// Retrieve and update the source designated for showing the directions route
                                        GeoJsonSource source = style.getSourceAs(ROUTE_SOURCE_ID);

// Create a LineString with the directions route's geometry and
// reset the GeoJSON source for the route LineLayer source
                                        if (source != null) {
                                            source.setGeoJson(LineString.fromPolyline(currentRoute.geometry(), PRECISION_6));
                                            if (callingActivity.equals("MainActivityy_driver")) {
                                                getNav(currentRoute, activity, context,driverSelectedRoute);
                                            }


                                        }
                                    }
                                });
                            }
                        }

                        @Override
                        public void onFailure(Call<DirectionsResponse> call, Throwable t) {

                        }
                    });
            LatLngBounds latLngBounds = new LatLngBounds.Builder()
                    .include(new LatLng(origin.latitude(), origin.longitude()))
                    .include(new LatLng(destination.latitude(), destination.longitude()))
                    .build();

            mapboxMap.animateCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, 20));

        } else if (origin.equals(Point.fromLngLat(Double.parseDouble(context.getString(R.string.Bhaktapur_long)), Double.parseDouble(context.getString(R.string.Bhaktapur_lat)))) && destination.equals(Point.fromLngLat(Double.parseDouble(context.getString(R.string.PuranoBuspark_long)), Double.parseDouble(context.getString(R.string.PuranoBuspark_lat))))) {
            NavigationRoute.builder(context)
                    .accessToken(context.getString(R.string.mapbox_key))
                    .origin(origin)
                    .destination(destination)
                    .addWaypoint(Point.fromLngLat(Double.parseDouble(context.getString(R.string.PuranoThimi_long)), Double.parseDouble(context.getString(R.string.PuranoThimi_lat))))
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


                            if (mapboxMap != null) {
                                mapboxMap.getStyle(new Style.OnStyleLoaded() {
                                    @Override
                                    public void onStyleLoaded(@NonNull Style style) {

// Retrieve and update the source designated for showing the directions route
                                        GeoJsonSource source = style.getSourceAs(ROUTE_SOURCE_ID);

// Create a LineString with the directions route's geometry and
// reset the GeoJSON source for the route LineLayer source
                                        if (source != null) {
                                            source.setGeoJson(LineString.fromPolyline(currentRoute.geometry(), PRECISION_6));
                                            if (callingActivity.equals("MainActivityy_driver")) {
                                                getNav(currentRoute, activity, context,driverSelectedRoute);
                                            }


                                        }
                                    }
                                });
                            }
                        }

                        @Override
                        public void onFailure(Call<DirectionsResponse> call, Throwable t) {

                        }
                    });
            LatLngBounds latLngBounds = new LatLngBounds.Builder()
                    .include(new LatLng(origin.latitude(), origin.longitude()))
                    .include(new LatLng(destination.latitude(), destination.longitude()))
                    .build();

            mapboxMap.animateCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, 20));

        } else {
            if (driverSelectedRoute!=null){
            String[] waypoints = driverSelectedRoute.split("-");

            NavigationRoute.builder(context)
                    .accessToken(context.getString(R.string.mapbox_key))
                    .origin(origin)
                    .destination(destination)
                    .profile(DirectionsCriteria.PROFILE_DRIVING)
                    .addWaypointNames(waypoints)
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


                            if (mapboxMap != null) {
                                mapboxMap.getStyle(new Style.OnStyleLoaded() {
                                    @Override
                                    public void onStyleLoaded(@NonNull Style style) {

// Retrieve and update the source designated for showing the directions route
                                        GeoJsonSource source = style.getSourceAs(ROUTE_SOURCE_ID);

// Create a LineString with the directions route's geometry and
// reset the GeoJSON source for the route LineLayer source
                                        if (source != null) {
                                            source.setGeoJson(LineString.fromPolyline(currentRoute.geometry(), PRECISION_6));
                                            if (callingActivity.equals("MainActivityy_driver")) {
                                                getNav(currentRoute, activity, context,driverSelectedRoute);
                                            }
                                        }
                                    }

                                });
                            }
                        }

                        @Override
                        public void onFailure(Call<DirectionsResponse> call, Throwable t) {

                        }
                    });


            LatLngBounds latLngBounds = new LatLngBounds.Builder()
                    .include(new LatLng(origin.latitude(), origin.longitude()))
                    .include(new LatLng(destination.latitude(), destination.longitude()))
                    .build();

            mapboxMap.animateCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, 20));


        }
        }

    }


    public static void getNav(DirectionsRoute currentRoute, Activity activity, Context context, String driverSelectedRoute) {
        userid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        reff = FirebaseDatabase.getInstance().getReference();
        navigation = new MapboxNavigation(context, context.getString(R.string.mapbox_key));
        if (currentRoute.routeOptions().waypointIndicesList() != null) {
            int numberOfWaypoints = currentRoute.routeOptions().waypointNamesList().size() - 1;
            map = new HashMap<>(numberOfWaypoints);
        }
        totalremainingWaypoints = currentRoute.routeOptions().waypointNamesList().size() - 1;
        executed = new boolean[totalremainingWaypoints];
        executed0 = new boolean[totalremainingWaypoints];
        executed5 = new boolean[totalremainingWaypoints];
        ;
        executed15 = new boolean[totalremainingWaypoints];
        executed30 = new boolean[totalremainingWaypoints];
        Arrays.fill(executed, false);
        Arrays.fill(executed0, false);
        Arrays.fill(executed5, false);
        Arrays.fill(executed15, false);
        Arrays.fill(executed30, false);


//Create a NavigationLauncherOptions object to package everything together
        /*NavigationLauncherOptions options = NavigationLauncherOptions.builder()
                .directionsRoute(currentRoute)
                .shouldSimulateRoute(true)
                .waynameChipEnabled(true)
                .build();

// Call this method with Context from within an Activity
        NavigationLauncher.startNavigation(activity, options);*/

        ReplayRouteLocationEngine replayRouteLocationEngine = new ReplayRouteLocationEngine();
        replayRouteLocationEngine.assign(currentRoute);
        navigation.setLocationEngine(replayRouteLocationEngine);
        navigation.startNavigation(currentRoute);


        progressChangeListener = new ProgressChangeListener() {
            @Override
            public void onProgressChange(Location location, RouteProgress routeProgress) {

                if (driverSelectedRoute != null) {
                    Log.d("PROGRESSCHANGE", "onProgressChange: ");

                    distanceRemaining = routeProgress.distanceRemaining();
                    distanceTraveled = routeProgress.distanceTraveled();
                    durationRemaining = routeProgress.durationRemaining() / 50;
                    remainingWaypoints = routeProgress.remainingWaypoints();

                    addValueEventListener();
                    removeValueEventListener();
                    /*if (!executed0) {
                        for (int i = 0; i < totalremainingWaypoints; i++) {
                            durationToWaypoint[i] = ((currentRoute.legs().get(i).duration()) / 50) + tempWaypointDuration;
                            tempWaypointDuration = durationToWaypoint[i];
                            Log.d("test", "onProgressChange: " + i + durationToWaypoint[i] + currentRoute.routeOptions().waypointNamesList().get(i + 1));
                        }
                    }
                    executed0=true;
                    for (int i = 0; i < totalremainingWaypoints; i++) {
                        int tempo = (int)(durationToWaypoint[i]-((currentRoute.duration()/50)-durationRemaining));
                        switch (tempo){
                            case 30:
                                if (!executed30) {
                                    sendNotifications(currentRoute.routeOptions().waypointNamesList().get(i + 1), 30);
                                }
                                executed30 = true;
                                break;
                            case 15:
                                if (!executed15) {
                                    sendNotifications(currentRoute.routeOptions().waypointNamesList().get(i + 1), 15);
                                }
                                executed15 = true;
                                break;
                            case 5:
                                if (!executed5) {
                                    sendNotifications(currentRoute.routeOptions().waypointNamesList().get(i + 1), 5);
                                }
                                executed5 = true;
                                break;
                            case 0:
                                if (!executed0) {
                                    sendNotifications(currentRoute.routeOptions().waypointNamesList().get(i + 1), 0);
                                }
                                executed0 = true;
                                break;
                        }

                        Log.d("test", "onProgressChange: " + i + tempo + currentRoute.routeOptions().waypointNamesList().get(i + 1));
                    }*/

                }
            }

            public void addValueEventListener() {
                reff.child("routeDetails").child(driverSelectedRoute).child(userid).addValueEventListener(valueEventListener);
                /*for (int i =0;i<totalremainingWaypoints;i++) {
                    reff.child("routeProgress").child(driverSelectedRoute).child(userid).child("DurationToWaypoint").child("durationToWaypoint"+i).addValueEventListener(sendNoti);
                }*/
            }

            public void removeValueEventListener() {
                reff.child("routeProgress").child(driverSelectedRoute).child(userid).removeEventListener(GetRoute.valueEventListener);
                //reff.removeEventListener(sendNoti);
            }

        };
        navigation.addProgressChangeListener(progressChangeListener);


        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (driverSelectedRoute != null) {
                    if (!executedonce) {
                        reff.child("routeProgress").child(driverSelectedRoute).child(userid).child("remainingDistance").setValue(distanceRemaining);
                        reff.child("routeProgress").child(driverSelectedRoute).child(userid).child("traveledDistance").setValue(distanceTraveled);
                        reff.child("routeProgress").child(driverSelectedRoute).child(userid).child("remainingDuration").setValue(durationRemaining);

                        for (int i = 0; i < totalremainingWaypoints; i++) {

                            //distanceToWaypoint[i] = currentRoute.legs().get(i).distance() + tempWaypointDistance;
                            //tempWaypointDistance = distanceToWaypoint[i];
                            durationToWaypoint[i] = ((currentRoute.legs().get(i).duration()) / 50) + tempWaypointDuration;
                            tempWaypointDuration = durationToWaypoint[i];
                            //reff.child("routeProgress").child(driverSelectedRoute).child(userid).child("distanceToWaypoint" + i).setValue(distanceToWaypoint[i] - distanceTraveled);
                            reff.child("routeProgress").child(driverSelectedRoute).child(userid).child("DurationToWaypoint").child("durationToWaypoint" + i).setValue(Math.round(durationToWaypoint[i] - ((currentRoute.duration() / 50) - durationRemaining)));

                        }
                        executedonce = true;
                    }
                    reff.child("routeProgress").child(driverSelectedRoute).child(userid).child("remainingDistance").setValue(distanceRemaining);
                    reff.child("routeProgress").child(driverSelectedRoute).child(userid).child("traveledDistance").setValue(distanceTraveled);
                    reff.child("routeProgress").child(driverSelectedRoute).child(userid).child("remainingDuration").setValue(durationRemaining);

                    for (int i = 0; i < totalremainingWaypoints; i++) {
                        //reff.child("routeProgress").child(driverSelectedRoute).child(userid).child("distanceToWaypoint" + i).setValue(distanceToWaypoint[i] - distanceTraveled);
                        reff.child("routeProgress").child(driverSelectedRoute).child(userid).child("DurationToWaypoint").child("durationToWaypoint" + i).setValue(Math.round(durationToWaypoint[i] - ((currentRoute.duration() / 50) - durationRemaining)));
                        int duration = (int) Math.round(durationToWaypoint[i] - ((currentRoute.duration() / 50) - durationRemaining));

                        if (duration == 30) {
                            if (!executed30[i]) {
                                sendNotifications(currentRoute.routeOptions().waypointNamesList().get(i + 1), 30,driverSelectedRoute);
                            }
                            executed30[i] = true;
                        } else if (duration == 15) {
                            if (!executed15[i]) {
                                sendNotifications(currentRoute.routeOptions().waypointNamesList().get(i + 1), 15,driverSelectedRoute);
                            }
                            executed15[i] = true;
                        } else if (duration == 5) {
                            if (!executed5[i]) {
                                sendNotifications(currentRoute.routeOptions().waypointNamesList().get(i + 1), 5,driverSelectedRoute);
                            }
                            executed5[i] = true;
                        } else if (duration == 0) {
                            if (!executed0[i]) {
                                sendNotifications(currentRoute.routeOptions().waypointNamesList().get(i + 1), 0,driverSelectedRoute);
                            }
                            executed0[i] = true;
                        }
                            /*switch ((int) Math.round(durationToWaypoint[i] - ((currentRoute.duration() / 50) - durationRemaining))) {
                                case 30:
                                    if (!executed30[i]) {
                                        sendNotifications(currentRoute.routeOptions().waypointNamesList().get(i + 1), 30);
                                    }
                                    executed30[i] = true;
                                    break;
                                case 15:
                                    if (!executed15[i]) {
                                        sendNotifications(currentRoute.routeOptions().waypointNamesList().get(i + 1), 15);
                                    }
                                    executed15[i] = true;
                                    break;
                                case 5:
                                    if (!executed5[i]) {
                                        sendNotifications(currentRoute.routeOptions().waypointNamesList().get(i + 1), 5);
                                    }
                                    executed5[i] = true;
                                    break;
                                case 0:
                                    if (!executed0[i]) {
                                        sendNotifications(currentRoute.routeOptions().waypointNamesList().get(i + 1), 0);
                                    }
                                    executed0[i] = true;
                                    break;

                            }*/

                    }
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        /*sendNoti = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d("NOTI", "onDataChange: "+snapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };*/

        navigation.addOffRouteListener(new OffRouteListener() {
            @Override
            public void userOffRoute(Location location) {
                Toast.makeText(activity, "The bus is off route!", Toast.LENGTH_SHORT).show();
            }
        });


    }

    public static void sendNotifications(String waypoint, int durationToWaypoint,String driverSelectedRoute) {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                int SDK_INT = android.os.Build.VERSION.SDK_INT;
                if (SDK_INT > 8) {
                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                            .permitAll().build();
                    StrictMode.setThreadPolicy(policy);

                    try {
                        String jsonResponse;

                        URL url = new URL("https://onesignal.com/api/v1/notifications");
                        HttpURLConnection con = (HttpURLConnection) url.openConnection();
                        con.setUseCaches(false);
                        con.setDoOutput(true);
                        con.setDoInput(true);

                        con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                        con.setRequestProperty("Authorization", "Basic ZjgwYjVkYTgtNzUyYy00NTc5LTlhNWItYzRmOWY4ODE5MjJh");
                        con.setRequestMethod("POST");

                        String strJsonBody;
                        if (durationToWaypoint == 0) {
                            strJsonBody = "{"
                                    + "\"app_id\": \"57089cf3-f615-4c03-8f7f-a8131359a24c\","

                                    + "\"filters\": [{\"field\": \"tag\", \"key\": \"Route\", \"relation\": \"=\", \"value\": \"" + driverSelectedRoute + "\"}],"

                                    + "\"data\": {\"foo\": \"bar\"},"
                                    //+ "\"contents\": [{\"en\": \"Arrived at \"" + waypoint + "\" ! \"}]"
                                    + "\"contents\": {\"en\": \"Arrived at " + waypoint + "!\"}"
                                    + "}";
                        } else {
                            strJsonBody = "{"
                                    + "\"app_id\": \"57089cf3-f615-4c03-8f7f-a8131359a24c\","

                                    + "\"filters\": [{\"field\": \"tag\", \"key\": \"Route\", \"relation\": \"=\", \"value\": \"" + driverSelectedRoute + "\"}],"

                                    + "\"data\": {\"foo\": \"bar\"},"
                                    //+ "\"contents\": {\"en\": \"Arriving at\"" + waypoint + "\" at\"" +durationToWaypoint+ "\" min ! \"}"
                                    + "\"contents\": {\"en\": \"Arriving at " + waypoint + " in " + durationToWaypoint + " min!\"}"
                                    + "}";
                        }


                        System.out.println("strJsonBody:\n" + strJsonBody);

                        byte[] sendBytes = strJsonBody.getBytes("UTF-8");
                        con.setFixedLengthStreamingMode(sendBytes.length);

                        OutputStream outputStream = con.getOutputStream();
                        outputStream.write(sendBytes);

                        int httpResponse = con.getResponseCode();
                        System.out.println("httpResponse: " + httpResponse);

                        if (httpResponse >= HttpURLConnection.HTTP_OK
                                && httpResponse < HttpURLConnection.HTTP_BAD_REQUEST) {
                            Scanner scanner = new Scanner(con.getInputStream(), "UTF-8");
                            jsonResponse = scanner.useDelimiter("\\A").hasNext() ? scanner.next() : "";
                            scanner.close();
                        } else {
                            Scanner scanner = new Scanner(con.getErrorStream(), "UTF-8");
                            jsonResponse = scanner.useDelimiter("\\A").hasNext() ? scanner.next() : "";
                            scanner.close();
                        }
                        System.out.println("jsonResponse:\n" + jsonResponse);

                    } catch (Throwable t) {
                        t.printStackTrace();
                    }
                }
            }
        });
    }


}




