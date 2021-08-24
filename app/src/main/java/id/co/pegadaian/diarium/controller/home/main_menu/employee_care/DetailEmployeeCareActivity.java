package id.co.pegadaian.diarium.controller.home.main_menu.employee_care;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import id.co.pegadaian.diarium.R;
import id.co.pegadaian.diarium.adapter.EmployeeCareReplyAdapter;
import id.co.pegadaian.diarium.model.EmployeeCareReplyModel;
import id.co.pegadaian.diarium.util.TimeHelper;
import id.co.pegadaian.diarium.util.UserSessionManager;
import id.co.pegadaian.diarium.util.element.ProgressDialogHelper;

public class DetailEmployeeCareActivity extends AppCompatActivity {
    TextView tvName, tvTitle, tvDesc, tvDate, tvAttachment, tvPost;
    ImageView ivImage;
    TimeHelper timeHelper;
    UserSessionManager session;
    private List<EmployeeCareReplyModel> listModel;
    private EmployeeCareReplyModel model;
    private EmployeeCareReplyAdapter adapter;
    ListView lvComment;
    private ProgressDialogHelper progressDialogHelper;
    String title, desc, image, name, personal, date, ticket, status;
    EditText etComment;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_employee_care);
        timeHelper = new TimeHelper();
        session = new UserSessionManager(this);
        progressDialogHelper = new ProgressDialogHelper();

        tvName = findViewById(R.id.tvName);
        tvTitle = findViewById(R.id.tvTitle);
        tvAttachment = findViewById(R.id.tvAttachment);
        tvDesc = findViewById(R.id.tvDesc);
        tvDate = findViewById(R.id.tvDate);
        ivImage = findViewById(R.id.ivImage);
        lvComment = findViewById(R.id.lvComment);
        tvPost = findViewById(R.id.tvPost);
        etComment = findViewById(R.id.etComment);

        Intent intent = getIntent();
        title = intent.getStringExtra("problem_type");
        desc = intent.getStringExtra("problem_desc");
        image = intent.getStringExtra("image");
        personal = intent.getStringExtra("personal_number");
        date = intent.getStringExtra("change_date");
        ticket = intent.getStringExtra("ticket");
        status = intent.getStringExtra("status");
        name = intent.getStringExtra("name");

        etComment.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.setFocusable(true);
                v.setFocusableInTouchMode(true);
                return false;
            }
        });

        if (tvAttachment.equals("")) {
            tvAttachment.setVisibility(View.GONE);
        }else {
            tvAttachment.setVisibility(View.VISIBLE);
            tvAttachment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(image));
                    startActivity(i);
                }
            });
        }

        tvPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = etComment.getText().toString();
                if (text.equals("")) {
                    Toast.makeText(DetailEmployeeCareActivity.this, "Fill the comment first", Toast.LENGTH_SHORT).show();
                } else {
                    submitComment(text, ticket);
                }
            }
        });

        tvName.setText("By : "+name);
        tvTitle.setText(title);
        tvDesc.setText(desc);
        tvDate.setText("At : "+timeHelper.getElapsedTime(date));
//        Picasso.get().load(image).error(R.drawable.profile).into(ivImage);
        getListReply(ticket);
        getSupportActionBar().setTitle("Employee Care");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getListReply(ticket);
    }


    @Override
    public boolean onSupportNavigateUp(){
        if (status.equals("y")) {
            AlertDialog.Builder builder = new AlertDialog.Builder(DetailEmployeeCareActivity.this);
            builder.setTitle("Confirm");
            builder.setMessage("Have your questions been answered ?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    // Do nothing but close the dialog
                    submitPost(ticket);
                    Toast.makeText(DetailEmployeeCareActivity.this, "Selesai", Toast.LENGTH_SHORT).show();
                }
            });
            builder.setNegativeButton("Not Yet", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                    dialog.dismiss();
                }
            });
            AlertDialog alert = builder.create();
            alert.show();
        } else {
            finish();
        }
        return true;
    }

    private void submitPost(String tiket) {
        progressDialogHelper.showProgressDialog(DetailEmployeeCareActivity.this, "Sending data...");

        @SuppressLint("SimpleDateFormat") SimpleDateFormat tgl = new SimpleDateFormat("yyyy-MM-dd");
        String tRes = tgl.format(new Date());

        @SuppressLint("SimpleDateFormat") SimpleDateFormat jam = new SimpleDateFormat("HH:mm");
        String jamRes = jam.format(new Date());
        AndroidNetworking.upload(session.getServerURL()+"users/employeeCare/updateEmployeeCare/nik/"+session.getUserNIK()+"/buscd/"+session.getUserBusinessCode()+"/tiket/"+tiket)
                .addHeaders("Accept","application/json")
                .addHeaders("Content-Type","application/json")
                .addHeaders("Authorization",session.getToken())
                .addMultipartParameter("problem_status","03")
                .addMultipartParameter("change_user",session.getUserNIK())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // do anything with response
                        System.out.println(response+"RESPONSUBMITNEWCARE");
                        try {
                            if(response.getInt("status")==200){
                                Toast.makeText(DetailEmployeeCareActivity.this, "Thank for your response", Toast.LENGTH_SHORT).show();
                                finish();
                            }else {
                                Toast.makeText(DetailEmployeeCareActivity.this,"Error!",Toast.LENGTH_SHORT).show();
                            }
                            progressDialogHelper.dismissProgressDialog(DetailEmployeeCareActivity.this);
                        }catch (Exception e){
                            progressDialogHelper.dismissProgressDialog(DetailEmployeeCareActivity.this);
                            System.out.println("bbb"+e);
                        }
                    }
                    @Override

                    public void onError(ANError error) {
                        progressDialogHelper.dismissProgressDialog(DetailEmployeeCareActivity.this);
                        System.out.println("ccc"+error);
                    }
                });
    }

    @Override
    public void onBackPressed() {
        if (status.equals("y")) {
            AlertDialog.Builder builder = new AlertDialog.Builder(DetailEmployeeCareActivity.this);
            builder.setTitle("Confirm");
            builder.setMessage("Have your questions been answered ?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    // Do nothing but close the dialog
                    submitPost(title, desc, ticket, image);
                }
            });
            builder.setNegativeButton("Not Yet", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                    dialog.dismiss();
                }
            });
            AlertDialog alert = builder.create();
            alert.show();
        } else {
            finish();
        }
    }


    private void submitPost(String title,String desc, String tiket, String image) {
        progressDialogHelper.showProgressDialog(DetailEmployeeCareActivity.this, "Sending data...");

        @SuppressLint("SimpleDateFormat") SimpleDateFormat tgl = new SimpleDateFormat("yyyy-MM-dd");
        String tRes = tgl.format(new Date());

        @SuppressLint("SimpleDateFormat") SimpleDateFormat jam = new SimpleDateFormat("HH:mm");
        String jamRes = jam.format(new Date());
        AndroidNetworking.upload(session.getServerURL()+"users/employeeCare/postEmployeeCare/nik/"+session.getUserNIK()+"/buscd/"+session.getUserBusinessCode()+"/tiket/"+tiket)
                .addHeaders("Accept","application/json")
                .addHeaders("Content-Type","application/json")
                .addHeaders("Authorization",session.getToken())
                .addMultipartParameter("begin_date",tRes)
                .addMultipartParameter("end_date","9999-12-31")
                .addMultipartParameter("business_code",session.getUserBusinessCode())
                .addMultipartParameter("personal_number",session.getUserNIK())
                .addMultipartParameter("ticket_number",tiket)
                .addMultipartParameter("problem_type",title)
                .addMultipartParameter("problem_desc",desc)
                .addMultipartParameter("image", image)
                .addMultipartParameter("problem_status","03")
                .addMultipartParameter("change_user",session.getUserNIK())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // do anything with response
                        System.out.println(response+"RESPONSUBMITNEWCARE");
                        try {
                            if(response.getInt("status")==200){
                                Toast.makeText(DetailEmployeeCareActivity.this, "Success end this ticket", Toast.LENGTH_SHORT).show();
                                finish();
                            }else {
                                Toast.makeText(DetailEmployeeCareActivity.this,"Error!",Toast.LENGTH_SHORT).show();
                            }
                            progressDialogHelper.dismissProgressDialog(DetailEmployeeCareActivity.this);
                        }catch (Exception e){
                            progressDialogHelper.dismissProgressDialog(DetailEmployeeCareActivity.this);
                            System.out.println("bbb"+e);
                        }
                    }
                    @Override

                    public void onError(ANError error) {
                        progressDialogHelper.dismissProgressDialog(DetailEmployeeCareActivity.this);
                        System.out.println("ccc"+error);
                    }
                });
    }


    private void submitComment(String text, String tiket) {
        progressDialogHelper.showProgressDialog(DetailEmployeeCareActivity.this, "Sending data...");

        @SuppressLint("SimpleDateFormat") SimpleDateFormat tgl = new SimpleDateFormat("yyyy-MM-dd");
        String tRes = tgl.format(new Date());

        @SuppressLint("SimpleDateFormat") SimpleDateFormat jam = new SimpleDateFormat("HH:mm");
        String jamRes = jam.format(new Date());
        AndroidNetworking.upload(session.getServerURL()+"employeecarereply")
                .addHeaders("Accept","application/json")
                .addHeaders("Content-Type","application/json")
                .addHeaders("Authorization",session.getToken())
                .addMultipartParameter("begin_date",tRes)
                .addMultipartParameter("end_date","9999-12-31")
                .addMultipartParameter("business_code",session.getUserBusinessCode())
                .addMultipartParameter("personal_number",session.getUserNIK())
                .addMultipartParameter("id_tiket",tiket)
                .addMultipartParameter("reply_text",text)
                .addMultipartParameter("content_field","")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // do anything with response
                        System.out.println(response+"RESPONCOMMENTEMPLOYEECARE");
                        try {
                            if(response.getInt("status")==200){
                                Toast.makeText(DetailEmployeeCareActivity.this, "Success submit data !", Toast.LENGTH_SHORT).show();
                                etComment.setText("");
                                getListReply(ticket);
                            }else {
                                Toast.makeText(DetailEmployeeCareActivity.this,"Error!",Toast.LENGTH_SHORT).show();
                            }
                            progressDialogHelper.dismissProgressDialog(DetailEmployeeCareActivity.this);
                        }catch (Exception e){
                            progressDialogHelper.dismissProgressDialog(DetailEmployeeCareActivity.this);
                            System.out.println("bbb"+e);
                        }
                    }
                    @Override

                    public void onError(ANError error) {
                        progressDialogHelper.dismissProgressDialog(DetailEmployeeCareActivity.this);
                        System.out.println("ccc"+error);
                    }
                });
    }

    private void getListReply(String ticket_id){
//        progressDialogHelper.showProgressDialog(DetailpostActivity.this, "Getting data...");
        AndroidNetworking.get(session.getServerURL()+"employeecarereply?id_tiket="+ticket_id)
                .addHeaders("Accept","application/json")
                .addHeaders("Content-Type","application/json")
                .addHeaders("Authorization",session.getToken())
                //.addJSONObjectBody(body)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println("RESPONCOMMENTCARE"+response);
                        // do anything with response
                        try {
                            if(response.getInt("status")==200){
//                                session.setToken(response.getString("token"));
                                listModel = new ArrayList<EmployeeCareReplyModel>();
                                JSONArray jsonArray = response.getJSONArray("data");
                                for (int a = 0; a < jsonArray.length(); a++) {
                                    JSONObject object = jsonArray.getJSONObject(a);
                                    String object_identifier = object.getString("object_identifier");
                                    String begin_date = object.getString("begin_date");
                                    String end_date = object.getString("end_date");
                                    String business_code = object.getString("business_code");
                                    String personal_number = object.getString("personal_number");
                                    String id_tiket = object.getString("id_tiket");
                                    String reply_text = object.getString("reply_text");
                                    String content_field = object.getString("content_field");
                                    String change_date = object.getString("change_date");
                                    String change_user = object.getString("change_user");
                                    String name = object.getJSONObject("name").getString("full_name");

                                    model = new EmployeeCareReplyModel(object_identifier, begin_date, end_date, business_code, personal_number, id_tiket, reply_text, content_field, change_date, change_user, name);
                                    listModel.add(model);

                                }
                                adapter = new EmployeeCareReplyAdapter(DetailEmployeeCareActivity.this, listModel);
                                lvComment.setAdapter(adapter);
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


}
