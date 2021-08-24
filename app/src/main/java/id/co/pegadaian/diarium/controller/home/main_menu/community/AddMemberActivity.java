package id.co.pegadaian.diarium.controller.home.main_menu.community;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
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
import id.co.pegadaian.diarium.adapter.AddMemberAdapter;
import id.co.pegadaian.diarium.model.SearchTempModel;
import id.co.pegadaian.diarium.util.UserSessionManager;
import id.co.pegadaian.diarium.util.element.ProgressDialogHelper;

public class AddMemberActivity extends AppCompatActivity {
    private EditText mEditText;
    UserSessionManager session;
    private ProgressDialogHelper progressDialogHelper;
    private List<SearchTempModel> listModel;
    private SearchTempModel model;
    private AddMemberAdapter adapter;
    String community_name, community_id;
    ListView listPartner;
    Button btnSearch;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_member);
        session = new UserSessionManager(this);
        Intent intent = getIntent();
        community_name = intent.getStringExtra("community_name");
        community_id = intent.getStringExtra("community_id");
        listPartner = (ListView) findViewById(R.id.listView1);
        mEditText = (EditText) findViewById(R.id.editText1);
        mEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    String param = mEditText.getText().toString();
                    getPartner(param);
                    return true;
                }
                return false;
            }
        });

    }

    private void getPartner(String param){
        AndroidNetworking.get(session.getServerURL()+"users/search/key/"+param+"/buscd/"+session.getUserBusinessCode())
                .addHeaders("Accept","application/json")
                .addHeaders("Content-Type","application/json")
                .addHeaders("Authorization",session.getToken())
                //.addJSONObjectBody(body)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // do anything with response
                        System.out.println(response+"seradkewjekj");
                        try {
                            if(response.getInt("status")==200){
                                listModel = new ArrayList<SearchTempModel>();
//                                SearchTempModel contacts = new SearchTempModel();
                                JSONArray jsonArray = response.getJSONArray("data");
                                for (int a = 0; a < jsonArray.length(); a++) {
                                    JSONObject object = jsonArray.getJSONObject(a);
                                    String end_date = object.getString("end_date");
                                    String business_code = object.getString("business_code");
                                    String personal_number = object.getString("personal_number");
                                    String full_name = object.getString("full_name");
                                    String nickname = object.getString("nickname");
                                    String born_city = object.getString("born_city");
                                    String born_date = object.getString("born_date");
                                    String gender = object.getString("gender");
                                    String religion = object.getString("religion");
                                    String language = object.getString("language");
                                    String national = object.getString("national");
                                    String tribe = object.getString("tribe");
                                    String blood_type = object.getString("blood_type");
                                    String rhesus = object.getString("rhesus");
                                    String marital_status = object.getString("marital_status");
                                    String marital_date = object.getString("marital_date");
                                    String personal_number_reference = object.getString("personal_number_reference");
                                    String change_date = object.getString("change_date");
                                    String change_user = object.getString("change_user");
                                    String begin_date = object.getString("begin_date");
                                    String job = object.getString("job");
                                    String unit = object.getString("unit");
                                    String posisi = object.getString("posisi");
                                    String profile = object.getString("profile");
                                    model = new SearchTempModel(personal_number,full_name,job,unit,posisi,profile);
                                    listModel.add(model);
                                }

                                adapter = new AddMemberAdapter(AddMemberActivity.this, listModel);
                                listPartner.setAdapter(adapter);
                                listPartner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(AddMemberActivity.this);
                                        builder.setTitle("Confirm");
                                        builder.setMessage("Are you sure to add "+listModel.get(position).getFull_name());
                                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                // Do nothing but close the dialog
                                                addMember(listModel.get(position).getPersonal_number(), listModel.get(position).getFull_name());
                                            }
                                        });
                                        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                // Do nothing
                                                dialog.dismiss();
                                            }
                                        });

                                        AlertDialog alert = builder.create();
                                        alert.show();
                                    }
                                });
                            }else{
//                                popUpLogin();
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

    private void addMember(String change_user, final String name) {
        SimpleDateFormat tgl = new SimpleDateFormat("yyyy-MM-dd");
        String tRes = tgl.format(new Date());
        SimpleDateFormat jam = new SimpleDateFormat("HH:mm");
        String jamRes = jam.format(new Date());

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("begin_date", tRes);
            jsonObject.put("end_date", "9999-12-31");
            jsonObject.put("business_code", session.getUserBusinessCode());
            jsonObject.put("personal_number", change_user);
            jsonObject.put("community_id", community_id);
            jsonObject.put("community_role", "US");
            jsonObject.put("aprov", "1");
            jsonObject.put("change_user",change_user);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        System.out.println(jsonObject+"PARAMADDMEMBER");
        AndroidNetworking.post(session.getServerURL()+"users/communityParticipant/nik/"+change_user+"/"+community_id+"/US/buscd/"+session.getUserBusinessCode())
                .addHeaders("Accept","application/json")
                .addHeaders("Content-Type","application/json")
                .addHeaders("Authorization",session.getToken())
                .addJSONObjectBody(jsonObject)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // do anything with response
                        System.out.println(response+"ADDMEMBER");
                        try {
                            if(response.getInt("status")==200){
                                Toast.makeText(AddMemberActivity.this,"Succes invite "+name,Toast.LENGTH_SHORT).show();
                                finish();
                            }else {
                                Toast.makeText(AddMemberActivity.this,"error!",Toast.LENGTH_SHORT).show();

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