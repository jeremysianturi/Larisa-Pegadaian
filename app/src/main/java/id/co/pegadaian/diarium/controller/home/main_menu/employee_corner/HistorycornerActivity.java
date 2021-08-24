package id.co.pegadaian.diarium.controller.home.main_menu.employee_corner;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import id.co.pegadaian.diarium.R;
import id.co.pegadaian.diarium.adapter.ViewPagerAdapter;
import id.co.pegadaian.diarium.model.EmployeeCornerThemeModel;
import id.co.pegadaian.diarium.util.UserSessionManager;
import in.galaxyofandroid.spinerdialog.SpinnerDialog;

public class HistorycornerActivity extends AppCompatActivity {

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
    Spinner spinnerTheme;
    LinearLayout opsii;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historycorner);

        session = new UserSessionManager(this);
        font = Typeface.createFromAsset(getApplication().getAssets(),"fonts/Nexa Light.otf");
        fontbold = Typeface.createFromAsset(getApplication().getAssets(),"fonts/Nexa Bold.otf");

        spinnerTheme = (Spinner) findViewById(R.id.spinnerTheme);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        viewPager = (ViewPager) findViewById(R.id.viewpager);

        viewPagerAdapter = new ViewPagerAdapter(HistorycornerActivity.this.getSupportFragmentManager());
        viewPagerAdapter.addFragments(new AvailableFragment(),"Available");
        viewPagerAdapter.addFragments(new ECCheckInFragment(),"Checkin");
        viewPagerAdapter.addFragments(new ECNotCheckInFragment(),"Not CheckIn" );
        viewPagerAdapter.addFragments(new ECCanceledFragment(),"Canceled");

        opsii = findViewById(R.id.layout_corner);
        getEcTheme();
//        Toast.makeText(this, "theme id : "+session.getThemeId(), Toast.LENGTH_SHORT).show();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void getEcTheme(){
        ArrayList<String> list;
        String result = "";
        SimpleDateFormat tgl = new SimpleDateFormat("yyyy-MM-dd");
        String tRes = tgl.format(new Date());
        AndroidNetworking.get(session.getServerURL()+"users/showalltheme/buscd/"+session.getUserBusinessCode())
                .addHeaders("Accept","application/json")
                .addHeaders("Content-Type","application/json")
                .addHeaders("Authorization",session.getToken())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        final ArrayList<EmployeeCornerThemeModel> contacts = new ArrayList<>();
                        System.out.println(response+"hiewuhruTheme");
                        try {
                            if(response.getInt("status")==200){
                                JSONArray jsonArray = response.getJSONArray("data");
                                if (jsonArray.length()==0) {
                                } else {
                                    for (int a = 0; a < jsonArray.length(); a++) {
                                        JSONObject object = jsonArray.getJSONObject(a);
                                        String begin_date = object.getString("begin_date");
                                        String end_date = object.getString("end_date");
                                        String business_code = object.getString("business_code");
                                        String theme_id = object.getString("theme_id");
                                        String theme_name = object.getString("theme_name");
                                        String image = object.getString("image");
                                        String change_date = object.getString("change_date");
                                        String change_user = object.getString("change_user");
                                        contacts.add(new EmployeeCornerThemeModel(begin_date, end_date, business_code, theme_id, theme_name, image, change_date, change_user));
                                    }

                                    for (int i =0; i<contacts.size(); i++) {
                                        list_tema.add(contacts.get(i).getTheme_name());
                                    }

                                    ArrayAdapter<String> adp1 = new ArrayAdapter<String>(HistorycornerActivity.this,
                                            android.R.layout.simple_spinner_dropdown_item, list_tema);
                                    adp1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                    spinnerTheme.setAdapter(adp1);
                                    spinnerTheme.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
                                    {
                                        @Override
                                        public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {
                                            // TODO Auto-generated method stub
//                                            Toast.makeText(HistorycornerActivity.this, contacts.get(position).getTheme_name()+contacts.get(position).getTheme_id(), Toast.LENGTH_SHORT).show();
                                            session.setThemeId(contacts.get(position).getTheme_id());
                                            viewPagerAdapter = new ViewPagerAdapter(HistorycornerActivity.this.getSupportFragmentManager());
                                            viewPagerAdapter.addFragments(new AvailableFragment(),"Available");
                                            viewPagerAdapter.addFragments(new ECCheckInFragment(),"Checkin");
                                            viewPagerAdapter.addFragments(new ECNotCheckInFragment(),"Not CheckIn" );
                                            viewPagerAdapter.addFragments(new ECCanceledFragment(),"Canceled");
                                            viewPager.setAdapter(viewPagerAdapter);
                                            tabLayout.setupWithViewPager(viewPager);
                                            viewPagerAdapter.notifyDataSetChanged();
                                        }
                                        @Override
                                        public void onNothingSelected(AdapterView<?> arg0) {
                                            // TODO Auto-generated method stub
                                        }
                                    });



//                                    ArrayAdapter<EmployeeCornerThemeModel> adapter =
//                                            new ArrayAdapter<EmployeeCornerThemeModel>(getApplicationContext(),  android.R.layout.simple_spinner_dropdown_item, contacts);
//                                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                                    spinnerTheme.setAdapter(adapter);
//                                    spinnerTheme.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//                                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                                            String name = contacts.get(position).getTheme_name();
//                                            String id_contact = contacts.get(position).getTheme_id();
//                                            Toast.makeText(HistorycornerActivity.this, "Isi "+name+" - "+id_contact, Toast.LENGTH_SHORT).show();
//                                        }
//                                        public void onNothingSelected(AdapterView<?> parent) {
//                                        }
//                                    });
                                }
                            }else{
                            }
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

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
}
