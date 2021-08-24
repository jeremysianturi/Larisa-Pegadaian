package id.co.pegadaian.diarium.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.List;

import id.co.pegadaian.diarium.R;
import id.co.pegadaian.diarium.model.MateriModel;

public class MateriAdapter extends BaseAdapter implements Filterable {
    private Context mContext;
    private MateriModel model;
    private List<MateriModel> listModel;
    private TextView txt_materi, txt_author;
    private Typeface font, fontbold;

    public MateriAdapter(Context mContext, List<MateriModel> listModel) {
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

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        model = listModel.get(position);
        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            view = inflater.inflate(R.layout.item_list_materi, null);
        }
        txt_materi = view.findViewById(R.id.txt_materi);
        txt_author = view.findViewById(R.id.txt_author);
        txt_materi.setText(model.getTitle());
        txt_author.setText(model.getDescription());
        return view;
    }

    @Override
    public Filter getFilter() {
        // return a filter that filters data based on a constraint

        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                return null;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {

            }
        };
    }
}
