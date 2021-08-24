package id.co.pegadaian.diarium.controller.home.main_menu.mydevelopment.mytraining;


import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import id.co.pegadaian.diarium.R;
import id.co.pegadaian.diarium.adapter.EventSessionAdapter;
import id.co.pegadaian.diarium.model.EventSessionModel;
import id.co.pegadaian.diarium.util.UserSessionManager;
import id.co.pegadaian.diarium.util.element.ProgressDialogHelper;

/**
 * A simple {@link Fragment} subclass.
 */
public class ActivityFragment extends Fragment {
    private ProgressDialogHelper progressDialogHelper;
    UserSessionManager session;
    private List<EventSessionModel> listModel;
    private EventSessionModel model;
    private EventSessionAdapter adapter;
    ListView listEventSession;
    TextView tvNull;

    public ActivityFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_activity, container, false);
        listEventSession = view.findViewById(R.id.listEventSession);
        session = new UserSessionManager(getActivity());
        progressDialogHelper = new ProgressDialogHelper();
        tvNull = view.findViewById(R.id.tvNull);
        getEventSession(session.getBatchLMS());
        return view;
    }



    private void getEventSession(String batch){
        System.out.println("GETACTIVITY");
        progressDialogHelper.showProgressDialog(getActivity(), "Getting data...");
        AndroidNetworking.get("https://testapi.digitalevent.id/lms/api/session?order[BEGDA]=asc&batch[]="+batch)
                .addHeaders("Accept","application/json")
                .addHeaders("Content-Type","application/json")
                .addHeaders("Authorization","Bearer "+session.getTokenLdap())
                //.addJSONObjectBody(body)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // do anything with response
                        System.out.println("RESPONSEEVENTSESSION"+response);
                        try {
                            listModel = new ArrayList<EventSessionModel>();
                            JSONArray jsonArray = response.getJSONArray("data");
                            System.out.println(jsonArray.length()+"PANJANGDATA");
                            if (jsonArray.length()==0) {
                                tvNull.setVisibility(View.VISIBLE);
                                listEventSession.setVisibility(View.GONE);
                            } else {
                                tvNull.setVisibility(View.GONE);
                                listEventSession.setVisibility(View.VISIBLE);
                                for (int a = 0; a < jsonArray.length(); a++) {
                                    JSONObject object = jsonArray.getJSONObject(a);
                                    String session_name = object.getString("session_name");
                                    String activity_name = object.getJSONObject("learning_activity").getString("activity_name");
                                    String begin_date = object.getJSONObject("learning_activity").getString("begin_date");
                                    String end_date = object.getJSONObject("learning_activity").getString("end_date");
                                    String activity_id = object.getJSONObject("learning_activity").getString("activity_id");
                                    model = new EventSessionModel(activity_id, session_name,activity_name,begin_date,end_date);
                                    listModel.add(model);
                                }
                                adapter = new EventSessionAdapter(getActivity(), listModel);
                                listEventSession.setAdapter(adapter);
                                listEventSession.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                                        Intent i = new Intent(getActivity(), ScheduleActivity.class);
                                        i.putExtra("bussines_code",session.getUserBusinessCode());
                                        i.putExtra("batch",session.getBatchLMS());
                                        i.putExtra("batch_name",session.getBatchNameLMS());
                                        i.putExtra("event_id", session.getEventId());
                                        i.putExtra("event_name",session.getEventNameLMS());
                                        i.putExtra("begin_date",session.getBeginDateLMS());
                                        i.putExtra("end_date",session.getEndDateLMS());
                                        i.putExtra("event_curr_stat",session.getevent_curr_statLMS());
                                        i.putExtra("event_status",session.getevent_statusLMS());
                                        i.putExtra("event_stat_id",session.getevent_stat_idLMS());
                                        i.putExtra("location_id",session.getlocation_idLMS());
                                        i.putExtra("location",session.getlocationLMS());
                                        i.putExtra("cur_id",session.getcur_idLMS());
                                        i.putExtra("curriculum",session.getcurriculumLMS());
                                        i.putExtra("event_type",session.getevent_typeLMS());
                                        i.putExtra("participant_id",session.getparticipant_idLMS());
                                        i.putExtra("partcipant_name",session.getpartcipant_nameLMS());
                                        i.putExtra("parti_nicknm",session.getparti_nicknmLMS());
                                        i.putExtra("company_name",session.getcompany_nameLMS());

                                        i.putExtra("activity_name",listModel.get(position).getActivity_name());
                                        i.putExtra("session_name",listModel.get(position).getSession_name());
                                        i.putExtra("begin_date_activity",listModel.get(position).getBegin_date());
                                        i.putExtra("end_date_activity",listModel.get(position).getEnd_date());
                                        startActivity(i);
                                    }
                                });
                            }
                            progressDialogHelper.dismissProgressDialog(getActivity());
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
