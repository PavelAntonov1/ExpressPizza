package com.example.expresspizzaapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

public class CustomPizzaActivity extends AppCompatActivity {
    private RadioButton rbThin;
    private RadioButton rbRegular;
    private RadioButton rbThick;

    private RadioButton rbBeef;
    private RadioButton rbHam;
    private RadioButton rbChicken;

    private RadioButton rbCheddar;
    private RadioButton rbMonterey;
    private RadioButton rbMozzarella;

    private CheckBox cbTomato;
    private CheckBox cbCucumber;
    private CheckBox cbSpinach;
    private CheckBox cbPepper;
    private CheckBox cbOnion;

    private DBManagerCart dbManagerCart;

    private TextView userName;

    private LinearLayout container;

    private Button btnAdd;

    private double sum;
    private String customName;

    private TextView displayTotal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_pizza);

        dbManagerCart = new DBManagerCart(this);
        dbManagerCart.open();

        userName = findViewById(R.id.userNameOrders);

        btnAdd = findViewById(R.id.btnAdd);

        rbThin = findViewById(R.id.rbDoughThin);
        rbThick = findViewById(R.id.rbDoughThick);
        rbRegular = findViewById(R.id.rbDoughRegular);

        rbBeef = findViewById(R.id.rbBeef);
        rbHam = findViewById(R.id.rbHam);
        rbChicken = findViewById(R.id.rbChicken);

        rbCheddar = findViewById(R.id.rbCheddar);
        rbMonterey = findViewById(R.id.rbMonterey);
        rbMozzarella = findViewById(R.id.rbMozarella);

        cbTomato = findViewById(R.id.cbTomato);
        cbCucumber = findViewById(R.id.cbCucumber);
        cbSpinach = findViewById(R.id.cbSpinach);
        cbPepper = findViewById(R.id.cbPepper);
        cbOnion = findViewById(R.id.cbOnion);

        displayTotal = findViewById(R.id.displayTotalCustom);

        Intent intent = getIntent();

        String name = intent.getExtras().getString("name");
        double points = intent.getExtras().getDouble("points");
        String email = intent.getExtras().getString("email");

        userName.setText(name);

        container = findViewById(R.id.container);
        calcPrice();

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String sumStr = String.format("%.2f",sum);
                double sumNew = Double.parseDouble(sumStr);
                dbManagerCart.insertItem(customName, 1, sumNew, email);
                finish();
            }
        });

        rbThin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calcPrice();
            }
        });

        rbThick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calcPrice();
            }
        });

        rbRegular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calcPrice();
            }
        });

        rbHam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calcPrice();
            }
        });

        rbBeef.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calcPrice();
            }
        });

        rbChicken.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calcPrice();
            }
        });

        rbCheddar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calcPrice();
            }
        });

        rbMonterey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calcPrice();
            }
        });

        rbMozzarella.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calcPrice();
            }
        });

        cbTomato.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calcPrice();
            }
        });

        cbPepper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calcPrice();
            }
        });

        cbSpinach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calcPrice();
            }
        });

        cbCucumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calcPrice();
            }
        });

        cbOnion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calcPrice();
            }
        });
    }

    public void calcPrice() {
        double doughCost = 0;
        double meatCost = 0;
        double veggiesCost = 0;
        double cheeseCost = 0;

        String dough = "";
        String veggies = "";
        String meat = "";
        String cheese = "";

        if (rbThin.isChecked()) {
            doughCost = 5.99;
            dough = "Thin";
        }

        if (rbRegular.isChecked()) {
            doughCost = 6.99;
            dough = "Reg";
        }

        if (rbThick.isChecked()) {
            doughCost = 7.99;
            dough = "Thick";
        }

        if (rbBeef.isChecked()) {
            meatCost = 6.99;
            meat = ", Beef";
        }

        if (rbHam.isChecked()) {
            meatCost = 5.99;
            meat = ", Ham";
        }

        if (rbChicken.isChecked()) {
            meatCost = 6.99;
            meat = ", Chicken";
        }

        if (cbTomato.isChecked() || cbCucumber.isChecked() || cbSpinach.isChecked() || cbPepper.isChecked() || cbOnion.isChecked()) {
            veggiesCost = 4.99;
            veggies = "";

            if (cbTomato.isChecked()) {
                veggies += ", tom";
            }
            if (cbTomato.isChecked()) {
                veggies += ", cuc";
            }

            if (cbSpinach.isChecked()) {
                veggies += ", spi";
            }

            if (cbPepper.isChecked()) {
                veggies += ", pep";
            }

            if (cbOnion.isChecked()) {
                veggies += ", oni";
            }
        }

        if (rbCheddar.isChecked()) {
            cheeseCost = 2.99;
            cheese = ", Cheddar";
        }

        if (rbMonterey.isChecked()) {
            cheeseCost = 3.99;
            cheese = ", Monterey";
        }

        if (rbMozzarella.isChecked()) {
            cheeseCost = 4.99;
            cheese = ", Mozzarella";
        }

        customName = dough + meat + veggies + cheese;

        sum = doughCost + meatCost + veggiesCost + cheeseCost;
        displayTotal.setText("$" + String.format("%.2f",sum));
    }
}