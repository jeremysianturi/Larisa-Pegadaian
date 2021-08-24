package id.co.pegadaian.diarium.controller.home.main_menu.mydevelopment.mytraining;

import androidx.annotation.IdRes;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

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
import id.co.pegadaian.diarium.adapter.AnswerKuisionerAdapter;
import id.co.pegadaian.diarium.model.AnswerQuesionerLMSModel;
import id.co.pegadaian.diarium.model.KuisionerLMSModel;
import id.co.pegadaian.diarium.util.UserSessionManager;
import id.co.pegadaian.diarium.util.element.ProgressDialogHelper;

public class KuisionerLMSActivity extends AppCompatActivity {
    int mSelectedItem = 0;
    Typeface font,fontbold;
    LinearLayout lay_esay, lay_multiple_choice, lay_true_false;
    Button buttonAction;
    int posisi = 0;
    UserSessionManager session;
    JSONArray answer ;
    private List<KuisionerLMSModel> listModel;
    private KuisionerLMSModel model;
    private List<AnswerQuesionerLMSModel> listModelAnswer;
    private AnswerQuesionerLMSModel modelAnswer;
    private AnswerKuisionerAdapter adapterPg;
    ArrayList<String> listJawabanEsay;
    private ProgressDialogHelper progressDialogHelper;
    ListView listMultipleChoice;
    EditText etEsay;
    TextView tvPertanyaanKe, tvText, tvTitle;
    RadioButton radioTrue, radioFalse, radioButton;
    RadioGroup rgTF;
    String jawaban;
    ArrayList<String> mylist = new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kuisioner_lms);
        Intent intent = getIntent();
        String template_code_id = intent.getStringExtra("template_code_id");
        listModel = new ArrayList<KuisionerLMSModel>();
        answer = new JSONArray();
        listJawabanEsay = new ArrayList<String>();
        session = new UserSessionManager(this);
        progressDialogHelper = new ProgressDialogHelper();
        font = Typeface.createFromAsset(KuisionerLMSActivity.this.getAssets(), "fonts/Nexa Light.otf");
        fontbold = Typeface.createFromAsset(KuisionerLMSActivity.this.getAssets(), "fonts/Nexa Bold.otf");
        tvPertanyaanKe = findViewById(R.id.tvpertanyaanKe);
        tvText = findViewById(R.id.tvText);
        tvTitle = findViewById(R.id.tvTitle);
        listMultipleChoice = findViewById(R.id.listMultipleChoice);
        etEsay = findViewById(R.id.etEsay);
        lay_esay = findViewById(R.id.lay_esay);
        lay_multiple_choice = findViewById(R.id.lay_multiple_choice);
        lay_true_false = findViewById(R.id.lay_true_false);
        buttonAction = findViewById(R.id.btnAction);
        radioTrue = findViewById(R.id.radioTrue);
        radioFalse = findViewById(R.id.radioFalse);
        rgTF = findViewById(R.id.radioGroupTF);
        listModelAnswer = new ArrayList<AnswerQuesionerLMSModel>();
        getQuestion(template_code_id);
        lay_multiple_choice.setVisibility(View.GONE);
        lay_true_false.setVisibility(View.GONE);
        lay_esay.setVisibility(View.GONE);
        buttonAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println(posisi+"POSISISAATINISETELAHCLICK"+session.getCountLMS());
                if (posisi+1==session.getCountLMS()) {
                    if (listModel.get(posisi).getQuesioner_type_value().equals("01")) {
                        System.out.println(listModelAnswer.size()+"PANJANGJAWABAN");
                        for (int i=0;i<listModelAnswer.size();i++) {
                            if (listModelAnswer.get(i).isSelected()) {
                                mylist.add(listModelAnswer.get(i).getAnswer_text());
                                System.out.println("Jawaban"+listModelAnswer.get(i).getAnswer_text());
                            }
                        }
                        String joined = TextUtils.join(", ", mylist);
                        submitKuisioner(listModel.get(posisi).getQuesioner_id(),"3",joined,"kelar");
                    } else if (listModel.get(posisi).getQuesioner_type_value().equals("02")) {
                        int selectedId = rgTF.getCheckedRadioButtonId();
                        RadioButton radioSexButton = findViewById(selectedId);
                        String txt = String.valueOf(radioSexButton.getText());
                        submitKuisioner(listModel.get(posisi).getQuesioner_id(),"3",txt,"kelar");
                    } else {
                        String text_answer = etEsay.getText().toString();
                        submitKuisioner(listModel.get(posisi).getQuesioner_id(),"3",text_answer,"kelar");
                    }
                } else {
                    if (listModel.get(posisi).getQuesioner_type_value().equals("01")) {
                        System.out.println(listModelAnswer.size()+"PANJANGJAWABAN");
                        for (int i=0;i<listModelAnswer.size();i++) {
                            if (listModelAnswer.get(i).isSelected()) {
                                mylist.add(listModelAnswer.get(i).getAnswer_text());
                                System.out.println("Jawaban"+listModelAnswer.get(i).getAnswer_text());
                            }
                        }
                        String joined = TextUtils.join(", ", mylist);
                        submitKuisioner(listModel.get(posisi).getQuesioner_id(),"3",joined,"b");
                    } else if (listModel.get(posisi).getQuesioner_type_value().equals("02")) {
                        int selectedId = rgTF.getCheckedRadioButtonId();
                        RadioButton radioSexButton = findViewById(selectedId);
                        String txt = String.valueOf(radioSexButton.getText());
                        submitKuisioner(listModel.get(posisi).getQuesioner_id(),"3",txt,"b");
                    } else {
                        String text_answer = etEsay.getText().toString();
                        submitKuisioner(listModel.get(posisi).getQuesioner_id(),"3",text_answer,"b");
                    }
                    posisi++;
                    tvPertanyaanKe.setText("Test "+(posisi+1)+" of "+session.getCountLMS());
                    tvText.setText(listModel.get(posisi).getQuesioner_text());
                    tvTitle.setText(listModel.get(posisi).getQuesioner_title());
                    getAnswer(listModel.get(posisi).getQuesioner_type_value(), listModel.get(posisi).getQuesioner_id());
                }
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Survey");
    }

    private void getQuestion(String template_code_id){
        progressDialogHelper.showProgressDialog(KuisionerLMSActivity.this, "Getting data...");
        System.out.println("MASUKPERTANYAANLMS®");
        AndroidNetworking.get("https://testapi.digitalevent.id/lms/api/lmsrelationobject?order[oid]=desc&object[]="+template_code_id+"&table_code[]=QUESN&relation[]=Q001&otype[]=TPLCD&per_page=999&begin_date_lte=2019-10-17&end_date_gte=2019-10-17")
                .addHeaders("Accept","application/json")
                .addHeaders("Content-Type","application/json")
                .addHeaders("Authorization","Bearer "+session.getTokenLdap())
                //.addJSONObjectBody(body)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println(response+"RESPONPERTANYAANLMS");
                        try {
                            JSONArray jsonArray = response.getJSONArray("data");
                            String quesioner_id = null,quesioner_text = null, quesioner_title = null, tipe = null;
                            if (jsonArray.length()==0) {
                                popUpLogin();
                            } else {
                                session.setCountLMS(jsonArray.length());
                                for (int a = 0; a < jsonArray.length(); a++) {
                                    JSONObject object = jsonArray.getJSONObject(a);
                                    quesioner_id = object.getJSONObject("id").getString("quesioner_id");
                                    quesioner_text = object.getJSONObject("id").getString("quesioner_text");
                                    quesioner_title = object.getJSONObject("id").getString("quesioner_title");
                                    tipe = object.getJSONObject("id").getJSONObject("quesioner_type").getString("id");
                                    model = new KuisionerLMSModel(quesioner_id,quesioner_text,quesioner_title,tipe);
                                    listModel.add(model);
                                }
                                tvPertanyaanKe.setText("Test "+(posisi+1)+" of "+session.getCountLMS());
                                tvText.setText(listModel.get(posisi).getQuesioner_text());
                                tvTitle.setText(listModel.get(posisi).getQuesioner_title());
                                getAnswer(listModel.get(posisi).getQuesioner_type_value(), listModel.get(posisi).getQuesioner_id());
                            }
                        }catch (Exception e){
                        }
                        progressDialogHelper.dismissProgressDialog(KuisionerLMSActivity.this);
                    }
                    @Override
                    public void onError(ANError error) {
                        progressDialogHelper.dismissProgressDialog(KuisionerLMSActivity.this);
                        System.out.println(error);
                    }
                });
    }

    private void getAnswer(String tipe, String id){
        progressDialogHelper.showProgressDialog(KuisionerLMSActivity.this, "Getting data...");
        AndroidNetworking.get("https://testapi.digitalevent.id/lms/api/quesionerchoice?quesioner[]="+id+"&per_page=999&begin_date_lte=2019-10-07&end_date_gte=2019-10-07")
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
                        System.out.println("RESPONANSWER"+response);
                        try {
                            // tipe "01" = Multiple Choice
                            // tipe "02" = True False
                            // tipe "03" = Esay`
                            if (tipe.equals("01")) {
                                lay_multiple_choice.setVisibility(View.VISIBLE);
                                lay_esay.setVisibility(View.GONE);
                                lay_true_false.setVisibility(View.GONE);
                                JSONArray jsonArray = response.getJSONArray("data");
                                for (int a = 0; a < jsonArray.length(); a++) {
                                    JSONObject object = jsonArray.getJSONObject(a);
                                    String text = object.getString("text_choice");
                                    modelAnswer= new AnswerQuesionerLMSModel("", text, false);
                                    listModelAnswer.add(modelAnswer);
                                }
                                adapterPg = new AnswerKuisionerAdapter(KuisionerLMSActivity.this, listModelAnswer, mSelectedItem);
                                listMultipleChoice.setAdapter(adapterPg);
                            } else if (tipe.equals("02")) {
                                lay_multiple_choice.setVisibility(View.GONE);
                                lay_esay.setVisibility(View.GONE);
                                lay_true_false.setVisibility(View.VISIBLE);
                                JSONArray jsonArray = response.getJSONArray("data");
                                for (int a = 0; a < jsonArray.length(); a++) {
                                    JSONObject object = jsonArray.getJSONObject(a);
                                    String text = object.getString("text_choice");
                                    modelAnswer= new AnswerQuesionerLMSModel("", text, false);
                                    listModelAnswer.add(modelAnswer);
                                }
                                rgTF.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                                    @Override
                                    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                                        RadioButton rb= findViewById(checkedId);
                                        jawaban = rb.getText().toString();
                                    }
                                });
                            } else {
                                lay_multiple_choice.setVisibility(View.GONE);
                                lay_true_false.setVisibility(View.GONE);
                                lay_esay.setVisibility(View.VISIBLE);
                                jawaban = etEsay.getText().toString();
                            }
                            progressDialogHelper.dismissProgressDialog(KuisionerLMSActivity.this);
                        }catch (Exception e){
                            System.out.println(e);
                            progressDialogHelper.dismissProgressDialog(KuisionerLMSActivity.this);
                        }
                    }
                    @Override
                    public void onError(ANError error) {
                        System.out.println(error);
                        progressDialogHelper.dismissProgressDialog(KuisionerLMSActivity.this);
                    }
                });
    }

    private void submitKuisioner(String quesioner, String relation_quesioner, String text_choice, String kondisi) {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat tgl = new SimpleDateFormat("yyyy-MM-dd");
        String tRes = tgl.format(new Date());
        @SuppressLint("SimpleDateFormat") SimpleDateFormat jam = new SimpleDateFormat("HH:mm");
        String jamRes = jam.format(new Date());
        JSONObject jGroup = new JSONObject();// /sub Object
        try {
            jGroup.put("begin_date", tRes);
            jGroup.put("end_date", "9999-12-31");
            jGroup.put("business_code", session.getUserBusinessCode());
            jGroup.put("participant", "2");
            jGroup.put("quesioner", quesioner);
            jGroup.put("relation_quesioner", relation_quesioner);
            jGroup.put("text_choice", text_choice);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        AndroidNetworking.post("https://testapi.digitalevent.id/lms/api/quesionerparticipantchoice")
                .addHeaders("Accept","application/json")
                .addHeaders("Content-Type","application/json")
                .addHeaders("Authorization","Bearer "+session.getTokenLdap())
                .addJSONObjectBody(jGroup)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // do anything with response
                        System.out.println(response+"RESPONPOST : "+text_choice);
                        if (kondisi.equals("kelar")) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(KuisionerLMSActivity.this);
                            builder.setMessage("Terima kasih telah mengisi kuesioner ini !");
                            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    finish();
                                }
                            });
                            AlertDialog alert = builder.create();
                            alert.show();
                        }
                    }
                    @Override
                    public void onError(ANError error) {
                        System.out.println(error);
                    }
                });
    }

    private void popUpLogin() {
        final Dialog dialog = new Dialog(KuisionerLMSActivity.this);
        dialog.setContentView(R.layout.no_kuisioner_question);
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

}
