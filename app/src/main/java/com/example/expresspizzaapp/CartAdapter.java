package com.example.expresspizzaapp;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {
    private ArrayList<ArrayList<String>> items;
    private String email;
    private DBManagerCart dbManagerCart;

    public CartAdapter(ArrayList<ArrayList<String>> data, String userEmail) {
        this.items = data;
        this.email = userEmail;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public Button btnQuantity;
        public TextView displayPizza;
        public TextView displayPrice;
        public LinearLayout itemLayout;


        public ViewHolder(View itemView) {
            super(itemView);

            this.btnQuantity = (Button) itemView.findViewById(R.id.btnQuantity);
            this.displayPizza = (TextView) itemView.findViewById(R.id.displayPizza);
            this.displayPrice = (TextView) itemView.findViewById(R.id.displayPrice);
            this.itemLayout = (LinearLayout) itemView.findViewById(R.id.item);
        }
    }

    @Override
    public CartAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.cart_item_layout, parent, false);

        dbManagerCart = new DBManagerCart(parent.getContext());
        dbManagerCart.open();

        return new CartAdapter.ViewHolder(listItem);
    }

    public void onBindViewHolder(CartAdapter.ViewHolder holder, int position) {
        final ArrayList<String> pizza = items.get(position);

        holder.displayPizza.setText(pizza.get(0));
        holder.btnQuantity.setText("x" + pizza.get(1));

        double price = Double.parseDouble(pizza.get(2));

        holder.displayPrice.setText("$" +  String.format("%.2f", price));

        holder.btnQuantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int count = Integer.parseInt(holder.btnQuantity.getText().toString().split("")[1]);

                dbManagerCart.deleteItem(pizza.get(0), email, price);

                --count;

                CartActivity.decreasePrice(price);

                if (count > 0) {
                    holder.btnQuantity.setText("x" + count);
                } else {
                    holder.itemLayout.setVisibility(View.GONE);
                    holder.itemLayout.removeAllViews();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
