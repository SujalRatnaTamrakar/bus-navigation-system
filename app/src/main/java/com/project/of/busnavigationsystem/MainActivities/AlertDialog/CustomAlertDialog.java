package com.project.of.busnavigationsystem.MainActivities.AlertDialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.project.of.busnavigationsystem.R;

public class CustomAlertDialog extends AlertDialog implements View.OnClickListener {


    public CustomAlertDialog(@NonNull Context context) {
        super(context);
    }

    public CustomAlertDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    public Activity activity;
    public Dialog dialog;
    public Button no;
    TextView title;
    RecyclerView recyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    RecyclerView.Adapter adapter;

    public CustomAlertDialog(Activity activity, RecyclerView.Adapter adapter) {
        super(activity);
        this.activity = activity;
        this.adapter = adapter;
        setupLayout();
    }

    private void setupLayout() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_alert_dialog_layout);
        no = (Button) findViewById(R.id.no);
        title = findViewById(R.id.title);
        recyclerView = findViewById(R.id.recycler_view);
        mLayoutManager = new LinearLayoutManager(activity);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(adapter);
        no.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.no) {
            dismiss();
        }
    }
}
