package com.example.khatabook;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;

import com.opensooq.pluto.PlutoIndicator;
import com.opensooq.pluto.PlutoView;
import com.opensooq.pluto.base.PlutoAdapter;
import com.opensooq.pluto.base.PlutoViewHolder;

import java.util.ArrayList;

import Adapters.CaraouselAdapter;
import Models.SampleCaraouselCard;

public class CaraouselActivity extends AppCompatActivity {

    private PlutoView plutoView;
    private PlutoAdapter plutoAdapter;
    private PlutoIndicator plutoIndicator;
    private CardView goToHomeButton;


    private ArrayList<SampleCaraouselCard> getModelList(){

        ArrayList<SampleCaraouselCard> modelList = new ArrayList<>();

        modelList.add(new SampleCaraouselCard(R.drawable.ic_caraousel1,
                "Create your Logo",
                "Simple and easy way to create unlimited logos"));

        modelList.add(new SampleCaraouselCard(R.drawable.ic_caraousel2,
                "Logo Suggestions for you",
                "We create logos for you in seconds based on simple steps"));

        modelList.add(new SampleCaraouselCard(R.drawable.ic_caraousel3,
                " Ultimate Editor",
                "Edit, Save and Share your beautiful logos to anyone"));

        return modelList;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_caraousel);

        plutoView = findViewById(R.id.slider_view);
        plutoAdapter = new CaraouselAdapter(getModelList(),
                this);
        plutoIndicator = findViewById(R.id.custom_indicator);
        goToHomeButton = findViewById(R.id.go_to_home_button);

        plutoIndicator.setDefaultIndicatorColor(getResources().getColor(R.color.black),
                getResources().getColor(R.color.grey));

        plutoView.create(plutoAdapter, getLifecycle());

        plutoView.setIndicatorVisibility(true);

        plutoView.setCustomIndicator(plutoIndicator);

        goToHomeButton.setOnClickListener(v -> {
            Intent intent = new Intent(CaraouselActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });
    }
}