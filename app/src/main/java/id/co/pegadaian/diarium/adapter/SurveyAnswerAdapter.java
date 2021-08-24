package id.co.pegadaian.diarium.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.List;

import id.co.pegadaian.diarium.R;
import id.co.pegadaian.diarium.model.AnswerMultipleChoiceModel;

public class SurveyAnswerAdapter extends BaseAdapter {
    private Context mContext;
    private AnswerMultipleChoiceModel model;
    private List<AnswerMultipleChoiceModel> listModel;
    private TextView tvAnswer, tvDate, tvDesc;
    private ImageView ivObat;
    private LinearLayout pesan;
    private RadioButton radioAnswer;
    private int posisinya;

    public SurveyAnswerAdapter(Context mContext, List<AnswerMultipleChoiceModel> listModel, int pos) {
        this.mContext = mContext;
        this.listModel = listModel;
        this.posisinya = pos;
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
            view = inflater.inflate(R.layout.list_item_answer, null);
        }
        pesan = (LinearLayout) view.findViewById(R.id.pesan);
        tvAnswer = (TextView) view.findViewById(R.id.tvAnswer);
//        if (position == posisinya) {
//            pesan.setBackgroundColor(Color.parseColor("#5FBA7D"));
//        } else {
//            pesan.setBackgroundColor(Color.parseColor("#EFF0F1"));
//        }
        tvAnswer.setText(model.getAnswer());
        return view;
    }
}
