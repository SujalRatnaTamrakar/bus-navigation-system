package com.project.of.busnavigationsystem.Popups;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.mapbox.mapboxsdk.plugins.markerview.MarkerViewManager;
import com.project.of.busnavigationsystem.NavBar.Routes.BusDetailActivity;
import com.project.of.busnavigationsystem.NavBar.Routes.BusDetailFragment;
import com.project.of.busnavigationsystem.R;
import com.project.of.busnavigationsystem.dummy.DummyContent;
import com.google.android.material.chip.Chip;

public class route_detail_popup extends Activity {
    TextView dist, dur;
    DummyContent.DummyItem item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_detail_popup);


        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout(((int) (width * .98)), ((int) (height * .25)));

        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.gravity = Gravity.BOTTOM;
        params.x = 0;
        params.y = -20;

        getWindow().setAttributes(params);

        dist = (TextView) findViewById(R.id.textView_distance);
        dur = (TextView) findViewById(R.id.textView_duration);
        Intent receive = getIntent();
        dist.setText(receive.getStringExtra("distance"));
        dur.setText(receive.getStringExtra("duration"));

        Chip chip = (Chip) findViewById(R.id.chip_moreinfo);

        chip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent receive = getIntent();
                if (receive.getStringExtra("route").equals("Route 1")) {
                    item = new DummyContent.DummyItem("1", "Attarkhel-Purano Buspark", "Bus Stops : \n 1. Attarkhel \n 2. Purano Buspark \nDistance : 9.6 km\nDuration :31 min");
                    Intent intent4 = new Intent(route_detail_popup.this, BusDetailActivity.class);
                    intent4.putExtra(BusDetailFragment.ARG_ITEM_ID, item.id);

                    startActivity(intent4);
                    finish();
                    

                } else if (receive.getStringExtra("route").equals("Route 2")) {
                    item = new DummyContent.DummyItem("2", "Bagbazaar-Kamalbinayak", "Bus Stops : \n 1. Bagbazaar \n 2. Kamalbinayak \nDistance : 15.7 km\nDuration : 39 min");
                    Intent intent4 = new Intent(route_detail_popup.this, BusDetailActivity.class);
                    intent4.putExtra(BusDetailFragment.ARG_ITEM_ID, item.id);

                    startActivity(intent4);
                    finish();


                } else if (receive.getStringExtra("route").equals("Route 3")) {
                    item = new DummyContent.DummyItem("3", "Bagbazaar-Ratnapark", "Bus Stops : \n 1. Bagbazaar \n 2. Ratnapark \nDistance : 0.2 km\nDuration : 1 min");
                    Intent intent4 = new Intent(route_detail_popup.this, BusDetailActivity.class);
                    intent4.putExtra(BusDetailFragment.ARG_ITEM_ID, item.id);

                    startActivity(intent4);
                    finish();

                } else if (receive.getStringExtra("route").equals("Route 4")) {
                    item = new DummyContent.DummyItem("4", "Balaju-Ratnapark", "Bus Stops : \n 1. Balaju \n 2. Ratnapark \nDistance : 3.3 km\nDuration : 9 min");
                    Intent intent4 = new Intent(route_detail_popup.this, BusDetailActivity.class);
                    intent4.putExtra(BusDetailFragment.ARG_ITEM_ID, item.id);

                    startActivity(intent4);
                    finish();

                } else if (receive.getStringExtra("route").equals("Route 5")) {
                    item = new DummyContent.DummyItem("5", "Balkhu-NayaBaneshwor-Sankhamul", "Bus Stops : \n 1. Balkhu \n 2. Naya Baneshwor \n 3. Sankhamul \nDistance : 11 km\nDuration : 23 min");
                    Intent intent4 = new Intent(route_detail_popup.this, BusDetailActivity.class);
                    intent4.putExtra(BusDetailFragment.ARG_ITEM_ID, item.id);

                    startActivity(intent4);
                    finish();

                } else if (receive.getStringExtra("route").equals("Route 6")) {
                    item = new DummyContent.DummyItem("6", "Balkhu-NayaBaneshwor-Shantinagar-Bhatkya Pul", "Bus Stops : \n 1. Balkhu \n 2. Naya Baneshwor \n 3. Shantinagar \n 4. Bhatkya Pul \nDistance : 17.2 km\nDuration : 41 min");
                    Intent intent4 = new Intent(route_detail_popup.this, BusDetailActivity.class);
                    intent4.putExtra(BusDetailFragment.ARG_ITEM_ID, item.id);

                    startActivity(intent4);
                    finish();

                } else if (receive.getStringExtra("route").equals("Route 7")) {
                    item = new DummyContent.DummyItem("7", "Balkumari-Gopi Krishna", "Bus Stops : \n 1. Balkumari \n 2. Gopi Krishna \nDistance : 6.7 km\nDuration : 16 min");
                    Intent intent4 = new Intent(route_detail_popup.this, BusDetailActivity.class);
                    intent4.putExtra(BusDetailFragment.ARG_ITEM_ID, item.id);

                    startActivity(intent4);
                    finish();

                } else if (receive.getStringExtra("route").equals("Route 8")) {
                    item = new DummyContent.DummyItem("8", "Bhaktapur-Purano Thimi-Purano Bus Park", "Bus Stops : \n 1. Bhaktapur \n 2. Purano Thimi \n 3. Purano Bus Park \nDistance : \nDuration :");
                    Intent intent4 = new Intent(route_detail_popup.this, BusDetailActivity.class);
                    intent4.putExtra(BusDetailFragment.ARG_ITEM_ID, item.id);

                    startActivity(intent4);
                    finish();

                } else if (receive.getStringExtra("route").equals("Route 9")) {
                    item = new DummyContent.DummyItem("9", "Boudha-Dillibazaar", "Bus Stops : \n 1. Boudha \n 2. Dillibazaar \nDistance : 4.2 km\nDuration : 13 min");
                    Intent intent4 = new Intent(route_detail_popup.this, BusDetailActivity.class);
                    intent4.putExtra(BusDetailFragment.ARG_ITEM_ID, item.id);

                    startActivity(intent4);
                    finish();

                } else if (receive.getStringExtra("route").equals("Route 10")) {
                    item = new DummyContent.DummyItem("10", "Budhanilkantha School-Ratna Park", "Bus Stops : \n 1. Budhanilkantha School \n 2. Ratna Park \nDistance : 10.3 km\nDuration : 31 min");
                    Intent intent4 = new Intent(route_detail_popup.this, BusDetailActivity.class);
                    intent4.putExtra(BusDetailFragment.ARG_ITEM_ID, item.id);

                    startActivity(intent4);
                    finish();

                } else if (receive.getStringExtra("route").equals("Route 11")) {
                    item = new DummyContent.DummyItem("11", "Bungamati-Bhaisipati-Charghare", "Bus Stops : \n 1. Bungamati \n 2. Bhaisipati \n 3. Charghare \nDistance : 10.7 km\nDuration : 31 min");
                    Intent intent4 = new Intent(route_detail_popup.this, BusDetailActivity.class);
                    intent4.putExtra(BusDetailFragment.ARG_ITEM_ID, item.id);

                    startActivity(intent4);
                    finish();

                } else if (receive.getStringExtra("route").equals("Route 12")) {
                    item = new DummyContent.DummyItem("12", "Changu-Ratnapark", "Bus Stops : \n 1. Changu \n 2. Ratnapark \nDistance : 15.4 km\nDuration : 46 min");
                    Intent intent4 = new Intent(route_detail_popup.this, BusDetailActivity.class);
                    intent4.putExtra(BusDetailFragment.ARG_ITEM_ID, item.id);

                    startActivity(intent4);
                    finish();

                } else if (receive.getStringExtra("route").equals("Route 13")) {
                    item = new DummyContent.DummyItem("13", "Lagankhel-Jawalakhel-Kupondol-Tripureshor", "Bus Stops : \n 1. Lagankhel \n 2. Jawalakhel \n 3. Kupondol \n 4. Tripureshor \nTotal Distance : 4 km \nDuration : 9 min");
                    Intent intent4 = new Intent(route_detail_popup.this, BusDetailActivity.class);
                    intent4.putExtra(BusDetailFragment.ARG_ITEM_ID, item.id);

                    startActivity(intent4);
                    finish();

                }else if (receive.getStringExtra("route").equals("Route 14")) {
                    item = new DummyContent.DummyItem("14", "Lagankhel-Kupondol-Thapathali", "Bus Stops : \n 1. Lagankhel \n 2. Kupondol \n 3. Thapathali \nDistance : 4.6 km \nDuration : 14 min");
                    Intent intent4 = new Intent(route_detail_popup.this, BusDetailActivity.class);
                    intent4.putExtra(BusDetailFragment.ARG_ITEM_ID, item.id);

                    startActivity(intent4);
                    finish();

                }  else if (receive.getStringExtra("route").equals("Route 15")) {
                    item = new DummyContent.DummyItem("15", "Putalisadak-Pulchowk", "Bus Stops : \n 1. Putalisadak \n 2. Pulchowk \nDistance : 4.8 km\nDuration : 18 min");
                    ;
                    Intent intent4 = new Intent(route_detail_popup.this, BusDetailActivity.class);
                    intent4.putExtra(BusDetailFragment.ARG_ITEM_ID, item.id);

                    startActivity(intent4);
                    finish();

                } else if (receive.getStringExtra("route").equals("Route 16")) {
                    item = new DummyContent.DummyItem("16", "Ratnapark-Samakhusi Chowk", "Bus Stops : \n 1. Ratnapark \n 2. Samakhusi Chowk \nDistance : 3.9 km\nDuration : 11 min");

                    Intent intent4 = new Intent(route_detail_popup.this, BusDetailActivity.class);
                    intent4.putExtra(BusDetailFragment.ARG_ITEM_ID, item.id);

                    startActivity(intent4);
                    finish();

                }

            }
        });


    }
}
