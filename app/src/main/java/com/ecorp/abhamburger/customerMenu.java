package com.ecorp.abhamburger;

import android.app.Application;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseError;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class customerMenu extends Fragment {
    private static customerMenu cm = null;
    public static customerMenu newInstance() {
        if(cm == null)
            cm = new customerMenu();
        return cm;
    }
    LinearLayout mDishs;
    List<Integer> dishToView;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        dishList = new ArrayList<>();
        View view = inflater.inflate(R.layout.customer_menu, container, false);
        mDishs = (LinearLayout) view.findViewById(R.id.dishsLinear);
        dishToView = new ArrayList<Integer>();
        getAllDishes();
        return view;
    }


    public void addDishs(){

        Log.e("Count dishlist" ," "+dishList.size());


        if(dishList.size()>1) {
            addDish(R.drawable.hamburger, dishList.get(0));
            addDish(R.drawable.hotdog, dishList.get(1));
            addDish(R.drawable.schnitzel, dishList.get(2));
            addDish(R.drawable.fries, dishList.get(3));
            addDish(R.drawable.roastbeef, dishList.get(4));
        }

//        List<Ingredient> id1 = new ArrayList<Ingredient>();
//        id1.add(new Ingredient("Meet"));
//        id1.add(new Ingredient("tomato"));
//        Dish d1 = new Dish(1000,"Hamburger", "200gr with chips",id1 ,50);
//        Dish d2 = new Dish(1001,"Hot-dog", "With mustard",null ,29);
//        Dish d3 = new Dish(1003,"Schnitzel", "With golden breadcrumbs",null ,30);
//        Dish d4 = new Dish(1004,"French-Fries", "With ketchup",null ,17);
//        Dish d5 = new Dish(1005,"Roast-beef sandwich", "Extra delicious",null ,42);
//
//        addDish(R.drawable.hamburger, d1);
//        addDish(R.drawable.hotdog, d2);
//        addDish(R.drawable.fries, d3);
//        addDish(R.drawable.schnitzel, d4);
//        addDish(R.drawable.roastbeef, d5);

//        addDishToDatabase(d1);
//        addDishToDatabase(d2);
//        addDishToDatabase(d3);
//        addDishToDatabase(d4);
//        addDishToDatabase(d5);


        //        dishToView.add(d1.id);
    }

    public void addDish(int img, Dish dish){
        dishToView.add(dish.id);
        View dishView = LayoutInflater.from(getActivity().getApplicationContext()).inflate(R.layout.dish, null);
        ((TextView)dishView.findViewById(R.id.dishName)).setText(dish.getName());
        ((TextView)dishView.findViewById(R.id.dishNotes)).setText(dish.getDescription());
        ((TextView)dishView.findViewById(R.id.dishPrice)).setText("$ "+dish.getPrice());
        ImageView im = dishView.findViewById(R.id.image);
        im.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO: activity for full details about the dish
            }
        });
        im.setImageDrawable(getResources().getDrawable(img));
        im.getLayoutParams().width = 400;
        im.getLayoutParams().height = 400;
        im.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        im.setScaleType(ImageView.ScaleType.FIT_CENTER);
        mDishs.addView(dishView);
    }

    List<Dish> dishList;



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






















    /**
     * use manually on new Dishes
     */
    final DatabaseReference db = FirebaseDatabase.getInstance().getReference();
    public void addDishToDatabase(final Dish dish){

        db.child("Dish").child(dish.id+"").addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            Toast.makeText(getActivity().getApplicationContext(), "User already exists", Toast.LENGTH_LONG).show();
                        } else {
                            db.child("Dish").child(dish.id+"").setValue(dish);
                            Toast.makeText(getActivity().getApplicationContext(), "ADDed.", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                }
        );
    }



}