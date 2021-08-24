package id.co.pegadaian.diarium.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import id.co.pegadaian.diarium.R;
import id.co.pegadaian.diarium.controller.home.main_menu.myevent.ems.EventActivity;
import id.co.pegadaian.diarium.model.EventModel;
import id.co.pegadaian.diarium.util.UserSessionManager;

public class EventAdapter extends BaseAdapter {
    private Context mContext;
    private EventModel model;
    private List<EventModel> listModel;
    private TextView tvTitle, tvDate, tvLocation;
    private ImageView ivObat;
    String typeEvent;
    Typeface font,fontbold;
    Button btnDetail;
    UserSessionManager session;

    public EventAdapter(Context mContext, List<EventModel> listModel, String typeEvent) {
        this.mContext = mContext;
        this.listModel = listModel;
        this.typeEvent = typeEvent;
        session = new UserSessionManager(mContext);
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
    public View getView(final int position, View view, ViewGroup viewGroup) {
        model = listModel.get(position);
        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            view = inflater.inflate(R.layout.item_list_event, null);
        }
        tvTitle = view.findViewById(R.id.tvTitle);
        tvDate = view.findViewById(R.id.tvDate);
        tvLocation = view.findViewById(R.id.tvLocation);
        btnDetail = view.findViewById(R.id.btnDetail);

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date value_start = null;
        Date value_end = null;
        try {
            value_start = formatter.parse(model.getEvent_start());
            value_end = formatter.parse(model.getEvent_end());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat dateFormatter = new SimpleDateFormat("dd MMM yyyy");
        dateFormatter.setTimeZone(TimeZone.getDefault());
        String dt_start = dateFormatter.format(value_start);
        String dt_end = dateFormatter.format(value_end);

        tvTitle.setText(model.getName());
        tvDate.setText(dt_start+" - "+dt_end);
        tvLocation.setText(model.getLocation());
        ivObat = (ImageView) view.findViewById(R.id.ivEvent);
        if (!model.getImage().equals("")) {
            Picasso.get().load(model.getImage()).error(R.drawable.placeholder_gallery).into(ivObat);
        }
        btnDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                session.setEventId(listModel.get(position).getId());
                Intent i = new Intent(mContext, EventActivity.class);
                i.putExtra("name", listModel.get(position).getName());
                i.putExtra("id", listModel.get(position).getId());
                i.putExtra("image", listModel.get(position).getImage());
                mContext.startActivity(i);
            }
        });
//        if (typeEvent.equals("N")) {
//        } else {
//            btnDetail.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    String url = listModel.get(position).getLink();
//                    Intent i = new Intent(Intent.ACTION_VIEW);
//                    i.setData(Uri.parse(url));
//                    mContext.startActivity(i);
//                }
//            });
//        }

        return view;
    }
}
