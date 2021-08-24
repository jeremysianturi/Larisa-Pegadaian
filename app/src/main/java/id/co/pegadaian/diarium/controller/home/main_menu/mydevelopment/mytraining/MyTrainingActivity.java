package id.co.pegadaian.diarium.controller.home.main_menu.mydevelopment.mytraining;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import id.co.pegadaian.diarium.R;
import id.co.pegadaian.diarium.adapter.ViewPagerAdapter;
import id.co.pegadaian.diarium.model.EmployeeCornerThemeModel;
import id.co.pegadaian.diarium.util.UserSessionManager;
import in.galaxyofandroid.spinerdialog.SpinnerDialog;

public class MyTrainingActivity extends AppCompatActivity {
    Toolbar toolbar;
    TabLayout tabLayout;
    ViewPager viewPager;
    ViewPagerAdapter viewPagerAdapter;
    final List<String> list_tema = new ArrayList<String>();
    ArrayList<EmployeeCornerThemeModel> opsi= new ArrayList<>();
    Map<String,String> myTheme = new HashMap<String,String>();
    ArrayList<EmployeeCornerThemeModel> ECTheme = new ArrayList<>();
    ArrayList<EmployeeCornerThemeModel> modelsTheme;
    ArrayAdapter<EmployeeCornerThemeModel> adapter;
    SpinnerDialog spinnerDialog;
    TextView choosecorner;
    Typeface font,fontbold;
    UserSessionManager session;
    LinearLayout opsii;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_training);

        session = new UserSessionManager(this);
        font = Typeface.createFromAsset(getApplication().getAssets(),"fonts/Nexa Light.otf");
        fontbold = Typeface.createFromAsset(getApplication().getAssets(),"fonts/Nexa Bold.otf");

        tabLayout = findViewById(R.id.tabs);
        viewPager = findViewById(R.id.viewpager);

        viewPagerAdapter = new ViewPagerAdapter(MyTrainingActivity.this.getSupportFragmentManager());
        viewPagerAdapter.addFragments(new OnGoingEventFragment(),"On Going");
        viewPagerAdapter.addFragments(new UpcomingEventFragment(),"Upcoming");
        viewPagerAdapter.addFragments(new EventCompleteFragment(),"Complete");
        if (session.getRoleLMS().equals("TRAINER")) {
            viewPagerAdapter.addFragments(new ConfirmEventFragment(),"Not Approved");
            viewPagerAdapter.addFragments(new PendingEventFragment(),"Pending");
        }
//                viewPagerAdapter.addFragments(new Event_ListFragment(),"Event List");
//        viewPagerAdapter.addFragments(new PaidFragment(),"Paid");
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        getTokenLdap();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("My Training");
    }
    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    private void getTokenLdap(){
        System.out.println("masuktokenldap");
        JSONObject body = new JSONObject();
        try {
            body.put("application_id","2");
            body.put("username","adminLMS");
            body.put("password","secret");

        }catch (JSONException e){
            System.out.println(e);
        }

        AndroidNetworking.post("https://testapi.digitalevent.id/ldap/api/auth/login")
                .addHeaders("Accept","application/json")
                .addHeaders("Content-Type","application/json")
                .addJSONObjectBody(body)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // do anything with response
                        System.out.println("RESPONTOKENLDAPLMS"+response);
                        try {
                            String token_ldap = response.getString("access_token");
                            session.setTokenLdap(token_ldap);
                        }catch (Exception e){
                            System.out.println(e);
                        }

                    }
                    @Override
                    public void onError(ANError error) {
                        System.out.println(error);
                    }
                });
    }
}
