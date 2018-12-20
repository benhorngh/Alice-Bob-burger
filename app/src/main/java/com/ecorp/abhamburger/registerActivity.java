package com.ecorp.abhamburger;

import android.app.DatePickerDialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class registerActivity extends AppCompatActivity {

    final String[] roles = { "Customer","Employee", "Manager" };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Spinner dropdown = findViewById(R.id.role);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, roles);
        dropdown.setAdapter(adapter);

        //role spinner listener
        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if(position == 1 || position == 2){ //Employee or manager
                    ((EditText)findViewById(R.id.phoneNum)).setEnabled(false);
                    ((EditText)findViewById(R.id.address)).setEnabled(false);
                    ((EditText)findViewById(R.id.Bday)).setEnabled(false);
                }
                else {
                    ((EditText)findViewById(R.id.phoneNum)).setEnabled(true);
                    ((EditText)findViewById(R.id.address)).setEnabled(true);
                    ((EditText)findViewById(R.id.Bday)).setEnabled(true);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });


        bday= (EditText) findViewById(R.id.Bday);
        setDateAction();


    }


    final DatabaseReference db = FirebaseDatabase.getInstance().getReference();

    /**
     * register new customer
     * @param person the new user
     */
    public void RegisterUser(final Person person, final String type){

        db.child(type).child(person.getEmail().replace(".", "|")).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            Toast.makeText(registerActivity.this, "User already exists", Toast.LENGTH_LONG).show();
                        } else {
                            db.child(type).child(person.getEmail().replace(".", "|")).setValue(person);
                            Toast.makeText(registerActivity.this, "Registration done.", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.e("try to reg", "error! "+ databaseError.toString());
                    }
                }
        );
    }





    /**
     * OnClick for register button
     * @param view
     */
    public void finish(View view){

        String pass1 = ((EditText)findViewById(R.id.password)).getText().toString();
        String pass2 = ((EditText)findViewById(R.id.password2)).getText().toString();
        String email = ((EditText)findViewById(R.id.email)).getText().toString();
        String fName = ((EditText)findViewById(R.id.firstName)).getText().toString();
        String lName = ((EditText)findViewById(R.id.lastName)).getText().toString();
        String address = ((EditText)findViewById(R.id.address)).getText().toString();

        if(!checkValid(((EditText)findViewById(R.id.password)))) return;
        if(!checkValid(((EditText)findViewById(R.id.password2)))) return;
        if(!checkValid(((EditText)findViewById(R.id.email)))) return;
        if(!checkValid(((EditText)findViewById(R.id.firstName)))) return;
        if(!checkValid(((EditText)findViewById(R.id.lastName)))) return;

        if(!pass1.equals(pass2)){
            ((EditText)findViewById(R.id.password2)).setError("Passwords are not the same");
            findViewById(R.id.password2).requestFocus();
            return;
        }

        if(!email.contains("@")){
            ((EditText)findViewById(R.id.email)).setError("invalid");
            findViewById(R.id.email).requestFocus();
            return;
        }


        String phone = ((EditText)findViewById(R.id.phoneNum)).getText().toString();
        String Bday = ((EditText)findViewById(R.id.Bday)).getText().toString();
        String role = ((Spinner)findViewById(R.id.role)).getSelectedItem().toString();

        String myFormat = "dd/MM/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        Date date = null;
        try {
            date = sdf.parse(Bday);
        }catch(Exception e){
            date = new Date();
        }
        Person newPerson = null;
        if(role.equals("Customer")) {
            newPerson = new Customer(fName, lName, email, pass1, date, phone, address);
            RegisterUser(newPerson, "Customer");
        }
        if(role.equals("Employee")) {
            newPerson = new Employee(fName, lName, email, pass1);
            RegisterUser(newPerson, "Employee");
        }
        if(role.equals("Manager")) {
            newPerson = new Manager(fName, lName, email, pass1);
            RegisterUser(newPerson, "Manager");
        }
        finish();
    }
    public boolean checkValid(EditText et){
        String str = et.getText().toString();
        if((str == null) || (str.isEmpty())){
            et.setError("Required");
            et.requestFocus();
            return false;
        }
        return true;
    }





    final Calendar myCalendar = Calendar.getInstance();
    EditText bday= null;
    public void setDateAction(){

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };

        bday.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new DatePickerDialog(registerActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }
    private void updateLabel() {
        String myFormat = "dd/MM/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        bday.setText(sdf.format(myCalendar.getTime()));
    }

}
