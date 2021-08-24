package id.co.pegadaian.diarium.adapter;


import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import id.co.pegadaian.diarium.R;
import id.co.pegadaian.diarium.model.CoaModel;
import id.co.pegadaian.diarium.model.IsiCoaModel;

public class CustomExpandableListAdapter extends BaseExpandableListAdapter {
    Context context;
    ArrayList<CoaModel> ListTerbaru;
    ArrayList<ArrayList<IsiCoaModel>> ListChildTerbaru;
    int count;

    public CustomExpandableListAdapter(Context context, ArrayList<CoaModel> ListTerbaru, ArrayList<ArrayList<IsiCoaModel>> ListChildTerbaru){
        this.context=context;
        this.ListTerbaru=ListTerbaru;
        this.ListChildTerbaru=ListChildTerbaru;
//      this.count=ListTerbaru.size();
//      this.count=ListChildTerbaru.size();
    }
    @Override
    public boolean areAllItemsEnabled()
    {
        return true;
    }


    @Override
    public IsiCoaModel getChild(int groupPosition, int childPosition) {
        return ListChildTerbaru.get(groupPosition).get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        IsiCoaModel childTerbaru = getChild(groupPosition, childPosition);
        ViewHolder holder= null;

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_item, null);

            holder=new ViewHolder();
            holder.tv_batch=(TextView)convertView.findViewById(R.id.tvDate);
            holder.tv_product_name=(TextView)convertView.findViewById(R.id.tvProductName);
            holder.tv_desc=(TextView)convertView.findViewById(R.id.tvDate);
            holder.tv_manufacture_date=(TextView)convertView.findViewById(R.id.tvManufactureDate);
            holder.tv_expired_date=(TextView)convertView.findViewById(R.id.tvExpiredDate);
            convertView.setTag(holder);

        }
        else{
            holder=(ViewHolder)convertView.getTag();
        }

        holder.tv_batch.setText("Production Code : "+childTerbaru.getF_produksi_code());
        holder.tv_product_name.setText("Item Name : "+childTerbaru.getF_item_name());
        holder.tv_desc.setText("Description : "+childTerbaru.getF_description());
        holder.tv_manufacture_date.setText("Manufacture Date : "+childTerbaru.getF_manufacture_date());
        holder.tv_expired_date.setText("Expired Date : "+childTerbaru.getF_expired_date());

        return convertView;
    }
    @Override
    public int getChildrenCount(int groupPosition) {
        return ListChildTerbaru.get(groupPosition).size();
    }

    @Override
    public CoaModel getGroup(int groupPosition) {
        return ListTerbaru.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return ListTerbaru.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        final CoaModel terbaruModel = (CoaModel) getGroup(groupPosition);
        ViewHolder holder= null;
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_group, null);

            holder=new ViewHolder();
            holder.tv_title=(TextView)convertView.findViewById(R.id.listTitle);
            holder.pdfView=(TextView)convertView.findViewById(R.id.pdfView);
            convertView.setTag(holder);
        }

        else{
            holder=(ViewHolder)convertView.getTag();
        }

        holder.tv_title.setText("Coa Number : "+terbaruModel.getCoa_number());
//        holder.pdfView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                System.out.println("3nekjn3"+terbaruModel.getFile());
//                Intent i = new Intent(context, PdfView.class);
//                i.putExtra("pdf",terbaruModel.getFile());
//                context.startActivity(i);
//            }
//        });
        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public boolean isChildSelectable(int arg0, int arg1) {
        return true;
    }


    static class ViewHolder{
        TextView tv_batch, tv_product_name, tv_desc,tv_manufacture_date,tv_expired_date, tv_title, pdfView;
    }

}