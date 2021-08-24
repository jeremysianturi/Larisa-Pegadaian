package id.co.pegadaian.diarium.adapter;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Typeface;
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
import id.co.pegadaian.diarium.model.FriendsModel;
import id.co.pegadaian.diarium.util.UserSessionManager;


/**
 * Created by LENOVO on 29/09/2017.
 */

public class ListFriendAdapter extends BaseAdapter {
    private Context mContext;
    private FriendsModel model;
    private List<FriendsModel> listModel;
    private TextView tvTitle, tvDate, tvDesc;
    private ImageView ivProfile;
    private LinearLayout pesan;
    private Button btnConfirm, btnDescline;
    Dialog myDialog;
    Typeface font,fontbold;
    UserSessionManager session;

    public ListFriendAdapter(Context mContext, List<FriendsModel> listModel) {
        this.mContext = mContext;
        this.listModel = listModel;
        myDialog = new Dialog(mContext);
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
    public View getView(int position, View view, ViewGroup viewGroup) {
        model = listModel.get(position);
        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            view = inflater.inflate(R.layout.item_list_data_friend, null);
        }
        tvTitle = view.findViewById(R.id.name);
        tvDate = view.findViewById(R.id.nik);
        ivProfile = view.findViewById(R.id.ivProfile);
        if (!model.getFull_name().equals("")) {
            tvTitle.setText(model.getFull_name());
        } else {
            tvTitle.setText("No Name");
        }
        tvDate.setText(model.getPersonal_number_teman());
        if (model.getProfile().isEmpty()) {
            ivProfile.setImageResource(R.drawable.profile);
        } else{
            Picasso.get().load(model.getProfile()).error(R.drawable.profile).into(ivProfile);
        }
        return view;
    }
}