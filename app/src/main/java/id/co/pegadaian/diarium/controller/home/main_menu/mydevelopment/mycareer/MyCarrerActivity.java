package id.co.pegadaian.diarium.controller.home.main_menu.mydevelopment.mycareer;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import id.co.pegadaian.diarium.R;
import id.co.pegadaian.diarium.model.MyCareerActivityModel;
import id.co.pegadaian.diarium.model.Mycarrer_bandModel;
import id.co.pegadaian.diarium.model.Mycarrer_personaldataModel;
import id.co.pegadaian.diarium.util.UserSessionManager;
import id.co.pegadaian.diarium.util.element.ProgressDialogHelper;

public class MyCarrerActivity extends AppCompatActivity {

  LinearLayout layout_ecp, layout_mcp, layout_scp;
  private UserSessionManager session;
  private List<MyCareerActivityModel> listmodel;
  private List<Mycarrer_bandModel> listmodel2;
  private List<Mycarrer_personaldataModel> listmodel3;
  private MyCareerActivityModel model;
  private Mycarrer_bandModel model2;
  private Mycarrer_personaldataModel model3;
  private ProgressDialogHelper progressDialogHelper;
  private TextView tv_nama, tv_job, tv_jabatan, tv_band, tv_company, tv_company_lokasi, tv_career_nik, tv_5;
  private ImageView iv_profile;
//  ServiceGeneratorCollege serviceGeneratorCollege;

  private String Band;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_my_carrer);


//    serviceGeneratorCollege = new ServiceGeneratorCollege();

    session = new UserSessionManager(this);
    progressDialogHelper = new ProgressDialogHelper();

    layout_ecp = findViewById(R.id.layout_ecp);
    layout_mcp = findViewById(R.id.layout_mcp);
    layout_scp = findViewById(R.id.layout_scp);
//    tv_company = findViewById(R.id.company_name);
//    tv_job = findViewById(R.id.career_job);
//    tv_career_nik = findViewById(R.id.career_nik);
    tv_company_lokasi = findViewById(R.id.career_company_place);
    tv_nama = findViewById(R.id.career_name);
    tv_band = findViewById(R.id.band);
    tv_jabatan = findViewById(R.id.jabatan);
    tv_5 = findViewById(R.id.tv_5);

    iv_profile = findViewById(R.id.career_profile);

//    tv_nama.setText(session.getUserFullName());

    System.out.println("fulnameCareer "+session.getUserFullName());

    layout_ecp.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Intent ecp = new Intent(MyCarrerActivity.this,EmployeeCP.class);
        ecp.putExtra("nama",model.getNamaLengkap()); // nama
        ecp.putExtra("nik",model.getNIK()); // x
        ecp.putExtra("unit",model.getJob());
        ecp.putExtra("jabatan",model.getJob()); // jabatan
        ecp.putExtra("band",model2.getBand());  // band
        ecp.putExtra("company_name",model.getNama_perusahaan()); // company name
        ecp.putExtra("company_unit",model.getLokasi_kantor());  // lcompany unit
        ecp.putExtra("url",model3.getUrl_foto()); // image iv_profile
        ecp.putExtra("comp",model3.getCompany_career_activity()); // company - tv5
        startActivity(ecp);
      }
    });
    layout_mcp.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Intent mcp = new Intent(MyCarrerActivity.this,ManagementCP.class);
        mcp.putExtra("nama",model.getNamaLengkap());
        mcp.putExtra("jabatan",model.getJob());
        mcp.putExtra("band",model2.getBand());
        mcp.putExtra("company_unit",model.getLokasi_kantor());
        mcp.putExtra("comp",model3.getCompany_career_activity());
        mcp.putExtra("url",model3.getUrl_foto());
        startActivity(mcp);
      }
    });
    layout_scp.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Intent scp = new Intent(MyCarrerActivity.this,SuccessionCP.class);
        scp.putExtra("nama",model.getNamaLengkap());
        scp.putExtra("jabatan",model.getJob());
        scp.putExtra("band",model2.getBand());
        scp.putExtra("company_unit",model.getLokasi_kantor());
        scp.putExtra("comp",model3.getCompany_career_activity());
        scp.putExtra("url",model3.getUrl_foto());
        startActivity(scp);
      }
    });


    getLevel();
    getMycareerData();
    getPersonalData();
//    getJabatan();
//    getTokenLdap();
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    getSupportActionBar().setTitle("My Career Plan");
  }

  private void getPersonalData() {
    AndroidNetworking.get("https://testapi.digitalevent.id/ldap/api/auth/account?include=173122013")
            .addHeaders("Accept","application/json")
            .addHeaders("Content-Type","application/json")
            .addHeaders("Authorization","Bearer "+session.getTokenMyCareer())
            .setPriority(Priority.MEDIUM)
            .build()
            .getAsJSONObject(new JSONObjectRequestListener() {
              @Override
              public void onResponse(JSONObject response) {
                System.out.println("personal_data "+response);
                try {
                  listmodel3 = new ArrayList<Mycarrer_personaldataModel>();
//                  if (response!=null) {
                  String comp = response.getJSONObject("data").getString("company");
                  String url = response.getJSONObject("data").getJSONObject("avatar").getString("avatar");

                  System.out.println("URL "+url);
                  System.out.println(comp);

                  model3 = new Mycarrer_personaldataModel(url,comp);
                  listmodel3.add(model3);

                  tv_5.setText(model3.getCompany_career_activity());

                  Picasso.get()
                          .load(model3.getUrl_foto())
                          .error(R.drawable.profile)
                          .into(iv_profile);


//                  }

                } catch (JSONException e) {
                  e.printStackTrace();
                }
              }

              @Override
              public void onError(ANError anError) {
                System.out.println(anError);

              }
            });
  }

  private void getLevel() {
    AndroidNetworking.get(session.getServerMyCareer()+"api/basic-level?&personnel_number[]=173122013&business_code[]=1200")
            .addHeaders("Accept","application/json")
            .addHeaders("Content-Type","application/json")
            .addHeaders("Authorization","Bearer "+session.getTokenMyCareer())
            .setPriority(Priority.MEDIUM)
            .build()
            .getAsJSONObject(new JSONObjectRequestListener() {
              @Override
              public void onResponse(JSONObject response) {
                System.out.println("getLEVEL_response"+response);
                try {
                  listmodel2 = new ArrayList<Mycarrer_bandModel>();
//                  if (response!=null) {
                    String band = response.getJSONArray("data").getJSONObject(0).getString("complete_name");

                    model2 = new Mycarrer_bandModel(band);
                    listmodel2.add(model2);

                    System.out.println("getLevel " + band);
                    System.out.println("getLevel " +model2.getBand());

                    Band = band;

                    tv_band.setText(model2.getBand());

//                  }

                } catch (JSONException e) {
                  e.printStackTrace();
                }
              }

              @Override
              public void onError(ANError anError) {
                System.out.println(anError);

              }
            });


  }


  private void getMycareerData() {
    progressDialogHelper.showProgressDialog(MyCarrerActivity.this,"Getting Data....");
    System.out.println("masukMycareer");
    AndroidNetworking.get(session.getServerMyCareer()+"api/organization-assignment?end_date_gte=9999-12-31&personnel_number[]=173122013")
//    AndroidNetworking.get(session.getServerMyCareer()+"api/qualifications?business_code[]=1200&personnel_number[]=173122013&per_page=1&parent_type[]=JOBFN&child_type[]=JOBFN&end_date_gte=9999-12-31")
            .addHeaders("Accept","application/json")
            .addHeaders("Content-Type","application/json")
            .addHeaders("Authorization","Bearer "+session.getTokenMyCareer())
            .setPriority(Priority.MEDIUM)
            .build()
            .getAsJSONObject(new JSONObjectRequestListener() {
              @Override
              public void onResponse(JSONObject response) {
                System.out.println("MyCareer" + response);
                try{
                  listmodel = new ArrayList<MyCareerActivityModel>();
//                  JSONArray jsonArray = response.getJSONArray("data");
//                  if (response!=null){
//                   for (int i = 0; i<jsonArray.length(); i++){
//                     JSONObject object = jsonArray.getJSONObject(i);

                     String jabatan = response.getJSONArray("data").getJSONObject(0).getJSONObject("position").getString("organization_name");
                     String company_address = response.getJSONArray("data").getJSONObject(0).getJSONObject("personnel_area").getString("value");
                     String name = response.getJSONArray("data").getJSONObject(0).getJSONObject("personnel_number").getString("complete_name");
//                     String name  = object.getJSONObject("personnel_number").getString("complete_name");
//                     String job_function = object.getJSONObject("parent").getString("value");


                     model = new MyCareerActivityModel(name,jabatan,"","",company_address,"","");
                     listmodel.add(model);

                  System.out.println("testCOmanyMane "+ jabatan);
                  System.out.println("testCOmanyMane "+ company_address);
                  System.out.println("testCOmanyMane "+ name);


//                     System.out.println("comapnyNameMycareer "+company_name);
//                   }
                  tv_jabatan.setText(model.getJob());
                  tv_nama.setText(model.getNamaLengkap());
                  tv_company_lokasi.setText(model.getLokasi_kantor());
//                    tv_company.setText(model.getNama_perusahaan());
//                    tv_company_lokasi.setText(model.getLokasi_kantor());
//                    tv_company_lokasi.setVisibility(View.GONE);
//                    tv_job.setText(model.getJob());
//                    tv_nama.setText(model.getNamaLengkap());
//                    tv_career_nik.setText(model.getNIK());
                    System.out.println("cekResponseMycareer"+model.getNamaLengkap());

//                    System.out.println("cekISIDATAMODEL"+" "+model.getNama_perusahaan());
                  progressDialogHelper.dismissProgressDialog(MyCarrerActivity.this);

                } catch (JSONException e) {
                  progressDialogHelper.dismissProgressDialog(MyCarrerActivity.this);

                  e.printStackTrace();
                }

              }
              @Override
              public void onError(ANError anError) {
                System.out.println("erorResponse"+anError.getResponse());
                progressDialogHelper.dismissProgressDialog(MyCarrerActivity.this);

              }
            });

  }




  @Override
  public boolean onSupportNavigateUp(){
    finish();
    return true;
  }
}
