package id.co.pegadaian.diarium.controller.home.main_menu.mydevelopment.myknowledge;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
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

public class MyKnowledgeActivity extends AppCompatActivity {
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
        setContentView(R.layout.activity_my_knowledge);

        session = new UserSessionManager(this);
        font = Typeface.createFromAsset(getApplication().getAssets(),"fonts/Nexa Light.otf");
        fontbold = Typeface.createFromAsset(getApplication().getAssets(),"fonts/Nexa Bold.otf");

        tabLayout = findViewById(R.id.tabs);
        viewPager = findViewById(R.id.viewpager);

        viewPagerAdapter = new ViewPagerAdapter(this.getSupportFragmentManager());
        viewPagerAdapter.addFragments(new NewContentFragment(),"New Uploaded");
        viewPagerAdapter.addFragments(new PopularContentFragment(),"Most Popular");
        viewPagerAdapter.addFragments(new SuggestedContentFragment(),"Suggested");
//                viewPagerAdapter.addFragments(new Event_ListFragment(),"Event List");
//        viewPagerAdapter.addFragments(new PaidFragment(),"Paid");
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        getTokenLdap();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Knowledge Management");
    }
    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_favorite) {
            Intent intent = new Intent(MyKnowledgeActivity.this, SearchContentActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
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
