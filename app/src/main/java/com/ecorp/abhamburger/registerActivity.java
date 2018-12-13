package com.ecorp.abhamburger;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
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

        finish();
    }
}
