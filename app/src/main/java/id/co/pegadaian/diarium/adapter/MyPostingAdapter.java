package id.co.pegadaian.diarium.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import id.co.pegadaian.diarium.R;
import id.co.pegadaian.diarium.controller.home.main_menu.posting.PostingLikesDetail;
import id.co.pegadaian.diarium.controller.profile.DetailpostActivity;
import id.co.pegadaian.diarium.model.MyPostingModel;
import id.co.pegadaian.diarium.util.TimeHelper;
import id.co.pegadaian.diarium.util.UserSessionManager;

public class MyPostingAdapter extends BaseAdapter {
    private Context mContext;
    private MyPostingModel model;
    private List<MyPostingModel> listModel;
    private TextView tvTitle, tvName, tvTime, tvContent, tvCountLike, tvCountComment;
    private ImageView ivShare, ivPost, ivLike, ivProfile;
    TimeHelper timeHelper;
    UserSessionManager session;

    public MyPostingAdapter(Context mContext, List<MyPostingModel> listModel) {
        this.mContext = mContext;
        this.listModel = listModel;
        timeHelper = new TimeHelper();
        session= new UserSessionManager(mContext);
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
        model = listModel.get(position);
        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            view = inflater.inflate(R.layout.item_list_post, null);
        }

        ivShare = view.findViewById(R.id.ivShare);
        ivLike = view.findViewById(R.id.ivLike);
        ivPost = view.findViewById(R.id.ivPost);
        ivProfile = view.findViewById(R.id.ivProfile);

        tvName = view.findViewById(R.id.tvName);
        tvTitle = view.findViewById(R.id.tvTitle);
        tvTime = view.findViewById(R.id.tvDate);
        tvContent = view.findViewById(R.id.tvDesc);
        tvCountLike = view.findViewById(R.id.tvCountLike);
        tvCountComment = view.findViewById(R.id.tvCountComment);

//      sini
        try {
            Picasso.get().load(listModel.get(position).getProfile()).error(R.drawable.profile).into(ivProfile);
        } catch (Exception e){
            Picasso.get().load(R.drawable.profile).into(ivProfile);
        }


//        if (listModel.get(position).isStatus_like()) {
//            ivLike.setImageResource(R.drawable.like_true);
//        } else {
//            ivLike.setImageResource(R.drawable.like_false);
//        }

        ivShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(Intent.EXTRA_SUBJECT, listModel.get(position).getTitle()+" - "+listModel.get(position).getDescription());
                sharingIntent.putExtra(Intent.EXTRA_TEXT, listModel.get(position).getImage());
                mContext.startActivity(Intent.createChooser(sharingIntent, "Share via"));
            }
        });

        if (!model.getImage().equals("")) {
//            Picasso.get().load(listModel.get(position).getAvatar()).error(R.drawable.profile).into(ivProfile);
//            Picasso.get().load(model.getImage()).resize(700, 700).error(R.drawable.placeholder_gallery).into(ivPost);
            Picasso.get().load(model.getImage()).resize(700,700).into(ivPost);
        }else {
            System.out.println("check image di posting adapter : " + model.getImage());
            Picasso.get().load(R.drawable.placeholder_gallery).into(ivPost);
        }


        tvTitle.setText(listModel.get(position).getTitle());
        tvName.setText(listModel.get(position).getName());
        tvTime.setText(timeHelper.getElapsedTime(listModel.get(position).getChange_date()));
        tvContent.setText(listModel.get(position).getDescription());

        // comments
        tvCountComment.setText(listModel.get(position).getJmlComment()+" Comments");
        tvCountComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, DetailpostActivity.class);
                intent.putExtra("posting_id",listModel.get(position).getPosting_id());
                intent.putExtra("title",listModel.get(position).getTitle());
                intent.putExtra("description",listModel.get(position).getDescription());
                intent.putExtra("date",listModel.get(position).getDate());
                intent.putExtra("image",listModel.get(position).getImage());
                intent.putExtra("name",listModel.get(position).getName());
                intent.putExtra("avatar",listModel.get(position).getProfile());
                intent.putExtra("status_like",listModel.get(position).getIsLiked());
                intent.putExtra("description",listModel.get(position).getDescription());
                mContext.startActivity(intent);
            }
        });

        // likes
        tvCountLike.setText(listModel.get(position).getJmlLoveLike()+" Likes");
        tvCountLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mContext, PostingLikesDetail.class);
                intent.putExtra("posting_id",listModel.get(position).getPosting_id());
                mContext.startActivity(intent);

            }
        });



//        ivProfile.setImageResource(listModel.get(position).getImage());
//        Picasso.get().load(listModel.get(position).getImage()).error(R.drawable.profile).into(ivProfile);

        return view;
    }

    private void submitDisLike(final boolean stat, final String posting_id ,final String identifier) {
        AndroidNetworking.delete(session.getServerURL()+"users/posting/lovelike?object_identifier="+identifier)
                .addHeaders("Accept","application/json")
                .addHeaders("Content-Type","application/json")
                .addHeaders("Authorization",session.getToken())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println(response+"RESPONDISLIKEDARIDEPAN");
                        // do anything with response
                        try {
                            if(response.getInt("status")==200){
                                notifyDataSetChanged();
                                if (stat) {
                                    ivLike.setImageResource(R.drawable.like_true);
                                } else {
                                    ivLike.setImageResource(R.drawable.like_false);
                                }
                            }
                        }catch (Exception e){
                            System.out.println("aaa"+e);
                        }
                    }
                    @Override
                    public void onError(ANError error) {
                        System.out.println("bbb"+error);
                    }
                });
    }

    private void submitLike(final boolean stat, final String posting_id) {
        SimpleDateFormat tgl = new SimpleDateFormat("yyyy-MM-dd");
        String tRes = tgl.format(new Date());

        SimpleDateFormat jam = new SimpleDateFormat("HH:mm");
        String jamRes = jam.format(new Date());

        SimpleDateFormat commentId = new SimpleDateFormat("yyyyMMddHHmmss");
        String commentIdRes = commentId.format(new Date());

        JSONObject jsonObject = new JSONObject();
//        JSONArray jsonArray = new JSONArray();
        try {
            jsonObject.put("begin_date", tRes);
            jsonObject.put("end_date", "9999-12-31");
            jsonObject.put("business_code", session.getUserBusinessCode());
            jsonObject.put("personal_number", session.getUserNIK());
            jsonObject.put("posting_id", posting_id);
            jsonObject.put("lovelike_id", "1");
            jsonObject.put("change_user", session.getUserNIK());
//            jsonArray.put(jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        System.out.println(jsonObject+"PARAMLIKE");
        AndroidNetworking.post(session.getServerURL()+"users/posting/lovelike")
                .addHeaders("Accept","application/json")
                .addHeaders("Content-Type","application/json")
                .addHeaders("Authorization",session.getToken())
                .addJSONObjectBody(jsonObject)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println(response+"RESPONLIKEDEPAN");
                        // do anything with response
                        try {
                            if(response.getInt("status")==200){
                                notifyDataSetChanged();
                                if (stat) {
                                    ivLike.setImageResource(R.drawable.like_true);
                                } else {
                                    ivLike.setImageResource(R.drawable.like_false);
                                }
                            }
                        }catch (Exception e){
                            System.out.println("aaa"+e);
                        }
                    }
                    @Override
                    public void onError(ANError error) {
                        System.out.println("bbb"+error);
                    }
                });
    }
}
