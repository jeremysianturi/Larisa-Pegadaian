package id.co.pegadaian.diarium.controller.home.main_menu.community;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import id.co.pegadaian.diarium.R;
import id.co.pegadaian.diarium.adapter.MemberCommunityAdapter;
import id.co.pegadaian.diarium.controller.login.LoginActivity;
import id.co.pegadaian.diarium.model.CommunityParticipantModel;
import id.co.pegadaian.diarium.util.UserSessionManager;
import id.co.pegadaian.diarium.util.element.ProgressDialogHelper;

public class DetailCommunityActivity extends AppCompatActivity {

    UserSessionManager session;
    private ProgressDialogHelper progressDialogHelper;
    private List<CommunityParticipantModel> listModel;
    private CommunityParticipantModel model;
    private MemberCommunityAdapter adapter;
    ListView listMember;


    String community_name, community_id, community_role;
    Typeface font,fontbold;
    TextView tvCommunityName, tvCountMember, tvMember;
    LinearLayout lvAddMember;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_community);
        Intent intent = getIntent();
        community_name = intent.getStringExtra("community_name");
        community_id = intent.getStringExtra("community_id");
        community_role = intent.getStringExtra("community_role");
        font = Typeface.createFromAsset(DetailCommunityActivity.this.getAssets(),"fonts/Nexa Light.otf");
        fontbold = Typeface.createFromAsset(DetailCommunityActivity.this.getAssets(),"fonts/Nexa Bold.otf");
        session = new UserSessionManager(this);
        progressDialogHelper = new ProgressDialogHelper();
//        Toast.makeText(this, "role : "+community_role, Toast.LENGTH_SHORT).show();
        lvAddMember = (LinearLayout) findViewById(R.id.lvAddMember);
        lvAddMember.setVisibility(View.GONE);
        if (community_role.equals("AD")) {
            lvAddMember.setVisibility(View.VISIBLE);
            lvAddMember.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent a = new Intent(DetailCommunityActivity.this, AddMemberActivity.class);
                    a.putExtra("community_id", community_id);
                    startActivity(a);
                }
        });
        }
        tvCommunityName = findViewById(R.id.nama_kelompok);
        tvCommunityName.setTypeface(fontbold);
        tvCommunityName.setText(community_name);
        tvCountMember = findViewById(R.id.jumlah_kelompok);
        tvCountMember.setTypeface(fontbold);
        tvMember = findViewById(R.id.member);
        tvMember.setTypeface(fontbold);
        listMember = findViewById(R.id.listanggota);


        getDetailCommunity(community_id);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    @Override
    public void onResume(){
        super.onResume();
        getDetailCommunity(community_id);
    }


    private void getDetailCommunity(final String community_id_get){
        progressDialogHelper.showProgressDialog(DetailCommunityActivity.this, "Getting data...");
        AndroidNetworking.get(session.getServerURL()+"users/showallparticipant/comid/"+community_id_get+"/buscd/"+session.getUserBusinessCode())
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
                        System.out.println(response+"RESPONALLPARTICIPANT");
                        try {
                            if(response.getInt("status")==200){
//                                session.setToken(response.getString("token"));
                                listModel = new ArrayList<CommunityParticipantModel>();
                                JSONArray jsonArray = response.getJSONArray("data");
                                if (jsonArray.length()==0) {
                                    tvCountMember.setText("This community have not yet any member");
                                } else {
                                    tvCountMember.setText(jsonArray.length()+" Member");
                                }
                                for (int a = 0; a < jsonArray.length(); a++) {
                                    JSONObject object = jsonArray.getJSONObject(a);
                                    String begin_date = object.getString("begin_date");
                                    String end_date = object.getString("end_date");
                                    String business_code = object.getString("business_code");
                                    String personal_number = object.getString("personal_number");
                                    String community_id = object.getString("community_id");
                                    String community_role = object.getString("community_role");
                                    String aprov = object.getString("aprov");
                                    String change_date = object.getString("change_date");
                                    String change_user = object.getString("change_user");
                                    String profile = object.getString("profile");
                                    String full_name = null;
                                    JSONArray arrayName = object.getJSONArray("full_name");
                                    for (int i=0;i<arrayName.length();i++) {
                                        JSONObject objectName = arrayName.getJSONObject(i);
                                        full_name = objectName.getString("full_name");
                                    }
//                                    String profile = object.getString("profile");
//                                    if (community_id_get.equals(community_id)) {
                                        model = new CommunityParticipantModel(begin_date, end_date, business_code, personal_number, community_id, community_role, aprov, change_date, change_user, full_name, profile);
                                        listModel.add(model);
//                                    }
                                }
                                adapter = new MemberCommunityAdapter(DetailCommunityActivity.this, listModel);
                                listMember.setAdapter(adapter);
                                progressDialogHelper.dismissProgressDialog(DetailCommunityActivity.this);
                            }else{
                                popUpLogin();
                                progressDialogHelper.dismissProgressDialog(DetailCommunityActivity.this);

                            }
                        }catch (Exception e){
                            progressDialogHelper.dismissProgressDialog(DetailCommunityActivity.this);

                            System.out.println(e);
                        }

                    }
                    @Override
                    public void onError(ANError error) {
                        progressDialogHelper.dismissProgressDialog(DetailCommunityActivity.this);

                        System.out.println(error);
                    }
                });
    }

    private void popUpLogin() {
        final Dialog dialog = new Dialog(DetailCommunityActivity.this);
        dialog.setContentView(R.layout.layout_session_end);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        dialog.getWindow().setAttributes(lp);
        dialog.setTitle("Input Code Here");
        Button btnYes =(Button) dialog.findViewById(R.id.btnYes);
        dialog.show();
        dialog.setCancelable(false);
        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                session.setLoginState(false);
                Intent i = new Intent(DetailCommunityActivity.this, LoginActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

}
