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

import com.squareup.picasso.Picasso;

import java.util.List;

import id.co.pegadaian.diarium.R;
import id.co.pegadaian.diarium.model.SearchTempModel;

public class AddMemberAdapter extends BaseAdapter {
    private Context mContext;
    private SearchTempModel model;
    private List<SearchTempModel> listModel;
    private TextView tvName, tvNik, tvJob;
    private ImageView ivProfile;
    private LinearLayout pesan;

    public AddMemberAdapter(Context mContext, List<SearchTempModel> listModel) {
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
            view = inflater.inflate(R.layout.item_list_team, null);
        }
        tvName = view.findViewById(R.id.tvName);
        tvNik = view.findViewById(R.id.tvNik);
        tvJob = view.findViewById(R.id.tvJob);
        ivProfile = view.findViewById(R.id.ivProfile);

        tvName.setText(model.getFull_name());
        tvNik.setText(model.getPersonal_number());
        tvJob.setText(model.getPosisi());

        if (model.getProfile().isEmpty()) {
            ivProfile.setImageResource(R.drawable.profile);
        } else{
            Picasso.get().load(model.getProfile()).error(R.drawable.profile).into(ivProfile);
        }



        return view;
    }
}

