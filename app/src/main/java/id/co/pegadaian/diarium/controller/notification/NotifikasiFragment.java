package id.co.pegadaian.diarium.controller.notification;


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
import android.widget.Button;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import id.co.pegadaian.diarium.R;
import id.co.pegadaian.diarium.adapter.NotificationAdapter;
import id.co.pegadaian.diarium.controller.login.LoginActivity;
import id.co.pegadaian.diarium.model.NotifModel;
import id.co.pegadaian.diarium.util.UserSessionManager;
import id.co.pegadaian.diarium.util.element.ProgressDialogHelper;

//import id.co.telkomsigma.Diarium.adapter.NotifikasiAdapter;


/**
 * A simple {@link Fragment} subclass.
 */
public class NotifikasiFragment extends Fragment {


    String[] nama={
            "Dian Fitriana Dewi",
            "Nurannisa Nasution",
            "Muhammad Hasbi",
    };
    String[] notifikasi={
            "Started Following You",
            "Started Following You",
            "Started Following You",
    };
    String[] date= {
        "20 days ago",
            "15 days ago",
            "10 days ago"
    };

    UserSessionManager session;
    private List<NotifModel> listModel;
    private NotifModel model;
    private NotificationAdapter adapter;
    private ListView listFriendsRequest;
    private TextView tvNull;
    Typeface font,fontbold;
//    private ProgressDialogHelper progressDialogHelper;
    SwipeRefreshLayout swipeRefreshLayout;

    public NotifikasiFragment() {
        // Required empty public constructor
    }
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notifikasi, container, false);
        font = Typeface.createFromAsset(getActivity().getAssets(),"fonts/Nexa Light.otf");
        fontbold = Typeface.createFromAsset(getActivity().getAssets(),"fonts/Nexa Bold.otf");
        session = new UserSessionManager(getActivity());
//        progressDialogHelper = new ProgressDialogHelper();
        listFriendsRequest = view.findViewById(R.id.list_notif);
        tvNull = (TextView) view.findViewById(R.id.tvNull);

        // swipe refresh layout
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_notificationfrag);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getNotif();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        getNotif();
        return view;
    }

    private void getNotif(){
        SimpleDateFormat tgl = new SimpleDateFormat("yyyy-MM-dd");
        String tRes = tgl.format(new Date());
        String urlGetNotif = session.getServerURL()+"users/showAllNotif/nik/"+session.getUserNIK()+"/"+tRes+"/buscd/"+session.getUserBusinessCode();
        System.out.println("url get notif : " + urlGetNotif);
//        progressDialogHelper.showProgressDialog(getActivity(), "Getting data...");
        AndroidNetworking.get(urlGetNotif)
                .addHeaders("Accept","application/json")
                .addHeaders("Content-Type","application/json")
                .addHeaders("Authorization",session.getToken())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println("response get notif : " + response);
                        try {
                            if(response.getInt("status")==200){
                                listModel = new ArrayList<NotifModel>();
                                JSONArray jsonArray = response.getJSONArray("data");
                                if (jsonArray.length()==0) {
                                    listFriendsRequest.setVisibility(View.GONE);
                                    tvNull.setVisibility(View.VISIBLE);
                                } else {
                                    listFriendsRequest.setVisibility(View.VISIBLE);
                                    tvNull.setVisibility(View.GONE);
                                    for (int a = 0; a < jsonArray.length(); a++) {
                                        JSONObject object = jsonArray.getJSONObject(a);
                                        String title = object.getString("title");
                                        String desc = object.getString("description");
                                        String change_date = object.getString("change_date");
                                        System.out.println("title: " + title + "\n" + "desc: " + desc + "\n" + "change date:" + change_date);
                                        model = new NotifModel(title, desc, change_date);
                                        listModel.add(model);
                                    }
                                    adapter = new NotificationAdapter(getActivity(), listModel);
                                    listFriendsRequest.setAdapter(adapter);
                                }
//                                progressDialogHelper.dismissProgressDialog(getActivity());
                            }else{
                                if (response.getInt("status") == 401){
                                    popUpLogin();
                                } else {
                                    Toast.makeText(getContext(),response.getString("message"),Toast.LENGTH_LONG).show();
                                }
                                System.out.println("else di get notif : " + response.getString("message"));
//                                progressDialogHelper.dismissProgressDialog(getActivity());
                            }
                        }catch (Exception e){
//                            progressDialogHelper.dismissProgressDialog(getActivity());
                            System.out.println("catch di get notif : " + e);
                        }
                    }
                    @Override
                    public void onError(ANError error) {
//                        progressDialogHelper.dismissProgressDialog(getActivity());
                        System.out.println("onerror di get notif : " + error.getErrorBody());
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
