package com.ecorp.abhamburger;
import android.os.Bundle;
import android.support.v4.app.Fragment;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;

public class customerAbout extends Fragment {
    public static customerAbout ca = null;
    public static customerAbout newInstance() {
        if(ca == null)
            ca = new customerAbout();
        return ca;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.customer_about, container, false);
    }
}