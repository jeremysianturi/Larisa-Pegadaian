package id.co.pegadaian.diarium.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
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
import id.co.pegadaian.diarium.model.OurTeamModel;

public class OurTeamAdapter extends BaseAdapter {
    private Context mContext;
    private OurTeamModel model;
    private List<OurTeamModel> listModel;
    private TextView tvTitle, tvDate, tvDesc, tvLeader;
    private ImageView ivObat;
    private LinearLayout pesan;
    Typeface font,fontbold;

    public OurTeamAdapter(Context mContext, List<OurTeamModel> listModel) {
        this.mContext = mContext;
        this.listModel = listModel;
        font = Typeface.createFromAsset(mContext.getAssets(), "fonts/Nexa Light.otf");
        fontbold = Typeface.createFromAsset(mContext.getAssets(), "fonts/Nexa Bold.otf");
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
            view = inflater.inflate(R.layout.item_list_ourteam, null);
        }

        TextView namaa = view.findViewById(R.id.tvName);
        TextView tvLeader = view.findViewById(R.id.tvLeader);
        ImageView ivProfile = view.findViewById(R.id.ivProfile);
        namaa.setText(model.getName());
        namaa.setTypeface(fontbold);

        if (model.getLead_posisi().equals("-")) {
            tvLeader.setVisibility(View.INVISIBLE);
        } else {
            tvLeader.setText(model.getLead_posisi());
        }
        TextView nikk= view.findViewById(R.id.tvNik);
        nikk.setText(model.getPersonal_number());
        nikk.setTypeface(font);

        TextView jabaatan = (TextView) view.findViewById(R.id.tvJob);
        jabaatan.setText(model.getPosition());
        jabaatan.setTypeface(fontbold);
        if (model.getProfile().isEmpty()) {
            ivProfile.setImageResource(R.drawable.profile);
        } else{
            Picasso.get().load(model.getProfile()).error(R.drawable.profile).into(ivProfile);
        }


        return view;
    }
}