package id.co.pegadaian.diarium.controller.home.main_menu.mydevelopment.mytraining;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
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
import id.co.pegadaian.diarium.adapter.MateriLMSAdapter;
import id.co.pegadaian.diarium.adapter.RoomAdapter;
import id.co.pegadaian.diarium.adapter.TrainerAdapter;
import id.co.pegadaian.diarium.model.MateriLMSModel;
import id.co.pegadaian.diarium.model.RoomModel;
import id.co.pegadaian.diarium.model.TrainerModel;
import id.co.pegadaian.diarium.util.TimeHelper;
import id.co.pegadaian.diarium.util.UserSessionManager;
import id.co.pegadaian.diarium.util.element.ProgressDialogHelper;

public class MateriTrainerActivity extends AppCompatActivity {
    TextView tvEventName, tvEventDate, tvEventType, tvEventCurriculum, tvEventCompany, tvEventLocation,tvEventBatch;
    TextView tvActivityName, tvSessionName, tvActivityDate;
    TextView tvScheduleName, tvTopic, tvDateSchedule, tvTimeSchedule;
    TextView tvNull, tvNullRoom, tvNullTrainer;

    ListView listMateri;
    ListView listRoom;
    ListView listTrainer;

    private ProgressDialogHelper progressDialogHelper;
    UserSessionManager session;
    private List<MateriLMSModel> listModel;
    private MateriLMSModel model;
    private MateriLMSAdapter adapter;

    private List<RoomModel> listModelRoom;
    private RoomModel modelRoom;
    private RoomAdapter adapterRoom;

    private List<TrainerModel> listModelTrainer;
    private TrainerModel modelTrainer;
    private TrainerAdapter adapterTrainer;

    Typeface font,fontbold;
    TimeHelper timeHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_materi_trainer);
        timeHelper = new TimeHelper();
        progressDialogHelper = new ProgressDialogHelper();
        session = new UserSessionManager(MateriTrainerActivity.this);
        font = Typeface.createFromAsset(MateriTrainerActivity.this.getAssets(),"fonts/Nexa Light.otf");
        fontbold = Typeface.createFromAsset(MateriTrainerActivity.this.getAssets(),"fonts/Nexa Bold.otf");

        //========================================================================================== EVENT
        tvEventName = findViewById(R.id.tvEventName);
        tvEventDate = findViewById(R.id.tvEventDate);
        tvEventType = findViewById(R.id.tvEventType);
        tvEventCurriculum = findViewById(R.id.tvEventCurriculum);
        tvEventCompany = findViewById(R.id.tvEventCompany);
        tvEventLocation = findViewById(R.id.tvEventLocation);
        tvEventBatch = findViewById(R.id.tvEventBatch);
        tvEventBatch.setVisibility(View.GONE);

        //========================================================================================== ACTIVITY
        tvActivityName = findViewById(R.id.tvActivityName);
        tvSessionName = findViewById(R.id.tvSessionName);
        tvActivityDate = findViewById(R.id.tvActivityDate);

        //========================================================================================== SCHEDULE
        tvScheduleName = findViewById(R.id.tvScheduleName);
        tvTopic = findViewById(R.id.tvTopic);
        tvDateSchedule = findViewById(R.id.tvDateSchedule);
        tvTimeSchedule = findViewById(R.id.tvTimeSchedule);

        tvNull = findViewById(R.id.tvNull);
        tvNullRoom = findViewById(R.id.tvNullRoom);
        tvNullTrainer = findViewById(R.id.tvNullTrainer);
        listMateri = findViewById(R.id.listMateri);
        listRoom = findViewById(R.id.listRoom);
        listTrainer = findViewById(R.id.listTrainer);

        Intent intent = getIntent();
//        bussines_code = intent.getStringExtra("bussines_code");
        String BEGDA  = intent.getStringExtra("BEGDA");
        String ENDDA  = intent.getStringExtra("ENDDA");
        String BUSCD  = intent.getStringExtra("BUSCD");
        String PERNR  = intent.getStringExtra("PERNR");
        String TRAID  = intent.getStringExtra("TRAID");
        String TRNAM  = intent.getStringExtra("TRNAM");
        String STTAR  = intent.getStringExtra("STTAR");
        String BUSC1 = intent.getStringExtra("BUSC1");
        String CHGDT = intent.getStringExtra("CHGDT");
        String CHUSR = intent.getStringExtra("CHUSR");
        String trainer_name = intent.getStringExtra("trainer_name");
        String schedule_id = intent.getStringExtra("schedule_id");
        String schedule_name = intent.getStringExtra("schedule_name");
        String session_id = intent.getStringExtra("session_id");
        String topic = intent.getStringExtra("topic");
        String begin_time = intent.getStringExtra("begin_time");
        String end_time = intent.getStringExtra("end_time");
        String day_number = intent.getStringExtra("day_number");
        String batch_name = intent.getStringExtra("batch_name");
        String batch = intent.getStringExtra("batch");
        String session_name = intent.getStringExtra("session_name");
        String event_id = intent.getStringExtra("event_id");
        String event_name = intent.getStringExtra("event_name");
        String event_status = intent.getStringExtra("event_status");
        String situation_code = intent.getStringExtra("situation_code");
        String situation_name = intent.getStringExtra("situation_name");
        String event_stat_id = intent.getStringExtra("event_stat_id");




        getEvent(event_id);
        getSession(session_id);
        getSchedule(schedule_id);
        getMateri();
        getRoom();
        getTrainer();
//        getMateri();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Materi");
    }



    private void getEvent(String event_id){
        System.out.println("GETEVENTTRAINER");
        AndroidNetworking.get("https://testapi.digitalevent.id/lms/api/event?event_id[]="+event_id)
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
                        System.out.println("RESPONEVENTTRAINER"+response);
                        try {
                            JSONArray jsonArray = response.getJSONArray("data");
                            System.out.println(jsonArray.length()+"PANJANGDATA");
                            String event_name = null, begin_date = null, event_type = null, location = null, batch_name = null, company_name = null, event_curr_stat = null;
                            for (int a = 0; a < jsonArray.length(); a++) {
                                JSONObject object = jsonArray.getJSONObject(a);
                                event_name = object.getString("event_name");
                                begin_date = object.getString("begin_date");
                                event_curr_stat = object.getString("description");
                                location = object.getJSONObject("vendor").getString("street");
                                event_type = object.getJSONObject("event_type").getString("value");
                                company_name = object.getJSONObject("vendor").getString("company_name");
                            }
                            tvEventName.setText(event_name);
                            tvEventDate.setText(timeHelper.getTimeFormat(begin_date));
                            tvEventType.setText(event_type);
                            tvEventCurriculum.setText(event_curr_stat);
                            tvEventCompany.setText(company_name);
                            tvEventLocation.setText(location);
//                            tvEventBatch.setText(batch_name);
                        }catch (Exception e){
                            System.out.println(e);
                        }

                    }
                    @Override
                    public void onError(ANError error) {
                        System.out.println(error);
                    }
                });
    }

    private void getSchedule(String schedule_id){
        AndroidNetworking.get("https://testapi.digitalevent.id/lms/api/schedule?schedule_id[]="+schedule_id)
                .addHeaders("Accept","application/json")
                .addHeaders("Content-Type","application/json")
                .addHeaders("Authorization","Bearer "+session.getTokenLdap())
                //.addJSONObjectBody(body)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onResponse(JSONObject response) {
                        // do anything with response
                        System.out.println("RESPONSCHEDULE"+response);
                        try {
                            JSONArray jsonArray = response.getJSONArray("data");
                            String schedule_name = null, topic = null, schedule_date = null, schedule_time_start = null, schedule_time_end = null;
                            for (int a = 0; a < jsonArray.length(); a++) {
                                JSONObject object = jsonArray.getJSONObject(a);
                                schedule_name = object.getString("schedule_name");
                                topic = object.getString("topic");
                                schedule_date = object.getString("begin_date");
                                schedule_time_start = object.getString("begin_time");
                                schedule_time_end = object.getString("end_time");
                            }
                            tvScheduleName.setText(schedule_name);
                            tvTopic.setText(topic);
                            tvDateSchedule.setText(timeHelper.getTimeFormat(schedule_date));
                            tvTimeSchedule.setText(schedule_time_start+" - "+schedule_time_end);
                        }catch (Exception e){
                            System.out.println(e);
                        }

                    }
                    @Override
                    public void onError(ANError error) {
                        System.out.println(error);
                    }
                });
    }

    private void getSession(String session_id){
        System.out.println("GETACTIVITY");
        AndroidNetworking.get("https://testapi.digitalevent.id/lms/api/session?session_id[]="+session_id)
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
                            JSONArray jsonArray = response.getJSONArray("data");
                            System.out.println(jsonArray.length()+"PANJANGDATA");
                            String activity_name = null, session_name = null, activity_date = null;
                            for (int a = 0; a < jsonArray.length(); a++) {
                                JSONObject object = jsonArray.getJSONObject(a);
                                activity_name = object.getJSONObject("learning_activity").getString("activity_name");
                                session_name = object.getJSONObject("learning_activity").getJSONObject("cycle").getString("value");
                                activity_date = object.getJSONObject("learning_activity").getString("begin_date");
                            }
                            tvActivityName.setText(activity_name);
                            tvSessionName.setText(session_name);
                            tvActivityDate.setText(activity_date);
                        }catch (Exception e){
                            System.out.println(e);
                        }

                    }
                    @Override
                    public void onError(ANError error) {
                        System.out.println(error);
                    }
                });
    }

    private void getBatch(String batch_id){
        System.out.println("GETACTIVITY");
        AndroidNetworking.get("https://testapi.digitalevent.id/lms/api/batch?batch_id[]="+batch_id)
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
                            JSONArray jsonArray = response.getJSONArray("data");
                            System.out.println(jsonArray.length()+"PANJANGDATA");
                            for (int a = 0; a < jsonArray.length(); a++) {
                                JSONObject object = jsonArray.getJSONObject(a);
                                String session_name = object.getString("session_name");
                                String activity_name = object.getJSONObject("learning_activity").getString("activity_name");
                                String begin_date = object.getJSONObject("learning_activity").getString("begin_date");
                                String end_date = object.getJSONObject("learning_activity").getString("end_date");
                                String activity_id = object.getJSONObject("learning_activity").getString("activity_id");
                            }
                        }catch (Exception e){
                            System.out.println(e);
                        }

                    }
                    @Override
                    public void onError(ANError error) {
                        System.out.println(error);
                    }
                });
    }


    private void getMateri(){
        System.out.println("MASUKMATERI");
        progressDialogHelper.showProgressDialog(MateriTrainerActivity.this, "Getting data...");
        AndroidNetworking.get("https://testapi.digitalevent.id/lms/api/lmsrelation?order[BEGDA]=asc&otype_parent[]=SCHDL&otype_child[]=MATER&relation[]=S003&parent[]=2&begin_date_lte=2019-09-30&end_date_gte=2019-09-30")
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
                            listModel = new ArrayList<MateriLMSModel>();
                            JSONArray jsonArray = response.getJSONArray("data");
                            System.out.println(jsonArray.length()+"PANJANGDATA");
                            if (jsonArray.length()==0) {
                                tvNull.setVisibility(View.VISIBLE);
                                listMateri.setVisibility(View.GONE);
                            } else {
                                tvNull.setVisibility(View.GONE);
                                listMateri.setVisibility(View.VISIBLE);
                                for (int a = 0; a < jsonArray.length(); a++) {
                                    JSONObject object = jsonArray.getJSONObject(a);
                                    String materi_id = object.getJSONObject("child").getString("materi_id");
                                    String materi_name = object.getJSONObject("child").getString("materi_name");
                                    String materi_type_value = object.getJSONObject("child").getJSONObject("materi_type").getString("value");
                                    String materi_address = object.getJSONObject("child").getString("address");
                                    String method_value = object.getJSONObject("child").getJSONObject("method").getString("value");
                                    String competence_value = object.getJSONObject("child").getJSONObject("competence").getString("value");
                                    String pl_code_value = object.getJSONObject("child").getJSONObject("pl_code").getString("value");
                                    model = new MateriLMSModel(materi_id,materi_name,materi_type_value,materi_address,method_value,competence_value,pl_code_value);
                                    listModel.add(model);
                                }
                                adapter = new MateriLMSAdapter(MateriTrainerActivity.this, listModel);
                                listMateri.setAdapter(adapter);
                                listMateri.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                                        if (listModel.get(position).getMateri_address().substring(listModel.get(position).getMateri_address().length()-3).equals("pdf")) {

                                            Intent i = new Intent(Intent.ACTION_VIEW);
                                            i.setData(Uri.parse("https://testapi.digitalevent.id/lms/api/materistream?materi_id="+listModel.get(position).getMateri_id()+"&token="+session.getTokenLdap()));
                                            startActivity(i);
                                        } else {
                                            Intent a = new Intent(MateriTrainerActivity.this, VideoActivity.class);
                                            a.putExtra("url","https://testapi.digitalevent.id/lms/api/materistream?materi_id="+listModel.get(position).getMateri_id()+"&token="+session.getTokenLdap());
                                            startActivity(a);
                                        }

//
//                                        Intent i = new Intent(MateriActivity.this, VideoActivity.class);
//                                        i.putExtra("title",listModel.get(position).getMateri_name());
//                                        i.putExtra("url",listModel.get(position).getMateri_address());
////                                        i.putExtra("batch_name",listModel.get(position).getBatch_name());
////                                        i.putExtra("event_id",listModel.get(position).getEvent_id());
////                                        i.putExtra("event_name",listModel.get(position).getEvent_name());
////                                        i.putExtra("begin_date",listModel.get(position).getBegin_date());
////                                        i.putExtra("end_date",listModel.get(position).getEnd_date());
////                                        i.putExtra("event_curr_stat",listModel.get(position).getEvent_curr_stat());
////                                        i.putExtra("event_status",listModel.get(position).getEvent_status());
////                                        i.putExtra("event_stat_id",listModel.get(position).getEvent_stat_id());
////                                        i.putExtra("location_id",listModel.get(position).getLocation_id());
////                                        i.putExtra("location",listModel.get(position).getLocation());
////                                        i.putExtra("cur_id",listModel.get(position).getCur_id());
////                                        i.putExtra("curriculum",listModel.get(position).getCurriculum());
////                                        i.putExtra("event_type",listModel.get(position).getEvent_type());
////                                        i.putExtra("participant_id",listModel.get(position).getParticipant_id());
////                                        i.putExtra("partcipant_name",listModel.get(position).getPartcipant_name());
////                                        i.putExtra("parti_nicknm",listModel.get(position).getParti_nicknm());
////                                        i.putExtra("company_name",listModel.get(position).getCompany_name());
//                                        startActivity(i);
                                    }
                                });
                            }
                            progressDialogHelper.dismissProgressDialog(MateriTrainerActivity.this);
                        }catch (Exception e){
                            progressDialogHelper.dismissProgressDialog(MateriTrainerActivity.this);
                            System.out.println(e);
                        }

                    }
                    @Override
                    public void onError(ANError error) {
                        progressDialogHelper.dismissProgressDialog(MateriTrainerActivity.this);
                        System.out.println(error);
                    }
                });
    }


    private void getRoom(){
        System.out.println("MASUKROOM");
        progressDialogHelper.showProgressDialog(MateriTrainerActivity.this, "Getting data...");
        AndroidNetworking.get("https://testapi.digitalevent.id/lms/api/lmsrelation?order[BEGDA]=asc&otype_parent[]=SCHDL&otype_child[]=ROOMS&relation[]=S001&parent[]=2&begin_date_lte=2019-09-30&end_date_gte=2019-09-30")
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
                        System.out.println("RESPONROOM"+response);
                        try {
                            listModelRoom = new ArrayList<RoomModel>();
                            JSONArray jsonArray = response.getJSONArray("data");
                            System.out.println(jsonArray.length()+"PANJANGDATA");
                            if (jsonArray.length()==0) {
                                tvNullRoom.setVisibility(View.VISIBLE);
                                listRoom.setVisibility(View.GONE);
                            } else {
                                tvNullRoom.setVisibility(View.GONE);
                                listRoom.setVisibility(View.VISIBLE);
                                for (int a = 0; a < jsonArray.length(); a++) {
                                    JSONObject object = jsonArray.getJSONObject(a);
                                    String room_id = object.getJSONObject("child").getString("room_id");
                                    String room_name = object.getJSONObject("child").getString("room_name");
                                    String floor = object.getJSONObject("child").getString("floor");
                                    String building = object.getJSONObject("child").getJSONObject("building").getString("building_name");
                                    modelRoom = new RoomModel(room_id,room_name,floor,building);
                                    listModelRoom.add(modelRoom);
                                }
                                adapterRoom = new RoomAdapter(MateriTrainerActivity.this, listModelRoom);
                                listRoom.setAdapter(adapterRoom);
                            }
                            progressDialogHelper.dismissProgressDialog(MateriTrainerActivity.this);
                        }catch (Exception e){
                            progressDialogHelper.dismissProgressDialog(MateriTrainerActivity.this);
                            System.out.println(e);
                        }

                    }
                    @Override
                    public void onError(ANError error) {
                        progressDialogHelper.dismissProgressDialog(MateriTrainerActivity.this);
                        System.out.println(error);
                    }
                });
    }

    private void getTrainer(){
        System.out.println("MASUKROOM");
        progressDialogHelper.showProgressDialog(MateriTrainerActivity.this, "Getting data...");
        AndroidNetworking.get("https://testapi.digitalevent.id/lms/api/lmsrelation?order[BEGDA]=asc&otype_parent[]=SCHDL&otype_child[]=TRAINR&parent[]=2&relation[]=ST02&begin_date_lte=2019-09-30&end_date_gte=2019-09-30")
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
                        System.out.println("RESPONROOM"+response);
                        try {
                            listModelTrainer = new ArrayList<TrainerModel>();
                            JSONArray jsonArray = response.getJSONArray("data");
                            System.out.println(jsonArray.length()+"PANJANGDATA");
                            if (jsonArray.length()==0) {
                                tvNullTrainer.setVisibility(View.VISIBLE);
                                listTrainer.setVisibility(View.GONE);
                            } else {
                                tvNullTrainer.setVisibility(View.GONE);
                                listTrainer.setVisibility(View.VISIBLE);
                                for (int a = 0; a < jsonArray.length(); a++) {
                                    JSONObject object = jsonArray.getJSONObject(a);
                                    String name = object.getJSONObject("child").getString("trainer_name");
                                    String status = object.getJSONObject("child").getJSONObject("trainer_status").getString("value");
                                    String company = object.getJSONObject("child").getJSONObject("business_code").getString("company_name");
                                    modelTrainer = new TrainerModel(name,status,company);
                                    listModelTrainer.add(modelTrainer);
                                }
                                adapterTrainer = new TrainerAdapter(MateriTrainerActivity.this, listModelTrainer);
                                listTrainer.setAdapter(adapterTrainer);
                            }
                            progressDialogHelper.dismissProgressDialog(MateriTrainerActivity.this);
                        }catch (Exception e){
                            progressDialogHelper.dismissProgressDialog(MateriTrainerActivity.this);
                            System.out.println(e);
                        }

                    }
                    @Override
                    public void onError(ANError error) {
                        progressDialogHelper.dismissProgressDialog(MateriTrainerActivity.this);
                        System.out.println(error);
                    }
                });
    }


}
