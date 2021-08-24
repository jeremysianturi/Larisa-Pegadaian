package id.co.pegadaian.diarium.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import id.co.pegadaian.diarium.R;

/**
 * Created by abdalla on 10/29/17.
 */

public class CustomAdapter extends ArrayAdapter<String> {

    String[] spinnerTitles;
    int[] spinnerImages;
    String[] spinnerPopulation;
    Context mContext;
    Typeface font,fontbold;

    public CustomAdapter(@NonNull Context context, String[] titles, int[] images, String[] population) {
        super(context, R.layout.custom_spinner_row);
        this.spinnerTitles = titles;
        this.spinnerImages = images;
        this.spinnerPopulation = population;
        this.mContext = context;

       font = Typeface.createFromAsset(getContext().getAssets(), "fonts/Nexa Light.otf");
        fontbold = Typeface.createFromAsset(getContext().getAssets(),"fonts/Nexa Bold.otf");

    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return getView(position, convertView, parent);
    }

    @Override
    public int getCount() {
        return spinnerTitles.length;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder mViewHolder = new ViewHolder();
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater) mContext.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.custom_spinner_row, parent, false);
            mViewHolder.mFlag = (ImageView) convertView.findViewById(R.id.image);
            mViewHolder.mName = (TextView) convertView.findViewById(R.id.jumlah);
            TextView mNames = convertView.findViewById(R.id.jumlah);
            mNames.setTypeface(fontbold);
            mViewHolder.mPopulation = (TextView) convertView.findViewById(R.id.nama_menu);
            TextView jumlah= convertView.findViewById(R.id.nama_menu);
            jumlah.setTypeface(fontbold);
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }
        mViewHolder.mFlag.setImageResource(spinnerImages[position]);
        mViewHolder.mName.setText(spinnerTitles[position]);
        mViewHolder.mPopulation.setVisibility(View.INVISIBLE);

        return convertView;
    }

    private static class ViewHolder {
        ImageView mFlag;
        TextView mName;
        TextView mPopulation;
    }
}