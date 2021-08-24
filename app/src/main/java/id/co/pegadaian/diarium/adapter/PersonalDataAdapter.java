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


/**
 * Created by LENOVO on 29/09/2017.
 */
public class PersonalDataAdapter extends BaseAdapter {
    private Context mContext;
    private PersonalDataModel model;
    private List<PersonalDataModel> listModel;
    private TextView tvKodeBooking, tvBookDate, tvName, tvValue, tvPlace, tvBatch;
    private ImageView ivObat;
    private LinearLayout pesan;
    Button btnCancel, btnReschedule;
    UserSessionManager session;
    Dialog dialog;

    public PersonalDataAdapter(Context mContext, List<PersonalDataModel> listModel) {
        this.mContext = mContext;
        this.listModel = listModel;
        session = new UserSessionManager(mContext);
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
        model = listModel.get(position);
        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            view = inflater.inflate(R.layout.lay_item_personal_data, null);
        }

        tvName = view.findViewById(R.id.tvTitle);
        tvValue = view.findViewById(R.id.tvValue);

        tvName.setText(listModel.get(position).getName());
        tvValue.setText(listModel.get(position).getValue());


        return view;
    }
}
