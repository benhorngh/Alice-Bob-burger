package com.ecorp.abhamburger;

import android.app.Application;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

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
        View view = inflater.inflate(R.layout.customer_menu, container, false);
        mDishs = (LinearLayout) view.findViewById(R.id.dishsLinear);
        dishToView = new ArrayList<Integer>();
        addDishs();
        return view;
    }


    public void addDishs(){
//        (String name, String description, List<Ingredient> ingredients, double price)
        Dish d1 = new Dish("Hamburger", "200gr with chips",null ,50);
        Dish d2 = new Dish("Hot-dog", "with ketchup",null ,15);

        addDish(R.drawable.hamburger, d1);
        dishToView.add(d1.id);

        addDish(R.drawable.hotdog, d2);

    }

    public void addDish(int img, Dish dish){
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



}