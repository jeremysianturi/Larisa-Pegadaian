package id.co.pegadaian.diarium.controller.home.main_menu.myevent.ems.rundown;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
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
import id.co.pegadaian.diarium.adapter.NewRundownAdapter;
import id.co.pegadaian.diarium.model.RundownModel;
import id.co.pegadaian.diarium.util.UserSessionManager;
import id.co.pegadaian.diarium.util.element.ProgressDialogHelper;

public class NewRundownActivity extends AppCompatActivity {
    Typeface font,fontbold;
    UserSessionManager session;
    private ProgressDialogHelper progressDialogHelper;
    private List<RundownModel> listModel;
    private RundownModel model;
    private NewRundownAdapter adapter;
    ListView listInbox;
    private TextView tvNull;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_rundown);

        Intent intent = getIntent();
        String agenda_id = intent.getStringExtra("agenda_id");
        String agenda_name = intent.getStringExtra("agenda_name");
        session = new UserSessionManager(this);
        progressDialogHelper = new ProgressDialogHelper();

        font = Typeface.createFromAsset(this.getAssets(),"fonts/Nexa Light.otf");
        fontbold = Typeface.createFromAsset(this.getAssets(),"fonts/Nexa Bold.otf");
        tvNull = (TextView) findViewById(R.id.tvNull);
        listInbox = findViewById(R.id.list_inbox);

        getRundown(agenda_id);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(agenda_name);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void getRundown(String agenda_id){
        progressDialogHelper.showProgressDialog(NewRundownActivity.this, "Getting data...");
        AndroidNetworking.get(session.getServerURL()+"users/eventEMS/"+session.getEventId()+"/agenda/"+agenda_id+"/rundown/buscd/"+session.getUserBusinessCode())
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
                        System.out.println(response+"RUNDOWNsdksj");
                        try {
                            if(response.getInt("status")==200){
//                                session.setToken(response.getString("token"));
                                listModel = new ArrayList<RundownModel>();
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
                                        String rundown_id = object.getString("rundown_id");
                                        String name = object.getString("name");
                                        String description = object.getString("description");
                                        String rundown_begin = object.getString("rundown_begin");
                                        String rundown_end = object.getString("rundown_end");
                                        String place = object.getString("place");
                                        String layout = object.getString("layout");
                                        String change_date = object.getString("change_date");
                                        String change_user = object.getString("change_user");
                                        model = new RundownModel(begin_date, end_date, business_code, personal_number, event_id, agenda_id, rundown_id, name, description, rundown_begin, rundown_end, place, layout, change_date, change_user);
                                        listModel.add(model);
                                    }
                                    adapter = new NewRundownAdapter(NewRundownActivity.this, listModel);
                                    listInbox.setAdapter(adapter);
                                }
                                progressDialogHelper.dismissProgressDialog(NewRundownActivity.this);
                            }else{
//                                popUpLogin();
                                progressDialogHelper.dismissProgressDialog(NewRundownActivity.this);

                            }
                        }catch (Exception e){
                            progressDialogHelper.dismissProgressDialog(NewRundownActivity.this);

                            System.out.println(e);
                        }

                    }
                    @Override
                    public void onError(ANError error) {
                        progressDialogHelper.dismissProgressDialog(NewRundownActivity.this);

                        System.out.println(error);
                    }
                });
    }
}
