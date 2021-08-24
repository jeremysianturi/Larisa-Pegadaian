package id.co.pegadaian.diarium.controller.home.main_menu.mydevelopment.marketplace.fragment_home;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.jama.carouselview.CarouselView;
import com.jama.carouselview.CarouselViewListener;
import com.jama.carouselview.enums.IndicatorAnimationType;
import com.jama.carouselview.enums.OffsetType;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import id.co.pegadaian.diarium.R;
import id.co.pegadaian.diarium.adapter.MenuMarketAdapter;
import id.co.pegadaian.diarium.adapter.ReviewFeaturedAdapter;
import id.co.pegadaian.diarium.adapter.TopCoursesAdapter;
import id.co.pegadaian.diarium.adapter.TrendingAdapter;
import id.co.pegadaian.diarium.controller.home.main_menu.mydevelopment.marketplace.DetailCourse.DetailCoursesActivity;
import id.co.pegadaian.diarium.controller.home.main_menu.mydevelopment.marketplace.PayActivity.HistoryPaymentActivity;
import id.co.pegadaian.diarium.controller.home.main_menu.mydevelopment.marketplace.PayActivity.PaymentActivity;
import id.co.pegadaian.diarium.controller.home.main_menu.mydevelopment.marketplace.PayActivity.SettingMarketActivity;
import id.co.pegadaian.diarium.controller.home.main_menu.mydevelopment.marketplace.PayActivity.TopUpActivity;
import id.co.pegadaian.diarium.controller.home.main_menu.mydevelopment.marketplace.categories.FinanceActivity;
import id.co.pegadaian.diarium.model.ReviewFeaturedModel;
import id.co.pegadaian.diarium.model.TopCoursesModel;
import id.co.pegadaian.diarium.model.TrendingModel;
import id.co.pegadaian.diarium.util.BaseFragment;
import id.co.pegadaian.diarium.util.ExpandableHeightGridView;
import id.co.pegadaian.diarium.util.ItemClickSupport;
import id.co.pegadaian.diarium.util.UserSessionManager;
import jp.wasabeef.recyclerview.animators.SlideInRightAnimator;

public class FeaturedFragment extends BaseFragment {

  private String TAG = "Featured_Fragment";
  private UserSessionManager session;
  /***  ==============  Best seller horizontal Recycle View =============== ****/
  // step 1 : define some variable
  private RecyclerView recyclerView;
  private List<TopCoursesModel> listmodel = new ArrayList<>();
  private TopCoursesModel model;
  private TopCoursesAdapter adapter;
  /***  ==============  Best seller horizontal Recycle View =============== ****/

  /***  ==============  Trending horizontal Recycle View =============== ****/

  // step 1 : define some variable
  private RecyclerView recyclerView2;
  private List<TrendingModel> listmodel2 = new ArrayList<>();
  private TrendingAdapter adapter2;
  private TrendingModel model2;
  /***  ==============  Trending horizontal Recycle View =============== ****/

  /***  ==============  Review horizontal Recycle View =============== ****/

  // step 1 : define some variable
  private RecyclerView recyclerView3;
  private List<ReviewFeaturedModel> listmodel3 = new ArrayList<>();
  private ReviewFeaturedAdapter adapter3;
  private ReviewFeaturedModel model3;
  /***  ==============  Review horizontal Recycle View =============== ****/

  private ExpandableHeightGridView gridView;
  private String[] menu ={
          "Finance & Accounting",
          "IT & Software",
          "Development",
          "Business",
          "Office Productivity",
          "Personal Development"

//          "My Development",
//          "FAQ (Chatbot)"
  };

  private int[] image={
          R.drawable.ic_finance,
          R.drawable.ic_sofware,
          R.drawable.ic_dev,
          R.drawable.ic_bussiness,
          R.drawable.ic_office,
          R.drawable.ic_personal_development

//          R.drawable.ic_myprofile,
//          R.drawable.ic_faqmenu
          //  R.drawable.ic_today_activity
  };

//  private CarouselView carouselView;
//  private ImageListener imageListener;
  private CarouselView carouselView;
  private int[] sampleImages = {R.drawable.events, R.drawable.events, R.drawable.events, R.drawable.events, R.drawable.events};
  private String[] txt_desc = {"Description banner 1","Description banner 2","Description banner 3","Description banner 4","Description banner 5"};
  private LinearLayout pay,topup,setting,history;


  public FeaturedFragment (){

  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {

    View view = inflater.inflate(R.layout.featured_fragment, container, false);

    session = new UserSessionManager(getActivity());

    //basefragment method
    findViews(view);
    initViews(view);
    initListener(view);

    // buildx
    buildGridView();
    buildCarousellView();
    buildRecycleView();

    // call method to get data an add to model
    getBestSeller();
    getDataTrending();
    getReviewFeatured();
    return view;
  }

  @Override
  public void findViews(View view) {
    // step 2 : initiallizing our views;
    recyclerView = view.findViewById(R.id.recyclerViews);
    // step 2 : initiallizing our views;
    recyclerView2 = view.findViewById(R.id.recyclerViews2);
    // step 2 : initiallizing our views;
    recyclerView3 = view.findViewById(R.id.recyclerViews3);
    gridView = view.findViewById(R.id.gridview);
    carouselView = view.findViewById(R.id.carouselView);

    // grid view
    setting = view.findViewById(R.id.layout_setting);
    history = view.findViewById(R.id.layout_history);
    topup = view.findViewById(R.id.layout_topup);
    pay = view.findViewById(R.id.layout_pay);

  }

  @Override
  public void initViews(View view) {



  }

  @Override
  public void initListener(View view) {

    pay.setOnClickListener(v -> {
      Intent i = new Intent(getActivity(), PaymentActivity.class);
      startActivity(i);
    });

    topup.setOnClickListener(v -> {
      Intent i = new Intent(getActivity(), TopUpActivity.class);
      startActivity(i);
    });

    history.setOnClickListener(v -> {
      Intent i = new Intent(getActivity(), HistoryPaymentActivity.class);
      startActivity(i);
    });

    setting.setOnClickListener(v -> {
      Intent i = new Intent(getActivity(), SettingMarketActivity.class);
      startActivity(i);
    });


  }

  private void buildGridView(){
    // grid adapter
    MenuMarketAdapter gridAdapter = new MenuMarketAdapter(getActivity(),image,menu);
    gridView.setAdapter(gridAdapter);
    gridView.setExpanded(true);
    gridView.setOnItemClickListener((parent, view, position, id) -> {
      String clickedText = menu[position];
      Intent i ;
      switch (clickedText) {
        case "Finance & Accounting" :
          i = new Intent(getActivity(), FinanceActivity.class);
          startActivity(i);
          break;

        case "IT & Software" :
          i = new Intent(getActivity(), FinanceActivity.class);
          startActivity(i);
          break;

        case "Development" :
          i = new Intent(getActivity(), FinanceActivity.class);
          startActivity(i);
          break;

        case "Business" :
          i = new Intent(getActivity(), FinanceActivity.class);
          startActivity(i);
          break;

        case "Office Productivity" :
          i = new Intent(getActivity(), FinanceActivity.class);
          startActivity(i);
          break;

        case "Personal Development" :
          i = new Intent(getActivity(), FinanceActivity.class);
          startActivity(i);
          break;
      }
    });

  }

  private void buildRecycleView() {
    /*  ==============  Best seller horizontal Recycle View =============== */

    // step 3 : initaillizing our adapter
    adapter = new TopCoursesAdapter(listmodel);

    // step 4 :layout manager
    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);
    recyclerView.setLayoutManager(layoutManager);
    recyclerView.setItemAnimator(new SlideInRightAnimator());
    recyclerView.setNestedScrollingEnabled(false);
    recyclerView.setHasFixedSize(true);
    // set divider in recycleview
//    recyclerView.addItemDecoration(new MyDividerItemDecoration(Objects.requireNonNull(getActivity()),LinearLayoutManager.HORIZONTAL));

    // add item recycleview click listener
    ItemClickSupport.addTo(recyclerView).setOnItemClickListener(
            (recyclerView, position, v) -> {
              // do it
//                TopCoursesModel model = listmodel.get(position);

              Intent i = new Intent(getActivity(), DetailCoursesActivity.class);
              startActivity(i);

//                Toast.makeText(getActivity(),"Selecting Best Seller : "+model.getTittle(),Toast.LENGTH_SHORT).show();
            }
    );

    // step 5 : binding recycleview to adapter
    recyclerView.setAdapter(adapter);

/*  ==============  Trending horizontal Recycle View =============== ****/

    // step 3 : initaillizing our adapter
    adapter2 = new TrendingAdapter(listmodel2);

    // step 4 :layout manager
    RecyclerView.LayoutManager layoutManager2 = new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);
    recyclerView2.setLayoutManager(layoutManager2);
    recyclerView2.setItemAnimator(new SlideInRightAnimator());
    recyclerView2.setHasFixedSize(true);
    recyclerView2.setNestedScrollingEnabled(false);

    // set divider in recycleview
//    recyclerView.addItemDecoration(new MyDividerItemDecoration(Objects.requireNonNull(getActivity()),LinearLayoutManager.HORIZONTAL));

    // add item recycleview click listener
    ItemClickSupport.addTo(recyclerView2).setOnItemClickListener(
            (recyclerView, position, v) -> {
              // do it
              Intent i = new Intent(getActivity(), DetailCoursesActivity.class);
              i.putExtra("course_id",listmodel2.get(position).getCourse_id());
              i.putExtra("partner_id",listmodel2.get(position).getPartner_id());
              startActivity(i);

//                TopCoursesModel model = listmodel.get(position);
//
//                Toast.makeText(getActivity(),"Selecting Trending : "+model.getTittle(),Toast.LENGTH_SHORT).show();
            }
    );

    // step 5 : binding recycleview to adapter
    recyclerView2.setAdapter(adapter2);

    /*  ==============  review featured horizontal Recycle View =============== ****/
    // step 3 : initaillizing our adapter
    adapter3 = new ReviewFeaturedAdapter(listmodel3);

    // step 4 :layout manager
    RecyclerView.LayoutManager layoutManager3 = new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);
    recyclerView3.setLayoutManager(layoutManager3);
    recyclerView3.setItemAnimator(new SlideInRightAnimator());
    recyclerView3.setNestedScrollingEnabled(false);
    recyclerView3.setHasFixedSize(true);
    // set divider in recycleview
//    recyclerView.addItemDecoration(new MyDividerItemDecoration(Objects.requireNonNull(getActivity()),LinearLayoutManager.HORIZONTAL));

    // add item recycleview click listener
    ItemClickSupport.addTo(recyclerView3).setOnItemClickListener(
            (recyclerView, position, v) -> {
              // do it
//                TopCoursesModel model = listmodel.get(position);
              Intent i = new Intent(getActivity(), DetailCoursesActivity.class);
              startActivity(i);

            }
    );

    // step 5 : binding recycleview to adapter
    recyclerView3.setAdapter(adapter3);
/*  ==============  review horizontal Recycle View =============== ****/

  }

  private void buildCarousellView() {
    /***  ==============  Carousel View  =============== ****/

    carouselView.setSize(sampleImages.length);
    carouselView.setResource(R.layout.center_carousel_item);
    carouselView.setAutoPlay(true);
    carouselView.setAutoPlayDelay(5000);
    carouselView.setIndicatorAnimationType(IndicatorAnimationType.THIN_WORM);
    carouselView.setCarouselOffset(OffsetType.START);
    carouselView.setCarouselViewListener(new CarouselViewListener() {
      @Override
      public void onBindView(View view, int position) {
        // Example here is setting up a full image carousel
        ImageView imageView = view.findViewById(R.id.imageView);
        imageView.setImageDrawable(getResources().getDrawable(sampleImages[position]));
        TextView textView = view.findViewById(R.id.txt_banner);
        textView.setText(txt_desc[position]);
        imageView.setOnClickListener(v -> Toast.makeText(getActivity(),"select"+ position,Toast.LENGTH_SHORT).show());
      }
    });

    // After you finish setting up, show the CarouselView
    carouselView.show();

  }

  private void getReviewFeatured() {
    AndroidNetworking.get(session.getServerMarketPlacer()+"api/gettesti?all=False&include=personal_number&course_id=1")
            .addHeaders("Authorization","Bearer "+session.getTokenMarket())
            .setPriority(Priority.MEDIUM)
            .build()
            .getAsJSONObject(new JSONObjectRequestListener() {
              @Override
              public void onResponse(JSONObject response) {
                Log.i("review_featured", "onResponse: "+response);
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

                     model3 = new ReviewFeaturedModel(end_date,business_code,personal_number,full_name,nickname,born_city,born_date,gender,religion,language,national,tribe,blood_type,rhesus,marital_status,marital_date,personal_number_reference,change_date,change_user,begin_date,isi_testimoni,course_id);
                     listmodel3.add(model3);
                  }

                  // set to adapter
                  adapter3.notifyDataSetChanged();
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

  private void getDataTrending() {
    AndroidNetworking.get(session.getServerMarketPlacer()+"api/gettrending?include=partner_id&include=course_id")
            .addHeaders("Accept","application/json")
            .addHeaders("Content-Type","application/json")
            .addHeaders("Authorization","Bearer "+session.getTokenMarket())
            .setPriority(Priority.MEDIUM)
            .build()
            .getAsJSONObject(new JSONObjectRequestListener() {
              @Override
              public void onResponse(JSONObject response) {
                Log.i("trending_response", "onResponse: " + response);
                try {
                  JSONArray array = response.getJSONArray("data");
                  for (int i = 0 ; i<array.length(); i++){
                    JSONObject object = array.getJSONObject(i);

                    String course_id = object.getString("course_id");
                    String course_name = object.getString("course_name");
                    String course_description = object.getString("course_description");
                    String partner_id = object.getString("partner_id");
                    String partner_name = object.getString("partner_name");
                    String partner_code = object.getString("partner_code");
                    String count_click = object.getString("count_click");
                    String image_course = object.getString("image_course");
                    String image_partner = object.getString("image_partner");

                    String url = "http://apimarketplace.digitalevent.id";
                    String url_course = url+image_course;
                    String url_partner = url+image_partner;

                    // add to model data
                    model2 = new TrendingModel(course_id,course_name,course_description,partner_id,partner_name,partner_code,count_click,url_course,url_partner);
                    listmodel2.add(model2);
                  }

                  // add to adapter
                  adapter2.notifyDataSetChanged();


                } catch (JSONException e) {
                  e.printStackTrace();
                }
              }

              @Override
              public void onError(ANError anError) {
                Log.d("Featured", "onError: "+anError);
                Log.d("Featured", "onError: "+anError.getResponse());
                Log.d("Featured", "onError: "+anError.getErrorBody());
              }
            });

  }

  private void getBestSeller() {
    AndroidNetworking.get(session.getServerMarketPlacer()+"api/bestseller?include=course_id&include=partner_id")
            .addHeaders("Authorization","Bearer "+session.getTokenMarket())
            .setPriority(Priority.MEDIUM)
            .build()
            .getAsJSONObject(new JSONObjectRequestListener() {
              @Override
              public void onResponse(JSONObject response) {
//                Log.i("bestseller", "onResponse: " + response);
                try {
                  JSONArray array = response.getJSONArray("data");
                  for (int j = 0; j<array.length(); j++){
                    JSONObject object = array.getJSONObject(j);

                    String bussines_code = object.getString("bussines_code");
                    String course_id = object.getString("course_id");
                    String course_name = object.getString("course_name");
                    String course_description = object.getString("course_description");
                    String partner_name = object.getString("partner_name");
                    String partner_code = object.getString("partner_code");
                    String partner_id = object.getString("partner_id");
                    String count_buy = object.getString("count_buy");
                    String image_course = object.getString("image_course");
                    String image_partner = object.getString("image_partner");

                    String url = "http://apimarketplace.digitalevent.id";
                    String url_course = url+image_course;
                    System.out.println("url_course "+url_course);
                    String url_partner = url+image_partner;

                    model = new TopCoursesModel(bussines_code,course_id,course_name,course_description,partner_name,partner_code,partner_id,count_buy,url_course,url_partner);
                    listmodel.add(model);

                  }
                  // notify adapter for update data
                  adapter.notifyDataSetChanged();

                } catch (JSONException e) {
                  e.printStackTrace();
                }
              }

              @Override
              public void onError(ANError anError) {
                Log.e(TAG, "onError: "+anError.getResponse());
                Log.e(TAG, "onError: "+anError.getErrorBody());
              }
            });

  }



}
