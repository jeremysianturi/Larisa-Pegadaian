package id.co.pegadaian.diarium.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import id.co.pegadaian.diarium.R;
import id.co.pegadaian.diarium.model.AnswerTestModel;

public class AnswerTestLMSAdapter extends BaseAdapter {
  private Context mContext;
  private AnswerTestModel model;
  private List<AnswerTestModel> listModel;
  private ImageView ivObat;
  private LinearLayout pesan;
  private RadioButton radioAnswer;
  private int posisinya;
  ArrayList<String> selectedStrings = new ArrayList<String>();

  public AnswerTestLMSAdapter(Context mContext, List<AnswerTestModel> listModel, int pos) {
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
    ViewHolder holder = null;
    model = listModel.get(position);

    if (view == null) {
      holder = new ViewHolder(); LayoutInflater inflater = (LayoutInflater) mContext
              .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
      view = inflater.inflate(R.layout.list_item_answer, null, true);
      holder.checkBox = view.findViewById(R.id.checkBoxAnswer);
      holder.tvAnswer = view.findViewById(R.id.tvAnswer);
      view.setTag(holder);
    }else {
      holder = (ViewHolder)view.getTag();
    }

    holder.checkBox.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if(listModel.get(position).isSelected()){
          listModel.get(position).setSelected(false);
          System.out.println(listModel.get(position).isSelected()+" posisiadapter"+position);
        }else {
          listModel.get(position).setSelected(true);
          System.out.println(listModel.get(position).isSelected()+" posisiadapter"+position);
        }
      }
    });
    holder.tvAnswer.setText(model.getAnswer_text());


    return view;
  }

  private class ViewHolder {
    protected CheckBox checkBox;
    private TextView tvAnswer;

  }
}
