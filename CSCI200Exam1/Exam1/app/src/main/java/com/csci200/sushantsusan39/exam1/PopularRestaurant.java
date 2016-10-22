package com.csci200.sushantsusan39.exam1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class PopularRestaurant extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popular_restaurant);
        String[] numbers = {"Subway","Florida Avenue Grill","1905 Restaurant","Dukem Ethiopian Restaurant", "Dino's Grotto", "Busboys and Poets", "Alero Restaurant", "Oohh's & Aahh", "Red Hen", "Marvin", "matchbox", "Masa 14"};
        ListView lv = (ListView) findViewById(R.id.restaurants_popu);
        lv.setAdapter(new ArrayAdapter<String>(PopularRestaurant.this,
                android.R.layout.simple_list_item_1, numbers));


    }


}
