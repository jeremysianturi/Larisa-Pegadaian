package id.co.pegadaian.diarium.adapter;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.media.MediaMetadataRetriever;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;

import id.co.pegadaian.diarium.R;
import id.co.pegadaian.diarium.model.KnowledgeModel;
import id.co.pegadaian.diarium.util.TimeHelper;

public class KnowledgeAdapter extends BaseAdapter {
    private Context mContext;
    private KnowledgeModel model;
    private List<KnowledgeModel> listModel;
    private TextView tvScheduleName, tvTopic, tvDate, tvTime;
    private ImageView ivIcon;
    String typeEvent;
    TimeHelper timeHelper;
    Typeface font,fontbold;

    public KnowledgeAdapter(Context mContext, List<KnowledgeModel> listModel) {
        this.mContext = mContext;
        this.listModel = listModel;
        timeHelper = new TimeHelper();
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
            view = inflater.inflate(R.layout.item_list_knowledge, null);
        }
        tvScheduleName = view.findViewById(R.id.tvTitle);
        ivIcon = view.findViewById(R.id.ivIcon);
        tvScheduleName.setText(model.getMateri_name());
        if (model.getAddress().equals("")) {
            ivIcon.setImageResource(R.drawable.placeholder_gallery);
        } else {
            System.out.println("HASILPOTONG"+String.valueOf(model.getAddress().substring(model.getAddress().length()-3)));
            if (model.getAddress().substring(model.getAddress().length()-3).equals("pdf")) {
                ivIcon.setImageResource(R.drawable.ic_pdf);
            } else {
                Picasso.get().load(model.getAddress()).error(R.drawable.ic_video).into(ivIcon);

//
//                Glide.with(mContext)
//                        .asBitmap()
//                        .load(model.getAddress())
//                        .diskCacheStrategy(DiskCacheStrategy.DATA)
//                        .into(ivIcon);
//                try {
//                    ivIcon.setImageBitmap(retriveVideoFrameFromVideo(model.getAddress()));
//                } catch (Throwable throwable) {
//                    throwable.printStackTrace();
//                }
//                ivIcon.setImageResource(R.drawable.ic_video);
            }
        }
        tvTopic = view.findViewById(R.id.tvTopic);
        tvDate = view.findViewById(R.id.tvDate);
        tvTime = view.findViewById(R.id.tvTime);

        return view;
    }

    public static Bitmap retriveVideoFrameFromVideo(String videoPath)throws Throwable
    {
        Bitmap bitmap = null;
        MediaMetadataRetriever mediaMetadataRetriever = null;
        try
        {
            mediaMetadataRetriever = new MediaMetadataRetriever();
            if (Build.VERSION.SDK_INT >= 14)
                mediaMetadataRetriever.setDataSource(videoPath, new HashMap<String, String>());
            else
                mediaMetadataRetriever.setDataSource(videoPath);
            //   mediaMetadataRetriever.setDataSource(videoPath);
            bitmap = mediaMetadataRetriever.getFrameAtTime(1, MediaMetadataRetriever.OPTION_CLOSEST);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            throw new Throwable("Exception in retriveVideoFrameFromVideo(String videoPath)"+ e.getMessage());
        }
        finally
        {
            if (mediaMetadataRetriever != null)
            {
                mediaMetadataRetriever.release();
            }
        }
        return bitmap;
    }
}