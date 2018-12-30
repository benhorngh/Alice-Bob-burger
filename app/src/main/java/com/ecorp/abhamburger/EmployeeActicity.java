package com.ecorp.abhamburger;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

public class EmployeeActicity extends AppCompatActivity {


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_profile:
                    changePage(profilePage);
                    return true;
                case R.id.navigation_orders:
                    changePage(ordersPage);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        bulidPage();
        changePage(profilePage);
    }






    View ordersPage =null;
    View profilePage =null;

    protected void changePage(View page){
        FrameLayout frame = (FrameLayout) findViewById(R.id.employee_main);
        frame.removeAllViews();
        frame.addView(page);
    }

    /**
     * build the Summary frame and homepage frame. only on create
     */
    protected void bulidPage(){
        if(ordersPage == null) {
            LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
            ordersPage = inflater.inflate(R.layout.employee_order,
                    (ViewGroup) findViewById(R.id.employeeOrders));
        }
        if(profilePage == null) {
            LayoutInflater inflater = (LayoutInflater)      this.getSystemService(LAYOUT_INFLATER_SERVICE);
            profilePage = inflater.inflate(R.layout.employee_orders,
                    (ViewGroup) findViewById(R.id.employeeOrders));
        }

        employeeOrders.getInstance().setAllOrders(ordersPage, this);
    }
}
