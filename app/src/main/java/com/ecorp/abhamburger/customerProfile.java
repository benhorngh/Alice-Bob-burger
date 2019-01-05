package com.ecorp.abhamburger;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class customerProfile extends Fragment {
    public static customerProfile cp = null;
    public static customerProfile newInstance() {
        if(cp == null)
            cp = new customerProfile();
        return cp;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.cusomer_profile, container, false);
        buildPage(v);
        return v;
    }

    void buildPage(View layout){
        /*in order to call findViewById you need to use layout.:
        textview = findViewById() - not working
        textview = layout.findViewById() - working

        Love you XOXO
         */

    }
}


