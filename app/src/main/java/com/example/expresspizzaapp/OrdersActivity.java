package com.example.expresspizzaapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import java.util.ArrayList;

public class OrdersActivity extends AppCompatActivity {
    private DBManagerOrders dbManagerOrders;
    private ArrayList<ArrayList<String>> orders;
    private String email;
    private TextView displayUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);

        displayUsername = findViewById(R.id.userNameOrders);

        dbManagerOrders = new DBManagerOrders(this);
        dbManagerOrders.open();

        Intent intent = getIntent();
        email = intent.getExtras().getString("email");

        String name = intent.getExtras().getString("name");
        double points = intent.getExtras().getDouble("points");

        displayUsername.setText(name);

        orders = dbManagerOrders.getOrders(email);

        // recycler view setting
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.ordersView);
        OrdersAdapter adapter = new OrdersAdapter (orders, this.email);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }
}