package id.co.pegadaian.diarium.controller.profile;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import id.co.pegadaian.diarium.R;
import id.co.pegadaian.diarium.adapter.MyPostingAdapter;
import id.co.pegadaian.diarium.controller.HomeActivity;
import id.co.pegadaian.diarium.controller.login.LoginActivity;
import id.co.pegadaian.diarium.model.MyPostingModel;
import id.co.pegadaian.diarium.util.UserSessionManager;
import id.co.pegadaian.diarium.util.element.ProgressDialogHelper;


public class ProfileActivity extends AppCompatActivity {

    ListView listMyPosting;
    Typeface font,fontbold;
    private ProgressDialogHelper progressDialogHelper;
    private List<MyPostingModel> listModel;
    private MyPostingModel model;
    private MyPostingAdapter adapter;
    UserSessionManager session;
    TextView tvNullPost, tvCountFriend, tvCountCommunity, tvName, tvNIK, tvJob, tvLokasi, tvFollow;
    String personal_numbe_paramr, avatar, personal_number, full_name, address, profile;
    Dialog dialog;
    LinearLayout lay_listfriend;
    ImageView ivProfile;
    boolean apakahtemen = false;
    int panjang;
    String statusFriend;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Intent intent = getIntent();
        personal_numbe_paramr = intent.getStringExtra("personal_number");
        System.out.println("check personal number param dr intent : " + personal_numbe_paramr);
        avatar = intent.getStringExtra("avatar");
        System.out.println("test avatar di profile activity: " + avatar);
        progressDialogHelper = new ProgressDialogHelper();
        session = new UserSessionManager(ProfileActivity.this);
        font = Typeface.createFromAsset(getApplication().getAssets(),"fonts/Nexa Light.otf");
        fontbold = Typeface.createFromAsset(getApplication().getAssets(),"fonts/Nexa Bold.otf");

        ivProfile = findViewById(R.id.ivProfile);
        lay_listfriend = findViewById(R.id.lay_listfriend);
        tvName = findViewById(R.id.tvName);
        tvNIK = findViewById(R.id.tvNIK);
        tvJob = findViewById(R.id.tvJob);
        tvFollow = findViewById(R.id.tv_follow);
        tvLokasi = findViewById(R.id.tvLokasi);
        tvNullPost = findViewById(R.id.tvNullPost);
        TextView e = findViewById(R.id.nama6);
        tvCountFriend = findViewById(R.id.tvCountFriend);
        tvCountCommunity = findViewById(R.id.tvCountCommunity);
        TextView h2 = findViewById(R.id.nama8);
        TextView h3 = findViewById(R.id.nama12);
        listMyPosting = findViewById(R.id.listMyPosting);

        checkMyFriend(personal_numbe_paramr);

        ivProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (personal_numbe_paramr.equals(session.getUserNIK())) {
                    Intent i = new Intent (ProfileActivity.this, FotoProfileActivity.class);
                    startActivity(i);
                } else {
                    Intent i = new Intent (ProfileActivity.this, FullFotoActivity.class);
                    i.putExtra("from","Profile activity");
                    i.putExtra("avatar", profile);
                    startActivity(i);
                }
            }
        });


        tvName.setTypeface(font);
        tvNIK.setTypeface(font);
        tvJob.setTypeface(fontbold);
//        tvName.setText(session.getUserNickName());
//        tvNIK.setText(session.getUserNIK());
//        tvJob.setText(session.getJob());
        e.setTypeface(font);
        tvCountFriend.setTypeface(fontbold);
        tvCountCommunity.setTypeface(fontbold);
        h2.setTypeface(font);
        h3.setTypeface(fontbold);

        lay_listfriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ProfileActivity.this, ListFriendActivity.class);
                i.putExtra("personal_numbe_paramr",personal_numbe_paramr);
                startActivity(i);
            }
        });

        LinearLayout sent = findViewById(R.id.sendpost);
        if (session.getUserNIK().equals(personal_numbe_paramr)) {
            sent.setVisibility(View.VISIBLE);
            sent.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    Intent hariian = new Intent(getApplication() , PostActivity.class);
                    startActivity(hariian);
                    ProfileActivity.this.finish();
//                Toast.makeText(ProfileActivity.this,"Comming soon",Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            sent.setVisibility(View.GONE);
        }


        getPersonalData(personal_numbe_paramr);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


//        getStatusFriend(personal_numbe_paramr,panjang);

    }

    @Override
    public void onResume(){
        super.onResume();
        getPersonalData(personal_numbe_paramr);
    }

    private void getStatusFriend(String nik, int arrayFriendLength){
        if (!nik.equals(session.getUserNIK())) {
            if (arrayFriendLength==0) {
                System.out.println("TESTFOLLOWBUTTON");
                tvFollow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popUpAddFriend(personal_number, full_name);
                    }
                });
            }
        } else {
            System.out.println("TESTFOLLOWedBUTTON");
            tvFollow.setText("Followed");
            tvFollow.setBackgroundColor(getResources().getColor(R.color.grey));
        }
    }

    private void getPersonalData(String nik){
        System.out.println("Url getPersonalData() : " + session.getServerURL()+"users/"+session.getUserNIK()+"/mypersonal/"+nik );
        System.out.println("TOKEN" + session.getToken());
        progressDialogHelper.showProgressDialog(ProfileActivity.this, "Getting data...");
        AndroidNetworking.get(session.getServerURL()+"users/"+session.getUserNIK()+"/personal/"+nik)
                .addHeaders("Accept","application/json")
                .addHeaders("Content-Type","application/json")
                .addHeaders("Authorization","Bearer "+session.getToken())
                //.addJSONObjectBody(body)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // do anything with response
                        System.out.println(response+"kjwrhbk4jrpersonaldata");
                        try {
                            if(response.getInt("status")==200){
                                personal_number = response.getJSONObject("data").getString("personal_number");
                                full_name = response.getJSONObject("data").getString("full_name");
                                address = response.getJSONObject("data").getString("address");
                                profile = response.getJSONObject("data").getString("profile");
                                JSONArray arrayPosisi = response.getJSONObject("data").getJSONArray("Posisi");
                                for (int i=0;i<arrayPosisi.length();i++) {
                                    JSONObject object = arrayPosisi.getJSONObject(i);
                                    JSONArray arrayPos = object.getJSONArray("posisi");
                                    for (int j=0; j<arrayPos.length(); j++) {
                                        JSONObject objectPos = arrayPos.getJSONObject(j);
                                        String organizational_name = objectPos.getString("organizational_name");
                                        tvJob.setText(organizational_name);
                                    }
                                }
                                tvLokasi.setText(address);
                                tvName.setText(full_name);
                                tvNIK.setText(personal_number);

                                if (!personal_numbe_paramr.equals(session.getUserNIK())) {
                                    try {
                                        Picasso.get().load(profile).error(R.drawable.profile).into(ivProfile);
                                    } catch (Exception e){
                                        Picasso.get().load(R.drawable.profile).into(ivProfile);
                                    }
                                }
                                else{
                                    try {
                                        Picasso.get().load(session.getAvatar()).error(R.drawable.profile).into(ivProfile);
                                    } catch (Exception e){
                                        Picasso.get().load(R.drawable.profile).into(ivProfile);
                                    }
                                }

//                                Picasso.get().load(profile).error(R.drawable.profile).into(ivProfile);
                                System.out.println("sebelum hit api getMyPost() : " + personal_number + " dan " + full_name);
                                getMyPost(personal_number, full_name);
                                getCountFriend(personal_number);
                                getCountCommunity(personal_number);
                                progressDialogHelper.dismissProgressDialog(ProfileActivity.this);
                            }else{
//                                popUpLogin();
                                progressDialogHelper.dismissProgressDialog(ProfileActivity.this);
                                System.out.println("Masuk ke else");
                            }
                        }catch (Exception e){
                            progressDialogHelper.dismissProgressDialog(ProfileActivity.this);
                            System.out.println(e);
                            System.out.println("Masuk ke catch");
                        }
                    }
                    @Override
                    public void onError(ANError error) {
                        progressDialogHelper.dismissProgressDialog(ProfileActivity.this);
                        System.out.println(error + "Masuk ke error");
                    }
                });
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        System.out.println("TESTONCREATEACT");
//        MenuInflater inflater = getMenuInflater();
//        if (!personal_numbe_paramr.equals(session.getUserNIK())) {
//            if (panjang==0) {
//                inflater.inflate(R.menu.menu_profile, menu);
//            }
//        } else {
////            inflater.inflate(R.menu.logout, menu);
//            inflater.inflate(R.menu.menu_activity, menu);
//
//        }
//        return super.onCreateOptionsMenu(menu);
//    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Take appropriate action for each action item click
//        switch (item.getItemId()) {
//            case R.id.addfriend:
//                popUpAddFriend(personal_number, full_name);
//                return true;
//            case R.id.logout:
//                AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
//
//                builder.setTitle("Confirm");
//                builder.setMessage("Are you sure to logout ?");
//
//                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//
//                    public void onClick(DialogInterface dialog, int which) {
//                        // Do nothing but close the dialog
//                        session.setLoginState(false);
////                        session.logoutUser();
//                        Intent i = new Intent(ProfileActivity.this, LoginActivity.class);
//                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                        startActivity(i);
//
//                    }
//                });
//
//                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
//
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//
//                        // Do nothing
//                        dialog.dismiss();
//                    }
//                });
//
//                AlertDialog alert = builder.create();
//                alert.show();
//                return true;
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//    }

    private void popUpAddFriend(String personal_number, String full_name) {
        dialog = new Dialog(ProfileActivity.this);
        dialog.setContentView(R.layout.layout_add_friend);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        dialog.getWindow().setAttributes(lp);
        dialog.setTitle("Input Code Here");
        Button btnYes =(Button) dialog.findViewById(R.id.btnYes);
        Button btnNo =(Button) dialog.findViewById(R.id.btnNo);
        dialog.show();
        dialog.setCancelable(false);
        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (panjang<1 || statusFriend == "02") {
                System.out.println("test on click follow");
                    addFriend(personal_number, full_name);
//                    System.out.println("add friend");
//                } else {
//                    Toast.makeText(ProfileActivity.this, "Already Friend", Toast.LENGTH_SHORT).show();
//                }
                dialog.dismiss();
            }
        });
    }


    private void addFriend(String friend, String name) {
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
            jsonObject.put("friend", friend);
            jsonObject.put("change_user", session.getUserNIK());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        System.out.println(jsonObject+"PARAMADDFRIEND");
        String urlAddFriend = session.getServerURL()+"users/"+session.getUserNIK()+"/addFriend/"+session.getUserNIK();
        System.out.println("Url add friend: " + urlAddFriend);
        AndroidNetworking.post(urlAddFriend)
                .addHeaders("Accept","application/json")
                .addHeaders("Content-Type","application/json")
                .addHeaders("Authorization",session.getToken())
                .addJSONObjectBody(jsonObject)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // do anything with response
                        System.out.println(response+"ADDFRIENDRESPONSE");
                        try {
                            if(response.getInt("status")==200){
                                Toast.makeText(ProfileActivity.this,"Succes add "+name+" as a friend !",Toast.LENGTH_SHORT).show();
                                finish();
                            }else {
                                Toast.makeText(ProfileActivity.this,"error!",Toast.LENGTH_SHORT).show();
                            }
                        }catch (Exception e){
                            System.out.println(e);
                        }
                    }
                    @Override
                    public void onError(ANError error) {
                        System.out.println("Button yes error response : "+error.getResponse());
                        System.out.println("Button yes error body : "+error.getErrorBody());
                        System.out.println("Button yes error message : "+error.getMessage());
                    }
                });
    }

    private void checkMyFriend(String temen){
        String urlCheckMyFriend = session.getServerURL()+"users/verifyFriends/nik/"+temen+"/buscd/"+session.getUserBusinessCode();
        System.out.println("check url checkMyFriend() : " + urlCheckMyFriend);
        AndroidNetworking.get(urlCheckMyFriend)
                .addHeaders("Accept","application/json")
                .addHeaders("Content-Type","application/json")
                .addHeaders("Authorization",session.getToken())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println(response+"RESPONCEKFRIEND");
                        try {
                            if(response.getInt("status")==200){
                                JSONArray jsonArray = response.getJSONArray("data");
                                System.out.println(jsonArray.length()+"arraynyatemen");

                                panjang = jsonArray.length();
                                if (panjang>0){
                                    statusFriend = jsonArray.getJSONObject(0).getString("status");
                                    System.out.println("STATUS FRIEND: " + statusFriend);
                                }

                                if (!personal_numbe_paramr.equals(session.getUserNIK())) {
                                    System.out.println("Personal number param: " + personal_numbe_paramr);
                                    System.out.println("Session Nik: " + session.getUserNIK());
                                    System.out.println("PANJANGARRAY" + panjang);
                                    if (panjang==0) {
                                        System.out.println("TESTFOLLOWBUTTON");
                                        tvFollow.setText("Follow");
                                        tvFollow.setBackgroundColor(getResources().getColor(R.color.green_pegadaian_color));
                                        tvFollow.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                popUpAddFriend(personal_number, full_name);
                                            }
                                        });
                                    } else {
                                        if (statusFriend.equals("00")){
                                            System.out.println("On Request");
                                            tvFollow.setText("Requested");
                                            tvFollow.setBackgroundColor(getResources().getColor(R.color.grey));
                                        }
                                        else if (statusFriend.equals("01")){
                                            System.out.println("Approved");
                                            tvFollow.setText("Followed");
                                            tvFollow.setBackgroundColor(getResources().getColor(R.color.grey));
                                        } else {
                                            System.out.println("Rejected");
                                            tvFollow.setText("Follow");
                                            tvFollow.setBackgroundColor(getResources().getColor(R.color.green_pegadaian_color));
                                            tvFollow.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    popUpAddFriend(personal_number, full_name);
                                                }
                                            });
                                        }

                                    }
                                } else {
                                    tvFollow.setVisibility(View.GONE);
                                }

                            }else{
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

    private void getCountFriend(String nik){
        AndroidNetworking.get(session.getServerURL()+"users/friendapprove/nik/"+nik+"/buscd/"+session.getUserBusinessCode())
                .addHeaders("Accept","application/json")
                .addHeaders("Content-Type","application/json")
                .addHeaders("Authorization",session.getToken())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println(response+"JUMLAHTEMEN");
                        try {
                            if(response.getInt("status")==200){
                                JSONArray jsonArray = response.getJSONArray("data");
                                tvCountFriend.setText(String.valueOf(jsonArray.length()));
                                System.out.println(jsonArray.length()+"jhertbjhrbken");
                                for (int a = 0; a < jsonArray.length(); a++) {
                                    JSONObject object = jsonArray.getJSONObject(a);
                                    String begin_date = object.getString("begin_date");
                                    String end_date = object.getString("end_date");
                                    String business_code = object.getString("business_code");
                                    String personal_number = object.getString("personal_number");
                                    String status = object.getString("status");
                                    String change_date = object.getString("change_date");
                                    String change_user = object.getString("change_user");
                                    JSONArray arrayFriend = object.getJSONArray("friend");
//                                    tvCountFriend.setText(String.valueOf(arrayFriend.length()));
                                    for (int b=0; b<arrayFriend.length(); b++) {
                                        JSONObject objectFriend = arrayFriend.getJSONObject(b);
                                        String personal_number_teman = objectFriend.getString("personal_number");
                                        String full_name = objectFriend.getString("full_name");
                                    }
                                }

                            }else{
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

    private void getCountCommunity(String nik){
        AndroidNetworking.get(session.getServerURL()+"users/myCommunity/nik/"+nik+"/buscd/"+session.getUserBusinessCode())
                .addHeaders("Accept","application/json")
                .addHeaders("Content-Type","application/json")
                .addHeaders("Authorization",session.getToken())
                //.addJSONObjectBody(body)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println(response+"JUMLAHCOMMUNITY");
                        try {
                            if(response.getInt("status")==200){
                                JSONArray jsonArray = response.getJSONArray("data");
                                System.out.println(jsonArray.length()+"kdwnr4jansbdkh");
                                tvCountCommunity.setText(String.valueOf(jsonArray.length()));
                            }else{
                                popUpLogin();
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

    private void getMyPost(String nik, String name){
        progressDialogHelper.showProgressDialog(ProfileActivity.this, "Getting data...");
//        String urlGetMyPost = session.getServerURL()+"users/myDetailPosting/limit/1000/nik/"+nik+"/buscd/"+session.getUserBusinessCode();     \\ url yang lama
        String urlGetMyPost = session.getServerURL()+"users/mypost?business_code="+session.getUserBusinessCode()+"&personal_number="
                + personal_numbe_paramr + "&limit=1000";
        System.out.println("url get my post di profile activity : " + urlGetMyPost);
        AndroidNetworking.get(urlGetMyPost)
                .addHeaders("Accept","application/json")
                .addHeaders("Content-Type","application/json")
                .addHeaders("Authorization",session.getToken())
                //.addJSONObjectBody(body)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // do anything with response
                        System.out.println("response di list my postingan : "+response);
                        try {
                            if(response.getInt("status")==200){
//                                session.setToken(response.getString("token"));
                                listModel = new ArrayList<MyPostingModel>();
                                JSONArray jsonArray = response.getJSONArray("data");
                                if (jsonArray.length()==0) {
                                    listMyPosting.setVisibility(View.GONE);
                                    tvNullPost.setVisibility(View.VISIBLE);
                                } else {
                                    listMyPosting.setVisibility(View.VISIBLE);
                                    tvNullPost.setVisibility(View.GONE);
                                    for (int a = 0; a < jsonArray.length(); a++) {
                                        JSONObject object = jsonArray.getJSONObject(a);
                                        String begin_date = object.getString("begin_date");
                                        String end_date = object.getString("end_date");
                                        String business_code = object.getString("business_code");
                                        String change_date = object.getString("change_date");
                                        String change_user = object.getString("change_user");
                                        String date = object.getString("date");
                                        String description = object.getString("description");
                                        String posting_id = object.getString("posting_id");
                                        String title = object.getString("title");
                                        String personal_number = object.getString("personal_number");
                                        String time = object.getString("time");
                                        String updateAt = object.getString("updated_at");
                                        String image = object.getString("image");
                                        String name = object.getString("name");
                                        String profile = object.getString("profile");
                                        int lovelike = object.getInt("jml_lovelike");
                                        int isLiked = object.getInt("is_liked");
                                        int dislike = object.getInt("jml_dislike");
                                        int comment = object.getInt("jml_comment");

                                        model = new MyPostingModel(begin_date,end_date,business_code,change_date,change_user,date,description,posting_id,title,personal_number,time,updateAt,image,name,profile,lovelike,isLiked,dislike,comment);
                                        listModel.add(model);
                                    }
                                    adapter = new MyPostingAdapter(ProfileActivity.this, listModel);
                                    listMyPosting.setAdapter(adapter);

                                    listMyPosting.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                                        generateActivityId();
                                            Intent i = new Intent(ProfileActivity.this, DetailpostActivity.class);
                                            i.putExtra("posting_id",listModel.get(position).getPosting_id());
                                            i.putExtra("title",listModel.get(position).getTitle());
                                            i.putExtra("description",listModel.get(position).getDescription());
                                            i.putExtra("date",listModel.get(position).getChange_date());
                                            i.putExtra("image",listModel.get(position).getImage());
                                            i.putExtra("status_like",listModel.get(position).getIsLiked());

//                                            i.putExtra("status_like",listModel.get(position).isStatus_like());

                                            // checking if poster name is equal with session
                                            System.out.println("check sesion name and list model name at detail post : \n" +
                                                    "session name : " + session.getUserFullName() + "\n" +
                                                    "list model name : " + listModel.get(position).getName());

                                            if (listModel.get(position).getName().equals(session.getUserFullName())){
                                                i.putExtra("name",session.getUserFullName());    // looking at my post
                                                i.putExtra("avatar",session.getAvatar());        // looking at my post
                                            } else {
                                                i.putExtra("name",listModel.get(position).getName());    // looking at friend post
                                                i.putExtra("avatar",listModel.get(position).getProfile());        // looking at friend post
                                            }

                                            startActivity(i);
                                        }
                                    });
                                }
                                progressDialogHelper.dismissProgressDialog(ProfileActivity.this);
                            }else{
                                popUpLogin();
                                progressDialogHelper.dismissProgressDialog(ProfileActivity.this);

                            }
                        }catch (Exception e){
                            progressDialogHelper.dismissProgressDialog(ProfileActivity.this);

                            System.out.println("parse json list my postingan : " + e);
                        }

                    }
                    @Override
                    public void onError(ANError error) {
                        progressDialogHelper.dismissProgressDialog(ProfileActivity.this);
                        System.out.println("Error di load my postingan : " + error);
                    }
                });
    }

    private void generateActivityId() {
        SimpleDateFormat tgl = new SimpleDateFormat("yyyy-MM-dd");
        String tRes = tgl.format(new Date());
//        progressDialogHelper.showProgressDialog(CheckinActivity.this, "Getting data...");
        AndroidNetworking.get(session.getServerURL() + "users/generateIDActivity")
                .addHeaders("Accept", "application/json")
                .addHeaders("Content-Type", "application/json")
                .addHeaders("Authorization", session.getToken())
                //.addJSONObjectBody(body)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println("djhwebfheActivityId" + response);
                        // do anything with response
                        try {
                            if (response.getInt("status") == 200) {
                                String id = response.getJSONObject("data").getString("id");
                                session.setGeneratedCommentId(id);
                            } else {
//                                popUpLogin();
                            }
                            System.out.println("status ya : " + session.getStat());
//                            progressDialogHelper.dismissProgressDialog(CheckinActivity.this);
                        } catch (Exception e) {
//                            progressDialogHelper.dismissProgressDialog(CheckinActivity.this);
                            System.out.println(e);
                        }
                    }

                    @Override
                    public void onError(ANError error) {
//                        progressDialogHelper.dismissProgressDialog(CheckinActivity.this);
                        System.out.println(error);
                    }
                });
    }

    private void popUpLogin() {
        final Dialog dialog = new Dialog(ProfileActivity.this);
        dialog.setContentView(R.layout.layout_session_end);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        dialog.getWindow().setAttributes(lp);
        dialog.setTitle("Input Code Here");
        Button btnYes =(Button) dialog.findViewById(R.id.btnYes);
        dialog.show();
        dialog.setCancelable(false);
        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                session.setLoginState(false);
                Intent i = new Intent(ProfileActivity.this, LoginActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp(){
        Intent i = new Intent(ProfileActivity.this, HomeActivity.class);
        startActivity(i);
        return true;
    }
}
