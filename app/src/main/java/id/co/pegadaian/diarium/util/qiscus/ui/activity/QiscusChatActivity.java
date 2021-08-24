package id.co.pegadaian.diarium.util.qiscus.ui.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.qiscus.sdk.Qiscus;
import com.qiscus.sdk.chat.core.data.model.QiscusChatRoom;
import com.qiscus.sdk.ui.QiscusGroupChatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import id.co.pegadaian.diarium.R;
import id.co.pegadaian.diarium.util.UserSessionManager;
import id.co.pegadaian.diarium.util.qiscus.ui.presenter.MainPresenter;
import id.co.pegadaian.diarium.util.qiscus.ui.presenter.MvpView.MainMvp;

public class QiscusChatActivity extends AppCompatActivity implements MainMvp {

    private MainPresenter mainPresenter;
    private ProgressDialog progressDialog;

    //@BindView(R.id.et_email)
    TextView etEmail;
    //@BindView(R.id.et_name)
    TextView etName;
    @BindView(R.id.btn_submit)
    Button btnSubmit;
    String botname;
    UserSessionManager session;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_qiscus);
        ButterKnife.bind(this);
        session = new UserSessionManager(this);
        mainPresenter = new MainPresenter();
        mainPresenter.onAttachView(this);
        getBotName();
        PostAuth();

//        String email = getIntent().getStringExtra("email");
//        String name = getIntent().getStringExtra("name");
//        mainPresenter.initiate(email, name);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void PostAuth(){
            JSONObject body = new JSONObject();
            try {
                body.put("bot_id","5d410ad61a3654482a324eee");
                body.put("email",session.getUserNIK()+"@diarium.co.id");
                body.put("nik",session.getUserNIK());
                body.put("type","qiscus");

            }catch (JSONException e){
                System.out.println(e);

            }
            System.out.println(body.toString());

            AndroidNetworking.post("https://api.vutura.io/v2/sdk/auth")
                    .addHeaders("Accept","application/json")
                    .addHeaders("Content-Type","application/json")
                    .addJSONObjectBody(body)
                    .setPriority(Priority.MEDIUM)
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {
                            // do anything with response
                            System.out.println(response+"RESPONPOSTAUTHVUTURA");
                            try {
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


    private void getBotName(){
        AndroidNetworking.get("http://13.229.91.70/botname?nik="+session.getUserNIK())
                .addHeaders("Accept","application/json")
                .addHeaders("Content-Type","application/json")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println(response+"RESPONBOTNAME");
                        try {
                            if(response.getBoolean("status")){
                                if (response.getString("bot_name").equals("0")) {
                                    botname = "Dexy-Kiwari";
                                } else {
                                    botname = response.getString("bot_name");
                                }
                            }else{
                                botname = "Dexy-Kiwari";
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

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    @OnClick(R.id.btn_submit)
    public void submit() {
        String email = getIntent().getStringExtra("email");
        System.out.println(email+"RETURNEMAIL");
//        String name;
//        if (session.getUserBusinessCode().equals("3000")) {
//            name = botname;
//        } else {
//            name = getIntent().getStringExtra("name");
//        }
        mainPresenter.initiate(email, botname);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mainPresenter.onDetachView();
    }

    @Override
    public void showLoading() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Loading...");
        }
        progressDialog.show();
    }

    @Override
    public void dismissLoading() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getBotName();
    }

    @Override
    public void showError(String error) {
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void startChat(QiscusChatRoom qiscusChatRoom) {
        Qiscus.getChatConfig()
                .setStatusBarColor(R.color.red)
                .setAppBarColor(R.color.red)
                .setAccentColor(R.color.red)
                .setOnlyEnablePushNotificationOutsideChatRoom(true)
                .setNotificationSmallIcon(R.mipmap.ic_launcher)
                .setSendButtonIcon(R.drawable.ic_send_button)
                .setTimeFormat(date -> new SimpleDateFormat("HH:mm").format(date));
        startActivity(QiscusGroupChatActivity.generateIntent(this, qiscusChatRoom));
    }
}
