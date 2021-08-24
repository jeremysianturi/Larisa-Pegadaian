package id.co.pegadaian.diarium.adapter;


import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import id.co.pegadaian.diarium.R;
import id.co.pegadaian.diarium.model.HRWikiModel;

public class HRWikiAdapter extends BaseAdapter {
    private Context mContext;
    private HRWikiModel model;
    private List<HRWikiModel> listModel;
    private TextView tvKasus, tvDate, tvSolusi;
    private ImageView ivProfile;
    private LinearLayout pesan;

    public HRWikiAdapter(Context mContext, List<HRWikiModel> listModel) {
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
            view = inflater.inflate(R.layout.item_list_hr_wiki, null);
        }
        tvKasus = view.findViewById(R.id.tvKasus);
        tvDate = view.findViewById(R.id.tvDate);
        tvSolusi = view.findViewById(R.id.tvSolusi);

        tvKasus.setText(model.getKasus());
        tvDate.setText(model.getChange_date());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            tvSolusi.setText(Html.fromHtml(model.getSolusi().replace("\\r\\n\\r\\n\t",System.getProperty("line.separator")), Html.FROM_HTML_MODE_COMPACT));
        } else {
            tvSolusi.setText(Html.fromHtml(model.getSolusi().replace("\\r\\n\\r\\n\t",System.getProperty("line.separator"))));
        }

        return view;
    }
}



//public class SearchTempAdapter extends BaseAdapter implements Filterable {
//
//    private ArrayList<SearchTempModel> _Contacts;
//    private Activity context;
//    private LayoutInflater inflater;
//    private ValueFilter valueFilter;
//    private ArrayList<SearchTempModel> mStringFilterList;
//
//    public SearchTempAdapter(Activity context, ArrayList<SearchTempModel> _Contacts) {
//        super();
//        this.context = context;
//        this._Contacts = _Contacts;
//        mStringFilterList =  _Contacts;
//        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        getFilter();
//    }
//
//    @Override
//    public int getCount() {
//        return _Contacts.size();
//    }
//
//    @Override
//    public Object getItem(int position) {
//        return _Contacts.get(position).getFull_name();
//    }
//
//    @Override
//    public long getItemId(int position) {
//        return 0;
//    }
//
//    public class ViewHolder {
//        TextView tvName, tvNIK, tvJOB;
//    }
//
//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//        ViewHolder holder;
//        if (convertView == null) {
//            holder = new ViewHolder();
//            convertView = inflater.inflate(R.layout.item_list_team, null);
//            holder.tvName = (TextView) convertView.findViewById(R.id.nama);
//            holder.tvNIK = (TextView) convertView.findViewById(R.id.nik);
//            holder.tvJOB = (TextView) convertView.findViewById(R.id.jabatan);
//            convertView.setTag(holder);
//        } else
//            holder = (ViewHolder) convertView.getTag();
//        holder.tvName.setText(_Contacts.get(position).getFull_name());
//        holder.tvNIK.setText(_Contacts.get(position).getPersonal_number());
//        holder.tvJOB.setText(_Contacts.get(position).getBorn_city());
//        return convertView;
//    }
//
//    @Override
//    public Filter getFilter() {
//        if(valueFilter==null) {
//
//            valueFilter=new ValueFilter();
//        }
//
//        return valueFilter;
//    }
//    private class ValueFilter extends Filter {
//
//        //Invoked in a worker thread to filter the data according to the constraint.
//        @Override
//        protected FilterResults performFiltering(CharSequence constraint) {
//            FilterResults results=new FilterResults();
//            if(constraint!=null && constraint.length()>0){
//                ArrayList<SearchTempModel> filterList=new ArrayList<SearchTempModel>();
//                for(int i=0;i<mStringFilterList.size();i++){
//                    if((mStringFilterList.get(i).getFull_name().toUpperCase())
//                            .contains(constraint.toString().toUpperCase())) {
//                        SearchTempModel contacts = new SearchTempModel();
//                        contacts.setFull_name(mStringFilterList.get(i).getFull_name());
//                        contacts.setPersonal_number(mStringFilterList.get(i).getPersonal_number());
//                        filterList.add(contacts);
//                    }
//                }
//                results.count=filterList.size();
//                results.values=filterList;
//            }else{
//                results.count=mStringFilterList.size();
//                results.values=mStringFilterList;
//            }
//            return results;
//        }
//
//
//        //Invoked in the UI thread to publish the filtering results in the user interface.
//        @SuppressWarnings("unchecked")
//        @Override
//        protected void publishResults(CharSequence constraint,
//                                      FilterResults results) {
//            _Contacts=(ArrayList<SearchTempModel>) results.values;
//            notifyDataSetChanged();
//        }
//    }
//}
