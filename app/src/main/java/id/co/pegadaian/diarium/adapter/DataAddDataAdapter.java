package id.co.pegadaian.diarium.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import id.co.pegadaian.diarium.R;
import id.co.pegadaian.diarium.model.DataAddDataModel;

public class DataAddDataAdapter extends BaseAdapter {

    private Context mContext;
    private DataAddDataModel model;
    private List<DataAddDataModel> listModel;
    private TextView tvSpinnerData;

    public DataAddDataAdapter(Context context, List<DataAddDataModel> listModel) {
        mContext = context;
        this.listModel = listModel;
    }

    @Override
    public int getCount() {return listModel.size();}

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        model = listModel.get(position);
        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(R.layout.spinner_add_data_personaldata, null);

        tvSpinnerData = convertView.findViewById(R.id.tv_spinner_add_data);
        tvSpinnerData.setText(model.getDataName());

        return convertView;
    }
}
