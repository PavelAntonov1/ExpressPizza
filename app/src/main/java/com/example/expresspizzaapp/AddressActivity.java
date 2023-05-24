package com.example.expresspizzaapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class AddressActivity extends AppCompatActivity {
    private EditText inputAddress;
    private Button btnAddAddress;
    private Button btnBack;
    private Context self = this;
    private DBManagerOrders dbManagerOrders;
    private DBManagerCart dbManagerCart;
    private DBManagerUsers dbManagerUsers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);

        inputAddress = findViewById(R.id.inputAddress);
        btnAddAddress = findViewById(R.id.btnAddAddress);
        btnBack = findViewById(R.id.btnBackAddress);

        Intent intent = getIntent();

        String description = intent.getExtras().getString("description");
        String email = intent.getExtras().getString("email");
        double price = intent.getExtras().getDouble("price");
        double points = intent.getExtras().getDouble("points");
        boolean pointsApplied = intent.getExtras().getBoolean("pointsApplied");

        dbManagerOrders = new DBManagerOrders(this);
        dbManagerOrders.open();

        dbManagerCart = new DBManagerCart(this);
        dbManagerCart.open();

        dbManagerUsers = new DBManagerUsers(this);
        dbManagerUsers.open();

        btnAddAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String address = inputAddress.getText().toString();

                if (address.trim().length() == 0) {
                    Toast.makeText(self, "Address field is empty",
                            Toast.LENGTH_SHORT).show();
                } else {
                    dbManagerOrders.insertOrder(description, price, address, email);

                    Toast.makeText(self, "A new order was added",
                            Toast.LENGTH_SHORT).show();

                    double pointsNew = 0;

                    if (pointsApplied) {
                        pointsNew = Math.round(price) * 10;
                        Log.d("TEST POINTS", points + " are now " + price * 10);
                        Toast.makeText(self, "You spent all of your points",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        Log.d("TEST POINTS", points + " + " + price * 10);
                        pointsNew = points + Math.round(price) * 10;
                    }

                    if (!email.equals("guest@gmail.com")) {
                        Toast.makeText(self,  "You now have " + pointsNew + " points",
                                Toast.LENGTH_SHORT).show();
                        dbManagerUsers.updateUserPoints(email, pointsNew);
                    }

                    dbManagerCart.deleteRecords();
                    finish();
                }

            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}