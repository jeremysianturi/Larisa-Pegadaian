package id.co.pegadaian.diarium.controller.home.main_menu.employee_care;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import id.co.pegadaian.diarium.R;
import id.co.pegadaian.diarium.adapter.ViewPagerAdapter;
import id.co.pegadaian.diarium.util.UserSessionManager;

public class EmployeeCareActivity extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager viewPager;
    ViewPagerAdapter viewPagerAdapter;
    Typeface font,fontbold;
    UserSessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_care);


        session = new UserSessionManager(this);
        font = Typeface.createFromAsset(getApplication().getAssets(),"fonts/Nexa Light.otf");
        fontbold = Typeface.createFromAsset(getApplication().getAssets(),"fonts/Nexa Bold.otf");

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        viewPager = (ViewPager) findViewById(R.id.viewpager);

        viewPagerAdapter = new ViewPagerAdapter(EmployeeCareActivity.this.getSupportFragmentManager());
        viewPagerAdapter.addFragments(new NewCareFragment(),"New Issue");
        viewPagerAdapter.addFragments(new InProgressFragment(),"In Progress");
        viewPagerAdapter.addFragments(new ClosedFragment(),"Closed");
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Employee Care");
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_add_post, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_post:
                Intent intent = new Intent(EmployeeCareActivity.this, AddNewCareActivity.class);
//                    intent.putExtra("name",session.getUserFullName());
//                    intent.putExtra("email","diarium@telkom.co.id");
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


}
