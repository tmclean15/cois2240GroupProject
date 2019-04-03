package ca.cois2240group20.grocerymanagementapp.page_fragments;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import ca.cois2240group20.grocerymanagementapp.utility.FoodTileInfo;
import ca.cois2240group20.grocerymanagementapp.R;
import ca.cois2240group20.grocerymanagementapp.adapters_and_viewholders.InventoryAdapter;
import ca.cois2240group20.grocerymanagementapp.view_models.SharedViewModel;

public class GroceryListFragment extends Fragment {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    // Just some dummy data
    Date date = new Date();
    FoodTileInfo tile1 = new FoodTileInfo("apples", date, date, 20.00, 5);
    FoodTileInfo tile2 = new FoodTileInfo("apples", date, date, 20.00, 5);
    FoodTileInfo tile3 = new FoodTileInfo("apples", date, date, 20.00, 5);
    List<FoodTileInfo> data = new ArrayList<>(Arrays.asList(tile1,tile2,tile3));

    @Override
    public View onCreateView(final LayoutInflater inflater,
                             final ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_grocerylist, container, false);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.grocerylist_recycler_view);

        // This setting will improve performance as the layout size of the RecyclerView will not
        // change with changes in content
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        adapter = new InventoryAdapter(data);
        recyclerView.setAdapter(adapter);

        SharedViewModel model = ViewModelProviders.of(getActivity()).get(SharedViewModel.class);

        // Create the observer which updates the UI when live data in view model changes
        final Observer<List<FoodTileInfo>> observer = new Observer<List<FoodTileInfo>>() {
            @Override
            public void onChanged(@Nullable List<FoodTileInfo> foodTileInfos) {
                // Todo: update ui
            }
        };

        // Observe the live data in the view model
        model.getGroceryListData().observe(this, observer);

        FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.fabGroceryList);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View parentView = inflater.inflate(R.layout.activity_main, container, false);
                ViewPager viewPager = (ViewPager) parentView.findViewById(R.id.pager);
                viewPager.setCurrentItem(3);
            }
        });

        return rootView;
    }

    public void onFabClick(View view) {

    }
}
