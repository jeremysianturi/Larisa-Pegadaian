package id.co.pegadaian.diarium.controller.inbox;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
import id.co.pegadaian.diarium.adapter.InboxAdapter;
import id.co.pegadaian.diarium.controller.login.LoginActivity;
import id.co.pegadaian.diarium.model.InboxModel;
import id.co.pegadaian.diarium.util.UserSessionManager;
import id.co.pegadaian.diarium.util.element.ProgressDialogHelper;


public class InboxActivity extends AppCompatActivity {
    UserSessionManager session;
    private ProgressDialogHelper progressDialogHelper;
    private List<InboxModel> listModel;
    private InboxModel model;
    private InboxAdapter adapter;
    ListView listInbox;
    private TextView tvNull;
    Typeface font,fontbold;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inbox);
        font = Typeface.createFromAsset(getApplication().getAssets(),"fonts/Nexa Light.otf");
        fontbold = Typeface.createFromAsset(getApplication().getAssets(),"fonts/Nexa Bold.otf");

        session = new UserSessionManager(this);
        progressDialogHelper = new ProgressDialogHelper();
        tvNull = findViewById(R.id.tvNull);
        listInbox = findViewById(R.id.list_inbox);

        getInbox();
        getSupportActionBar().setTitle("Inbox");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void getInbox(){
        progressDialogHelper.showProgressDialog(InboxActivity.this, "Getting data...");
        AndroidNetworking.get(session.getServerURL()+"users/inbox/buscd/"+session.getUserBusinessCode())
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
                        System.out.println(response+"ktjbkgrjbhy");
                        try {
                            if(response.getInt("status")==200){
//                                session.setToken(response.getString("token"));
                                listModel = new ArrayList<InboxModel>();
                                JSONArray jsonArray = response.getJSONArray("data");
                                if (jsonArray.length()==0) {
                                    listInbox.setVisibility(View.GONE);
                                    tvNull.setVisibility(View.VISIBLE);
                                } else {
                                    listInbox.setVisibility(View.VISIBLE);
                                    tvNull.setVisibility(View.GONE);
                                    for (int a = 0; a < jsonArray.length(); a++) {
                                        JSONObject object = jsonArray.getJSONObject(a);
                                        String begin_date = object.getString("begin_date");
                                        String end_date = object.getString("end_date");
                                        String business_code = object.getString("business_code");
                                        String personal_number = object.getString("personal_number");
                                        String inbox_id = object.getString("inbox_id");
                                        String title = object.getString("title");
                                        String description = object.getString("description");
                                        String change_date = object.getString("change_date");
                                        String change_user = object.getString("change_user");
                                        model = new InboxModel(begin_date, end_date, business_code, personal_number, inbox_id, title, description, change_date, change_user);
                                        listModel.add(model);
                                    }
                                    adapter = new InboxAdapter(InboxActivity.this, listModel);
                                    listInbox.setAdapter(adapter);
                                    listInbox.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
                                            String item = (String) listInbox.getItemAtPosition(position);
                                            Toast.makeText(InboxActivity.this,"You selected : " + item, Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    listInbox.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                            Intent i = new Intent(InboxActivity.this, DetailInboxActivity.class);
                                            i.putExtra("title",listModel.get(position).getTitle());
                                            i.putExtra("date",listModel.get(position).getChange_date());
                                            i.putExtra("desc",listModel.get(position).getDescription());
                                            startActivity(i);
                                        }
                                    });
                                }
                                progressDialogHelper.dismissProgressDialog(InboxActivity.this);
                            }else{
                                popUpLogin();
                                progressDialogHelper.dismissProgressDialog(InboxActivity.this);

                            }
                        }catch (Exception e){
                            progressDialogHelper.dismissProgressDialog(InboxActivity.this);

                            System.out.println(e);
                        }

                    }
                    @Override
                    public void onError(ANError error) {
                        progressDialogHelper.dismissProgressDialog(InboxActivity.this);

                        System.out.println(error);
                    }
                });
    }

    private void popUpLogin() {
        final Dialog dialog = new Dialog(InboxActivity.this);
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
                Intent i = new Intent(InboxActivity.this, LoginActivity.class);
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

