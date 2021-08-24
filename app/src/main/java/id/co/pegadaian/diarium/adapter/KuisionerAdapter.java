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
import id.co.pegadaian.diarium.model.KuisionerModel;

public class KuisionerAdapter extends BaseAdapter {
    private Context mContext;
    private KuisionerModel model;
    private List<KuisionerModel> listModel;
    private ImageView ivObat;
    private LinearLayout pesan;

    public KuisionerAdapter(Context mContext, List<KuisionerModel> listModel) {
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
        ViewHolder holder;
        model = listModel.get(position);
        if (view == null) {
            holder = new ViewHolder();

            LayoutInflater inflater = LayoutInflater.from(mContext);
            if (model.getType().equals("1")) {
                view = inflater.inflate(R.layout.item_list_kuisioner_essay, null);
                holder.tvNumber = view.findViewById(R.id.tvNumber);
                holder.tvSoal = view.findViewById(R.id.tvTitle);
                holder.tvNumber.setText(String.valueOf(position+1));
                holder.tvSoal.setText(model.getPertanyaan());

            } else {
                view = inflater.inflate(R.layout.item_list_kuisioner_pg, null);
                holder.tvNumber = view.findViewById(R.id.tvNumber);
                holder.tvSoal = view.findViewById(R.id.tvTitle);
                holder.tvNumber.setText(String.valueOf(position+1));
                holder.tvSoal.setText(model.getPertanyaan());
            }
        }

        return view;
    }

    class ViewHolder {
        private TextView tvNumber, tvSoal;
    }

}
