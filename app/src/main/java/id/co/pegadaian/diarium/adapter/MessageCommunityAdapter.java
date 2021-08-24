package id.co.pegadaian.diarium.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import id.co.pegadaian.diarium.R;
import id.co.pegadaian.diarium.model.MessageContentModel;
import id.co.pegadaian.diarium.util.TimeHelper;
import id.co.pegadaian.diarium.util.UserSessionManager;


/**
 * Created by LENOVO on 29/09/2017.
 */
public class MessageCommunityAdapter extends BaseAdapter {
    private Context mContext;
    private MessageContentModel model;
    private List<MessageContentModel> listModel;
    UserSessionManager session;
    TimeHelper timeHelper;
    public MessageCommunityAdapter(Context mContext, List<MessageContentModel> listModel) {
        this.mContext = mContext;
        this.listModel = listModel;
        session = new UserSessionManager(mContext);
        timeHelper = new TimeHelper();
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
        ViewHolder holder;
        model = listModel.get(position);
        if (view == null) {
            holder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(mContext);
            view = inflater.inflate(R.layout.list_post_community, null);
            holder.tvName = view.findViewById(R.id.tvName);
            holder.tvTitle = view.findViewById(R.id.tvTitle);
            holder.tvDate = view.findViewById(R.id.tvDate);
            holder.tvDesc = view.findViewById(R.id.tvDesc);
            holder.ivImage = view.findViewById(R.id.ivProfile);

            holder.tvName.setText(model.getNama());
            holder.tvTitle.setText(model.getContent_title());
            holder.tvDate.setText(timeHelper.getElapsedTime(model.getChange_date()));
            holder.tvDesc.setText(model.getContent_desc());
            Picasso.get().load(listModel.get(position).getProfile()).error(R.drawable.profile).into(holder.ivImage);

        }


        return view;
    }



    class ViewHolder {
        private TextView tvTitle, tvDate, tvDesc, tvName;
        private ImageView ivImage;
    }
}
