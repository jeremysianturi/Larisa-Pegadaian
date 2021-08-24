package id.co.pegadaian.diarium.controller.home.main_menu.mydevelopment.mytraining;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
import id.co.pegadaian.diarium.adapter.ForumCommentAdapter;
import id.co.pegadaian.diarium.model.ForumCommentModel;
import id.co.pegadaian.diarium.util.TimeHelper;
import id.co.pegadaian.diarium.util.UserSessionManager;
import id.co.pegadaian.diarium.util.element.ProgressDialogHelper;

public class ForumDetailActivity extends AppCompatActivity {
    UserSessionManager session;
    private ProgressDialogHelper progressDialogHelper;
    private List<ForumCommentModel> listModel;
    private ForumCommentModel model;
    private ForumCommentAdapter adapter;
    ListView lvComment;
    TextView tvCountLike, tvPost;
    EditText etComment;
    LinearLayout lvLike;
    ImageView ivLike, ivProfile;
    TimeHelper timeHelper;
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forum_detail);

        session = new UserSessionManager(this);
        progressDialogHelper = new ProgressDialogHelper();
        timeHelper = new TimeHelper();
        Intent intent = getIntent();
        String batch_name = intent.getStringExtra("batch_name");
        String change_date = intent.getStringExtra("change_date");
        String forum_image = intent.getStringExtra("forum_image");
        String forum_title = intent.getStringExtra("forum_title");
        String owner = intent.getStringExtra("owner");

        ivLike = findViewById(R.id.ivLike);
        ivProfile = findViewById(R.id.ivProfile);
        lvLike = findViewById(R.id.lvLike);
        etComment = findViewById(R.id.etComment);
        tvPost = findViewById(R.id.tvPost);
        lvComment = findViewById(R.id.lvComment);
        tvCountLike = findViewById(R.id.tvCountLike);
        lvComment.setDivider(null);

        TextView txtTitle = findViewById(R.id.tvName);
        txtTitle.setText(owner);
        TextView txtTitle_dua =  findViewById(R.id.tvTitle);
        txtTitle_dua.setText(forum_title);
        TextView txtTitle_tiga = findViewById(R.id.tvDate);
        txtTitle_tiga.setText(change_date);
        TextView txtTitle_empat = findViewById(R.id.tvDesc);
        txtTitle_empat.setText(forum_title);

        etComment.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.setFocusable(true);
                v.setFocusableInTouchMode(true);
                return false;
            }
        });
        tvPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String comment = etComment.getText().toString();
                if (comment.isEmpty() || comment.equals("")||comment.length()==0) {
                    Toast.makeText(ForumDetailActivity.this, "Plase input comment first !", Toast.LENGTH_SHORT).show();
                } else {
                    submitComment("1", comment);
                }
            }
        });

        getListComment();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Detail Forum");
    }

    private void submitComment(String forum, String comment) {
        SimpleDateFormat tgl = new SimpleDateFormat("yyyy-MM-dd");
        String tRes = tgl.format(new Date());
        SimpleDateFormat jam = new SimpleDateFormat("HH:mm");
        String jamRes = jam.format(new Date());
        SimpleDateFormat commentId = new SimpleDateFormat("yyyyMMddHHmmss");
        String commentIdRes = commentId.format(new Date());

        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        try {
            jsonObject.put("begin_date", tRes);
            jsonObject.put("begin_time", jamRes);
            jsonObject.put("business_code", session.getUserBusinessCode());
            jsonObject.put("comment", comment);
            jsonObject.put("forum", forum);
            jsonObject.put("otype_parent", "SCHDL");
            jsonObject.put("owner", session.getUserNIK());
            jsonArray.put(jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        System.out.println(jsonArray+"PARAMKOMEN");
        AndroidNetworking.post("https://testapi.digitalevent.id/lms/api/forumcomment")
                .addHeaders("Accept","application/json")
                .addHeaders("Content-Type","application/json")
                .addHeaders("Authorization","Bearer "+session.getTokenLdap())
                .addJSONArrayBody(jsonArray)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println(response+"RESPONKOMEN");
                        // do anything with response
                        try {
                            getListComment();
                            adapter.notifyDataSetChanged();
                            etComment.setText("");
                        }catch (Exception e){
                            System.out.println("aaa"+e);
                        }
                    }
                    @Override
                    public void onError(ANError error) {
                        System.out.println("bbb"+error);
                    }
                });
    }

    private void getListComment(){
        AndroidNetworking.get("https://testapi.digitalevent.id/lms/api/forumcomment?begin_date_lte=2019-10-10&end_date_gte=2019-10-10&forum[]=1")
                .addHeaders("Accept","application/json")
                .addHeaders("Content-Type","application/json")
                .addHeaders("Authorization","Bearer "+session.getTokenLdap())
                //.addJSONObjectBody(body)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println("COMMENTRESPONSE"+response);
                        // do anything with response
                        try {
                            listModel = new ArrayList<ForumCommentModel>();
                            JSONArray jsonArray = response.getJSONArray("data");
                            for (int a = 0; a < jsonArray.length(); a++) {
                                JSONObject object = jsonArray.getJSONObject(a);
                                String owner = object.getString("owner");
                                String begin_date = object.getString("begin_date");
                                String time = object.getString("begin_time");
                                String comment = object.getString("comment");
                                model = new ForumCommentModel(owner,begin_date,time,comment);
                                listModel.add(model);
                            }
                            adapter = new ForumCommentAdapter(ForumDetailActivity.this, listModel);
                            lvComment.setAdapter(adapter);
                            progressDialogHelper.dismissProgressDialog(ForumDetailActivity.this);

                        }catch (Exception e){
                            progressDialogHelper.dismissProgressDialog(ForumDetailActivity.this);
                            System.out.println(e);
                        }
                    }
                    @Override
                    public void onError(ANError error) {
                        progressDialogHelper.dismissProgressDialog(ForumDetailActivity.this);

                        System.out.println(error);
                    }
                });
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
}
