package id.co.pegadaian.diarium.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import id.co.pegadaian.diarium.R;


/**
 * Created by LENOVO on 29/09/2017.
 */

public class BookingecornerAdapter extends ArrayAdapter<String> {
    private final Activity context;
    private final String[] startdate;
    private final String[] enddate;
//    private final int img_corner;

    Typeface font,fontbold;

    Dialog myDialog;


    public BookingecornerAdapter(Activity context, String[] startdate, String[] enddate) {
        super(context, R.layout.item_list_booking, startdate);
        // TODO Auto-generated constructor stub

        this.context=context;
        this.startdate=startdate;
        this.enddate=enddate;
//        this.img_corner=img_corner;

        font = Typeface.createFromAsset(context.getAssets(), "fonts/Nexa Light.otf");
        fontbold = Typeface.createFromAsset(context.getAssets(), "fonts/Nexa Bold.otf");




    }

    public View getView(int position, final View view, ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.item_list_booking, null,true);

//        TextView txtTitle = (TextView) rowView.findViewById(R.id.datestart);
//        txtTitle.setText(startdate[position]);
//        txtTitle.setTypeface(font);
//        TextView txtTitle2 = (TextView) rowView.findViewById(R.id.dateend);
//        txtTitle2.setText(enddate[position]);
//        txtTitle2.setTypeface(font);
//        TextView jdl = (TextView) rowView.findViewById(R.id.judul);
//        jdl.setTypeface(fontbold);
//        TextView strdate = (TextView) rowView.findViewById(R.id.startdate);
//        strdate.setTypeface(fontbold);
//        TextView endate = (TextView) rowView.findViewById(R.id.enddate);
//        endate .setTypeface(fontbold);
//        TextView datee = (TextView) rowView.findViewById(R.id.date);
//        datee .setTypeface(fontbold);
//        TextView kpts = (TextView) rowView.findViewById(R.id.kapasitas);
//        kpts.setTypeface(fontbold);
//        Button btn = (Button) rowView.findViewById(R.id.btn_book);
//        btn.setTypeface(fontbold);
//
//
//





        return rowView;

    };



}
