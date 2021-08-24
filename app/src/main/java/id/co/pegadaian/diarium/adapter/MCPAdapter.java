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
import id.co.pegadaian.diarium.model.RotasiECPModel;
import id.co.pegadaian.diarium.util.TimeHelper;

public class MCPAdapter extends BaseAdapter {


  private Context mContext;
  private RotasiECPModel model;
  private List<RotasiECPModel> listModel;
  private TextView tvAplican, tvScore, tvRank, tvBand, tvTittle;
  private ImageView ivType;
  String typeEvent;
  TimeHelper timeHelper;
  Typeface font, fontbold;

  public MCPAdapter(Context mContext, List<RotasiECPModel> listModel) {
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
      view = inflater.inflate(R.layout.item_list_managementcp__adapter, null);
    }

    TextView tvNumber = view.findViewById(R.id.number);
    tvNumber.setTypeface(fontbold);
    tvNumber.setText(model.getNumber()+".  ");

    tvAplican = view.findViewById(R.id.isi_aplicans);
    tvAplican.setTypeface(font);
    tvAplican.setText(model.getAplicans());

    tvScore = view.findViewById(R.id.isi_score);
    tvScore.setTypeface(font);
    tvScore.setText(model.getScore());

    tvBand = view.findViewById(R.id.isi_band);
    tvBand.setTypeface(font);
    tvBand.setText(model.getBand());

    tvRank = view.findViewById(R.id.isi_rank);
    tvRank.setTypeface(font);
    tvRank.setText(model.getRank());

    tvTittle = view.findViewById(R.id.tittle);
    tvTittle.setTypeface(fontbold);
    tvTittle.setText(model.getTittle());



/*    if (view == null){
      LayoutInflater inflater = LayoutInflater.from(mContext);
    }*/



    return view;
  }
}
