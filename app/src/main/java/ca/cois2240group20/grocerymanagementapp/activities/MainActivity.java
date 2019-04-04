package ca.cois2240group20.grocerymanagementapp.activities;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import ca.cois2240group20.grocerymanagementapp.R;
import ca.cois2240group20.grocerymanagementapp.adapters_and_viewholders.PagerAdapter;
import ca.cois2240group20.grocerymanagementapp.utility.FoodTileInfo;
import ca.cois2240group20.grocerymanagementapp.view_models.SharedViewModel;

public class MainActivity extends AppCompatActivity {
    // The viewPager is the main layout widget that will show all the different page fragments
    ViewPager viewPager;
    // The pagerAdapter is what handles which pages get shown in the viewPager
    PagerAdapter pagerAdapter;
    // View model is a shared state among the activity and its associated fragments
    SharedViewModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set up view model for this activity. It will be used to share data with page fragments
        model = ViewModelProviders.of(this).get(SharedViewModel.class);

        pagerAdapter = new PagerAdapter(getSupportFragmentManager(), 3);
        viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setAdapter(pagerAdapter);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            toolbar.setTitle(R.string.app_name);
            setSupportActionBar(toolbar);
        }

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        // A tab listener is needed to specify which actions to take when the user selects a tab
        TabLayout.OnTabSelectedListener listener = new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                // Uses the pagerAdapter getItem() to determine what fragment is at the position of the selected tab
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                // Nothing really needs to be done here. We could potentially hide the page,
                // but that wouldn't be particularly useful to the user. This method needs to exist
                // either way to satisfy the TabListener abstract class methods.
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                // Similarly, nothing really needs to be done.
            }
        };

        tabLayout.addOnTabSelectedListener(listener);

        // This next few lines of code sets up a listener for our viewPager that changes tabs
        // when the user swipes through pages on their phone.
        viewPager.addOnPageChangeListener(
                new ViewPager.SimpleOnPageChangeListener() {
                    @Override
                    public void onPageSelected(int position) {
                        // When swiping between pages, select the
                        // corresponding tab.
                        viewPager.setCurrentItem(position);
                    }
                });

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

    }

    // This callback will be called if AddFoodTileActivity sends an intent with user data for new
    // product to be added
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        if(intent.getStringExtra("method").equals("addInventory")) {
            addInventory();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    private void addInventory() {
        FoodTileInfo newInvData = getIntent().getParcelableExtra("FoodTileInfo");
        model.addInventory(newInvData);
    }
}