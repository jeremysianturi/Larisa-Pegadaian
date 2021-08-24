package id.co.pegadaian.diarium.controller.home.main_menu.community;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import id.co.pegadaian.diarium.R;
import id.co.pegadaian.diarium.util.UserSessionManager;
import id.co.pegadaian.diarium.util.element.ProgressDialogHelper;

public class CreateCommunityActivity extends AppCompatActivity {
    EditText etComName, etComDesc, etComMaxPerson;
    Spinner spinnerType;
    Button btnCreate;
    UserSessionManager session;
    final List<String> list = new ArrayList<String>();
    String type;
    private ProgressDialogHelper progressDialogHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_community);
        session = new UserSessionManager(this);
        etComName = (EditText) findViewById(R.id.etComName);
        etComDesc = (EditText) findViewById(R.id.etComDesc);
        etComMaxPerson = (EditText) findViewById(R.id.etComMaxPerson);
        spinnerType = (Spinner) findViewById(R.id.spinnerCommunityType);
        btnCreate = (Button) findViewById(R.id.btnCreate);
        progressDialogHelper = new ProgressDialogHelper();
        spinnerType = findViewById(R.id.spinnerCommunityType);
        list.add("Choose Community Type");
        list.add("Public");
        list.add("Private");
        ArrayAdapter<String> adp1 = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, list);
        adp1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerType.setAdapter(adp1);
        spinnerType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {
                // TODO Auto-generated method stub
                if (list.get(position).equals("Public")) {
                    type="00";
                } else if (list.get(position).equals("Private")){
                    type="01";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });

        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = etComName.getText().toString();
                String desc = etComDesc.getText().toString();
                String max = etComMaxPerson.getText().toString();
                String text = spinnerType.getSelectedItem().toString();
                if (name.equals("")||desc.equals("")||max.equals("")) {
                    Toast.makeText(CreateCommunityActivity.this, "Please fill all the form !", Toast.LENGTH_SHORT).show();
                } else if (text.equals("Choose Community Type")){
                    Toast.makeText(CreateCommunityActivity.this, "Choose Community Type First", Toast.LENGTH_SHORT).show();
                } else {
                    String tipe;
                    if (text.equals("Private")) {
                        tipe = "01";
                    } else {
                        tipe = "00";
                    }
                    generatePostId(name, desc, max, tipe);
                }
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Create Community");
    }
    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    private void generatePostId(final String name, final String desc, final String max, final String tipe) {
        progressDialogHelper.showProgressDialog(CreateCommunityActivity.this, "Creating community...");
        SimpleDateFormat tgl = new SimpleDateFormat("yyyy-MM-dd");
        String tRes = tgl.format(new Date());
//        progressDialogHelper.showProgressDialog(CheckinActivity.this, "Getting data...");
        AndroidNetworking.get(session.getServerURL() + "users/community/generateIDCommunity")
                .addHeaders("Accept", "application/json")
                .addHeaders("Content-Type", "application/json")
                .addHeaders("Authorization", session.getToken())
                //.addJSONObjectBody(body)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println("GenerateIDCOmmunity" + response);
                        // do anything with response
                        try {
                            if (response.getInt("status") == 200) {
                                String generatedCommunityId = response.getJSONObject("data").getString("id");
                                createCommunity(generatedCommunityId, name, desc, max, tipe);
                            } else {
                            }
                            System.out.println("status ya : " + session.getStat());
//                            progressDialogHelper.dismissProgressDialog(CheckinActivity.this);
                        } catch (Exception e) {
//                            progressDialogHelper.dismissProgressDialog(CheckinActivity.this);
                            System.out.println(e);
                        }
                    }

                    @Override
                    public void onError(ANError error) {
//                        progressDialogHelper.dismissProgressDialog(CheckinActivity.this);
                        System.out.println(error);
                    }
                });
    }


    private void createCommunity(final String ComId, final String name, String desc, String max, String tipe) {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat tgl = new SimpleDateFormat("yyyy-MM-dd");
        String tRes = tgl.format(new Date());

        @SuppressLint("SimpleDateFormat") SimpleDateFormat jam = new SimpleDateFormat("HH:mm");
        String jamRes = jam.format(new Date());

        JSONObject jResult = new JSONObject();// main object
        JSONArray jArray = new JSONArray();// /ItemDetail jsonArray
        JSONObject jGroup = new JSONObject();// /sub Object
        try {
            jGroup.put("begin_date", tRes);
            jGroup.put("end_date", "9999-12-31");
            jGroup.put("business_code", session.getUserBusinessCode());
            jGroup.put("personal_number", session.getUserNIK());
            jGroup.put("community_id", ComId);
            jGroup.put("community_name", name);
            jGroup.put("community_desc", desc);
            jGroup.put("community_type", tipe);
            jGroup.put("community_max_person", max);
            jGroup.put("community_date", tRes);
            jGroup.put("change_date", tRes);
            jGroup.put("change_user", session.getUserNIK());
            jArray.put(jGroup);
            // /itemDetail Name is JsonArray Name
            jResult.put("activity", jArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        System.out.println(jArray + "PARAMCOMUNITY");
        AndroidNetworking.post(session.getServerURL()+"users/community/"+ComId+"/type/"+tipe+"/buscd/"+session.getUserBusinessCode())
                .addHeaders("Accept","application/json")
                .addHeaders("Content-Type","application/json")
                .addHeaders("Authorization",session.getToken())
                .addJSONArrayBody(jArray)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // do anything with response
                        System.out.println(response+"RESPONYAKOMUNITY");
                        try {
                            if(response.getInt("status")==200){
                                joinCommunity(ComId, name);
                            }else {
                                Toast.makeText(CreateCommunityActivity.this,"Connection Problem",Toast.LENGTH_SHORT).show();
                            }
                            progressDialogHelper.dismissProgressDialog(CreateCommunityActivity.this);
                        }catch (Exception e){
                            System.out.println(e);
                            progressDialogHelper.dismissProgressDialog(CreateCommunityActivity.this);
                        }
                    }
                    @Override
                    public void onError(ANError error) {
                        System.out.println(error);
                        progressDialogHelper.dismissProgressDialog(CreateCommunityActivity.this);
                    }
                });
    }

    private void joinCommunity(String comId, String comName) {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat tgl = new SimpleDateFormat("yyyy-MM-dd");
        String tRes = tgl.format(new Date());

        @SuppressLint("SimpleDateFormat") SimpleDateFormat jam = new SimpleDateFormat("HH:mm");
        String jamRes = jam.format(new Date());

        JSONObject jGroup = new JSONObject();// /sub Object
        try {
            jGroup.put("begin_date", tRes);
            jGroup.put("end_date", "9999-12-31");
            jGroup.put("business_code", session.getUserBusinessCode());
            jGroup.put("personal_number", session.getUserNIK());
            jGroup.put("community_id", comId);
            jGroup.put("community_role", "AD");
            jGroup.put("aprov", "1");
            jGroup.put("change_user", session.getUserNIK());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        System.out.println(jGroup + "PARAMADDPARTISIPAN");
        AndroidNetworking.post(session.getServerURL()+"users/communityParticipant/nik/"+session.getUserNIK()+"/"+comId+"/AD/buscd/"+session.getUserBusinessCode())
                .addHeaders("Accept","application/json")
                .addHeaders("Content-Type","application/json")
                .addHeaders("Authorization",session.getToken())
                .addJSONObjectBody(jGroup)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // do anything with response
                        System.out.println(response+"ADDPARTISIPANHASIL");
                        try {
                            if(response.getInt("status")==200){
                                Toast.makeText(CreateCommunityActivity.this, "Success create community", Toast.LENGTH_SHORT).show();
                                Intent a = new Intent(CreateCommunityActivity.this, CommunityMessageActivity.class);
                                a.putExtra("community_id",comId);
                                a.putExtra("community_name",comName);
                                a.putExtra("community_role","AD");
                                startActivity(a);
                                finish();
                            }else {
                                Toast.makeText(CreateCommunityActivity.this,"Failed!",Toast.LENGTH_SHORT).show();
                            }
                        }catch (Exception e){
                            System.out.println(e);
                        }
                    }
                    @Override
                    public void onError(ANError error) {
                        System.out.println(error);
                    }
                });
    }
}
