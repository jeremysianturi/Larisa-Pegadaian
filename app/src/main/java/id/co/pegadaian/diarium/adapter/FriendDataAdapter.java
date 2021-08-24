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

import java.util.List;

import id.co.pegadaian.diarium.R;
import id.co.pegadaian.diarium.model.FriendsModel;
import id.co.pegadaian.diarium.util.UserSessionManager;

public class FriendDataAdapter extends BaseAdapter {
    private Context mContext;
    private FriendsModel model;
    private List<FriendsModel> listModel;
    private TextView tvTitle, tvDate, tvDesc;
    private ImageView ivObat;
    private LinearLayout pesan;
    private Button btnConfirm, btnDescline;
    Dialog myDialog;
    Typeface font,fontbold;
    UserSessionManager session;

    public FriendDataAdapter(Context mContext, List<FriendsModel> listModel) {
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
            view = inflater.inflate(R.layout.item_list_friends, null);
        }
        tvTitle = view.findViewById(R.id.name);
        tvDate = view.findViewById(R.id.nik);
        btnConfirm = view.findViewById(R.id.confirm);
        btnDescline = view.findViewById(R.id.reject);
        tvTitle.setText(model.getPersonal_number());
        tvDate.setText(model.getBegin_date());

        return view;
    }
}