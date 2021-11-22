package com.example.khatabook;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class DataEntry extends AppCompatActivity {
    private EditText mName,mDescription,mvalue;
    private ToggleButton mAssOrLia, mInOrEx;
    private Button btnSubmit;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mReference;
    private String name,description,value,toggle1,toggle2;
    private Spinner spinner1,spinner2;
    private ImageView goHomeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_entry);

        mName =  findViewById(R.id.edtName);
        mDescription = findViewById(R.id.edtDes);
        mvalue = findViewById(R.id.edtVal);

        goHomeButton = findViewById(R.id.go_to_home_button);
//        mAssOrLia = findViewById(R.id.toggleAssorLia);
//        mInOrEx = findViewById(R.id.toggleExporInc);
        name = mName.getText().toString();
        description = mDescription.getText().toString();
        value = mvalue.getText().toString();
        btnSubmit = findViewById(R.id.btn_submit);
        spinner1 = findViewById(R.id.spinnerAL);
        spinner2 = findViewById(R.id.spinnerIE);
        String[] items1 = new String[]{"Choose","Assets","Liability","None"};
        String[] items2 = new String[]{"Choose","Income","Expenditure","None"};
        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item,items1);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item,items2);
        spinner1.setAdapter(adapter1);
        spinner2.setAdapter(adapter2);

        goHomeButton.setOnClickListener(view -> {
            finish();
        });

        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                toggle1 = items1[i];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                toggle2 = items2[i];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

//        mAssOrLia.setOnClickListener(
//                new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        if(mAssOrLia.isChecked())
//                        {
//                            toggle1 = "asset";
//                        }
//                        else
//                        {
//                            toggle1 = "liability";
//                        }
//                    }
//                }
//        );
//        mInOrEx.setOnClickListener(
//                new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        if(mAssOrLia.isChecked())
//                        {
//                            toggle2 = "expense";
//                        }
//                        else
//                        {
//                            toggle2 = "income";
//                        }
//                    }
//                }
//        );

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mName.getText().toString().isEmpty()|| mDescription.getText().toString().isEmpty() || mvalue.getText().toString().isEmpty())
                {
                    Toast.makeText(getApplicationContext(),"Please fill all the details",Toast.LENGTH_SHORT).show();
                }
                else {

                    Map<String, Object> taskMap = new HashMap<String, Object>();
                    taskMap.put("name",mName.getText().toString());
                    taskMap.put("description",mDescription.getText().toString());
                    taskMap.put("value",mvalue.getText().toString());
//                taskMap.put("sheetType",toggle1);
//                taskMap.put("expenditure",toggle2);
                    if(toggle1 == "Assets")
                    {
                        FirebaseDatabase.getInstance().getReference("dataEntry").child("Assets").child("data-"+getAlphaNumericString()).updateChildren(taskMap);

                    }
                    if(toggle1 == "Liability")
                    {
                        FirebaseDatabase.getInstance().getReference("dataEntry").child("Liability").child("data-"+getAlphaNumericString()).updateChildren(taskMap);

                    }

                    if(toggle2 == "Income")
                    {
                        FirebaseDatabase.getInstance().getReference("dataEntry").child("Income").child("data-"+getAlphaNumericString()).updateChildren(taskMap);

                    }
                    if(toggle2 == "Expenditure")
                    {
                        FirebaseDatabase.getInstance().getReference("dataEntry").child("Expenditure").child("data-"+getAlphaNumericString()).updateChildren(taskMap);

                    }

                    DataEntry.super.finish();
                }






            }
        });


    }


    // function to generate a random string of length n
    static String getAlphaNumericString()
    {
    int n=10;
        // chose a Character random from this String
        String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                + "0123456789"
                + "abcdefghijklmnopqrstuvxyz";

        // create StringBuffer size of AlphaNumericString
        StringBuilder sb = new StringBuilder(n);

        for (int i = 0; i < n; i++) {

            // generate a random number between
            // 0 to AlphaNumericString variable length
            int index
                    = (int)(AlphaNumericString.length()
                    * Math.random());

            // add Character one by one in end of sb
            sb.append(AlphaNumericString
                    .charAt(index));
        }

        return sb.toString();
    }


}