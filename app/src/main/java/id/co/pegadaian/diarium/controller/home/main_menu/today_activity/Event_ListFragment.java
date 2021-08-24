package id.co.pegadaian.diarium.controller.home.main_menu.today_activity;


import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

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
import id.co.pegadaian.diarium.adapter.TodayEventListAdapter;
import id.co.pegadaian.diarium.model.TodayEventListModel;
import id.co.pegadaian.diarium.util.UserSessionManager;
import id.co.pegadaian.diarium.util.element.ProgressDialogHelper;


/**
 * A simple {@link Fragment} subclass.
 */
public class Event_ListFragment extends Fragment {
    private ProgressDialogHelper progressDialogHelper;
    UserSessionManager session;
    private List<TodayEventListModel> listModel;
    private TodayEventListModel model;
    private TodayEventListAdapter adapter;
    TextView tvNull;
    ListView listActivity;
//    TextView tvNull;


    //Variable Listview
    ListView listmenu;


    Typeface font,fontbold;

    public Event_ListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_eventlist, container,false);
        font = Typeface.createFromAsset(getActivity().getAssets(),"fonts/Nexa Light.otf");
        fontbold = Typeface.createFromAsset(getActivity().getAssets(),"fonts/Nexa Bold.otf");
        progressDialogHelper = new ProgressDialogHelper();
        session = new UserSessionManager(getActivity());
        font = Typeface.createFromAsset(getActivity().getAssets(),"fonts/Nexa Light.otf");
        fontbold = Typeface.createFromAsset(getActivity().getAssets(),"fonts/Nexa Bold.otf");
        listActivity = view.findViewById(R.id.listActivity);
        tvNull = view.findViewById(R.id.tvNull);
        getTodayEventList(session.getTodayActivity());
//        Toast.makeText(getActivity(), "Param aktifitas : "+session.getTodayActivity(), Toast.LENGTH_SHORT).show();

        return view;
    }

    private void getTodayEventList(final String date){
        progressDialogHelper.showProgressDialog(getActivity(), "Getting data...");
        AndroidNetworking.get(session.getServerURL()+"users/todayActivity/"+date)
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
                        System.out.println(response+"kjwrhbk4jrEvent"+date);
                        try {
                            if(response.getInt("status")==200){
//                                session.setToken(response.getString("token"));
                                JSONArray jsonArray = response.getJSONArray("data");

                                    listActivity.setVisibility(View.VISIBLE);
                                    tvNull.setVisibility(View.GONE);
                                    System.out.println(jsonArray.length()+"wdefrefw");
                                    for (int a = 0; a < jsonArray.length(); a++) {
                                        JSONObject object = jsonArray.getJSONObject(a);
                                        JSONArray jsonArrayTodayActivity = object.getJSONArray("event list");
                                        if (jsonArrayTodayActivity.length()==0) {
                                            listActivity.setVisibility(View.GONE);
                                            tvNull.setVisibility(View.VISIBLE);
                                        } else {
                                        System.out.println(jsonArrayTodayActivity.length()+"dlj3nr432Event");
                                        listModel = new ArrayList<TodayEventListModel>();
                                        for (int b = 0; b < jsonArrayTodayActivity.length(); b++) {
                                            JSONObject objectTodayActivity = jsonArrayTodayActivity.getJSONObject(b);
                                            String id = objectTodayActivity.getString("id");
                                            String name = objectTodayActivity.getString("name");
                                            String description = objectTodayActivity.getString("description");
                                            String event_start = objectTodayActivity.getString("event_start");
                                            String event_end = objectTodayActivity.getString("event_end");
                                            model = new TodayEventListModel(id, name, description, event_start, event_end);
                                            listModel.add(model);
                                        }
                                            adapter = new TodayEventListAdapter(getActivity(), listModel);
                                            listActivity.setAdapter(adapter);
                                            progressDialogHelper.dismissProgressDialog(getActivity());
                                        }
                                }
                                progressDialogHelper.dismissProgressDialog(getActivity());
                            }else{
//                                popUpLogin();
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

}

