package ca.cois2240group20.grocerymanagementapp.database;

//Room Database Import
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.persistence.room.*;

import ca.cois2240group20.grocerymanagementapp.database.entities.*;

//Util Import
import java.util.List;

//Data Access Object, executes queries on the FoodTile entity (table)
@Dao
public interface FoodTileDAO {
    //Query

    //Get EVERYTHING from inventory
    @Query("SELECT * FROM Inventory")
    //MutableLiveData<List<FoodTileInfoInventory>> getInventoryData();
    List<FoodTileInfoInventory> getInventoryData();

    //Get EVERYTHING from Grocery
    @Query("SELECT * FROM Grocery")
    //MutableLiveData<List<FoodTileInfoGroceryList>> getGroceryListData();
    List<FoodTileInfoGroceryList> getGroceryListData();

    //Update Inventory table onClose for persistence
    //First delete current Inventory Table
    @Query("DELETE FROM Inventory")
    void deleteInventory();

    //Insert pass liveData list to insert into table
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertInventory(List<FoodTileInfoInventory> inventoryLiveData);

    //Update Grocery List table onClose
    //First Delete current Grocery Table
    @Query("DELETE FROM Grocery")
    void deleteGroceryList();

    //Insert New Live Data
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertGroceryList(List<FoodTileInfoGroceryList> groceryListLiveData);


}