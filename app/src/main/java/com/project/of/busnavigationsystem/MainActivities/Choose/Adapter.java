package com.project.of.busnavigationsystem.MainActivities.Choose;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.viewpager.widget.PagerAdapter;

import com.google.android.material.card.MaterialCardView;
import com.project.of.busnavigationsystem.Login.LoginActivity;
import com.project.of.busnavigationsystem.R;
import com.project.of.busnavigationsystem.Signup.SignUpBus;
import com.project.of.busnavigationsystem.Signup.SignUpUser;


import java.util.List;

public class Adapter extends PagerAdapter {

    private List<Model> models;
    private LayoutInflater layoutInflater;
    private Context context;


    public Adapter(List<Model> models, Context context) {
        this.models = models;
        this.context = context;
    }

    @Override
    public int getCount() {
        return models.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.item, container, false);

        ImageView imageView;
        TextView  desc;
        Button signUp;


        imageView = view.findViewById(R.id.image);
        desc = view.findViewById(R.id.desc);
        signUp=view.findViewById(R.id.btnSignUp);

        imageView.setImageResource(models.get(position).getImage());
        desc.setText(models.get(position).getDesc());
        signUp.setText(desc.getText());

        switch (position){
            case 0:
                signUp.setBackground(view.getResources().getDrawable(R.drawable.sign_up_button2));
                break;
            case 1:
                signUp.setBackground(view.getResources().getDrawable(R.drawable.sign_up_button1));
                break;
        }



        /*view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, LoginActivity.class);
                context.startActivity(intent);
                // finish();
            }
        });*/



        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Position", "onClick: "+position);
                switch (position){
                    case 0:
                        Intent intent_signUpUser = new Intent(context, SignUpUser.class);
                        context.startActivity(intent_signUpUser);
                        break;
                    case 1:
                        Intent intent_signUpDriver = new Intent(context, SignUpBus.class);
                        context.startActivity(intent_signUpDriver);
                        break;
                }
            }
        });

        container.addView(view, 0);
        return view;    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View)object);
    }


}
