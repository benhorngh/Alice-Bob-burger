package com.ecorp.abhamburger;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class employeeOrders {

    List<Order> orderList = null;
    Context context;
    LinearLayout layout = null;

    private static employeeOrders instance = null;

    public static employeeOrders getInstance(){
        if(instance == null)
            instance = new employeeOrders();
        return instance;
    }

    private employeeOrders(){}

    public void setAllOrders(View view, Context context){
        getAllDishes();
        getAllCustomers();

        Log.e("ORDERS11","HEre1");
        layout = view.findViewById(R.id.employeeOrders).findViewById(R.id.orderList);
        this.context = context;
        if(orderList == null)
            orderList = new ArrayList<Order>();

        getAllOrders();
    }

    public void getAllOrders() {
        final DatabaseReference db = FirebaseDatabase.getInstance().getReference();

        db.child("Order").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Log.e("Count ", "" + snapshot.getChildrenCount());
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    Log.e("ORDERS11 ", "" + 1);
                    orderList.add(postSnapshot.getValue(Order.class));
                }
                addOrders();
            }

            @Override
            public void onCancelled(DatabaseError firebaseError) {
                Log.e("The read failed: ", firebaseError.getMessage());
            }
        });
    }

    public void addOrders(){
        Log.e("ORDERS11", orderList.size()+" :SIZE");
        for(Order order: orderList){
            if(!order.getStatus().equals("Done"))
                addOrder(order);
        }
    }

    public void addOrder(Order order){

        DateFormat df = new SimpleDateFormat("HH:mm:ss  dd/MM/yyyy");


        Log.e("ORDERS11","PLUS ONE!");
        View orderView = LayoutInflater.from(context).inflate(R.layout.employee_order, null);
        ((TextView)orderView.findViewById(R.id.fname)).setText("Customer name");
        ((TextView)orderView.findViewById(R.id.orderId)).setText(order.getOrderID());
        if(order.getTime() != null)
            ((TextView)orderView.findViewById(R.id.time)).setText(df.format(order.getTime())); //fix
        ((TextView)orderView.findViewById(R.id.notes)).setText(order.getNotes());
        ((EditText)orderView.findViewById(R.id.status)).setText(order.getStatus());

        ((CheckBox)orderView.findViewById(R.id.delivery)).setChecked(order.isDelivery());
        orderView.findViewById(R.id.delivery).setEnabled(false);

        final Order o2 = order;
        ((ImageButton)orderView.findViewById(R.id.imageButton)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newStatus = ((EditText)((View) v.getParent()).findViewById(R.id.status)).getText().toString();
                if(newStatus == null || newStatus.isEmpty()) return;
                updateStatus(o2, newStatus, (View)v.getParent().getParent());
            }
        });

        setCustomerName(order.getCustomerId(), orderView);
        setDishName(order.getDishesId(), orderView);
        layout.addView(orderView);
    }

    public void updateStatus(Order order, String newStatus, View parent){
        final DatabaseReference db = FirebaseDatabase.getInstance().getReference();
        db.child("Order").child(order.getOrderID()).child("status").setValue(newStatus);
        if(newStatus.equals("Done")){
            layout.removeView(parent);
            orderList.remove(order);
        }
        Toast.makeText(context, "Changed.", Toast.LENGTH_LONG).show();


        String key =AuthenticatedUserHolder.instance.getAppUser().getEmail().replace(".", "|");
        int actions = ((Employee)AuthenticatedUserHolder.instance.getAppUser()).actions;
        ((Employee)AuthenticatedUserHolder.instance.getAppUser()).actions++;
        db.child("Employee").child(key).child("actions").setValue((actions+1));

    }



    List<Dish> dishList = null;
    public void getAllDishes() {
        dishList = new ArrayList<>();
        final DatabaseReference db = FirebaseDatabase.getInstance().getReference();
        db.child("Dish").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Log.e("Count ", "" + snapshot.getChildrenCount());
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    dishList.add(postSnapshot.getValue(Dish.class));
                    Log.e("add ", "" + 1);
                }
            }

            @Override
            public void onCancelled(DatabaseError firebaseError) {
                Log.e("The read failed: ", firebaseError.getMessage());
            }
        });
    }
    public void setCustomerName(String customerId ,View parent) {
        for (Customer c : customerList)
            if (c.getEmail().equals(customerId)) {
                if(c.address != null)
                    ((TextView) parent.findViewById(R.id.fname)).setText(c.firstName + " " + c.lastName);
                ((TextView) parent.findViewById(R.id.address)).setText(c.address);
                return;
            }
    }


    public void setDishName(List<Integer> dishIds, View parent){
        for(Integer id : dishIds) {

            TextView valueTV = new TextView(context);
            String name = "not found";
            for(int i=0; i<dishList.size(); i++) {
                if (dishList.get(i).id == id) {
                    name = dishList.get(i).getName();
                    break;
                }
            }
            valueTV.setText(" - "+name);
            valueTV.setTextColor(Color.BLACK);
            valueTV.setBackgroundColor(Color.TRANSPARENT);
            valueTV.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT
                    ,LinearLayout.LayoutParams.WRAP_CONTENT));
            ((LinearLayout) parent.findViewById(R.id.dishesList)).addView(valueTV);

        }
    }


    List<Customer> customerList = null;
    public void getAllCustomers() {
        customerList = new ArrayList<>();
        final DatabaseReference db = FirebaseDatabase.getInstance().getReference();
        db.child("Customer").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Log.e("Count ", "" + snapshot.getChildrenCount());
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    customerList.add(postSnapshot.getValue(Customer.class));
                    Log.e("add ", "" + 1);
                }
            }

            @Override
            public void onCancelled(DatabaseError firebaseError) {
                Log.e("The read failed: ", firebaseError.getMessage());
            }
        });
    }

}
