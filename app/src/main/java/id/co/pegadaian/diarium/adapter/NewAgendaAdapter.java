package id.co.pegadaian.diarium.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import id.co.pegadaian.diarium.R;
import id.co.pegadaian.diarium.model.AgendaModel;

public class NewAgendaAdapter extends BaseAdapter {
    private Context mContext;
    private AgendaModel model;
    private List<AgendaModel> listModel;
    private TextView tvTitle, tvDate, tvNo;
    private ImageView ivObat;
    private LinearLayout pesan;

    public NewAgendaAdapter(Context mContext, List<AgendaModel> listModel) {
        this.mContext = mContext;
        this.listModel = listModel;
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
            view = inflater.inflate(R.layout.item_list_new_agenda, null);
        }
        tvTitle = view.findViewById(R.id.tvTitle);
        tvDate = view.findViewById(R.id.tvDate);
        tvNo = view.findViewById(R.id.tvNo);

        tvTitle.setText(model.getName());
        tvDate.setText(model.getDate());
        tvNo.setText(String.valueOf(position+1));


        return view;
    }
}
