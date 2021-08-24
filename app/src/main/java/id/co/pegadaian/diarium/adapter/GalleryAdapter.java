package id.co.pegadaian.diarium.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

import id.co.pegadaian.diarium.R;
import id.co.pegadaian.diarium.model.GalleryModel;

public class GalleryAdapter extends BaseAdapter {
    private List<GalleryModel> mDataList;
    private GalleryModel model;
    private Context mContext;

    public GalleryAdapter(Context context, List<GalleryModel> dataList) {
        mContext = context;
        mDataList = dataList;
    }

    @Override
    public int getCount() {
        return mDataList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Object getItem(int position) {
        return mDataList.get(position);
    }

    @Override
    public int getViewTypeCount() {
        return 5;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View getView(int position, View view, ViewGroup parent) {
        model = mDataList.get(position);

        // inflating list view layout if null
        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            view = inflater.inflate(R.layout.item_grid_gallery, null);
        }

        ImageView mImageView = view.findViewById(R.id.iv_grid_gallery);

        try {
            Picasso.get().load(model.getImage()).error(R.drawable.placeholder_gallery).into(mImageView);
            System.out.println("berhasil load image: " + model.getImage());
        } catch (Exception e){
            Picasso.get().load(R.drawable.placeholder_gallery).into(mImageView);
        }

// picasso yang lama nya
//        Picasso.get().load(model.getImage()).error(R.drawable.placeholder_gallery).into(mImageView);
// picasso yang lama nya

//        if (model.getGallery_file() != null) {
//            String imgURL = model.getGallery_file();
////            downloadImage(mContext, imgURL, mImageView);
//            mImageView.setBackground(null);
//        } else {
//            mImageView.setImageResource(R.drawable.icon_expo);
//        }
        return view;
    }

//    private void downloadImage(Context context, String url, ImageView image) {
//        Picasso.with(context)
//                .load(url)
//                .error(R.drawable.icon_expo)
//                .into(image);
//    }
}
