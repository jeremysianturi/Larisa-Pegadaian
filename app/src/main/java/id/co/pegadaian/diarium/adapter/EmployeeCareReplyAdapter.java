package id.co.pegadaian.diarium.adapter;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import id.co.pegadaian.diarium.R;
import id.co.pegadaian.diarium.model.EmployeeCareReplyModel;
import id.co.pegadaian.diarium.util.TimeHelper;

public class EmployeeCareReplyAdapter extends BaseAdapter {
    private Context mContext;
    private EmployeeCareReplyModel model;
    private List<EmployeeCareReplyModel> listModel;
    private TextView tvDate, tvName, tvComment, tvAttachment;
    private ImageView ivObat;
    TimeHelper timeHelper;

    public EmployeeCareReplyAdapter(Context mContext, List<EmployeeCareReplyModel> listModel) {
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
            view = inflater.inflate(R.layout.list_reply, null);
        }
        tvDate = view.findViewById(R.id.tvDate);
        tvName = view.findViewById(R.id.tvName);
        tvComment = view.findViewById(R.id.tvReplyText);
        tvAttachment = view.findViewById(R.id.tvAttachment);

        tvDate.setText(timeHelper.getElapsedTime(model.getChange_date()));
        tvName.setText(model.getName());
        tvComment.setText(model.getReply_text());
        if (model.getContent_field().equals("")) {
            tvAttachment.setVisibility(View.GONE);
        }else {
            tvAttachment.setVisibility(View.VISIBLE);
            tvAttachment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(model.getContent_field()));
                    mContext.startActivity(i);
                }
            });
        }
        return view;
    }
}

