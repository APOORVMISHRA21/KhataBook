package com.example.khatabook;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import Adapters.AssetAdapter;
import Adapters.InventoryAdapter;
import Adapters.LiabilityAdapter;
import Models.AssetEntry;
import Models.Inventory;

public class ViewAssetLiability extends AppCompatActivity {

    private ArrayList<AssetEntry> assetList;
    private ArrayList<AssetEntry> liabilityList;
    private DatabaseReference databaseReferenceAsset, databaseReferenceLiability;
    private AssetAdapter assetAdapter;
    private LiabilityAdapter liabilityAdapter;
    private String TAG = "ViewAssetsLiability";
    private RecyclerView mAssetRecyclerView;
    private RecyclerView mLiabilityRecyclerView;
    private RelativeLayout progressLoadingView;
    private TextView netAssetTxt, netLiabilityTxt;
    private int netAssetValue = 0, netLiabilityValue = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_asset_liability);
        assetList = new ArrayList<>();
        liabilityList = new ArrayList<>();
        mAssetRecyclerView = findViewById(R.id.asset_recycler);
        mLiabilityRecyclerView = findViewById(R.id.liability_recycler);
        netAssetTxt = findViewById(R.id.net_asset_value);
        netLiabilityTxt = findViewById(R.id.net_liability_value);
        progressLoadingView = findViewById(R.id.loadingPanel);
        databaseReferenceAsset = FirebaseDatabase.getInstance().getReference("dataEntry").child("Assets");
        databaseReferenceLiability = FirebaseDatabase.getInstance().getReference("dataEntry").child("Liability");
        populateAssetList();
    }

    private void populateAssetList(){

        databaseReferenceAsset.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                if(dataSnapshot.hasChildren()){
                    for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                        AssetEntry asset = snapshot.getValue(AssetEntry.class);
                        Log.e(TAG, "Entry Loaded : " + asset.getName());
                        assetList.add(asset);
                    }
                }
                assetAdapter = new AssetAdapter(assetList,ViewAssetLiability.this);
                mAssetRecyclerView.setAdapter(assetAdapter);
                mAssetRecyclerView.setLayoutManager(new LinearLayoutManager(ViewAssetLiability.this));
                //progressLoadingView.setVisibility(View.GONE);
                populateLiabilityList();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }

    private void populateLiabilityList(){
        databaseReferenceLiability.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                if(dataSnapshot.hasChildren()){
                    for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                        AssetEntry asset = snapshot.getValue(AssetEntry.class);
                        Log.e(TAG, "Entry Loaded : " + asset.getName());
                        liabilityList.add(asset);
                    }
                }

                updateNetValues();

                liabilityAdapter = new LiabilityAdapter(liabilityList,ViewAssetLiability.this);
                mLiabilityRecyclerView.setAdapter(liabilityAdapter);
                mLiabilityRecyclerView.setLayoutManager(new LinearLayoutManager(ViewAssetLiability.this));
                progressLoadingView.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }

    private void updateNetValues(){

        for(AssetEntry entry : assetList){
            netAssetValue += Integer.parseInt(entry.getValue());
        }

        for(AssetEntry entry : liabilityList){
        netLiabilityValue += Integer.parseInt(entry.getValue());
        }

        netAssetTxt.setText("+ Rs. " + netAssetValue);
        netLiabilityTxt.setText("- Rs. " + netLiabilityValue);
    }
}