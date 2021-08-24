package id.co.pegadaian.diarium.controller.home.main_menu.community;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

import id.co.pegadaian.diarium.R;
import id.co.pegadaian.diarium.util.UserSessionManager;

public class OverviewCommunityActivity extends AppCompatActivity {
    String community_name, community_id, community_type, community_desc;
    TextView tvName, tvDesc;
    Button btnNext;
    Dialog dialog;
    UserSessionManager session;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview_community);
        session = new UserSessionManager(this);
        Intent intent = getIntent();
        community_name = intent.getStringExtra("community_name");
        community_id = intent.getStringExtra("community_id");
        community_desc = intent.getStringExtra("community_desc");
        community_type = intent.getStringExtra("community_type");

        System.out.println(community_id+"COMMUNITYID");
        tvName = (TextView) findViewById(R.id.nama_komunitas);
        tvDesc = (TextView) findViewById(R.id.deskripsi);
        btnNext = (Button) findViewById(R.id.btnNext);

        tvName.setText(community_name);
        tvDesc.setText(community_desc);

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (community_type.equals("01")) {
                    Toast.makeText(OverviewCommunityActivity.this, "This is private Community, only Admin can invite you", Toast.LENGTH_SHORT).show();
                } else {
                    popUpJoin(community_id, community_name);
                }
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Community");
    }
    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    private void popUpJoin(final String comId, final String comName) {
        dialog = new Dialog(OverviewCommunityActivity.this);
        dialog.setContentView(R.layout.layout_confirm_join_community);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        dialog.getWindow().setAttributes(lp);
        dialog.setTitle("Input Code Here");
        Button btnYes =(Button) dialog.findViewById(R.id.btnYes);
        Button btnNo =(Button) dialog.findViewById(R.id.btnNo);
        dialog.show();
        dialog.setCancelable(false);
        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                joinCommunity(comId, comName);
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
            jGroup.put("community_role", "US");
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
                                Toast.makeText(OverviewCommunityActivity.this, "Success create community", Toast.LENGTH_SHORT).show();
                                Intent a = new Intent(OverviewCommunityActivity.this, CommunityMessageActivity.class);
                                a.putExtra("community_id",comId);
                                a.putExtra("community_name",comName);
                                a.putExtra("community_role","US");
                                startActivity(a);
                                finish();
                            }else {
                                Toast.makeText(OverviewCommunityActivity.this,"Failed!",Toast.LENGTH_SHORT).show();
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
