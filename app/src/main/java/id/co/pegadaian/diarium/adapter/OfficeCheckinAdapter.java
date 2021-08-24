package id.co.pegadaian.diarium.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

import id.co.pegadaian.diarium.R;
import id.co.pegadaian.diarium.model.OfficeCheckinModel;

public class OfficeCheckinAdapter extends ArrayAdapter<OfficeCheckinModel> {

    public OfficeCheckinAdapter(@NonNull Context context, @NonNull ArrayList<OfficeCheckinModel> spinnerList) {
        super(context, 0, spinnerList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return super.getView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return super.getDropDownView(position, convertView, parent);
    }

    private View initView(int position,View convertView,ViewGroup parent){

        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.spinner_item,parent,false);
        }
        TextView spinnerTextSelectedItem = convertView.findViewById(R.id.tv_spinner_selected_text);

        OfficeCheckinModel currentItem = getItem(position);
        Log.d("positionspinner", "INIDIA" + position);

        if (currentItem != null) {
            spinnerTextSelectedItem.setText(currentItem.getMspinnerList());
        }
        return convertView;
    }
}
