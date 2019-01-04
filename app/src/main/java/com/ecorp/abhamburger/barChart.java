package com.ecorp.abhamburger;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class barChart extends AppCompatActivity {

    private Map<String, Integer> data;
    private HorizontalBarChart chart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bar_chart);

        setChart();
    }

    void setChart(){
        chart =  findViewById(R.id.employeeChart);
        data = new LinkedHashMap<>();
        getAllEmployees();
    }
    List<Employee> employeeList = null;
    public void getAllEmployees(){
        employeeList = new ArrayList<>();
        final DatabaseReference db = FirebaseDatabase.getInstance().getReference();
        db.child("Employee").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Log.e("Count " ,""+snapshot.getChildrenCount());
                for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                    employeeList.add(postSnapshot.getValue(Employee.class));
                    Log.e("add " ,""+1);
                }
                buildData();
            }
            @Override
            public void onCancelled(DatabaseError firebaseError) {
                Log.e("The read failed: " ,firebaseError.getMessage());
            }
        });
    }

    void buildData(){
        for(Employee e : employeeList)
            data.put(e.firstName+" "+ e.lastName, e.actions);
        plotData();
    }


    String[] xv;
    private void plotData() {
        chart.clear();
        final ArrayList<String> xVals = new ArrayList<>();
        ArrayList<BarEntry> yVals = new ArrayList<>();

        for (Map.Entry<String, Integer> entry : data.entrySet()) {
            yVals.add(new BarEntry(xVals.size(), entry.getValue()));
            xVals.add(entry.getKey());
        }

        xv  = new String[xVals.size()];
        for(int i=0; i<xv.length; i++) xv[i] = xVals.get(i);

        XAxis xAxis = chart.getXAxis();
        xAxis.setDrawGridLines(false);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularityEnabled(true);
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                if (value >= 0) {
                    if (xv.length > (int) value) {
                        return xv[(int) value];
                    } else return "";
                } else {
                    return "";
                }
            }
        });

        chart.getAxisRight().setEnabled(false);
        YAxis axisLeft = chart.getAxisLeft();
        axisLeft.setAxisMinValue(0f);
        axisLeft.setGranularityEnabled(true);
        axisLeft.setGranularity(1f);

        BarDataSet dataSet = new BarDataSet(yVals, "Actions");
        BarData barData = new BarData(dataSet);
        barData.setBarWidth(0.7f);
        chart.setData(barData);
        chart.invalidate();
    }
}