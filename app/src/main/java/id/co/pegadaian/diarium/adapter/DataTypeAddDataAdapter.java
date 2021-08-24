package id.co.pegadaian.diarium.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

import id.co.pegadaian.diarium.R;
import id.co.pegadaian.diarium.model.DataTypeAddDataModel;

public class DataTypeAddDataAdapter extends ArrayAdapter<DataTypeAddDataModel> {

    public DataTypeAddDataAdapter(@NonNull Context context, @NonNull ArrayList<DataTypeAddDataModel> spinnerList) {
        super(context,0, spinnerList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        System.out.println("Position Spinner Data Type: " + position);
        return initView(position,convertView,parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position,convertView,parent);
    }


    private View initView(int position,View convertView,ViewGroup parent){
        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.spinner_item,parent,false);
        }
        convertView = LayoutInflater.from(getContext()).inflate(R.layout.spinner_add_datatype_personaldata,parent,false);
        TextView spinnerTextSelectedItem = convertView.findViewById(R.id.tv_spinner_add_datatype);

        DataTypeAddDataModel currentItem = getItem(position);
        Log.d("positionspinner", "INIDIA" + position);

        if (currentItem != null) {
            spinnerTextSelectedItem.setText(currentItem.getDataType());
        }
        return convertView;
    }

}
