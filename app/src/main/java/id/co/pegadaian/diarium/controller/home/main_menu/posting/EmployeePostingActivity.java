package id.co.pegadaian.diarium.controller.home.main_menu.posting;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
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
import id.co.pegadaian.diarium.adapter.MyPostingAdapter;
import id.co.pegadaian.diarium.controller.profile.DetailpostActivity;
import id.co.pegadaian.diarium.model.MyPostingModel;
import id.co.pegadaian.diarium.util.UserSessionManager;
import id.co.pegadaian.diarium.util.element.ProgressDialogHelper;

public class EmployeePostingActivity extends AppCompatActivity {
    ListView listMyPosting;
    private ProgressDialogHelper progressDialogHelper;
    private List<MyPostingModel> listModel;
    private MyPostingModel model;
    private MyPostingAdapter adapter;
    UserSessionManager session;
    String personal_numbe_paramr;
    TextView tvNullPost, tvCountFriend, tvCountCommunity, tvName, tvNIK, tvJob;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_posting);
        Intent intent = getIntent();
        personal_numbe_paramr = intent.getStringExtra("personal_number");
        progressDialogHelper = new ProgressDialogHelper();
        session = new UserSessionManager(this);
        listMyPosting = findViewById(R.id.listMyPosting);
        tvNullPost = (TextView) findViewById(R.id.tvNullPost);
        getMyPost(session.getUserNIK(), session.getUserFullName());

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Employee Posting");
    }

    @Override
    public void onResume(){
        super.onResume();
        getMyPost(session.getUserNIK(), session.getUserFullName());
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

    private void getMyPost(String nik, String name){
        progressDialogHelper.showProgressDialog(EmployeePostingActivity.this, "Getting data...");
//        String urlGetMyPost = session.getServerURL()+"users/timelineDetailPosting/limit/1000/nik/"+nik+"/buscd/"+session.getUserBusinessCode();   // url yang lama
        String urlGetMyPost = session.getServerURL()+"users/timelineposting/limit/1000/nik/"+nik+"/buscd/"+session.getUserBusinessCode();
        System.out.println("url get my post : " + urlGetMyPost);
        AndroidNetworking.get(urlGetMyPost)
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
                        System.out.println("response get my post di employee posting : " + response);
                        try {
                            if(response.getInt("status")==200){
//                                session.setToken(response.getString("token"));
                                listModel = new ArrayList<MyPostingModel>();
                                JSONArray jsonArray = response.getJSONArray("data");
                                if (jsonArray.length()==0) {
                                    System.out.println("check masuk ke 0 get my post");
                                    listMyPosting.setVisibility(View.GONE);
                                    tvNullPost.setVisibility(View.VISIBLE);
                                } else {
                                    System.out.println("check masuk ga ke 0 get my post");
                                    listMyPosting.setVisibility(View.VISIBLE);
                                    tvNullPost.setVisibility(View.GONE);
                                    for (int a = 0; a < jsonArray.length(); a++) {

                                        JSONObject object = jsonArray.getJSONObject(a);

                                        String begin_date = object.getString("begin_date");
                                        String end_date = object.getString("end_date");
                                        String business_code = object.getString("business_code");
                                        String change_date = object.getString("change_date");
                                        String change_user = object.getString("change_user");
                                        String date = object.getString("date");
                                        String description = object.getString("description");
                                        String posting_id = object.getString("posting_id");
                                        String title = object.getString("title");
                                        String personal_number = object.getString("personal_number");
                                        String time = object.getString("time");
                                        String updateAt = object.getString("updated_at");
                                        String image = object.getString("image");
                                        String name = object.getString("name");
                                        String profile = object.getString("profile");
                                        int lovelike = object.getInt("jml_lovelike");
                                        int isLiked = object.getInt("is_liked");
                                        int dislike = object.getInt("jml_dislike");
                                        int comment = object.getInt("jml_comment");

                                        model = new MyPostingModel(begin_date,end_date,business_code,change_date,change_user,date,description,posting_id,title,personal_number,time,updateAt,image,name,profile,lovelike,isLiked,dislike,comment);
                                        listModel.add(model);
                                    }
                                    adapter = new MyPostingAdapter(EmployeePostingActivity.this, listModel);
                                    listMyPosting.setAdapter(adapter);

                                    listMyPosting.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                                        generateActivityId();
                                            Intent i = new Intent(EmployeePostingActivity.this, DetailpostActivity.class);
                                            i.putExtra("posting_id",listModel.get(position).getPosting_id());
                                            i.putExtra("title",listModel.get(position).getTitle());
                                            i.putExtra("description",listModel.get(position).getDescription());
                                            i.putExtra("date",listModel.get(position).getChange_date());
                                            i.putExtra("image",listModel.get(position).getImage());
                                            i.putExtra("name",listModel.get(position).getName());
                                            i.putExtra("avatar",listModel.get(position).getProfile());
                                            i.putExtra("status_like",listModel.get(position).getIsLiked());
                                            startActivity(i);
                                        }
                                    });
                                }
                                progressDialogHelper.dismissProgressDialog(EmployeePostingActivity.this);
                            }else{
                                progressDialogHelper.dismissProgressDialog(EmployeePostingActivity.this);

                            }
                        }catch (Exception e){
                            progressDialogHelper.dismissProgressDialog(EmployeePostingActivity.this);

                            System.out.println(e);
                        }

                    }
                    @Override
                    public void onError(ANError error) {
                        progressDialogHelper.dismissProgressDialog(EmployeePostingActivity.this);

                        System.out.println("onerror get my post di employee posting : " + error);
                    }
                });
    }
}
