package id.co.pegadaian.diarium.adapter;//package id.co.telkomsigma.Diarium.adapter;
//
//import android.content.Context;
//import android.content.Intent;
//import android.graphics.Typeface;
//import android.support.annotation.NonNull;
//import android.support.annotation.Nullable;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ArrayAdapter;
//import android.widget.BaseAdapter;
//import android.widget.Button;
//import android.widget.ImageView;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//
//import java.util.ArrayList;
//
//import id.co.telkomsigma.Diarium.R;
//import id.co.telkomsigma.Diarium.controller.more.employeecorner.BookingcornerActivity;
//import id.co.telkomsigma.Diarium.controller.more.ic_report.ReportActivity;
//
//
//public class EmployeecornerAdapter extends BaseAdapter {
//
//    Context context;
//    private final String [] values;
//    private final int [] images;
//    Button btn;
//    Typeface font,fontbold;
//
//    public EmployeecornerAdapter(Context context, String [] values, int [] images){
//        //super(context, R.layout.single_list_app_item, utilsArrayList);
//        this.context = context;
//        this.values = values;
//        this.images = images;
//    }
//
//    @Override
//    public int getCount() {
//        return values.length;
//    }
//
//    @Override
//    public Object getItem(int i) {
//        return i;
//    }
//
//    @Override
//    public long getItemId(int i) {
//        return i;
//    }
//
//    @NonNull
//    @Override
//    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
//
//
//        ViewHolder viewHolder;
//
//        final View result;
//
//        if (convertView == null) {
//
//            viewHolder = new ViewHolder();
//            LayoutInflater inflater = LayoutInflater.from(context);
//            convertView = inflater.inflate(R.layout.item_list_corner, parent, false);
//
//            font = Typeface.createFromAsset(context.getAssets(),"fonts/Nexa Light.otf");
//            fontbold = Typeface.createFromAsset(context.getAssets(),"fonts/Nexa Bold.otf");
//            viewHolder.txtName = (TextView) convertView.findViewById(R.id.corner_name);
//            viewHolder.txtName.setTypeface(font);
//            viewHolder.icon = (ImageView) convertView.findViewById(R.id.imagecorner);
//
//            Button btn = (Button) convertView.findViewById(R.id.btn_choose);
//            btn.setTypeface(fontbold);
//
//            RelativeLayout layout = (RelativeLayout) convertView.findViewById(R.id.layout_corner);
//            layout. setOnClickListener(new View.OnClickListener() {
//
//                @Override
//                public void onClick(View view) {
//                    Intent intent = new Intent(context, BookingcornerActivity.class);
//                    context.startActivity(intent);
//                }
//            });
//
//
//            result=convertView;
//
//            convertView.setTag(viewHolder);
//        } else {
//            viewHolder = (ViewHolder) convertView.getTag();
//            result=convertView;
//        }
//
//        viewHolder.txtName.setText(values[position]);
//        viewHolder.icon.setImageResource(images[position]);
//
//        return convertView;
//    }
//
//    private static class ViewHolder {
//
//        TextView txtName;
//        ImageView icon;
//
//    }
//
//}