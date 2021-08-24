package id.co.pegadaian.diarium.controller.home.main_menu.mydevelopment.mycareer;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import id.co.pegadaian.diarium.R;
import id.co.pegadaian.diarium.adapter.ViewPagerAdapter;
import id.co.pegadaian.diarium.util.UserSessionManager;

public class EmployeeCP extends AppCompatActivity {

  private UserSessionManager session;
  TabLayout tabLayout;
  ViewPager viewPager;
  ViewPagerAdapter viewPagerAdapter;
  String nama,nik,posisi, jabatan, band, company, comapany_name, company_unit;
  private TextView tv_nama, tv_unit, tv_jabatan, tv_band, tv_company, tv_company_lokasi, tv_career_nik, tv_5;
//  private MyCareerActivityModel model;
//  private List<MyCareerActivityModel> listmodel;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_employee_cp);

    tabLayout = findViewById(R.id.tabs);
    viewPager = findViewById(R.id.viewpager);
    session = new UserSessionManager(this);
//    AndroidNetworking.initialize(getApplicationContext());
//    AndroidNetworking.setParserFactory(new JacksonParserFactory());
//
//    Intent data = getIntent();
//
//    String nama = data.getStringExtra("nama");
//    String nik = data.getStringExtra("nik");
//    String unit = data.getStringExtra("unit");
//    String jabatan = data.getStringExtra("jabatan");
//    String band = data.getStringExtra("band");
//    String company_name = data.getStringExtra("company_name");
//    String company_unit = data.getStringExtra("company_unit");
//    String url = data.getStringExtra("url");
//    String comp = data.getStringExtra("comp");
//
//
//
//
//    tv_nama = findViewById(R.id.career_name);
//    tv_nama.setText(nama);
//
//    tv_jabatan = findViewById(R.id.career_jabatan);
//    tv_jabatan.setText(jabatan);
//
//    tv_band = findViewById(R.id.career_band);
//    tv_band.setText(band);
//
//    tv_company = findViewById(R.id.career_company_name);
//    tv_company.setText(company_unit);
//
//    tv_5 = findViewById(R.id.tv_5);
//    tv_5.setText(comp);
//
//    ImageView iv_profile = findViewById(R.id.career_profile);
//    Picasso.get().load(url).error(R.drawable.profile).into(iv_profile);


//    tv_company.setText(model.getNama_perusahaan());
//    tv_company_lokasi.setText(model.getLokasi_kantor());
//    tv_job.setText(model.getJob());
//    tv_nama.setText(model.getNamaLengkap());
//    tv_career_nik.setText(model.getNIK());

//    System.out.println("employeeDATACareer"+ model.getNamaLengkap());

    viewPagerAdapter = new ViewPagerAdapter(EmployeeCP.this.getSupportFragmentManager());
    viewPagerAdapter.addFragments(new PromosiECP_Fragment(),"promosi");
    viewPagerAdapter.addFragments(new RotasiECP_Fragment(),"rotasi");
//    viewPagerAdapter.addFragments(new AbsenFragment(),"Absensi");
    viewPager.setAdapter(viewPagerAdapter);
    tabLayout.setupWithViewPager(viewPager);


    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    getSupportActionBar().setTitle("Employee Career Plan");
  }

  @Override
  public boolean onSupportNavigateUp(){
    finish();
    return true;
  }
}
