package id.co.pegadaian.diarium.controller.home.main_menu.survey;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONArray;
import org.json.JSONObject;

import id.co.pegadaian.diarium.R;
import id.co.pegadaian.diarium.util.UserSessionManager;

public class DetailSurveyActivity extends AppCompatActivity {

    Typeface font,fontbold;
    UserSessionManager session;
    TextView jmlquestion, tvContent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_survey);
        session = new UserSessionManager(this);
        Intent intent = getIntent();
        final String survey_id = intent.getStringExtra("survey_id");
        final String survey_name = intent.getStringExtra("survey_name");
        final String survey_content = intent.getStringExtra("survey_content");
        font = Typeface.createFromAsset(DetailSurveyActivity.this.getAssets(),"fonts/Nexa Light.otf");
        fontbold = Typeface.createFromAsset(DetailSurveyActivity.this.getAssets(),"fonts/Nexa Bold.otf");
        TextView nmsurvey = (TextView) findViewById(R.id.tvSurveyName);
        nmsurvey.setTypeface(fontbold);
        nmsurvey.setText("Survey for : "+survey_name);
        jmlquestion = (TextView) findViewById(R.id.tvCountQuestion);
        tvContent = (TextView) findViewById(R.id.tvDate);
        jmlquestion.setTypeface(fontbold);
        tvContent.setText(survey_content);

        Button menu = (Button) findViewById(R.id.button2);
        menu.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent hariian = new Intent(DetailSurveyActivity.this , QuestionSurveyActivity.class);
                hariian.putExtra("survey_id", survey_id);
                hariian.putExtra("survey_name", survey_name);
                hariian.putExtra("survey_content", survey_content);
                DetailSurveyActivity.this.startActivity(hariian);
            }
        });
        getQuestion(survey_id);
        getResult(survey_id);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void getResult(String surveyid){
        AndroidNetworking.get(session.getServerURL()+"users/getAllSurveyResult/nik/"+session.getUserNIK()+"/srvid/"+surveyid+"/buscd/"+session.getUserBusinessCode())
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
                        System.out.println(response+"RESPONRESULT");
                        try {
                            if(response.getInt("status")==200){
                                JSONArray jsonArray = response.getJSONArray("data");
                                if (jsonArray.length()>0) {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(DetailSurveyActivity.this);
                                    builder.setTitle("Alert");
                                    builder.setMessage("You already fill this survey !");
                                    builder.setCancelable(false);
                                    builder.setPositiveButton("Back", new DialogInterface.OnClickListener() {

                                        public void onClick(DialogInterface dialog, int which) {
                                            // Do nothing but close the dialog
                                            finish();
                                        }
                                    });
                                    AlertDialog alert = builder.create();
                                    alert.show();
                                }
                            }else{
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

    private void getQuestion(String surveyid){
        AndroidNetworking.get(session.getServerURL()+"users/getAllSurveyQuestion/srvid/"+surveyid+"/buscd/"+session.getUserBusinessCode())
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
                                JSONArray jsonArray = response.getJSONArray("data");
                                for (int a = 0; a < jsonArray.length(); a++) {
                                    JSONObject object = jsonArray.getJSONObject(a);
                                    String begin_date = object.getString("begin_date");
                                    String end_date = object.getString("end_date");
                                    String business_code = object.getString("business_code");
                                    String survey_id = object.getString("survey_id");
                                    String survey_name = object.getString("survey_name");
                                    String survey_content = object.getString("survey_content");
                                    String startdate_survey = object.getString("startdate_survey");
                                    String enddate_survey = object.getString("change_date");
                                    String change_date = object.getString("change_user");
                                    String change_user = object.getString("change_user");
                                    String personal_number = object.getString("change_user");
                                    JSONArray arrayQuestions = object.getJSONArray("questions");
                                    session.setCountQuestion(arrayQuestions.length());
                                    jmlquestion.setText(arrayQuestions.length()+" Questions");
                                    if (arrayQuestions.length()==0) {
                                        popUpLogin();
                                    }
                                    for (int b=0;b<arrayQuestions.length();b++) {
                                        JSONObject objectQuestion = arrayQuestions.getJSONObject(b);
                                        String question_id = objectQuestion.getString("question_id");
                                        String survey_type = objectQuestion.getString("survey_type");
                                        String list_question = objectQuestion.getString("list_question");
                                        JSONArray arrayChoice = objectQuestion.getJSONArray("answer");
                                        for (int c=0;c<arrayChoice.length();c++) {
                                            JSONObject objectChoice = arrayChoice.getJSONObject(c);
                                            String answer_id = objectChoice.getString("answer_id");
                                            String question_id_ans = objectChoice.getString("question_id");
                                            String answer = objectChoice.getString("answer");

                                        }
                                    }
                                }
                            }else{
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

    private void popUpLogin() {
        final Dialog dialog = new Dialog(DetailSurveyActivity.this);
        dialog.setContentView(R.layout.no_question);
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
                finish();
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
}
