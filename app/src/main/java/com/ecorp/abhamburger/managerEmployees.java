package com.ecorp.abhamburger;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
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

public class managerEmployees {

    List<Employee> employeeList = null;
    Context context;
    LinearLayout layout = null;

    private static managerEmployees instance = null;

    public static managerEmployees getInstance(){
        if(instance == null)
            instance = new managerEmployees();
        return instance;
    }

    private managerEmployees(){}

    void setAllEmployees(View parent, Context context){
        layout = parent.findViewById(R.id.employee_list);
        this.context = context;
        employeeList = new ArrayList<>();
        getAllEmployees();
    }

    public void getAllEmployees(){

        final DatabaseReference db = FirebaseDatabase.getInstance().getReference();
        db.child("Employee").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Log.e("Count " ,""+snapshot.getChildrenCount());
                for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                    employeeList.add(postSnapshot.getValue(Employee.class));
                    Log.e("add " ,""+1);
                }
                addEmployees();
            }
            @Override
            public void onCancelled(DatabaseError firebaseError) {
                Log.e("The read failed: " ,firebaseError.getMessage());
            }
        });
    }
    void addEmployees(){
        for (Employee e : employeeList)
            addEmployee(e);
    }
    void addEmployee(Employee employee){
        View empView = LayoutInflater.from(context).inflate(R.layout.manager_employee, null);
        ((TextView)empView.findViewById(R.id.name)).setText(employee.firstName + " " + employee.lastName);
        ((EditText)empView.findViewById(R.id.type)).setText(employee.type);
        ((EditText)empView.findViewById(R.id.salary)).setText(employee.salary+"");
        ((TextView)empView.findViewById(R.id.actions)).setText("Actions: "+employee.actions+"");

        ((ImageButton) empView.findViewById(R.id.submit)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateEmployee((View)v.getParent().getParent().getParent());
            }
        });

        layout.addView(empView);
    }

    void updateEmployee(View parent){
        String type = ((EditText)parent.findViewById(R.id.type)).getText().toString();
        String slry = ((EditText)parent.findViewById(R.id.salary)).getText().toString();
        double salary = 0;
        try {
            salary = Double.parseDouble(slry);
        }catch (Exception e){return;}

        final DatabaseReference db = FirebaseDatabase.getInstance().getReference();
        String key = employeeList.get(layout.indexOfChild(parent)).email.replace(".", "|");


        db.child("Employee").child(key).child("salary").setValue(salary);
        db.child("Employee").child(key).child("type").setValue(type);

        Toast.makeText(context, "Changed.", Toast.LENGTH_LONG).show();

    }



}
