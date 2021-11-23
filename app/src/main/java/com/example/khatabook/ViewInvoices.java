package com.example.khatabook;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import Adapters.InventoryAdapter;
import Adapters.InvoiceAdapter;
import Models.Inventory;
import Models.Invoice;

public class ViewInvoices extends AppCompatActivity {

    private final String TAG = "ViewInvoices";
    private RecyclerView mRecyclerView;
    private DatabaseReference databaseReference;
    private ArrayList<Invoice> invoiceList;
    private InvoiceAdapter adapter;
    private RelativeLayout progressLoadingView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_invoices);
        mRecyclerView = findViewById(R.id.view_invoice_recyclerview);
        progressLoadingView = findViewById(R.id.loadingPanel);
        invoiceList = new ArrayList<>();
        databaseReference = FirebaseDatabase.getInstance().getReference("invoice");
        populateInvoiceList();
    }

    private void populateInvoiceList(){

        Log.e(TAG, "Populating Invoice List...");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                if(dataSnapshot.hasChildren()){
                    for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                        Invoice invoice = snapshot.getValue(Invoice.class);
                        Log.e(TAG, "Invoice Loaded : " + invoice.getProduct_name());
                        invoiceList.add(invoice);
                    }
                }
                adapter = new InvoiceAdapter(invoiceList,ViewInvoices.this);
                mRecyclerView.setAdapter(adapter);
                mRecyclerView.setLayoutManager(new LinearLayoutManager(ViewInvoices.this));
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