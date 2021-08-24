package id.co.pegadaian.diarium.adapter;


import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import id.co.pegadaian.diarium.R;
import id.co.pegadaian.diarium.model.EmployeeCareModel;
import id.co.pegadaian.diarium.util.TimeHelper;
import id.co.pegadaian.diarium.util.UserSessionManager;

public class EmployeeCareAdapter extends BaseAdapter {
    private Context mContext;
    private EmployeeCareModel model;
    private List<EmployeeCareModel> listModel;
    private TextView tvTicket, tvTitle, tvDate, tvDesc;
    private ImageView ivObat;
    private LinearLayout pesan;
    Button btnCancel, btnReschedule;
    UserSessionManager session;
    Dialog dialog;
    TimeHelper timeHelper;

    public EmployeeCareAdapter(Context mContext, List<EmployeeCareModel> listModel) {
        this.mContext = mContext;
        this.listModel = listModel;
        session = new UserSessionManager(mContext);
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
            view = inflater.inflate(R.layout.item_list_emp_care, null);
        }

        tvTicket = view.findViewById(R.id.tvTicket);
        tvTitle = view.findViewById(R.id.tvTitle);
        tvDate = view.findViewById(R.id.tvDate);
        tvDesc = view.findViewById(R.id.tvDesc);

        tvTicket.setText(listModel.get(position).getTicket_number());
        tvTitle.setText(listModel.get(position).getProblem_type());
        tvDesc.setText(listModel.get(position).getProblem_desc());
        tvDate.setText(timeHelper.getElapsedTime(listModel.get(position).getChange_date()));

        return view;
    }



}
