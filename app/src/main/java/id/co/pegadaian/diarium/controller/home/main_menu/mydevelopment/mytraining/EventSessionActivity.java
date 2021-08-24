package id.co.pegadaian.diarium.controller.home.main_menu.mydevelopment.mytraining;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;
import com.google.android.material.tabs.TabLayout;

import java.util.List;

import id.co.pegadaian.diarium.R;
import id.co.pegadaian.diarium.adapter.EventSessionAdapter;
import id.co.pegadaian.diarium.adapter.ViewPagerAdapter;
import id.co.pegadaian.diarium.model.EventSessionModel;
import id.co.pegadaian.diarium.util.TimeHelper;
import id.co.pegadaian.diarium.util.UserSessionManager;
import id.co.pegadaian.diarium.util.element.ProgressDialogHelper;

public class EventSessionActivity extends AppCompatActivity {
    TabLayout tabLayout;
    ViewPager viewPager;
    ViewPagerAdapter viewPagerAdapter;

    TextView tvEvent, tvBatch, tvLocation, tvType, tvCurriculum, tvDate, tvCompany;
    private ProgressDialogHelper progressDialogHelper;
    UserSessionManager session;
    private List<EventSessionModel> listModel;
    private EventSessionModel model;
    private EventSessionAdapter adapter;
    ListView listEventSession;
    Typeface font,fontbold;
    TimeHelper timeHelper;
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

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_session);
        timeHelper = new TimeHelper();
        progressDialogHelper = new ProgressDialogHelper();
        session = new UserSessionManager(EventSessionActivity.this);
        font = Typeface.createFromAsset(EventSessionActivity.this.getAssets(),"fonts/Nexa Light.otf");
        fontbold = Typeface.createFromAsset(EventSessionActivity.this.getAssets(),"fonts/Nexa Bold.otf");
        tvEvent = findViewById(R.id.tvEvent);
        tvBatch = findViewById(R.id.tvBatch);
        tvLocation = findViewById(R.id.tvLocation);
        tvType = findViewById(R.id.tvType);
        tvCurriculum = findViewById(R.id.tvCurriculum);
        tvDate = findViewById(R.id.tvDate);
        tvCompany = findViewById(R.id.tvCompany);
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

        session.setBatchLMS(batch);
        session.setBatchNameLMS(batch_name);
        session.setEventIdLMS(event_id);
        session.setEventNameLMS(event_name);
        session.setBeginDateLMS(begin_date);
        session.setEndDateLMS(end_date);
        session.setevent_curr_statLMS(event_curr_stat);
        session.setevnt_curr_statidLMS(evnt_curr_statid);
        session.setevent_statusLMS(event_status);
        session.setevent_stat_idLMS(event_stat_id);
        session.setlocation_idLMS(location_id);
        session.setlocationLMS(location);
        session.setcur_idLMS(cur_id);
        session.setcurriculumLMS(curriculum);
        session.setevent_typeLMS(event_type);
        session.setparticipant_idLMS(participant_id);
        session.setpartcipant_nameLMS(partcipant_name);
        session.setparti_nicknmLMS(parti_nicknm);
        session.setcompany_nameLMS(company_name);

        tvEvent.setText(event_name);
        tvBatch.setText(batch+ " "+ batch_name);
        tvLocation.setText(location);
        tvType.setText(event_type+" Event");
        tvDate.setText(timeHelper.getTimeFormat(begin_date)+" - "+timeHelper.getTimeFormat(end_date));
        tvEvent.setText(event_name);
        tvCompany.setText(company_name+" - "+location_id);
        tvCurriculum.setText(curriculum);

        Bundle bundle = new Bundle();
        bundle.putString("batch", batch);
        OnGoingEventFragment fragobj = new OnGoingEventFragment();
        fragobj.setArguments(bundle);

        viewPagerAdapter = new ViewPagerAdapter(EventSessionActivity.this.getSupportFragmentManager());
        viewPagerAdapter.addFragments(new ActivityFragment(),"Session");
        viewPagerAdapter.addFragments(new ForumFragment(),"Forum");
        viewPagerAdapter.addFragments(new InsightFragment(),"Insight");
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Activity");
    }
    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

}
