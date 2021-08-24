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
import id.co.pegadaian.diarium.model.CommunityParticipantModel;


/**
 * Created by LENOVO on 29/09/2017.
 */


public class MemberCommunityAdapter extends BaseAdapter {
    private Context mContext;
    private CommunityParticipantModel model;
    private List<CommunityParticipantModel> listModel;
    private TextView tvName, tvNIK, tvAdmin;
    private ImageView ivProfile;
    private LinearLayout pesan;

    public MemberCommunityAdapter(Context mContext, List<CommunityParticipantModel> listModel) {
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
            view = inflater.inflate(R.layout.item_list_membercommunity, null);
        }
        tvAdmin = view.findViewById(R.id.tvAdmin);
        tvName = view.findViewById(R.id.tvTitle);
        tvNIK = view.findViewById(R.id.tvNIK);
        ivProfile = view.findViewById(R.id.ivProfile);
        if (!model.getCommunity_role().equals("AD")) {
            tvAdmin.setVisibility(View.INVISIBLE);
        }
        tvName.setText(model.getFull_name());
        tvNIK.setText(model.getPersonal_number());
        Picasso.get().load(model.getProfile()).error(R.drawable.profile).into(ivProfile);

        return view;
    }
}
