package com.ecorp.abhamburger;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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
                               findViewById(R.id.fab).setVisibility(View.GONE);
                                break;
                            case R.id.navigation_menu:
                                selectedFragment = customerMenu.newInstance();
                                findViewById(R.id.fab).setVisibility(View.VISIBLE);
                                break;
                            case R.id.navigation_about:
                                selectedFragment = customerAbout.newInstance();
                                findViewById(R.id.fab).setVisibility(View.GONE);
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

        String orderid = ((Customer)AuthenticatedUserHolder.instance.getAppUser()).orderId;
        if(orderid != null &&
                !orderid.isEmpty() )
            setStatusListener(orderid);

    }



    public void checkout(View view){

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
        String orderid = ((Customer)AuthenticatedUserHolder.instance.getAppUser()).getOrderId();
        if(orderid != null && !orderid.isEmpty()){
            Toast.makeText(this, "You have an active order!", Toast.LENGTH_LONG).show();
        }

        else if(dishIdList.size() == 0){
            Toast.makeText(this, "Please choose dish.", Toast.LENGTH_LONG).show();
            return;
        }


        Intent orderActivity = new Intent(getApplicationContext(), OrderActivity.class);
        orderActivity.putIntegerArrayListExtra("selectedDishes",dishIdList);
        ca = this;
        startActivity(orderActivity);
    }
    static customerActivity ca;








    public void setStatusListener(String orderId) {
        final DatabaseReference db = FirebaseDatabase.getInstance().getReference();
        db.child("Order").child(orderId).child("status").addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            String status  = dataSnapshot.getValue(String.class);
                            throwNotification(status);


                            OrderActivity.updateStatus(status);

                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                }
        );
    }

    public void throwNotification(String status){
        createNotificationChannel();
//        Intent orderActivity = new Intent(getApplicationContext(), OrderActivity.class);
////        Intent intent = new Intent(this, OrderActivity.class);
//        orderActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, orderActivity, 0);
        NotificationCompat.Builder mBuilder = null;
        if(status.equals("Done")){
            mBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle("New update!")
                    .setContentText("your order is ready! : " + status)
//                .setContentIntent(pendingIntent)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setAutoCancel(true);

            final DatabaseReference db = FirebaseDatabase.getInstance().getReference();
            String customerId = AuthenticatedUserHolder.instance.getAppUser().getEmail().replace(".", "|");
            db.child("Customer").child(customerId).child("orderId").setValue(null);
            ((Customer)AuthenticatedUserHolder.instance.getAppUser()).orderId = null;


        }else {
            mBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle("New update!")
                    .setContentText("your order new status: " + status)
//                .setContentIntent(pendingIntent)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setAutoCancel(true);
        }

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(notificationId++, mBuilder.build());
    }


    static int notificationId=0;

    final String CHANNEL_ID = "ORDERS";

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_order_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }



}
