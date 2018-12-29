package com.ecorp.abhamburger;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class OrderActivity extends AppCompatActivity {

    static Order order = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        boolean exits = checkIfOrderAppend();
        ArrayList<Integer> dishIdList = null;
        if(exits == false){
            Intent intent = getIntent();
            dishIdList = intent.getIntegerArrayListExtra("selectedDishes");
            buildOrder(dishIdList);
        }

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

    public boolean checkIfOrderAppend(){

        final String customerOrder = ((Customer)AuthenticatedUserHolder.instance.getAppUser()).getOrderId();
        if( customerOrder == null || customerOrder.isEmpty())
            return false;
        else{
            Log.e("ORDER","FOUND");
            final DatabaseReference db = FirebaseDatabase.getInstance().getReference();
            db.child("Order").child(customerOrder).addListenerForSingleValueEvent(
                    new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            order = dataSnapshot.getValue(Order.class);
                            ArrayList<Integer> dishIdList = (ArrayList) order.getDishesId();
                            buildOrder(dishIdList);
                            updateOrder();

                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                        }
                    }
            );

        }
        return true;

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
                deliver,
                "Sent"
                );

        final DatabaseReference db = FirebaseDatabase.getInstance().getReference();
        String key = db.child("Order").push().getKey();
        order.setOrderID(key);
        db.child("Order").child(key).setValue(order);

        ((Customer)AuthenticatedUserHolder.instance.getAppUser()).setOrderId(key);
        db.child("Customer").child(AuthenticatedUserHolder.instance.getAppUser().getEmail().replace(".", "|")).child("orderId").setValue(key);

        updateOrder();
    }

    void updateOrder(){
        //TODO: update layout after the current Order

        findViewById(R.id.submit).setVisibility(View.GONE);
        ((TextView)findViewById(R.id.status)).setText("status:");
        ((TextView)findViewById(R.id.updatesStatus)).setText("Sent.");
        ((TextView)findViewById(R.id.orderId)).setText(order.getOrderID());
        ((EditText)findViewById(R.id.notes)).setText(order.getNotes());
        ((EditText)findViewById(R.id.notes)).setEnabled(false);
        ((Switch)findViewById(R.id.delivery)).setEnabled(false);



        final DatabaseReference db = FirebaseDatabase.getInstance().getReference();
        db.child("Order").child(order.getOrderID()).child("status").addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            String status  = dataSnapshot.getValue(String.class);
                            ((TextView)findViewById(R.id.updatesStatus)).setText(status);
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                }
        );
    }

}
