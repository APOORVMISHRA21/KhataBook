package com.example.khatabook;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
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

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import Adapters.InventoryAdapter;
import Models.Inventory;
import Models.Invoice;

public class InvoiceActivity extends AppCompatActivity {

    private final String TAG = "InvoiceActivity";
    private final String YOUR_FOLDER_NAME = "Khatabook";
    private ArrayList<String> productNameList;
    private ArrayList<Inventory> productList;
    private DatabaseReference databaseReference,mbase;
    private TextView totalAmount;
    private TextInputEditText noOfBoxes, discount, customerMail, customerName;
    private TextInputLayout noOfBoxesLayout, discountLayout, customerMailLayout;
    private Inventory inventorySelected;
    private CardView calculateAmountButton, submitButton;
    private ProgressBar progressBar;
    private RelativeLayout progressLoadingView;
    private ImageView goHomeButton;
    private String totalInvoiceAmount;
    private Float amount;
    private float finalInvoiceAmount;
    private int selected=0;


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

    @RequiresApi(api = Build.VERSION_CODES.O)
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

        goHomeButton = findViewById(R.id.go_to_home_button);
        goHomeButton.setOnClickListener(view -> {
            finish();
        });

        calculateAmountButton.setOnClickListener(view -> {

            if(discount.getText().toString().isEmpty())
            {
                Toast.makeText(getApplicationContext(),"Please fill discount value",Toast.LENGTH_SHORT).show();

            }
            else if(noOfBoxes.getText().toString().isEmpty())
            {
                Toast.makeText(getApplicationContext(),"Please enter number of boxes",Toast.LENGTH_SHORT).show();
            }
            else if(selected == 0)
            {
                Toast.makeText(getApplicationContext(),"Please select product",Toast.LENGTH_SHORT).show();
            }
            else {
                Float discountedValue = inventorySelected.getSp() -
                        (convertStringToInt(discount.getText().toString())*inventorySelected.getSp()/100.0f);
                Log.e(TAG, "dISCOUNT : " + convertStringToInt(discount.getText().toString()));
                Log.e(TAG, "dISCOUNTED VALUE : " + discountedValue);
                amount = convertStringToInt(noOfBoxes.getText().toString()) * discountedValue;
                StringBuilder sb = new StringBuilder();
                sb.append("Rs. ");
                sb.append(amount);
                totalInvoiceAmount = sb.toString();
                totalAmount.setText(sb.toString());
            }




        });

        submitButton.setOnClickListener(view -> {

            if(customerMail.getText().toString().isEmpty())
            {
                Toast.makeText(getApplicationContext(),"Please enter customer mail",Toast.LENGTH_SHORT).show();

            }
            else if(customerName.getText().toString().isEmpty())
            {
                Toast.makeText(getApplicationContext(),"Please enter customer name",Toast.LENGTH_SHORT).show();
            }
            else if(discount.getText().toString().isEmpty())
            {
                Toast.makeText(getApplicationContext(),"Please enter discount value",Toast.LENGTH_SHORT).show();
            }
            else if(noOfBoxes.getText().toString().isEmpty())
            {
                Toast.makeText(getApplicationContext(),"Please enter number of boxes",Toast.LENGTH_SHORT).show();
            }
            else if(selected == 0)
            {
                Toast.makeText(getApplicationContext(),"Please select product",Toast.LENGTH_SHORT).show();
            }
            else
            {
                progressLoadingView.setVisibility(View.VISIBLE);

                new Handler(Looper.getMainLooper()).postDelayed((Runnable) () -> {

                    updateInventory(inventorySelected.getProductId(),
                            inventorySelected.getQuantity() - convertStringToInt(noOfBoxes.getText().toString()));
                    //generatePdf();
                    sendEmail();
                    addToFirebaseDb(customerName.getText().toString(),
                            customerMail.getText().toString(),
                            inventorySelected.getProduct_name(),
                            noOfBoxes.getText().toString(),
                            discount.getText().toString(), amount);

                }, 2000);
                InvoiceActivity.super.finish();

            }




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
                selected =1;
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

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void addToFirebaseDb(String name, String mail, String product, String quantity, String discount, float totalAmount)
    {

        LocalTime time = LocalTime.now(ZoneId.systemDefault());
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("hh:mm a", Locale.ENGLISH);
        String formattedTime = dtf.format(time);
//        Date c = Calendar.getInstance().getTime();
//        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
//        String formatedDate = df.format(c);
//        Map<String, Object> taskMap = new HashMap<String, Object>();
//        taskMap.put("customerName",name);
//        taskMap.put("customerMail",mail);
//        taskMap.put("timestamp",new Date().getTime());
//        taskMap.put("invoiceProduct",product);
//        taskMap.put("invoiceQuantity",quantity);
//        taskMap.put("invoiceDiscount",discount);
//        taskMap.put("invoiceTotalAmount",totalAmount);
        mbase = FirebaseDatabase.getInstance().getReference().child("finalInvoiceAmount");

        Invoice invoice = new Invoice(name, mail, product, convertStringToInt(quantity),convertStringToInt(discount), new Date().getTime(),totalAmount);
        FirebaseDatabase.getInstance().getReference("invoice").child(formattedTime).setValue(invoice);
        mbase.addListenerForSingleValueEvent(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                 finalInvoiceAmount = snapshot.getValue(Integer.class);
                finalInvoiceAmount =  finalInvoiceAmount + totalAmount;
                FirebaseDatabase.getInstance().getReference("finalInvoiceAmount").setValue(finalInvoiceAmount);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private void generatePdf(){

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);

        RelativeLayout invoiceView = (RelativeLayout) inflater.inflate(R.layout.sample_invoice, null);

        TextView invoiceCustomerName,
                invoiceCustomerMail,
                invoiceDate,
                invoiceProduct,
                invoiceQuantity,
                invoiceDiscount,
                invoiceTotalAmount;

        invoiceDate = invoiceView.findViewById(R.id.invoice_date);
        invoiceCustomerName = invoiceView.findViewById(R.id.invoice_customer_name);
        invoiceCustomerMail = invoiceView.findViewById(R.id.invoice_customer_mail);
        invoiceProduct = invoiceView.findViewById(R.id.invoice_product_name);
        invoiceQuantity = invoiceView.findViewById(R.id.invoice_noOfBoxes);
        invoiceDiscount = invoiceView.findViewById(R.id.invoice_discount);
        invoiceTotalAmount = invoiceView.findViewById(R.id.invoice_amount);

        invoiceCustomerName.setText(customerName.getText().toString());
        invoiceCustomerMail.setText(customerMail.getText().toString());
        invoiceProduct.setText(inventorySelected.getProduct_name());
        invoiceQuantity.setText(noOfBoxes.getText().toString());
        invoiceDiscount.setText(discount.getText().toString());

        Float discountedValue = inventorySelected.getSp() -
                (convertStringToInt(discount.getText().toString())*inventorySelected.getSp()/100.0f);
        Log.e(TAG, "dISCOUNT : " + convertStringToInt(discount.getText().toString()));
        Log.e(TAG, "dISCOUNTED VALUE : " + discountedValue);
        Float amount = convertStringToInt(noOfBoxes.getText().toString()) * discountedValue;
        StringBuilder sb = new StringBuilder();
        sb.append("Rs. ");
        sb.append(amount);

        invoiceTotalAmount.setText(sb.toString());

        invoiceView.setDrawingCacheEnabled(true);

        getSaveImageFilePath(invoiceView);
    }

    public static Bitmap getBitmapFromView(View view) {
        //Define a bitmap with the same size as the view
        Bitmap returnedBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(),Bitmap.Config.ARGB_8888);
        //Bind a canvas to it
        Canvas canvas = new Canvas(returnedBitmap);
        //Get the view's background
        Drawable bgDrawable =view.getBackground();
        if (bgDrawable!=null)
            //has background drawable, then draw it on the canvas
            bgDrawable.draw(canvas);
        else
            //does not have background drawable, then draw white background on the canvas
            canvas.drawColor(Color.WHITE);
        // draw the view on the canvas
        view.draw(canvas);
        //return the bitmap
        return returnedBitmap;
    }

    String getSaveImageFilePath(View view) {

        File mediaStorageDir = new File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), YOUR_FOLDER_NAME);
        // Create a storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(YOUR_FOLDER_NAME, "Failed to create directory");
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageName = "IMG_" + timeStamp + ".jpg";

        String selectedOutputPath = mediaStorageDir.getPath() + File.separator + imageName;
        Log.d(YOUR_FOLDER_NAME, "selected camera path " + selectedOutputPath);

        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(view.getDrawingCache());

        int maxSize = 1080;

        int bWidth = bitmap.getWidth();
        int bHeight = bitmap.getHeight();

        if (bWidth > bHeight) {
            int imageHeight = (int) Math.abs(maxSize * ((float)bitmap.getWidth() / (float) bitmap.getHeight()));
            bitmap = Bitmap.createScaledBitmap(bitmap, maxSize, imageHeight, true);
        } else {
            int imageWidth = (int) Math.abs(maxSize * ((float)bitmap.getWidth() / (float) bitmap.getHeight()));
            bitmap = Bitmap.createScaledBitmap(bitmap, imageWidth, maxSize, true);
        }
        view.setDrawingCacheEnabled(false);
        view.destroyDrawingCache();

        OutputStream fOut = null;
        try {
            File file = new File(selectedOutputPath);
            fOut = new FileOutputStream(file);

            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
            fOut.flush();
            fOut.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return selectedOutputPath;
    }
}