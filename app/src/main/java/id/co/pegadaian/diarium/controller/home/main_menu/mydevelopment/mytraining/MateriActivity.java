package id.co.pegadaian.diarium.controller.home.main_menu.mydevelopment.mytraining;

import androidx.appcompat.app.AppCompatActivity;

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
import id.co.pegadaian.diarium.adapter.TemplateKuisionerAdapter;
import id.co.pegadaian.diarium.adapter.TesAdapter;
import id.co.pegadaian.diarium.model.MateriLMSModel;
import id.co.pegadaian.diarium.model.TemplateKuisionerModel;
import id.co.pegadaian.diarium.model.TesModel;
import id.co.pegadaian.diarium.util.TimeHelper;
import id.co.pegadaian.diarium.util.UserSessionManager;
import id.co.pegadaian.diarium.util.element.ProgressDialogHelper;

public class MateriActivity extends AppCompatActivity {
    String  bussines_code;
    String  batch;
    String  batch_name;
    String  event_id;
    String  event_name;
    String  begin_date;
    String  end_date;
    String  event_curr_stat;
    String  evnt_curr_statid;
    String  event_status;
    String  event_stat_id;
    String  location_id;
    String  location;
    String  cur_id;
    String  curriculum;
    String  event_type;
    String  participant_id;
    String  partcipant_name;
    String  parti_nicknm;
    String  company_name;
    String  activity_name;
    String  session_name;
    String  begin_date_activity;
    String  end_date_activity;
    String  schedule_id;
    String  schedule_name;
    String  topic;
    String  begin_date_schedule;
    String  end_date_schedule;
    String  begin_time_schedule;
    String  end_time_schedule;

    TextView tvEvent, tvBatch, tvLocation, tvType, tvCurriculum, tvDate, tvActivitName, tvSessionName, tvDateActivity, tvCompany, tvScheduleName, tvTopic, tvDateSchedule, tvTimeSchedule;
    ListView listView;
    private ProgressDialogHelper progressDialogHelper;
    UserSessionManager session;
    private List<MateriLMSModel> listModel;
    private MateriLMSModel model;
    private MateriLMSAdapter adapter;

    private List<TemplateKuisionerModel> listModelKuisioner;
    private TemplateKuisionerModel modelKuisioner;
    private TemplateKuisionerAdapter adapterKuisioner;

    private List<TesModel> listModelTes;
    private TesModel modelTes;
    private TesAdapter adapterTes;

    TextView tvNull;
    ListView listEventSession;

    TextView tvNullKuisioner;
    ListView listKuisioner;

    TextView tvNullTes;
    ListView listTes;

    Typeface font,fontbold;
    TimeHelper timeHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_materi);
        timeHelper = new TimeHelper();
        progressDialogHelper = new ProgressDialogHelper();
        session = new UserSessionManager(MateriActivity.this);
        font = Typeface.createFromAsset(MateriActivity.this.getAssets(),"fonts/Nexa Light.otf");
        fontbold = Typeface.createFromAsset(MateriActivity.this.getAssets(),"fonts/Nexa Bold.otf");
        tvEvent = findViewById(R.id.tvEvent);
        tvBatch = findViewById(R.id.tvBatch);
        tvLocation = findViewById(R.id.tvLocation);
        tvType = findViewById(R.id.tvType);
        tvCurriculum = findViewById(R.id.tvCurriculum);
        tvDate = findViewById(R.id.tvDate);
        tvNull = findViewById(R.id.tvNull);
        tvNullKuisioner = findViewById(R.id.tvNullKuisioner);
        tvNullTes = findViewById(R.id.tvNullTes);
        tvActivitName = findViewById(R.id.tvActivityName);
        tvSessionName = findViewById(R.id.tvSessionName);
        tvDateActivity = findViewById(R.id.tvDateActivity);
        tvCompany = findViewById(R.id.tvCompany);
        tvScheduleName = findViewById(R.id.tvScheduleName);
        tvTopic = findViewById(R.id.tvTopic);
        tvDateSchedule = findViewById(R.id.tvDateSchedule);
        tvTimeSchedule = findViewById(R.id.tvTimeSchedule);
        listEventSession = findViewById(R.id.listEventSession);
        listKuisioner = findViewById(R.id.listKuisioner);
        listTes = findViewById(R.id.listTes);

        Intent intent = getIntent();
        bussines_code = intent.getStringExtra("bussines_code");
        batch = intent.getStringExtra("batch");
        batch_name = intent.getStringExtra("batch_name");
        event_id = intent.getStringExtra("event_id");
        event_name = intent.getStringExtra("event_name");
        begin_date = intent.getStringExtra("begin_date");
        end_date = intent.getStringExtra("end_date");
        event_curr_stat = intent.getStringExtra("event_curr_stat");
        evnt_curr_statid = intent.getStringExtra("evnt_curr_statid");
        event_status = intent.getStringExtra("event_status");
        event_stat_id = intent.getStringExtra("event_stat_id");
        location_id = intent.getStringExtra("location_id");
        location = intent.getStringExtra("location");
        cur_id = intent.getStringExtra("cur_id");
        curriculum = intent.getStringExtra("curriculum");
        event_type = intent.getStringExtra("event_type");
        participant_id = intent.getStringExtra("participant_id");
        partcipant_name = intent.getStringExtra("partcipant_name");
        parti_nicknm = intent.getStringExtra("parti_nicknm");
        company_name = intent.getStringExtra("company_name");

        activity_name = intent.getStringExtra("activity_name");
        session_name = intent.getStringExtra("session_name");
        begin_date_activity = intent.getStringExtra("begin_date_activity");
        end_date_activity = intent.getStringExtra("end_date_activity");

        schedule_id = intent.getStringExtra("schedule_id");
        schedule_name = intent.getStringExtra("schedule_name");
        topic = intent.getStringExtra("topic");
        begin_date_schedule = intent.getStringExtra("begin_date_schedule");
        end_date_schedule = intent.getStringExtra("end_date_schedule");
        begin_time_schedule = intent.getStringExtra("begin_time_schedule");
        end_time_schedule = intent.getStringExtra("end_time_schedule");

        tvEvent.setText(event_name);
        tvBatch.setText(batch+ " "+ batch_name);
        tvLocation.setText(location);
        tvType.setText(event_type+" Event");
        tvDate.setText(timeHelper.getTimeFormat(begin_date)+" - "+timeHelper.getTimeFormat(end_date));
        tvEvent.setText(event_name);
        tvCompany.setText(company_name+" - "+location_id);
        tvCurriculum.setText(curriculum);
        tvNull.setVisibility(View.GONE);

        tvActivitName.setText(activity_name);
        tvSessionName.setText(session_name);
        tvDateActivity.setText(timeHelper.getTimeFormat(begin_date_activity)+" - "+timeHelper.getTimeFormat(end_date_activity));

        tvScheduleName.setText(schedule_name);
        tvTopic.setText(topic);
        tvDateSchedule.setText(timeHelper.getTimeFormat(begin_date_schedule));
        tvTimeSchedule.setText(begin_time_schedule+" - "+end_time_schedule);
        tvNull.setVisibility(View.GONE);
        tvNullKuisioner.setVisibility(View.GONE);
        getMateri();
        getKuisioner();
        getTes();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Materi");
    }
    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }


    private void getMateri(){
        System.out.println("MASUKMATERI");
        progressDialogHelper.showProgressDialog(MateriActivity.this, "Getting data...");
        AndroidNetworking.get("https://testapi.digitalevent.id/lms/api/lmsrelation?order[BEGDA]=asc&otype_parent[]=SCHDL&otype_child[]=MATER&relation[]=S003&parent[]=2&begin_date_lte=2019-09-19&end_date_gte=2019-09-19")
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
                                listEventSession.setVisibility(View.GONE);
                            } else {
                                tvNull.setVisibility(View.GONE);
                                listEventSession.setVisibility(View.VISIBLE);
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
                                adapter = new MateriLMSAdapter(MateriActivity.this, listModel);
                                listEventSession.setAdapter(adapter);
                                listEventSession.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                                        if (listModel.get(position).getMateri_address().substring(listModel.get(position).getMateri_address().length()-3).equals("pdf")) {

                                            Intent i = new Intent(Intent.ACTION_VIEW);
                                            i.setData(Uri.parse("https://testapi.digitalevent.id/lms/api/materistream?materi_id="+listModel.get(position).getMateri_id()+"&token="+session.getTokenLdap()));
                                            startActivity(i);
                                        } else {
                                            Intent a = new Intent(MateriActivity.this, VideoActivity.class);
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
                            progressDialogHelper.dismissProgressDialog(MateriActivity.this);
                        }catch (Exception e){
                            progressDialogHelper.dismissProgressDialog(MateriActivity.this);
                            System.out.println(e);
                        }
                    }
                    @Override
                    public void onError(ANError error) {
                        progressDialogHelper.dismissProgressDialog(MateriActivity.this);
                        System.out.println(error);
                    }
                });
    }


    private void getKuisioner(){
        System.out.println("MASUKKUIRIONER");
        progressDialogHelper.showProgressDialog(MateriActivity.this, "Getting data...");
        AndroidNetworking.get("https://testapi.digitalevent.id/lms/api/relationquesioner?order[BEGDA]=asc&include[]=id&include[]=business_code&include[]=template_code&include[]=template_type&include[]=schedule&include[]=relation_type&schedule[]=2&begin_date_lte=2019-10-04&end_date_gte=2019-10-04")
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
                        System.out.println("RESPONKUISIONER"+response);
                        try {
                            listModelKuisioner = new ArrayList<TemplateKuisionerModel>();
                            JSONArray jsonArray = response.getJSONArray("data");
                            System.out.println(jsonArray.length()+"PANJANGDATA");
                            if (jsonArray.length()==0) {
                                tvNullKuisioner.setVisibility(View.VISIBLE);
                                listKuisioner.setVisibility(View.GONE);
                            } else {
                                tvNullKuisioner.setVisibility(View.GONE);
                                listKuisioner.setVisibility(View.VISIBLE);
                                for (int a = 0; a < jsonArray.length(); a++) {
                                    JSONObject object = jsonArray.getJSONObject(a);
                                    String template_id = object.getJSONObject("template_code").getString("id");
                                    String template_name = object.getJSONObject("template_code").getString("value");
                                    String template_type = object.getJSONObject("template_type").getString("value");
                                    modelKuisioner = new TemplateKuisionerModel(template_id,template_name,template_type);
                                    listModelKuisioner.add(modelKuisioner);
                                }
                                adapterKuisioner = new TemplateKuisionerAdapter(MateriActivity.this, listModelKuisioner);
                                listKuisioner.setAdapter(adapterKuisioner);
                                listKuisioner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                                        Intent i = new Intent(MateriActivity.this, KuisionerLMSActivity.class);
                                        i.putExtra("template_code_id",listModelKuisioner.get(position).getTemplate_id());
                                        startActivity(i);
                                    }
                                });
                            }
                            progressDialogHelper.dismissProgressDialog(MateriActivity.this);
                        }catch (Exception e){
                            progressDialogHelper.dismissProgressDialog(MateriActivity.this);
                            System.out.println(e);
                        }

                    }
                    @Override
                    public void onError(ANError error) {
                        progressDialogHelper.dismissProgressDialog(MateriActivity.this);
                        System.out.println(error);
                    }
                });
    }

    private void getTes(){
        System.out.println("MASUKTES");
        progressDialogHelper.showProgressDialog(MateriActivity.this, "Getting data...");
        AndroidNetworking.get("https://testapi.digitalevent.id/lms/api/relationquestion?order[BEGDA]=asc&include[]=business_code&include[]=test_code&include[]=test_type&include[]=schedule&schedule[]=2&begin_date_lte=2019-10-10&end_date_gte=2019-10-10")
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
                        System.out.println("RESPONTES"+response);
                        try {
                            listModelTes = new ArrayList<TesModel>();
                            JSONArray jsonArray = response.getJSONArray("data");
                            System.out.println(jsonArray.length()+"PANJANGDATA");
                            if (jsonArray.length()==0) {
                                tvNullTes.setVisibility(View.VISIBLE);
                                listTes.setVisibility(View.GONE);
                            } else {
                                tvNullTes.setVisibility(View.GONE);
                                listTes.setVisibility(View.VISIBLE);
                                for (int a = 0; a < jsonArray.length(); a++) {
                                    JSONObject object = jsonArray.getJSONObject(a);
                                    String template_name = object.getJSONObject("test_code").getString("value");
                                    String type = object.getJSONObject("test_type").getString("value");
                                    String test_code_id = object.getJSONObject("test_code").getString("id");

                                    System.out.println("TESTCODEID"+test_code_id);

                                    modelTes = new TesModel(template_name,type,test_code_id);
                                    listModelTes.add(modelTes);
                                }
                                adapterTes = new TesAdapter(MateriActivity.this, listModelTes);
                                listTes.setAdapter(adapterTes);
                                listTes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                                        Intent i = new Intent(MateriActivity.this, TestLMSActivity.class);
                                        i.putExtra("test_code_id",listModelTes.get(position).getTest_code_id());
                                        startActivity(i);
                                    }
                                });
                            }
                            progressDialogHelper.dismissProgressDialog(MateriActivity.this);
                        }catch (Exception e){
                            progressDialogHelper.dismissProgressDialog(MateriActivity.this);
                            System.out.println(e);
                        }

                    }
                    @Override
                    public void onError(ANError error) {
                        progressDialogHelper.dismissProgressDialog(MateriActivity.this);
                        System.out.println(error);
                    }
                });
    }
}
