package id.co.pegadaian.diarium.adapter;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

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
import java.util.logging.Handler;

import id.co.pegadaian.diarium.R;
import id.co.pegadaian.diarium.controller.HomeActivity;
import id.co.pegadaian.diarium.controller.inbox.InboxFragment;
import id.co.pegadaian.diarium.controller.login.LoginActivity;
import id.co.pegadaian.diarium.controller.splashscreen.SplashscreenActivity;
import id.co.pegadaian.diarium.model.FriendsModel;
import id.co.pegadaian.diarium.util.UserSessionManager;


/**
 * Created by LENOVO on 29/09/2017.
 */

public class FriendsAdapter extends BaseAdapter {
    private Context mContext;
    private FriendsModel model;
    private List<FriendsModel> listModel;
    private TextView tvTitle, tvDate, tvDesc, tvDateInbox;
    private ImageView ivProfile;
    private LinearLayout pesan;
    private Button btnConfirm, btnDescline;
    private LinearLayout mLinearLayout, llAction;
    InboxFragment mInboxFragment;
    Dialog myDialog;
    Typeface font,fontbold;
    UserSessionManager session;

    public FriendsAdapter(Context mContext, List<FriendsModel> listModel) {
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

        mLinearLayout = view.findViewById(R.id.linearlayout_friendreq_adapter);
        tvTitle = view.findViewById(R.id.name);
        tvDate = view.findViewById(R.id.nik);
        btnConfirm = view.findViewById(R.id.confirm);
        btnDescline = view.findViewById(R.id.reject);
        ivProfile = view.findViewById(R.id.ivProfile);
        tvDateInbox = view.findViewById(R.id.tv_date_forinbox);
        llAction = view.findViewById(R.id.ll_action_friendrequset);

            tvDateInbox.setVisibility(View.GONE);

            if (model.getProfile().isEmpty()) {
                ivProfile.setImageResource(R.drawable.profile);
            } else{
                Picasso.get().load(model.getProfile()).error(R.drawable.profile).into(ivProfile);
            }
            btnConfirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                ShowPopupApprove(model.getPersonal_number());
                    System.out.println("masuk ke confirm friend request");
                    approveFriends(model.getFull_name(), model.getPersonal_number_teman());
                    btnConfirm.setVisibility(View.GONE);
                    btnDescline.setVisibility(View.GONE);
                    llAction.setVisibility(View.GONE);

                }
            });

            btnDescline.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                ShowPopupDecline(model.getPersonal_number());
                    System.out.println("masuk ke reject friend request");
                    declineFriends(model.getFull_name(), model.getPersonal_number_teman());
                    btnConfirm.setVisibility(View.GONE);
                    btnDescline.setVisibility(View.GONE);
                    llAction.setVisibility(View.GONE);

//                    FragmentManager manager = ((AppCompatActivity)mContext).getSupportFragmentManager();
//                    FragmentTransaction fragmentTransaction = manager.beginTransaction();
//                    InboxFragment fragment= new InboxFragment();
////                    fragmentTransaction.replace(R.id.inboxfrag_layout, fragment,"tag");
//                    fragmentTransaction.commit();

                }
            });

            tvTitle.setText(model.getFull_name());
            tvDate.setText(model.getBegin_date());


        return view;
    }

    private void approveFriends(final String nama_friend, final String friends) {
        SimpleDateFormat tgl = new SimpleDateFormat("yyyy-MM-dd");
        String tRes = tgl.format(new Date());

        SimpleDateFormat jam = new SimpleDateFormat("HH:mm");
        String jamRes = jam.format(new Date());

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("begin_date", tRes);
            jsonObject.put("end_date", "9999-12-31");
            jsonObject.put("business_code", session.getUserBusinessCode());
            jsonObject.put("personal_number", session.getUserNIK());
            jsonObject.put("friend", friends);
            jsonObject.put("change_user", session.getUserNIK());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        AndroidNetworking.post(session.getServerURL()+"users/"+session.getUserNIK()+"/approveFriend/"+friends)
                .addHeaders("Accept","application/json")
                .addHeaders("Content-Type","application/json")
                .addHeaders("Authorization",session.getToken())
                .addJSONObjectBody(jsonObject)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println(response+"KONFIRMYA");
                        // do anything with response
                        try {
                            if(response.getInt("status")==200){
                                ShowPopupApprove(nama_friend, friends);
                                delay();
                                
                            }else {
                                Toast.makeText(mContext, "Failed", Toast.LENGTH_SHORT).show();
                            }
                        }catch (Exception e){
                            System.out.println(e);
                        }
                    }
                    @Override
                    public void onError(ANError error) {
                        System.out.println(error);
                    }
                });
    }

    private void declineFriends(final String nama_friend, final String friends) {
        SimpleDateFormat tgl = new SimpleDateFormat("yyyy-MM-dd");
        String tRes = tgl.format(new Date());

        SimpleDateFormat jam = new SimpleDateFormat("HH:mm");
        String jamRes = jam.format(new Date());

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("begin_date", tRes);
            jsonObject.put("end_date", "9999-12-31");
            jsonObject.put("business_code", session.getUserBusinessCode());
            jsonObject.put("personal_number", session.getUserNIK());
            jsonObject.put("friend", friends);
            jsonObject.put("change_user", session.getUserNIK());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        AndroidNetworking.post(session.getServerURL()+"users/"+session.getUserNIK()+"/declineFriend/"+friends)
                .addHeaders("Accept","application/json")
                .addHeaders("Content-Type","application/json")
                .addHeaders("Authorization",session.getToken())
                .addJSONObjectBody(jsonObject)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println(response+"REJECTYA");
                        // do anything with response
                        try {
                            if(response.getInt("status")==200){
                                ShowPopupDecline(nama_friend, friends);
                                listModel.notify();
                                delay();
                            }else {
                                Toast.makeText(mContext, "Failed", Toast.LENGTH_SHORT).show();
                            }
                        }catch (Exception e){
                            System.out.println(e);
                        }
                    }
                    @Override
                    public void onError(ANError error) {
                        System.out.println(error);
                    }
                });
    }

    public void ShowPopupApprove(String nama_friend, String friends) {
        TextView txtapprove;
        Button btnFollow;
        myDialog.setContentView(R.layout.custom_popup);
        txtapprove =(TextView) myDialog.findViewById(R.id.text);
        txtapprove.setTypeface(fontbold);
        txtapprove.setText(nama_friend+" is now your friend");
        txtapprove.setTextColor(Color.GRAY);
//        btnFollow = (Button) myDialog.findViewById(R.id.confirm);
        txtapprove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
            }
        });
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();
    }

    public void ShowPopupDecline(String nama_friend, String friends) {
        TextView txtapprove;
        Button btnFollow;
        myDialog.setContentView(R.layout.custom_popup_decline);
        txtapprove =(TextView) myDialog.findViewById(R.id.text);
        txtapprove.setTypeface(fontbold);
        txtapprove.setText("Success reject "+nama_friend);
        txtapprove.setTextColor(Color.GRAY);
//        btnFollow = (Button) myDialog.findViewById(R.id.confirm);
        txtapprove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
            }
        });
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();
    }

    private void delay(){
        Thread splashTread = new Thread() {
            @Override
            public void run() {

                try {
                    sleep(2000);
                } catch (InterruptedException e) {
                    System.out.println("failed di action friend request: " + e);
                    // do nothing
                } finally {

                    if(session.isLogin()) {
                        Intent i = new Intent(mContext, HomeActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        mContext.startActivity(i);
                    }else {
                        Intent i = new Intent(mContext, LoginActivity.class);
                        mContext.startActivity(i);

                    }

                }
            }
        };
        splashTread.start();
    }
}