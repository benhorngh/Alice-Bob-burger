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

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class EmployeeActivity extends AppCompatActivity {


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

        buildPage();
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
    protected void buildPage(){
        if(ordersPage == null) {
            LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
            ordersPage = inflater.inflate(R.layout.employee_orders,
                    (ViewGroup) findViewById(R.id.employeeOrders));
        }
        if(profilePage == null) {
            LayoutInflater inflater = (LayoutInflater)      this.getSystemService(LAYOUT_INFLATER_SERVICE);
            profilePage = inflater.inflate(R.layout.employee_orders,
                    (ViewGroup) findViewById(R.id.employeeOrders));
        }

        employeeOrders.getInstance().setAllOrders(ordersPage, this);
        buildProfile();

    }

    protected void buildProfile(){
        Employee e=(Employee) AuthenticatedUserHolder.instance.getAppUser();
        ((TextView)profilePage.findViewById(R.id.fname_tx)).setText("firt name: "+e.firstName);
        ((TextView)profilePage.findViewById(R.id.lname_tx)).setText("last name: "+e.lastName);
        ((TextView)profilePage.findViewById(R.id.mail_tx)).setText("name: "+e.email);
        ((TextView)profilePage.findViewById(R.id.type_tx)).setText("name: "+e.type);
        ((TextView)profilePage.findViewById(R.id.salary_tx)).setText("name: "+e.salary);
    }
}
