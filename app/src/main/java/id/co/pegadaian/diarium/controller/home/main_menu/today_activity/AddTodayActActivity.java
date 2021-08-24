package id.co.pegadaian.diarium.controller.home.main_menu.today_activity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import id.co.pegadaian.diarium.R;
import id.co.pegadaian.diarium.util.UserSessionManager;

public class AddTodayActActivity extends AppCompatActivity {

    Typeface font, fontbold;
    ArrayList<String> listId;
    ArrayList<String> listItems;
    ArrayList<String> listItemsStartDate;
    ArrayList<String> listItemsEndDate;
    ArrayAdapter<String> adapter;
    int countdone = 0;
    int id_selanjutnya=0;
    int id_lanjutan=0;
    boolean jenis = true;
    TextView tvAdd, tvStartDate, tvEndDate;
    EditText etActivity;
    final Calendar myCalendar = Calendar.getInstance();
    ListView list;
    LinearLayout rootView;
    Button btnFinish;
    UserSessionManager session;
    String id_global, startDateCheck, endDateCheck;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_today);
        session = new UserSessionManager(AddTodayActActivity.this);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        font = Typeface.createFromAsset(getApplication().getAssets(), "fonts/Nexa Light.otf");
        fontbold = Typeface.createFromAsset(getApplication().getAssets(), "fonts/Nexa Bold.otf");
        rootView = findViewById(R.id.lin_login);
        tvAdd = findViewById(R.id.tvAdd);
        list = findViewById(R.id.list_activity);
        etActivity = findViewById(R.id.etActivity);

        TextView a = findViewById(R.id.add);
        btnFinish = findViewById(R.id.btnFinish);

        a.setTypeface(fontbold);
        etActivity.setTypeface(font);
        btnFinish.setTypeface(fontbold);


        tvAdd.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                String aktifitas = etActivity.getText().toString();
                if (aktifitas.equals("") || aktifitas.isEmpty()) {
                    Snackbar.make(rootView, "Activity must be input !", Snackbar.LENGTH_LONG).show();
                } else {
                    AddActivity(aktifitas);
                }
            }
        });

        btnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                    session.setStat("CI");
//                Toast.makeText(AddTodayActActivity.this, "lenght : "+list.getCount(), Toast.LENGTH_SHORT).show();
//                generateActivityId();

                System.out.println("tes listview size() add today activity:" + listItems.size());

                if (listItems.size() == 0){
                    Toast.makeText(AddTodayActActivity.this, "Please fill in the blank", Toast.LENGTH_SHORT).show();
                } else {
                    submitTodayActivity();
                }

//                System.out.println(listItems.size()+"JUMLAHNYA");
//                for (int i=0; i<listItems.size();i++) {
//                    String activity_title = listItems.get(i);
//                    String activity_start = listItemsStartDate.get(i);
//                    String activity_finish = listItemsEndDate.get(i);
//
//                    String id = id_global;
//                }
            }
        });

        listItems = new ArrayList<String>();
        listId = new ArrayList<String>();
        listItemsStartDate = new ArrayList<String>();
        listItemsEndDate = new ArrayList<String>();

        adapter = new ArrayAdapter<String>(AddTodayActActivity.this,
                android.R.layout.simple_list_item_1, listItems);
        list.setAdapter(adapter);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void AddActivity(final String aktifitas) {
        final Dialog dialog = new Dialog(AddTodayActActivity.this);
        dialog.setContentView(R.layout.popup_add_activity);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        dialog.getWindow().setAttributes(lp);
        dialog.setTitle("Input Code Here");

        tvStartDate = dialog.findViewById(R.id.tvPopupStartDate);
        tvEndDate =  dialog.findViewById(R.id.tvPopupEndDate);
        Button btnSave = (Button) dialog.findViewById(R.id.btnSave);
        Button btnCancel = (Button) dialog.findViewById(R.id.btnCancel);
        TextView tvActivity = (TextView) dialog.findViewById(R.id.tvActivity);

        tvActivity.setText(aktifitas);
        dialog.show();
        dialog.setCancelable(false);

        final DatePickerDialog.OnDateSetListener dateStart = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabelStartDate();
            }
        };

        final DatePickerDialog.OnDateSetListener dateEnd = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                updateLabelEndDate();
            }
        };

        tvStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(AddTodayActActivity.this, dateStart, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        tvEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(AddTodayActActivity.this, dateEnd, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String S = tvStartDate.getText().toString();
                String E = tvEndDate.getText().toString();

                System.out.println("s:::: " + S + " " + "e:::" + E);
                if (S.equals("")||E.equals("")) {
                    Toast.makeText(AddTodayActActivity.this, "Please input date", Toast.LENGTH_SHORT).show();
                } else {
                    if(checkDate(S,E)){
                        listItems.add(aktifitas);
                        listItemsStartDate.add(S);
                        listItemsEndDate.add(E);
                        adapter.notifyDataSetChanged();
                        dialog.dismiss();
                        etActivity.getText().clear();
                    }
                    else{
                        Toast.makeText(AddTodayActActivity.this, "Start date cannot be greater than end date", Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });
    }

    private Boolean checkDate (String startDate, String endDate){
        boolean status = true;

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date sDate = sdf.parse(startDate);
            Date eDate = sdf.parse(endDate);

            if (sDate.after(eDate)) {
                status = false;
                System.out.println("check end date after start date");
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return status;
    }

    private void generateActivityId() {
        SimpleDateFormat tgl = new SimpleDateFormat("yyyy-MM-dd");
        String tRes = tgl.format(new Date());
//        progressDialogHelper.showProgressDialog(CheckinActivity.this, "Getting data...");
        System.out.println("generate id: " + session.getServerURL() + "users/generateIDActivity");
        AndroidNetworking.get(session.getServerURL() + "users/generateIDActivity")
                .addHeaders("Accept", "application/json")
                .addHeaders("Content-Type", "application/json")
                .addHeaders("Authorization", session.getToken())
                //.addJSONObjectBody(body)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println("Generate Activity Id:\n" + session.getServerURL() + "users/generateIDActivity\n"+ response);
                        // do anything with response
                        try {
                            if (response.getInt("status") == 200) {
                                String idhasil = response.getJSONObject("data").getString("id");
                                System.out.println(idhasil+"IDNYAADALAH");

                                id_selanjutnya = Integer.parseInt(idhasil);
                                submitTodayActivity();
//                                if (id_lanjutan<Integer.parseInt(idhasil)) {
//                                    id_lanjutan = Integer.parseInt(idhasil);
//                                } else {
////                                    submitTodayActivity(id_selanjutnya,activity_title,activity_start,activity_finish);
//                                }

                            } else {
                                System.out.println("masuk ke ke else generateid: " + response);
//                                popUpLogin();
                            }
//                            progressDialogHelper.dismissProgressDialog(CheckinActivity.this);
                        } catch (Exception e) {
                            System.out.println("masuk ke catch generateid: " + response + "\n" + "exception generateid: " + e);
//                            progressDialogHelper.dismissProgressDialog(CheckinActivity.this);
                            System.out.println(e);
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        System.out.println("masuk ke onError generateid: " + error);
//                        progressDialogHelper.dismissProgressDialog(CheckinActivity.this);
                        System.out.println(error);
                    }
                });
    }

    private void submitTodayActivity() {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat tgl = new SimpleDateFormat("yyyy-MM-dd");
        String tRes = tgl.format(new Date());
        @SuppressLint("SimpleDateFormat") SimpleDateFormat jamParam = new SimpleDateFormat("yyyMMddHHmmss");
        String jamResParam = jamParam.format(new Date());

        JSONObject obj = null;
        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < listItems.size(); i++) {
            @SuppressLint("SimpleDateFormat") SimpleDateFormat jamId = new SimpleDateFormat("yyyMMddHHmmss");
            String jamResId = jamId.format(new Date());
            obj = new JSONObject();
            try {
                obj.put("begin_date", tRes);
                obj.put("end_date", "9999-12-31");
                obj.put("business_code", session.getUserBusinessCode());
                obj.put("personal_number", session.getUserNIK());
                obj.put("activity_id", "0");
                obj.put("activity_type", "00");
                obj.put("activity_title", listItems.get(i));
                obj.put("activity_start", listItemsStartDate.get(i));
                obj.put("activity_finish", listItemsEndDate.get(i));
                obj.put("approval_status", "1");
                obj.put("change_user", session.getUserNIK());

            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            jsonArray.put(obj);
        }

        System.out.println(jsonArray + "3nkej3n3eAktivitas");
        System.out.println("test post today activity: " + session.getServerURL()+"users/activity/"+id_selanjutnya+"/nik/"+session.getUserNIK()+"/buscd/"+session.getUserBusinessCode());
        AndroidNetworking.post(session.getServerURL()+"users/activity/"+id_selanjutnya+"/nik/"+session.getUserNIK()+"/buscd/"+session.getUserBusinessCode())
                .addHeaders("Accept","application/json")
                .addHeaders("Content-Type","application/json")
                .addHeaders("Authorization",session.getToken())
                .addJSONArrayBody(jsonArray)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // do anything with response
                        System.out.println(response+"edknwkjrAktivitas");
                        try {
                            if(response.getInt("status")==200){
                                Toast.makeText(AddTodayActActivity.this, "Success add activity, happy work :)", Toast.LENGTH_SHORT).show();
                                finish();
//                                Intent a = new Intent(AddTodayActActivity.this, HomeActivity.class);
//                                a.putExtra("key", "asd");
//                                startActivity(a);

//                                countdone++;
//                                id_selanjutnya++;
//                                if (countdone==listItems.size()) {
//
//                                }
                            }else {
                                Snackbar.make(rootView,"Something wrong", Snackbar.LENGTH_LONG).show();
                                System.out.println("masuk ke else submit today activity: " + response);
                            }
                        }catch (Exception e){
                            System.out.println(e);
                            System.out.println("masuk ke catch submit today activity: " + response + "\n" + "exception submit today activity: " + e);
                        }
                    }
                    @Override
                    public void onError(ANError error) {
                        System.out.println(error);
                        System.out.println("masuk ke onError submit today activity: " + error);
                    }
                });
    }

    private void updateLabelStartDate() {
        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        tvStartDate.setText(sdf.format(myCalendar.getTime()));
    }

    private void updateLabelEndDate() {
        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        tvEndDate.setText(sdf.format(myCalendar.getTime()));
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
