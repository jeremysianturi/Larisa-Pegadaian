package id.co.pegadaian.diarium.controller.home.main_menu.myevent.ems.rundown;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import id.co.pegadaian.diarium.R;
import id.co.pegadaian.diarium.adapter.NewAgendaAdapter;
import id.co.pegadaian.diarium.model.AgendaModel;
import id.co.pegadaian.diarium.util.UserSessionManager;
import id.co.pegadaian.diarium.util.element.ProgressDialogHelper;

public class NewAgendaActivity extends AppCompatActivity {
    private List<AgendaModel> listModel;
    private AgendaModel model;
    private NewAgendaAdapter adapter;
    ListView listInbox;
    private TextView tvNull;
    UserSessionManager session;
    private ProgressDialogHelper progressDialogHelper;
    Typeface font,fontbold;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_agenda);

        session = new UserSessionManager(this);
        progressDialogHelper = new ProgressDialogHelper();

        font = Typeface.createFromAsset(this.getAssets(),"fonts/Nexa Light.otf");
        fontbold = Typeface.createFromAsset(this.getAssets(),"fonts/Nexa Bold.otf");
        tvNull = (TextView) findViewById(R.id.tvNull);
        listInbox = findViewById(R.id.list_inbox);

        getAgenda();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Rundown Agenda");
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void getAgenda(){
        progressDialogHelper.showProgressDialog(NewAgendaActivity.this, "Getting data...");
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
                        System.out.println(response+"ktjbkgrjbhy");
                        try {
                            if(response.getInt("status")==200){
//                                session.setToken(response.getString("token"));
                                listModel = new ArrayList<AgendaModel>();
                                JSONArray jsonArray = response.getJSONArray("data");
                                if (jsonArray.length()==0) {
                                    listInbox.setVisibility(View.GONE);
                                    tvNull.setVisibility(View.VISIBLE);
                                } else {
                                    listInbox.setVisibility(View.VISIBLE);
                                    tvNull.setVisibility(View.GONE);
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
                                        model = new AgendaModel(begin_date, end_date, business_code, personal_number, event_id, agenda_id, name, date, day, location, change_date, change_date);
                                        listModel.add(model);
                                    }
                                    adapter = new NewAgendaAdapter(NewAgendaActivity.this, listModel);
                                    listInbox.setAdapter(adapter);
                                    listInbox.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                            Intent i = new Intent(NewAgendaActivity.this, NewRundownActivity.class);
                                            i.putExtra("agenda_id", listModel.get(position).getAgenda_id());
                                            i.putExtra("agenda_name", listModel.get(position).getName());
                                            startActivity(i);
                                        }
                                    });
                                }
                                progressDialogHelper.dismissProgressDialog(NewAgendaActivity.this);
                            }else{
                                progressDialogHelper.dismissProgressDialog(NewAgendaActivity.this);

                            }
                        }catch (Exception e){
                            progressDialogHelper.dismissProgressDialog(NewAgendaActivity.this);

                            System.out.println(e);
                        }

                    }
                    @Override
                    public void onError(ANError error) {
                        progressDialogHelper.dismissProgressDialog(NewAgendaActivity.this);

                        System.out.println(error);
                    }
                });
    }
}
