package com.ecorp.abhamburger;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Spinner;

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
    }

    public void finish(View view){
        //TODO: get all information, check if vaild, and update in firebase

        String pass1 = ((EditText)findViewById(R.id.password)).getText().toString();
        String pass2 = ((EditText)findViewById(R.id.password2)).getText().toString();
        String email = ((EditText)findViewById(R.id.email)).getText().toString();
        String fullName = ((EditText)findViewById(R.id.FullName)).getText().toString();

        if(!checkValid(((EditText)findViewById(R.id.password)))) return;
        if(!checkValid(((EditText)findViewById(R.id.password2)))) return;
        if(!checkValid(((EditText)findViewById(R.id.email)))) return;
        if(!checkValid(((EditText)findViewById(R.id.FullName)))) return;

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
}
