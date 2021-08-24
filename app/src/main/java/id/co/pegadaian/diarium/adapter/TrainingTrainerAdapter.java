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
import id.co.pegadaian.diarium.model.TrainingTrainerModel;
import id.co.pegadaian.diarium.util.TimeHelper;
import id.co.pegadaian.diarium.util.UserSessionManager;

public class TrainingTrainerAdapter extends BaseAdapter {
    private Context mContext;
    TimeHelper timeHelper;
    private TrainingTrainerModel model;
    private List<TrainingTrainerModel> listModel;
    private TextView tvEvent, tvBatch, tvLocation, tvType, tvCurriculum, tvDate;
    private ImageView ivType;
    String typeEvent;
    Typeface font,fontbold;
    UserSessionManager session;

    public TrainingTrainerAdapter(Context mContext, List<TrainingTrainerModel> listModel) {
        this.mContext = mContext;
        this.listModel = listModel;
        session = new UserSessionManager(mContext);
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
        if (session.getRoleLMS().equals("TRAINER")) {
            tvLocation.setVisibility(View.GONE);
            tvType.setVisibility(View.GONE);
            tvCurriculum.setVisibility(View.GONE);
        } else {
            tvLocation.setText("Location : ");
            tvType.setText("Type : ");
            tvCurriculum.setText("Curriculum : ");
        }
        tvDate.setText("Date : "+timeHelper.getTimeFormat(model.getBEGDA())+" - "+timeHelper.getTimeFormat(model.getENDDA()));

        return view;
    }
}
