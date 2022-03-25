package com.project.of.busnavigationsystem.MainActivities.DriverRouteOperations;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.mapbox.api.directions.v5.models.DirectionsRoute;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapFragment;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.plugins.annotation.Symbol;
import com.mapbox.mapboxsdk.plugins.annotation.SymbolManager;
import com.mapbox.mapboxsdk.plugins.annotation.SymbolOptions;
import com.project.of.busnavigationsystem.R;

import java.util.List;

public class GetAnnotations {
    public void getAnnotations(MapboxMap mapboxMap, Context context, DirectionsRoute currentRoute, MapView mapView) {
        //trying to add annotations
        if (currentRoute != null) {
            List<Point> waypoints = currentRoute.routeOptions().coordinates();
            List<String> waypointNames = currentRoute.routeOptions().waypointNamesList();
            View[] markerAnnotation = new View[waypoints.size()];
            TextView[] busStopName = new TextView[waypoints.size()];
            Typeface typeface = Typeface.createFromAsset(context.getAssets(), "fonts/abel.ttf");
            Bitmap[] bitmaps = new Bitmap[waypoints.size()];
            mapboxMap.getStyle(new Style.OnStyleLoaded() {
                @Override
                public void onStyleLoaded(@NonNull Style style) {
                    for (int i = 0; i < waypoints.size(); i++) {
                        markerAnnotation[i] = LayoutInflater.from(context).inflate(R.layout.marker_annotation, null);
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
                                .withIconSize(0.7f)
                                .withIconOffset(new Float[]{0f, -40f}));
                    }
                }
            });
        }

        //end of trying
    }
}
