package id.co.pegadaian.diarium.controller.home.main_menu.myevent.ems.rundown;


import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import id.co.pegadaian.diarium.R;
import id.co.pegadaian.diarium.adapter.AgendaAdapter;
import id.co.pegadaian.diarium.adapter.RundownAdapter;
import id.co.pegadaian.diarium.model.AgendaModel;
import id.co.pegadaian.diarium.model.RundownModel;
import id.co.pegadaian.diarium.util.DataSession;
import id.co.pegadaian.diarium.util.OnItemClickListener;
import id.co.pegadaian.diarium.util.UserSessionManager;

/**
 * A simple {@link Fragment} subclass.
 */
public class RundownFragment extends Fragment {

    private DataSession dataSess;
    private UserSessionManager session;
    Typeface font, fontbold;
    private String idAgenda = "", idEvent;
    private ProgressBar progressBar;
    private RecyclerView lv_time;
    private ListView lv_rundown;
    private TextView tanggal;
    private AgendaModel agendaModel;
    private List<AgendaModel> agendaModelList;
    private AgendaAdapter adapterAgenda;
    private RundownModel rundownModel;
    private List<RundownModel> rundownModelList;
    private RundownAdapter adapterRundown;
    private List<String> dayList;
    private List<String> dateList;
    private String theDay, agendaName, longs, lats;
    private int kode;
    TextView tvTgl;
    public RundownFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_rundown, container, false);
        dataSess = new DataSession(getActivity(), "event" + idAgenda);
        session = new UserSessionManager(getActivity());
        LinearLayoutManager MyLayoutManager = new LinearLayoutManager(getActivity());
        MyLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Nexa Light.otf");
        fontbold = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Nexa Bold.otf");
        progressBar = view.findViewById(R.id.progressBar);
        lv_time = view.findViewById(R.id.lv_time);
        lv_rundown = view.findViewById(R.id.lv_rundown);
        progressBar.setVisibility(View.GONE);
        tanggal = view.findViewById(R.id.tanggal);
        tanggal.setTypeface(font);
        String myValue = this.getArguments().getString("name");
        tvTgl = (TextView) view.findViewById(R.id.tvTanggal);
        tvTgl.setText(myValue);
        String agendaId = this.getArguments().getString("agenda_id");
        idEvent = agendaId;
        getAgenda(agendaId);
        getRundown("2");
        return view;
    }

    private void getAgenda(String agenda_id){
//        progressDialogHelper.showProgressDialog(ReportActivity.this, "Getting data...");
        AndroidNetworking.get(session.getServerURL()+"users/eventEMS/"+session.getEventId()+"/agenda/buscd/"+session.getUserBusinessCode())
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
                        System.out.println(response+"agendajehri4u");
                        try {
                            SimpleDateFormat formatter = new SimpleDateFormat("dd MMMM yyyy");
                            Date local = new Date();
                            agendaModelList = new ArrayList<AgendaModel>();
                            dayList = new ArrayList<String>();
                            dateList = new ArrayList<String>();
                            if(response.getInt("status")==200){
//                                session.setToken(response.getString("token"));
                                agendaModelList = new ArrayList<AgendaModel>();
                                JSONArray jsonArray = response.getJSONArray("data");
                                for (int a = 0; a < jsonArray.length(); a++) {
                                    JSONObject object = jsonArray.getJSONObject(a);
                                    String begin_date = object.getString("begin_date");
                                    String end_date = object.getString("end_date");
                                    String business_code = object.getString("business_code");
                                    String personal_number = object.getString("personal_number");
                                    String event_id = object.getString("event_id");
                                    String agenda_id = object.getString("agenda_id");
                                    String name = object.getString("name");
                                    String date = object.getString("date");
                                    String day = object.getString("day");
                                    String location = object.getString("location");
                                    String change_date = object.getString("change_date");
                                    String change_user = object.getString("change_user");
                                    agendaModel = new AgendaModel(begin_date, end_date, business_code, personal_number, event_id, agenda_id, name, date, day, location, change_date, change_date);
                                    agendaModelList.add(agendaModel);
                                    dayList.add(day);
                                    dateList.add(date);
                                    getRundown("2");

                                    //convert date server
                                    SimpleDateFormat fServer = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                    try {
                                        Date dateServer = fServer.parse(date);
                                        SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy", new Locale("id"));
                                        theDay = sdf.format(dateServer);
                                        String today = formatter.format(local);

                                        if (idAgenda.isEmpty()) {
//                                            getRundown(agendaModelList.get(0).getAgenda_id(), theDay);
                                            getRundown("2");
                                            if (theDay.equals(today)) {
                                                tanggal.setText(theDay);
                                                kode = Integer.parseInt(agenda_id);
                                                adapterAgenda = new AgendaAdapter(getActivity(), dayList, kode);
                                                getRundown("2");
                                            }
                                        }
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                }
                                adapterAgenda = new AgendaAdapter(getActivity(), dayList, new OnItemClickListener() {
                                    @Override
                                    public void onItemClick(String id) {
                                        agendaName = agendaModelList.get(Integer.parseInt(id) - 1).getName();
                                        String date = agendaModelList.get(Integer.parseInt(id) - 1).getDate();
                                        SimpleDateFormat fServer = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                        try {
                                            Date dateServer = fServer.parse(date);
                                            SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy", new Locale("id"));
                                            String theDay = sdf.format(dateServer);
                                            tanggal.setText(theDay);
                                        } catch (ParseException e) {
                                            e.printStackTrace();
                                        }
                                        idAgenda = agendaModelList.get(Integer.parseInt(id) - 1).getAgenda_id();;
                                        getRundown("2");
                                    }
                                });

                                LinearLayoutManager MyLayoutManager = new LinearLayoutManager(getContext());
                                MyLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);

                                lv_time.setHasFixedSize(true);
                                lv_time.setAdapter(adapterAgenda);
                                lv_time.setLayoutManager(MyLayoutManager);
                            }else{
//                                popUpLogin();
//                                progressDialogHelper.dismissProgressDialog(ReportActivity.this);
                            }
                        }catch (Exception e){
//                            progressDialogHelper.dismissProgressDialog(ReportActivity.this);
                            System.out.println(e);
                        }

                    }
                    @Override
                    public void onError(ANError error) {
//                        progressDialogHelper.dismissProgressDialog(ReportActivity.this);
                        System.out.println(error);
                    }
                });
    }

    private void getRundown(String id){
        AndroidNetworking.get(session.getServerURL()+"users/eventEMS/"+session.getEventId()+"/agenda/"+id+"/rundown/buscd/"+session.getUserBusinessCode())
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
                        System.out.println(response+"rundownajksdbe");
                        try {
                            if(response.getInt("status")==200){
//                                session.setToken(response.getString("token"));
                                rundownModelList = new ArrayList<RundownModel>();
                                JSONArray jsonArray = response.getJSONArray("data");
                                for (int a = 0; a < jsonArray.length(); a++) {
                                    JSONObject object = jsonArray.getJSONObject(a);
                                    String begin_date = object.getString("begin_date");
                                    String end_date = object.getString("end_date");
                                    String business_code = object.getString("business_code");
                                    String personal_number = object.getString("personal_number");
                                    String event_id = object.getString("event_id");
                                    String agenda_id = object.getString("agenda_id");
                                    String rundown_id = object.getString("rundown_id");
                                    String name = object.getString("name");
                                    String description = object.getString("description");
                                    String rundown_begin = object.getString("rundown_begin");
                                    String rundown_end = object.getString("rundown_end");
                                    String place = object.getString("place");
                                    String layout = object.getString("layout");
                                    String change_date = object.getString("change_date");
                                    String change_user = object.getString("change_user");
                                    rundownModel = new RundownModel(begin_date, end_date, business_code, personal_number, event_id, agenda_id, rundown_id, name, description, rundown_begin, rundown_end, place, layout, change_date, change_user);
                                    rundownModelList.add(rundownModel);
                                }
                                adapterRundown = new RundownAdapter(getActivity().getApplicationContext(), rundownModelList, dataSess, idEvent, idAgenda);
                                lv_rundown.setAdapter(adapterRundown);
//                                lv_rundown.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {
//                                    @Override
//                                    public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
//                                        String item = (String) listInbox.getItemAtPosition(position);
//                                        Toast.makeText(getActivity(),"You selected : " + item, Toast.LENGTH_SHORT).show();
//                                    }
//                                });
                            }else{
//                                popUpLogin();
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

}
