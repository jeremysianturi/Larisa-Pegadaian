package id.co.pegadaian.diarium.controller.home.main_menu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

import id.co.pegadaian.diarium.R;
import id.co.pegadaian.diarium.controller.login.LoginActivity;
import id.co.pegadaian.diarium.util.UserSessionManager;
import id.co.pegadaian.diarium.util.element.ProgressDialogHelper;

public class ChangePassword extends AppCompatActivity {

    String oldPassword, newPassword, confirmPassword;
    ConstraintLayout clChangePass;
    TextView tvOldPassword, tvNewPassword, tvConfirmPassword;
    TextInputEditText etOldPassword, etNewPassword, etConfirmPassword;
    Button btnSubmit;
    UserSessionManager session;
    private ProgressDialogHelper progressDialogHelper;

    JSONObject errorJSON;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        clChangePass = findViewById(R.id.cl_changepassword);
        tvOldPassword = findViewById(R.id.tv_title_oldpassword);
        etOldPassword = findViewById(R.id.et_oldpassword);
        tvNewPassword = findViewById(R.id.tv_title_newpassword);
        etNewPassword = findViewById(R.id.et_newpassword);
        tvConfirmPassword = findViewById(R.id.tv_title_newconfirmpassword);
        etConfirmPassword = findViewById(R.id.et_newconfirmpassword);
        btnSubmit = findViewById(R.id.btn_submit_changepassword);

        progressDialogHelper = new ProgressDialogHelper();
        session = new UserSessionManager(ChangePassword.this);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oldPassword = etOldPassword.getText().toString();
                newPassword = etNewPassword.getText().toString();
                confirmPassword = etConfirmPassword.getText().toString();

                InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

                AlertDialog.Builder builderChange = new AlertDialog.Builder(ChangePassword.this);

                builderChange.setTitle("Confirm");
                builderChange.setMessage("Are you sure to change password ?");

                builderChange.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        postChangePassword(oldPassword,newPassword,confirmPassword);
                    }
                });

                builderChange.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog alertChange = builderChange.create();
                alertChange.show();
            }
        });

    }

    private void postChangePassword(String oldpass, String newPass, String confirmPass){

        progressDialogHelper.showProgressDialog(ChangePassword.this, "Update Password..");
        JSONObject body = new JSONObject();
        try {
            body.put("old_password",oldpass);
            body.put("password",newPass);
            body.put("password_confirmation",confirmPass);

        }catch (JSONException e){
            System.out.println(e);
        }
        System.out.println("Body Submit Change Password: "+body);

        AndroidNetworking.post( session.getServerURLHCISAUTH()+"api/auth/account/password")
                .addHeaders("Accept","application/json")
                .addHeaders("Content-Type","application/json")
                .addHeaders("Authorization","Bearer "+session.getToken())
                .addJSONObjectBody(body)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // do anything with response
                        System.out.println("response submit identification: "+response);
                        try {
                            if(response.getInt("status")==200){
                                progressDialogHelper.dismissProgressDialog(ChangePassword.this);
                                Snackbar.make(clChangePass, response.getString("message"), Snackbar.LENGTH_LONG).show();
                                logout();
                            }else{
                                progressDialogHelper.dismissProgressDialog(ChangePassword.this);
                                System.out.println("On response message: "+response.getString("message"));
                            }
                        }catch (Exception e){
                            progressDialogHelper.dismissProgressDialog(ChangePassword.this);
                            System.out.println(e);
                        }
                    }
                    @Override
                    public void onError(ANError error) {
                        progressDialogHelper.dismissProgressDialog(ChangePassword.this);
                        String jsonErrorBody = error.getErrorBody();
                        System.out.println("response token on error change postchangepass(): "+ jsonErrorBody);

//                        try {
//                            if (error.getResponse() != null && error.getResponse().body() != null
//                                    && error.getResponse().body().source() != null){
//                                error.setErrorBody(Okio.buffer(error.getResponse().body().source().readUtf8()));
//                            }
//                        } catch (Exception e){
//
//                        }


//                        Snackbar.make(clChangePass, "password doesn't meet complexity requirements", Snackbar.LENGTH_LONG).show();


                        AlertDialog.Builder builderChange = new AlertDialog.Builder(ChangePassword.this);

                        builderChange.setTitle("Information");
                        builderChange.setMessage("password doesn't meet complexity requirements");

                        builderChange.setPositiveButton("Got it!", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });

                        AlertDialog alertChange = builderChange.create();
                        alertChange.show();
                    }
                });
    }

    private void logout(){
        progressDialogHelper.showProgressDialog(ChangePassword.this,"Load Data...");
        AndroidNetworking.get(session.getServerURLHCISAUTH()+"api/auth/logout")
                .addHeaders("Accept","application/json")
                .addHeaders("Content-Type","application/json")
                .addHeaders("Authorization","Bearer "+session.getToken())
                //.addJSONObjectBody(body)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println("Response logout setelah change password: " + response);
                        // do anything with response
                        try {
                            if (response.getInt("status")==200){
                                Toast.makeText(ChangePassword.this,"Logout Success!", Toast.LENGTH_LONG);
//                                Snackbar.make(fmMenuFrag,"Logout Success!", Snackbar.LENGTH_LONG).show();
                                System.out.println("Print response success logout after change password: " + response.get("message"));

                                Intent i = new Intent(ChangePassword.this, LoginActivity.class);
                                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(i);
                            }
                            else {
                                Toast.makeText(ChangePassword.this,"Logout Failed!", Toast.LENGTH_LONG);
//                                Snackbar.make(fmMenuFrag,"Logout Success!", Snackbar.LENGTH_LONG).show();
                                System.out.println("Print response failed logout after change password: " + response.get("message"));

                                Intent i = new Intent(ChangePassword.this, LoginActivity.class);
                                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(i);
                            }
                            progressDialogHelper.dismissProgressDialog(ChangePassword.this);

                        } catch (Exception e){
                            e.printStackTrace();
                            progressDialogHelper.dismissProgressDialog(ChangePassword.this);
                            System.out.println("Print response error logout after change password: " + e.getMessage());

                            Intent i = new Intent(ChangePassword.this, LoginActivity.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(i);

                        }

                    }
                    @Override
                    public void onError(ANError error) {
                        progressDialogHelper.dismissProgressDialog(ChangePassword.this);
                        System.out.println("get my post error logout after change password"+ error.getErrorBody());
                        System.out.println("get my post error logout after change password"+ error);
                        System.out.println("get my post error logout after change password"+ error.getResponse());

                        Intent i = new Intent(ChangePassword.this, LoginActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(i);
                    }
                });
    }
}