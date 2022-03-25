package com.project.of.busnavigationsystem.NavBar.Routes;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;

import com.google.android.material.appbar.CollapsingToolbarLayout;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.project.of.busnavigationsystem.R;
import com.project.of.busnavigationsystem.dummy.DummyContent;

/**
 * A fragment representing a single Bus detail screen.
 * This fragment is either contained in a {@link BusListActivity}
 * in two-pane mode (on tablets) or a {@link BusDetailActivity}
 * on handsets.
 */
public class BusDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";

    /**
     * The dummy content this fragment is presenting.
     */
    private DummyContent.DummyItem mItem;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public BusDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            mItem = DummyContent.ITEM_MAP.get(getArguments().getString(ARG_ITEM_ID));

            Activity activity = this.getActivity();
            net.opacapp.multilinecollapsingtoolbar.CollapsingToolbarLayout appBarLayout = (net.opacapp.multilinecollapsingtoolbar.CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle(mItem.content);
                Typeface typeface = Typeface.createFromAsset(getContext().getAssets(), "fonts/abel.ttf");
                appBarLayout.setCollapsedTitleTypeface(typeface);
                appBarLayout.setExpandedTitleTypeface(typeface);
                appBarLayout.setMaxLines(2);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.bus_detail, container, false);

        // Show the dummy content as text in a TextView.
        if (mItem != null) {
            Typeface typeface = Typeface.createFromAsset(container.getContext().getAssets(), "fonts/abel.ttf");
            ((TextView) rootView.findViewById(R.id.bus_detail)).setText(mItem.details);
            ((TextView) rootView.findViewById(R.id.bus_detail)).setTypeface(typeface);
        }

        return rootView;
    }
}
