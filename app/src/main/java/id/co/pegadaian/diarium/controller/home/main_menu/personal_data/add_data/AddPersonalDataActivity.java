package id.co.pegadaian.diarium.controller.home.main_menu.personal_data.add_data;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import id.co.pegadaian.diarium.R;
import id.co.pegadaian.diarium.adapter.DataAddDataAdapter;
import id.co.pegadaian.diarium.adapter.DataTypeAddDataAdapter;
import id.co.pegadaian.diarium.model.DataAddDataModel;
import id.co.pegadaian.diarium.model.DataTypeAddDataModel;
import id.co.pegadaian.diarium.util.UserSessionManager;
import id.co.pegadaian.diarium.util.element.ProgressDialogHelper;

public class AddPersonalDataActivity extends AppCompatActivity {

    private List<DataAddDataModel> listmodelData;
    private DataAddDataModel modelData;
    private DataAddDataAdapter adapterData;

    UserSessionManager session;
    TextView tvet1,tvet2,tvTitleData,dateIssueIdentification,expireDateIdentification;
    EditText et1,et2;
    Button btnSubmit;
    ConstraintLayout constrainAddData;
    LinearLayout linearIdentification;
    Spinner dataTypeSpinner, dataSpinner;
    String chosenItem, chosenItemData, tgl, ObjectCode, identificationObjectCode,selectedItem,userFullName,intentFamName;

    private ProgressDialogHelper progressDialogHelper;
    final Calendar myCalendar = Calendar.getInstance();

    private ArrayList<String> listData  = new ArrayList<>();
    private ArrayList<DataTypeAddDataModel> spinnerListDataType;
    private DataTypeAddDataAdapter spinnerDataTypeAdapter;

    private ArrayList<String> listDataCode  = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_personal_data);
        setTitle("Add Personal Data");

        AndroidNetworking.initialize(getApplicationContext());
        progressDialogHelper = new ProgressDialogHelper();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        tgl = sdf.format(new Date());

        session = new UserSessionManager(AddPersonalDataActivity.this);

//        final TextView selectedItems=(TextView)findViewById(R.id.txt);

        linearIdentification = findViewById(R.id.linear_for_identification);
        constrainAddData = findViewById(R.id.cl_add_personaldata);
        tvet1 = findViewById(R.id.tv_title_et1);
        tvet2 = findViewById(R.id.tv_title_et2);
        tvTitleData = findViewById(R.id.tv_title_data);
        et1 = findViewById(R.id.et1);
        et2 = findViewById(R.id.et2);
        dateIssueIdentification = findViewById(R.id.tv_dateissue_identification);
        expireDateIdentification = findViewById(R.id.tv_expiredate_identification);
        dataTypeSpinner = findViewById(R.id.spinner_datatype);
        dataSpinner = findViewById(R.id.spinner_data);
        btnSubmit = findViewById(R.id.btn_submit_addData);

        userFullName = session.getUserFullName();
        System.out.println("check user full name value: " + userFullName);

        Intent intent = getIntent();
        intentFamName = intent.getStringExtra("name");
        System.out.println("cekricek name: " + intentFamName);

//      SPINNER SEBELUM HINT

//        spinnerListDataType = new ArrayList<>();
//        spinnerListDataType.add(new DataTypeAddDataModel("Communication"));
//        spinnerListDataType.add(new DataTypeAddDataModel("Identification"));
//        spinnerDataTypeAdapter = new DataTypeAddDataAdapter(AddPersonalDataActivity.this,spinnerListDataType);
//        dataTypeSpinner.setAdapter(spinnerDataTypeAdapter);

//        dataTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                System.out.println("APIPOSITION" + Integer.toString(position));
//
//                try {
//
//                    DataTypeAddDataModel clickedItem = (DataTypeAddDataModel) parent.getItemAtPosition(position);
//                    chosenItem = clickedItem.getDataType();
//                    System.out.println("Print chosen item" + chosenItem);
//
//                    if (chosenItem.equals("Communication")) {
//                        tvTitleData.setText("Tipe Alat Komunikasi");
//                        tvet1.setText("Alat Komunikasi Order");
//                        et1.setInputType(InputType.TYPE_CLASS_NUMBER);
//                        tvet2.setText("Nomor Alat Komunikasi");
//                        getCommunicationList();
//                    }
//                    else {
//                        tvTitleData.setText("Tipe Identitas");
//                        tvet1.setText("Place Issue");
//                        et1.setInputType(InputType.TYPE_CLASS_TEXT);
//                        tvet2.setText("Identification Number");
//                        getIdentificationList();
//                        linearIdentification.setVisibility(View.VISIBLE);
//                        popUpDateIssuePicker();
//                        popUpExpireDatePicker();
//
//                    }
//                } catch (Exception e){
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });

//      SPINNER SEBELUM HINT


        //      spinner setelah hint

        String[] dataTypes = new String[]{
                "Select data type...",
                "Communication",
                "Identification"
        };

        final List<String> dataTypeList = new ArrayList<>(Arrays.asList(dataTypes));

        final ArrayAdapter<String> spinnerhintDataTypeAdapter = new ArrayAdapter<String>
                (this,R.layout.spinner_datatype_addpersonaldata,dataTypeList){
            @Override
            public boolean isEnabled(int position) {
                if (position == 0){
                    return false;
                } else {
                    return true;
                }
            }

            @Override
            public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = super.getDropDownView(position,convertView,parent);
                TextView tv = (TextView) view;
                if (position == 0){
                    tv.setTextColor(Color.GRAY);
                } else {
                    tv.getResources().getColor(R.color.black);
                }
                return view;
            }
        };

        spinnerhintDataTypeAdapter.setDropDownViewResource(R.layout.spinner_datatype_addpersonaldata);
        dataTypeSpinner.setAdapter(spinnerhintDataTypeAdapter);

        dataTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedItem = (String) parent.getItemAtPosition(position);
                System.out.println("Print selected item setelah spinner ada hint: " + selectedItem +
                "\n" + "dengan position: " + position);
                if (position == 1){
                    tvTitleData.setText("Tipe Alat Komunikasi");
                    tvTitleData.setVisibility(View.VISIBLE);
                    tvet1.setText("Alat Komunikasi Order");
                    tvet1.setVisibility(View.VISIBLE);
                    et1.setInputType(InputType.TYPE_CLASS_NUMBER);
                    et1.setVisibility(View.VISIBLE);
                    et2.setVisibility(View.VISIBLE);
                    tvet2.setText("Nomor Alat Komunikasi");
                    tvet2.setVisibility(View.VISIBLE);
                    dataSpinner.setVisibility(View.VISIBLE);
                    linearIdentification.setVisibility(View.GONE);
                    btnSubmit.setVisibility(View.VISIBLE);
                    getCommunicationList();
                } else if (position == 2){
                    tvTitleData.setText("Tipe Identitas");
                    tvTitleData.setVisibility(View.VISIBLE);
                    tvet1.setText("Place Issue");
                    tvet1.setVisibility(View.VISIBLE);
                    et2.setVisibility(View.VISIBLE);
                    et1.setInputType(InputType.TYPE_CLASS_TEXT);
                    et1.setVisibility(View.VISIBLE);
                    tvet2.setText("Identification Number");
                    tvet2.setVisibility(View.VISIBLE);
                    getIdentificationList();
                    linearIdentification.setVisibility(View.VISIBLE);
                    dataSpinner.setVisibility(View.VISIBLE);
                    btnSubmit.setVisibility(View.VISIBLE);
                    popUpDateIssuePicker();
                    popUpExpireDatePicker();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //      spinner setelah hint


//        getCommunicationList();



        dataSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                chosenItemData = parent.getItemAtPosition(position).toString();
                System.out.println("Print chosen Item Data: " + chosenItemData);

                ObjectCode = listmodelData.get(position).getDataCode();
                System.out.println("Print chosen item data code: " + ObjectCode);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(AddPersonalDataActivity.this);
                builder.setTitle("Confirm");
                builder.setMessage("Are you sure to submit this personal data ?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        System.out.println("tes selected item data type sebelum post: " + selectedItem);
                        if(selectedItem.equals("Communication")){
                            if (intentFamName.equals("")){
                                submitPostCommunicationPersonal();
                            } else {
                                submitPostCommunicationFamily();
                            }
                        }else {

//                          check if expire date is null
                            if (expireDateIdentification.getText().toString().equals("")){
                                expireDateIdentification.setText("2100-12-31");
                            }
                            System.out.println("test expire date indentification if null: " + expireDateIdentification.getText());
//                          check if expire date is null

                            if (intentFamName.equals("")){
                                submitPostIdentificationPersonal();
                            } else {
                                submitPostIdentificationFamily();
                            }
                        }
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
    }


    private void getCommunicationList(){
        progressDialogHelper.showProgressDialog(AddPersonalDataActivity.this,"Load Data...");

        String getComListUrl = session.getServerURLHCIS()+"ldap/api/"+"object?begin_date_lte="+tgl+"&end_date_gt="+tgl+
                "&business_code="+session.getUserBusinessCode()+"&object_type=COMTY";
        System.out.println("get communication list spinner add personal data: " + getComListUrl);

        AndroidNetworking.get(getComListUrl)
                .addHeaders("Accept","application/json")
                .addHeaders("Content-Type","application/json")
                .addHeaders("Authorization","Bearer "+session.getToken())
                //.addJSONObjectBody(body)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println("Response getCommunication: " + response);
                        // do anything with response
                        try {
                            if (response.getInt("status")==200){
                                listmodelData = new ArrayList<DataAddDataModel>();
                                listData  = new ArrayList<String>();
//                                listDataCode = new ArrayList<String>();
                                JSONArray array = response.getJSONArray("data");
                                for (int a=0;a<array.length();a++) {
                                    JSONObject obj_name = array.getJSONObject(a);
                                    String jsonarray_name = obj_name.getString("object_name");
                                    String jsonarray_code = obj_name.getString("object_code");
                                    System.out.println("Tes object name: " + jsonarray_name + "\n"
                                    + "Tes object name:" + jsonarray_code);

                                    listData.add(jsonarray_name);
//                                    listDataCode.add(jsonarray_code);

                                    modelData = new DataAddDataModel(jsonarray_name,jsonarray_code);
                                    listmodelData.add(modelData);
//                                    adapterData = new DataAddDataAdapter(AddPersonalDataActivity.this,listmodelData);
                                }

                                ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>
                                        (AddPersonalDataActivity.this, R.layout.spinner_item_list, listData);
                                spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
                                dataSpinner.setAdapter(spinnerArrayAdapter);
                                dataSpinner.setSelected(false);  // must
//                                dataSpinner.setSelection(Pos_year,false);
                            }
                            else {

                            }
                            progressDialogHelper.dismissProgressDialog(AddPersonalDataActivity.this);

                        } catch (Exception e){
                            e.printStackTrace();
                            progressDialogHelper.dismissProgressDialog(AddPersonalDataActivity.this);

                        }

                    }
                    @Override
                    public void onError(ANError error) {
                        progressDialogHelper.dismissProgressDialog(AddPersonalDataActivity.this);
                        System.out.println("get my post error"+ error.getErrorBody());
                        System.out.println("get my post error"+ error);
                        System.out.println("get my post error"+ error.getResponse());
                    }
                });
//        progressDialogHelper.dismissProgressDialog(AddPersonalDataActivity.this);
    }





    private void getIdentificationList(){
//        progressDialogHelper.showProgressDialog(AddPersonalDataActivity.this,"Load Data...");
        AndroidNetworking.get("https://hcis.pegadaian.co.id/ldap/api/"+"object?begin_date_lte="+tgl+"&end_date_gt="+tgl+
                "&business_code="+session.getUserBusinessCode()+"&object_type=IDTYP")
                .addHeaders("Accept","application/json")
                .addHeaders("Content-Type","application/json")
                .addHeaders("Authorization","Bearer "+session.getToken())
                //.addJSONObjectBody(body)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println("Response getCommunication: " + response);
                        // do anything with response
                        try {
                            if (response.getInt("status")==200){
                                listmodelData = new ArrayList<DataAddDataModel>();
                                listData  = new ArrayList<>();
//                                listDataCode = new ArrayList<>();
                                JSONArray array = response.getJSONArray("data");
                                for (int a=0;a<array.length();a++) {
                                    JSONObject obj_name = array.getJSONObject(a);
                                    String jsonarray_name = obj_name.getString("object_name");
                                    String jsonarray_code = obj_name.getString("object_code");
                                    System.out.println("Tes object code identification: " + jsonarray_name + jsonarray_code);

                                    listData.add(jsonarray_name);
//                                    listDataCode.add(jsonarray_code);
                                    modelData = new DataAddDataModel(jsonarray_name,jsonarray_code);
                                    listmodelData.add(modelData);
                                }

                                ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>
                                        (AddPersonalDataActivity.this, R.layout.spinner_item_list, listData);
                                spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
                                dataSpinner.setAdapter(spinnerArrayAdapter);
                                dataSpinner.setSelected(false);  // must
//                                dataSpinner.setSelection(Pos_year,false);
                            }
                            else {

                            }
//                            progressDialogHelper.dismissProgressDialog(AddPersonalDataActivity.this);

                        } catch (Exception e){
                            e.printStackTrace();
//                            progressDialogHelper.dismissProgressDialog(AddPersonalDataActivity.this);

                        }

                    }
                    @Override
                    public void onError(ANError error) {
                        progressDialogHelper.dismissProgressDialog(AddPersonalDataActivity.this);
                        System.out.println("get my post error"+ error.getErrorBody());
                        System.out.println("get my post error"+ error);
                        System.out.println("get my post error"+ error.getResponse());
                    }
                });
//        progressDialogHelper.dismissProgressDialog(AddPersonalDataActivity.this);
    }


    private void submitPostCommunicationPersonal () {
        System.out.println("print para value: "+ "\n" + "et1: "+ et1.getText().toString() + "\n" + "et1: " + et2.getText().toString());
        progressDialogHelper.showProgressDialog(AddPersonalDataActivity.this, "Logging in..");
        JSONObject body = new JSONObject();
        try {
            body.put("begin_date",tgl);
            body.put("end_date","9999-12-31");
            body.put("business_code",session.getUserBusinessCode());
            body.put("personnel_number",session.getUserNIK());
            body.put("communication_type",ObjectCode);
            body.put("communication_order",et1.getText().toString());
            body.put("communication_number",et2.getText().toString());
            body.put("change_user",session.getUserNIK());
        }catch (JSONException e){
            System.out.println(e);
        }
        System.out.println("Body Submit Communication: "+body);
        String communicationUrl = session.getServerURLHCIS()+"hcis/api/communication";
        Log.d("post communication", communicationUrl);
        System.out.println("post communication :" + communicationUrl );

        AndroidNetworking.post( communicationUrl)
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
                        System.out.println("response submit communication: "+response);
                        try {
                            if(response.getInt("status")==200){
                                progressDialogHelper.dismissProgressDialog(AddPersonalDataActivity.this);
                                System.out.println("On response message: "+response.getString("message"));
//                                Snackbar.make(constrainAddData, response.getString("message"), Snackbar.LENGTH_LONG).show();
                                Toast.makeText(AddPersonalDataActivity.this,response.getString("message"),Toast.LENGTH_LONG).show();
                                finish();
//                                Intent i = new Intent(AddPersonalDataActivity.this, PersonalDataActivity.class);
//                                startActivity(i);
                            }else{
                                progressDialogHelper.dismissProgressDialog(AddPersonalDataActivity.this);
                            }
                        }catch (Exception e){
                            progressDialogHelper.dismissProgressDialog(AddPersonalDataActivity.this);
                            System.out.println(e);
                        }
                    }
                    @Override
                    public void onError(ANError error) {
                        progressDialogHelper.dismissProgressDialog(AddPersonalDataActivity.this);
                        String jsonError = error.getErrorBody();
                        System.out.println("response token"+ jsonError);
                        Snackbar.make(constrainAddData, error.getResponse().message(), Snackbar.LENGTH_LONG).show();
                        if (error.getResponse().message().equals("Unauthorized") ){
                            Snackbar.make(constrainAddData,"Wrong email or password", Snackbar.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private void submitPostIdentificationPersonal() {

        System.out.println("print para value: "+ "\n" + "et1: "+ et1.getText().toString() + "\n" + "et1: " + et2.getText().toString()
                + "\n" + "date issue: " + dateIssueIdentification.getText().toString() + "\n" + "expire date: " + expireDateIdentification.getText().toString());
        progressDialogHelper.showProgressDialog(AddPersonalDataActivity.this, "Adding data..");
        JSONObject body = new JSONObject();
        try {
            body.put("begin_date",tgl);
            body.put("end_date","9999-12-31");
            body.put("business_code",session.getUserBusinessCode());
            body.put("personnel_number",session.getUserNIK());
            body.put("identification_type",ObjectCode);
            body.put("identification_number",et2.getText().toString());
            body.put("date_issue",dateIssueIdentification.getText().toString());
            body.put("expire_date",expireDateIdentification.getText().toString());
            body.put("place_issue",et1.getText().toString());
            body.put("change_user",session.getUserNIK());
        }catch (JSONException e){
            System.out.println(e);
        }
        System.out.println("Body Submit Identification: "+body);
        String urlSubmitPostIden = session.getServerURLHCIS()+"hcis/api/identification";
        System.out.println("url submit post identification: " + urlSubmitPostIden);
        AndroidNetworking.post( urlSubmitPostIden)
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
                                progressDialogHelper.dismissProgressDialog(AddPersonalDataActivity.this);
//                                Snackbar.make(constrainAddData, response.getString("message"), Snackbar.LENGTH_LONG).show();
                                Toast.makeText(AddPersonalDataActivity.this,response.getString("message"),Toast.LENGTH_LONG).show();
                                finish();
//                                Intent i = new Intent(AddPersonalDataActivity.this, PersonalDataActivity.class);
//                                startActivity(i);
                            }else{
                                progressDialogHelper.dismissProgressDialog(AddPersonalDataActivity.this);
                                Toast.makeText(AddPersonalDataActivity.this,response.getString("message"),Toast.LENGTH_LONG).show();
                                System.out.println("On response message: "+response.getString("message"));
                            }
                        }catch (Exception e){
                            progressDialogHelper.dismissProgressDialog(AddPersonalDataActivity.this);
                            Toast.makeText(AddPersonalDataActivity.this,e.getMessage(),Toast.LENGTH_LONG).show();
                            System.out.println(e);
                        }
                    }
                    @Override
                    public void onError(ANError error) {
                        progressDialogHelper.dismissProgressDialog(AddPersonalDataActivity.this);
                        String jsonError = error.getErrorBody();
                        System.out.println("response token"+ jsonError);
                        Snackbar.make(constrainAddData, error.getResponse().message(), Snackbar.LENGTH_LONG).show();
                        if (error.getResponse().message().equals("Unauthorized") ){
                            Snackbar.make(constrainAddData,"Wrong email or password", Snackbar.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private void submitPostCommunicationFamily(){

    }

    private void submitPostIdentificationFamily(){

    }

    private void popUpExpireDatePicker() {
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

        expireDateIdentification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(AddPersonalDataActivity.this, expireDate, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

    }

    private void popUpDateIssuePicker() {
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

        dateIssueIdentification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(AddPersonalDataActivity.this, dateIssue, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

    }

    private void updateLabelDateIssue() {
        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        dateIssueIdentification.setText(sdf.format(myCalendar.getTime()));
    }

    private void updateLabelExpireDate() {
        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        expireDateIdentification.setText(sdf.format(myCalendar.getTime()));

    }



    private void getDataCommunication(){

        AndroidNetworking.get(session.getServerURL()+"users/timelineDetailPosting/limit/1000/nik/"+session.getUserNIK()+"/buscd/"+session.getUserBusinessCode())
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
                        System.out.println(response+"ktjbkgrjbhymyposting");

                    }
                    @Override
                    public void onError(ANError error) {
                        System.out.println("get my post error"+ error.getErrorBody());
                        System.out.println("get my post error"+ error);
                        System.out.println("get my post error"+ error.getResponse());
                    }
                });
    }

    private void getDataIdentification(){

        AndroidNetworking.get(session.getServerURL()+"users/timelineDetailPosting/limit/1000/nik/"+session.getUserNIK()+"/buscd/"+session.getUserBusinessCode())
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
                        System.out.println(response+"ktjbkgrjbhymyposting");

                    }
                    @Override
                    public void onError(ANError error) {
                        System.out.println("get my post error"+ error.getErrorBody());
                        System.out.println("get my post error"+ error);
                        System.out.println("get my post error"+ error.getResponse());
                    }
                });

    }
}