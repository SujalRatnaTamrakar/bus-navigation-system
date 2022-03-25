package com.project.of.busnavigationsystem.Popups;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.project.of.busnavigationsystem.Adapter.ListViewAdapter;
import com.project.of.busnavigationsystem.MainActivities.MainActivityy;
import com.project.of.busnavigationsystem.R;

import java.util.ArrayList;


public class search_popup extends Activity implements SearchView.OnQueryTextListener {

    // Declare Variables
    ListView list;
    ListViewAdapter adapter;
    SearchView editsearch;
    String[] RouteList;
    ArrayList<RouteName> arraylist = new ArrayList<RouteName>();
    String busStop;
    private static final int RESPOND_CODE_SEARCH_POPUP = 2;
    TextView suggest, closestStop;
    Animation bottomAnim;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_popup);

        bottomAnim = AnimationUtils.loadAnimation(this, R.anim.bottom_animation);
        busStop = getIntent().getStringExtra("busstop");
        suggest = (TextView) findViewById(R.id.suggestedRoute);
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/abel.ttf");
        closestStop = (TextView) findViewById(R.id.closestBusStop);
        closestStop.setText("Closest Bus Stop: " + busStop);
        suggest.setText("Following route(s) are suggested:");
        closestStop.setTypeface(typeface);
        suggest.setTypeface(typeface);

        // Generate sample data

        RouteList = new String[]{
                "Lagankhel-Jawalakhel-Kupondol-Tripureshor",
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
                "Putalisadak-Pulchowk"

        };
        java.util.Arrays.sort(RouteList);

        // Locate the ListView in listview_main.xml
        list = (ListView) findViewById(R.id.listview);

        for (int i = 0; i < RouteList.length; i++) {
            RouteName placeNames = new RouteName(RouteList[i]);
            // Binds all strings into an array
            arraylist.add(placeNames);
        }

        // Pass results to ListViewAdapter Class
        adapter = new ListViewAdapter(this, arraylist);

        // Binds the Adapter to the ListView
        list.setAdapter(adapter);

        // Locate the EditText in activity_search_popup.xml
        editsearch = (SearchView) findViewById(R.id.search);


        editsearch.setOnQueryTextListener(this);
        editsearch.setQuery(busStop, true);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent output = new Intent();
                output.putExtra("Route", RouteList[(int) (id)]);

                Log.d("LiST", "onItemClick: " + RouteList[(int) (id)]);

                setResult(RESPOND_CODE_SEARCH_POPUP, output);
                finish();


                //Toast.makeText(search_popup.this,RouteList[position],Toast.LENGTH_SHORT).show();
            }
        });

        //popup_window
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout(((int) (width * .8)), ((int) (height * .5)));

        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.gravity = Gravity.CENTER;
        params.x = 0;
        params.y = -20;
        getWindow().setWindowAnimations(R.style.Animation);


        getWindow().setAttributes(params);
    }


    //onQueryTextListener methods
    @Override
    public boolean onQueryTextSubmit(String query) {

        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        String text = newText;
        adapter.filter(text);
        return false;
    }
}
