package id.co.pegadaian.diarium.adapter;

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
import id.co.pegadaian.diarium.model.FamilyDataModel;
import id.co.pegadaian.diarium.model.PersonalDataModel;
import id.co.pegadaian.diarium.util.UserSessionManager;

public class FamilyDataAdapter extends BaseAdapter {

    private Context mContext;
    private FamilyDataModel model;
    private List<FamilyDataModel> listModel;
    private TextView tvName, tvStatus;
    private ImageView tvImage;
    private LinearLayout pesan;
    UserSessionManager session;

    public FamilyDataAdapter(Context context, List<FamilyDataModel> listModel) {
        mContext = context;
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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        model = listModel.get(position);
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(R.layout.lay_item_family_data, null);
        }

        tvName = convertView.findViewById(R.id.tvNameFamily);
        tvStatus = convertView.findViewById(R.id.tvFamilyValue);
        tvImage = convertView.findViewById(R.id.iv_family);

        String famNumber = listModel.get(position).getNumber();
        String famStatus = listModel.get(position).getValue();
        String relation = "";
        String number;

        if (famStatus.equals("I")){
            relation = "Istri";
        }else if (famStatus.equals("S")){
            relation = "Suami";
        }else if (famStatus.equals("A")){
            relation = "Anak";
        } else if (famStatus.equals("F")){
            relation = "Ayah";
        } else if (famStatus.equals("M")){
            relation = "Ibu";
        } else if (famStatus.equals("L")){
            relation = "Mertua";
        } else {
            relation = "Data not valid";
        }

        System.out.println("check value fam number di adapterfam: " + famNumber);

        tvName.setText(listModel.get(position).getName());
        tvStatus.setText(relation + " ke-" + famNumber);

        return convertView;
    }
}
