package id.co.pegadaian.diarium.controller.home.main_menu.myevent;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import id.co.pegadaian.diarium.R;
import id.co.pegadaian.diarium.adapter.EventAdapter;
import id.co.pegadaian.diarium.model.EventModel;
import id.co.pegadaian.diarium.util.UserSessionManager;
import id.co.pegadaian.diarium.util.element.ProgressDialogHelper;
import in.galaxyofandroid.spinerdialog.OnSpinerItemClick;
import in.galaxyofandroid.spinerdialog.SpinnerDialog;


public class MyEventActivity extends AppCompatActivity {
    private ProgressDialogHelper progressDialogHelper;
    private List<EventModel> listModel;
    private EventModel model;
    private EventAdapter adapter;
    TextView tvNull;
    ListView list;
    UserSessionManager session;
    Toolbar toolbar;
//    TabLayout tabLayout;
//    ViewPager viewPager;
//    ViewPagerAdapter viewPagerAdapter;
    TextView option;
    ArrayList<String> opsi= new ArrayList<>();
    SpinnerDialog spinnerDialogs;
    Typeface font,fontbold;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_event);
        session = new UserSessionManager(this);

        font = Typeface.createFromAsset(getApplication().getAssets(),"fonts/Nexa Light.otf");
        fontbold = Typeface.createFromAsset(getApplication().getAssets(),"fonts/Nexa Bold.otf");
        progressDialogHelper = new ProgressDialogHelper();


//        tabLayout = (TabLayout) findViewById(R.id.tabs);
//        viewPager = (ViewPager) findViewById(R.id.viewpager);
//        viewPagerAdapter = new ViewPagerAdapter(MyEventActivity.this.getSupportFragmentManager());
//        viewPagerAdapter.addFragments(new TematikFragment(),"Thematic");
//        viewPagerAdapter.addFragments(new NonTematikFragment(),"Non Thematic");
//        viewPagerAdapter.addFragments(new TematikFragment(),"Past");
//        viewPager.setAdapter(viewPagerAdapter);
//        tabLayout.setupWithViewPager(viewPager);

//        option = (TextView) findViewById(R.id.option);
//        option.setTypeface(fontbold);
//        LinearLayout opsii = findViewById(R.id.opsi);

        spinnerDialogs = new SpinnerDialog(MyEventActivity.this,opsi,"Public");
        spinnerDialogs.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String lantai, int position) {
                Toast.makeText(MyEventActivity.this,""+lantai,Toast.LENGTH_SHORT).show();
                option.setText(lantai);
            }
        });

//        opsii.setOnClickListener(new View.OnClickListener()
//        {
//
//            @Override
//            public void onClick(View v) {
//                spinnerDialogs.showSpinerDialog();
//
//            }
//        });

        list = findViewById(R.id.list_upcoming);
        tvNull = findViewById(R.id.tvNull);
        getEvent("T");
        list.setVisibility(View.GONE);
        tvNull.setVisibility(View.GONE);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    private void getEvent(final String type){
        progressDialogHelper.showProgressDialog(MyEventActivity.this, "Getting data...");
        AndroidNetworking.get(session.getServerURL()+"users/allevent/buscd/"+session.getUserBusinessCode())
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
                        System.out.println(response+"kjwrhbk4jrEvent");
                        try {
                            if(response.getInt("status")==200){
//                                session.setToken(response.getString("token"));
                                listModel = new ArrayList<EventModel>();
                                JSONArray jsonArray = response.getJSONArray("data");
                                if (jsonArray.length()<=0) {
                                    list.setVisibility(View.GONE);
                                    tvNull.setVisibility(View.VISIBLE);
                                } else {
                                    list.setVisibility(View.VISIBLE);
                                    tvNull.setVisibility(View.GONE);
                                    for (int a = 0; a < jsonArray.length(); a++) {
                                        JSONObject object = jsonArray.getJSONObject(a);
                                        String begin_date = object.getString("begin_date");
                                        String end_date = object.getString("end_date");
                                        String business_code = object.getString("business_code");
                                        String personal_number = object.getString("personal_number");
                                        String id = object.getString("event_id");
                                        String code = object.getString("code");
                                        String event_type = object.getString("event_type");
                                        String theme = object.getString("theme");
                                        String name = object.getString("name");
                                        String description = object.getString("description");
                                        String location = object.getString("location");
                                        String city = object.getString("city");
                                        String phone = object.getString("phone");
                                        String link = object.getString("link");
                                        String event_start = object.getString("event_start");
                                        String event_end = object.getString("event_end");
                                        String change_date = object.getString("change_date");
                                        String change_user = object.getString("change_user");
                                        String image = object.getString("image");
                                        System.out.println(begin_date+"dl2j3nr3");
                                        model = new EventModel(begin_date, end_date, business_code, personal_number, id, code, event_type, theme, name, description, location, city, phone, link, event_start, event_end, change_date, change_user, image);
                                        listModel.add(model);
                                    }
                                    adapter = new EventAdapter(MyEventActivity.this, listModel, type);
                                    list.setAdapter(adapter);
                                }
                                progressDialogHelper.dismissProgressDialog(MyEventActivity.this);
                            }else{
//                                popUpLogin();
                                progressDialogHelper.dismissProgressDialog(MyEventActivity.this);
                            }
                        }catch (Exception e){
                            progressDialogHelper.dismissProgressDialog(MyEventActivity.this);
                            System.out.println(e);
                        }

                    }
                    @Override
                    public void onError(ANError error) {
                        progressDialogHelper.dismissProgressDialog(MyEventActivity.this);
                        System.out.println(error);
                    }
                });
    }

}
