package id.co.pegadaian.diarium.controller.home.main_menu.mydevelopment.marketplace.DetailCourse;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import id.co.pegadaian.diarium.R;
import id.co.pegadaian.diarium.adapter.ReviewCourseAdapter;
import id.co.pegadaian.diarium.adapter.SylabusAdapter;
import id.co.pegadaian.diarium.controller.home.main_menu.mydevelopment.marketplace.Review.ReviewActivity;
import id.co.pegadaian.diarium.model.DetailCourseModel;
import id.co.pegadaian.diarium.model.ReviewCourseModel;
import id.co.pegadaian.diarium.model.SylabusModel;
import id.co.pegadaian.diarium.util.BaseActivity;
import id.co.pegadaian.diarium.util.UserSessionManager;
import id.co.pegadaian.diarium.util.element.ProgressDialogHelper;

public class DetailCoursesActivity extends BaseActivity {

  private ProgressDialogHelper progressDialogHelper;
  private UserSessionManager sessionManager;
  private List<DetailCourseModel> listmodel_detail = new ArrayList<DetailCourseModel>();
  private List<ReviewCourseModel> listmodelReview = new ArrayList<ReviewCourseModel>();
  private List<SylabusModel> listmodelSylabus = new ArrayList<SylabusModel>();
  private DetailCourseModel model_detail;
  private ReviewCourseModel modelReview;
  private SylabusModel modelSylabus;
  private ReviewCourseAdapter adapterReview;
  private SylabusAdapter adapterSylabus;
  private RecyclerView recyclerView , rc_syllabus;
  private TextView review;
  private String mCourse_id, mPartner_id;
  private String TAG = "Detail_course";
  private ConstraintLayout parent_view;
  private String stCourseName, stCourseDesc, stPrice ,stPoint, stCondition, stDateNow, stTime, stUrlCourse;
  private String stPartnerId, stConditionId;
  private int inCourseId, inPartnerId;

  private TextView tvCourse_name , tvCourse_description, tvPrice, tvPoint, tvAdd_cart, tvWishlist, tvDescription;
  private ImageView ivContent, ivBacgroundContent;

  private ArrayList<String> arrayList_partner_name = new ArrayList<>();
  private ArrayList<String> arrayList_partner_id = new ArrayList<>();
  private ArrayList<String> arrayListCondition = new ArrayList<>();
  private ArrayList<String> arrayListConditionID = new ArrayList<>();
  private ArrayList<String> arrayListPrice = new ArrayList<>();

  private String id_mapping;

  private Spinner spnPartner, spnCondition;

  // date convert
  Locale id = new Locale("in", "ID");

  String[] courses_option = {"Certificate","Training","Certificate + Training"};

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_detail_courses);

    sessionManager = new UserSessionManager(this);
    progressDialogHelper = new ProgressDialogHelper();

    /* get Intent data from previous activity */
    Intent i = getIntent();
    mCourse_id =i.getStringExtra("course_id");
    mPartner_id = i.getStringExtra("partner_id");

    /* action bar and tittle bar */
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    getSupportActionBar().setTitle("Detail Course");

    /* Date  */
    SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
    stDateNow =fmt.format(new Date());

    findViews();
    initViews();
    initListener();

    buildRecycleView(); // recycleview method
    spinnerPartner();// spinner method

    /* API REQUEST */
    getDataReview();
    getCustomCondition();

  }



  @Override
  public void findViews() {

    parent_view = findViewById(R.id.parent_view);
    tvCourse_name = findViewById(R.id.course_name);
    tvCourse_description = findViewById(R.id.course_description);
    tvPrice = findViewById(R.id.price);
    tvPoint = findViewById(R.id.points);
    tvAdd_cart = findViewById(R.id.tvadd_chart);
    tvWishlist = findViewById(R.id.tvadd_wishlist);
    tvAdd_cart = findViewById(R.id.tvadd_chart);
    tvWishlist = findViewById(R.id.tvadd_wishlist);

    tvDescription = findViewById(R.id.txt_description);

    review = findViewById(R.id.all_review);

    /** rc sylabus **/
    rc_syllabus = findViewById(R.id.recyclerView_sylabus);
    recyclerView = findViewById(R.id.recyclerView_review);

    ivContent = findViewById(R.id.iv_content);
    ivBacgroundContent = findViewById(R.id.iv_content_background);

    // spinner
    spnPartner = findViewById(R.id.spinner_partner);
    spnCondition = findViewById(R.id.spinner_condition);


  }

  @Override
  public void initViews() {

    tvPrice.setText("Rp. "+stPrice);
    tvPoint.setText(stPoint+" Points");
    tvCourse_name.setText(stCourseName);
    tvCourse_description.setText(stCourseDesc);
    tvDescription.setText(stCourseDesc);

    Picasso.get()
            .load(stUrlCourse)
            .placeholder(R.color.grey)
            .error(R.drawable.events)
            .into(ivContent);

    Picasso.get()
            .load(stUrlCourse)
            .placeholder(R.color.grey)
            .error(R.drawable.events)
            .into(ivBacgroundContent);

  }

  @Override
  public void initListener() {

    review.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
//        Toast.makeText(DetailCoursesActivity.this,"selected Review ",Toast.LENGTH_SHORT).show();
        Intent i = new Intent(DetailCoursesActivity.this, ReviewActivity.class);
        startActivity(i);
      }
    });

    tvWishlist.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        postToWishlist();
      }
    });

    tvAdd_cart.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        postToChart();
      }
    });

    // spinner partner
    spnPartner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
      @Override
      public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        String name  = parent.getItemAtPosition(position).toString();
        long posisi = parent.getItemIdAtPosition(position);
        String partner_id = arrayList_partner_id.get(position);


        stPartnerId = partner_id;

        spinnerCondition(stPartnerId);
        Log.d(TAG, "onItemSelected: "+name+"   "+ posisi + "   "+partner_id + "  "+ stPartnerId);
      }

      @Override
      public void onNothingSelected(AdapterView<?> parent) {

      }
    });

    //spinner condition
    spnCondition.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
      @Override
      public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        String conditionId = arrayListConditionID.get(position);

        stConditionId = conditionId;
        Log.d(TAG, "onItemSelected() returned: " + stConditionId);

        getIdMapping(stPartnerId,stConditionId);
      }

      @Override
      public void onNothingSelected(AdapterView<?> parent) {

      }
    });

  }

  private void postToWishlist() {
    String crs_id = String.valueOf(inCourseId);
    String prt_id = String.valueOf(inPartnerId);

//    progressDialogHelper.showProgressDialog(DetailCoursesActivity.this,"Submit Wishlist");
    AndroidNetworking.post(sessionManager.getServerMarketPlacer()+"api/wishlist")
            .addHeaders("Content-Type","application/x-www-form-urlencoded") // baru di tambah
            .addHeaders("Authorization","Bearer "+sessionManager.getTokenMarket())
            .addBodyParameter("course_id",crs_id)
            .addBodyParameter("partner_id",prt_id)
            .addBodyParameter("begin_date",stDateNow)
            .addBodyParameter("end_date","9999-12-31")
            .setPriority(Priority.MEDIUM)
            .build()
            .getAsJSONObject(new JSONObjectRequestListener() {
              @Override
              public void onResponse(JSONObject response) {
                Log.d(TAG, "onResponse: " +response);
                try {
                  String msg = response.getString("message");
                  String status = response.getString("status");

                  Toast.makeText(DetailCoursesActivity.this,msg,Toast.LENGTH_SHORT).show();
                  progressDialogHelper.dismissProgressDialog(DetailCoursesActivity.this);

                } catch (JSONException e) {
                  e.printStackTrace();
                }
              }

              @Override
              public void onError(ANError anError) {
                progressDialogHelper.dismissProgressDialog(DetailCoursesActivity.this);
                Log.e(TAG, "onError: wishlist "+anError.getResponse());
                Log.e(TAG, "onError: wishlist "+anError.getErrorBody());
                Log.e(TAG, "onError: wishlist ",anError );
              }
            });

  }

  private void postToChart() {
    String crs_id = String.valueOf(inCourseId);
    String prt_id = String.valueOf(inPartnerId);
    AndroidNetworking.post(sessionManager.getServerMarketPlacer()+"api/cart")
            .addHeaders("Content-Type","application/x-www-form-urlencoded") // baru di tambah
            .addHeaders("Authorization","Bearer "+sessionManager.getTokenMarket())
            .addBodyParameter("begin_date",stDateNow)
            .addBodyParameter("end_date","9999-12-31")
            .addBodyParameter("course_id",crs_id)
            .addBodyParameter("partner_id",prt_id)
            .addBodyParameter("personal_number",sessionManager.getUserNIK())
            .addBodyParameter("condition",stCondition)
            .setPriority(Priority.MEDIUM)
            .build()
            .getAsJSONObject(new JSONObjectRequestListener() {
              @Override
              public void onResponse(JSONObject response) {
                Log.d(TAG, "postchart: "+ response);
                try {
                  String msg = response.getString("message");
                  String status = response.getString("status");
                  Toast.makeText(DetailCoursesActivity.this,msg,Toast.LENGTH_SHORT).show();
                  progressDialogHelper.dismissProgressDialog(DetailCoursesActivity.this);
                } catch (JSONException e) {
                  e.printStackTrace();
                }
              }

              @Override
              public void onError(ANError anError) {
                progressDialogHelper.dismissProgressDialog(DetailCoursesActivity.this);
                Toast.makeText(DetailCoursesActivity.this,"already in your cart",Toast.LENGTH_SHORT).show();
                Log.e(TAG, "onError: postcart "+anError.getResponse());
                Log.e(TAG, "onError: postcart "+anError.getErrorBody());
                Log.e(TAG, "onError: postcart ",anError );
              }
            });

  }

  private void buildRecycleView() {
    /* review rc */
    adapterReview = new ReviewCourseAdapter(listmodelReview);
    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
    recyclerView.setLayoutManager(layoutManager);
    recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
    recyclerView.setNestedScrollingEnabled(false);
    recyclerView.setAdapter(adapterReview);

    /* sylabus rc */
    adapterSylabus = new SylabusAdapter(listmodelSylabus);
    RecyclerView.LayoutManager layoutManager2 = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
    rc_syllabus.setLayoutManager(layoutManager2);
    rc_syllabus.addItemDecoration(new DividerItemDecoration(rc_syllabus.getContext(), DividerItemDecoration.VERTICAL));
    rc_syllabus.setNestedScrollingEnabled(false);
    rc_syllabus.setAdapter(adapterSylabus);
  }

  private void spinnerPartner() {
   AndroidNetworking.get(sessionManager.getServerMarketPlacer()+"api/getcustom?include=partner_id&include=condition&id_course="+mCourse_id)
           .setPriority(Priority.MEDIUM)
           .build()
           .getAsJSONObject(new JSONObjectRequestListener() {
             @Override
             public void onResponse(JSONObject response) {
               Log.i(TAG, "onResponse: spinner_partner" + response );

               try {
                 arrayList_partner_name = new ArrayList<String>();
                 arrayList_partner_id = new ArrayList<String>();

                 JSONArray array = response.getJSONArray("data");
                 for (int i = 0 ; i<array.length(); i++){
                   JSONObject object = array.getJSONObject(i);

                   String partner_name = object.getJSONObject("partner_id").getString("partner_name");
                   String partner_id = object.getJSONObject("partner_id").getString("partner_id");
                   String object_name = object.getJSONObject("condition").getString("object_name");
                   String object_code = object.getJSONObject("condition").getString("object_code");

                   String price_course = object.getString("price_course");
                   String point_course = object.getString("point_course");

                   arrayList_partner_name.add(partner_name);
                   arrayList_partner_id.add(partner_id);

                 }

                 ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(DetailCoursesActivity.this,   R.layout.spinner_item_list, arrayList_partner_name);
                 spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
                 spnPartner.setAdapter(spinnerArrayAdapter);
                 spnPartner.setSelected(false);

               } catch (Exception e) {
                 e.printStackTrace();
               }

             }

             @Override
             public void onError(ANError anError) {
               Log.d(TAG, "spinPartnet: "+anError);
               Log.d(TAG, "spinPartnet: "+anError.getResponse());
               Log.d(TAG, "spinPartnet: "+anError.getErrorBody());
             }
           });

  }
  private void spinnerCondition(String partner) {
   AndroidNetworking.get(sessionManager.getServerMarketPlacer()+"api/getcustom?include=partner_id&include=condition&id_course="+mCourse_id)
           .setPriority(Priority.MEDIUM)
           .build()
           .getAsJSONObject(new JSONObjectRequestListener() {
             @Override
             public void onResponse(JSONObject response) {
               Log.i(TAG, "onResponse: spinner_partner" + response );

               try {
                 arrayListCondition = new ArrayList<String>();
                 arrayListConditionID = new ArrayList<String>();

                 JSONArray array = response.getJSONArray("data");
                 for (int i = 0 ; i<array.length(); i++){
                   JSONObject object = array.getJSONObject(i);

                   String partner_name = object.getJSONObject("partner_id").getString("partner_name");
                   String partner_id = object.getJSONObject("partner_id").getString("partner_id");
                   String object_name = object.getJSONObject("condition").getString("object_name");
                   String object_code = object.getJSONObject("condition").getString("object_code");

                   String price_course = object.getString("price_course");
                   String point_course = object.getString("point_course");

                   if (partner_id.equals(partner)){
                     arrayListCondition.add(object_name);
                     arrayListConditionID.add(object_code);
                   }

                 }
//
//                 ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(DetailCoursesActivity.this,   R.layout.spinner_item_list, arrayList_partner_name);
//                 spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
//                 spnPartner.setAdapter(spinnerArrayAdapter);
                  ArrayAdapter<String> spinnerArrayAdapter2 = new ArrayAdapter<String>(DetailCoursesActivity.this,   R.layout.spinner_item_list, arrayListCondition);
                 spinnerArrayAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
                 spnCondition.setAdapter(spinnerArrayAdapter2);
                 spnCondition.setSelected(false);

               } catch (Exception e) {
                 e.printStackTrace();
               }

             }
             @Override
             public void onError(ANError anError) {
               Log.d(TAG, "spinPartnet: "+anError);
               Log.d(TAG, "spinPartnet: "+anError.getResponse());
               Log.d(TAG, "spinPartnet: "+anError.getErrorBody());
             }
           });

  }

  private void getDataReview() {
    AndroidNetworking.get(sessionManager.getServerMarketPlacer()+"api/gettesti?all=False&include=personal_number&course_id=1")
            .addHeaders("Authorization","Bearer "+sessionManager.getTokenMarket())
            .setPriority(Priority.MEDIUM)
            .build()
            .getAsJSONObject(new JSONObjectRequestListener() {
              @Override
              public void onResponse(JSONObject response) {
//                Log.i("review_featured", "onResponse: "+response);
                try {
                  JSONArray array = response.getJSONArray("data");
                  for (int x = 0 ; x<3; x++){
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
                Log.d("Featured", "review: "+anError);
                Log.d("Featured", "review: "+anError.getResponse());
                Log.d("Featured", "review: "+anError.getErrorBody());
              }
            });
  }

  private void getCustomCondition(){
    progressDialogHelper.showProgressDialog(DetailCoursesActivity.this,"Load data ...");
    AndroidNetworking.get(sessionManager.getServerMarketPlacer()+"api/getcustom?id_course="+mCourse_id+"&include=id_course&include=partner_id&include=condition&distinct=id_course&partner_id="+mPartner_id)
            .addHeaders("Authorization","Bearer "+sessionManager.getTokenMarket())
            .setPriority(Priority.MEDIUM)
            .build()
            .getAsJSONObject(new JSONObjectRequestListener() {
              @Override
              public void onResponse(JSONObject response) {
                Log.d(TAG, "onResponse: "+ response);
                try {
                  JSONArray array = response.getJSONArray("data");
                  for (int a=0 ; a<array.length(); a++){
                    JSONObject object = array.getJSONObject(a);

                    String object_identifier = object.getString("object_identifier");
                    int course_id = Integer.parseInt(object.getJSONObject("id_course").getString("course_id"));
                    String course_name = object.getJSONObject("id_course").getString("course_name");
                    String course_description = object.getJSONObject("id_course").getString("course_description");
                    String image = object.getJSONObject("id_course").getString("image");
                    int partner_id = Integer.parseInt(object.getJSONObject("partner_id").getString("partner_id"));
                    String partner_code = object.getJSONObject("partner_id").getString("partner_code");
                    String partner_name = object.getJSONObject("partner_id").getString("partner_name");
                    int price_course = object.getInt("price_course");
                    int point_course = object.getInt("point_course");
                    int id_mapping = object.getInt("id_mapping");
                    String object_code = object.getJSONObject("condition").getString("object_code");


                    String url = "http://apimarketplace.digitalevent.id";
                    String url_course = url+image;

                    stCourseName = course_name;
                    stCourseDesc = course_description;
                    stPrice = String.valueOf(price_course);
                    stPoint = String.valueOf(point_course);
                    inCourseId = course_id;
                    inPartnerId = partner_id;
                    stCondition = object_code;
                    stUrlCourse = url_course;

                    model_detail = new DetailCourseModel(object_identifier,course_name,partner_name,course_description,course_id,partner_id,image,price_course,point_course,id_mapping,object_code);
                    listmodel_detail.add(model_detail);
                  }
                  progressDialogHelper.dismissProgressDialog(DetailCoursesActivity.this);
                  initViews();

                } catch (Exception e) {
                  e.printStackTrace();
                }
              }

              @Override
              public void onError(ANError anError) {
                progressDialogHelper.dismissProgressDialog(DetailCoursesActivity.this);
                Log.e(TAG, "onError: condition "+anError.getResponse());
                Log.e(TAG, "onError: condition "+anError.getErrorBody());
              }
            });
  }

  private void getIdMapping(String partner_id, String condition) {

    System.out.println("MASUK METHOD");

    AndroidNetworking.get(sessionManager.getServerMarketPlacer()+"api/getcustom?include=id_course&include=partner_id&include=condition&condition="
            +condition +"&distinct=partner_id&partner_id=" + partner_id)
            .addHeaders("Authorization","Bearer "+sessionManager.getTokenMarket())
            .setPriority(Priority.MEDIUM)
            .build()
            .getAsJSONObject(new JSONObjectRequestListener() {
              @Override
              public void onResponse(JSONObject response) {
                Log.d(TAG, "onResponse: id_Mapping "+response);

                try {

                  String id_mapping = response.getJSONArray("data").getJSONObject(0).getString("id_mapping");

                  System.out.println("id_mapping_Broo "+ id_mapping);

                  getSessionSylabus(id_mapping);

                } catch (JSONException e) {
                  e.printStackTrace();
                }


              }

              @Override
              public void onError(ANError anError) {
                Log.e(TAG, "onError: id_mapping"+anError.getResponse() );
                Log.e(TAG, "onError: id_mapping"+anError.getErrorBody() );

              }
            });


  }

  // harcode id mapping
  private void getSessionSylabus(String id_mapping){
//    AndroidNetworking.get("https://lmsdemo.digitalevent.id/lms/api/session?order%5BBEGDA%5D=asc&batch%5B%5D="+id_mapping+"&per_page=100")
    AndroidNetworking.get("https://lmsdemo.digitalevent.id/lms/api/session?order%5BBEGDA%5D=asc&batch%5B%5D=5&per_page=100")
            .addHeaders("Authorization","Bearer "+sessionManager.getTokenMarket())
            .setPriority(Priority.MEDIUM)
            .build()
            .getAsJSONObject(new JSONObjectRequestListener() {
              @Override
              public void onResponse(JSONObject response) {
                Log.d(TAG, "onResponse: SessionSylabus "+response);
                try {

                  JSONArray array = response.getJSONArray("data");

                  String session_id = array.getJSONObject(0).getString("session_id");

                  getSylabusData(session_id);
                } catch (Exception e) {
                  e.printStackTrace();
                }
              }

              @Override
              public void onError(ANError anError) {
                Log.e(TAG, "onError: sessionSylabus"+anError );
                Log.e(TAG, "onError: sessionSylabus"+anError.getResponse() );
                Log.e(TAG, "onError: sessionSylabus"+anError.getErrorBody() );

              }
            });
  }

  //hardcode session_id
  private void getSylabusData(String session_id){
    listmodelSylabus.clear();
    AndroidNetworking.get("https://lmsdemo.digitalevent.id/lms/api/schedule?order%5BBEGDA%5D=asc&session%5B%5D="+"3"+"&per_page=100")
            .addHeaders("Authorization","Bearer "+sessionManager.getTokenMarket())
            .setPriority(Priority.MEDIUM)
            .build()
            .getAsJSONObject(new JSONObjectRequestListener() {
              @Override
              public void onResponse(JSONObject response) {
//                Log.d(TAG, "onResponse: SylabusData " +response);
                try{

                  JSONArray array = response.getJSONArray("data");
                  for (int i= 0; i<array.length();i++){
                    JSONObject object = array.getJSONObject(i);

                    String schedule_name = object.getString("schedule_name");
                    System.out.println("schedule_name_ "+ schedule_name);

                    modelSylabus = new SylabusModel(schedule_name);
                    listmodelSylabus.add(modelSylabus);
                  }

                  adapterSylabus.notifyDataSetChanged();

                } catch (Exception e) {
                  e.printStackTrace();
                }
              }

              @Override
              public void onError(ANError anError) {

              }
            });

  }

  @Override
  public boolean onSupportNavigateUp() {
    finish();
    return true;
  }
}
