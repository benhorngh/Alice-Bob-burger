package com.ecorp.abhamburger;

import android.content.Context;
import android.content.Intent;
import android.view.View;

public class statistics {
    Context context;
    View layout = null;

    private static statistics instance = null;

    public static statistics getInstance(){
        if(instance == null)
            instance = new statistics();
        return instance;
    }

    void setStatistics(View page, final Context context){

        this.context = context;

        page.findViewById(R.id.dishChart).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, pieChart.class);
                context.startActivity(intent);
            }
        });
    }





}
