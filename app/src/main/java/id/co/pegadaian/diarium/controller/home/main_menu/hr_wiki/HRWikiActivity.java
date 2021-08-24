package id.co.pegadaian.diarium.controller.home.main_menu.hr_wiki;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import id.co.pegadaian.diarium.R;
import id.co.pegadaian.diarium.adapter.HRWikiAdapter;
import id.co.pegadaian.diarium.model.HRWikiModel;
import id.co.pegadaian.diarium.util.UserSessionManager;
import id.co.pegadaian.diarium.util.element.ProgressDialogHelper;

public class HRWikiActivity extends AppCompatActivity {
    private EditText mEditText;
    UserSessionManager session;
    private ProgressDialogHelper progressDialogHelper;
    private List<HRWikiModel> listModel;
    private HRWikiModel model;
    private HRWikiAdapter adapter;
    ListView listPartner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hrwiki);
        session = new UserSessionManager(this);
        listPartner = findViewById(R.id.listView1);
        mEditText = findViewById(R.id.editText1);
        progressDialogHelper = new ProgressDialogHelper();

        mEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    String param = mEditText.getText().toString();
                    getPartner(param);
                    return true;
                }
                return false;
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("HR Wiki");
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void getPartner(String param){
        progressDialogHelper.showProgressDialog(HRWikiActivity.this, "Searching...");
        AndroidNetworking.get(session.getServerURL()+"incident?business_code="+session.getUserBusinessCode()+"&search="+param)
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
                        System.out.println(response+"seradkewjekj");
                        try {
                            if(response.getInt("status")==200){
                                listModel = new ArrayList<HRWikiModel>();
//                                SearchTempModel contacts = new SearchTempModel();
                                JSONArray jsonArray = response.getJSONArray("data");
                                if (jsonArray.length()==0) {
                                    Toast.makeText(HRWikiActivity.this, "No result for "+param, Toast.LENGTH_SHORT).show();
                                }
                                for (int a = 0; a < jsonArray.length(); a++) {
                                    JSONObject object = jsonArray.getJSONObject(a);
                                    String object_identifier = object.getString("object_identifier");
                                    String begin_date = object.getString("begin_date");
                                    String end_date = object.getString("end_date");;
                                    String business_code = object.getString("business_code");;
                                    String id_incident = object.getString("id_incident");
                                    String kasus = object.getString("kasus");
                                    String solusi = object.getString("solusi");
                                    String like = object.getString("likes");
                                    String dislike = object.getString("dislikes");
                                    String hits = object.getString("hits");
                                    String active_flag = object.getString("active_flag");
                                    String application_id = object.getString("application_id");
                                    String change_date = object.getString("change_date");
                                    String change_user = object.getString("change_user");
                                    model = new HRWikiModel(object_identifier, begin_date, end_date, business_code, id_incident, kasus, solusi, like, dislike, hits, active_flag, application_id, change_date, change_user);
                                    listModel.add(model);
                                }

                                adapter = new HRWikiAdapter(HRWikiActivity.this, listModel);
                                listPartner.setAdapter(adapter);
                                listPartner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                        Intent c = new Intent(HRWikiActivity.this, DetailHRWiki.class);
                                        c.putExtra("kasus",listModel.get(i).getKasus());
                                        c.putExtra("date",listModel.get(i).getChange_date());
                                        c.putExtra("solusi",listModel.get(i).getSolusi());
                                        startActivity(c);
                                    }
                                });
                                progressDialogHelper.dismissProgressDialog(HRWikiActivity.this);
                            }else{
                                progressDialogHelper.dismissProgressDialog(HRWikiActivity.this);
                                Toast.makeText(HRWikiActivity.this, "no result for "+param, Toast.LENGTH_SHORT).show();
                            }
                        }catch (Exception e){
                            progressDialogHelper.dismissProgressDialog(HRWikiActivity.this);
                            System.out.println(e);
                        }
                    }
                    @Override
                    public void onError(ANError error) {
                        progressDialogHelper.dismissProgressDialog(HRWikiActivity.this);
                        System.out.println(error);
                    }
                });
    }
}