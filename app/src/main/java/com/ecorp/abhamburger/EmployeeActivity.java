package com.ecorp.abhamburger;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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

        SalaryListener();
        TypeListener();
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
            LayoutInflater inflater = (LayoutInflater)this.getSystemService(LAYOUT_INFLATER_SERVICE);
            profilePage = inflater.inflate(R.layout.employee_profile,
                    (ViewGroup) findViewById(R.id.employeeProfile));
        }
        employeeOrders.getInstance().setAllOrders(ordersPage, this);
        buildProfile();

    }

    /**
     * build the profile page
     */
    protected void buildProfile(){
        Employee e=(Employee) AuthenticatedUserHolder.instance.getAppUser();
        ((TextView)profilePage.findViewById(R.id.fname_tx)).setText(e.firstName);
        ((TextView)profilePage.findViewById(R.id.fname1_tx)).setText("first name: ");
        ((TextView)profilePage.findViewById(R.id.lname1_tx)).setText("last name: ");
        ((TextView)profilePage.findViewById(R.id.lname_tx)).setText(e.lastName);
        ((TextView)profilePage.findViewById(R.id.mail_tx)).setText(e.email);
        ((TextView)profilePage.findViewById(R.id.mail1_tx)).setText("Email: ");
        ((TextView)profilePage.findViewById(R.id.type1_tx)).setText("type: ");
        ((TextView)profilePage.findViewById(R.id.type_tx)).setText(e.type);
        ((TextView)profilePage.findViewById(R.id.salary_tx)).setText(e.salary+"$");
        ((TextView)profilePage.findViewById(R.id.salary1_tx)).setText("salary: ");
    }

    boolean openingS = true;
    void SalaryListener(){

        String key = AuthenticatedUserHolder.instance.getAppUser().getEmail().replace(".", "|");
        final DatabaseReference db = FirebaseDatabase.getInstance().getReference();

        db.child("Employee").child(key).child("salary").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(openingS) {
                    openingS= false;
                    return;
                }

                throwNotification(dataSnapshot.getValue().toString());
                int newIntSalry = Integer.parseInt(dataSnapshot.getValue().toString());
                ((Employee)AuthenticatedUserHolder.instance.getAppUser()).salary = newIntSalry;
                buildProfile();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    boolean openingT = true;
    void TypeListener(){

        String key = AuthenticatedUserHolder.instance.getAppUser().getEmail().replace(".", "|");
        final DatabaseReference db = FirebaseDatabase.getInstance().getReference();

        db.child("Employee").child(key).child("type").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(openingT) {
                    openingT= false;
                    return;
                }
                ((Employee)AuthenticatedUserHolder.instance.getAppUser()).type = dataSnapshot.getValue().toString();
                buildProfile();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }





    public void throwNotification(String newSalary){
        int newIntSalry = Integer.parseInt(newSalary);
        createNotificationChannel();
        NotificationCompat.Builder mBuilder = null;

            mBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setSmallIcon(R.mipmap.ic_launcher_icon)
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(),
                            R.mipmap.ic_launcher_icon))
                    .setContentTitle("You got a raise!")
                    .setContentText("your new salary: " + newSalary)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setAutoCancel(true);


        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(notificationId++, mBuilder.build());
    }


    static int notificationId=0;

    final String CHANNEL_ID = "SALARY";

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
