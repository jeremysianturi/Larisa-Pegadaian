package id.co.pegadaian.diarium.controller.home.main_menu.survey;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
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
import id.co.pegadaian.diarium.adapter.SurveyAdapter;
import id.co.pegadaian.diarium.controller.login.LoginActivity;
import id.co.pegadaian.diarium.model.SurveyModel;
import id.co.pegadaian.diarium.util.UserSessionManager;
import id.co.pegadaian.diarium.util.element.ProgressDialogHelper;

public class SurveyActivity extends AppCompatActivity {

    Typeface font,fontbold;
    UserSessionManager session;
    private ProgressDialogHelper progressDialogHelper;
    private List<SurveyModel> listModel;
    private SurveyModel model;
    private SurveyAdapter adapter;
    ListView listInbox;
    private TextView tvNull;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey);
        session = new UserSessionManager(this);
        progressDialogHelper = new ProgressDialogHelper();

        font = Typeface.createFromAsset(this.getAssets(),"fonts/Nexa Light.otf");
        fontbold = Typeface.createFromAsset(this.getAssets(),"fonts/Nexa Bold.otf");
//        tvNull = (TextView) findViewById(R.id.tvNull);
        listInbox = findViewById(R.id.list_survey);
        tvNull= findViewById(R.id.tvNull);

        getSurvey();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onResume(){
        super.onResume();
        getSurvey();
    }

    private void getSurvey(){
        progressDialogHelper.showProgressDialog(SurveyActivity.this, "Getting data...");
        AndroidNetworking.get(session.getServerURL()+"users/getAllEmployeeSurveyParticipant/nik/"+session.getUserNIK()+"/buscd/"+session.getUserBusinessCode())
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
                        System.out.println(response+"surveyaknekwjnke");
                        try {
                            if(response.getInt("status")==200) {
//                                session.setToken(response.getString("token"));
                                listModel = new ArrayList<SurveyModel>();
                                JSONArray jsonArray = response.getJSONArray("data");
                                System.out.println("panjangnyaadalah " + jsonArray.length());
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
                                    String survey_id = object.getString("survey_id");
                                    String change_date = object.getString("change_date");
                                    String change_user = object.getString("change_user");
                                    String personal_number = object.getString("personal_number");
                                    String survey_name = null, survey_content = null, startdate_survey = null, enddate_survey = null;
                                    JSONArray arraySurvey = object.getJSONArray("survey");
                                    for (int i = 0; i < arraySurvey.length(); i++) {
                                        JSONObject objSurvey = arraySurvey.getJSONObject(i);
                                        survey_name = objSurvey.getString("survey_name");
                                        survey_content = objSurvey.getString("survey_content");
                                        startdate_survey = objSurvey.getString("startdate_survey");
                                        enddate_survey = objSurvey.getString("enddate_survey");
                                        model = new SurveyModel(begin_date, end_date, business_code, survey_id, survey_name, survey_content, startdate_survey, enddate_survey, change_date, change_user, personal_number);
                                        listModel.add(model);
                                    }
                                }
                            }
                                    adapter = new SurveyAdapter(SurveyActivity.this, listModel);
                                    listInbox.setAdapter(adapter);
                                    listInbox.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                            Intent i = new Intent(SurveyActivity.this, DetailSurveyActivity.class);
                                            i.putExtra("survey_id",listModel.get(position).getSurvey_id());
                                            i.putExtra("survey_name",listModel.get(position).getSurvey_name());
                                            i.putExtra("survey_content",listModel.get(position).getSurvey_content());
                                            startActivity(i);
                                        }
                                    });
//                                }
                                progressDialogHelper.dismissProgressDialog(SurveyActivity.this);
                            }else{
                                popUpLogin();
                                progressDialogHelper.dismissProgressDialog(SurveyActivity.this);

                            }
                        }catch (Exception e){
                            progressDialogHelper.dismissProgressDialog(SurveyActivity.this);

                            System.out.println(e);
                        }

                    }
                    @Override
                    public void onError(ANError error) {
                        progressDialogHelper.dismissProgressDialog(SurveyActivity.this);

                        System.out.println(error);
                    }
                });
    }

    private void popUpLogin() {
        final Dialog dialog = new Dialog(SurveyActivity.this);
        dialog.setContentView(R.layout.layout_session_end);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        dialog.getWindow().setAttributes(lp);
        dialog.setTitle("Input Code Here");
        Button btnYes =(Button) dialog.findViewById(R.id.btnYes);
        dialog.show();
        dialog.setCancelable(false);
        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                session.setLoginState(false);
                Intent i = new Intent(SurveyActivity.this, LoginActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
}
