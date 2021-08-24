package id.co.pegadaian.diarium.controller.profile;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import id.co.pegadaian.diarium.R;
import id.co.pegadaian.diarium.adapter.FriendsAdapter;
import id.co.pegadaian.diarium.model.FriendsModel;
import id.co.pegadaian.diarium.util.UserSessionManager;
import id.co.pegadaian.diarium.util.element.ProgressDialogHelper;

public class FriendDataActivity extends AppCompatActivity {
    UserSessionManager session;
    private List<FriendsModel> listModel;
    private FriendsModel model;
    private FriendsAdapter adapter;
    private ListView listFriendsRequest;
    private TextView tvNull;
    Typeface font,fontbold;
    private ProgressDialogHelper progressDialogHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_data);

        font = Typeface.createFromAsset(this.getAssets(),"fonts/Nexa Light.otf");
        fontbold = Typeface.createFromAsset(this.getAssets(),"fonts/Nexa Bold.otf");
        session = new UserSessionManager(this);
        progressDialogHelper = new ProgressDialogHelper();
        listFriendsRequest = findViewById(R.id.list_friends);
        tvNull = (TextView) findViewById(R.id.tvNull);
        getFriendRequest();
    }

    private void getFriendRequest(){
        progressDialogHelper.showProgressDialog(this, "Getting data...");
        AndroidNetworking.get(session.getServerURL()+"users/"+session.getUserNIK()+"/friendrequest")
                .addHeaders("Accept","application/json")
                .addHeaders("Content-Type","application/json")
                .addHeaders("Authorization",session.getToken())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if(response.getInt("status")==200){
                                listModel = new ArrayList<FriendsModel>();
                                JSONArray jsonArray = response.getJSONArray("data");
                                if (jsonArray.length()==0) {
                                    listFriendsRequest.setVisibility(View.GONE);
                                    tvNull.setVisibility(View.VISIBLE);
                                } else {
                                    listFriendsRequest.setVisibility(View.VISIBLE);
                                    tvNull.setVisibility(View.GONE);
                                    for (int a = 0; a < jsonArray.length(); a++) {
                                        JSONObject object = jsonArray.getJSONObject(a);
                                        String begin_date = object.getString("begin_date");
                                        String end_date = object.getString("end_date");
                                        String business_code = object.getString("business_code");
                                        String personal_number = object.getString("personal_number");
                                        String status = object.getString("status");
                                        String change_date = object.getString("change_date");
                                        String change_user = object.getString("change_user");
                                        String profile = object.getString("profile");
                                        String tipe = "1";
                                        JSONArray arrayFriend = object.getJSONArray("friend");
                                        for (int b=0; b<arrayFriend.length(); b++) {
                                            JSONObject objectFriend = arrayFriend.getJSONObject(b);
                                            String full_name = objectFriend.getString("full_name");
                                            String personal_number_teman = objectFriend.getString("personal_number");

                                            model = new FriendsModel(begin_date, end_date, business_code, personal_number, status, change_date, change_user, full_name,personal_number_teman, profile);
                                            listModel.add(model);
                                        }
                                    }
                                    adapter = new FriendsAdapter(FriendDataActivity.this, listModel);
                                    listFriendsRequest.setAdapter(adapter);
                                }
                                progressDialogHelper.dismissProgressDialog(FriendDataActivity.this);
                            }else{
                                progressDialogHelper.dismissProgressDialog(FriendDataActivity.this);
                            }
                        }catch (Exception e){
                            progressDialogHelper.dismissProgressDialog(FriendDataActivity.this);
                            System.out.println(e);
                        }
                    }
                    @Override
                    public void onError(ANError error) {
                        progressDialogHelper.dismissProgressDialog(FriendDataActivity.this);
                        System.out.println(error);
                    }
                });
    }
}
