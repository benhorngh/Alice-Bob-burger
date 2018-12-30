package com.ecorp.abhamburger;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
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

public class managerMenu {

     List<Dish> dishList = null;
     Context context;
     LinearLayout layout = null;

    private static managerMenu instance = null;

    public static managerMenu getInstance(){
        if(instance == null)
            instance = new managerMenu();
        return instance;
    }

    public void setAllDishs(View view, Context mcontext){
        layout = (LinearLayout) view.findViewById(R.id.menulist);
        context = mcontext;
        if(dishList == null){
            dishList = new ArrayList<Dish>();
        }
        getAllDishes();
    }


    public void getAllDishes(){

        final DatabaseReference db = FirebaseDatabase.getInstance().getReference();
        db.child("Dish").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Log.e("Count " ,""+snapshot.getChildrenCount());
                for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                    dishList.add(postSnapshot.getValue(Dish.class));
                    Log.e("add " ,""+1);
                }
                addDishs();
            }
            @Override
            public void onCancelled(DatabaseError firebaseError) {
                Log.e("The read failed: " ,firebaseError.getMessage());
            }
        });


    }

    public void addDishs() {

        addDish(R.drawable.hamburger, dishList.get(0));
        addDish(R.drawable.hotdog, dishList.get(1));
        addDish(R.drawable.schnitzel, dishList.get(2));
        addDish(R.drawable.fries, dishList.get(3));
        addDish(R.drawable.roastbeef, dishList.get(4));

    }

    public void addDish(int img, Dish dish){
        View dishView = LayoutInflater.from(context).inflate(R.layout.manager_dish, null);
        ((EditText)dishView.findViewById(R.id.dishName)).setText(dish.getName());
        ((EditText)dishView.findViewById(R.id.dishNotes)).setText(dish.getDescription());
        ((EditText)dishView.findViewById(R.id.dishPrice)).setText(""+dish.getPrice());
        ImageView im = dishView.findViewById(R.id.image);
        ((ImageButton)dishView.findViewById(R.id.submit)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateOnDatabase(view);
            }
        });

        im.setImageDrawable(context.getResources().getDrawable(img));
        im.getLayoutParams().width = 250;
        im.getLayoutParams().height = 250;
        im.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        im.setScaleType(ImageView.ScaleType.FIT_CENTER);
        layout.addView(dishView);
    }

    private void updateOnDatabase(View view) {
        FrameLayout fl = (FrameLayout) view.getParent().getParent();
        int childIndex = layout.indexOfChild(fl) -1;
        Dish changedDish = dishList.get(childIndex);
        changedDish.setName(((EditText)fl.findViewById(R.id.dishName)).getText().toString());
        changedDish.setDescription(((EditText)fl.findViewById(R.id.dishNotes)).getText().toString());
        try {
            changedDish.setPrice(Double.parseDouble(((EditText) fl.findViewById(R.id.dishPrice)).getText().toString()));
            upload(changedDish);
        }catch (Exception e){((EditText) fl.findViewById(R.id.dishPrice)).setError("Invalid price");}
    }


    public void upload(final Dish dish){
        final DatabaseReference db = FirebaseDatabase.getInstance().getReference();

        db.child("Dish").child(dish.id+"").addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            db.child("Dish").child(dish.id+"").setValue(dish);
                            Toast.makeText(context, "Changed.", Toast.LENGTH_LONG).show();
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                }
        );
    }



}
