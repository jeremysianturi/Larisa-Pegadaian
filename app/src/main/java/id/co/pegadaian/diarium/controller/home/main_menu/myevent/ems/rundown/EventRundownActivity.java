package id.co.pegadaian.diarium.controller.home.main_menu.myevent.ems.rundown;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import id.co.pegadaian.diarium.R;
import id.co.pegadaian.diarium.adapter.RundownAdapter;
import id.co.pegadaian.diarium.model.AgendaModel;
import id.co.pegadaian.diarium.model.RundownModel;
import id.co.pegadaian.diarium.util.DataSession;
import id.co.pegadaian.diarium.util.UserSessionManager;

public class EventRundownActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private UserSessionManager sessionManager;
    private ProgressBar progressBar;
    private List<String> dayList;
    private List<String> dateList;
    private AgendaModel model;
    private List<AgendaModel> modelList;
    private String theDay, longs, lats;
    private List<RundownModel> rundownModelList;
    private DataSession dataSess;
    private String idAgenda = "", idEvent;
    TextView tanggal;
    //    private int kode;
//    private AgendaEventAdapter adapterHari;
    RecyclerView lvTime;
    private RundownModel rundownModel;
    private RundownAdapter adapterAcara;
    ListView lvRundown;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_rundown);
        sessionManager = new UserSessionManager(getApplicationContext());
        dataSess = new DataSession(EventRundownActivity.this, "event" + idAgenda);
        LinearLayoutManager MyLayoutManager = new LinearLayoutManager(EventRundownActivity.this);
        MyLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        progressBar = findViewById(R.id.progressBar);
        tanggal = findViewById(R.id.tanggal);
        lvTime = findViewById(R.id.lv_time);
        lvRundown = findViewById(R.id.lv_rundown);

        progressBar.setVisibility(View.GONE);
//        getAgenda(sessionManager.getEventId());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Schedule");
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

//    public void getAgenda(final String id) {
//        progressBar.setVisibility(View.VISIBLE);
//        System.out.println("agenda " + id);
//        AndroidNetworking.get(ConstantUtils.URL.AGENDA + "{event_id}")
//                .addPathParameter("event_id", id)
//                .setTag("Agenda")
//                .setPriority(Priority.MEDIUM)
//                .build()
//                .getAsJSONObject(new JSONObjectRequestListener() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        try {
//                            SimpleDateFormat formatter = new SimpleDateFormat("dd MMMM yyyy");
//                            Date local = new Date();
//                            modelList = new ArrayList<AgendaModel>();
//                            dayList = new ArrayList<String>();
//                            dateList = new ArrayList<String>();
//                            JSONArray jsonArray = response.getJSONArray(ConstantUtils.AGENDA.agenda);
//                            for (int a = 0; a < jsonArray.length(); a++) {
//                                JSONObject object = jsonArray.getJSONObject(a);
//                                String id = object.getString(ConstantUtils.AGENDA.agenda_id);
//                                String name = object.getString(ConstantUtils.AGENDA.agenda_name);
//                                String event = object.getString(ConstantUtils.AGENDA.event_id);
//                                String date = object.getString(ConstantUtils.AGENDA.agenda_date);
//                                String day = object.getString(ConstantUtils.AGENDA.day_x);
//                                model = new AgendaModel(id, name, date, event, day,"","","","");
//                                modelList.add(model);
//                                dayList.add(day);
//                                dateList.add(date);
//
//                                //convert date server
//                                SimpleDateFormat fServer = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                                try {
//                                    Date dateServer = fServer.parse(date);
//                                    SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy", new Locale("id"));
//                                    theDay = sdf.format(dateServer);
//                                    String today = formatter.format(local);
//
//                                    if (idAgenda.isEmpty()) {
//                                        getRundown(modelList.get(0).getAgenda_id(), theDay);
//                                        if (theDay.equals(today)) {
//                                            tanggal.setText(theDay);
////                                            kode = Integer.parseInt(id);
//                                            adapterHari = new AgendaEventAdapter(EventRundownActivity.this, dayList, 4);
//                                            getRundown(id, theDay);
//                                        }
//                                    }
//                                } catch (ParseException e) {
//                                    e.printStackTrace();
//                                }
//                            }
//
//                            adapterHari = new AgendaEventAdapter(EventRundownActivity.this, dayList, new OnItemClickListener() {
//                                @Override
//                                public void onItemClick(String id) {
////                                    agendaName = modelList.get(Integer.parseInt(id) - 1).getAgenda_name();
//                                    String date = modelList.get(Integer.parseInt(id) - 1).getAgenda_date();
//                                    SimpleDateFormat fServer = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                                    try {
//                                        Date dateServer = fServer.parse(date);
//                                        SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy", new Locale("id"));
//                                        String theDay = sdf.format(dateServer);
//                                        tanggal.setText(theDay);
//                                    } catch (ParseException e) {
//                                        e.printStackTrace();
//                                    }
//                                    idAgenda = modelList.get(Integer.parseInt(id) - 1).getAgenda_id();;
//                                    getRundown(idAgenda, theDay);
//                                }
//                            });
//
//                            LinearLayoutManager MyLayoutManager = new LinearLayoutManager(EventRundownActivity.this);
//                            MyLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
//
//                            lvTime.setHasFixedSize(true);
//                            lvTime.setAdapter(adapterHari);
//                            lvTime.setLayoutManager(MyLayoutManager);
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//
//                        progressBar.setVisibility(View.GONE);
//                    }
//
//                    @Override
//                    public void onError(ANError anError) {
//                        progressBar.setVisibility(View.GONE);
//                    }
//                });
//    }


//    public void getRundown(final String id, final String day) {
//        progressBar.setVisibility(View.VISIBLE);
//        AndroidNetworking.get(ConstantUtils.URL.RUNDOWN + "{agenda_id}")
//                .addPathParameter("agenda_id", id)
//                .setTag("Rundwon")
//                .setPriority(Priority.MEDIUM)
//                .build()
//                .getAsJSONObject(new JSONObjectRequestListener() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        System.out.println(response+"3d2jdoi32"+id+day);
//                        try {
//                            rundownModelList = new ArrayList<RundownModel>();
//                            dayList = new ArrayList<String>();
//                            JSONArray jsonArray = response.getJSONArray(ConstantUtils.RUNDOWN.TAG_TITLE);
//                            for (int a = 0; a < jsonArray.length(); a++) {
//                                JSONObject object = jsonArray.getJSONObject(a);
//                                String id = object.getString(ConstantUtils.RUNDOWN.TAG_ID);
//                                String name = object.getString(ConstantUtils.RUNDOWN.TAG_NAME);
//                                String desc = object.getString(ConstantUtils.RUNDOWN.TAG_DESC);
//                                String start = object.getString(ConstantUtils.RUNDOWN.TAG_START);
//                                String end = object.getString(ConstantUtils.RUNDOWN.TAG_END);
//                                String place = object.getString(ConstantUtils.RUNDOWN.TAG_PLACE);
//                                String layout = object.getString(ConstantUtils.RUNDOWN.TAG_LAYOUT);
//                                rundownModel = new RundownModel(id, name, start, end, place, layout, desc);
//                                rundownModelList.add(rundownModel);
//                            }
//
//                            adapterAcara = new RundownAdapter(EventRundownActivity.this.getApplicationContext(), rundownModelList, dataSess, idEvent, idAgenda);
//                            lvRundown.setAdapter(adapterAcara);
////                            lvRundown.setOnItemClickListener(new AdapterView.OnItemClickListener() {
////                                @Override
////                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
////                                    if (session.isLogin()) {
////                                        Intent intent = new Intent(getActivity().getApplicationContext(), DetailEventActivity.class);
////                                        intent.putExtra(ConstantUtils.RUNDOWN.TAG_ID, rundownModelList.get(position).getRundown_id());
////                                        intent.putExtra(ConstantUtils.RUNDOWN.TAG_NAME, rundownModelList.get(position).getRundown_name());
////                                        intent.putExtra(ConstantUtils.RUNDOWN.TAG_START, rundownModelList.get(position).getRundown_start());
////                                        intent.putExtra(ConstantUtils.RUNDOWN.TAG_END, rundownModelList.get(position).getRundown_end());
////                                        intent.putExtra(ConstantUtils.RUNDOWN.TAG_PLACE, rundownModelList.get(position).getRundown_place());
////                                        intent.putExtra(ConstantUtils.RUNDOWN.TAG_LAYOUT, rundownModelList.get(position).getRundown_layout());
////                                        intent.putExtra(ConstantUtils.RUNDOWN.TAG_DESC, rundownModelList.get(position).getRundown_desc());
////                                        intent.putExtra(ConstantUtils.AGENDA.TAG_NAME, agendaName);
////                                        intent.putExtra(ConstantUtils.AGENDA.TAG_DATE, tanggal.getText());
////                                        intent.putExtra("hari", day);
////                                        startActivity(intent);
////                                    } else {
////                                        Toast.makeText(getActivity(), "You have to login first", Toast.LENGTH_SHORT).show();
////                                    }
////                                }
////                            });
//
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                        progressBar.setVisibility(View.GONE);
//                    }
//
//                    @Override
//                    public void onError(ANError anError) {
//                        progressBar.setVisibility(View.GONE);
//                    }
//                });
//    }
}