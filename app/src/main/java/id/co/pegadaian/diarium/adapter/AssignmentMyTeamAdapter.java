package id.co.pegadaian.diarium.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import id.co.pegadaian.diarium.R;
import id.co.pegadaian.diarium.model.OurTeamModel;
import id.co.pegadaian.diarium.model.TeamAssignmenModel;

public class AssignmentMyTeamAdapter extends BaseAdapter implements CompoundButton.OnCheckedChangeListener {
    SparseBooleanArray mCheckStates;
    private Context mContext;
    private OurTeamModel model;
    private List<TeamAssignmenModel> listModel;
    private LinearLayout pesan;
    Typeface font,fontbold;

    public AssignmentMyTeamAdapter(Context mContext, List<TeamAssignmenModel> listModel) {
        this.mContext = mContext;
        this.listModel = listModel;
        font = Typeface.createFromAsset(mContext.getAssets(), "fonts/Nexa Light.otf");
        fontbold = Typeface.createFromAsset(mContext.getAssets(), "fonts/Nexa Bold.otf");
        mCheckStates = new SparseBooleanArray(listModel.size());
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
        ViewHolder holder = null;
        LayoutInflater inflator = ((Activity) mContext).getLayoutInflater();
        if (view == null) {

            holder = new ViewHolder();
            view = inflator.inflate(  R.layout.list_team, null);
            holder.ivProfile = (ImageView) view.findViewById(R.id.ivProfile);
            holder.tvName = (TextView) view.findViewById(R.id.tvTitlemyteam);
            holder.tvNik = (TextView) view.findViewById(R.id.nik);
            holder.tvPosition = (TextView) view.findViewById(R.id.tvTitle);
            holder.checkBox = (CheckBox) view.findViewById(R.id.checkBox);
            holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView,
                                             boolean isChecked) {
                    // we get  the position that we have set for the checkbox using setTag.
                    listModel.get(position).setChecked(buttonView.isChecked()); // Set the value
                    if (isChecked) {
                        //do something here
                        listModel.get(position).setChecked(true);
                    }
                    else
                    {
                        listModel.get(position).setChecked(false);
                    }
                }
            });
        }
        else{
            holder = (ViewHolder) view.getTag();
        }
        if (listModel.get(position).getProfile().isEmpty()) {
            holder.ivProfile.setImageResource(R.drawable.profile);
        } else{
            Picasso.get().load(listModel.get(position).getProfile()).error(R.drawable.profile).into(holder.ivProfile);
//        baru
            Picasso.get().load(listModel.get(position).getProfile()).error(R.drawable.profile).into(holder.ivProfile);
        }
//        Picasso.get().load(listModel.get(position).getProfile()).error(R.drawable.profile).into(holder.ivProfile);
        holder.tvName.setText(listModel.get(position).getName());
        holder.tvNik.setText(listModel.get(position).getPersonal_number());
        holder.tvPosition.setText(listModel.get(position).getPosition());
        return view;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

    }
    static class ViewHolder {
        ImageView imgIcon, ivProfile;
        TextView tvName, tvNik, tvPosition;
        CheckBox checkBox;

    }
}