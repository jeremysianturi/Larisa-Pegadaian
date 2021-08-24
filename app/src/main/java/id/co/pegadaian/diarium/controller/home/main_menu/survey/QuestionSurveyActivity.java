package id.co.pegadaian.diarium.controller.home.main_menu.survey;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import id.co.pegadaian.diarium.R;
import id.co.pegadaian.diarium.adapter.SurveyAnswerAdapter;
import id.co.pegadaian.diarium.model.AnswerMultipleChoiceModel;
import id.co.pegadaian.diarium.model.JawabanSurveyModel;
import id.co.pegadaian.diarium.model.QuestionSurveyModel;
import id.co.pegadaian.diarium.util.UserSessionManager;
import id.co.pegadaian.diarium.util.element.ProgressDialogHelper;

public class QuestionSurveyActivity extends AppCompatActivity {
    int mSelectedItem = 0;
    int count=0;
    Typeface font,fontbold;
    //NUMBER FOR THE COUNTER
    public int mCount = 100;
    LinearLayout lay_esay, lay_pg;
    Button btnEsay, btnPg;
    int posisi;
    UserSessionManager session;
    JSONArray answer ;
    private List<QuestionSurveyModel> listModel;
    private QuestionSurveyModel model;
    private List<JawabanSurveyModel> listModelJawaban;
    private JawabanSurveyModel modelJawaban;
    private List<AnswerMultipleChoiceModel> listModelAnswer;
    private AnswerMultipleChoiceModel modelAnswer;
    private SurveyAnswerAdapter adapterPg;
    boolean click = false;
    ArrayList<String> listJawabanEsay;
    private ProgressDialogHelper progressDialogHelper;
    ListView lvPg;
    EditText etEsay;
    TextView tvPertanyaanKe, tvQuestion;
    String survey_id, survey_name, survey_content;
    JSONArray dat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_survey);
        Intent intent = getIntent();
        answer = new JSONArray();
        posisi = 0;
        survey_id = intent.getStringExtra("survey_id");
        survey_name = intent.getStringExtra("survey_name");
        survey_content = intent.getStringExtra("survey_content");
        listJawabanEsay = new ArrayList<String>();
        session = new UserSessionManager(this);
        progressDialogHelper = new ProgressDialogHelper();
        font = Typeface.createFromAsset(QuestionSurveyActivity.this.getAssets(), "fonts/Nexa Light.otf");
        fontbold = Typeface.createFromAsset(QuestionSurveyActivity.this.getAssets(), "fonts/Nexa Bold.otf");
        tvPertanyaanKe = (TextView) findViewById(R.id.tvpertanyaanKe);
        tvQuestion = (TextView) findViewById(R.id.question);
        lvPg = (ListView) findViewById(R.id.lvRadio);
        etEsay = (EditText) findViewById(R.id.etEsay);
        lay_esay = (LinearLayout) findViewById(R.id.lay_esay);
        lay_pg = (LinearLayout) findViewById(R.id.lay_pg);
        btnEsay = (Button) findViewById(R.id.btnNext);
//        btnPg = (Button) findViewById(R.id.btnPg);

        listModelJawaban = new ArrayList<JawabanSurveyModel>();

        getQuestion(survey_id);

        btnEsay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((posisi+1)==session.getCountQuestion()) {
                    @SuppressLint("SimpleDateFormat") SimpleDateFormat tgl = new SimpleDateFormat("yyyy-MM-dd");
                    String tRes = tgl.format(new Date());
                    String et_esay = etEsay.getText().toString();
                    if (listModel.get(posisi).getSurvey_type().equals("2")) {
                        if (et_esay.equals("")) {
                            Toast.makeText(QuestionSurveyActivity.this, "Please insert your answer !", Toast.LENGTH_SHORT).show();
                        } else {
                            @SuppressLint("SimpleDateFormat") SimpleDateFormat jamId = new SimpleDateFormat("yyyMMddHHmmss");
                            String jamResId = jamId.format(new Date());

                            modelJawaban = new JawabanSurveyModel(tRes,"9999-12-31",session.getUserBusinessCode(),session.getUserNIK(),survey_id,listModel.get(posisi).getQuestion_id(),jamResId,et_esay,session.getUserNIK());
                            listModelJawaban.add(modelJawaban);
                            etEsay.setText("");
                        }

                        //                    Toast.makeText(QuestionSurveyActivity.this, "Cukup", Toast.LENGTH_SHORT).show();
                        AlertDialog.Builder builder = new AlertDialog.Builder(QuestionSurveyActivity.this);

                        builder.setTitle("Confirm");
                        builder.setMessage("Are you sure to submit this survey ?");

                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int which) {
                                // Do nothing but close the dialog
                                submitSurvey();
                            }
                        });

                        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // Do nothing
                                dialog.dismiss();
                            }
                        });

                        AlertDialog alert = builder.create();
                        alert.show();

                    }else {
                        if (click) {
//                    Toast.makeText(QuestionSurveyActivity.this, "Cukup", Toast.LENGTH_SHORT).show();
                            AlertDialog.Builder builder = new AlertDialog.Builder(QuestionSurveyActivity.this);

                            builder.setTitle("Confirm");
                            builder.setMessage("Are you sure to submit this survey ?");

                            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                                public void onClick(DialogInterface dialog, int which) {
                                    // Do nothing but close the dialog
                                    submitSurvey();
                                }
                            });

                            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // Do nothing
                                    dialog.dismiss();
                                }
                            });

                            AlertDialog alert = builder.create();
                            alert.show();
                        } else {
                            Toast.makeText(QuestionSurveyActivity.this, "Select the answer", Toast.LENGTH_SHORT).show();
                        }
                    }

                } else {
                    @SuppressLint("SimpleDateFormat") SimpleDateFormat tgl = new SimpleDateFormat("yyyy-MM-dd");
                    String tRes = tgl.format(new Date());
                    if (listModel.get(posisi).getSurvey_type().equals("2")) {
                        String et_esay = etEsay.getText().toString();
                        if (et_esay.equals("")) {
                            Toast.makeText(QuestionSurveyActivity.this, "Please insert your answer !", Toast.LENGTH_SHORT).show();
                        } else {
                            @SuppressLint("SimpleDateFormat") SimpleDateFormat jamId = new SimpleDateFormat("yyyMMddHHmmss");
                            String jamResId = jamId.format(new Date());
                            modelJawaban = new JawabanSurveyModel(tRes,"9999-12-31",session.getUserBusinessCode(),"",survey_id,listModel.get(posisi).getQuestion_id(),jamResId,et_esay,session.getUserNIK());
                            listModelJawaban.add(modelJawaban);
                            etEsay.setText("");
                            posisi++;
                            getQuestion(survey_id);
                        }
                    } else {
                        if (click) {
                            posisi++;
                            getQuestion(survey_id);
                        } else {
                            Toast.makeText(QuestionSurveyActivity.this, "Select the answer", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Survey for "+survey_name);
    }

    private void getQuestion(final String surveyid){
//        Toast.makeText(this, "posisi : "+posisi, Toast.LENGTH_SHORT).show();
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
                            listModel = new ArrayList<QuestionSurveyModel>();
                            listModelAnswer = new ArrayList<AnswerMultipleChoiceModel>();
                            if(response.getInt("status")==200){
                                dat =  response.getJSONArray("data");
                                JSONArray jsonArray = response.getJSONArray("data");
                                String question_id = null;
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
                                    for (int b=0;b<arrayQuestions.length();b++) {
                                        JSONObject objectQuestion = arrayQuestions.getJSONObject(posisi);
                                        question_id = objectQuestion.getString("question_id");
                                        String survey_type = objectQuestion.getString("survey_type");
                                        String list_question = objectQuestion.getString("list_question");
                                        model = new QuestionSurveyModel(begin_date, end_date, business_code, survey_id, survey_name, survey_content, startdate_survey, enddate_survey, change_date, change_user, personal_number, question_id, survey_type, list_question, String.valueOf(posisi+1));
                                        listModel.add(model);
                                    }
                                }
                                click =false;
                                getAnswer(question_id, surveyid);
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

    private void getAnswer(String question_id, String surveyid){

//        Toast.makeText(this, "posisi : "+posisi, Toast.LENGTH_SHORT).show();
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
//                            listModel = new ArrayList<QuestionSurveyModel>();

                            if(response.getInt("status")==200){
                                dat =  response.getJSONArray("data");
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
                                    for (int b=0;b<arrayQuestions.length();b++) {
                                        JSONObject objectQuestion = arrayQuestions.getJSONObject(posisi);
                                        String question_id = objectQuestion.getString("question_id");
                                        String survey_type = objectQuestion.getString("survey_type");
                                        String list_question = objectQuestion.getString("list_question");
                                        listModelAnswer = new ArrayList<AnswerMultipleChoiceModel>();
                                        JSONArray arrayChoice = objectQuestion.getJSONArray("answer");
                                        for (int c=0;c<arrayChoice.length();c++) {
                                            JSONObject objectChoice = arrayChoice.getJSONObject(c);
                                            String answer_id = objectChoice.getString("answer_id");
                                            String question_id_ans = objectChoice.getString("question_id");
                                            String answer = objectChoice.getString("answer");
                                            if (listModel.get(posisi).getQuestion_id().equals(question_id)) {
                                                modelAnswer= new AnswerMultipleChoiceModel(answer_id, question_id_ans, answer);
                                                listModelAnswer.add(modelAnswer);
                                            }
                                        }
                                    }
                                }

                            }else{
                            }
                            adapterPg = new SurveyAnswerAdapter(QuestionSurveyActivity.this, listModelAnswer, mSelectedItem);
                            if(listModel.size()>0){
                                if (listModel.get(posisi).getSurvey_type().equals("2")) {
                                    lay_esay.setVisibility(View.VISIBLE);
                                    lay_pg.setVisibility(View.GONE);
                                } else {
                                    lay_esay.setVisibility(View.GONE);
                                    lay_pg.setVisibility(View.VISIBLE);
                                    lvPg.setAdapter(adapterPg);
                                    lvPg.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                            @SuppressLint("SimpleDateFormat") SimpleDateFormat tgl = new SimpleDateFormat("yyyy-MM-dd");
                                            String tRes = tgl.format(new Date());
                                            String answerId = listModelAnswer.get(position).getAnswer_id();
                                            String answer = listModelAnswer.get(position).getAnswer();
                                            modelJawaban = new JawabanSurveyModel(tRes,"9999-12-31",session.getUserBusinessCode(),session.getUserNIK(),survey_id,listModel.get(posisi).getQuestion_id(),answerId,answer,session.getUserNIK());
                                            listModelJawaban.add(modelJawaban);
                                            mSelectedItem = position;
                                            adapterPg.notifyDataSetChanged();
                                            click = true;
                                        }
                                    });
                                }
                                tvPertanyaanKe.setText("Pertanyaan ke-"+(posisi+1)+" / "+session.getCountQuestion());
                                tvQuestion.setText(listModel.get(posisi).getList_question());
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

    private void submitSurvey() {
        progressDialogHelper.showProgressDialog(QuestionSurveyActivity.this, "Submit data...");

        @SuppressLint("SimpleDateFormat") SimpleDateFormat tgl = new SimpleDateFormat("yyyy-MM-dd");
        String tRes = tgl.format(new Date());

        @SuppressLint("SimpleDateFormat") SimpleDateFormat jam = new SimpleDateFormat("HH:mm");
        String jamRes = jam.format(new Date());

//        JSONObject jResult = new JSONObject();// main object
        JSONArray jArray = new JSONArray();// /ItemDetail jsonArray
        System.out.println(listModelJawaban.size()+"neje4nk4jrn");
        for (int i = 0; i < listModelJawaban.size(); i++) {
            JSONObject jGroup = new JSONObject();// /sub Object
            try {
                jGroup.put("begin_date", tRes);
                jGroup.put("end_date", "9999-12-31");
                jGroup.put("business_code", session.getUserBusinessCode());
                jGroup.put("personal_number", session.getUserNIK());
                jGroup.put("survey_id", survey_id);
                jGroup.put("question_id", listModelJawaban.get(i).getQuestion_id());
                jGroup.put("answer_id", listModelJawaban.get(i).getAnswer_id());
                jGroup.put("answer", listModelJawaban.get(i).getAnswer());
                jGroup.put("change_user", session.getUserNIK());
                jGroup.put("submit_status", "1");
                jArray.put(jGroup);
                // /itemDetail Name is JsonArray Name
//                jResult.put("activity", jArray);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        System.out.println(jArray + "ARRAYSURVEYNYA");
        AndroidNetworking.post(session.getServerURL()+"users/employeeContentSurveyResult/nik/"+session.getUserNIK()+"/srvid/"+survey_id+"/buscd/"+session.getUserBusinessCode())
                .addHeaders("Accept","application/json")
                .addHeaders("Content-Type","application/json")
                .addHeaders("Authorization",session.getToken())
                .addJSONArrayBody(jArray)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // do anything with response
                        System.out.println(response+"RESPONSUBMITSURVEY");
                        try {
                            if(response.getInt("status")==200){
                                Toast.makeText(QuestionSurveyActivity.this, "Success send this survey !", Toast.LENGTH_SHORT).show();
                                Intent a = new Intent(QuestionSurveyActivity.this, SurveyActivity.class);
                                startActivity(a);
                                finish();
                            } else {
                                Toast.makeText(QuestionSurveyActivity.this, "Error send survey", Toast.LENGTH_SHORT).show();
                            }
                            progressDialogHelper.dismissProgressDialog(QuestionSurveyActivity.this);
                        }catch (Exception e){
                            System.out.println(e);
                            progressDialogHelper.dismissProgressDialog(QuestionSurveyActivity.this);
                        }
                    }
                    @Override
                    public void onError(ANError error) {
                        System.out.println(error);
                        progressDialogHelper.dismissProgressDialog(QuestionSurveyActivity.this);
                    }
                });
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

}

