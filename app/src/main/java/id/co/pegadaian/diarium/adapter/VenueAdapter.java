package id.co.pegadaian.diarium.adapter;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import id.co.pegadaian.diarium.R;
import id.co.pegadaian.diarium.model.VenueModel;

public class VenueAdapter extends BaseAdapter {
    private Context mContext;
    private VenueModel model;
    private List<VenueModel> listModel;
    private TextView tvTitle, tvDate, tvPlace;
    private Button btnMaps;
    private ImageView ivObat;
    private LinearLayout pesan;

    public VenueAdapter(Context mContext, List<VenueModel> listModel) {
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
    public View getView(final int position, View view, ViewGroup viewGroup) {
        model = listModel.get(position);
        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            view = inflater.inflate(R.layout.item_list_venue, null);
        }
        tvTitle = view.findViewById(R.id.tvTitle);
        tvDate = view.findViewById(R.id.tvDate);
        tvPlace = view.findViewById(R.id.tvPlace);
        pesan = view.findViewById(R.id.pesan);
        btnMaps = view.findViewById(R.id.btnMaps);
        ivObat = view.findViewById(R.id.ivVenue);
        tvTitle.setText(model.getTitle());
        tvDate.setText(model.getBegin_date());
        tvPlace.setText(model.getVenue_desc());
        if (model.getImage().isEmpty()) {
            ivObat.setImageResource(R.drawable.placeholder_gallery);
        } else{
            Picasso.get().load(model.getImage()).error(R.drawable.profile).into(ivObat);
        }
        btnMaps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://maps.google.com/maps?daddr="+listModel.get(position).getLatitude()+","+listModel.get(position).getLongitude()));
                mContext.startActivity(intent);
            }
        });

        return view;
    }
}
