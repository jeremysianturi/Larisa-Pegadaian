package id.co.pegadaian.diarium.controller.home.main_menu.mydevelopment.mycareer;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import id.co.pegadaian.diarium.R;
import id.co.pegadaian.diarium.adapter.ManajementCPAdapter;
import id.co.pegadaian.diarium.model.ManajementCPModel;
import id.co.pegadaian.diarium.util.UserSessionManager;
import id.co.pegadaian.diarium.util.element.ProgressDialogHelper;

public class ManagementCP extends AppCompatActivity {

//  private ManajementCPModel model;
  private List<ManajementCPModel> listmodel;
  private ManajementCPAdapter adapter;
  private UserSessionManager session;
  private ProgressDialogHelper progressDialogHelper;
  private ManajementCPModel modelmanajement;
  private ListView listView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_management_cp);

    session = new UserSessionManager(this);
    progressDialogHelper = new ProgressDialogHelper();
    listView = findViewById(R.id.list_mcp);
    listmodel = new ArrayList<ManajementCPModel>();

    Intent mcp = getIntent();
    String nama = mcp.getStringExtra("nama");
    String jabatan = mcp.getStringExtra("jabatan");
    String band = mcp.getStringExtra("band");
    String company_unit = mcp.getStringExtra("nama");
    String comp = mcp.getStringExtra("comp");
    String url = mcp.getStringExtra("url");

    ImageView iv_profile = findViewById(R.id.career_profile);
    Picasso.get().load(url).error(R.drawable.profile).into(iv_profile);

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

//
//    modelmanajement = new ManajementCPModel("","","","","","","","");
//    listmodel.add(modelmanajement);
//
//    adapter = new ManajementCPAdapter(ManagementCP.this,listmodel);
//    listView.setAdapter(adapter);




    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    getSupportActionBar().setTitle("Management Career Plan");

    getDataManagementCP();
  }

  private void getDataManagementCP() {
    progressDialogHelper.showProgressDialog(ManagementCP.this, "Load data ...");
//    https://testapi.digitalevent.id/hcis/api/qualifications/data-mcp?business_code=1200&personnel_number=173122013
    AndroidNetworking.get(session.getServerMyCareer()+"api/qualifications/data-mcp?business_code=1200&personnel_number=173122013")
            .addHeaders("Accept","application/json")
            .addHeaders("Content-Type","application/json")
            .addHeaders("Authorization","Bearer "+session.getTokenMyCareer())
            .setPriority(Priority.MEDIUM)
            .build()
            .getAsJSONObject(new JSONObjectRequestListener() {
              @Override
              public void onResponse(JSONObject response) {
//                System.out.println("getdataManagementCP " + response);
                try {

                  listmodel = new ArrayList<ManajementCPModel>();
                  JSONArray array = response.getJSONArray("data");

                  for (int i = 0 ; i<array.length(); i++){
                    JSONObject object = array.getJSONObject(i);

                    String personnel_number = object.getString("personnel_number");
                    String complete_name = object.getString("complete_name");
                    String organization_code = object.getString("organization_code");
                    String organization_name = object.getString("organization_name");
                    String job = object.getString("job");
                    String job_name = object.getString("job_name");
                    String position_code = object.getString("position_code");
                    String position_name = object.getString("position_name");

                    modelmanajement = new ManajementCPModel(personnel_number,complete_name,organization_code,organization_name,job,job_name,position_code,position_name);
                    listmodel.add(modelmanajement);
                  }

                  adapter = new ManajementCPAdapter(ManagementCP.this,listmodel);
                  listView.setAdapter(adapter);

                  progressDialogHelper.dismissProgressDialog(ManagementCP.this);

                } catch (Exception e) {
                  e.printStackTrace();
                }


              }

              @Override
              public void onError(ANError anError) {
                progressDialogHelper.dismissProgressDialog(ManagementCP.this);

                System.out.println("error "+ anError);

              }
            });


  }


  @Override
  public boolean onSupportNavigateUp(){
    finish();
    return true;
  }
}
