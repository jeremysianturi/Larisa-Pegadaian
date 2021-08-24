package id.co.pegadaian.diarium.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import id.co.pegadaian.diarium.R;
import id.co.pegadaian.diarium.model.SearchModel;

public class SearchAdapter extends BaseAdapter implements Filterable {
    private Context mContext;
    private SearchModel model;
    private List<SearchModel> listModel;
    public static List<SearchModel> temporarylist;
    private TextView tvTitle, tvJob, tvDesc;
    private ImageView ivProfile;
    private LinearLayout pesan;

    public SearchAdapter(Context mContext, List<SearchModel> listModel) {
        this.mContext = mContext;
        this.listModel = listModel;
        temporarylist=listModel;
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
            view = inflater.inflate(R.layout.item_list_search, null);
        }
        ivProfile = view.findViewById(R.id.ivProfile);
        tvTitle = view.findViewById(R.id.product_name);
        tvJob = view.findViewById(R.id.tvJob);
        tvTitle.setText(model.getFull_name());
        if (model.getProfile().isEmpty()) {
            ivProfile.setImageResource(R.drawable.profile);
        } else{
            Picasso.get().load(model.getProfile()).error(R.drawable.profile).into(ivProfile);
        }
        return view;
    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                temporarylist=(List<SearchModel>)results.values;
                notifyDataSetChanged();
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                ArrayList<String> FilteredList= new ArrayList<String>();
                if (constraint == null || constraint.length() == 0) {
                    // No filter implemented we return all the list
                    results.values = listModel;
                    results.count = listModel.size();
                }
                else {
                    for (int i = 0; i < listModel.size(); i++) {
                        String data = String.valueOf(listModel.get(i).getFull_name());
                        if (data.toLowerCase().contains(constraint.toString()))  {
                            FilteredList.add(data);
                        }
                    }
                    results.values = FilteredList;
                    results.count = FilteredList.size();
                }
                return results;
            }
        };
        return filter;
    }
}
