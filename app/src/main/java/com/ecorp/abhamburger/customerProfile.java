package com.ecorp.abhamburger;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

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
        Customer c = (Customer) AuthenticatedUserHolder.instance.getAppUser();
        ((TextView)layout.findViewById(R.id.name_tx)).setText(c.firstName + " " + c.lastName);
        ((TextView)layout.findViewById(R.id.phone_tx)).setText(c.phone);
        ((TextView)layout.findViewById(R.id.email_tx)).setText(c.email);
        ((TextView)layout.findViewById(R.id.address_tx)).setText(c.address);

        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        ((TextView)layout.findViewById(R.id.bday_tx)).setText(df.format(c.birthday));

        /*in order to call findViewById you need to use layout.:
        textview = findViewById() - not working
        textview = layout.findViewById() - working

        Love you XOXO
         */
    }
}


