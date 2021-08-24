package id.co.pegadaian.diarium.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import id.co.pegadaian.diarium.R;


/**
 * Created by LENOVO on 29/09/2017.
 */

public class EventTematikAdapter extends ArrayAdapter<String> {
    private final Activity context;
    private final String[] judul_event;
    private final String[] tanggal;
    private final String[] tempat;
    Typeface font,fontbold;

    Dialog myDialog;


    public EventTematikAdapter(Activity context, String[] judul_event, String[] tanggal, String[] tempat) {
        super(context, R.layout.item_list_event, judul_event);
        // TODO Auto-generated constructor stub

        this.context=context;
        this.judul_event=judul_event;
        this.tanggal=tanggal;
        this.tempat=tempat;

        font = Typeface.createFromAsset(context.getAssets(), "fonts/Nexa Light.otf");
        fontbold = Typeface.createFromAsset(context.getAssets(), "fonts/Nexa Bold.otf");

        myDialog = new Dialog(context);
    }

    public View getView(int position, final View view, ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.item_list_event, null,true);

        TextView judulevent = (TextView) rowView.findViewById(R.id.tvTitle);
        judulevent.setText(judul_event[position]);
        judulevent.setTypeface(fontbold);

        TextView date= (TextView) rowView.findViewById(R.id.tanggal);
        date.setText(tanggal[position]);
        date.setTypeface(font);

        TextView lokasi = (TextView) rowView.findViewById(R.id.tvLocation);
        lokasi.setText(tempat[position]);
       lokasi.setTypeface(fontbold);

        Button menu = (Button) rowView.findViewById(R.id.btnDetail);
        menu.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://emsforti.digitalevent.id/"));
                context.startActivity(i);
            }
        });

        return rowView;

    };




}
