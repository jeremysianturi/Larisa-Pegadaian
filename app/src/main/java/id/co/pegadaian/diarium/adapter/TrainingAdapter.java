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
import id.co.pegadaian.diarium.model.TrainingModel;
import id.co.pegadaian.diarium.util.TimeHelper;

public class TrainingAdapter extends BaseAdapter {
    private Context mContext;
    TimeHelper timeHelper;
    private TrainingModel model;
    private List<TrainingModel> listModel;
    private TextView tvEvent, tvBatch, tvLocation, tvType, tvCurriculum, tvDate;
    private ImageView ivType;
    String typeEvent;
    Typeface font,fontbold;

    public TrainingAdapter(Context mContext, List<TrainingModel> listModel) {
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
            view = inflater.inflate(R.layout.item_list_training, null);
        }
        tvEvent = view.findViewById(R.id.tvEvent);
        tvBatch = view.findViewById(R.id.tvBatch);
        tvLocation = view.findViewById(R.id.tvLocation);
        tvType = view.findViewById(R.id.tvType);
        tvCurriculum = view.findViewById(R.id.tvCurriculum);
        tvDate = view.findViewById(R.id.tvDate);

        tvEvent.setText("Event : "+model.getEvent_name());
        tvBatch.setText("Batch : "+model.getBatch_name());
        tvLocation.setText("Location : "+model.getLocation());
        tvType.setText("Type : "+model.getEvent_type());
        tvCurriculum.setText("Curriculum : "+model.getCurriculum());
        tvDate.setText("Date : "+timeHelper.getTimeFormat(model.getBegin_date())+" - "+timeHelper.getTimeFormat(model.getEnd_date()));

        return view;
    }
}
