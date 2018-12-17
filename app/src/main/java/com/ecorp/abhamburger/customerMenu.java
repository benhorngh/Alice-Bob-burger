package com.ecorp.abhamburger;

import android.app.Application;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

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

        addDish(R.drawable.hamburger, "","",0);
        addDish(R.drawable.hotdog,"","",0);

    }

    public void addDish(int img, String name, String note, int price){
        View dish = LayoutInflater.from(getActivity().getApplicationContext()).inflate(R.layout.dish, null);
        ImageView im = dish.findViewById(R.id.image);
        im.setImageDrawable(getResources().getDrawable(img));
        im.getLayoutParams().width = 400;
        im.getLayoutParams().height = 400;
        im.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        im.setScaleType(ImageView.ScaleType.FIT_CENTER);
        mDishs.addView(dish);
    }

}