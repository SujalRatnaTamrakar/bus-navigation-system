package com.project.of.busnavigationsystem.NavBar.Routes;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.onesignal.OneSignal;
import com.onesignal.OneSignalAPIClient;
import com.project.of.busnavigationsystem.MainActivities.DriverRouteOperations.GetRoute;
import com.project.of.busnavigationsystem.MainActivities.MainActivityy;
import com.project.of.busnavigationsystem.R;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.ActionBar;
import androidx.core.app.NavUtils;
import androidx.core.widget.NestedScrollView;

import android.view.MenuItem;

/**
 * An activity representing a single Bus detail screen. This
 * activity is only used on narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link BusListActivity}.
 */


public class BusDetailActivity extends AppCompatActivity {

    public static String token;
    String clickedRoute = MainActivityy.clickedroute.getValue();
    NestedScrollView scrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);
        Mapbox.getInstance(this, getString(R.string.mapbox_key));
        WorkAroundMapFragment mapFragment;
        String[] route = new String[]{"Lagankhel-Jawalakhel-Kupondol-Tripureshor",
                "Lagankhel-Kupondol-Thapathali",
                "Bungamati-Bhaisipati-Charghare",
                "Attarkhel-Purano Buspark",
                "Bagbazaar-Ratnapark",
                "Bagbazaar-Kamalbinayak",
                "Balaju-Ratnapark",
                "Balkhu-NayaBaneshwor-Sankhamul",
                "Balkhu-NayaBaneshwor-Shantinagar-Bhatkya Pul",
                "Balkumari-Gopi Krishna",
                "Bhaktapur-Purano Thimi-Purano Buspark",
                "Budhanilkantha School-Ratnapark",
                //"Chakrapath Parikrama",
                "Changu-Ratnapark",
                "Ratnapark-Samakhusi Chowk",
                "Boudha-Dillibazaar",
                "Putalisadak-Pulchowk"};
        java.util.Arrays.sort(route);

        /*
        if (savedInstanceState == null) {

// Create fragment
            final FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

// Build mapboxMap
            MapboxMapOptions options = MapboxMapOptions.createFromAttributes(this, null);
            options.camera(new CameraPosition.Builder()
                    .target(new LatLng(-52.6885, -70.1395))
                    .zoom(9)
                    .build());

// Create map fragment
            mapFragment = (WorkAroundMapFragment) WorkAroundMapFragment.newInstance(options);


// Add map fragment to parent container
            transaction.add(R.id.container, mapFragment, "com.mapbox.map");
            transaction.commit();
        } else {
            mapFragment = (WorkAroundMapFragment) getSupportFragmentManager().findFragmentByTag("com.mapbox.map");
        }*/
        mapFragment = (WorkAroundMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapFragment);
        if (mapFragment != null) {
            mapFragment.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(@NonNull MapboxMap mapboxMap) {
                    //Removing mapbox attribution
                    mapboxMap.getUiSettings().setAttributionEnabled(false);
                    mapboxMap.getUiSettings().setLogoEnabled(false);

                    scrollView = findViewById(R.id.bus_detail_container);
                    ((WorkAroundMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapFragment)).setListener(new WorkAroundMapFragment.OnTouchListener() {
                        @Override
                        public void onTouch() {
                            scrollView.requestDisallowInterceptTouchEvent(true);
                        }
                    });

                    mapboxMap.setStyle(Style.MAPBOX_STREETS, new Style.OnStyleLoaded() {
                        @RequiresApi(api = Build.VERSION_CODES.Q)
                        @Override
                        public void onStyleLoaded(@NonNull Style style) {
                            if (getIntent().getStringExtra(BusDetailFragment.ARG_ITEM_ID) != null) {
                                int id = Integer.parseInt(getIntent().getStringExtra(BusDetailFragment.ARG_ITEM_ID));
                                Log.d("Try", "onStyleLoaded: " + route[id - 1] + id);
                                switch (route[id - 1]) {
                                    case "Attarkhel-Purano Buspark":
                                        GetRoute.getRoute(mapboxMap,
                                                Point.fromLngLat(Double.parseDouble(getString(R.string.Attarkhel_long)), Double.parseDouble(getString(R.string.Attarkhel_lat))),
                                                Point.fromLngLat(Double.parseDouble(getString(R.string.PuranoBuspark_long)), Double.parseDouble(getString(R.string.PuranoBuspark_lat))), getApplicationContext(), BusDetailActivity.this, "BusDetailActivity",route[id-1]);
                                        break;
                                    case "Lagankhel-Jawalakhel-Kupondol-Tripureshor":
                                        GetRoute.getRoute(mapboxMap,
                                                Point.fromLngLat(Double.parseDouble(getString(R.string.Lagankhel_long)), Double.parseDouble(getString(R.string.Lagankhel_lat))),
                                                Point.fromLngLat(Double.parseDouble(getString(R.string.Tripureshor_long)), Double.parseDouble(getString(R.string.Tripureshor_lat))), getApplicationContext(), BusDetailActivity.this, "BusDetailActivity",route[id-1]);
                                        break;
                                    case "Lagankhel-Kupondol-Thapathali":
                                        GetRoute.getRoute(mapboxMap,
                                                Point.fromLngLat(Double.parseDouble(getString(R.string.Lagankhel_long)), Double.parseDouble(getString(R.string.Lagankhel_lat))),
                                                Point.fromLngLat(Double.parseDouble(getString(R.string.Thapathali_long)), Double.parseDouble(getString(R.string.Thapathali_lat))), getApplicationContext(), BusDetailActivity.this, "BusDetailActivity",route[id-1]);
                                        break;
                                    case "Bungamati-Bhaisipati-Charghare":
                                        GetRoute.getRoute(mapboxMap,
                                                Point.fromLngLat(Double.parseDouble(getString(R.string.Bungamati_long)), Double.parseDouble(getString(R.string.Bungamati_lat))),
                                                Point.fromLngLat(Double.parseDouble(getString(R.string.Charghare_long)), Double.parseDouble(getString(R.string.Charghare_lat))), getApplicationContext(), BusDetailActivity.this, "BusDetailActivity",route[id-1]);
                                        break;
                                    case "Bagbazaar-Ratnapark":
                                        GetRoute.getRoute(mapboxMap,
                                                Point.fromLngLat(Double.parseDouble(getString(R.string.Bagbazaar_long)), Double.parseDouble(getString(R.string.Bagbazaar_lat))),
                                                Point.fromLngLat(Double.parseDouble(getString(R.string.Ratnapark_long)), Double.parseDouble(getString(R.string.Ratnapark_lat))), getApplicationContext(), BusDetailActivity.this, "BusDetailActivity",route[id-1]);
                                        break;
                                    case "Bagbazaar-Kamalbinayak":
                                        GetRoute.getRoute(mapboxMap,
                                                Point.fromLngLat(Double.parseDouble(getString(R.string.Bagbazaar_long)), Double.parseDouble(getString(R.string.Bagbazaar_lat))),
                                                Point.fromLngLat(Double.parseDouble(getString(R.string.Kamalbinayak_long)), Double.parseDouble(getString(R.string.Kamalbinayak_lat))), getApplicationContext(), BusDetailActivity.this, "BusDetailActivity",route[id-1]);
                                        break;
                                    case "Balaju-Ratnapark":
                                        GetRoute.getRoute(mapboxMap,
                                                Point.fromLngLat(Double.parseDouble(getString(R.string.Balaju_long)), Double.parseDouble(getString(R.string.Balaju_lat))),
                                                Point.fromLngLat(Double.parseDouble(getString(R.string.Ratnapark_long)), Double.parseDouble(getString(R.string.Ratnapark_lat))), getApplicationContext(), BusDetailActivity.this, "BusDetailActivity",route[id-1]);
                                        break;
                                    case "Balkhu-NayaBaneshwor-Sankhamul":
                                        GetRoute.getRoute(mapboxMap,
                                                Point.fromLngLat(Double.parseDouble(getString(R.string.Balkhu_long)), Double.parseDouble(getString(R.string.Balkhu_lat))),
                                                Point.fromLngLat(Double.parseDouble(getString(R.string.Sankhamul_long)), Double.parseDouble(getString(R.string.Sankhamul_lat))), getApplicationContext(), BusDetailActivity.this, "BusDetailActivity",route[id-1]);
                                        break;
                                    case "Balkhu-NayaBaneshwor-Shantinagar-Bhatkya Pul":
                                        GetRoute.getRoute(mapboxMap,
                                                Point.fromLngLat(Double.parseDouble(getString(R.string.Balkhu_long)), Double.parseDouble(getString(R.string.Balkhu_lat))),
                                                Point.fromLngLat(Double.parseDouble(getString(R.string.BhatkyaPul_long)), Double.parseDouble(getString(R.string.BhatkyaPul_lat))), getApplicationContext(), BusDetailActivity.this, "BusDetailActivity",route[id-1]);
                                        break;
                                    case "Balkumari-Gopi Krishna":
                                        GetRoute.getRoute(mapboxMap,
                                                Point.fromLngLat(Double.parseDouble(getString(R.string.Balkumari_long)), Double.parseDouble(getString(R.string.Balkumari_lat))),
                                                Point.fromLngLat(Double.parseDouble(getString(R.string.GopiKrishna_long)), Double.parseDouble(getString(R.string.GopiKrishna_lat))), getApplicationContext(), BusDetailActivity.this, "BusDetailActivity",route[id-1]);
                                        break;
                                    case "Bhaktapur-Purano Thimi-Purano Bus Park":
                                        GetRoute.getRoute(mapboxMap,
                                                Point.fromLngLat(Double.parseDouble(getString(R.string.Bhaktapur_long)), Double.parseDouble(getString(R.string.Bhaktapur_lat))),
                                                Point.fromLngLat(Double.parseDouble(getString(R.string.PuranoBuspark_long)), Double.parseDouble(getString(R.string.PuranoBuspark_lat))), getApplicationContext(), BusDetailActivity.this, "BusDetailActivity",route[id-1]);
                                        break;
                                    case "Budhanilkantha School-Ratnapark":
                                        GetRoute.getRoute(mapboxMap,
                                                Point.fromLngLat(Double.parseDouble(getString(R.string.BudhanilkanthaSchool_long)), Double.parseDouble(getString(R.string.BudhanilkanthaSchool_lat))),
                                                Point.fromLngLat(Double.parseDouble(getString(R.string.Ratnapark_long)), Double.parseDouble(getString(R.string.Ratnapark_lat))), getApplicationContext(), BusDetailActivity.this, "BusDetailActivity",route[id-1]);
                                        break;
                                    case "Changu-Ratnapark":
                                        GetRoute.getRoute(mapboxMap,
                                                Point.fromLngLat(Double.parseDouble(getString(R.string.Changu_long)), Double.parseDouble(getString(R.string.Changu_lat))),
                                                Point.fromLngLat(Double.parseDouble(getString(R.string.Ratnapark_long)), Double.parseDouble(getString(R.string.Ratnapark_lat))), getApplicationContext(), BusDetailActivity.this, "BusDetailActivity",route[id-1]);
                                        break;
                                    case "Ratnapark-Samakhusi Chowk":
                                        GetRoute.getRoute(mapboxMap,
                                                Point.fromLngLat(Double.parseDouble(getString(R.string.Ratnapark_long)), Double.parseDouble(getString(R.string.Ratnapark_lat))),
                                                Point.fromLngLat(Double.parseDouble(getString(R.string.SamakhusiChowk_long)), Double.parseDouble(getString(R.string.SamakhusiChowk_lat))), getApplicationContext(), BusDetailActivity.this, "BusDetailActivity",route[id-1]);
                                        break;
                                    case "Boudha-Dillibazaar":
                                        GetRoute.getRoute(mapboxMap,
                                                Point.fromLngLat(Double.parseDouble(getString(R.string.Boudha_long)), Double.parseDouble(getString(R.string.Boudha_lat))),
                                                Point.fromLngLat(Double.parseDouble(getString(R.string.Dillibazaar_long)), Double.parseDouble(getString(R.string.Dillibazaar_lat))), getApplicationContext(), BusDetailActivity.this, "BusDetailActivity",route[id-1]);
                                        break;
                                    case "Putalisadak-Pulchowk":
                                        GetRoute.getRoute(mapboxMap,
                                                Point.fromLngLat(Double.parseDouble(getString(R.string.Putalisadak_long)), Double.parseDouble(getString(R.string.Putalisadak_lat))),
                                                Point.fromLngLat(Double.parseDouble(getString(R.string.Pulchowk_long)), Double.parseDouble(getString(R.string.Pulchowk_lat))), getApplicationContext(), BusDetailActivity.this, "BusDetailActivity",route[id-1]);
                                        break;
                                }

                            }
                        }
                    });
                }
            });
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        FloatingActionButton fab2 = (FloatingActionButton) findViewById(R.id.fab2);
        fab2.setEnabled(false);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Notifications turned ON!", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                OneSignal.sendTag("Route", clickedRoute);
                fab.setVisibility(View.INVISIBLE);
                fab.setEnabled(false);
                fab2.setVisibility(View.VISIBLE);
                fab2.setEnabled(true);

            }
        });

        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(v, "Notifications turned OFF!", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                OneSignal.deleteTag("Route");
                fab.setVisibility(View.VISIBLE);
                fab.setEnabled(true);
                fab2.setVisibility(View.INVISIBLE);
                fab2.setEnabled(false);
            }
        });

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        // savedInstanceState is non-null when there is fragment state
        // saved from previous configurations of this activity
        // (e.g. when rotating the screen from portrait to landscape).
        // In this case, the fragment will automatically be re-added
        // to its container so we don't need to manually add it.
        // For more information, see the Fragments API guide at:
        //
        // http://developer.android.com/guide/components/fragments.html
        //
        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            Bundle arguments = new Bundle();
            arguments.putString(BusDetailFragment.ARG_ITEM_ID,
                    getIntent().getStringExtra(BusDetailFragment.ARG_ITEM_ID));
            BusDetailFragment fragment = new BusDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.bus_detail_container_linearLayout, fragment)
                    .commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // This ID represents the Home or Up button. In the case of this
            // activity, the Up button is shown. Use NavUtils to allow users
            // to navigate up one level in the application structure. For
            // more details, see the Navigation pattern on Android Design:
            //
            // http://developer.android.com/design/patterns/navigation.html#up-vs-back
            //
            NavUtils.navigateUpTo(this, new Intent(this, BusListActivity.class));

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
