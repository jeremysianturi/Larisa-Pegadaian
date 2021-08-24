package id.co.pegadaian.diarium.adapter;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import id.co.pegadaian.diarium.R;
import id.co.pegadaian.diarium.model.PersonalDataModel;
import id.co.pegadaian.diarium.util.UserSessionManager;

public class DataPersonalAdapter extends BaseAdapter {
    private Context mContext;
    private PersonalDataModel model;
    private List<PersonalDataModel> listModel;
    private TextView tvKodeBooking, tvBookDate, tvName, tvValue, tvPlace, tvBatch;
    private ImageView ivObat, tvImage;
    private LinearLayout pesan;
    Button btnCancel, btnReschedule;
    UserSessionManager session;
    Dialog dialog;

    public DataPersonalAdapter(Context mContext, List<PersonalDataModel> listModel) {
        this.mContext = mContext;
        this.listModel = listModel;
//        session = new UserSessionManager(mContext);
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
    public View getView(final int position, View view, ViewGroup viewGroup) {
        session = new UserSessionManager(mContext);
        model = listModel.get(position);
        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            view = inflater.inflate(R.layout.lay_item_personal_data, null);
        }

        tvName = view.findViewById(R.id.tvTitle);
        tvValue = view.findViewById(R.id.tvValue);
        tvImage = view.findViewById(R.id.imageViewList);

        tvName.setText(listModel.get(position).getName());
        tvValue.setText(listModel.get(position).getValue());

        String name = tvName.getText().toString();

        switch (name){
            case "Telephone":
                tvImage.setImageResource(R.drawable.personal_phone);
                break;
            case "Phone Number":
                tvImage.setImageResource(R.drawable.personal_phone);
                break;
            case "Email":
                tvImage.setImageResource(R.drawable.personal_email);
                break;
            case "Mail":
                tvImage.setImageResource(R.drawable.personal_email);
                break;
            default:
                if(name.contains("No. Rekening")){
                    tvImage.setImageResource(R.drawable.personal_norek);
                    break;
                }
                else{
                    tvImage.setImageResource(R.drawable.personal_npwp);
                    break;
                }
        }
//
//        if (tvName.getText().toString().equals("Telephone") || tvName.getText().toString().equals("Phone Number")){
//            tvImage.setImageResource(R.drawable.personal_phone);
//        } else if (tvName.getText().toString().equals("Email")){
//            tvImage.setImageResource(R.drawable.personal_email);
//        } else if (tvName.getText().toString().equals("KTP")){
//            tvImage.setImageResource(R.drawable.personal_ktp);
//        } else if (tvName.getText().toString().equals("NPWP")){
//            tvImage.setImageResource(R.drawable.personal_npwp);
//        } else if (tvName.getText().toString().equals("BPJS Kesehatan")){
//            tvImage.setImageResource(R.drawable.personal_bpjskes);
//        } else if (tvName.getText().toString().equals("BPJS Ketenagakerjaan")){
//            tvImage.setImageResource(R.drawable.personal_bpjstk);
//        } else if (tvName.getText().toString().contains("No. Rekening")){
//            tvImage.setImageResource(R.drawable.personal_norek);
//        }

        return view;
    }
}
