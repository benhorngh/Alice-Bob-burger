package com.ecorp.abhamburger;

import android.app.DatePickerDialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class registerActivity extends AppCompatActivity {

    final String[] roles = { "Customer","Worker", "Manager" };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Spinner dropdown = findViewById(R.id.role);
//create a list of items for the spinner.
//create an adapter to describe how the items are displayed, adapters are used in several places in android.
//There are multiple variations of this, but this is the basic variant.
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, roles);
//set the spinners adapter to the previously created one.
        dropdown.setAdapter(adapter);

        bday= (EditText) findViewById(R.id.Bday);
        setDateAction();

    }






    /**
     * OnClick for register button
     * @param view
     */
    public void finish(View view){
        //TODO: get all information, check if vaild, and update in firebase

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
        Customer newCustomer = null;
        if(role.equals("Customer"))
             newCustomer = new Customer(fName,lName, date, email, phone, address);

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
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };

        bday.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
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
