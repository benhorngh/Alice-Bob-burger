package com.ecorp.abhamburger;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class pieChart extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pie_chart);

        setStatistics();
    }
    List<Dish> dishList = null;
    List<Order> orderList = null;


    void setStatistics(){
        getAllDishes();
        getAllOrders();
    }

    public void getAllDishes(){
        dishList = new ArrayList<>();
        final DatabaseReference db = FirebaseDatabase.getInstance().getReference();
        db.child("Dish").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                    dishList.add(postSnapshot.getValue(Dish.class));
                }
            }
            @Override
            public void onCancelled(DatabaseError firebaseError) {
                Log.e("The read failed: " ,firebaseError.getMessage());
            }
        });
    }
    public void getAllOrders() {
        orderList = new ArrayList<>();
        final DatabaseReference db = FirebaseDatabase.getInstance().getReference();

        db.child("Order").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    orderList.add(postSnapshot.getValue(Order.class));
                }
                setPie();
            }

            @Override
            public void onCancelled(DatabaseError firebaseError) {
                Log.e("The read failed: ", firebaseError.getMessage());
            }
        });
    }

    void setPie(){
        HashMap<String, Integer> hm = new HashMap<>();

        float sum=0;
        for (int i=0; i<dishList.size(); i++){
            int count = 0;
            Integer key = dishList.get(i).id;
            for(int j=0; j<orderList.size(); j++){
                for(int k = 0; k<orderList.get(j).getDishesId().size(); k++){
                    if(key.equals(orderList.get(j).getDishesId().get(k)))
                        count++;
                }
            }
            hm.put(dishList.get(i).name, count);
            sum+=count;
        }
        List<PieEntry> pieEntries = new ArrayList<>();

        for(int i =0; i< dishList.size(); i++){
            pieEntries.add(new PieEntry((float)((float)hm.get(dishList.get(i).name)/* /sum */),dishList.get(i).name));
        }

        PieDataSet pieDataSet = new PieDataSet(pieEntries, "Popular dish");
        pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        PieData pieData = new PieData(pieDataSet);

        PieChart pieChart = findViewById(R.id.pieChart);

        pieChart.setData(pieData);
        pieChart.invalidate();

    }



}
