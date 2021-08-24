package id.co.pegadaian.diarium.controller.inbox;


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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.Toolbar;
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
import id.co.pegadaian.diarium.adapter.InboxAdapter;
import id.co.pegadaian.diarium.controller.login.LoginActivity;
import id.co.pegadaian.diarium.model.FriendsModel;
import id.co.pegadaian.diarium.model.InboxModel;
import id.co.pegadaian.diarium.util.UserSessionManager;
import id.co.pegadaian.diarium.util.element.ProgressDialogHelper;


/**
 * A simple {@link Fragment} subclass.
 */

public class InboxFragment extends Fragment {

    private List<FriendsModel> listModel;
    private FriendsModel model;
    private FriendsAdapter adapter;

    Typeface font, fontbold;
    UserSessionManager session;
//    private ProgressDialogHelper progressDialogHelper;

    private List<InboxModel> listModelInbox;
    private InboxModel modelInbox;
    private InboxAdapter adapterInbox;

    ListView listFriendRequest, lvInbox;
    private TextView tvTitleFrag, tvNull, tvNullInbox;
    Toolbar mToolbar;

    SwipeRefreshLayout swipeRefreshLayout;


    public InboxFragment() {
        // Required empty public constructor
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_inbox, container, false);
        session = new UserSessionManager(getActivity());
//        progressDialogHelper = new ProgressDialogHelper();

        font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Nexa Light.otf");
        fontbold = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Nexa Bold.otf");
        tvNull = (TextView) view.findViewById(R.id.tvNull);
        tvNullInbox = view.findViewById(R.id.tvNullInbox);
        listFriendRequest = view.findViewById(R.id.list_friends_request);
        lvInbox = view.findViewById(R.id.list_inbox);

        tvTitleFrag = view.findViewById(R.id.tv_title_inboxfrag);
//        tvTitleFrag.setText("Inbox");
        mToolbar = view.findViewById(R.id.toolbar_inboxfrag);
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_inboxfrag);


//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            listFriendRequest.setNestedScrollingEnabled(false);
//            lvInbox.setNestedScrollingEnabled(false);
//        }

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getFriendRequest();
                getInbox();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        getFriendRequest();
        getInbox();

        return view;
    }


    private void getFriendRequest() {
//        COBA
//        lnFriendRequest.setVisibility(View.VISIBLE);
//        COBA
        System.out.println("masuk ke getFriendRequest()");

//        progressDialogHelper.showProgressDialog(getActivity(), "Getting data...");
        System.out.println("print url get friend request di inbox fragment : " + session.getServerURL() + "users/friendrequest/nik/" + session.getUserNIK() + "/buscd/" + session.getUserBusinessCode());
        AndroidNetworking.get(session.getServerURL() + "users/friendrequest/nik/" + session.getUserNIK() + "/buscd/" + session.getUserBusinessCode())
                .addHeaders("Accept", "application/json")
                .addHeaders("Content-Type", "application/json")
                .addHeaders("Authorization", session.getToken())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getInt("status") == 200) {
                                System.out.println("response getFriendRequest: " + response);
                                listModel = new ArrayList<FriendsModel>();
                                JSONArray jsonArray = response.getJSONArray("data");

                                if (jsonArray.length() == 0) {
                                    listFriendRequest.setVisibility(View.GONE);
                                } else {
                                    System.out.println("masuk ke else dalem get friend request");
//                                    tvNull.setVisibility(View.GONE);
                                    listFriendRequest.setVisibility(View.VISIBLE);
                                    for (int a = 0; a < jsonArray.length(); a++) {
                                        JSONObject object = jsonArray.getJSONObject(a);
                                        String begin_date = object.getString("begin_date");
                                        System.out.println("begindate: " + begin_date);
                                        String end_date = object.getString("end_date");
                                        System.out.println("enddate: " + end_date);
                                        String business_code = object.getString("business_code");
                                        System.out.println("bucd: " + business_code);
                                        String personal_number = object.getString("personal_number");
                                        System.out.println("penr: " + personal_number);
                                        String status = object.getString("status");
                                        System.out.println("status: " + status);
                                        String change_date = object.getString("change_date");
                                        System.out.println("changedate: " + change_date);
                                        String change_user = object.getString("change_user");
                                        System.out.println("changeuser: " + change_user);
                                        String profile = object.getString("profile");
                                        System.out.println("profile: " + profile);
                                        JSONArray arrayFriend = object.getJSONArray("friend");
                                        System.out.println("Panjang arrayFriend: " + arrayFriend.length());
                                        for (int b = 0; b < arrayFriend.length(); b++) {
                                            JSONObject objectFriend = arrayFriend.getJSONObject(b);
                                            String personal_number_teman = objectFriend.getString("personal_number");
                                            String full_name = objectFriend.getString("full_name");

                                            model = new FriendsModel(begin_date, end_date, business_code, personal_number, status, change_date,
                                                    change_user, full_name, personal_number_teman, profile);
                                            listModel.add(model);
                                        }
                                    }
                                    adapter = new FriendsAdapter(getActivity(), listModel);
                                    listFriendRequest.setAdapter(adapter);
                                }
//                                progressDialogHelper.dismissProgressDialog(getActivity());
                            } else {
//                                progressDialogHelper.dismissProgressDialog(getActivity());
                                System.out.println("masuk ke else getFriendRequest(): " + response);
                            }
                        } catch (Exception e) {
                            System.out.println("masuk ke catch getFriendRequest(): " + response + "  exception: " + e);
                            System.out.println(e);
                        }
//                        progressDialogHelper.dismissProgressDialog(getActivity());
                    }

                    @Override
                    public void onError(ANError error) {
//                        progressDialogHelper.dismissProgressDialog(getActivity());
                        System.out.println("masuk ke onError getFriendRequest() :" + error.getErrorBody());
                        System.out.println(error);
                    }
                });

//        COBA
//        if (adapter != null){
//            lnFriendRequest.setVisibility(View.GONE);
//        }
//        COBA

    }

    private void getInbox() {
//        progressDialogHelper.showProgressDialog(getActivity(), "Getting data...");
        System.out.println("url getInbox(): " + session.getServerURL() + "users/inbox?personal_number=" + session.getUserNIK() + "&business_code=" + session.getUserBusinessCode());
        AndroidNetworking.get(session.getServerURL() + "users/inbox?personal_number=" + session.getUserNIK() + "&business_code=" + session.getUserBusinessCode())
//        AndroidNetworking.get(session.getServerURL()+"users/inbox/buscd/"+session.getUserBusinessCode())
                .addHeaders("Accept", "application/json")
                .addHeaders("Content-Type", "application/json")
                .addHeaders("Authorization", session.getToken())
                //.addJSONObjectBody(body)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // do anything with response
                        System.out.println("tes response getInbox(): " + response);
                        try {
                            if (response.getInt("status") == 200) {
//                                session.setToken(response.getString("token"));
                                listModelInbox = new ArrayList<InboxModel>();
                                JSONArray jsonArray = response.getJSONArray("data");
                                System.out.println("response getInbox(): " + response + "\n" + "panjang arraynya: " + jsonArray.length());

                                if (jsonArray.length() == 0) {
                                    lvInbox.setVisibility(View.GONE);
                                    tvNullInbox.setVisibility(View.VISIBLE);
                                } else {
                                    lvInbox.setVisibility(View.VISIBLE);
                                    tvNullInbox.setVisibility(View.GONE);
                                    for (int a = 0; a < jsonArray.length(); a++) {
                                        JSONObject object = jsonArray.getJSONObject(a);
                                        String begin_date = object.getString("begin_date");
                                        String end_date = object.getString("end_date");
                                        String business_code = object.getString("business_code");
                                        String personal_number = object.getString("personal_number");
                                        String inbox_id = object.getString("inbox_id");
                                        String title = object.getString("inbox_title");
                                        String description = object.getString("inbox_description");
                                        String change_date = object.getString("change_date");
                                        String change_user = object.getString("change_user");
                                        String tipe = "2";
                                        System.out.println("begin date: " + begin_date +  "\n" + "end date: " + end_date + "\n" + "busc: " + business_code + "\n"
                                        + "pernr: " + personal_number + "\n" + "inbox_id: " + inbox_id + "\n" + "title: " + title + "\n" + "description: "
                                        + description + "\n" + "change date: " + change_date + "\n" + "change user: " + change_user);
                                        modelInbox = new InboxModel(begin_date, end_date, business_code, personal_number, inbox_id,
                                                title, description, change_date, change_user);
                                        listModelInbox.add(modelInbox);
                                    }
                                    adapterInbox = new InboxAdapter(getActivity(), listModelInbox);
                                    lvInbox.setAdapter(adapterInbox);
                                    lvInbox.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                            String item = (String) lvInbox.getItemAtPosition(position);
                                            Toast.makeText(getActivity(), "You selected : " + item, Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    lvInbox.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                            Intent i = new Intent(getActivity(), DetailInboxActivity.class);
                                            i.putExtra("title", listModelInbox.get(position).getTitle());
                                            i.putExtra("date", listModelInbox.get(position).getChange_date());
                                            i.putExtra("desc", listModelInbox.get(position).getDescription());
                                            startActivity(i);
                                        }
                                    });
                                }
//                                progressDialogHelper.dismissProgressDialog(getActivity());
                            } else {

                                if (response.getInt("status") == 401){
                                            popUpLogin();
                                } else {
                                    Toast.makeText(getContext(),response.getString("message"),Toast.LENGTH_LONG).show();
                                }
//                                progressDialogHelper.dismissProgressDialog(getActivity());
                                System.out.println("error ke else getInbox(): " + response);

                            }
                        } catch (Exception e) {
//                            progressDialogHelper.dismissProgressDialog(getActivity());
                            System.out.println("error ke catch getInbox(): " + response + "  exception: " + e);
                            System.out.println(e);
                        }

                    }

                    @Override
                    public void onError(ANError error) {
//                        progressDialogHelper.dismissProgressDialog(getActivity());
                        System.out.println("error ke onError getInbox(): " + error.getErrorBody());
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

    @Override
    public void onResume() {
        super.onResume();
//        getFriendRequest();
//        System.out.println("tes onclick di adapter friend req");
    }
}
