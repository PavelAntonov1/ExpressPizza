package com.example.expresspizzaapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class CartActivity extends AppCompatActivity {
    private DBManagerCart dbManagerCart;
    private DBManagerUsers dbManagerUsers;
    private ArrayList<ArrayList<String>> items;
    private String email;
    private TextView displayUsername;
    private TextView displayPoints;
    private Button btnCheckout;
    private Button btnOrders;
    private CheckBox cbApplyPoints;
    private static TextView displayTotalPrice;
    private static double sum;
    private Context self = this;
    private static int discount;
    private static TextView displayDiscountText;
    private static double sumDiscountSenior;

    private static double sumDiscount;
    private double points;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        displayUsername = findViewById(R.id.userNameOrders);
        displayPoints = findViewById(R.id.displayPoints);
        displayDiscountText = findViewById(R.id.discountText);

        displayTotalPrice = findViewById(R.id.displayTotalPrice);
        btnCheckout = findViewById(R.id.btnCheckout);
        btnOrders = findViewById(R.id.btnOrders);

        cbApplyPoints = findViewById(R.id.cbPoints);

        dbManagerCart = new DBManagerCart(this);
        dbManagerCart.open();

        dbManagerUsers = new DBManagerUsers(this);
        dbManagerUsers.open();

        Intent intent = getIntent();
        email = intent.getExtras().getString("email");

        String name = intent.getExtras().getString("name");

        if (!email.equals("guest@gmail.com") && !name.equals("GUEST")) {
            points = Double.parseDouble(dbManagerUsers.getUserInfo(email)[1]);
        } else {
            points = 0;
        }

        discount = intent.getExtras().getInt("discount");

        if (email.equals("guest@gmail.com") || name.equals("GUEST")) {
            cbApplyPoints.setVisibility(View.GONE);
            ImageView iv = findViewById(R.id.imageView3);
            iv.setVisibility(View.GONE);
            displayPoints.setVisibility(View.GONE);
        }

        displayUsername.setText(name);

        items = dbManagerCart.getItems(email);
        calcTotalPrice();

        // recycler view setting
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.cartView);
        CartAdapter adapter = new CartAdapter(items, this.email);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        // seniour discount
        if (discount == 1) {
            displayDiscountText.setText("Price without senior discount is $" + String.format("%.2f", sum) + ",\npoints are applied to it");
        } else {
            displayDiscountText.setText("");
        }

        btnCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(self, AddressActivity.class);
                String description = "";

                if (items.size() <= 0) {
                    Toast.makeText(self, "You don't have any items in your cart",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                int i = 0;
                for (ArrayList<String> item : items) {
                    String qStr = "x" + item.get(1);
                    String nameStr = item.get(0);
                    if (i != items.size() - 1) {
                        description += qStr + " " + nameStr + "\n";
                    } else {
                        description += qStr + " " + nameStr;
                    }
                    i++;
                }

                intent.putExtra("description", description);

                if (cbApplyPoints.isChecked() && points >= 2000) {
                    intent.putExtra("price", sumDiscount);
                    dbManagerUsers.updateUserPoints(email, 0);
                    intent.putExtra("pointsApplied", true);
                } else {
                    if (discount == 1) {
                        intent.putExtra("price", sumDiscountSenior);
                        intent.putExtra("pointsApplied", false);
                    } else {
                        intent.putExtra("price", sum);
                        intent.putExtra("pointsApplied", false);
                    }
                }


                intent.putExtra("email", email);
                intent.putExtra("points", points);

                startActivity(intent);
                finish();
            }
        });

        cbApplyPoints.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!email.equals("guest@gmail.com") && !name.equals("GUEST")) {
                    points = Double.parseDouble(dbManagerUsers.getUserInfo(email)[1]);
                } else {
                    points = 0;
                }

                Log.d("UPDATED POINTS", points + "");

                if (cbApplyPoints.isChecked()) {
                    displayPoints.setText(String.format("%.0f", points));

                    if (points >= 2000) {
                        double discountPercents = Math.floor(points / 200);
                        double discount = (discountPercents / 100) * sum;

                        sumDiscount = sum - discount;

                        if (sumDiscount < 0) {
                            sumDiscount = 0;
                        }

                        displayTotalPrice.setText("$" + String.format("%.2f", sumDiscount));
                    } else {
                        Toast.makeText(self, "You need to have 2000 points min to apply ",
                                Toast.LENGTH_SHORT).show();
                    }
                } else {
                    displayPoints.setText("XXXX");

                    if (discount == 1) {
                        displayTotalPrice.setText("$" + String.format("%.2f", sumDiscountSenior));
                    } else {
                        displayTotalPrice.setText("$" + String.format("%.2f", sum));
                    }
                }
            }
        });

        btnOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(self, OrdersActivity.class);

                intent.putExtra("email", email);
                intent.putExtra("name", name);
                intent.putExtra("points", points);

                startActivity(intent);
            }
        });
    }

    public void calcTotalPrice() {
        sum = 0;

        for (ArrayList<String> item : items) {
            int q = Integer.parseInt(item.get(1));
            double price = Double.parseDouble(item.get(2));
            sum += q * price;
        }

        if (discount == 1) {
            sumDiscountSenior = sum - (0.1 * sum);
            displayTotalPrice.setText("$" + String.format("%.2f", sumDiscountSenior));
        } else {
            displayTotalPrice.setText("$" + String.format("%.2f", sum));
        }

    }

    public static void decreasePrice(double amount) {
        sum -= amount;

        if (sum < 0) {
            sum = 0;
        }

        if (discount == 1) {
            sumDiscountSenior = sum - (0.1 * sum);
            displayTotalPrice.setText("$" + String.format("%.2f", sumDiscountSenior));
            displayDiscountText.setText("Price without senior discount is $" + String.format("%.2f", sum));
        } else {
            displayTotalPrice.setText("$" + String.format("%.2f", sum));
        }
    }
}