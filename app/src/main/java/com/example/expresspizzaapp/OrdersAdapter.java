package com.example.expresspizzaapp;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.ViewHolder> {
    private ArrayList<ArrayList<String>> orders;
    private String email;
    private DBManagerOrders dbManagerOrders;

    public OrdersAdapter(ArrayList<ArrayList<String>> data, String userEmail) {
        this.orders = data;
        this.email = userEmail;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public Button btnReceived;
        public TextView displayDescription;
        public TextView displayPrice;
        public TextView displayAddress;
        public TextView displayOrderId;
        public LinearLayout itemLayout;


        public ViewHolder(View itemView) {
            super(itemView);

            this.btnReceived = (Button) itemView.findViewById(R.id.btnReceived);
            this.displayDescription = (TextView) itemView.findViewById(R.id.orderDescription);
            this.displayPrice = (TextView) itemView.findViewById(R.id.orderPrice);
            this.displayAddress = (TextView) itemView.findViewById(R.id.orderAddress);
            this.displayOrderId = (TextView) itemView.findViewById(R.id.orderId);
            this.itemLayout = (LinearLayout) itemView.findViewById(R.id.order);
        }
    }

    @Override
    public OrdersAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.order_item_layout, parent, false);

        dbManagerOrders = new DBManagerOrders(parent.getContext());
        dbManagerOrders.open();

        return new OrdersAdapter.ViewHolder(listItem);
    }

    public void onBindViewHolder(OrdersAdapter.ViewHolder holder, int position) {
        final ArrayList<String> pizza = orders.get(position);

        holder.displayOrderId.setText("ORDER");
        holder.displayDescription.setText(pizza.get(1));
        holder.displayAddress.setText("To " + pizza.get(3));

        double price = Double.parseDouble(pizza.get(2));
        holder.displayPrice.setText("$" +  String.format("%.2f", price));

        holder.btnReceived.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dbManagerOrders.deleteItem(pizza.get(0));

                holder.itemLayout.setVisibility(View.GONE);
                holder.itemLayout.removeAllViews();
            }
        });
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }
}