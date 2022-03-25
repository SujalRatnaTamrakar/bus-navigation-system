package com.project.of.busnavigationsystem.MainActivities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.animation.ArgbEvaluator;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;


import com.project.of.busnavigationsystem.MainActivities.Choose.Adapter;
import com.project.of.busnavigationsystem.MainActivities.Choose.Model;
import com.project.of.busnavigationsystem.R;
import com.project.of.busnavigationsystem.Signup.SignUpBus;
import com.project.of.busnavigationsystem.Signup.SignUpUser;

import java.util.ArrayList;
import java.util.List;


public class ChooseActivity extends AppCompatActivity {

    ViewPager viewPager;
    Adapter adapter;
    List<Model> models;
    Integer[] colors = null;
    ArgbEvaluator argbEvaluator = new ArgbEvaluator();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_choose);

        models = new ArrayList<>();
        models.add(new Model(R.drawable.signupuserimg, "Sign Up as a User"));
        models.add(new Model(R.drawable.signupdriver, "Sign Up as a Driver"));

        adapter = new Adapter(models, this);

        viewPager = findViewById(R.id.viewPager);
        viewPager.setAdapter(adapter);
        viewPager.setPadding(130, 0, 130, 0);

        Integer[] colors_temp = {
                getResources().getColor(R.color.colorPrimary),
                getResources().getColor(R.color.colorPrimaryDark)
        };

        colors = colors_temp;



        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                if (position < (adapter.getCount() -1) && position < (colors.length - 1)) {
                    viewPager.setBackgroundColor(

                            (Integer) argbEvaluator.evaluate(
                                    positionOffset,
                                    colors[position],
                                    colors[position + 1]
                            )
                    );
                }

                else {
                    viewPager.setBackgroundColor(colors[colors.length - 1]);
                }
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });



















/*//        OldCode
        ImageButton forBus = (ImageButton) findViewById(R.id.botton);
        ImageButton forUser = (ImageButton) findViewById(R.id.btn2);

        forBus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i= new Intent(ChooseActivity.this, SignUpBus.class);
                startActivity(i);
            }
        });
        forUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent j=new Intent(ChooseActivity.this, SignUpUser.class);
                startActivity(j);
            }
        });*/
    }
}
