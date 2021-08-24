package id.co.pegadaian.diarium.controller.home.main_menu.myevent.ems.feedback;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

import id.co.pegadaian.diarium.R;
import id.co.pegadaian.diarium.controller.home.main_menu.myevent.ems.EventActivity;
import id.co.pegadaian.diarium.controller.home.main_menu.myevent.ems.gallery.GalleryActivity;
import id.co.pegadaian.diarium.controller.home.main_menu.mytime.checkin.CheckinActivity;
import id.co.pegadaian.diarium.util.UserSessionManager;
import id.co.pegadaian.diarium.util.element.ProgressDialogHelper;

public class FeedbackActivity extends AppCompatActivity {
    private TextView textView;
    private Button btnSend;
    private UserSessionManager session;
    EditText etFeedback;
    FrameLayout fmFeedback;
    private ProgressDialogHelper progressDialogHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        session = new UserSessionManager(getApplicationContext());
        progressDialogHelper = new ProgressDialogHelper();

        textView = findViewById(R.id.txt_feedback);
        btnSend = findViewById(R.id.btnSend);
        etFeedback = findViewById(R.id.etFeedback);
        fmFeedback = findViewById(R.id.frame_layout_feedback);

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String feedback = etFeedback.getText().toString();
                if (feedback.matches("")) {
                    Toast.makeText(FeedbackActivity.this, "Please input your feedback", Toast.LENGTH_SHORT).show();
                } else {
                    generateFeedbackId(feedback);
                }
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Feedback");
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void generateFeedbackId(final String feedback) {
        SimpleDateFormat tgl = new SimpleDateFormat("yyyy-MM-dd");
        String tRes = tgl.format(new Date());
        progressDialogHelper.showProgressDialog(FeedbackActivity.this, "Getting data...");
        AndroidNetworking.get(session.getServerURL() + "users/eventEMS/"+session.getEventId()+"/generateIDFeedback")
                .addHeaders("Accept", "application/json")
                .addHeaders("Content-Type", "application/json")
                .addHeaders("Authorization", session.getToken())
                //.addJSONObjectBody(body)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println("djhwebfheActivityId" + response);
                        // do anything with response
                        try {
                            if (response.getInt("status") == 200) {
                                String id = response.getJSONObject("data").getString("id");
                                sendFeedback(id, feedback);

//                                Toast.makeText(FeedbackActivity.this,"Success!",Toast.LENGTH_LONG).show();
                                progressDialogHelper.dismissProgressDialog(FeedbackActivity.this);
                                finish();
                            } else {
//                                popUpLogin();
                            }
                            System.out.println("status ya : " + session.getStat());
                            Snackbar.make(fmFeedback,response.getString("message"), Snackbar.LENGTH_LONG).show();

                            progressDialogHelper.dismissProgressDialog(FeedbackActivity.this);
                        } catch (Exception e) {
//                            progressDialogHelper.dismissProgressDialog(CheckinActivity.this);
                            System.out.println(e);
                            progressDialogHelper.dismissProgressDialog(FeedbackActivity.this);
                        }
                    }

                    @Override
                    public void onError(ANError error) {
//                        progressDialogHelper.dismissProgressDialog(CheckinActivity.this);
                        System.out.println(error);
                        Snackbar.make(fmFeedback,error.getMessage(), Snackbar.LENGTH_LONG).show();
                        progressDialogHelper.dismissProgressDialog(FeedbackActivity.this);
                    }
                });
    }

    private void sendFeedback(String feedback_id, String feedback_isi) {
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
            jGroup.put("personal_number", session.getUserNIK());
            jGroup.put("event_id", session.getEventId());
            jGroup.put("feedback_id", feedback_id);
            jGroup.put("description", feedback_isi);
            jGroup.put("fdtype", "1");
            jGroup.put("file_content", "1");
            jGroup.put("change_user", session.getUserNIK());
            jArray.put(jGroup);
            // /itemDetail Name is JsonArray Name
            jResult.put("activity", jArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        System.out.println(jArray + "FEEDBACKPOSTPARAM");
        AndroidNetworking.post(session.getServerURL()+"users/eventEMS/"+session.getEventId()+"/feedback/"+feedback_id+"/buscd/"+session.getUserBusinessCode())
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
                        System.out.println(response+"RESPONPOSTFEEDBACK");
                        try {
                            if(response.getInt("status")==200){
                                Toast.makeText(FeedbackActivity.this, "Success to send feedback", Toast.LENGTH_SHORT).show();
                                etFeedback.setText("");

                            }else {
                                Toast.makeText(FeedbackActivity.this, "Fail to send feedback", Toast.LENGTH_SHORT).show();

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
}