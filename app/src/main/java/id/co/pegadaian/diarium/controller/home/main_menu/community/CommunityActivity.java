package id.co.pegadaian.diarium.controller.home.main_menu.community;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import id.co.pegadaian.diarium.R;
import id.co.pegadaian.diarium.adapter.ViewPagerAdapter;

public class CommunityActivity extends AppCompatActivity {

    Toolbar toolbar;
    TabLayout tabLayout;
    ViewPager viewPager;
    ViewPagerAdapter viewPagerAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPagerAdapter = new ViewPagerAdapter(CommunityActivity.this.getSupportFragmentManager());
        viewPagerAdapter.addFragments(new CommunityJoinFragment(),"My Community");
        viewPagerAdapter.addFragments(new CommunityUnjoinFragment(),"All Community");
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
        MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.create_community, menu);

        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Take appropriate action for each action item click
        switch (item.getItemId()) {
            case R.id.create_community:
                Intent i = new Intent(CommunityActivity.this, CreateCommunityActivity.class);
                startActivity(i);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}

