package id.co.pegadaian.diarium.controller;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import id.co.pegadaian.diarium.R;
import id.co.pegadaian.diarium.adapter.ViewPagerAdapter;
import id.co.pegadaian.diarium.controller.friend.FriendsFragment;
import id.co.pegadaian.diarium.controller.home.HomeFragment;
import id.co.pegadaian.diarium.controller.home.main_menu.MoreFragment;
import id.co.pegadaian.diarium.controller.home.main_menu.myteam.MyTeamActivity;
import id.co.pegadaian.diarium.controller.home.main_menu.myteam.MyTeamFragment;
import id.co.pegadaian.diarium.controller.home.main_menu.myteam.OurTeamFragment;
import id.co.pegadaian.diarium.controller.inbox.InboxFragment;
import id.co.pegadaian.diarium.controller.notification.NotifikasiFragment;
import id.co.pegadaian.diarium.util.UserSessionManager;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;

public class HomeActivityWithViewPager extends AppCompatActivity {

    ViewPagerAdapter viewPagerAdapter;
    ViewPager viewPager;
    TabLayout tabLayout;
    UserSessionManager session;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_with_view_pager);

        session = new UserSessionManager(this);
        tabLayout = (TabLayout) findViewById(R.id.tabs_home);
        viewPager = (ViewPager) findViewById(R.id.viewpager_home);

        viewPagerAdapter = new ViewPagerAdapter(HomeActivityWithViewPager.this.getSupportFragmentManager());
        viewPagerAdapter.addFragments(new HomeFragment(),"Home");
        viewPagerAdapter.addFragments(new InboxFragment(),"Inbox");
        viewPagerAdapter.addFragments(new FriendsFragment(),"Friends");
        viewPagerAdapter.addFragments(new NotifikasiFragment(),"Notification");
        viewPagerAdapter.addFragments(new MoreFragment(),"More");
//        viewPagerAdapter.addFragments(new PaidFragment(),"Paid");
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}