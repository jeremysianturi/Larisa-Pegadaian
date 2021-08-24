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
import com.androidnetworking.interfaces.JSONArrayRequestListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import id.co.pegadaian.diarium.R;
import id.co.pegadaian.diarium.adapter.MentoringAdapter;
import id.co.pegadaian.diarium.model.MentoringModel;
import id.co.pegadaian.diarium.util.UserSessionManager;
import id.co.pegadaian.diarium.util.element.ProgressDialogHelper;

/**
 * A simple {@link Fragment} subclass.
 */
public class MentoringFragment extends Fragment {
    private ProgressDialogHelper progressDialogHelper;
    UserSessionManager session;
    private List<MentoringModel> listModel;
    private MentoringModel model;
    private MentoringAdapter adapter;
    ListView listEventSession;
    TextView tvNull;

    public MentoringFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_mentoring, container, false);
        listEventSession = view.findViewById(R.id.listMentoring);
        session = new UserSessionManager(getActivity());
        progressDialogHelper = new ProgressDialogHelper();
        tvNull = view.findViewById(R.id.tvNull);
        getEventSession(session.getBatchLMS());
        return view;
    }


    private void getEventSession(String batch){
        System.out.println("GETMENTORING");
        progressDialogHelper.showProgressDialog(getActivity(), "Getting data...");
        AndroidNetworking.get("https://testapi.digitalevent.id/lms/api/mentoringparticipant/session?begin_date=2019-10-11&end_date=2019-10-11&order[BEGDA]=asc&otype=PARTI&id=2&session=3")
                .addHeaders("Accept","application/json")
                .addHeaders("Content-Type","application/json")
                .addHeaders("Authorization","Bearer "+session.getTokenLdap())
                //.addJSONObjectBody(body)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        // do anything with response
                        System.out.println("RESPONMENTORING"+response);
                        try {
                            listModel = new ArrayList<MentoringModel>();
                            if (response.length()==0) {
                                tvNull.setVisibility(View.VISIBLE);
                                listEventSession.setVisibility(View.GONE);
                            } else {
                                tvNull.setVisibility(View.GONE);
                                listEventSession.setVisibility(View.VISIBLE);
                                for (int a = 0; a < response.length(); a++) {
                                    JSONObject object = response.getJSONObject(a);
                                    String mentoring_id = object.getString("mentoring_id");
                                    String title = object.getString("title");
                                    String topic = object.getString("topic");
                                    String description = object.getString("description");
                                    String duration = object.getString("duration");
                                    String begin_date = object.getString("begin_date");
                                    String end_date = object.getString("end_date");
                                    model = new MentoringModel(mentoring_id, title,topic,description,duration,begin_date,end_date);
                                    listModel.add(model);
                                }
                                adapter = new MentoringAdapter(getActivity(), listModel);
                                listEventSession.setAdapter(adapter);
                                listEventSession.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                                        Intent i = new Intent(getActivity(), MentoringDetailActivity.class);
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
                                        i.putExtra("activity_name", session.getactivity_nameLMS());
                                        i.putExtra("session_name",session.getsession_nameLMS());
                                        i.putExtra("begin_date_activity",session.getbegin_date_activityLMS());
                                        i.putExtra("end_date_activity",session.getend_date_activityLMS());

                                        i.putExtra("title",listModel.get(position).getTitle());
                                        i.putExtra("topic",listModel.get(position).getTopic());
                                        i.putExtra("description",listModel.get(position).getDescription());
                                        i.putExtra("mentoring_id",listModel.get(position).getMentoring_id());
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
