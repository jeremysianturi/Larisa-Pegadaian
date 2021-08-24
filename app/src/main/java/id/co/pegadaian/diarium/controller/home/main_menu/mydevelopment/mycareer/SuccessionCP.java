package id.co.pegadaian.diarium.controller.home.main_menu.mydevelopment.mycareer;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.squareup.picasso.Picasso;

import id.co.pegadaian.diarium.R;
import id.co.pegadaian.diarium.adapter.ViewPagerAdapter;
import id.co.pegadaian.diarium.util.UserSessionManager;
import id.co.pegadaian.diarium.util.element.ProgressDialogHelper;

public class SuccessionCP extends AppCompatActivity {

  private UserSessionManager session;
  private ProgressDialogHelper progressDialogHelper;
  TabLayout tabLayout;
  ViewPager viewPager;
  ViewPagerAdapter viewPagerAdapter;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_succession_cp);

    tabLayout = findViewById(R.id.tabs);
    viewPager = findViewById(R.id.viewpager);
    session = new UserSessionManager(this);
    progressDialogHelper = new ProgressDialogHelper();


    Intent scp = getIntent();
    String nama = scp.getStringExtra("nama");
    String jabatan = scp.getStringExtra("jabatan");
    String band = scp.getStringExtra("band");
    String company_unit = scp.getStringExtra("nama");
    String comp = scp.getStringExtra("comp");
    String url = scp.getStringExtra("url");

    ImageView iv_profile = findViewById(R.id.career_profile);
    Picasso.get()
            .load(url)
            .error(R.drawable.profile)
            .into(iv_profile);


    TextView tv1 = findViewById(R.id.tv1);
    TextView tv2 = findViewById(R.id.tv2);
    TextView tv3 = findViewById(R.id.tv3);
    TextView tv4 = findViewById(R.id.tv4);
    TextView tv5 = findViewById(R.id.tv5);

    tv1.setText(nama);
    tv2.setText(jabatan);
    tv3.setText(band);
    tv4.setText(company_unit);
    tv5.setText(comp);

    viewPagerAdapter = new ViewPagerAdapter(SuccessionCP.this.getSupportFragmentManager());
    viewPagerAdapter.addFragments(new Promosi_SCP_Fragment(),"promosi");
    viewPagerAdapter.addFragments(new Rotasi_SCP_Fragment(),"rotasi");
//    viewPagerAdapter.addFragments(new AbsenFragment(),"Absensi");
    viewPager.setAdapter(viewPagerAdapter);
    tabLayout.setupWithViewPager(viewPager);



    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    getSupportActionBar().setTitle("Succession Career Plan");
  }


  @Override
  public boolean onSupportNavigateUp(){
    finish();
    return true;
  }
}
