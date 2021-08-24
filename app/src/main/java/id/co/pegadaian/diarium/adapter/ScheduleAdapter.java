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
import id.co.pegadaian.diarium.model.ScheduleModel;
import id.co.pegadaian.diarium.util.TimeHelper;

public class ScheduleAdapter extends BaseAdapter {
    private Context mContext;
    private ScheduleModel model;
    private List<ScheduleModel> listModel;
    private TextView tvScheduleName, tvTopic, tvDate, tvTime;
    private ImageView ivType;
    String typeEvent;
    TimeHelper timeHelper;
    Typeface font,fontbold;

    public ScheduleAdapter(Context mContext, List<ScheduleModel> listModel) {
        this.mContext = mContext;
        this.listModel = listModel;
        timeHelper = new TimeHelper();
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

    @SuppressLint("SetTextI18n")
    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        model = listModel.get(position);
        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            view = inflater.inflate(R.layout.item_list_schedule, null);
        }
        tvScheduleName = view.findViewById(R.id.tvScheduleName);
        tvTopic = view.findViewById(R.id.tvTopic);
        tvDate = view.findViewById(R.id.tvDate);
        tvTime = view.findViewById(R.id.tvTime);

        tvScheduleName.setText(model.getSchedule_name());
        tvTopic.setText(model.getTopic());
        tvDate.setText(timeHelper.getTimeFormat(model.getBegin_date()));
        tvTime.setText(model.getBegin_time()+" - "+model.getEnd_time());
        return view;
    }
}
