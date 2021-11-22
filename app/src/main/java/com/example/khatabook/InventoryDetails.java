package com.example.khatabook;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.google.firebase.database.ChildEventListener;
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
    private InventoryAdapter adapter;
    private DatabaseReference databaseReference,fdb;
    private ImageView goHomeButton;
    private ArrayList<Inventory> productList;
    private RelativeLayout progressLoadingView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory_details);

        productList = new ArrayList<>();
        recyclerView = findViewById(R.id.inventory_recycler);
        progressLoadingView = findViewById(R.id.loadingPanel);
        goHomeButton = findViewById(R.id.go_to_home_button);
        goHomeButton.setOnClickListener(view -> {
            finish();
        });

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("inventory");

        populateProductList();
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    populateProductList();

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
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
                adapter = new InventoryAdapter(productList,InventoryDetails.this);
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(InventoryDetails.this));
                progressLoadingView.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });


    }
}