package id.co.pegadaian.diarium.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.Html;
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
import id.co.pegadaian.diarium.controller.home.main_menu.myevent.ems.news.DetailNewsActivity;
import id.co.pegadaian.diarium.model.NewsModel;

/**
 * Created by ITDE on 04/12/2018.
 */


public class NewsAdapter extends BaseAdapter {
    private Context mContext;
    private NewsModel model;
    private List<NewsModel> listModel;
    private TextView tvTitle, tvTime, tvDesc;
    private ImageView ivLogo;
    private Button btnDetail;

    public NewsAdapter(Context mContext, List<NewsModel> listModel) {
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

    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {
        model = listModel.get(position);
        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            view = inflater.inflate(R.layout.item_list_news, null);
        }

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date value = null;
        try {
            value = formatter.parse(model.getBegin_date());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat dateFormatter = new SimpleDateFormat("dd MMM yyyy");
        dateFormatter.setTimeZone(TimeZone.getDefault());
        String dt = dateFormatter.format(value);

        ivLogo = view.findViewById(R.id.ivLogo);
        tvTitle = view.findViewById(R.id.tvTitle);
        tvTime = view.findViewById(R.id.tvDate);
        tvDesc = view.findViewById(R.id.tvDesc);
        btnDetail = view.findViewById(R.id.btnDetail);

        tvTitle.setText(model.getTitle());
        tvTime.setText(dt);
        tvDesc.setText(Html.fromHtml(model.getDescription()));
        if (model.getImage().isEmpty()) {
            ivLogo.setImageResource(R.drawable.placeholder_gallery);
        } else{
            Picasso.get().load(model.getImage()).error(R.drawable.placeholder_gallery).into(ivLogo);
        }
//        downloadImage(mContext, model.getMedia_center_image(), ivLogo);
        btnDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, DetailNewsActivity.class);
                intent.putExtra("title", listModel.get(position).getTitle());
                intent.putExtra("desc", listModel.get(position).getDescription());
                intent.putExtra("image", listModel.get(position).getImage());
                intent.putExtra("date", dt);
                mContext.startActivity(intent);
            }
        });
        return view;
    }
//
//    private void downloadImage(Context context, String url, ImageView image) {
//        Picasso.with(context)
//                .load(url)
//                .fit()
//                .error(R.drawable.icon_expo)
//                .into(image);
//    }
}