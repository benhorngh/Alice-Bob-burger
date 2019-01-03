package com.ecorp.abhamburger;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
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
import java.util.Date;

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

        ref = this;
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
            View v = customerMenu.newInstance().dishToView(dish, this);
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
                ,new Date() //current time/ date.
                );

        final DatabaseReference db = FirebaseDatabase.getInstance().getReference();
        String key = db.child("Order").push().getKey();
        order.setOrderID(key);
        db.child("Order").child(key).setValue(order);

        ((Customer)AuthenticatedUserHolder.instance.getAppUser()).setOrderId(key);
        db.child("Customer").child(AuthenticatedUserHolder.instance.getAppUser().getEmail().replace(".", "|")).child("orderId").setValue(key);

        customerActivity.ca.setStatusListener(key);

        updateOrder();
    }

    /**
     * update layout after the current Order
     */
    void updateOrder(){
        findViewById(R.id.submit).setVisibility(View.GONE);
        ((TextView)findViewById(R.id.status)).setText("status:");
        ((TextView)findViewById(R.id.orderId)).setText("Order id: "+order.getOrderID());
        ((EditText)findViewById(R.id.notes)).setText(order.getNotes());
        ((TextView)findViewById(R.id.updatesStatus)).setText(order.getStatus());
        ((EditText)findViewById(R.id.notes)).setEnabled(false);
        ((Switch)findViewById(R.id.delivery)).setEnabled(false);
        if(order.isDelivery())
            ((Switch)findViewById(R.id.delivery)).setChecked(true);
        else ((Switch)findViewById(R.id.delivery)).setChecked(false);


    }

    public static OrderActivity ref = null;
    public static void updateStatus(String status){
        if(order == null) return;
        order.setStatus(status);
        if(ref == null) return;
        ((TextView)ref.findViewById(R.id.updatesStatus)).setText(order.getStatus());

        if(order.getStatus().equals("Done")){
            Toast.makeText(ref, "Your order is done! exit...", Toast.LENGTH_LONG).show();
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {

                    try{
                        Thread.sleep(7000);
                        ref.finish();
                    }catch (Exception e){}
                }
            });
            t.start();

        }
    }


}
