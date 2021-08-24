package id.co.pegadaian.diarium.controller.home.main_menu.mydevelopment.mytraining;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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
import id.co.pegadaian.diarium.adapter.MentoringAdapter;
import id.co.pegadaian.diarium.model.MentoringModel;
import id.co.pegadaian.diarium.util.UserSessionManager;
import id.co.pegadaian.diarium.util.element.ProgressDialogHelper;

public class MentoringActivity extends AppCompatActivity {
    private ProgressDialogHelper progressDialogHelper;
    UserSessionManager session;
    private List<MentoringModel> listModel;
    private MentoringModel model;
    private MentoringAdapter adapter;
    ListView listEventSession;
    TextView tvNull;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mentoring);
        listEventSession = findViewById(R.id.listMentoring);
        session = new UserSessionManager(this);
        progressDialogHelper = new ProgressDialogHelper();
        tvNull = findViewById(R.id.tvNull);
        tvNull.setVisibility(View.GONE);
        getEventSession(session.getBatchLMS());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Mentoring");
    }

    private void getEventSession(String batch){
        System.out.println("GETMENTORING");
        progressDialogHelper.showProgressDialog(MentoringActivity.this, "Getting data...");
        AndroidNetworking.get("https://testapi.digitalevent.id/lms/api/mentoringparticipant?order[BEGDA]=asc&otype[]=MNTOR&include[]=id&include[]=mentoring&id[]=2")
                .addHeaders("Accept","application/json")
                .addHeaders("Content-Type","application/json")
                .addHeaders("Authorization","Bearer "+session.getTokenLdap())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // do anything with response
                        System.out.println("RESPONMENTORING"+response);
                        try {
                            listModel = new ArrayList<MentoringModel>();
                            JSONArray jsonArray = response.getJSONArray("data");
                            if (jsonArray.length()==0) {
                                tvNull.setVisibility(View.VISIBLE);
                                listEventSession.setVisibility(View.GONE);
                            } else {
                                tvNull.setVisibility(View.GONE);
                                listEventSession.setVisibility(View.VISIBLE);
                                for (int a = 0; a < jsonArray.length(); a++) {
                                    JSONObject object = jsonArray.getJSONObject(a);
                                    String mentoring_id = object.getJSONObject("mentoring").getString("mentoring_id");
                                    String mentoring_title = object.getJSONObject("mentoring").getString("title");
                                    String mentoring_topic = object.getJSONObject("mentoring").getString("topic");
                                    String mentoring_description = object.getJSONObject("mentoring").getString("description");
                                    String mentoring_duration = object.getJSONObject("mentoring").getString("duration");
                                    String mentoring_begin_date = object.getJSONObject("mentoring").getString("begin_date");
                                    String mentoring_end_date = object.getJSONObject("mentoring").getString("end_date");
                                    model = new MentoringModel(mentoring_id,mentoring_title,mentoring_topic,mentoring_description,mentoring_duration,mentoring_begin_date,mentoring_end_date);
                                    listModel.add(model);
                                }
                                adapter = new MentoringAdapter(MentoringActivity.this, listModel);
                                listEventSession.setAdapter(adapter);
                                listEventSession.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                                        Intent i = new Intent(MentoringActivity.this, MentorMentoringDetailActivity.class);
                                        i.putExtra("title",listModel.get(position).getTitle());
                                        i.putExtra("topic",listModel.get(position).getTopic());
                                        i.putExtra("description",listModel.get(position).getDescription());
                                        i.putExtra("mentoring_id",listModel.get(position).getMentoring_id());
                                        startActivity(i);
                                    }
                                });
                            }
                            progressDialogHelper.dismissProgressDialog(MentoringActivity.this);
                        }catch (Exception e){
                            progressDialogHelper.dismissProgressDialog(MentoringActivity.this);
                            System.out.println(e);
                        }
                    }
                    @Override
                    public void onError(ANError error) {
                        progressDialogHelper.dismissProgressDialog(MentoringActivity.this);
                        System.out.println(error);
                    }
                });
    }
}
