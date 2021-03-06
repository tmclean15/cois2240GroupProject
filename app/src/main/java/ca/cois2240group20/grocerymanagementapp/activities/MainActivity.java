package ca.cois2240group20.grocerymanagementapp.activities;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import java.util.List;

import ca.cois2240group20.grocerymanagementapp.R;
import ca.cois2240group20.grocerymanagementapp.adapters_and_viewholders.PagerAdapter;
import ca.cois2240group20.grocerymanagementapp.database.AppDatabase;
import ca.cois2240group20.grocerymanagementapp.database.entities.FoodTileInfoGroceryList;
import ca.cois2240group20.grocerymanagementapp.database.entities.FoodTileInfoInventory;
import ca.cois2240group20.grocerymanagementapp.utility.App;
import ca.cois2240group20.grocerymanagementapp.view_models.SharedViewModel;

public class MainActivity extends AppCompatActivity {
    // This tag will be used to tag logs that come from this activity
    private static final String TAG = "MainActivity";
    // The viewPager is the main layout widget that will show all the different page fragments
    ViewPager viewPager;
    // The pagerAdapter is what handles which pages get shown in the viewPager
    PagerAdapter pagerAdapter;
    // View model is a shared state among the activity and its associated fragments
    SharedViewModel model;

    private AppDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Grab database
        database = App.getAppDatabase();

        // Set up view model for this activity. It will be used to share data with page fragments
        model = ViewModelProviders.of(this).get(SharedViewModel.class);

        if (database.foodTileDAO().getInventoryData() == null) {
            database.foodTileDAO().insertInventory(model.getAllInventory());
        }
        if (database.foodTileDAO().getGroceryListData() == null) {
            database.foodTileDAO().insertGroceryList(model.getAllGroceryList());
        }

        // Loads in data from the database into the view model
        loadInDatabaseData(database, model);

        pagerAdapter = new PagerAdapter(getSupportFragmentManager(), 2);
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
    // product to be added (to either Inventory or Grocery List)
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        if (intent.getStringExtra("method") != null) {
            if (intent.getStringExtra("method").equals("addInventory")) {
                addInventory();
            } else if (intent.getStringExtra("method").equals("addGroceryList")) {
                addGroceryList();
            } else if (intent.getStringExtra("method").equals("error")) {
                Log.d(TAG, "Error with adding product");
            }
        }
        if (intent.getStringExtra("edit") != null) {
            if (intent.getStringExtra("edit").equals("editInventory")) {
                editInventory();
            } else if (intent.getStringExtra("edit").equals("editGroceryList")) {
                editGroceryList();
            } else if (intent.getStringExtra("edit").equals("error")) {
                Log.d(TAG, "Error with editing product");
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    private void addInventory() {
        FoodTileInfoInventory newInvData = getIntent().getParcelableExtra("FoodTileInfoInventory");
        model.addInventory(newInvData);
        // We want to display the page that we initially clicked the floating action button
        // to add an item from, in this case InventoryFragment
        viewPager.setCurrentItem(0);
    }

    private void editInventory() {
        FoodTileInfoInventory editedInvData = getIntent().getParcelableExtra("FoodTileInfoInventory");
        int indexToEdit = getIntent().getIntExtra("index", -1);
        model.editInventory(editedInvData, indexToEdit);
        viewPager.setCurrentItem(0);
    }

    private void addGroceryList() {
        FoodTileInfoGroceryList newGroceryData = getIntent().getParcelableExtra("FoodTileInfoGroceryList");
        model.addGroceryList(newGroceryData);
        viewPager.setCurrentItem(1);
    }

    private void editGroceryList() {
        FoodTileInfoGroceryList editedGroceryData = getIntent().getParcelableExtra("FoodTileInfoGroceryList");
        int indexToEdit = getIntent().getIntExtra("index", -1);
        model.editGroceryList(editedGroceryData, indexToEdit);
        viewPager.setCurrentItem(1);
    }

    //onStop saves data altered in SharedViewModel by clearing old data from tables and inserting
    // new data from livedata
    @Override
    protected void onStop(){
        super.onStop();

        //Update tables in database (inventory)
        //Delete current contents
        database.foodTileDAO().deleteInventory();
        //Insert Live Data into new table
        //LiveData<List<FoodTileInfoInventory>> inventory = model.getInventoryData();
        List<FoodTileInfoInventory> inventory = model.getAllInventory();
        database.foodTileDAO().insertInventory(inventory);

        //Update tables in database (grocery)
        //delete current contents
        database.foodTileDAO().deleteGroceryList();
        //Insert Live Data into new table
        //LiveData<List<FoodTileInfoGroceryList>> grocery = model.getGroceryListData();
        List<FoodTileInfoGroceryList> grocery = model.getAllGroceryList();
        database.foodTileDAO().insertGroceryList(grocery);

    }

    protected void loadInDatabaseData(AppDatabase database, SharedViewModel model) {
        model.setInventoryData(database);
        model.setGroceryListData(database);
    }
}
