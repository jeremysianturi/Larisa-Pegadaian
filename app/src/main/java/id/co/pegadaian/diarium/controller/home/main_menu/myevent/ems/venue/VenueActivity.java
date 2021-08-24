package id.co.pegadaian.diarium.controller.home.main_menu.myevent.ems.venue;

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
import id.co.pegadaian.diarium.adapter.VenueAdapter;
import id.co.pegadaian.diarium.model.VenueModel;
import id.co.pegadaian.diarium.util.UserSessionManager;
import id.co.pegadaian.diarium.util.element.ProgressDialogHelper;

public class VenueActivity extends AppCompatActivity {
    private ListView listView;
    private UserSessionManager session;
    private List<VenueModel> listModel;
    private VenueModel model;
    private VenueAdapter adapter;
    private ProgressDialogHelper progressDialogHelper;
    TextView tvNull;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_venue);
        session = new UserSessionManager(getApplicationContext());
        progressDialogHelper = new ProgressDialogHelper();
        tvNull = (TextView) findViewById(R.id.tvNull);
        listView = findViewById(R.id.lv_venue);
        progressDialogHelper.dismissProgressDialog(VenueActivity.this);
        getVenue();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Venue");
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void getVenue(){
        progressDialogHelper.showProgressDialog(VenueActivity.this, "Getting data...");
        AndroidNetworking.get(session.getServerURL()+"users/getAllVenue/eventid/"+session.getEventId()+"/buscd/"+session.getUserBusinessCode())
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
                                listModel = new ArrayList<VenueModel>();
                                JSONArray jsonArray = response.getJSONArray("data");
                                System.out.println(jsonArray.length()+"PANJANGNYA");
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
                                        String venue_id = object.getString("venue_id");
                                        String title = object.getString("title");
                                        String venue_desc = object.getString("venue_desc");
                                        String latitude = object.getString("latitude");
                                        String longitude = object.getString("longitude");
                                        String image = object.getString("image");
                                        String change_date = object.getString("change_date");
                                        String change_user = object.getString("change_user");
                                        System.out.println(begin_date+"dl2j3nr3");
                                        model = new VenueModel(begin_date, end_date, business_code, personal_number, event_id, venue_id, title, venue_desc,latitude, longitude, image, change_date, change_user);
                                        listModel.add(model);
                                    }
                                    adapter = new VenueAdapter(VenueActivity.this, listModel);
                                    listView.setAdapter(adapter);
                                }
                                progressDialogHelper.dismissProgressDialog(VenueActivity.this);
                            }else{
//                                popUpLogin();
                                progressDialogHelper.dismissProgressDialog(VenueActivity.this);
                            }
                        }catch (Exception e){
                            progressDialogHelper.dismissProgressDialog(VenueActivity.this);
                            System.out.println(e);
                        }

                    }
                    @Override
                    public void onError(ANError error) {
                        progressDialogHelper.dismissProgressDialog(VenueActivity.this);
                        System.out.println(error);
                    }
                });
    }
}
