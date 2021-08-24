package id.co.pegadaian.diarium.adapter;//package id.co.telkomsigma.Diarium.adapter;
//
//import android.app.Activity;
//import android.app.Dialog;
//import android.graphics.Color;
//import android.graphics.Typeface;
//import android.graphics.drawable.ColorDrawable;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ArrayAdapter;
//import android.widget.Button;
//import android.widget.TextView;
//
//import id.co.telkomsigma.Diarium.R;
//
//
///**
// * Created by LENOVO on 29/09/2017.
// */
//
//public class NotifikasiAdapter extends ArrayAdapter<String> {
//    private final Activity context;
//    private final String[] name;
//    private final String[] notifikasi;
//    private final String[] tanggal;
//    Typeface font,fontbold;
//
//    Dialog myDialog;
//
//
//    public NotifikasiAdapter(Activity context, String[] name, String[] notifikasi,String[] tanggal) {
//        super(context, R.layout.item_list_notif, name);
//        // TODO Auto-generated constructor stub
//
//        this.context=context;
//        this.name=name;
//        this.notifikasi=notifikasi;
//        this.tanggal=tanggal;
//        font = Typeface.createFromAsset(context.getAssets(), "fonts/Nexa Light.otf");
//        fontbold = Typeface.createFromAsset(context.getAssets(), "fonts/Nexa Bold.otf");
//
//        myDialog = new Dialog(context);
//    }
//
//    public View getView(int position, final View view, ViewGroup parent) {
//        LayoutInflater inflater=context.getLayoutInflater();
//        View rowView=inflater.inflate(R.layout.item_list_notif, null,true);
//
//        TextView txtTitle = (TextView) rowView.findViewById(R.id.name);
//        txtTitle.setText(name[position]);
//        txtTitle.setTypeface(fontbold);
//
//        TextView txtTitle_dua = (TextView) rowView.findViewById(R.id.date);
//        txtTitle_dua.setText(tanggal[position]);
//        txtTitle_dua.setTypeface(font);
//
//        TextView txtTitle_dua1 = (TextView) rowView.findViewById(R.id.notifikasi);
//        txtTitle_dua1.setText(tanggal[position]);
//        txtTitle_dua1.setTypeface(font);
//
//
//
//
//        return rowView;
//
//    };
//
//
//
//
//}
