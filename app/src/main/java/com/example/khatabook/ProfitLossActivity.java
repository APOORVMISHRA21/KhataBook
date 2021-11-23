package com.example.khatabook;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

import Adapters.PayrollAdapter;
import Adapters.ProfitLossAdapter;
import Models.Inventory;
import Models.Invoice;
import Models.Payroll;
import Models.expenditure;

public class ProfitLossActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ProfitLossAdapter adapter;
    private DatabaseReference mbase,mbase2,mbase3,mbase4,mbase5,mbase6,mbase7;
    private ImageView goHomeButton;
    private TextView finalIncome,manOfCost,payrollAmount,netFinalIncome,expenseAmount,finalOutcome;
    private int mCP;
    private int productSP,mCost,payroll,expense;
    private float percentage,netIncome,netInvestment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profit_loss);

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
        finalIncome = findViewById(R.id.finalIncome);
        manOfCost = findViewById(R.id.manfCost);
        payrollAmount = findViewById(R.id.payrollAmount);
        netFinalIncome = findViewById(R.id.netFinalIncome);
        expenseAmount = findViewById(R.id.expenseAmount);
        finalOutcome = findViewById(R.id.finalOutcome);

        mbase = FirebaseDatabase.getInstance().getReference().child("dataEntry").child("Expenditure");
        mbase2 = FirebaseDatabase.getInstance().getReference().child("finalInvoiceAmount");
        mbase3 = FirebaseDatabase.getInstance().getReference().child("payRollAmount");
        mbase5 = FirebaseDatabase.getInstance().getReference().child("manufacturingCost");
        mbase6 = FirebaseDatabase.getInstance().getReference().child("totalExpenseAmount");
        mbase7 = FirebaseDatabase.getInstance().getReference().child("maufacturingCost");


        recyclerView = findViewById(R.id.profitLossRecyclerView);

        recyclerView.setLayoutManager(
                new LinearLayoutManager(this)
        );
        FirebaseRecyclerOptions<expenditure> options
                = new FirebaseRecyclerOptions.Builder<expenditure>()
                .setQuery(mbase, expenditure.class)
                .build();
        adapter = new ProfitLossAdapter(options);
        recyclerView.setAdapter(adapter);
        calculateManufacturingPrice();

        mbase2.addListenerForSingleValueEvent(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Integer value = snapshot.getValue(Integer.class);
                productSP = value;
                finalIncome.setText("Product net sale value:(₹): "+ value.toString());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        mbase3.addListenerForSingleValueEvent(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String value = snapshot.getValue(String.class);
                if(value!=null)
                {
                    payroll = Integer.parseInt(value);
                    payrollAmount.setText("Amount payed to Employee(₹): "+value);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        mbase6.addListenerForSingleValueEvent(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String value = snapshot.getValue(String.class);
                if(value!=null)
                {
                    expense = Integer.parseInt(value);
                    expenseAmount.setText("Total Expenses(₹): "+value);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        mbase7.addListenerForSingleValueEvent(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Integer value = snapshot.getValue(Integer.class);
                if(value!=null)
                {
                    mCost = value;
                    netInvestment = mCost + payroll + expense;
                    netIncome = productSP - mCost -payroll -expense;
                    percentage =  netIncome/netInvestment * 100 ;
                    netFinalIncome.setText("Net Income(₹) : "+String.valueOf(netIncome));
                    if(netIncome > 0)
                    {
                        finalOutcome.setTextColor(Color.parseColor("#00ff44"));
                        finalOutcome.setText("Profit: "+percentage+"%");
                        dialog.dismiss();
                    }
                    else
                    {
                        finalOutcome.setTextColor(Color.parseColor("#ff1717"));
                        finalOutcome.setText("Loss: "+percentage+"%");
                        dialog.dismiss();
                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

//








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


    public void calculateManufacturingPrice()
    {
        mbase4 = FirebaseDatabase.getInstance().getReference().child("invoice");

        mbase4.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.hasChildren()){
                    for(DataSnapshot dataSnapshot : snapshot.getChildren()){

                        Invoice invoice = dataSnapshot.getValue(Invoice.class);
                        int quantity = invoice.getProduct_quantity();
                        String name = invoice.getProduct_name();

                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("inventory");
                        databaseReference
                                .orderByChild("product_name")
                                .equalTo(name)
                                .addListenerForSingleValueEvent(
                                        new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                for(DataSnapshot snapshot1: snapshot.getChildren())
                                                {
//                                                    Parentkey =snapshot1.get();
                                                    Inventory inventory = snapshot1.getValue(Inventory.class);
                                                    int cp = inventory.getCp();
                                                     mCP = mCP + cp * quantity;
                                                    FirebaseDatabase.getInstance().getReference("maufacturingCost").setValue(mCP);
                                                    manOfCost.setText("Product net manufacturing cost:(₹): "+mCP);



                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) { }
                                        }

                                );












                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}