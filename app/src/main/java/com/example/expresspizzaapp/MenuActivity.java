package com.example.expresspizzaapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MenuActivity extends AppCompatActivity {
    private String name;
    private double points;
    private int discount;
    private String email;
    private String[][] pizzas;
    private DBManagerPizzas dbManagerPizzas;
    private DBManagerCart dbManagerCart;
    private Context self = this;

    TextView displayName;
    TextView displayPrice;

    Button btnSmallSize;
    Button btnMediumSize;
    Button btnLargeSize;

    ImageView btnCart;

    Button btnCustom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        // getting input data
        Intent intent = getIntent();

        name = intent.getExtras().getString("name");
        points = intent.getExtras().getDouble("points");
        discount = intent.getExtras().getInt("discount");
        email = intent.getExtras().getString("email");

        // initializing dbs

        dbManagerPizzas = new DBManagerPizzas(this);
        dbManagerPizzas.open();

        dbManagerCart = new DBManagerCart(this);
        dbManagerCart.open();

        btnSmallSize = findViewById(R.id.btnSmallSize);
        btnMediumSize = findViewById(R.id.btnMediumSize);
        btnLargeSize = findViewById(R.id.btnLargeSize);
        displayPrice = findViewById(R.id.pizzaPrice);
        btnCart = findViewById(R.id.btnCart);
        btnCustom = findViewById(R.id.buttonCustom);

        // getting pizza data from the database
        pizzas = dbManagerPizzas.getPizzas();

        dbManagerPizzas.close();

        // recycler view setting
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.menuView);
        MenuAdapter adapter = new MenuAdapter(pizzas, this.email);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        // displaying the user data
        displayName = findViewById(R.id.userNameOrders);
        displayName.setText(name);

        btnCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(self, CartActivity.class);

                intent.putExtra("name", name);
                intent.putExtra("points", points);
                intent.putExtra("discount", discount);
                intent.putExtra("email", email);

                startActivity(intent);
            }
        });

        btnCustom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(self, CustomPizzaActivity.class);

                intent.putExtra("name", name);
                intent.putExtra("points", points);
                intent.putExtra("discount", discount);
                intent.putExtra("email", email);

                startActivity(intent);
            }
        });
    }

}