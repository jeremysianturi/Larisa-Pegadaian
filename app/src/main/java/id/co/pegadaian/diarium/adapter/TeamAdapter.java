package id.co.pegadaian.diarium.adapter;

import android.app.Activity;
import android.app.Dialog;
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

public class TeamAdapter extends ArrayAdapter<String> {
    private final Activity context;
    private final String[] name;
    private final String[] nik;
    private final String[] jabatan;
    Typeface font,fontbold;

    Dialog myDialog;


    public TeamAdapter(Activity context, String[] name, String[] nik,String[] jabatan) {
        super(context, R.layout.item_list_team, name);
        // TODO Auto-generated constructor stub

        this.context=context;
        this.name=name;
        this.nik=nik;
        this.jabatan=jabatan;

        font = Typeface.createFromAsset(context.getAssets(), "fonts/Nexa Light.otf");
        fontbold = Typeface.createFromAsset(context.getAssets(), "fonts/Nexa Bold.otf");

        myDialog = new Dialog(context);
    }

    public View getView(int position, final View view, ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.item_list_team, null,true);

        TextView namaa = (TextView) rowView.findViewById(R.id.tvTitle);
        namaa.setText(name[position]);
        namaa.setTypeface(fontbold);

        TextView nikk= (TextView) rowView.findViewById(R.id.nik);
        nikk.setText(nik[position]);
        nikk.setTypeface(font);

        TextView jabaatan = (TextView) rowView.findViewById(R.id.tvTitle);
        jabaatan.setText(jabatan[position]);
        jabaatan.setTypeface(fontbold);




        return rowView;

    };




}
