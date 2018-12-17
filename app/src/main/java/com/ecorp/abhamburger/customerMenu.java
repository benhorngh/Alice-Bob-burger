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

public class customerMenu extends Fragment {
    private static customerMenu cm = null;
    public static customerMenu newInstance() {
        if(cm == null)
            cm = new customerMenu();
        return cm;
    }
    LinearLayout mDishs;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.customer_menu, container, false);
        mDishs = (LinearLayout) view.findViewById(R.id.dishsLinear);
        addDishs();
        return view;
    }

    public void addDishs(){

        addDish(R.drawable.hamburger, "Hambur","with chips",60);
        addDish(R.drawable.hotdog,"hot dog","drink not included",50.5);

    }

    public void addDish(int img, String name, String note, double price){
        View dish = LayoutInflater.from(getActivity().getApplicationContext()).inflate(R.layout.dish, null);
        ((TextView)dish.findViewById(R.id.dishName)).setText(name);
        ((TextView)dish.findViewById(R.id.dishNotes)).setText(note);
        ((TextView)dish.findViewById(R.id.dishPrice)).setText("$ "+price);
        ImageView im = dish.findViewById(R.id.image);
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
        mDishs.addView(dish);
    }


}