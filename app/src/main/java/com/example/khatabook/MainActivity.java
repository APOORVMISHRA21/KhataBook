package com.example.khatabook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseUser mCurrentUser;
    private Toolbar mToolbar;
    private CardView goToInventory,goDataEntry, goToInvoice, goToPayroll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();

        goToInventory = findViewById(R.id.go_to_see_inventory);
        goDataEntry = findViewById(R.id.go_to_data_entry);
        goToInvoice = findViewById(R.id.go_to_invoice);
        goToPayroll = findViewById(R.id.go_to_payroll);

        goToInventory.setOnClickListener(view -> {
            startActivity(new Intent(MainActivity.this, InventoryDetails.class));
        });
        goDataEntry.setOnClickListener(view -> {
            startActivity(new Intent(MainActivity.this, DataEntry.class));
        });
        goToInvoice.setOnClickListener(view -> {
            startActivity(new Intent(MainActivity.this, InvoiceActivity.class));
        });
        goToPayroll.setOnClickListener(view -> {
            startActivity(new Intent(MainActivity.this, PayrollActivity.class));
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            sendToStart();

        }

    }
    private void sendToStart() {
        Intent startIntent = new Intent(MainActivity.this,CaraouselActivity.class);
        startActivity(startIntent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {

            case R.id.settings:
                Intent i = new Intent(MainActivity.this, PayrollActivity.class);
                startActivity(i);
                finish();
                break;

            case R.id.logout:
                mAuth.signOut();
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }
}