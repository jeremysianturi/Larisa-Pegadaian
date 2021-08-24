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
import id.co.pegadaian.diarium.model.PromosiEcpModel;
import id.co.pegadaian.diarium.util.TimeHelper;

public class Adapter_ecp_promosi extends BaseAdapter {

  private Context mContext;
  private PromosiEcpModel model;
  private List<PromosiEcpModel> listModel;
  private TextView tvAplican, tvScore, tvRank, tvBand;
  private ImageView ivType;
  String typeEvent;
  TimeHelper timeHelper;
  Typeface font, fontbold;

  public Adapter_ecp_promosi(Context mContext, List<PromosiEcpModel> listModel) {
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
      view = inflater.inflate(R.layout.item_list_promosi, null);
    }

    tvAplican = view.findViewById(R.id.isi_aplicans);
    tvAplican.setTypeface(fontbold);
    tvScore = view.findViewById(R.id.isi_score);
    tvScore.setTypeface(fontbold);
    tvBand = view.findViewById(R.id.isi_band);
    tvBand.setTypeface(fontbold);
    tvRank = view.findViewById(R.id.isi_rank);
    tvRank.setTypeface(fontbold);



/*    if (view == null){
      LayoutInflater inflater = LayoutInflater.from(mContext);
    }*/



    return view;
  }
}
