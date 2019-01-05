package com.ecorp.abhamburger;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class statistics {
    Context context;
    View layout = null;

    private static statistics instance = null;

    List<Order> orderList = null;

    public static statistics getInstance(){
        if(instance == null)
            instance = new statistics();
        return instance;
    }

    void setStatistics(View page, final Context context){
        this.layout = page;
        this.context = context;
        getAllOrders();
        page.findViewById(R.id.dishChart).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, pieChart.class);
                context.startActivity(intent);
            }
        });

        page.findViewById(R.id.barChart).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, barChart.class);
                context.startActivity(intent);
            }
        });


    }


    public void getAllOrders() {
        final DatabaseReference db = FirebaseDatabase.getInstance().getReference();
        orderList = new ArrayList<Order>();
        db.child("Order").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Log.e("Count ", "" + snapshot.getChildrenCount());
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    Log.e("ORDERS11 ", "" + 1);
                    orderList.add(postSnapshot.getValue(Order.class));
                }

                calcTotal();
            }

            @Override
            public void onCancelled(DatabaseError firebaseError) {
                Log.e("The read failed: ", firebaseError.getMessage());
            }
        });
    }


    void calcTotal(){
        double total = 0;
        for(Order o : orderList){
            total += o.getTotal();
        }
        ((TextView)layout.findViewById(R.id.total)).setText("$ "+total);
    }


}
