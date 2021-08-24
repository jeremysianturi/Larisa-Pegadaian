package id.co.pegadaian.diarium.controller.home.main_menu.today_activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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

import id.co.pegadaian.diarium.R;
import id.co.pegadaian.diarium.util.UserSessionManager;
import in.galaxyofandroid.spinerdialog.SpinnerDialog;

public class CommentActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

  TextView option;
  ArrayList<String> opsi = new ArrayList<>();
  SpinnerDialog spinnerDialogs;
  Typeface font,fontbold;
  Dialog myDialog;
  UserSessionManager session;
  TextView tvName, tvJob, tvActivityTitle;
  Spinner spinnerType;
  String full_name,personal_number_aktivitas,activity_id, activity_title, activity_start, activity_finish, activity_type, activity_type_get, approval_status;
  Button btnSubmit;
  ImageView ivProfile;
  String[] type={"To Do","In Progress","Pending","Done","Cancel"};

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_comment);
    Intent intent = getIntent();
    full_name = intent.getStringExtra("full_name");
    personal_number_aktivitas = intent.getStringExtra("personal_number");
    activity_id = intent.getStringExtra("activity_id");
    System.out.println("EXTRA ACTIVITY ID: " + activity_id);
    activity_type_get = intent.getStringExtra("activity_type");
    activity_title = intent.getStringExtra("activity_title");
    activity_start = intent.getStringExtra("activity_start");
    activity_finish = intent.getStringExtra("activity_finish");
    approval_status = intent.getStringExtra("approval_status");
    session = new UserSessionManager(this);
    font = Typeface.createFromAsset(getApplication().getAssets(),"fonts/Nexa Light.otf");
    fontbold = Typeface.createFromAsset(getApplication().getAssets(),"fonts/Nexa Bold.otf");

    ivProfile = (ImageView) findViewById(R.id.ivProfile);
    tvName = (TextView) findViewById(R.id.tvName);
    tvJob = (TextView) findViewById(R.id.tvJob);
    tvActivityTitle = (TextView) findViewById(R.id.tvTitle);
    spinnerType = (Spinner) findViewById(R.id.spinnerType);
    btnSubmit = (Button) findViewById(R.id.btnSubmit);

    tvName.setTypeface(fontbold);
    tvJob.setTypeface(fontbold);

    tvName.setText(session.getUserFullName());
    tvJob.setText(session.getJob());
    if (session.getAvatar().isEmpty()) {
      ivProfile.setImageResource(R.drawable.profile);
    } else{
      Picasso.get().load(session.getAvatar()).error(R.drawable.profile).into(ivProfile);
    }

    tvActivityTitle.setText(activity_title);
    //Getting the instance of Spinner and applying OnItemSelectedListener on it
    spinnerType.setOnItemSelectedListener(this);

    //Creating the ArrayAdapter instance having the bank name list
    ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item,type);
    aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    //Setting the ArrayAdapter data on the Spinner
    spinnerType.setAdapter(aa);
    if (activity_type_get.equals("00")) {
      spinnerType.setSelection(0);
    } else if (activity_type_get.equals("01")) {
      spinnerType.setSelection(1);
    } else if (activity_type_get.equals("02")) {
      spinnerType.setSelection(2);
    } else if (activity_type_get.equals("03")) {
      spinnerType.setSelection(3);
    } else {
      spinnerType.setSelection(4);
    }
    myDialog = new Dialog(this);
//        ImageView menu = (ImageView) findViewById(R.id.add);
//        menu.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View v) {
//                ShowPopup();
//            }
//        });

    btnSubmit.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        submitTodayActivity();
      }
    });


    getSupportActionBar().setDisplayHomeAsUpEnabled(true);


  }

  private void submitTodayActivity() {
    @SuppressLint("SimpleDateFormat") SimpleDateFormat tgl = new SimpleDateFormat("yyyy-MM-dd");
    String tRes = tgl.format(new Date());

    @SuppressLint("SimpleDateFormat") SimpleDateFormat jam = new SimpleDateFormat("HH:mm");
    String jamRes = jam.format(new Date());

    JSONObject jResult = new JSONObject();// main object
    JSONArray jArray = new JSONArray();// /ItemDetail jsonArray
    JSONObject jGroup = new JSONObject();// /sub Object
    try {
      jGroup.put("begin_date", tRes);
      jGroup.put("end_date", "9999-12-31");
      jGroup.put("business_code", session.getUserBusinessCode());
      jGroup.put("personal_number", personal_number_aktivitas);
      jGroup.put("activity_id", activity_id);
      jGroup.put("activity_type", activity_type);
      jGroup.put("activity_title", activity_title);
      jGroup.put("activity_start", activity_start);
      jGroup.put("activity_finish", activity_finish);
      jGroup.put("approval_status", approval_status);
      jGroup.put("change_user", session.getUserNIK());
      jArray.put(jGroup);
      // /itemDetail Name is JsonArray Name
      jResult.put("activity", jArray);
    } catch (JSONException e) {
      e.printStackTrace();
    }
    System.out.println(jArray + "3nkej3n3eAktivitas");
    System.out.println("url change status report activity: "
            + session.getServerURL()+"users/activity/"+activity_id+"/nik/"+personal_number_aktivitas+"/buscd/"+session.getUserBusinessCode());
    AndroidNetworking.post(session.getServerURL()+"users/activity/"+activity_id+"/nik/"+personal_number_aktivitas+"/buscd/"+session.getUserBusinessCode())
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
                System.out.println(response+"edknwkjrAktivitas");
                try {
                  if(response.getInt("status")==200){
                    Toast.makeText(CommentActivity.this, "Success update your activity", Toast.LENGTH_SHORT).show();
//                                Intent a = new Intent(CommentActivity.this, TodayActivity.class);
//                                a.putExtra("key", "asd");
//                                startActivity(a);
                    finish();
                  }else {
                    Toast.makeText(CommentActivity.this,"Connection Problem",Toast.LENGTH_SHORT).show();
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

  @Override
  public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {
    switch (type[position]) {
      case "To Do":
        activity_type = "00";
        break;
      case "In Progress":
        activity_type = "01";
        break;
      case "Pending":
        activity_type = "02";
        break;
      case "Done":
        activity_type = "03";
        break;
      case "Cancel":
        activity_type = "04";
        break;
    }
//        Toast.makeText(getApplicationContext(), type[position]+activity_type, Toast.LENGTH_LONG).show();
  }

  @Override
  public void onNothingSelected(AdapterView<?> arg0) {
    // TODO Auto-generated method stub

  }



  @Override
  public boolean onSupportNavigateUp(){
    finish();
    return true;
  }


  private void initItem() {
    opsi.add("To do");
    opsi.add("In Progress");
    opsi.add("Pending");
    opsi.add("Done");
    opsi.add("Cancel");
  }

  public void ShowPopup() {
    TextView teks;
    Button submit;
    myDialog.setContentView(R.layout.custom_popup_comment);
    teks =(TextView) myDialog.findViewById(R.id.text);
    teks.setTypeface(fontbold);
    submit = (Button) myDialog.findViewById(R.id.submit);
    submit.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        myDialog.dismiss();
      }
    });
    myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    myDialog.show();
  }

}
