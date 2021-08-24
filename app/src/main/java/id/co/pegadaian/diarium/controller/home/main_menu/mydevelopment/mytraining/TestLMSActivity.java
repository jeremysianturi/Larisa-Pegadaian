package id.co.pegadaian.diarium.controller.home.main_menu.mydevelopment.mytraining;

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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.IdRes;
import androidx.appcompat.app.AppCompatActivity;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import id.co.pegadaian.diarium.R;
import id.co.pegadaian.diarium.adapter.AnswerTestLMSAdapter;
import id.co.pegadaian.diarium.model.AnswerTestModel;
import id.co.pegadaian.diarium.model.TestLMSModel;
import id.co.pegadaian.diarium.util.UserSessionManager;
import id.co.pegadaian.diarium.util.element.ProgressDialogHelper;

public class TestLMSActivity extends AppCompatActivity {

  int mSelectedItem = 0;
  Typeface font,fontbold;
  LinearLayout lay_esay, lay_multiple_choice, lay_true_false;
  Button buttonAction;
  int posisi = 0;
  UserSessionManager session;
  JSONArray answer ;
  private List<TestLMSModel> listModel;
  private TestLMSModel model;
  private List<AnswerTestModel> listModelAnswer;
  private AnswerTestModel modelAnswer;
  private AnswerTestLMSAdapter adapterPg;
  ArrayList<String> listJawabanEsay;
  private ProgressDialogHelper progressDialogHelper;
  ListView listMultipleChoice;
  EditText etEsay;
  TextView tvPertanyaanKe, tvText, tvTitle;
  RadioButton radioTrue, radioFalse, radioButton;
  RadioGroup rgTF;
  String jawaban,id_code;
  ImageView ivTestLMS;
  ArrayList<String> mylist = new ArrayList<String>();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_test_lms);

    Intent intent = getIntent();
   id_code = intent.getStringExtra("test_code_id");
    System.out.println("IDCODETESTLMS"+"  "+ id_code);
    listModel = new ArrayList<TestLMSModel>();


    ivTestLMS = findViewById(R.id.ivTestImage);
    answer = new JSONArray();
    listJawabanEsay = new ArrayList<String>();
    session = new UserSessionManager(this);
    progressDialogHelper = new ProgressDialogHelper();
    font = Typeface.createFromAsset(TestLMSActivity.this.getAssets(), "fonts/Nexa Light.otf");
    fontbold = Typeface.createFromAsset(TestLMSActivity.this.getAssets(), "fonts/Nexa Bold.otf");
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
    getQuestion(id_code);
//        getAnswer();
    lay_multiple_choice.setVisibility(View.GONE);
    lay_true_false.setVisibility(View.GONE);
    lay_esay.setVisibility(View.GONE);
//    lay_multiple_answer.setVisibility(View.GONE);
    buttonAction.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        System.out.println(posisi+"POSISISAATINISETELAHCLICK"+session.getCountTest());
        System.out.println("TIPEQUESTION"+""+listModel.get(posisi).getQuesioner_type_value());
        if (posisi+1==session.getCountTest()) {
          if (listModel.get(posisi).getQuesioner_type_value().equals("01")) {
            System.out.println(listModelAnswer.size()+"PANJANGJAWABAN");
            for (int i=0;i<listModelAnswer.size();i++) {
              if (listModelAnswer.get(i).isSelected()) {
                mylist.add(listModelAnswer.get(i).getAnswer_text());
                System.out.println("Jawaban"+listModelAnswer.get(i).getAnswer_text());
              }
            }
            String joined = TextUtils.join(", ", mylist);
            submitKuisioner(listModel.get(posisi).getQuesioner_id(),"3",joined,"kelar",listModelAnswer.get(posisi).getAnswer_squance());

//            Toast.makeText(TestLMSActivity.this,"BERHASIL HORE ",Toast.LENGTH_SHORT).show();
          } else if (listModel.get(posisi).getQuesioner_type_value().equals("02")) {
            int selectedId = rgTF.getCheckedRadioButtonId();
            RadioButton radioSexButton = findViewById(selectedId);
            String txt = String.valueOf(radioSexButton.getText());
//            Toast.makeText(TestLMSActivity.this,"BERHASIL HORE ",Toast.LENGTH_SHORT).show();
            submitKuisioner(listModel.get(posisi).getQuesioner_id(),"3",txt,"kelar",listModelAnswer.get(posisi).getAnswer_squance());
          } else {
            String text_answer = etEsay.getText().toString();
//            Toast.makeText(TestLMSActivity.this,"BERHASIL HORE ",Toast.LENGTH_SHORT).show();
            submitKuisioner(listModel.get(posisi).getQuesioner_id(),"3",text_answer,"kelar",listModelAnswer.get(posisi).getAnswer_squance());
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
            submitKuisioner(listModel.get(posisi).getQuesioner_id(),"3",joined,"b",listModelAnswer.get(posisi).getAnswer_squance());
//            Toast.makeText(TestLMSActivity.this,"BERHASIL HORE ",Toast.LENGTH_SHORT).show();

          } else if (listModel.get(posisi).getQuesioner_type_value().equals("02")) {
            int selectedId = rgTF.getCheckedRadioButtonId();
            RadioButton radioSexButton = findViewById(selectedId);
            String txt = String.valueOf(radioSexButton.getText());
//            Toast.makeText(TestLMSActivity.this,"BERHASIL HORE ",Toast.LENGTH_SHORT).show();
            submitKuisioner(listModel.get(posisi).getQuesioner_id(),"3",txt,"b",listModelAnswer.get(posisi).getAnswer_squance());
          } else {
            String text_answer = etEsay.getText().toString();
            submitKuisioner(listModel.get(posisi).getQuesioner_id(),"3",text_answer,"b",listModelAnswer.get(posisi).getAnswer_squance());
//            Toast.makeText(TestLMSActivity.this,"BERHASIL HORE ",Toast.LENGTH_SHORT).show();

          }
          posisi++;
          tvPertanyaanKe.setText("Test "+(posisi+1)+" of "+session.getCountTest());
          tvText.setText(listModel.get(posisi).getQuesioner_text());
          System.out.println("CEKDATATEST"+listModel.get(posisi).getQuesioner_text());
          Picasso.get().load(listModel.get(posisi).getGetQuesioner_image()).error(R.drawable.profile).into(ivTestLMS);
//          tvTitle.setText(listModel.get(posisi).getQuesioner_title());
          getAnswer(listModel.get(posisi).getQuesioner_type_value());

        }


      }
    });
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    getSupportActionBar().setTitle("Test");
  }

//  private void submitKuisioner(String quesioner_id, String s, String text_answer, String b) {
//    Toast.makeText(TestLMSActivity.this,"BEHASIL SUBMIT NJING ",Toast.LENGTH_SHORT).show();
//  }

  private void getQuestion(String id_code) {
    System.out.println("MASUKKEQUESTION TEST");
    progressDialogHelper.showProgressDialog(TestLMSActivity.this,"Getting data....");
    AndroidNetworking.get("https://testapi.digitalevent.id/lms/api/lmsrelationobject?table_code[]=TSQST&relation[]=T001&otype[]=TSTCD&object[]="+id_code+"&per_page=999&begin_date_lte=2019-10-21&end_date_gte=2019-10-22")
            .addHeaders("Accept","application/json")
            .addHeaders("Content-Type","application/json")
            .addHeaders("Authorization","Bearer "+session.getTokenLdap())
            .setPriority(Priority.MEDIUM)
            .build()
            .getAsJSONObject(new JSONObjectRequestListener() {
              @Override
              public void onResponse(JSONObject response) {
                System.out.println("RESPONTESTLMS" + response);
                try{
                  JSONArray jsonArray = response.getJSONArray("data");
                  String quesioner_id = null,quesioner_text = null, quesioner_title = null, tipe = null, question_image = null;
                  if (jsonArray.length()==0) {
                    popUpLogin();
                  } else {
                    session.setCountTest(jsonArray.length());
                    for (int a = 0; a < jsonArray.length(); a++) {
                      JSONObject object = jsonArray.getJSONObject(a);
                      quesioner_id = object.getJSONObject("id").getString("question_id");
                      quesioner_text = object.getJSONObject("id").getString("question_text");
//                    quesioner_title = jsonObject.getJSONObject("id").getString("quesioner_title");
                      question_image = object.getJSONObject("id").getString("question_image");
//                    question_type = jsonObject.getJSONObject("id").getJSONObject("question_type").getString("id");
                      tipe = object.getJSONObject("id").getJSONObject("question_type").getString("id");
                      model = new TestLMSModel(quesioner_id,quesioner_text,quesioner_title,tipe,question_image);
                      listModel.add(model);
                    }
                    System.out.println("IMAGETEST"+"  "+listModel.get(posisi).getGetQuesioner_image());
                    tvPertanyaanKe.setText("Test "+(posisi+1)+" of "+session.getCountTest());
                    tvText.setText(listModel.get(posisi).getQuesioner_text());
                    Picasso.get().load(listModel.get(posisi).getGetQuesioner_image()).error(R.drawable.profile).into(ivTestLMS);
//                    tvTitle.setText(listModel.get(posisi).getQuesioner_text());
                    getAnswer(listModel.get(posisi).getQuesioner_type_value());
                    System.out.println("TIPEQUESTIONTESTLMS"+" "+listModel.get(posisi).getQuesioner_type_value());
                  }
                }catch (Exception e){
                }
                progressDialogHelper.dismissProgressDialog(TestLMSActivity.this);
              }
              @Override
              public void onError(ANError error) {
                progressDialogHelper.dismissProgressDialog(TestLMSActivity.this);
                System.out.println(error);
              }
            });

  }

  private void getAnswer(String tipe) {
    progressDialogHelper.showProgressDialog(TestLMSActivity.this,"Getting data....");
    AndroidNetworking.get("https://testapi.digitalevent.id/lms/api/testquestionchoice?question[]=1&per_page=999&begin_date_lte=2019-10-23&end_date_gte=2019-10-23")
            .addHeaders("Accept","application/json")
            .addHeaders("Content-Type","application/json")
            .addHeaders("Authorization","Bearer "+session.getTokenLdap())
            //.addJSONObjectBody(body)
            .setPriority(Priority.MEDIUM)
            .build()
            .getAsJSONObject(new JSONObjectRequestListener() {
              @Override
              public void onResponse(JSONObject response) {
                System.out.println("RESPONANSWER"+response);
                try {
                  System.out.println("tipeQuestion"+tipe);
                  // tipe "01", "04" = Multiple Choice
                  // tipe "02" = True False
                  // tipe "03" = Esay`
                  if (tipe.equals("01")) {
                    listModelAnswer = new ArrayList<AnswerTestModel>();
                    lay_multiple_choice.setVisibility(View.VISIBLE);
                    lay_esay.setVisibility(View.GONE);
                    lay_true_false.setVisibility(View.GONE);
                    JSONArray jsonArray = response.getJSONArray("data");
                    for (int a = 0; a < jsonArray.length(); a++) {
                      JSONObject object = jsonArray.getJSONObject(a);
                      String text = object.getString("text_choice");
                      String squance= object.getString("sequence_no");
                      System.out.println("chioce"+text);
                      modelAnswer= new AnswerTestModel("", text, false,squance);
                      listModelAnswer.add(modelAnswer);
                    }
                    adapterPg = new AnswerTestLMSAdapter(TestLMSActivity.this, listModelAnswer, mSelectedItem);
                    listMultipleChoice.setAdapter(adapterPg);
                  } else if (tipe.equals("02")) {
                    lay_multiple_choice.setVisibility(View.GONE);
                    lay_esay.setVisibility(View.GONE);
                    lay_true_false.setVisibility(View.VISIBLE);
                    JSONArray jsonArray = response.getJSONArray("data");
                    for (int a = 0; a < jsonArray.length(); a++) {
                      JSONObject object = jsonArray.getJSONObject(a);
                      String text = object.getString("text_choice");
                      String squance= object.getString("sequence_no");
                      modelAnswer= new AnswerTestModel("", text, false,squance);
                      listModelAnswer.add(modelAnswer);
                    }
                    rgTF.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                      @Override
                      public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                        RadioButton rb= findViewById(checkedId);
                        jawaban = rb.getText().toString();
                      }
                    });
                  }else if (tipe.equals("04")) {
                    listModelAnswer = new ArrayList<AnswerTestModel>();
                    lay_multiple_choice.setVisibility(View.VISIBLE);
                    lay_esay.setVisibility(View.GONE);
                    lay_true_false.setVisibility(View.GONE);
                    JSONArray jsonArray = response.getJSONArray("data");
                    for (int a = 0; a < jsonArray.length(); a++) {
                      JSONObject object = jsonArray.getJSONObject(a);
                      String text = object.getString("text_choice");
                      String squance= object.getString("sequence_no");
                      System.out.println("chioce"+text);
                      modelAnswer= new AnswerTestModel("", text, false,squance);
                      listModelAnswer.add(modelAnswer);
                    }
                    adapterPg = new AnswerTestLMSAdapter(TestLMSActivity.this, listModelAnswer, mSelectedItem);
                    listMultipleChoice.setAdapter(adapterPg);
                  }
                  else {
                    lay_multiple_choice.setVisibility(View.GONE);
                    lay_true_false.setVisibility(View.GONE);
                    lay_esay.setVisibility(View.VISIBLE);
                    jawaban = etEsay.getText().toString();
                  }
                  progressDialogHelper.dismissProgressDialog(TestLMSActivity.this);
                }catch (Exception e){
                  System.out.println(e);
                  progressDialogHelper.dismissProgressDialog(TestLMSActivity.this);
                }
              }
              @Override
              public void onError(ANError error) {
                System.out.println(error);
                progressDialogHelper.dismissProgressDialog(TestLMSActivity.this);
              }
            });
  }
  private void popUpLogin() {
    final Dialog dialog = new Dialog(TestLMSActivity.this);
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
  private void submitKuisioner(String quesioner, String relation_quesioner, String text_choice, String kondisi, String sequence_no) {
    @SuppressLint("SimpleDateFormat") SimpleDateFormat tgl = new SimpleDateFormat("yyyy-MM-dd");
    String tRes = tgl.format(new Date());
    @SuppressLint("SimpleDateFormat") SimpleDateFormat jam = new SimpleDateFormat("HH:mm");
    String jamRes = jam.format(new Date());
    JSONObject jGroup = new JSONObject();// /sub Object
    System.out.println("MASUKSUBMIT TEST GAN");
    System.out.println("MASUKSUBMIT"+"  "+session.getUserBusinessCode()+quesioner+text_choice+kondisi);
    try {
      jGroup.put("begin_date", tRes);
      jGroup.put("end_date", "9999-12-31");
      jGroup.put("business_code", session.getUserBusinessCode());
      jGroup.put("participant", "2");
      jGroup.put("question", quesioner);
      jGroup.put("relation_question", relation_quesioner);
      jGroup.put("text_choice", text_choice);
      jGroup.put("sequence_no", sequence_no);
    } catch (JSONException e) {
      e.printStackTrace();
    }
    System.out.println("ISIBODYSUBMIT"+jGroup);
    AndroidNetworking.post("https://testapi.digitalevent.id/lms/api/testquestionparticipantchoice")
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
                  AlertDialog.Builder builder = new AlertDialog.Builder(TestLMSActivity.this);
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
                System.out.println("ERRORSUBMIT"+error.getResponse());
              }
            });
  }

  @Override
  public boolean onSupportNavigateUp(){
    finish();
    return true;
  }

}
