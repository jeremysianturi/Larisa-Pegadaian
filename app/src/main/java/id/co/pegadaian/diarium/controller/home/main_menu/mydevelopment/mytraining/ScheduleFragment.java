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
import id.co.pegadaian.diarium.adapter.ScheduleAdapter;
import id.co.pegadaian.diarium.model.ScheduleModel;
import id.co.pegadaian.diarium.util.TimeHelper;
import id.co.pegadaian.diarium.util.UserSessionManager;
import id.co.pegadaian.diarium.util.element.ProgressDialogHelper;

/**
 * A simple {@link Fragment} subclass.
 */
public class ScheduleFragment extends Fragment {
    private List<ScheduleModel> listModel;
    private ScheduleModel model;
    private ScheduleAdapter adapter;
    TextView tvNull;
    ListView listEventSession;
    private ProgressDialogHelper progressDialogHelper;
    UserSessionManager session;
    TimeHelper timeHelper;
    public ScheduleFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_schedule, container, false);
        timeHelper = new TimeHelper();
        session = new UserSessionManager(getActivity());
        progressDialogHelper = new ProgressDialogHelper();
        tvNull = view.findViewById(R.id.tvNull);
        listEventSession = view.findViewById(R.id.listSchedule);
        tvNull.setVisibility(View.GONE);
        getSchedule();
        return view;
    }

    private void getSchedule(){
        System.out.println("MASUKSCHEDULE");
        progressDialogHelper.showProgressDialog(getActivity(), "Getting data...");
        AndroidNetworking.get("https://testapi.digitalevent.id/lms/api/schedule?order[BEGDA]=asc&session[]=3")
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
                        System.out.println("RESPONSCHEDULE"+response);
                        try {
                            listModel = new ArrayList<ScheduleModel>();
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
                                    String schedule_id = object.getString("schedule_id");
                                    String begin_date = object.getString("begin_date");
                                    String end_date = object.getString("end_date");
                                    String begin_time = object.getString("begin_time");
                                    String end_time = object.getString("end_time");
                                    String schedule_name = object.getJSONObject("reference").getString("schedule_name");
                                    String topic = object.getJSONObject("reference").getString("topic");
                                    String day_number = object.getString("day_number");
                                    model = new ScheduleModel(schedule_id,begin_date,end_date,begin_time,end_time,schedule_name,topic, day_number);
                                    listModel.add(model);
                                }
                                adapter = new ScheduleAdapter(getActivity(), listModel);
                                listEventSession.setAdapter(adapter);
                                listEventSession.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                                        Intent i = new Intent(getActivity(), MateriActivity.class);
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

                                        i.putExtra("activity_name",session.getactivity_nameLMS());
                                        i.putExtra("session_name",session.getsession_nameLMS());
                                        i.putExtra("begin_date_activity",session.getbegin_date_activityLMS());
                                        i.putExtra("end_date_activity",session.getend_date_activityLMS());

                                        i.putExtra("schedule_id",listModel.get(position).getSchedule_id());
                                        i.putExtra("schedule_name",listModel.get(position).getSchedule_name());
                                        i.putExtra("topic",listModel.get(position).getTopic());
                                        i.putExtra("begin_date_schedule",listModel.get(position).getBegin_date());
                                        i.putExtra("end_date_schedule",listModel.get(position).getEnd_date());
                                        i.putExtra("begin_time",listModel.get(position).getBegin_time());
                                        i.putExtra("end_time",listModel.get(position).getEnd_time());
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
