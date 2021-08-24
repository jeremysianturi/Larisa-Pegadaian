package id.co.pegadaian.diarium.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import id.co.pegadaian.diarium.R;


public class MenuEventAdapter extends BaseAdapter {

    Context context;
    private final int[] image;
    private final String[] isi;
    LayoutInflater layoutInflater;
    View view;
    Typeface font,fontbold;


    public MenuEventAdapter(Activity context, int[] image, String[] isi) {

        // TODO Auto-generated constructor stub

        this.context=context;
        this.image=image;
        this.isi=isi;
        font = Typeface.createFromAsset(context.getAssets(), "fonts/Nexa Light.otf");
        fontbold = Typeface.createFromAsset(context.getAssets(), "fonts/Nexa Bold.otf");
    }

    public int getCount() {

        return image.length;
    }

    public String getItem(int position) {


        return null;
    }

    public long getItemId(int position) {

        return 0;

    }

    public View getView(int position, View convertView, ViewGroup parent) {

        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if(convertView == null){
            view = new View(context);
            view = layoutInflater.inflate(R.layout.item_gridmenu,null);
            ImageView imageView = (ImageView) view.findViewById(R.id.imView);
            TextView textView = (TextView) view.findViewById(R.id.txtJudul);
            imageView.setImageResource(image[position]);
            textView.setText(isi[position]);
            textView.setTypeface(fontbold);




        }

        return view;

    }


}

