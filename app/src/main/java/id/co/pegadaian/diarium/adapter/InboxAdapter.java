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
import id.co.pegadaian.diarium.model.InboxModel;
import id.co.pegadaian.diarium.util.TimeHelper;

public class InboxAdapter extends BaseAdapter {
    private Context mContext;
    private InboxModel model;
    private List<InboxModel> listModel;
    private TextView tvTitle, tvDate, tvDesc;
    private ImageView ivObat;
    private LinearLayout pesan;
    TimeHelper timeHelper;

    public InboxAdapter(Context mContext, List<InboxModel> listModel) {
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
            view = inflater.inflate(R.layout.item_list_inbox, null);
        }

        tvTitle = view.findViewById(R.id.tvTitle);
        tvDate = view.findViewById(R.id.tvDate);
        tvDesc = view.findViewById(R.id.tvDesc);
        pesan = view.findViewById(R.id.pesan);
        tvTitle.setText(model.getTitle());
        tvDate.setText(timeHelper.getElapsedTime(model.getChange_date()));
        System.out.println("TVDATEINBOX" + timeHelper.getElapsedTime(model.getChange_date()));

        tvDesc.setText(model.getDescription());

        return view;
    }

}
