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

import com.squareup.picasso.Picasso;

import java.util.List;

import id.co.pegadaian.diarium.R;
import id.co.pegadaian.diarium.model.InsightModel;
import id.co.pegadaian.diarium.util.TimeHelper;

public class InsightAdapter extends BaseAdapter {
    private Context mContext;
    private InsightModel model;
    private List<InsightModel> listModel;
    private TextView tvScheduleName, tvTopic, tvDate, tvTime;
    private ImageView ivType;
    String typeEvent;
    TimeHelper timeHelper;
    Typeface font,fontbold;

    public InsightAdapter(Context mContext, List<InsightModel> listModel) {
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
            view = inflater.inflate(R.layout.item_list_insight, null);
        }
        tvScheduleName = view.findViewById(R.id.tvScheduleName);
        tvTopic = view.findViewById(R.id.tvTopic);
        tvDate = view.findViewById(R.id.tvDate);
        tvTime = view.findViewById(R.id.tvTime);
        ivType = view.findViewById(R.id.ivImage);

        if (model.getForum_image().isEmpty()) {
            ivType.setImageResource(R.drawable.profile);
        } else{
            Picasso.get().load(model.getForum_image()).error(R.drawable.profile).into(ivType);
        }
        tvScheduleName.setText(model.getForum_title());
        tvTopic.setText(model.getForum_text());
        tvDate.setText(model.getOwner());
        tvTime.setText(model.getForum_time());

        return view;
    }
}