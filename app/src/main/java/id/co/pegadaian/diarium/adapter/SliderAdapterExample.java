package id.co.pegadaian.diarium.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.smarteist.autoimageslider.SliderViewAdapter;
import java.util.ArrayList;
import java.util.List;
import id.co.pegadaian.diarium.R;
import id.co.pegadaian.diarium.controller.profile.FullFotoActivity;
import id.co.pegadaian.diarium.model.SliderItem;

public class SliderAdapterExample extends
        SliderViewAdapter<SliderAdapterExample.SliderAdapterVH> {

    private Context context;
    private List<SliderItem> mSliderItems = new ArrayList<>();

    public SliderAdapterExample(Context context, List<SliderItem> mSliderItems) {
        this.context = context;
        this.mSliderItems = mSliderItems;
    }

    public void renewItems(List<SliderItem> sliderItems) {
        this.mSliderItems = sliderItems;
        notifyDataSetChanged();
    }

    public void deleteItem(int position) {
        this.mSliderItems.remove(position);
        notifyDataSetChanged();
    }

    public void addItem(SliderItem sliderItem) {
        this.mSliderItems.add(sliderItem);
        notifyDataSetChanged();
    }

    @Override
    public SliderAdapterVH onCreateViewHolder(ViewGroup parent) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_slider_layout_item, null);
        return new SliderAdapterVH(inflate);
    }

    @Override
    public void onBindViewHolder(SliderAdapterVH viewHolder, final int position) {

        SliderItem sliderItem = mSliderItems.get(position);

//        viewHolder.textViewDescription.setText(sliderItem.getImage());
//        viewHolder.textViewDescription.setTextSize(16);
//        viewHolder.textViewDescription.setTextColor(Color.WHITE);

        if (sliderItem.getImage().equals("")){
            viewHolder.imageViewBackground.setImageResource(R.drawable.placeholder);
        } else {
            Glide.with(viewHolder.itemView)
                    .load(sliderItem.getImage())
                    .fitCenter()
                    .into(viewHolder.imageViewBackground);
        }

        String finalBannerImage = sliderItem.getImage();
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, FullFotoActivity.class);
                intent.putExtra("from","home fragment");
                intent.putExtra("banner_image", finalBannerImage);
                System.out.println("check banner image before go to fullfotoactivity : " + finalBannerImage);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getCount() {
        //slider view count could be dynamic size
        return mSliderItems.size();
    }

    class SliderAdapterVH extends SliderViewAdapter.ViewHolder {

        View itemView;
        ImageView imageViewBackground;

        public SliderAdapterVH(View itemView) {
            super(itemView);
            imageViewBackground = itemView.findViewById(R.id.iv_bannerhome);
            this.itemView = itemView;
        }
    }

}
