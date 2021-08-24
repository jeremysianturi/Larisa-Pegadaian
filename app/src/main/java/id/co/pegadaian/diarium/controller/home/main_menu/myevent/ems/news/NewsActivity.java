package id.co.pegadaian.diarium.controller.home.main_menu.myevent.ems.news;

import android.os.Bundle;
import android.view.View;
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
import id.co.pegadaian.diarium.adapter.NewsAdapter;
import id.co.pegadaian.diarium.model.NewsModel;
import id.co.pegadaian.diarium.util.UserSessionManager;
import id.co.pegadaian.diarium.util.element.ProgressDialogHelper;

public class NewsActivity extends AppCompatActivity {
    private ListView listView;
    private UserSessionManager session;
    private List<NewsModel> listModel;
    private NewsModel model;
    private NewsAdapter adapter;
    private ProgressDialogHelper progressDialogHelper;
    TextView tvNull;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        session = new UserSessionManager(getApplicationContext());
        progressDialogHelper = new ProgressDialogHelper();

        tvNull = (TextView) findViewById(R.id.tvNull);
        listView = (ListView) findViewById(R.id.lv_media);
        getNews(session.getEventId());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("News");
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void getNews(String event_id){
        progressDialogHelper.showProgressDialog(NewsActivity.this, "Getting data...");
        AndroidNetworking.get(session.getServerURL()+"users/eventEMS/"+event_id+"/news/buscd/"+session.getUserBusinessCode())
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
                        System.out.println(response+"awdede");
                        try {
                            if(response.getInt("status")==200){
//                                session.setToken(response.getString("token"));
                                listModel = new ArrayList<NewsModel>();
                                JSONArray jsonArray = response.getJSONArray("data");
                                if (jsonArray.length()==0) {
                                    tvNull.setVisibility(View.VISIBLE);
                                    listView.setVisibility(View.GONE);
                                } else {
                                    tvNull.setVisibility(View.GONE);
                                    listView.setVisibility(View.VISIBLE);
                                    for (int a = 0; a < jsonArray.length(); a++) {
                                        JSONObject object = jsonArray.getJSONObject(a);
                                        String begin_date = object.getString("begin_date");
                                        String end_date = object.getString("end_date");
                                        String business_code = object.getString("business_code");
                                        String personal_number = object.getString("personal_number");
                                        String event_id = object.getString("event_id");
                                        String news_id = object.getString("news_id");
                                        String title = object.getString("title");
                                        String description = object.getString("description");
                                        String image = object.getString("image");
                                        String change_date = object.getString("change_date");
                                        String change_user = object.getString("change_user");
                                        System.out.println(begin_date+"dl2j3nr3");
                                        model = new NewsModel(begin_date, end_date, business_code, personal_number, event_id, news_id, title, description, image, change_date, change_user);
                                        listModel.add(model);
                                    }
                                    adapter = new NewsAdapter(NewsActivity.this, listModel);
                                    listView.setAdapter(adapter);
                                }
                                progressDialogHelper.dismissProgressDialog(NewsActivity.this);
                            }else{
//                                popUpLogin();
                                progressDialogHelper.dismissProgressDialog(NewsActivity.this);
                            }
                        }catch (Exception e){
                            progressDialogHelper.dismissProgressDialog(NewsActivity.this);
                            System.out.println(e);
                        }

                    }
                    @Override
                    public void onError(ANError error) {
                        progressDialogHelper.dismissProgressDialog(NewsActivity.this);
                        System.out.println(error);
                    }
                });
    }
}
