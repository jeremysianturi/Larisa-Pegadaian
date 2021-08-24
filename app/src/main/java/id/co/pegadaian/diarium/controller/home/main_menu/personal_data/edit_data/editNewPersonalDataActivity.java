package id.co.pegadaian.diarium.controller.home.main_menu.personal_data.edit_data;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import id.co.pegadaian.diarium.R;
import id.co.pegadaian.diarium.controller.home.main_menu.personal_data.PersonalDataActivity;
import id.co.pegadaian.diarium.util.UserSessionManager;
import id.co.pegadaian.diarium.util.element.ProgressDialogHelper;

public class editNewPersonalDataActivity extends AppCompatActivity {

    String personalIdentityName,personalIdentityValue,personalIdentityObjectIdentifier,personalIdentityType,personalIdentityCodeObject,tgl,from,serialNumber
            ,familyNumber,familyType;
    ConstraintLayout constrainEditData;
    LinearLayout linearEditIdentification;
    TextView tvTitleEditDataType,tvTitleEditDataTypeObject,tvTitleScnd,tvTitleThrd,tvEditDateIssued,tvEditExpireDate;
    EditText etScnd,etThrd;
    Button btnSubmitEdit;
    UserSessionManager session;
    private ProgressDialogHelper progressDialogHelper;
    final Calendar myCalendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_new_personal_data);

        setTitle("Edit Personal Data");

        AndroidNetworking.initialize(getApplicationContext());
        progressDialogHelper = new ProgressDialogHelper();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        tgl = sdf.format(new Date());

        session = new UserSessionManager(editNewPersonalDataActivity.this);

        constrainEditData = findViewById(R.id.cl_edit_personaldata);
        linearEditIdentification = findViewById(R.id.linear_foredit_identification);
        tvTitleEditDataType = findViewById(R.id.tv_titleedit_datatype);
        tvTitleEditDataTypeObject = findViewById(R.id.tv_edit_datatype_object);
        tvTitleScnd = findViewById(R.id.tv_titlesecond);
        tvTitleThrd = findViewById(R.id.tv_titlethird);
        etScnd = findViewById(R.id.et_second);
        etThrd = findViewById(R.id.et_third);
        tvEditDateIssued = findViewById(R.id.tv_edit_dateissue_identification);
        tvEditExpireDate = findViewById(R.id.tv_edit_expiredate_identification);
        btnSubmitEdit = findViewById(R.id.btn_submit_editData);

        Intent intent = getIntent();
        personalIdentityName = intent.getStringExtra("personal_identity_name");
        personalIdentityValue = intent.getStringExtra("personal_identity_value");
        personalIdentityObjectIdentifier = intent.getStringExtra("personal_identity_objidentifier");
        personalIdentityType = intent.getStringExtra("personal_identity_type");
        personalIdentityCodeObject = intent.getStringExtra("object_code");
        from = intent.getStringExtra("from");
        serialNumber = intent.getStringExtra("serial_number");
        familyNumber = intent.getStringExtra("family_number");
        familyType = intent.getStringExtra("family_type");

        System.out.println("Print Extra : " + personalIdentityName + "\n" +  personalIdentityValue + "\n"
                +  personalIdentityObjectIdentifier + "\n" +  personalIdentityType);



        if (personalIdentityType.equals("communication")){

            tvTitleEditDataType.setText("Communication Type");
            tvTitleEditDataTypeObject.setText(personalIdentityName);
            tvTitleScnd.setText("Communication Order");
            etScnd.setHint("Input Communication Order");
            etScnd.setInputType(InputType.TYPE_CLASS_NUMBER);
            tvTitleThrd.setText("Communication Number/Value");
            etThrd.setText(personalIdentityValue);

        } else if (personalIdentityType.equals("identification")){

            tvTitleEditDataType.setText("Identification Type");
            tvTitleEditDataTypeObject.setText(personalIdentityName);
            tvTitleScnd.setText("Place Issued");
            etScnd.setHint("Input Place Issued");
            tvTitleThrd.setText("Identification Number/Value");
            etThrd.setText(personalIdentityValue);
            linearEditIdentification.setVisibility(View.VISIBLE);
            popUpEditDateIssuePicker();
            popUpEditExpireDatePicker();

        }


        btnSubmitEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (personalIdentityType.equals("communication")){
                    if (from.equals("personal data")){
                        submitPostEditCommunicationPersonal();
                    } else {
                        submitPostEditCommunicationFamilyPersonal();
                    }
                } else if (personalIdentityType.equals("identification")){

                    if (from.equals("personal data")){
                        submitPostEditIdentificationPersonal();
                    } else {
                        submitPostEditIdentificationFamilyPersonal();
                    }

                }

            }
        });
    }

    private void popUpEditExpireDatePicker() {

        final DatePickerDialog.OnDateSetListener expireDate = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabelExpireDate();
            }
        };

        tvEditExpireDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(editNewPersonalDataActivity.this, expireDate, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

    }

    private void popUpEditDateIssuePicker() {
        final DatePickerDialog.OnDateSetListener dateIssue = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabelDateIssue();
            }
        };

        tvEditDateIssued.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(editNewPersonalDataActivity.this, dateIssue, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

    private void updateLabelDateIssue() {
        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        tvEditDateIssued.setText(sdf.format(myCalendar.getTime()));
    }

    private void updateLabelExpireDate() {
        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        tvEditExpireDate.setText(sdf.format(myCalendar.getTime()));
    }


    private void submitPostEditCommunicationPersonal () {
//        System.out.println("print para value: "+ "\n" + "et: "+ et1.getText().toString() + "\n" + "et1: " + et2.getText().toString()
//                + "\n" + "date issue: " + dateIssueIdentification.getText().toString() + "\n" + "expire date: " + expireDateIdentification.getText().toString());
        progressDialogHelper.showProgressDialog(editNewPersonalDataActivity.this, "Updating data..");
        JSONObject body = new JSONObject();
        try {
            body.put("object_identifier",personalIdentityObjectIdentifier);
            body.put("begin_date",tgl);
            body.put("end_date","9999-12-31");
            body.put("business_code",session.getUserBusinessCode());
            body.put("personnel_number",session.getUserNIK());
            body.put("communication_type",personalIdentityCodeObject);
            body.put("communication_order",etScnd.getText().toString());
            body.put("communication_number",etThrd.getText().toString());
            body.put("change_user",session.getUserNIK());
        }catch (JSONException e){
            System.out.println(e);
        }
        System.out.println("Body Submit Edit Communication: "+body);
        String postEditUrlCommunication = session.getServerURLHCIS()+"hcis/api/communication";
        System.out.println("url post edit communication: " + postEditUrlCommunication);

        AndroidNetworking.put(postEditUrlCommunication)
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
                        System.out.println("response submit edit communication: "+response);
                        try {
                            if(response.getInt("status")==200){
                                Snackbar.make(constrainEditData, response.getString("message"), Snackbar.LENGTH_LONG).show();
                                progressDialogHelper.dismissProgressDialog(editNewPersonalDataActivity.this);

//                                Intent i = new Intent(editNewPersonalDataActivity.this, PersonalDataActivity.class);
//                                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
//                                startActivity(i);
//                                editNewPersonalDataActivity.this.finish();
                                finish();

                            }else{
                                progressDialogHelper.dismissProgressDialog(editNewPersonalDataActivity.this);
                                System.out.println("On response message: "+response.getString("message"));
                            }
                        }catch (Exception e){
                            progressDialogHelper.dismissProgressDialog(editNewPersonalDataActivity.this);
                            System.out.println(e);
                        }
                    }
                    @Override
                    public void onError(ANError error) {
                        progressDialogHelper.dismissProgressDialog(editNewPersonalDataActivity.this);
                        String jsonError = error.getErrorBody();
                        System.out.println("response token"+ jsonError);
                        Snackbar.make(constrainEditData, error.getResponse().message(), Snackbar.LENGTH_LONG).show();
                        if (error.getResponse().message().equals("Unauthorized") ){
                            Snackbar.make(constrainEditData,"Wrong email or password", Snackbar.LENGTH_LONG).show();
                        }
                    }
                });
    }


    private void submitPostEditIdentificationPersonal() {
//        System.out.println("print para value: "+ "\n" + "et: "+ et1.getText().toString() + "\n" + "et1: " + et2.getText().toString()
//                + "\n" + "date issue: " + dateIssueIdentification.getText().toString() + "\n" + "expire date: " + expireDateIdentification.getText().toString());
        progressDialogHelper.showProgressDialog(editNewPersonalDataActivity.this, "Updating data..");
        JSONObject body = new JSONObject();
        try {
            body.put("object_identifier",personalIdentityObjectIdentifier);
            body.put("begin_date",tgl);
            body.put("end_date","9999-12-31");
            body.put("business_code",session.getUserBusinessCode());
            body.put("personnel_number",session.getUserNIK());
            body.put("identification_type",personalIdentityCodeObject);
            body.put("place_issue",etScnd.getText().toString());
            body.put("identification_number",etThrd.getText().toString());
            body.put("date_issue",tvEditDateIssued.getText().toString());
            body.put("expire_date",tvEditExpireDate.getText().toString());
            body.put("change_user",session.getUserNIK());
        }catch (JSONException e){
            System.out.println(e);
        }
        System.out.println("Body Submit Edit Identification: "+body);
        String postEditUrlIdentification = session.getServerURLHCIS()+"hcis/api/identification";
        System.out.println("post edit identification url: " + postEditUrlIdentification);

        AndroidNetworking.put( postEditUrlIdentification)
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
                        System.out.println("response submit edit identification: "+response);
                        try {
                            if(response.getInt("status")==200){
                                Snackbar.make(constrainEditData, response.getString("message"), Snackbar.LENGTH_LONG).show();
                                progressDialogHelper.dismissProgressDialog(editNewPersonalDataActivity.this);

//                                Intent i = new Intent(editNewPersonalDataActivity.this, PersonalDataActivity.class);
//                                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
//                                startActivity(i);
//                                editNewPersonalDataActivity.this.finish();
                                finish();

                            }else{

                                progressDialogHelper.dismissProgressDialog(editNewPersonalDataActivity.this);
                                Toast.makeText(editNewPersonalDataActivity.this,"Something wrong!",Toast.LENGTH_LONG).show();

                            }
                        }catch (Exception e){

                            progressDialogHelper.dismissProgressDialog(editNewPersonalDataActivity.this);
                            Toast.makeText(editNewPersonalDataActivity.this,"Something wrong!",Toast.LENGTH_LONG).show();
                            System.out.println(e);

                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        progressDialogHelper.dismissProgressDialog(editNewPersonalDataActivity.this);
                        Toast.makeText(editNewPersonalDataActivity.this,"Something wrong!",Toast.LENGTH_LONG).show();
                        String jsonError = error.getErrorBody();
                        System.out.println("response token"+ jsonError);
                        Snackbar.make(constrainEditData, error.getResponse().message(), Snackbar.LENGTH_LONG).show();
                        if (error.getResponse().message().equals("Unauthorized") ){
                            Snackbar.make(constrainEditData,"Wrong email or password", Snackbar.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private void submitPostEditCommunicationFamilyPersonal(){

        progressDialogHelper.showProgressDialog(editNewPersonalDataActivity.this, "Updating data..");
        JSONObject body = new JSONObject();
        try {
            body.put("object_identifier",personalIdentityObjectIdentifier);
            body.put("begin_date",tgl);
            body.put("end_date","9999-12-31");
            body.put("business_code",session.getUserBusinessCode());
            body.put("personnel_number",session.getUserNIK());
            body.put("communication_type",personalIdentityCodeObject);
            body.put("serial_number",etScnd.getText().toString());
            body.put("communication_number",etThrd.getText().toString());
            body.put("family_type",familyType);
            body.put("family_number",familyNumber);
            body.put("change_user",session.getUserNIK());
        }catch (JSONException e){
            System.out.println(e);
        }
        System.out.println("Body Submit Edit communication family personal: " + body);
        String postEditUrlCommunicationFamPersonal = session.getServerURLHCIS()+"hcis/api/familycommunication";
        System.out.println("post edit communication family personal url: " + postEditUrlCommunicationFamPersonal);

        AndroidNetworking.put( postEditUrlCommunicationFamPersonal)
                .addHeaders("Accept","application/json")
                .addHeaders("Content-Type","application/json")
                .addHeaders("Authorization","Bearer "+session.getToken())
                .addJSONObjectBody(body)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println("response post communication personal family: " + response);

                        try {
                            if (response.getInt("status") == 200){

                                Toast.makeText(editNewPersonalDataActivity.this,"Data updated!",Toast.LENGTH_SHORT).show();

//                                Intent i = new Intent(editNewPersonalDataActivity.this, PersonalDataActivity.class);
//                                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
//                                startActivity(i);
//                                editNewPersonalDataActivity.this.finish();
                                finish();

                            } else {

                                Toast.makeText(editNewPersonalDataActivity.this,"Update Failed!",Toast.LENGTH_SHORT).show();

                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        progressDialogHelper.dismissProgressDialog(editNewPersonalDataActivity.this);
                    }

                    @Override
                    public void onError(ANError anError) {
                        System.out.println("onError post communication personal family: " + anError);
                        Toast.makeText(editNewPersonalDataActivity.this,"Error!",Toast.LENGTH_SHORT).show();
                        progressDialogHelper.dismissProgressDialog(editNewPersonalDataActivity.this);
                    }
                });
    }

    private void submitPostEditIdentificationFamilyPersonal(){

        progressDialogHelper.showProgressDialog(editNewPersonalDataActivity.this, "Updating data..");
        JSONObject body = new JSONObject();
        try {
            body.put("object_identifier",personalIdentityObjectIdentifier);
            body.put("family_type",familyType);
            body.put("date_issue",tvEditDateIssued.getText().toString());
            body.put("family_number",familyNumber);
            body.put("personal_number",session.getUserNIK());
            body.put("place_issue",etScnd.getText().toString());
            body.put("end_date","9999-12-31");
            body.put("expire_date",tvEditExpireDate.getText().toString());
            body.put("begin_date",tgl);
            body.put("identification_number",etThrd.getText().toString());
            body.put("identification_type",personalIdentityCodeObject);
            body.put("business_code",session.getUserBusinessCode());
            body.put("change_user",session.getUserNIK());
        }catch (JSONException e){
            System.out.println(e);
        }
        System.out.println("Body Submit Edit Identification family personal: " + body);
        String postEditUrlIdentificationFamPersonal = session.getServerURLHCIS()+"hcis/api/familyidentification";
        System.out.println("post edit identification family personal url: " + postEditUrlIdentificationFamPersonal);

        AndroidNetworking.put( postEditUrlIdentificationFamPersonal)
                .addHeaders("Accept","application/json")
                .addHeaders("Content-Type","application/json")
                .addHeaders("Authorization","Bearer "+session.getToken())
                .addJSONObjectBody(body)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println("response post identification personal family: " + response);

                        try {
                            if (response.getInt("status") == 200){

                                Toast.makeText(editNewPersonalDataActivity.this,"Success change data",Toast.LENGTH_SHORT).show();

//                                Intent i = new Intent(editNewPersonalDataActivity.this, PersonalDataActivity.class);
//                                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
//                                startActivity(i);
//                                editNewPersonalDataActivity.this.finish();
                                finish();

                            } else {

                                Toast.makeText(editNewPersonalDataActivity.this,"Update Failed!",Toast.LENGTH_SHORT).show();

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        progressDialogHelper.dismissProgressDialog(editNewPersonalDataActivity.this);
                    }

                    @Override
                    public void onError(ANError anError) {

                        System.out.println("onError post identification personal family: " + anError);
                        Toast.makeText(editNewPersonalDataActivity.this,"Error!",Toast.LENGTH_SHORT).show();
                        progressDialogHelper.dismissProgressDialog(editNewPersonalDataActivity.this);

                    }
                });
    }

}