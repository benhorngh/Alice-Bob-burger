package com.ecorp.abhamburger;
import android.os.Bundle;
import android.support.v4.app.Fragment;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
import android.widget.ImageView;

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
        View view = inflater.inflate(R.layout.customer_about, container, false);
        setImage(view);
        return view;
    }
    public void setImage(View view){
        ImageView im = view.findViewById(R.id.image);
        im.setImageDrawable(getResources().getDrawable(R.drawable.restpic));
        im.getLayoutParams().width = 400;
        im.getLayoutParams().height = 400;
        im.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        im.setScaleType(ImageView.ScaleType.FIT_CENTER);
    }
}