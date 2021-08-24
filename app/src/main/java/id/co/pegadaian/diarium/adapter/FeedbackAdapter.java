package id.co.pegadaian.diarium.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.List;

import id.co.pegadaian.diarium.R;
import id.co.pegadaian.diarium.model.FeedbackModel;
import id.co.pegadaian.diarium.util.DataSession;

public class FeedbackAdapter extends BaseAdapter {
    private Context mContext;
    private FeedbackModel model;
    private List<FeedbackModel> listModel;
    private Typeface font, fontbold;
    private DataSession dataSession;
    private String typeFeedback;

    public FeedbackAdapter(Context mContext, List<FeedbackModel> listModel, DataSession dataSession, String typeFeedback) {
        this.mContext = mContext;
        this.listModel = listModel;
        this.dataSession = dataSession;
        this.typeFeedback = typeFeedback;

        fontbold = Typeface.createFromAsset(mContext.getAssets(), "fonts/AvenirLTStd-Medium.otf");
        font = Typeface.createFromAsset(mContext.getAssets(), "fonts/AvenirLTStd-Book.otf");
    }

    @Override
    public int getCount() {
        return listModel.size();
    }

    @Override
    public FeedbackModel getItem(int position) {
        return listModel.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        model = listModel.get(position);
        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            view = inflater.inflate(R.layout.item_list_feedback, null);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        holder.txt_quest.setTypeface(fontbold);
        holder.txt_quest.setText(model.getFeedback_question());

        holder.ratingBar.setOnRatingBarChangeListener(onRatingChangedListener(holder, position));
        holder.ratingBar.setTag(position);
        holder.ratingBar.setRating(getItem(position).getRatingStar());
        return view;
    }

    private RatingBar.OnRatingBarChangeListener onRatingChangedListener(final ViewHolder holder, final int position) {
        return new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                FeedbackModel item = getItem(position);
                item.setRatingStar(v);
                int nilai = Math.round(v);
                dataSession.setData("feedback" + (position) + typeFeedback, String.valueOf(nilai));
            }
        };
    }

    private static class ViewHolder {
        private TextView txt_quest;
        private RatingBar ratingBar;

        private ViewHolder(View view) {
            txt_quest = view.findViewById(R.id.txt_fb_quest);
            ratingBar = view.findViewById(R.id.rate_fb_quest);
        }
    }
}
