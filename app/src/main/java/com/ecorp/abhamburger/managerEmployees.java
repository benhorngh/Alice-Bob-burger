package com.ecorp.abhamburger;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;

import java.util.List;

public class managerEmployees {

    List<Order> employeeList = null;
    Context context;
    LinearLayout layout = null;

    private static managerEmployees instance = null;

    public static managerEmployees getInstance(){
        if(instance == null)
            instance = new managerEmployees();
        return instance;
    }

    private managerEmployees(){

    }

    void setAllEmployees(View parent, Context context){
        layout = parent.findViewById(R.id.employee_list);
        this.context = context;
    }



}
