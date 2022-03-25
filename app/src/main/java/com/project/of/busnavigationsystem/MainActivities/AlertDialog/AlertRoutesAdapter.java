package com.project.of.busnavigationsystem.MainActivities.AlertDialog;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.project.of.busnavigationsystem.R;

public class AlertRoutesAdapter extends RecyclerView.Adapter<AlertRoutesAdapter.RoutesViewHolder>  {
    private String[] mDataset;
    RecyclerViewItemClickListener recyclerViewItemClickListener;

    public AlertRoutesAdapter(String[] myDataset, RecyclerViewItemClickListener listener) {
        mDataset = myDataset;
        this.recyclerViewItemClickListener = listener;
    }

    @NonNull
    @Override
    public AlertRoutesAdapter.RoutesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.route_item, parent, false);

        RoutesViewHolder vh = new RoutesViewHolder(v);
        return vh;

    }

    @Override
    public void onBindViewHolder(@NonNull AlertRoutesAdapter.RoutesViewHolder routesViewHolder, int i) {
        routesViewHolder.mTextView.setText(mDataset[i]);
    }

    @Override
    public int getItemCount() {
        return mDataset.length;
    }



    public  class RoutesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView mTextView;

        public RoutesViewHolder(View v) {
            super(v);
            mTextView = (TextView) v.findViewById(R.id.textView);
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            recyclerViewItemClickListener.clickOnItem(mDataset[this.getAdapterPosition()]);

        }
    }

    public interface RecyclerViewItemClickListener {
        void clickOnItem(String data);
    }
}
