package id.co.pegadaian.diarium.controller.home.main_menu.mydevelopment.mytraining;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;

import id.co.pegadaian.diarium.R;
import id.co.pegadaian.diarium.adapter.ViewPagerAdapter;
import id.co.pegadaian.diarium.util.TimeHelper;
import id.co.pegadaian.diarium.util.UserSessionManager;

public class ScheduleActivity extends AppCompatActivity {
    TabLayout tabLayout;
    ViewPager viewPager;
    ViewPagerAdapter viewPagerAdapter;

    String  bussines_code;
    String  batch;
    String  batch_name;
    String  event_id;
    String  event_name;
    String  begin_date;
    String  end_date;
    String  event_curr_stat;
    String  evnt_curr_statid;
    String  event_status;
    String  event_stat_id;
    String  location_id;
    String  location;
    String  cur_id;
    String  curriculum;
    String  event_type;
    String  participant_id;
    String  partcipant_name;
    String  parti_nicknm;
    String  company_name;
    String  activity_name;
    String  session_name;
    String  begin_date_activity;
    String  end_date_activity;
    TextView tvEvent, tvBatch, tvLocation, tvType, tvCurriculum, tvDate, tvActivitName, tvSessionName, tvDateActivity, tvCompany;
    ListView listView;
    TimeHelper timeHelper;
    Typeface font,fontbold;
    UserSessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        timeHelper = new TimeHelper();
        font = Typeface.createFromAsset(ScheduleActivity.this.getAssets(),"fonts/Nexa Light.otf");
        fontbold = Typeface.createFromAsset(ScheduleActivity.this.getAssets(),"fonts/Nexa Bold.otf");
        tvEvent = findViewById(R.id.tvEvent);
        tvBatch = findViewById(R.id.tvBatch);
        tvLocation = findViewById(R.id.tvLocation);
        tvType = findViewById(R.id.tvType);
        tvCurriculum = findViewById(R.id.tvCurriculum);
        tvDate = findViewById(R.id.tvDate);
        tvActivitName = findViewById(R.id.tvActivityName);
        tvSessionName = findViewById(R.id.tvSessionName);
        tvDateActivity = findViewById(R.id.tvDateActivity);
        tvCompany = findViewById(R.id.tvCompany);
        session = new UserSessionManager(this);
        tabLayout = findViewById(R.id.tabs);
        viewPager = findViewById(R.id.viewpager);

        Intent intent = getIntent();
        bussines_code = intent.getStringExtra("bussines_code");
        batch = intent.getStringExtra("batch");
        batch_name = intent.getStringExtra("batch_name");
        event_id = intent.getStringExtra("event_id");
        event_name = intent.getStringExtra("event_name");
        begin_date = intent.getStringExtra("begin_date");
        end_date = intent.getStringExtra("end_date");
        event_curr_stat = intent.getStringExtra("event_curr_stat");
        evnt_curr_statid = intent.getStringExtra("evnt_curr_statid");
        event_status = intent.getStringExtra("event_status");
        event_stat_id = intent.getStringExtra("event_stat_id");
        location_id = intent.getStringExtra("location_id");
        location = intent.getStringExtra("location");
        cur_id = intent.getStringExtra("cur_id");
        curriculum = intent.getStringExtra("curriculum");
        event_type = intent.getStringExtra("event_type");
        participant_id = intent.getStringExtra("participant_id");
        partcipant_name = intent.getStringExtra("partcipant_name");
        parti_nicknm = intent.getStringExtra("parti_nicknm");
        company_name = intent.getStringExtra("company_name");

        activity_name = intent.getStringExtra("activity_name");
        session_name = intent.getStringExtra("session_name");
        begin_date_activity = intent.getStringExtra("begin_date_activity");
        end_date_activity = intent.getStringExtra("end_date_activity");

        session.setactivity_nameLMS(activity_name);
        session.setsession_nameLMS(session_name);
        session.setbegin_date_activityLMS(begin_date_activity);
        session.setend_date_activityLMS(end_date_activity);

        tvEvent.setText(event_name);
        tvBatch.setText(batch+ " "+ batch_name);
        tvLocation.setText(location);
        tvType.setText(event_type+" Event");
        tvDate.setText(timeHelper.getTimeFormat(begin_date)+" - "+timeHelper.getTimeFormat(end_date));
        tvEvent.setText(event_name);
        tvCompany.setText(company_name+" - "+location_id);
        tvCurriculum.setText(curriculum);

        tvActivitName.setText(activity_name);
        tvSessionName.setText(session_name);
        tvDateActivity.setText(timeHelper.getTimeFormat(begin_date_activity)+" - "+timeHelper.getTimeFormat(end_date_activity));

        viewPagerAdapter = new ViewPagerAdapter(ScheduleActivity.this.getSupportFragmentManager());
        viewPagerAdapter.addFragments(new ScheduleFragment(),"Schedule");
        viewPagerAdapter.addFragments(new MentoringFragment(),"Mentoring");
        viewPagerAdapter.addFragments(new AbsenFragment(),"Absensi");
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Schedule");
    }
    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }



}
