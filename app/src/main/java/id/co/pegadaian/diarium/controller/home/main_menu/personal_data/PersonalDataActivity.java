package id.co.pegadaian.diarium.controller.home.main_menu.personal_data;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import id.co.pegadaian.diarium.R;
import id.co.pegadaian.diarium.adapter.DataPersonalAdapter;
import id.co.pegadaian.diarium.controller.home.main_menu.personal_data.edit_data.editNewPersonalDataActivity;
import id.co.pegadaian.diarium.controller.home.main_menu.personal_data.familydata.FamilyDataOnly;
import id.co.pegadaian.diarium.controller.home.main_menu.personal_data.personaldata.PersonalDataList;
import id.co.pegadaian.diarium.model.PersonalDataModel;
import id.co.pegadaian.diarium.util.UserSessionManager;
import id.co.pegadaian.diarium.util.element.ProgressDialogHelper;


public class PersonalDataActivity extends AppCompatActivity {

    Typeface font,fontbold;
    private static final int REQUEST_READ_CONTACTS = 0;
    public static final int ENROLL_REQ_CODE = 12800;
    public static final int AUTH_REQ_CODE = 12801;

    String[] identity={
            "Nama",
            "NIK",
            "KTP",
            "KK",
            "SIM",
            "No BPJS",
    };
    String[] isi={
            "12345",
            "67891",
            "23456",
            "12345",
            "67891",
            "23456",
    };

    private List<PersonalDataModel> listModel;
    private PersonalDataModel model;
    private DataPersonalAdapter adapter;

//    Button btnFRRegis;
    TextView tvName, tvNIK, tvJob, tvKTP, tvRekening, tvNPWP, tvBPJS;
    private ProgressDialogHelper progressDialogHelper;
    UserSessionManager session;
    ListView lvDataPersonal;
    String dataType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_data);
        progressDialogHelper = new ProgressDialogHelper();
        session = new UserSessionManager(PersonalDataActivity.this);

        font = Typeface.createFromAsset(getApplication().getAssets(),"fonts/Nexa Light.otf");
        fontbold = Typeface.createFromAsset(getApplication().getAssets(),"fonts/Nexa Bold.otf");
//        btnFRChange = (Button) findViewById(R.id.btnFRChange);
//        btnFRRegis = (Button) findViewById(R.id.btnFRRegis);
        lvDataPersonal = (ListView) findViewById(R.id.listDataPersonal);


        TextView a = (TextView) findViewById(R.id.identity);
        tvName = (TextView) findViewById(R.id.tvTitle);
        tvNIK = (TextView) findViewById(R.id.tvNik);
        tvJob = (TextView) findViewById(R.id.tvJob);
        a.setTypeface(fontbold);
//        btnFRRegis.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                Toast.makeText(PersonalDataActivity.this, "not available at this time", Toast.LENGTH_SHORT).show();
//                Intent i = new Intent(PersonalDataActivity.this, FMActivity.class);
//                i.putExtra(FMActivity.USER_ID,session.getUserFaceCode());
//                i.putExtra(FMActivity.METHOD,FMActivity.METHOD_ENROLL);
//                startActivityForResult(i,ENROLL_REQ_CODE);
//            }
//        });

        tvName.setText(session.getUserFullName());
        tvNIK.setText(session.getUserNIK());
        tvJob.setText(session.getJob());
//        getMyIdentity();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("Data pribadi karyawan");
        arrayList.add("Data keluarga karyawan");
        ArrayAdapter arrayAdapter = new ArrayAdapter(this,R.layout.bg_outline_list,arrayList);
        lvDataPersonal.setAdapter(arrayAdapter);

        lvDataPersonal.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0){
                    Intent intent = new Intent(PersonalDataActivity.this, PersonalDataList.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(PersonalDataActivity.this, FamilyDataOnly.class);
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AUTH_REQ_CODE) {
            if (session.getFMResult() == Activity.RESULT_OK) {
//                Snackbar.make(rootView,"Your face has successfuly registered",Snackbar.LENGTH_LONG).show();
                // User enrolled successfully
                try {
                    System.out.println(session.getFMResponse());
                    System.out.println(session.getFMMessage());
                    JSONObject dat = new JSONObject(session.getFMMessage());
                    if(dat.getString("result").equals("VERIFIED")){
                        session.setFMResponse("-");
                        session.setFMMessage("-");
                        session.setFMResult(Activity.RESULT_CANCELED);
                        Toast.makeText(this, "Success on face register", Toast.LENGTH_SHORT).show();
//                        session.setLoginState(true);
//                        Intent i = new Intent(PersonalDataActivity.this, HomeActivity.class);
//                        i.putExtra("key", "none");
//                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                        startActivity(i);
                        finish();
                    }else{
                        new AlertDialog.Builder(PersonalDataActivity.this)
                                .setMessage("Your face is not valid")
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        session.setFMResponse("-");
                                        session.setFMMessage("-");
                                        session.setFMResult(Activity.RESULT_CANCELED);
                                    }

                                })
                                .show();
                    }
                }catch (Exception e){
                    System.out.println(e);
                    Toast.makeText(PersonalDataActivity.this,"Seems like you have connection problem ",Toast.LENGTH_SHORT).show();
                }


            } else if(resultCode == Activity.RESULT_CANCELED){
                //System.out.println(data.getStringExtra(FMActivity.MESSAGE));
                Toast.makeText(this, "Face registration failed, Try Again!", Toast.LENGTH_SHORT).show();
//                Snackbar.make(rootView,"Face registration failed, Try Again!",Snackbar.LENGTH_LONG).show();
                // Enrollment cancelled
            }else{
                Toast.makeText(this, "No activity found", Toast.LENGTH_SHORT).show();
//                Snackbar.make(rootView,"No activity found",Snackbar.LENGTH_LONG).show();
            }
        }

        if (requestCode == ENROLL_REQ_CODE) {
            if (session.getFMResult() == Activity.RESULT_OK) {
                try {
                    System.out.println(session.getFMResponse());
                    System.out.println(session.getFMMessage());
                    JSONObject dat = new JSONObject(session.getFMMessage());
                    if(dat.getBoolean("isModelReady")){
                        session.setFMResponse("-");
                        session.setFMMessage("-");
                        session.setFMResult(Activity.RESULT_CANCELED);
                        Toast.makeText(this, "sukses", Toast.LENGTH_SHORT).show();
//                        session.setLoginState(true);
//                        Intent i = new Intent(PersonalDataActivity.this, HomeActivity.class);
//                        i.putExtra("key", "none");
//                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                        startActivity(i);
                        finish();
                    }else{
                        new AlertDialog.Builder(PersonalDataActivity.this)
                                .setMessage("Your face is not valid, please check your camera and try again")
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        session.setFMResponse("-");
                                        session.setFMMessage("-");
                                        session.setFMResult(Activity.RESULT_CANCELED);
                                    }

                                })
                                .show();
                    }
                }catch (Exception e){
                    System.out.println(e);
                    Toast.makeText(PersonalDataActivity.this,"Seems like you have connection problem ",Toast.LENGTH_SHORT).show();

                }


            } else if(resultCode == Activity.RESULT_CANCELED){

                //System.out.println(data.getStringExtra(FMActivity.MESSAGE));
                Toast.makeText(this, "Face registration failed, Try Again!", Toast.LENGTH_SHORT).show();
//                Snackbar.make(rootView,"Face registration failed, Try Again!",Snackbar.LENGTH_LONG).show();
                // Enrollment cancelled
            }else{
                Toast.makeText(this, "No activity found", Toast.LENGTH_SHORT).show();
//                Snackbar.make(rootView,"No activity found",Snackbar.LENGTH_LONG).show();
            }
        }

    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
////        if (session.getUserNIK().equals(session.getTempPers())) {
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.menu_add_post, menu);
////    }
//        return true;
//    }
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.add_post:
//                Intent intent = new Intent(PersonalDataActivity.this, AddPersonalDataActivity.class);
////                    intent.putExtra("name",session.getUserFullName());
////                    intent.putExtra("email","diarium@telkom.co.id");
//                startActivity(intent);
//                return true;
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//    }


    private void getMyIdentity(){
        SimpleDateFormat tgl = new SimpleDateFormat("yyyy-MM-dd");
        String tRes = tgl.format(new Date());
        progressDialogHelper.showProgressDialog(PersonalDataActivity.this, "Getting data...");
        System.out.println("GETMYIDENTITY"+session.getServerURL()+"users/mydetailidentity/buscd/"+session.getUserBusinessCode());
        AndroidNetworking.get(session.getServerURL()+"users/mydetailidentity/buscd/"+session.getUserBusinessCode())
                .addHeaders("Accept","application/json")
                .addHeaders("Content-Type","application/json")
                .addHeaders("Authorization",session.getToken())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println("RESPONAVAILABLE"+response);
                        listModel = new ArrayList<PersonalDataModel>();

                        try {
                            if(response.getInt("status")==200){

                                JSONArray jsonArraycommunication = response.getJSONObject("data").getJSONArray("communication");
                                System.out.println("OY" + jsonArraycommunication.length());
                                for (int a=0;a<jsonArraycommunication.length();a++) {
                                    JSONObject objcommunication = jsonArraycommunication.getJSONObject(a);
                                    String value = objcommunication.getString("communication_number");
//                                    JSONObject objname = objcommunication.getJSONObject("communication_name");
                                    String name = objcommunication.getString("communication_name");
                                    String objIdentifier = objcommunication.getString("object_identifier");
                                    String objectCode = objcommunication.getString("communication_type");
                                    String type = "communication";

                                    model = new PersonalDataModel(name, value,objIdentifier,type,objectCode);
                                    listModel.add(model);
                                    System.out.println("name first" + a + name);
                                }

                                System.out.println("error disini : No Value for communication_type");
                                JSONArray jsonArrayidentification = response.getJSONObject("data").getJSONArray("identification");
                                for (int b=0;b<jsonArrayidentification.length();b++) {
                                    JSONObject objidentification = jsonArrayidentification.getJSONObject(b);
                                    String value = objidentification.getString("identification_number");
                                    String name = objidentification.getString("identification_name");
                                    String objIndentifier = objidentification.getString("object_identifier");
                                    String objectCode = objidentification.getString("identification_type");
                                    String type = "identification";
                                    model = new PersonalDataModel(name, value,objIndentifier,type,objectCode);
                                    listModel.add(model);
//                                    for (int o=0;o<namearray.length();o++) {
//                                        JSONObject objname = namearray.getJSONObject(o);
//                                        String name = objname.getString("name");
//
//                                    }
                                    System.out.println("name second : " + b + name);
                                }

                                JSONArray jsonArrayNPWP = response.getJSONObject("data").getJSONArray("npwp");
                                for (int b=0;b<jsonArrayidentification.length();b++){
                                    JSONObject objNpwp = jsonArrayNPWP.getJSONObject(b);
                                    String jsonarrayNpwp_name = "NPWP";
                                    String jsonarrayNpwp_number = objNpwp.getString("npwp_number");
                                    String objIndentifier = objNpwp.getString("object_identifier");
//                                    String objectCode = objNpwp.getString("communication_type");
                                    String objectCode = "00000";
                                    String type = "npwp";
                                    model = new PersonalDataModel(jsonarrayNpwp_name,jsonarrayNpwp_number,objIndentifier,type,objectCode);
                                    listModel.add(model);
                                    System.out.println("name third : " + jsonarrayNpwp_number);
//                                String jsonarrayNpwp_name = "NPWP";
//                                String jsonarrayNpwp_number = response.getJSONObject("data").getString("npwp");
                                }


                                int bpjs_kesehatan_length = response.getJSONObject("data").getJSONArray("bpjs_kesehatan").length();
                                if (bpjs_kesehatan_length>0){
                                    JSONObject obj = response.getJSONObject("data").getJSONArray("bpjs_kesehatan").getJSONObject(0);
                                    String value_bpjs_kesehatan = obj.getString("insurance_number");
                                    String objIndentifier = obj.getString("object_identifier");
//                                    String objectCode = obj.getString("communication_type");
                                    String objectCode = "00000";
                                    String type = "bpjs_kesehatan";
                                    model = new PersonalDataModel("BPJS Kesehatan", value_bpjs_kesehatan,objIndentifier,type,objectCode);
                                    listModel.add(model);
                                }

                                int bpjs_ketenagakerjaan_length = response.getJSONObject("data").getJSONArray("bpjs_ketenagakerjaan").length();
                                if (bpjs_ketenagakerjaan_length>0){
                                    JSONObject obj = response.getJSONObject("data").getJSONArray("bpjs_ketenagakerjaan").getJSONObject(0);
                                    String value_bpjs_ketenagakerjaan = obj.getString("bpjs_number");
                                    String objIndentifier = obj.getString("object_identifier");
//                                    String objectCode = obj.getString("communication_type");
                                    String objectCode = "00000";
                                    String type = "bpjs_ketenagakerjaan";
                                    model = new PersonalDataModel("BPJS Ketenagakerjaan", value_bpjs_ketenagakerjaan,objIndentifier,type,objectCode);
                                    listModel.add(model);
                                }

                                int arrayBankLength = response.getJSONObject("data").getJSONArray("bank_employee").length();
                                if (arrayBankLength>0){
                                    JSONArray jsonarraybank_employee = response.getJSONObject("data").getJSONArray("bank_employee");
                                    for (int a=0;a<jsonarraybank_employee.length();a++) {
                                        JSONObject objBank = jsonarraybank_employee.getJSONObject(a);
                                        String value = objBank.getString("account_number");
                                        String name = objBank.getString("bank_type");
                                        String name_edit = "No. Rekening " + name;
                                        String objIndentifier = objBank.getString("object_identifier");
//                                        String objectCode = objBank.getString("communication_type");
                                        String objectCode = "00000";
                                        String type = "bank_employee";
                                        model = new PersonalDataModel(name_edit, value,objIndentifier,type,objectCode);
                                        listModel.add(model);
                                        System.out.println("name sixth : " + name + value);
                                    }
                                }


                                System.out.println("MODEL PERSONAL:" + "\n" + listModel);

//                                adapter = new DataPersonalAdapter(PersonalDataActivity.this, listModel);
//                                lvDataPersonal.setAdapter(adapter);

                                progressDialogHelper.dismissProgressDialog(PersonalDataActivity.this);
                            }else{
                                progressDialogHelper.dismissProgressDialog(PersonalDataActivity.this);
                                System.out.println("error getMyData" + response.getString("message"));
                            }
                        }catch (Exception e){
                            progressDialogHelper.dismissProgressDialog(PersonalDataActivity.this);
                            System.out.println(" Error PERSONAL "+e);
                            System.out.println("MODEL PERSONAL:" + "\n" + listModel);

                        }

                        adapter = new DataPersonalAdapter(PersonalDataActivity.this, listModel);
                        lvDataPersonal.setAdapter(adapter);



                        lvDataPersonal.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                                String identityName = listModel.get(position).getName();
                                String identityValue = listModel.get(position).getValue();
                                String objectIdentifier = listModel.get(position).getObjectIdentifier();
                                String type = listModel.get(position).getType();
                                String objectCodeClicked = listModel.get(position).getObjectCode();
                                popupMenuActivity(identityName,identityValue,objectIdentifier,type,objectCodeClicked);
                            }
                        });
                    }
                    @Override
                    public void onError(ANError error) {
                        progressDialogHelper.dismissProgressDialog(PersonalDataActivity.this);
                        System.out.println( "Error personal " + error);
                    }
                });
    }

    private void popupMenuActivity(final String identityName, final String identityValue, final String identityObjIdentifier,
                                   final String identityType,final String codeObject) {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.popup_editdelete_personaldata);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        dialog.getWindow().setAttributes(lp);
        dialog.setTitle("Input Code Here");
        Button btnChange =(Button) dialog.findViewById(R.id.btnChange);
        Button btnDelete =(Button) dialog.findViewById(R.id.btnDelete);
        TextView tvTitle =(TextView) dialog.findViewById(R.id.tvTitle);
        tvTitle.setText("Update your "+identityName+" data");
        dialog.show();
        dialog.setCancelable(true);

//        if (activity_type.equals("03")) {
//            btnDelete.setVisibility(View.GONE);
//        }
//        else {
            btnDelete.setVisibility(View.VISIBLE);
            btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(PersonalDataActivity.this);
                    builder.setTitle("Confirm");
                    builder.setMessage("Are you sure to delete this personal data ?");
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            submitDelete(identityObjIdentifier,identityType);
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
                    dialog.dismiss();
                }
            });
//        }
        btnChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(PersonalDataActivity.this, editNewPersonalDataActivity.class);
                i.putExtra("personal_identity_name",identityName);
                i.putExtra("personal_identity_value",identityValue);
                i.putExtra("personal_identity_objidentifier",identityObjIdentifier);
                i.putExtra("personal_identity_type",identityType);
                i.putExtra("object_code",codeObject);
                startActivity(i);
                dialog.dismiss();
            }
        });
    }

    private void submitDelete(String personalIdentifier, String identityType) {
        System.out.println("ketikamasukdelete : "+personalIdentifier + "  " + identityType);
        String type = "";
        switch (identityType){
            case "communication":
                type = "communication";
//                AndroidNetworking.post(session.getServerURL()+"communication?object_identifier="+personalIdentifier)
//                        .addHeaders("Accept","application/json")
//                        .addHeaders("Content-Type","application/json")
////                .addHeaders("Authorization",session.getToken())
//                        .setPriority(Priority.MEDIUM)
//                        .build()
//                        .getAsJSONObject(new JSONObjectRequestListener() {
//                            @Override
//                            public void onResponse(JSONObject response) {
//                                // do anything with response
//                                System.out.println(response+"HASILDELETENYA");
//                                try {
//                                    if(response.getInt("status")==200){
//                                        Toast.makeText(PersonalDataActivity.this, "Success delete activity", Toast.LENGTH_SHORT).show();
//                                        adapter.notifyDataSetChanged();
////                                getTodayActivityList(session.getTodayActivity());
//                                    } else {
//                                        Toast.makeText(PersonalDataActivity.this, "error", Toast.LENGTH_SHORT).show();
//                                    }
//                                }catch (Exception e){
//                                    System.out.println(e);
//                                }
//                            }
//                            @Override
//                            public void onError(ANError error) {
//                                System.out.println(error);
//                            }
//                        });
                break;

            case "identification":
                type = "identification";
                break;
            case "bpjs_ketenagakerjaan":
                type = "jamsostek";
                break;
            case "bpjs_kesehatan":
                type = "insurance";
                break;
            case "bank_employee":
                type = "bankemployee";
                break;
            case "npwp":
                type = "tax";
                break;
        }

        System.out.println("Test type value : " + type);

        AndroidNetworking.delete(session.getServerURL()+type+"?object_identifier="+personalIdentifier)
                .addHeaders("Accept","application/json")
                .addHeaders("Content-Type","application/json")
//                .addHeaders("Authorization",session.getToken())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // do anything with response
                        System.out.println(response+"HASILDELETENYA");
                        try {
                            if(response.getInt("status")==200){
                                Toast.makeText(PersonalDataActivity.this, "Success delete personal data", Toast.LENGTH_SHORT).show();
                                adapter.notifyDataSetChanged();
//                                getMyIdentity();
//                                getTodayActivityList(session.getTodayActivity());
                            } else {
                                Toast.makeText(PersonalDataActivity.this, "error", Toast.LENGTH_SHORT).show();
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

    @Override
    protected void onResume() {
        super.onResume();
//        getMyIdentity();
    }
}
