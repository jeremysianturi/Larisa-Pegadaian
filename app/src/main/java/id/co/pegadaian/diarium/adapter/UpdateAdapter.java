package id.co.pegadaian.diarium.adapter;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import id.co.pegadaian.diarium.R;
import id.co.pegadaian.diarium.controller.profile.DetailpostActivity;


public class UpdateAdapter extends  ArrayAdapter<String> {

    private final Activity context;
    private final String[] nama;
    private final String[] jabatan;
    private final String[] day;
    private final String[] konten;
    private final String[] like;
    private final String[] komen;
    LayoutInflater layoutInflater;
    View view;
    Typeface font,fontbold;


    public UpdateAdapter(Activity context, String[] nama, String[] jabatan, String[] day, String[] konten, String[] like, String[] komen) {
        super(context, R.layout.item_list_post, nama);

        // TODO Auto-generated constructor stub

        this.context=context;
        this.nama=nama;
        this.jabatan=jabatan;
        this.day=day;
        this.konten=konten;
        this.like=like;
        this.komen=komen;
        font = Typeface.createFromAsset(context.getAssets(), "fonts/Nexa Light.otf");
        fontbold = Typeface.createFromAsset(context.getAssets(), "fonts/Nexa Bold.otf");
    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.item_list_post, null,true);

        TextView txtTitle = (TextView) rowView.findViewById(R.id.tvTitle);
        txtTitle.setText(nama[position]);
        txtTitle.setTypeface(fontbold);
        TextView txtTitle_dua = (TextView) rowView.findViewById(R.id.tvTitle);
        txtTitle_dua.setText(jabatan[position]);
        txtTitle_dua.setTypeface(fontbold);
        TextView txtTitle_tiga = (TextView) rowView.findViewById(R.id.tvDate);
        txtTitle_tiga.setText(day[position]);
        txtTitle_tiga.setTypeface(font);
        TextView txtTitle_empat = (TextView) rowView.findViewById(R.id.tvDate);
        txtTitle_empat.setText(konten[position]);
        txtTitle_empat.setTypeface(fontbold);

        LinearLayout menu = (LinearLayout) rowView.findViewById(R.id.post);
            menu.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    Intent hariian = new Intent(context , DetailpostActivity.class);
                    context.startActivity(hariian);
                }
            });






        return rowView;

    };



}

