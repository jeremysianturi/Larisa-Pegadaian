package id.co.pegadaian.diarium.controller.home.main_menu.mydevelopment.marketplace.fragment_home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import id.co.pegadaian.diarium.R;
import id.co.pegadaian.diarium.adapter.ChartAdapter;
import id.co.pegadaian.diarium.helper.MyDividerItemDecoration;
import id.co.pegadaian.diarium.model.ChartModel;
import id.co.pegadaian.diarium.util.UserSessionManager;
import jp.wasabeef.recyclerview.animators.SlideInRightAnimator;

public class CartFragment extends Fragment {

  private UserSessionManager session;
  private RecyclerView recyclerView;
  private List<ChartModel> listmodel = new ArrayList<>();
  private ChartModel model;
  private ChartAdapter adapter;
  private String TAG = "Chart_Fragemnt";

  private String OID;

  private String url_image;
  private LinearLayout parent_view;

  public CartFragment(){

  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {

    View view = inflater.inflate(R.layout.chart_fragment, container, false);

    session = new UserSessionManager(getActivity());
    parent_view = view.findViewById(R.id.parent_view);

    // step 2 : initiallizing our views;
    recyclerView = view.findViewById(R.id.recyclerViews);

    // step 3 : initaillizing our adapter
    adapter = new ChartAdapter(listmodel);

    // step 4 :layout manager
    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
    recyclerView.setLayoutManager(layoutManager);
    recyclerView.setItemAnimator(new SlideInRightAnimator());
    recyclerView.addItemDecoration(new MyDividerItemDecoration(Objects.requireNonNull(getActivity()), MyDividerItemDecoration.VERTICAL_LIST,16));

    adapter.setOnItemClickListener(new ChartAdapter.OnItemClickListener() {
      @Override
      public void onSave(int position) {
        Toast.makeText(getActivity(),"position : "+position,Toast.LENGTH_SHORT).show();
      }

      @Override
      public void onDelete(int position, int obj_id) {

        String object_id = String.valueOf(obj_id);
        System.out.println("obj_id "+ object_id);
        postDelete(object_id);
      }


    });
    // set divider in recycleview
//    recyclerView.addItemDecoration(new MyDividerItemDecoration(Objects.requireNonNull(getActivity()),LinearLayoutManager.HORIZONTAL));

//    // add item recycleview click listener
//    ItemClickSupport.addTo(recyclerView).setOnItemClickListener(
//            new ItemClickSupport.OnItemClickListener() {
//              @Override
//              public void onItemClicked(RecyclerView recyclerView, int position, View v) {
//                // do it
//                 ChartModel model = listmodel.get(position);
//
//              }
//            }
//    );

    // step 5 : binding recycleview to adapter
    recyclerView.setAdapter(adapter);


    // call method to get data an add to model
    getChartData();
    return view;
  }

  private void postDelete(String object_id) {
    String oid = object_id;
    System.out.println("iod" + oid);


    JSONObject object = new  JSONObject();
    try {
      object.put("oid",oid);
    } catch (JSONException e) {
      e.printStackTrace();
    }

//    JSONObject body = new JSONObject();

    AndroidNetworking.delete(session.getServerMarketPlacer()+"api/cart")
            .setContentType("application/raw")
            .addHeaders("Authorization","Bearer "+session.getTokenMarket())
//            .addJSONObjectBody(object)
            .addBodyParameter("oid",oid)
//            .addApplicationJsonBody(object)
            .setPriority(Priority.MEDIUM)
            .build()
            .getAsJSONObject(new JSONObjectRequestListener() {
              @Override
              public void onResponse(JSONObject response) {
                Log.i(TAG, "onResponse: " +response);
                try {
                  String msg = response.getString("message");
                  String status = response.getString("status");

                  Snackbar snackbar = Snackbar.make(parent_view,msg, BaseTransientBottomBar.LENGTH_SHORT);
                  snackbar.show();

                } catch (Exception e) {
                  e.printStackTrace();
                }

              }
              @Override
              public void onError(ANError anError) {
                Log.e("postDelete", "onError: " + anError.getResponse());
                Log.e("postDelete", "onError: " + anError.getErrorBody());
              }
            });
  }

  private void getChartData() {
    AndroidNetworking.get(session.getServerMarketPlacer()+"api/cart?include=course_id&include=partner_id&distinct=course_id")
            .addHeaders("Authorization","Bearer "+session.getTokenMarket())
            .setPriority(Priority.MEDIUM)
            .build()
            .getAsJSONObject(new JSONObjectRequestListener() {
              @Override
              public void onResponse(JSONObject response) {
                Log.d("Chart_fragment", "onResponse: "+response);
                try {
                  JSONArray array = response.getJSONArray("data");
                  for (int i = 0; i<array.length(); i++){
                    JSONObject object = array.getJSONObject(i);

                    int object_identifier = object.getInt("object_identifier");
                    String begin_date = object.getString("begin_date");
                    String end_date = object.getString("end_date");
                    String change_date = object.getString("change_date");
                    String change_user = object.getString("change_user");
                    int course_id = object.getInt("course_id");
                    int partner_id = object.getInt("partner_id");
                    String personal_number = object.getString("personal_number");
                    String condition = object.getString("condition");

                    String url = "https://cdn5.f-cdn.com/contestentries/1147802/23358977/59da4d152cbc9_thumb900.jpg";


                    model =  new ChartModel("","","","",url,condition,object_identifier,partner_id,course_id);
                    listmodel.add(model);
                  }

                  for (int y = 0; y<listmodel.size(); y++){
                    int course_id = listmodel.get(y).getCourse_id();
                    int partner_id = listmodel.get(y).getPartner_id();
                    String condition = listmodel.get(y).getCondition();
                    getDetailChart(course_id,partner_id,condition,y);

                  }

                } catch (Exception e) {
                  e.printStackTrace();
                }
              }

              @Override
              public void onError(ANError anError) {
                Log.e("chart_data", "onError: " + anError.getResponse());
                Log.e("chart_data", "onError: " + anError.getErrorBody());
              }
            });
//
    // notofiy adapter with data change
    adapter.notifyDataSetChanged();
  }

  private void getDetailChart(int course_id, int partner_id, String condition, int position) {
    AndroidNetworking.get(session.getServerMarketPlacer()+"api/getcustom?include=id_course&include=partner_id&distinct=id_course&include=condition&id_course="+course_id+"&partner_id="+partner_id+"&condition="+condition)
            .setPriority(Priority.HIGH)
            .build()
            .getAsJSONObject(new JSONObjectRequestListener() {
              @Override
              public void onResponse(JSONObject response) {
                Log.d("custom_chart ", "onResponse: " +response);
                try {
                  JSONArray array = response.getJSONArray("data");
                  JSONObject object = array.getJSONObject(0);

                  String price = object.getString("price_course");
                  String point = object.getString("point_course");
                  String course_description = object.getJSONObject("id_course").getString("course_description");
                  String partner_name = object.getJSONObject("partner_id").getString("partner_name");
                  String image = object.getJSONObject("id_course").getString("image");

                  String url = "http://apimarketplace.digitalevent.id";
                  String url_course = url+image;

                  listmodel.get(position).setPrice(price);
                  listmodel.get(position).setPoint(point);
                  listmodel.get(position).setTittle(course_description);
                  listmodel.get(position).setCreator(partner_name);
                  listmodel.get(position).setUrl(url_course);

                  adapter.notifyDataSetChanged();

                } catch (Exception e) {
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

}
