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
import id.co.pegadaian.diarium.model.PaymentModel;
import id.co.pegadaian.diarium.util.TimeHelper;

public class PaymentAdapter extends BaseAdapter {

  private Context mContext;
  private PaymentModel model;
  private List<PaymentModel> listModel;
  private TextView tvAplican, tvScore, tvRank, tvBand, tvTittle;
  private ImageView ivType;
  String typeEvent;
  TimeHelper timeHelper;
  Typeface font, fontbold;

  public PaymentAdapter(Context mContext, List<PaymentModel> listModel) {
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
      view = inflater.inflate(R.layout.item_list_payment_adapter, null);
    }

    TextView tv1 = view.findViewById(R.id.text1);
//    tv1.setText(model.getTittle());

    TextView tv2 = view.findViewById(R.id.text2);
//    tv1.setText(model.getTittle());

    ImageView img = view.findViewById(R.id.image_pay);



    return view;
  }
}
