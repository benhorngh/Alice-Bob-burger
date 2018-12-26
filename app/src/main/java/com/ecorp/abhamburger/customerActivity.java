package com.ecorp.abhamburger;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class customerActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer);

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation);
//        customer_navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        bottomNavigationView.setOnNavigationItemSelectedListener
                (new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        Fragment selectedFragment = null;
                        switch (item.getItemId()) {
                            case R.id.navigation_profile:
                                selectedFragment = customerProfile.newInstance();
                                break;
                            case R.id.navigation_menu:
                                selectedFragment = customerMenu.newInstance();
                                break;
                            case R.id.navigation_about:
                                selectedFragment = customerAbout.newInstance();
                                break;
                        }
                        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.frame_layout, selectedFragment);
                        transaction.commit();

                        return true;
                    }
                });

        //Manually displaying the first fragment - one time only
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, customerMenu.newInstance());
        transaction.commit();

        bottomNavigationView.setSelectedItemId(R.id.navigation_menu);

    }

    public void checkout(View view){
        //TODO move to checkout page - build checkout activity, with the selected dishes and total price.

        Fragment menu = customerMenu.newInstance();
        LinearLayout dishes = ((customerMenu) menu).mDishs;
        ArrayList<Integer> dishIdList = new ArrayList<Integer>();
        for(int i=0; i < dishes.getChildCount(); i++){
            CheckBox cb =  ((customerMenu) menu).mDishs.getChildAt(i).findViewById(R.id.checkBox);
            if(cb.isChecked()){
                Integer dishId = ((customerMenu) menu).dishToView.get(i);
                dishIdList.add(dishId);
                Log.e("DISHES",dishId+"");
            }
        }

        if(dishIdList.size() == 0){
            Toast.makeText(this, "Please choose dish.", Toast.LENGTH_LONG).show();
            return;
        }

        Intent intent = new Intent(getApplicationContext(), OrderActivity.class);
        intent.putIntegerArrayListExtra("selectedDishes",dishIdList);
        startActivity(intent);
    }
}
