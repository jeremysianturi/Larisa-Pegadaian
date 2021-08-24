package id.co.pegadaian.diarium.controller.home.main_menu.mydevelopment.marketplace.fragment_home;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import id.co.pegadaian.diarium.R;
import id.co.pegadaian.diarium.adapter.MyCoursesAdapter;
import id.co.pegadaian.diarium.model.MyCourseModel;
import id.co.pegadaian.diarium.util.ItemClickSupport;
import id.co.pegadaian.diarium.util.UserSessionManager;
import id.co.pegadaian.diarium.util.element.ProgressDialogHelper;
import jp.wasabeef.recyclerview.animators.SlideInRightAnimator;

public class MyCoursesFragment extends Fragment {

  private ProgressDialogHelper progressDialogHelper;
  private RecyclerView recyclerView;
  private MyCoursesAdapter adapter;
  private List<MyCourseModel> listmodel = new ArrayList<>();
  private MyCourseModel model;
  private UserSessionManager session;
  private String TAG = "MyCourse";
  private String mCurr_stat, batch;
//  private SearchView searchitem;
  private EditText etSearch;

  // filter mycourse
  private Spinner filter;

  public MyCoursesFragment(){

  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.mycourses_fragment, container, false);

    session = new UserSessionManager(getActivity());
    progressDialogHelper = new ProgressDialogHelper();


    /** Spinner Option  **/
    filter = view.findViewById(R.id.filter);
    filter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
      @Override
      public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        listmodel.clear();
        // get selected item positiom
        int selected = parent.getSelectedItemPosition();
        if (selected == 0 ){
          mCurr_stat = "01";
        }else {
          mCurr_stat = "03";
        }
        getAccountLMS();
//        Toast.makeText(getActivity(),"selected Courses : "+selected + "+ curr_stat : "+mCurr_stat,Toast.LENGTH_SHORT).show();
      }

      @Override
      public void onNothingSelected(AdapterView<?> parent) {

      }
    });

    /**     recycleview for list Courses    **/
    // step 2 : initiallizing our views;
    recyclerView = view.findViewById(R.id.recyclerViews);


    // step 3 : initaillizing our adapter
    adapter = new MyCoursesAdapter(listmodel);

    // step 4 :layout manager
    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
    recyclerView.setLayoutManager(layoutManager);
    recyclerView.setItemAnimator(new SlideInRightAnimator());
    // set divider in recycleview
//    recyclerView.addItemDecoration(new MyDividerItemDecoration(Objects.requireNonNull(getActivity()),LinearLayoutManager.HORIZONTAL));

    // add item recycleview click listener
    ItemClickSupport.addTo(recyclerView).setOnItemClickListener(
            new ItemClickSupport.OnItemClickListener() {
              @Override
              public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                // do it
                MyCourseModel model = listmodel.get(position);

                Toast.makeText(getActivity(),"Selecting : "+model.getCourse_name(),Toast.LENGTH_SHORT).show();

              }
            }
    );

    // step 5 : binding recycleview to adapter
    recyclerView.setAdapter(adapter);

    /** filter adapter  **/

    etSearch = view.findViewById(R.id.inputSearch);
    etSearch.addTextChangedListener(new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after) {

      }

      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count) {

      }

      @Override
      public void afterTextChanged(Editable s) {
        adapter.getFilter().filter(s.toString());
      }
    });



    /** call method  **/
    setFilterCourse();

    return view;
  }

  private void getAccountLMS() {
    progressDialogHelper.showProgressDialog(Objects.requireNonNull(getActivity()),"Load data ...");
    AndroidNetworking.get(session.getServerLMSDEMO()+"lms/api/account?type%5B%5D=PARID")
            .addHeaders("Authorization","Bearer "+session.getTokenMarket())
            .setPriority(Priority.MEDIUM)
            .build()
            .getAsJSONArray(new JSONArrayRequestListener() {
              @Override
              public void onResponse(JSONArray response) {
                Log.d(TAG, "gettokenLMS: "+ response);
                try {
                 String access_token = response.getJSONObject(0).getString("access_token");
                 String username = response.getJSONObject(0).getString("username");
                 String type = response.getJSONObject(0).getString("type");
                 String id = response.getJSONObject(0).getString("id");

                 getDataCourses(access_token,2, mCurr_stat);

                } catch (Exception e) {
                  e.printStackTrace();
                }
              }

              @Override
              public void onError(ANError anError) {
                progressDialogHelper.dismissProgressDialog(getActivity());
                Log.e(TAG, "onError: "+anError.getResponse() );
                Log.e(TAG, "onError: "+anError.getErrorBody() );

              }
            });

  }

  private void getDataCourses(String access_token, int i, String stat) {
    AndroidNetworking.get(session.getServerLMSDEMO()+"lms/api/myevent/peserta?curr_stat="+stat+"&parid="+i)
            .addHeaders("Authorization","Bearer "+access_token)
            .setPriority(Priority.MEDIUM)
            .build()
            .getAsJSONObject(new JSONObjectRequestListener() {
              @Override
              public void onResponse(JSONObject response) {
                Log.d(TAG, "getbatch: "+ response);
                try {
                  JSONArray array = response.getJSONArray("data");
                  for (int i=0; i<array.length(); i++){
                    JSONObject object = array.getJSONObject(i);

                    String bussines_code = object.getString("bussines_code");
                    batch = object.getString("batch");
                    String batch_name = object.getString("batch_name");
                    String event_id = object.getString("event_id");
                    String event_name = object.getString("event_name");
                    String begin_date = object.getString("begin_date");
                    String end_date = object.getString("end_date");
                    String event_curr_stat = object.getString("event_curr_stat");
                    String evnt_curr_statid = object.getString("evnt_curr_statid");
                    String event_status = object.getString("event_status");
                    String event_stat_id = object.getString("event_stat_id");
                    String location_id = object.getString("location_id");
                    String location = object.getString("location");
                    String cur_id = object.getString("cur_id");
                    String curriculum = object.getString("curriculum");
                    String event_type = object.getString("event_type");
                    String curriculum_buscd = object.getString("curriculum_buscd");
                    String participant_id = object.getString("participant_id");
                    String partcipant_name = object.getString("partcipant_name");
                    String parti_nicknm = object.getString("parti_nicknm");
                    String company_name = object.getString("company_name");

                  }
                  getDetail(batch);

                } catch (Exception e) {
                  e.printStackTrace();
                }
              }

              @Override
              public void onError(ANError anError) {
                progressDialogHelper.dismissProgressDialog(getActivity());
                Log.e(TAG, "onError_Batch : "+anError.getResponse() );
                Log.e(TAG, "onError_Batch :"+anError.getErrorBody() );

              }
            });


  }

  private void getDetail(String id_mapping) {
    AndroidNetworking.get(session.getServerMarketPlacer()+"api/getcustom?include=id_course&include=partner_id&distinct=id_course&id_mapping="+id_mapping)
            .addHeaders("Authorization","Bearer "+session.getTokenMarket())
            .setPriority(Priority.MEDIUM)
            .build()
            .getAsJSONObject(new JSONObjectRequestListener() {
              @Override
              public void onResponse(JSONObject response) {
                Log.d(TAG, "getDetail: "+response);
                try{
                  JSONArray array = response.getJSONArray("data");
                  for (int j=0; j<array.length(); j++){
                    JSONObject object = array.getJSONObject(j);

                    String price = object.getString("price_course");
                    String point = object.getString("point_course");
                    String course_code = object.getJSONObject("id_course").getString("course_code");
                    String image_url = object.getJSONObject("id_course").getString("image");
                    String course_name = object.getJSONObject("id_course").getString("course_name");
                    String partner_name = object.getJSONObject("partner_id").getString("partner_name");
                    String partner_code = object.getJSONObject("partner_id").getString("partner_code");

                    String url = "http://apimarketplace.digitalevent.id";
                    String url_course = url+image_url;
                    model = new MyCourseModel(course_name,course_code,price,point,partner_name,partner_code,url_course);
                    listmodel.add(model);
                  }
                  progressDialogHelper.dismissProgressDialog(getActivity());

                  // tell adapter
                  adapter.notifyDataSetChanged();

                } catch (Exception e) {
                  e.printStackTrace();
                }

              }

              @Override
              public void onError(ANError anError) {
                progressDialogHelper.dismissProgressDialog(getActivity());
                Log.e(TAG, "onError_custom : "+anError.getResponse() );
                Log.e(TAG, "onError_custom :"+anError.getErrorBody() );
              }
            });
  }


  private void setFilterCourse() {

    String filterName[] = {"On GoingCourses" ,"Completed Courses"};

    ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getContext(), R.layout.spinner_item_list, filterName);
    spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
    filter.setAdapter(spinnerArrayAdapter);

  }
}
