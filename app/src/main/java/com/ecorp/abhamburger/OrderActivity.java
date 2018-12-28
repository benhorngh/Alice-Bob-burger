package com.ecorp.abhamburger;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class OrderActivity extends AppCompatActivity {

    static Order order = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);


        Intent intent = getIntent();
        ArrayList<Integer> dishIdList = intent.getIntegerArrayListExtra("selectedDishes");
        buildOrder(dishIdList);

        if(order != null)
            updateOrder();
    }

    public void buildOrder(ArrayList<Integer> dishIdList){
        double total =0;
        for(Integer id : dishIdList){
            Dish dish = null;
            for(int i = 0;  i < customerMenu.newInstance().dishList.size(); i++){
                if(customerMenu.newInstance().dishList.get(i).id == id){
                    dish = customerMenu.newInstance().dishList.get(i);
                    break;
                }
            }
            if(dish == null) continue;
            total += dish.getPrice();
            View v = customerMenu.newInstance().dishToView(dish);
            ((CheckBox)v.findViewById(R.id.checkBox)).setVisibility(View.GONE);
            LinearLayout dishes = findViewById(R.id.selectedDishes);
            dishes.addView(v);
        }
        ((TextView)findViewById(R.id.total)).setText("Total: $ "+total);
    }

    public void submitOrder(View view){
        //TODO: built new Order object, update on firebase and set the static order var
        EditText notes = findViewById(R.id.notes);
        String tstr = ((TextView)findViewById(R.id.total)).getText().toString();
        tstr = tstr.substring(tstr.indexOf("$")+1);
        double total = 0;
        try{
            total = Double.parseDouble(tstr);
        }catch (Exception e){}
        boolean deliver = ((Switch) findViewById(R.id.delivery)).isChecked();

        order = new Order(AuthenticatedUserHolder.instance.getAppUser().getEmail(),
                getIntent().getIntegerArrayListExtra("selectedDishes"),
                notes.getText().toString(),
                total,
                deliver
                );

        final DatabaseReference db = FirebaseDatabase.getInstance().getReference();
        String key = db.child("Order").push().getKey();
        order.setOrderID(key);
        db.child("Order").child(key).setValue(order);
        updateOrder();
    }

    void updateOrder(){
        //TODO: update layout after the current Order
    }

}
