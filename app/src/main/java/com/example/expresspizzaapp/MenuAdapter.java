package com.example.expresspizzaapp;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.ViewHolder> {
    private String[][] pizzas;
    private DBManagerCart dbManagerCart;
    private String email;
    private double price;

    // constructor
    public MenuAdapter(String[][] data, String userEmail) {
        this.pizzas = data;
        this.email = userEmail;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView displayPizzaName;
        public TextView displayPizzaPrice;
        public Button btnSmallSize, btnMediumSize, btnLargeSize, btnAddToCart;

        public ViewHolder(View itemView) {
            super(itemView);

            this.displayPizzaName = (TextView) itemView.findViewById(R.id.pizzaName);
            this.displayPizzaPrice = (TextView) itemView.findViewById(R.id.pizzaPrice);

            this.btnSmallSize = (Button) itemView.findViewById(R.id.btnSmallSize);
            this.btnMediumSize = (Button) itemView.findViewById(R.id.btnMediumSize);
            this.btnLargeSize = (Button) itemView.findViewById(R.id.btnLargeSize);
            this.btnAddToCart = (Button) itemView.findViewById(R.id.btnAddToCart);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.item_layout, parent, false);

        dbManagerCart = new DBManagerCart(parent.getContext());
        dbManagerCart.open();

        return new ViewHolder(listItem);
    }

    public void resetButtonColors(Button[] buttons) {
        for (Button btn : buttons) {
            btn.setTextColor(Color.parseColor("#556080"));
            btn.setBackgroundColor(Color.parseColor("#F6F5F5"));
        }
    }

    public void markButton(Button btn) {
        btn.setTextColor(Color.parseColor("#F6F5F5"));
        btn.setBackgroundColor(Color.parseColor("#556080"));
    }

    public void onBindViewHolder(ViewHolder holder, int position) {
        final String[] pizza = pizzas[position];

        holder.displayPizzaName.setText(pizza[0]);

        price = Double.parseDouble(pizza[1]);

        holder.displayPizzaPrice.setText("$" + price);

        holder.btnSmallSize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetButtonColors(new Button[] {holder.btnSmallSize, holder.btnMediumSize, holder.btnLargeSize});
                markButton(holder.btnSmallSize);

                price = Double.parseDouble(pizza[1]);

                holder.displayPizzaPrice.setText("$" + String.format("%.2f", + price));
            }
        });

        holder.btnMediumSize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetButtonColors(new Button[] {holder.btnSmallSize, holder.btnMediumSize, holder.btnLargeSize});
                markButton(holder.btnMediumSize);
                price = Double.parseDouble(pizza[1]) + 4;

                holder.displayPizzaPrice.setText("$" + String.format("%.2f", price));
            }
        });

        holder.btnLargeSize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetButtonColors(new Button[] {holder.btnSmallSize, holder.btnMediumSize, holder.btnLargeSize});
                markButton(holder.btnLargeSize);

                price = Double.parseDouble(pizza[1]) + 8;
                holder.displayPizzaPrice.setText("$" + String.format("%.2f", price));
            }
        });

        holder.btnAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String priceStr = String.format("%.2f", price);
                double priceNew = Double.parseDouble(priceStr);

                dbManagerCart.insertItem(pizza[0], 1, priceNew, email);
            }
        });
    }

    @Override
    public int getItemCount() {
        return pizzas.length;
    }
}
