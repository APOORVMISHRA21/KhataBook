package com.example.khatabook;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toolbar;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import Adapters.PayrollAdapter;
import Models.Payroll;

public class PayrollActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private PayrollAdapter adapter;
    private DatabaseReference mbase,mbase2;
    private Toolbar toolbar;
    private TextView paymentMessage;
    private ProgressBar progressBar;
    private ImageView goHomeButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payroll);
        //getSupportActionBar().setTitle("PayRoll");

        paymentMessage = findViewById(R.id.finslMsg);

        ProgressDialog dialog = new ProgressDialog(this);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setTitle("Loading");
        dialog.setMessage("Loading. Please wait...");
        dialog.setIndeterminate(true);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        goHomeButton = findViewById(R.id.go_to_home_button);
        goHomeButton.setOnClickListener(view -> {
            finish();
        });

        mbase = FirebaseDatabase.getInstance().getReference().child("payroll");
        mbase2 = FirebaseDatabase.getInstance().getReference().child("payRollAmount");
        recyclerView = findViewById(R.id.payrollRecyclerView);

        recyclerView.setLayoutManager(
                new LinearLayoutManager(this)
        );
        FirebaseRecyclerOptions<Payroll> options
                = new FirebaseRecyclerOptions.Builder<Payroll>()
                .setQuery(mbase,Payroll.class)
                .build();

        adapter = new PayrollAdapter(options);
        recyclerView.setAdapter(adapter);
        mbase2.addListenerForSingleValueEvent(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String value = snapshot.getValue(String.class);
                if(value!=null)
                {
                    paymentMessage.setText("Total Payable Amount(₹): "+value);
                    dialog.dismiss();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    @Override protected void onStart()
    {
        super.onStart();
        adapter.startListening();
    }
    @Override protected void onStop()
    {
        super.onStop();
        adapter.stopListening();
    }

}