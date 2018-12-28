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
    List<Dish> dishList;



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



    public int getImg(Dish dish){
        if(dish.getName().equals("Hamburger"))
            return R.drawable.hamburger;
        if(dish.getName().equals("Hot-dog"))
            return R.drawable.hotdog;
        if(dish.getName().equals("Schnitzel"))
            return R.drawable.schnitzel;
        if(dish.getName().equals("French-Fries"))
            return R.drawable.fries;
        if(dish.getName().equals("Roast-beef sandwich"))
            return R.drawable.roastbeef;

        return 0 ;
    }


    public void addDishs(){

        Log.e("Count dishlist" ," "+dishList.size());

        for(int i=0; i<dishList.size(); i++){
            View nv = dishToView(dishList.get(i));
            mDishs.addView(nv);
            dishToView.add(dishList.get(i).id);
        }

    }

    public View dishToView(Dish dish){
        int img = getImg(dish);
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
        im.getLayoutParams().width = 250;
        im.getLayoutParams().height = 250;
        im.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        im.setScaleType(ImageView.ScaleType.FIT_CENTER);
        return dishView;
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