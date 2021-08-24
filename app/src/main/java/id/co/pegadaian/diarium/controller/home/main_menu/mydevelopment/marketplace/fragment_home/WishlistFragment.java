package id.co.pegadaian.diarium.controller.home.main_menu.mydevelopment.marketplace.fragment_home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import id.co.pegadaian.diarium.R;
import id.co.pegadaian.diarium.adapter.WishlistAdapter;
import id.co.pegadaian.diarium.helper.MyDividerItemDecoration;
import id.co.pegadaian.diarium.model.WishlistModel;
import id.co.pegadaian.diarium.model.WishlistModel2;
import id.co.pegadaian.diarium.util.ItemClickSupport;
import id.co.pegadaian.diarium.util.UserSessionManager;
import id.co.pegadaian.diarium.util.element.ProgressDialogHelper;
import jp.wasabeef.recyclerview.animators.SlideInRightAnimator;

//import com.facebook.shimmer.ShimmerFrameLayout;

public class WishlistFragment extends Fragment {


  private ProgressDialogHelper progressDialogHelper;
  private WishlistAdapter adapter;
  private WishlistModel model;
  private WishlistModel model2;
  private WishlistModel2 model3;
  private List<WishlistModel> listmodel = new ArrayList<>();
  private List<WishlistModel> listmodel2 = new ArrayList<>();
  private List<WishlistModel2> listmodel3;
  private UserSessionManager session;
  private ListView listView;
  private String token , datenow;
  private int count;

  private DecimalFormat decimalFormat = new DecimalFormat("#.##");

  private RecyclerView recyclerView;
//  private ShimmerFrameLayout shimmerFrameLayout;

  public WishlistFragment(){

  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.wishlist_fragment, container, false);

    progressDialogHelper = new ProgressDialogHelper();
    session = new UserSessionManager(getActivity());
//    shimmerFrameLayout = view.findViewById(R.id.shimmer_view_container);
//    listView = view.findViewById(R.id.listview);

    // step 2 : initiallizing our views;
    recyclerView = view.findViewById(R.id.recyclerView);


    SimpleDateFormat sdf = new SimpleDateFormat("yyy-MM-dd");
    datenow = sdf.format(new Date());

    buildRecycleView();
    getWishlist();

     count = 0;

    return view;


  }

  private void buildRecycleView() {
    // step 3 : initaillizing our adapter
    adapter = new WishlistAdapter(listmodel,listmodel2);

    // step 4 :layout manager
    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
    recyclerView.setLayoutManager(layoutManager);
    recyclerView.setItemAnimator(new SlideInRightAnimator());
    recyclerView.addItemDecoration(new MyDividerItemDecoration(Objects.requireNonNull(getActivity()), MyDividerItemDecoration.VERTICAL_LIST,16));

    // add item recycleview click listener
    ItemClickSupport.addTo(recyclerView).setOnItemClickListener(
            new ItemClickSupport.OnItemClickListener() {
              @Override
              public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                // do it
                WishlistModel model = listmodel.get(position);

                Toast.makeText(getActivity(),"Selecting : "+listmodel.get(position),Toast.LENGTH_SHORT).show();
              }
            }
    );

    // step 5 : binding recycleview to adapter
    recyclerView.setAdapter(adapter);
  }

  private void getWishlist() {
    AndroidNetworking.get(session.getServerMarketPlacer()+"api/wishlist?include=course_id&include=partner_id&distinct=course_id")
            .addHeaders("Authorization","Bearer "+session.getTokenMarket())
            .setPriority(Priority.MEDIUM)
            .build()
            .getAsJSONObject(new JSONObjectRequestListener() {
              @Override
              public void onResponse(JSONObject response) {
                Log.i("response", "onResponse_parent: " + response);
//                shimmerFrameLayout.stopShimmer();
                recyclerView.setVisibility(View.VISIBLE);
//                shimmerFrameLayout.setVisibility(View.GONE);
                try {
                  listmodel3 = new ArrayList<WishlistModel2>();
                  JSONArray jsonArray = response.getJSONArray("data");
//                  System.out.println("panjang_data " + jsonArray.length());
                  for (int x=0;x<jsonArray.length();x++ ){

                    JSONObject object = jsonArray.getJSONObject(x);

                    int course_id = object.getJSONObject("course_id").getInt("course_id");
                    String course_description = object.getJSONObject("course_id").getString("course_description");
                    int partner_id = object.getJSONObject("partner_id").getInt("partner_id");
                    String partner_name = object.getJSONObject("partner_id").getString("partner_name");

                    String url = "https://cdn5.f-cdn.com/contestentries/1147802/23358977/59da4d152cbc9_thumb900.jpg";

                    System.out.println("course_id model : "+course_id +"partner_id model : " + partner_id );
                    // model for course_id and partner_id›
                    model3 = new WishlistModel2(course_id,partner_id);
                    listmodel3.add(model3);

                    System.out.println("course_id : "+listmodel3.get(x).getId_course() +" partner_id : " + listmodel3.get(x).getId_part() );

                    // model data for listview
                    model= new WishlistModel(course_description,"0","BestSeller","","",partner_name,url);
                    listmodel.add(model);

                  }

                  adapter.notifyDataSetChanged();
//                   call method detail before set adapter
                  for (int i = 0; i<listmodel3.size(); i++) {
                    String course_id = String.valueOf(listmodel3.get(i).getId_course());
                    String partner_id = String.valueOf(listmodel3.get(i).getId_part());
                    System.out.println("course model "+ course_id + "   " + partner_id);

                    getDetail(course_id,partner_id,i);
                    getRating(course_id,partner_id,i);
                  }


                } catch (JSONException e) {
                  e.printStackTrace();
                }
              }

              @Override
              public void onError(ANError anError) {
                Log.e("response_detail", "onError: "+anError.getErrorBody() );
                Log.e("response_detail", "onError: "+anError.getResponse() );

              }
            });

  }

  private void getRating(String course_id, String partner_id, int count) {
    int total = listmodel3.size();

    AndroidNetworking.get(session.getServerMarketPlacer()+"api/getaveragerating?begin_date__lte="+datenow+"&end_date__gt="+datenow+"&course_id="+course_id+"&partner_id="+partner_id)
            .setPriority(Priority.HIGH)
            .build()
            .getAsJSONObject(new JSONObjectRequestListener() {
              @Override
              public void onResponse(JSONObject response) {
                Log.d("response_rating", "onResponse: "+response);

                try {

                    double rate = response.getDouble("data");

                    String rating = decimalFormat.format(rate);
                    System.out.println("result_rating "+rating);

                    listmodel.get(count).setRating(rating);

                    // step 6 : notify adapter
                    adapter.notifyDataSetChanged();

                } catch (JSONException e) {
                  e.printStackTrace();
                }


              }

              @Override
              public void onError(ANError anError) {
                Log.e("response_rating", "onError: " + anError.getResponse());
                Log.e("response_rating", "onError: " + anError.getErrorBody());
              }
            });
  }


  private void getDetail(String course_id, String partner_id, int count) {
    System.out.println("masuk_method_detail ");

    int total = listmodel3.size();

      System.out.println("count : "+count);
      System.out.println("total : "+total);

      System.out.println("course_id + partnert_id request :  "+ course_id+ " " + partner_id );

      AndroidNetworking.get(session.getServerMarketPlacer() +
              "api/getcustom?include=id_course&include=partner_id&include=condition&distinct=id_course&id_course="
              + course_id + "&partner_id=" + partner_id)
              .addHeaders("Accept","application/json")
              .setPriority(Priority.HIGH)
              .build()
              .getAsJSONObject(new JSONObjectRequestListener() {
                @Override
                public void onResponse(JSONObject response) {
                  System.out.println("response_detail " + response);
                  try {
                    JSONArray array = response.getJSONArray("data");

                      if (array.length() == 0){
                        listmodel.get(count).setPrice("0");
                      }
                      else{
                        JSONObject object = array.getJSONObject(0);

                        String image_url = object.getJSONObject("id_course").getString("image");
                        System.out.println("IMG_URL " + image_url);
                        int price = Integer.parseInt(object.getString("price_course"));

                        String priceString = String.valueOf(price);
//                        System.out.println("price " + price);

                        String url = "http://apimarketplace.digitalevent.id";
                        String url_course = url+image_url;

                        System.out.println("URL_COURSE "+url_course);

                        listmodel.get(count).setUrl(url_course);
                        listmodel.get(count).setPrice(priceString);
                    }
                  // step 6 : notify adapter
                    adapter.notifyDataSetChanged();

                  } catch (JSONException e) {
                    e.printStackTrace();
                  }


                }

                @Override
                public void onError(ANError anError) {
                  Log.e("response_detail", "onError: " + anError.getResponse());
                  Log.e("response_detail", "onError: " + anError.getErrorBody());

                }
              });
  }

}
