package com.example.chirag.navigationdrawer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class ChooseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose);
        Button forBus = (Button) findViewById(R.id.botton);
        Button forUser = (Button) findViewById(R.id.btn2);

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
        });
    }
}
