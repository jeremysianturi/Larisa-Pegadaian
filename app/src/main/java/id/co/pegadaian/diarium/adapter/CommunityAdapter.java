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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import id.co.pegadaian.diarium.R;
import id.co.pegadaian.diarium.model.CommunityModel;


/**
 * Created by LENOVO on 29/09/2017.
 */

public class CommunityAdapter extends BaseAdapter {
    private Context mContext;
    private CommunityModel model;
    private List<CommunityModel> listModel;
    private TextView tvName, tvDate, tvDesc;
    private ImageView ivObat;
    private LinearLayout pesan;

    public CommunityAdapter(Context mContext, List<CommunityModel> listModel) {
        this.mContext = mContext;
        this.listModel = listModel;
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
            view = inflater.inflate(R.layout.item_listcommunity, null);
        }
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date value = null;
        try {
            value = formatter.parse(model.getCommunity_date());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat dateFormatter = new SimpleDateFormat("dd MMM yyyy");
        dateFormatter.setTimeZone(TimeZone.getDefault());
        String dt = dateFormatter.format(value);

        tvName = view.findViewById(R.id.tvCommunityName);
        tvDate = view.findViewById(R.id.tvCommunityDate);
        tvDesc = view.findViewById(R.id.tvCommunityDesc);
        tvName.setText(model.getCommunity_name());
        tvDate.setText("created at : "+dt);
        tvDesc.setText(model.getCommunity_desc());

        return view;
    }
}
