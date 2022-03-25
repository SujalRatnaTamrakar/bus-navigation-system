package com.project.of.busnavigationsystem.Adapter;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.project.of.busnavigationsystem.R;
import com.project.of.busnavigationsystem.Popups.RouteName;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ListViewAdapter extends BaseAdapter {

    // Declare Variables
    Context mContext;
    LayoutInflater inflater;
    private List<RouteName> RouteList = null;
    private ArrayList<RouteName> arraylist;

    public ListViewAdapter(Context context, List<RouteName> RouteList) {
        mContext = context;
        this.RouteList = RouteList;
        inflater = LayoutInflater.from(mContext);
        this.arraylist = new ArrayList<RouteName>();
        this.arraylist.addAll(RouteList);
    }

    public class ViewHolder {
        TextView name;
    }

    @Override
    public int getCount() {
        return RouteList.size();
    }

    @Override
    public RouteName getItem(int position) {
        return RouteList.get(position);
    }

    @Override
    public long getItemId(int position) {
        int itemID;
        if (arraylist == null) {
            itemID = position;
        } else {
            itemID = arraylist.indexOf(RouteList.get(position));
        }
        return itemID;
    }

    public View getView(final int position, View view, ViewGroup parent) {
        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.list_view_items, null);

            // Locate the TextViews in listview_item.xml
            holder.name = (TextView) view.findViewById(R.id.name);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        // Set the results into TextViews
        holder.name.setText(RouteList.get(position).getRouteName());
        TextView nameLabel = view.findViewById(R.id.nameLabel);
        Typeface typeface = Typeface.createFromAsset(parent.getContext().getAssets(), "fonts/abel.ttf");
        holder.name.setTypeface(typeface);
        nameLabel.setTypeface(typeface);
        return view;

    }

    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        RouteList.clear();
        if (charText.length() == 0) {
            RouteList.addAll(arraylist);
        } else {
            for (RouteName wp : arraylist) {
                if (wp.getRouteName().toLowerCase(Locale.getDefault()).contains(charText)) {
                    RouteList.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }

}
