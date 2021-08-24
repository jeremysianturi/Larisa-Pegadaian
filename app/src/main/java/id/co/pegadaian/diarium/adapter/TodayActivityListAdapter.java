package id.co.pegadaian.diarium.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import id.co.pegadaian.diarium.R;
import id.co.pegadaian.diarium.model.TodayActivityListModel;

public class TodayActivityListAdapter extends BaseAdapter {
    private Context mContext;
    private TodayActivityListModel model;
    private List<TodayActivityListModel> listModel;

    String typeEvent;
    Typeface font,fontbold;

    public TodayActivityListAdapter(Context mContext, List<TodayActivityListModel> listModel) {
        this.mContext = mContext;
        this.listModel = listModel;

        font = Typeface.createFromAsset(mContext.getAssets(), "fonts/Nexa Light.otf");
        fontbold = Typeface.createFromAsset(mContext.getAssets(), "fonts/Nexa Bold.otf");
    }

    @Override
    public int getCount() {
        return listModel.size();
    }

    @Override
    public Object getItem(int position) {
        return listModel.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    class ViewHolder {
        private TextView tvTitle, tvDate, tvType;
        private ImageView ivType, ivStatus;
        int position;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        model = listModel.get(position);
        System.out.println("TesModelBRo"+model);
        if (view == null) {
            holder = new ViewHolder();
            holder.position = position;
            LayoutInflater inflater = LayoutInflater.from(mContext);
            view = inflater.inflate(R.layout.item_list_today_activity, null);
            holder.tvTitle = view.findViewById(R.id.tvTitle);
            holder.tvDate = view.findViewById(R.id.tvDate);
            holder.tvType = view.findViewById(R.id.tvType);
            holder.ivType = view.findViewById(R.id.ivType);
//            holder.tvStatus = view.findViewById(R.id.tvStatus);
            System.out.println(listModel.get(position).getActivity_title()+"d32hkbe2k34r");
            holder.tvTitle.setText(listModel.get(position).getActivity_title());
            holder.tvDate.setText(listModel.get(position).getActivity_start()+" - "+listModel.get(position).getActivity_finish());
            String type = listModel.get(position).getActivity_type();
//            if (listModel.get(position).getApproval_status().equals("0")) {
//                holder.tvStatus.setText("need approval");
//                holder.tvStatus.setTextColor(Color.RED);
//            } else {
//                holder.tvStatus.setText("approved");
//                holder.tvStatus.setTextColor(Color.GREEN);
//            }
            String typeResult = null;
            switch (type) {
                case "00":
                    typeResult = "To Do";
                    holder.ivType.setImageResource(R.drawable.report_todo);
                    break;
                case "01":
                    typeResult = "In Progress";
                    holder.ivType.setImageResource(R.drawable.report_progress);
                    break;
                case "02":
                    typeResult = "Pending";
                    holder.ivType.setImageResource(R.drawable.report_pending);
                    break;
                case "03":
                    typeResult = "Done";
                    holder.ivType.setBackgroundResource(R.drawable.report_done);
                    break;
                case "04":
                    typeResult = "Cancel";
                    holder.ivType.setBackgroundResource(R.drawable.report_cancel);
                    break;
            }
            holder.tvType.setText(typeResult);
        }
        return view;
    }
}


