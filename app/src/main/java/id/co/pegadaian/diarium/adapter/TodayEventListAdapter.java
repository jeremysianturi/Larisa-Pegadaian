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
import id.co.pegadaian.diarium.model.TodayEventListModel;

public class TodayEventListAdapter extends BaseAdapter {
    private Context mContext;
    private TodayEventListModel model;
    private List<TodayEventListModel> listModel;
    private TextView tvTitle, tvDate, tvType;
    private ImageView ivType;
    String typeEvent;
    Typeface font,fontbold;

    public TodayEventListAdapter(Context mContext, List<TodayEventListModel> listModel) {
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

    @SuppressLint("SetTextI18n")
    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        model = listModel.get(position);
        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            view = inflater.inflate(R.layout.item_list_today_activity, null);
        }
        tvTitle = view.findViewById(R.id.tvTitle);
        tvDate = view.findViewById(R.id.tvDate);
        tvType = view.findViewById(R.id.tvType);
        ivType = view.findViewById(R.id.ivType);
        System.out.println(model.getDescription()+"d32hkbe2k34r");
        tvTitle.setText(model.getName());
        tvDate.setText(model.getEvent_start()+" - "+model.getEvent_end());
        tvType.setText(model.getDescription());


        return view;
    }
}
