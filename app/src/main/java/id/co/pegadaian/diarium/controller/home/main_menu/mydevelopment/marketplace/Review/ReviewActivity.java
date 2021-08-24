package id.co.pegadaian.diarium.controller.home.main_menu.mydevelopment.marketplace.Review;

import android.os.Bundle;
import android.util.Log;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import id.co.pegadaian.diarium.R;
import id.co.pegadaian.diarium.adapter.ReviewActivityAdapter;
import id.co.pegadaian.diarium.model.ReviewCourseModel;
import id.co.pegadaian.diarium.util.BaseActivity;
import id.co.pegadaian.diarium.util.UserSessionManager;

public class ReviewActivity extends BaseActivity {

  private UserSessionManager session;
  private List<ReviewCourseModel> listmodelReview = new ArrayList<ReviewCourseModel>();
  private ReviewCourseModel modelReview;
  private RecyclerView recyclerView;
  private ReviewActivityAdapter adapterReview;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_review);

    session = new UserSessionManager(ReviewActivity.this);
    adapterReview = new ReviewActivityAdapter(listmodelReview);

    // set ActionBar and Tittle Bar
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    getSupportActionBar().setTitle("Detail Course");

    /* Base Activity Method */
    findViews();
    initViews();
    initListener();

    /* Build Recycle View */
    buildRecycleview();

    /* Api Request */
    getDataReview();
  }
  private void buildRecycleview(){

    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
    recyclerView.setLayoutManager(layoutManager);
    recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
    recyclerView.setNestedScrollingEnabled(false);

    recyclerView.setAdapter(adapterReview);

  }

  @Override
  public void findViews() {
    recyclerView = findViewById(R.id.recyclerView);

  }

  @Override
  public void initViews() {

  }

  @Override
  public void initListener() {

  }

  private void getDataReview() {
    AndroidNetworking.get(session.getServerMarketPlacer()+"api/gettesti?all=False&include=personal_number&course_id=1")
            .addHeaders("Authorization","Bearer "+session.getTokenMarket())
            .setPriority(Priority.MEDIUM)
            .build()
            .getAsJSONObject(new JSONObjectRequestListener() {
              @Override
              public void onResponse(JSONObject response) {
//                Log.i("review_featured", "onResponse: "+response);
                try {
                  JSONArray array = response.getJSONArray("data");
                  for (int x = 0 ; x<array.length(); x++){
                    JSONObject object = array.getJSONObject(x);
                    String end_date = object.getJSONObject("personal_number").getString("end_date");
                    String business_code = object.getJSONObject("personal_number").getString("business_code");
                    String personal_number = object.getJSONObject("personal_number").getString("personal_number");
                    String full_name = object.getJSONObject("personal_number").getString("full_name");
                    String nickname = object.getJSONObject("personal_number").getString("nickname");
                    String born_city = object.getJSONObject("personal_number").getString("born_city");
                    String born_date= object.getJSONObject("personal_number").getString("born_date");
                    String gender = object.getJSONObject("personal_number").getString("gender");
                    String religion = object.getJSONObject("personal_number").getString("religion");
                    String language = object.getJSONObject("personal_number").getString("language");
                    String national = object.getJSONObject("personal_number").getString("national");
                    String tribe = object.getJSONObject("personal_number").getString("tribe");
                    String blood_type = object.getJSONObject("personal_number").getString("blood_type");
                    String rhesus = object.getJSONObject("personal_number").getString("rhesus");
                    String marital_status = object.getJSONObject("personal_number").getString("marital_status");
                    String marital_date = object.getJSONObject("personal_number").getString("marital_date");
                    String personal_number_reference = object.getJSONObject("personal_number").getString("personal_number_reference");
                    String change_date = object.getJSONObject("personal_number").getString("change_date");
                    String change_user = object.getJSONObject("personal_number").getString("change_user");
                    String begin_date = object.getJSONObject("personal_number").getString("begin_date");

                    String isi_testimoni = object.getString("isi_testimoni");
                    String course_id =object.getString("course_id");



                    SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
                    Date newDate = null;
                    try {
                      newDate = format.parse(begin_date);
                    } catch (ParseException e) {
                      e.printStackTrace();
                    }

                    format = new SimpleDateFormat("MMM, dd");
                    String dateReview = format.format(newDate);

                    modelReview= new ReviewCourseModel(end_date,business_code,personal_number,full_name,nickname,born_city,born_date,gender,religion,language,national,tribe,blood_type,rhesus,marital_status,marital_date,personal_number_reference,change_date,change_user,dateReview,isi_testimoni,course_id);
                    listmodelReview.add(modelReview);
                  }

                  // set to adapter
                  adapterReview.notifyDataSetChanged();
                } catch (JSONException e) {
                  e.printStackTrace();
                }
              }

              @Override
              public void onError(ANError anError) {
                Log.d("ReviewActivity", "review: "+anError);
                Log.d("ReviewActivity", "review: "+anError.getResponse());
                Log.d("ReviewActivity", "review: "+anError.getErrorBody());
              }
            });
  }

  @Override
  public boolean onSupportNavigateUp() {
    finish();
    return true;
  }
}
