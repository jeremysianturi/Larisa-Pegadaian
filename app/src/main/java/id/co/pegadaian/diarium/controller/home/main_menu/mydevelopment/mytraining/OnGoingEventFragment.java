package id.co.pegadaian.diarium.controller.home.main_menu.mydevelopment.mytraining;


import android.content.Intent;
import android.graphics.Typeface;
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
import id.co.pegadaian.diarium.adapter.TrainingAdapter;
import id.co.pegadaian.diarium.adapter.TrainingTrainerAdapter;
import id.co.pegadaian.diarium.model.TrainingModel;
import id.co.pegadaian.diarium.model.TrainingTrainerModel;
import id.co.pegadaian.diarium.util.UserSessionManager;
import id.co.pegadaian.diarium.util.element.ProgressDialogHelper;

/**
 * A simple {@link Fragment} subclass.
 */
public class OnGoingEventFragment extends Fragment {

    private ProgressDialogHelper progressDialogHelper;
    UserSessionManager session;
    private List<TrainingModel> listModel;
    private List<TrainingTrainerModel> listModelTrainer;
    private TrainingModel model;
    private TrainingTrainerModel modelTrainer;
    private TrainingAdapter adapter;
    private TrainingTrainerAdapter adapterTrainer;
    TextView tvNull;
    ListView listActivity;
    Typeface font,fontbold;

    public OnGoingEventFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_on_going_event, container, false);
        progressDialogHelper = new ProgressDialogHelper();
        session = new UserSessionManager(getActivity());
        font = Typeface.createFromAsset(getActivity().getAssets(),"fonts/Nexa Light.otf");
        fontbold = Typeface.createFromAsset(getActivity().getAssets(),"fonts/Nexa Bold.otf");
        listActivity = view.findViewById(R.id.listOnGoing);
        tvNull = view.findViewById(R.id.tvNull);
        if (session.getRoleLMS().equals("TRAINER")) {
            getEventTrainer();
        } else {
            getEventParticipant();
        }
        return view;
    }

    private void getEventParticipant(){
        System.out.println("MASUKPARTICIPANTONGOING");
        progressDialogHelper.showProgressDialog(getActivity(), "Getting data...");
        AndroidNetworking.get("https://testapi.digitalevent.id/lms/api/myevent/peserta?curr_stat=01&parid=2&stat_relat=ST02")
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
                        System.out.println("RESPONTRAININGONGOING"+response);
                        try {
                            listModel = new ArrayList<TrainingModel>();
                            JSONArray jsonArray = response.getJSONArray("data");
                            System.out.println(jsonArray.length()+"PANJANGDATA");
                            if (jsonArray.length()==0) {
                                tvNull.setVisibility(View.VISIBLE);
                                listActivity.setVisibility(View.GONE);
                            } else {
                                tvNull.setVisibility(View.GONE);
                                listActivity.setVisibility(View.VISIBLE);
                                for (int a = 0; a < jsonArray.length(); a++) {
                                    JSONObject object = jsonArray.getJSONObject(a);
                                    String bussines_code = object.getString("bussines_code");
                                    String batch = object.getString("batch");
                                    String batch_name = object.getString("batch_name");
                                    String event_id = object.getString("event_id");
                                    String event_name = object.getString("event_name");
                                    String begin_date = object.getString("begin_date");
                                    String end_date = object.getString("end_date");
                                    String event_curr_stat = object.getString("event_curr_stat");
                                    String evnt_curr_statid = object.getString("evnt_curr_statid");
                                    String event_status = object.getString("event_status");
                                    String event_stat_id = object.getString("event_stat_id");
                                    String location_id = object.getString("location_id");
                                    String location = object.getString("location");
                                    String cur_id = object.getString("cur_id");
                                    String curriculum = object.getString("curriculum");
                                    String event_type = object.getString("event_type");
                                    String participant_id = object.getString("participant_id");
                                    String partcipant_name = object.getString("partcipant_name");
                                    String parti_nicknm = object.getString("parti_nicknm");
                                    String company_name = object.getString("company_name");
                                    model = new TrainingModel(bussines_code, batch, batch_name, event_id, event_name, begin_date, end_date, event_curr_stat, evnt_curr_statid, event_status, event_stat_id, location_id, location, cur_id, curriculum, event_type, participant_id, partcipant_name, parti_nicknm, company_name);
                                    listModel.add(model);
                                }
                                adapter = new TrainingAdapter(getActivity(), listModel);
                                listActivity.setAdapter(adapter);
                                listActivity.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                                        Intent i = new Intent(getActivity(), EventSessionActivity.class);
                                        i.putExtra("bussines_code",listModel.get(position).getBussines_code());
                                        i.putExtra("batch",listModel.get(position).getBatch());
                                        i.putExtra("batch_name",listModel.get(position).getBatch_name());
                                        i.putExtra("event_id",listModel.get(position).getEvent_id());
                                        i.putExtra("event_name",listModel.get(position).getEvent_name());
                                        i.putExtra("begin_date",listModel.get(position).getBegin_date());
                                        i.putExtra("end_date",listModel.get(position).getEnd_date());
                                        i.putExtra("event_curr_stat",listModel.get(position).getEvent_curr_stat());
                                        i.putExtra("event_status",listModel.get(position).getEvent_status());
                                        i.putExtra("event_stat_id",listModel.get(position).getEvent_stat_id());
                                        i.putExtra("location_id",listModel.get(position).getLocation_id());
                                        i.putExtra("location",listModel.get(position).getLocation());
                                        i.putExtra("cur_id",listModel.get(position).getCur_id());
                                        i.putExtra("curriculum",listModel.get(position).getCurriculum());
                                        i.putExtra("event_type",listModel.get(position).getEvent_type());
                                        i.putExtra("participant_id",listModel.get(position).getParticipant_id());
                                        i.putExtra("partcipant_name",listModel.get(position).getPartcipant_name());
                                        i.putExtra("parti_nicknm",listModel.get(position).getParti_nicknm());
                                        i.putExtra("company_name",listModel.get(position).getCompany_name());
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


    private void getEventTrainer(){
        System.out.println("MASUKTRAINERONGOING");
        progressDialogHelper.showProgressDialog(getActivity(), "Getting data...");
        AndroidNetworking.get("https://testapi.digitalevent.id/lms/api/myevent/trainer?event_status=01&tr_id=2&stat_relat=ST02")
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
                        System.out.println("RESPONTRAININGONGOING"+response);
                        try {
                            listModelTrainer = new ArrayList<TrainingTrainerModel>();
                            JSONArray jsonArray = response.getJSONArray("data");
                            System.out.println(jsonArray.length()+"PANJANGDATA");
                            if (jsonArray.length()==0) {
                                tvNull.setVisibility(View.VISIBLE);
                                listActivity.setVisibility(View.GONE);
                            } else {
                                tvNull.setVisibility(View.GONE);
                                listActivity.setVisibility(View.VISIBLE);
                                for (int a = 0; a < jsonArray.length(); a++) {
                                    JSONObject object = jsonArray.getJSONObject(a);
                                    String BEGDA = object.getString("BEGDA");
                                    String ENDDA = object.getString("ENDDA");
                                    String BUSCD = object.getString("BUSCD");
                                    String PERNR = object.getString("PERNR");
                                    String TRAID = object.getString("TRAID");
                                    String TRNAM = object.getString("TRNAM");
                                    String STTAR = object.getString("STTAR");
                                    String BUSC1 = object.getString("BUSC1");
                                    String CHGDT = object.getString("CHGDT");
                                    String CHUSR = object.getString("CHUSR");
                                    String trainer_name = object.getString("trainer_name");
                                    String schedule_id  = object.getString("schedule_id");
                                    String schedule_name = object.getString("schedule_name");
                                    String session_id = object.getString("session_id");
                                    String topic = object.getString("topic");
                                    String begin_time = object.getString("begin_time");
                                    String end_time  = object.getString("end_time");
                                    String day_number = object.getString("day_number");
                                    String batch_name = object.getString("batch_name");
                                    String batch = object.getString("batch");
                                    String session_name = object.getString("session_name");
                                    String event_id = object.getString("event_id");
                                    String event_name = object.getString("event_name");
                                    String event_status = object.getString("event_status");
                                    String situation_code = object.getString("situation_code");
                                    String situation_name = object.getString("situation_name");
                                    String event_stat_id = object.getString("event_stat_id");
                                    modelTrainer = new TrainingTrainerModel(BEGDA, ENDDA, BUSCD, PERNR, TRAID, TRNAM, STTAR, BUSC1, CHGDT, CHUSR, trainer_name, schedule_id, schedule_name, session_id, topic, begin_time, end_time, day_number, batch_name, batch, session_name, event_id, event_name, event_status, situation_code,situation_name, event_stat_id);
                                    listModelTrainer.add(modelTrainer);
                                }
                                adapterTrainer = new TrainingTrainerAdapter(getActivity(), listModelTrainer);
                                listActivity.setAdapter(adapterTrainer);
                                listActivity.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                                        Intent i = new Intent(getActivity(), MateriTrainerActivity.class);
                                        i.putExtra("BEGDA",listModelTrainer.get(position).getBEGDA());
                                        i.putExtra("ENDDA",listModelTrainer.get(position).getENDDA());
                                        i.putExtra("BUSCD",listModelTrainer.get(position).getBUSCD());
                                        i.putExtra("PERNR",listModelTrainer.get(position).getPERNR());
                                        i.putExtra("TRAID",listModelTrainer.get(position).getTRAID());
                                        i.putExtra("TRNAM",listModelTrainer.get(position).getTRNAM());
                                        i.putExtra("STTAR",listModelTrainer.get(position).getSTTAR());
                                        i.putExtra("BUSC1",listModelTrainer.get(position).getBUSC1());
                                        i.putExtra("CHGDT",listModelTrainer.get(position).getCHGDT());
                                        i.putExtra("CHUSR",listModelTrainer.get(position).getCHUSR());
                                        i.putExtra("trainer_name",listModelTrainer.get(position).getTrainer_name());
                                        i.putExtra("schedule_id",listModelTrainer.get(position).getSchedule_id());
                                        i.putExtra("schedule_name",listModelTrainer.get(position).getSchedule_name());
                                        i.putExtra("session_id",listModelTrainer.get(position).getSession_id());
                                        i.putExtra("topic",listModelTrainer.get(position).getTopic());
                                        i.putExtra("begin_time",listModelTrainer.get(position).getBegin_time());
                                        i.putExtra("end_time",listModelTrainer.get(position).getEnd_time());
                                        i.putExtra("day_number",listModelTrainer.get(position).getDay_number());
                                        i.putExtra("batch_name",listModelTrainer.get(position).getBatch_name());
                                        i.putExtra("batch",listModelTrainer.get(position).getBatch());
                                        i.putExtra("session_name",listModelTrainer.get(position).getSession_name());
                                        i.putExtra("event_id",listModelTrainer.get(position).getEvent_id());
                                        i.putExtra("event_name",listModelTrainer.get(position).getEvent_name());
                                        i.putExtra("event_status",listModelTrainer.get(position).getEvent_status());
                                        i.putExtra("situation_code",listModelTrainer.get(position).getSituation_code());
                                        i.putExtra("situation_name",listModelTrainer.get(position).getSituation_name());
                                        i.putExtra("event_stat_id",listModelTrainer.get(position).getEvent_stat_id());
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