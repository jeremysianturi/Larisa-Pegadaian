package id.co.pegadaian.diarium.adapter;

import android.app.Activity;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import id.co.pegadaian.diarium.R;


/**
 * Created by LENOVO on 29/09/2017.
 */

public class ActivityAdapter extends ArrayAdapter<String> {
        private final Activity context;
        private final String[] no;
        private final String[] kegiatan;
        Typeface font,fontbold;


    public ActivityAdapter(Activity context, String[] no, String[] kegiatan) {
        super(context, R.layout.item_list_activity, no);
        // TODO Auto-generated constructor stub

        this.context=context;
        this.no=no;
        this.kegiatan=kegiatan;

        font = Typeface.createFromAsset(context.getAssets(), "fonts/Nexa Light.otf");
        fontbold = Typeface.createFromAsset(context.getAssets(), "fonts/Nexa Bold.otf");
    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.item_list_activity, null,true);

        TextView txtTitle = (TextView) rowView.findViewById(R.id.no);
        txtTitle.setText(no[position]);
        txtTitle.setTypeface(fontbold);

        TextView txtTitle_dua = (TextView) rowView.findViewById(R.id.kegiatan);
        txtTitle_dua.setText(kegiatan[position]);
        txtTitle_dua.setTypeface(fontbold);


        return rowView;

    };


}
