package com.example.khatabook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chivorn.smartmaterialspinner.SmartMaterialSpinner;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import Adapters.InventoryAdapter;
import Models.Inventory;

public class InvoiceActivity extends AppCompatActivity {

    private final String TAG = "InvoiceActivity";
    ArrayList<String> productNameList;
    ArrayList<Inventory> productList;
    DatabaseReference databaseReference;
    TextView totalAmount;
    TextInputEditText noOfBoxes, discount, customerMail, customerName;
    TextInputLayout noOfBoxesLayout, discountLayout, customerMailLayout;
    Inventory inventorySelected;
    CardView calculateAmountButton, submitButton;
    ProgressBar progressBar;
    RelativeLayout progressLoadingView;

    private int numberOfBoxesInStock;

    private SmartMaterialSpinner<String> spProvince;

    private TextWatcher emailWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int i, int i1, int i2) {
            if(!isValidEmail(s.toString())){
                customerMailLayout.setBoxStrokeColor(getResources().getColor(R.color.red));
            }
            else{
                customerMailLayout.setBoxStrokeColor(getResources().getColor(R.color.grey));
            }
        }
        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }
        @Override
        public void afterTextChanged(Editable editable) {

        }
    };

    private TextWatcher discountWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int i, int i1, int i2) {
        }
        @Override
        public void onTextChanged(CharSequence s, int i, int i1, int i2) {
            if(convertStringToInt(s.toString()) > Integer.valueOf(100) || convertStringToInt(s.toString()) < Integer.valueOf(0)){
                discountLayout.setBoxStrokeColor(getResources().getColor(R.color.red));
            }
            else{
                discountLayout.setBoxStrokeColor(getResources().getColor(R.color.grey));
            }
        }
        @Override
        public void afterTextChanged(Editable editable) { }
    };

    private TextWatcher noOfBoxesWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int i, int i1, int i2) {

        }
        @Override
        public void onTextChanged(CharSequence s, int i, int i1, int i2) {
            if(convertStringToInt(s.toString()) > numberOfBoxesInStock || convertStringToInt(s.toString()) < Integer.valueOf(0)){
                noOfBoxesLayout.setBoxStrokeColor(getResources().getColor(R.color.red));
            }else{
                noOfBoxesLayout.setBoxStrokeColor(getResources().getColor(R.color.grey));

            }
        }

        @Override
        public void afterTextChanged(Editable editable) { }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice);

        customerMail = findViewById(R.id.customer_mail);
        customerMailLayout = findViewById(R.id.customer_mail_layout);
        calculateAmountButton = findViewById(R.id.calculate_amount);
        noOfBoxes = findViewById(R.id.noOfBoxes);
        noOfBoxesLayout = findViewById(R.id.noOfBoxesLayout);

        discount = findViewById(R.id.discount);
        discountLayout = findViewById(R.id.discount_layout);

        totalAmount = findViewById(R.id.total_amount);
        submitButton = findViewById(R.id.submit_button);

        customerName = findViewById(R.id.customer_name);

        progressBar = findViewById(R.id.progressBar);
        progressLoadingView = findViewById(R.id.loadingPanel);

        customerMail.addTextChangedListener(emailWatcher);
        noOfBoxes.addTextChangedListener(noOfBoxesWatcher);
        discount.addTextChangedListener(discountWatcher);

        databaseReference = FirebaseDatabase.getInstance().getReference("inventory");
        spProvince = findViewById(R.id.spinner1);
        productList = new ArrayList<>();
        populateProductList();

        calculateAmountButton.setOnClickListener(view -> {
            Float discountedValue = inventorySelected.getSp() -
                    (convertStringToInt(discount.getText().toString())*inventorySelected.getSp()/100.0f);
            Log.e(TAG, "dISCOUNT : " + convertStringToInt(discount.getText().toString()));
            Log.e(TAG, "dISCOUNTED VALUE : " + discountedValue);
            Float amount = convertStringToInt(noOfBoxes.getText().toString()) * discountedValue;
            StringBuilder sb = new StringBuilder();
            sb.append("Rs. ");
            sb.append(amount);
            totalAmount.setText(sb.toString());
        });

        submitButton.setOnClickListener(view -> {
            progressLoadingView.setVisibility(View.VISIBLE);

            new Handler(Looper.getMainLooper()).postDelayed((Runnable) () -> {

                updateInventory(inventorySelected.getProductId(),
                        inventorySelected.getQuantity() - convertStringToInt(noOfBoxes.getText().toString()));

                sendEmail();
            }, 2000);



            //sendEmail();
        });
    }

    private void initSpinner(){
        productNameList = new ArrayList<>();
        for(Inventory inventory : productList){
            productNameList.add(inventory.getProduct_name());
        }

        spProvince.setItem(productNameList);

        spProvince.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(InvoiceActivity.this,
                        productList.get(i).getProduct_name(),
                        Toast.LENGTH_SHORT).show();

                inventorySelected = productList.get(i);
                StringBuilder sb = new StringBuilder();
                numberOfBoxesInStock = inventorySelected.getQuantity();
                sb.append(inventorySelected.getQuantity());
                sb.append(" Boxes Available.");
                spProvince.setFloatingLabelText(sb.toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
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
                    progressLoadingView.setVisibility(View.GONE);
                    initSpinner();
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

    }

    public static boolean isValidEmail(String email) {
        String expression = "^[\\w\\.]+@([\\w]+\\.)+[A-Z]{2,7}$";
        CharSequence inputString = email;
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputString);
        if (matcher.matches())
        {
            return true;
        }
        else {
            return false;
        }

    }

    private Integer convertStringToInt(String s){

        try {
            Log.e(TAG, "Integer value : " + Integer.valueOf(s));
            return Integer.valueOf(s);
        } catch (NumberFormatException e){
            return 0;
        }
    }

    private void sendEmail(){
        String[] TO = {customerMail.getText().toString()};
        Log.e(TAG, TO.toString());
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);

        StringBuilder message = new StringBuilder();
        message.append("Dear ");
        message.append(customerName.getText().toString());
        message.append(",\n\n");
        message.append("Invoice against purchase of ");
        message.append(inventorySelected.getProduct_name());
        message.append(".");
        message.append("\n\n\n");

        message.append("No. of boxes booked : ");
        message.append(convertStringToInt(noOfBoxes.getText().toString()));
        message.append(".");
        message.append("\n\n\n");

        message.append("Discount Offered : " + convertStringToInt(discount.getText().toString()) + " %.");
        message.append("\n\n\n");

        message.append("Total Amount : " + totalAmount.getText().toString());
        message.append("\n\n\n");

        message.append("Thanks & Regards,");
        message.append("\n");
        message.append("Khatabook Pvt. Ltd.");


        //emailIntent.setDataAndType(Uri.parse("mailto:"), "message/rfc822");
        emailIntent.setType("message/rfc822");
        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Invoice from KhataBook");
        emailIntent.putExtra(Intent.EXTRA_TEXT, message.toString());
        emailIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        try{
            startActivity(Intent.createChooser(emailIntent, "Send Email..."));
            //finish();
        }catch(android.content.ActivityNotFoundException e){
            Toast.makeText(InvoiceActivity.this,
                    "There is no email client installed..!", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateInventory(String productId, int quantity){
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("inventory");
//        myRef.child("quantity").setValue(quantity);

        Inventory inventory = new Inventory(productId,
                inventorySelected.getProduct_name(), quantity,
                inventorySelected.getCp(),
                inventorySelected.getSp());

        myRef.child(productId).setValue(inventory);
    }
}