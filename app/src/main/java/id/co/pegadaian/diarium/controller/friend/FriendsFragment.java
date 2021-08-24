package id.co.pegadaian.diarium.controller.friend;


import android.app.Dialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import id.co.pegadaian.diarium.R;
import id.co.pegadaian.diarium.adapter.FriendsAdapter;
import id.co.pegadaian.diarium.adapter.ListFriendAdapter;
import id.co.pegadaian.diarium.controller.login.LoginActivity;
import id.co.pegadaian.diarium.controller.profile.ProfileActivity;
import id.co.pegadaian.diarium.model.FriendsModel;
import id.co.pegadaian.diarium.util.UserSessionManager;
import id.co.pegadaian.diarium.util.element.ProgressDialogHelper;


/**
 * A simple {@link Fragment} subclass.
 */
public class FriendsFragment extends Fragment {

    UserSessionManager session;
    private List<FriendsModel> listModel;
    private FriendsModel model;
    private FriendsAdapter adapter;
    private ListView listFriendsRequest;
    private LinearLayout lnFriendRequest;

    private List<FriendsModel> listModelFriend;
    private FriendsModel modelFriend;
    private ListFriendAdapter adapterFriend;
    private ListView listFriends;
    SwipeRefreshLayout swipeRefreshLayout;


    private TextView tvFriendRequest,tvFriend;
    Typeface font,fontbold;
//    private ProgressDialogHelper progressDialogHelper;

    public FriendsFragment() {
        // Required empty public constructor
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_friends, container, false);
        font = Typeface.createFromAsset(getActivity().getAssets(),"fonts/Nexa Light.otf");
        fontbold = Typeface.createFromAsset(getActivity().getAssets(),"fonts/Nexa Bold.otf");
        session = new UserSessionManager(getActivity());
//        progressDialogHelper = new ProgressDialogHelper();
        listFriendsRequest = view.findViewById(R.id.list_friends_request);
        listFriends = view.findViewById(R.id.list_friends);
//        tvFriendRequest = view.findViewById(R.id.tvFriendRequest);
        tvFriend = view.findViewById(R.id.tvFriend);
//        lnFriendRequest = view.findViewById(R.id.lnFriendRequest);
//        tvNull = (TextView) view.findViewById(R.id.tvNull);
//        getFriendRequest();

        // swipe refresh layout
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_friendfrag);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getListFriend(session.getUserNIK());
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        getListFriend(session.getUserNIK());
        return view;
    }


//    private void getFriendRequest(){
////        COBA
////        lnFriendRequest.setVisibility(View.VISIBLE);
//        //        COBA
//
//        progressDialogHelper.showProgressDialog(getActivity(), "Getting data...");
//        AndroidNetworking.get(session.getServerURL()+"users/friendrequest/nik/"+session.getUserNIK()+"/buscd/"+session.getUserBusinessCode())
//                .addHeaders("Accept","application/json")
//                .addHeaders("Content-Type","application/json")
//                .addHeaders("Authorization",session.getToken())
//                .setPriority(Priority.MEDIUM)
//                .build()
//                .getAsJSONObject(new JSONObjectRequestListener() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        try {
//                            if(response.getInt("status")==200){
//                                listModel = new ArrayList<FriendsModel>();
//                                JSONArray jsonArray = response.getJSONArray("data");
//                                if (jsonArray.length()==0) {
//                                    listFriendsRequest.setVisibility(View.GONE);
//                                    tvFriendRequest.setVisibility(View.GONE);
//                                } else {
//                                    listFriendsRequest.setVisibility(View.VISIBLE);
//                                    tvFriendRequest.setVisibility(View.VISIBLE);
//                                    for (int a = 0; a < jsonArray.length(); a++) {
//                                        JSONObject object = jsonArray.getJSONObject(a);
//                                        String begin_date = object.getString("begin_date");
//                                        String end_date = object.getString("end_date");
//                                        String business_code = object.getString("business_code");
//                                        String personal_number = object.getString("personal_number");
//                                        String status = object.getString("status");
//                                        String change_date = object.getString("change_date");
//                                        String change_user = object.getString("change_user");
//                                        String profile = object.getString("profile");
//                                        JSONArray arrayFriend = object.getJSONArray("friend");
//                                        for (int b=0; b<arrayFriend.length(); b++) {
//                                            JSONObject objectFriend = arrayFriend.getJSONObject(b);
//                                            String personal_number_teman = objectFriend.getString("personal_number");
//                                            String full_name = objectFriend.getString("full_name");
//
//                                            model = new FriendsModel(begin_date, end_date, business_code, personal_number, status, change_date, change_user, full_name, personal_number_teman, profile);
//                                            listModel.add(model);
//                                        }
//                                    }
//                                    adapter = new FriendsAdapter(getActivity(), listModel);
//                                    listFriendsRequest.setAdapter(adapter);
//                                }
//                                progressDialogHelper.dismissProgressDialog(getActivity());
//                            }else{
//                                progressDialogHelper.dismissProgressDialog(getActivity());
//                            }
//                        }catch (Exception e){
//                            progressDialogHelper.dismissProgressDialog(getActivity());
//                            System.out.println(e);
//                        }
//                    }
//                    @Override
//                    public void onError(ANError error) {
//                        progressDialogHelper.dismissProgressDialog(getActivity());
//                        System.out.println(error);
//                    }
//                });
//
////        COBA
////        if (adapter != null){
////            lnFriendRequest.setVisibility(View.GONE);
////        }
////        COBA
//
//    }

    private void getListFriend(String personal_numbe_paramr){
//        progressDialogHelper.showProgressDialog(getActivity(), "Getting data...");
        System.out.println("GETLISTFRIEND"+session.getServerURL()+"users/friendapprove/nik/"+personal_numbe_paramr+"/buscd/"+session.getUserBusinessCode());
        AndroidNetworking.get(session.getServerURL()+"users/friendapprove/nik/"+personal_numbe_paramr+"/buscd/"+session.getUserBusinessCode())
                .addHeaders("Accept","application/json")
                .addHeaders("Content-Type","application/json")
                .addHeaders("Authorization",session.getToken())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println(response+"LISTFRIENDNYA");
                        try {
                            if(response.getInt("status")==200){
                                listModelFriend = new ArrayList<FriendsModel>();
                                JSONArray jsonArray = response.getJSONArray("data");
                                if (jsonArray.length()==0) {
                                    listFriends.setVisibility(View.GONE);
                                    tvFriend.setVisibility(View.GONE);
                                } else {
                                    listFriends.setVisibility(View.VISIBLE);
                                    tvFriend.setVisibility(View.VISIBLE);
                                    for (int a = 0; a < jsonArray.length(); a++) {
                                        JSONObject object = jsonArray.getJSONObject(a);
                                        String begin_date = object.getString("begin_date");
                                        String end_date = object.getString("end_date");
                                        String business_code = object.getString("business_code");
                                        String personal_number = object.getString("personal_number");
                                        String status = object.getString("status");
                                        String change_date = object.getString("change_date");
                                        String change_user = object.getString("change_user");
                                        String tipe = "1";

                                        JSONArray arrayFriend = object.getJSONArray("friend");
                                        for (int b=0; b<arrayFriend.length(); b++) {
                                            JSONObject objectFriend = arrayFriend.getJSONObject(b);
                                            String personal_number_teman = objectFriend.getString("personal_number");
                                            String full_name = objectFriend.getString("full_name");
//                                            String nickname = objectFriend.getString("nick_name");
                                            String profile = objectFriend.getString("profile");

                                            modelFriend = new FriendsModel(begin_date, end_date, business_code, personal_number, status, change_date, change_user, full_name, personal_number_teman, profile);
                                            listModelFriend.add(modelFriend);
                                        }
                                    }
                                    adapterFriend = new ListFriendAdapter(getActivity(), listModelFriend);
                                    listFriends.setAdapter(adapterFriend);
                                    listFriends.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                            System.out.println("Test OnCLick");
                                            Intent i = new Intent (getActivity(), ProfileActivity.class);
                                            i.putExtra("personal_number",listModelFriend.get(position).getPersonal_number_teman());
                                            i.putExtra("avatar",listModelFriend.get(position).getPersonal_number_teman());
                                            startActivity(i);
                                        }
                                    });
                                }
//                                progressDialogHelper.dismissProgressDialog(getActivity());
                            }else{
                                if (response.getInt("status") == 401){
                                    popUpLogin();
                                } else {
                                    Toast.makeText(getContext(),response.getString("message"),Toast.LENGTH_LONG).show();
                                }
//                                progressDialogHelper.dismissProgressDialog(getActivity());
                            }
                        }catch (Exception e){
//                            progressDialogHelper.dismissProgressDialog(getActivity());
                            System.out.println(e);
                        }
                    }
                    @Override
                    public void onError(ANError error) {
//                        progressDialogHelper.dismissProgressDialog(getActivity());
                        System.out.println(error);
                    }
                });
    }

    private void popUpLogin() {
        final Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.layout_session_end);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        dialog.getWindow().setAttributes(lp);
        dialog.setTitle("Input Code Here");
        Button btnYes = (Button) dialog.findViewById(R.id.btnYes);
        dialog.show();
        dialog.setCancelable(false);
        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                session.setLoginState(false);
                Intent i = new Intent(getActivity(), LoginActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
            }
        });
    }
}
