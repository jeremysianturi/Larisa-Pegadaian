package id.co.pegadaian.diarium.controller.home.main_menu.myevent;


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

import id.co.pegadaian.diarium.R;
import id.co.pegadaian.diarium.adapter.EventAdapter;
import id.co.pegadaian.diarium.controller.login.LoginActivity;
import id.co.pegadaian.diarium.model.EventModel;
import id.co.pegadaian.diarium.util.UserSessionManager;
import id.co.pegadaian.diarium.util.element.ProgressDialogHelper;


/**
 * A simple {@link Fragment} subclass.
 */
public class TematikFragment extends Fragment {


    String[] judul_event={

            "Forti BUMN",
    };
    String[] tanggal={

            "20 Januari 2019",
    };
    String[] tempat={

            "Jakarta",
    };

    private ProgressDialogHelper progressDialogHelper;
    UserSessionManager session;
    private List<EventModel> listModel;
    private EventModel model;
    private EventAdapter adapter;
    ListView list;
    Typeface font,fontbold;
    TextView tvNull;
    public TematikFragment() {
        // Required empty public constructor
    }
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tematik, container, false);
        font = Typeface.createFromAsset(getActivity().getAssets(),"fonts/Nexa Light.otf");
        fontbold = Typeface.createFromAsset(getActivity().getAssets(),"fonts/Nexa Bold.otf");
        progressDialogHelper = new ProgressDialogHelper();
        session = new UserSessionManager(getActivity());

        list = view.findViewById(R.id.listEvent);
        tvNull = view.findViewById(R.id.tvNull);

        list.setVisibility(View.GONE);
        tvNull.setVisibility(View.GONE);

        getEvent("T");
        return view;
    }

    private void getEvent(final String type){
        progressDialogHelper.showProgressDialog(getActivity(), "Getting data...");
        AndroidNetworking.get(session.getServerURL()+"users/allevent/type/"+type+"/buscd/"+session.getUserBusinessCode())
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
                        System.out.println(response+"kjwrhbk4jrEvent");
                        try {
                            if(response.getInt("status")==200){
//                                session.setToken(response.getString("token"));
                                listModel = new ArrayList<EventModel>();
                                JSONArray jsonArray = response.getJSONArray("data");
                                if (jsonArray.length()<=0) {
                                    list.setVisibility(View.GONE);
                                    tvNull.setVisibility(View.VISIBLE);
                                } else {
                                    list.setVisibility(View.VISIBLE);
                                    tvNull.setVisibility(View.GONE);
                                    for (int a = 0; a < jsonArray.length(); a++) {
                                        JSONObject object = jsonArray.getJSONObject(a);
                                        String begin_date = object.getString("begin_date");
                                        String end_date = object.getString("end_date");
                                        String business_code = object.getString("business_code");
                                        String personal_number = object.getString("personal_number");
                                        String id = object.getString("id");
                                        String code = object.getString("code");
                                        String event_type = object.getString("event_type");
                                        String theme = object.getString("theme");
                                        String name = object.getString("name");
                                        String description = object.getString("description");
                                        String location = object.getString("location");
                                        String city = object.getString("city");
                                        String phone = object.getString("phone");
                                        String link = object.getString("link");
                                        String event_start = object.getString("event_start");
                                        String event_end = object.getString("event_end");
                                        String change_date = object.getString("change_date");
                                        String change_user = object.getString("change_user");
                                        String image = object.getString("image");
                                        System.out.println(begin_date+"dl2j3nr3");
                                        model = new EventModel(begin_date, end_date, business_code, personal_number, id, code, event_type, theme, name, description, location, city, phone, link, event_start, event_end, change_date, change_user, image);
                                        listModel.add(model);
                                    }
                                    adapter = new EventAdapter(getActivity(), listModel, type);
                                    list.setAdapter(adapter);
                                }
                                progressDialogHelper.dismissProgressDialog(getActivity());
                            }else{
                                popUpLogin();
                                progressDialogHelper.dismissProgressDialog(getActivity());
                            }
                        }catch (Exception e){
                            progressDialogHelper.dismissProgressDialog(getActivity());
                            System.out.println(e);
                        }

                    }
                    @Override
                    public void onError(ANError error) {
                        progressDialogHelper.dismissProgressDialog(getActivity());
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
        Button btnYes =(Button) dialog.findViewById(R.id.btnYes);
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
