package id.co.pegadaian.diarium.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import id.co.pegadaian.diarium.R;
import id.co.pegadaian.diarium.model.NotifModel;
import id.co.pegadaian.diarium.util.TimeHelper;


/**
 * Created by LENOVO on 29/09/2017.
 */

public class NotificationAdapter extends BaseAdapter {
    private Context mContext;
    private NotifModel model;
    private List<NotifModel> listModel;
    private TextView tvTitle, tvDate, tvDesc;
    private ImageView ivObat;
    private LinearLayout pesan;
    TimeHelper timeHelper;

    public NotificationAdapter(Context mContext, List<NotifModel> listModel) {
        this.mContext = mContext;
        this.listModel = listModel;
        timeHelper = new TimeHelper();
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
            view = inflater.inflate(R.layout.item_list_notif, null);
        }

        tvTitle = view.findViewById(R.id.title);
        tvDesc = view.findViewById(R.id.desc);
        tvDate = view.findViewById(R.id.date);

        tvTitle.setText(model.getTitle());
        tvDesc.setText(model.getDescription());
        tvDate.setText(timeHelper.getElapsedTime(model.getChange_date()));

        return view;
    }

}
