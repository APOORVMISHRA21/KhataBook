package com.example.khatabook;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import Adapters.InventoryAdapter;
import Models.Inventory;

public class InventoryDetails extends AppCompatActivity {

    private final String TAG = "InventoryDetails";

    private RecyclerView recyclerView;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    private ArrayList<Inventory> productList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory_details);

        productList = new ArrayList<>();
        recyclerView = findViewById(R.id.inventory_recycler);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("inventory");

        populateProductList();

    }

    private void populateProductList(){

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                if(dataSnapshot.hasChildren()){
                    for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                        Inventory inventory = snapshot.getValue(Inventory.class);
                        Log.e(TAG, "Inventory Loaded : " + inventory.getProduct_name());
                        productList.add(inventory);
                    }
                }

                recyclerView.setAdapter(new InventoryAdapter(productList, InventoryDetails.this));
                recyclerView.setLayoutManager(new LinearLayoutManager(InventoryDetails.this));
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

    }
}