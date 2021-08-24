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
import id.co.pegadaian.diarium.model.DetailMentoringModel;
import id.co.pegadaian.diarium.util.TimeHelper;

public class DetailMentoringAdapter extends BaseAdapter {
    private Context mContext;
    private DetailMentoringModel model;
    private List<DetailMentoringModel> listModel;
    private TextView tvScheduleName, tvTopic, tvDate, tvTime;
    private ImageView ivType;
    String typeEvent;
    TimeHelper timeHelper;
    Typeface font,fontbold;

    public DetailMentoringAdapter(Context mContext, List<DetailMentoringModel> listModel) {
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
            view = inflater.inflate(R.layout.item_list_comment_mentoring, null);
        }
        tvScheduleName = view.findViewById(R.id.tvScheduleName);
        tvTopic = view.findViewById(R.id.tvTopic);
        tvDate = view.findViewById(R.id.tvDate);
        tvTime = view.findViewById(R.id.tvTime);
        ivType = view.findViewById(R.id.ivImage);

//        if (model.getForum_image().isEmpty()) {
//            ivType.setImageResource(R.drawable.profile);
//        } else{
//            Picasso.get().load(model.getForum_image()).error(R.drawable.profile).into(ivType);
//        }
        tvScheduleName.setText(model.getChar_text());
        tvTopic.setText(model.getBegin_date());
        tvDate.setVisibility(View.GONE);
        tvTime.setVisibility(View.GONE);

        return view;
    }
}