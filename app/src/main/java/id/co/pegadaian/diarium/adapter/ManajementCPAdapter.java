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
import id.co.pegadaian.diarium.model.ManajementCPModel;
import id.co.pegadaian.diarium.util.TimeHelper;

public class ManajementCPAdapter extends BaseAdapter {
  private Context mContext;
  private ManajementCPModel model;
  private List<ManajementCPModel> listModel;
  private TextView tvNama, tvNik, tvBand, tvJabatan, tvLokCompany, tvNamaCompany;
  private ImageView ivPhotoProfile;
  String typeEvent;
  TimeHelper timeHelper;
  Typeface font, fontbold;

  public ManajementCPAdapter(Context mContext, List<ManajementCPModel> listModel) {
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
      view = inflater.inflate(R.layout.item_list_mcp, null);
    }

    tvNama = view.findViewById(R.id.mcp_nama);
    tvNama.setText(model.getComplete_name());
//    tvNama.setTypeface(font);
//    tvNama.setText(model.getNama_lengkap());

    tvNik = view.findViewById(R.id.mcp_NIK);
    tvNik.setText(model.getPersonnel_number());
//    tvJob.setTypeface(font);
//    tvJob.setText(model.getJob_function());

    tvJabatan = view.findViewById(R.id.mcp_jabatan);
    tvJabatan.setText(model.getPosition_name());
//    tvJabatan.setTypeface(font);
//    tvJabatan.setText(model.getJabatan());

//    tvBand = view.findViewById(R.id.mcp_band);
//    tvBand.setTypeface(font);
//    tvBand.setText(model.getBand());

//    tvLokCompany = view.findViewById(R.id.mcp_lokasi_kantor);
//    tvLokCompany.setTypeface(font);
//    tvLokCompany.setText(model.getLokasi_kantor());

//    tvNamaCompany = view.findViewById(R.id.mcp_nama_kantor);
//    tvNamaCompany.setTypeface(font);
//    tvNamaCompany.setText(model.getNama_kantor());

    ivPhotoProfile = view.findViewById(R.id.mcp_photo);
//    if (model.getPhoto().isEmpty()) {
//      Picasso.get().load(R.drawable.profile).into(ivPhotoProfile);
//    }else {
//      Picasso.get().load(model.getPhoto()).into(ivPhotoProfile);
//    }




/*    if (view == null){
      LayoutInflater inflater = LayoutInflater.from(mContext);
    }*/



    return view;
  }
}
