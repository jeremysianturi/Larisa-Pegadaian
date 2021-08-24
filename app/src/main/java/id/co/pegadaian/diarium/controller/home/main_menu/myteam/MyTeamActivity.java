package id.co.pegadaian.diarium.controller.home.main_menu.myteam;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import id.co.pegadaian.diarium.R;
import id.co.pegadaian.diarium.adapter.ViewPagerAdapter;
import id.co.pegadaian.diarium.util.UserSessionManager;

public class MyTeamActivity extends AppCompatActivity {

    Toolbar toolbar;
    TabLayout tabLayout;
    ViewPager viewPager;
    ViewPagerAdapter viewPagerAdapter;
    UserSessionManager session;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_team);
        session = new UserSessionManager(this);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPagerAdapter = new ViewPagerAdapter(MyTeamActivity.this.getSupportFragmentManager());
        viewPagerAdapter.addFragments(new MyTeamFragment(),"My Team");
        viewPagerAdapter.addFragments(new OurTeamFragment(),"Our Team ");
//        viewPagerAdapter.addFragments(new PaidFragment(),"Paid");
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        System.out.println(session.getStatus()+"STATUSNYADALAH");
//        if (session.getStatus().equals("chief")) {
//            MenuInflater inflater = getMenuInflater();
//            inflater.inflate(R.menu.menu_add_post, menu);
//        }
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_post:
                Intent intent = new Intent(MyTeamActivity.this, AssignTodayActivity.class);
//                    intent.putExtra("name",session.getUserFullName());
//                    intent.putExtra("email","diarium@telkom.co.id");
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}

