package id.co.pegadaian.diarium.controller.home.main_menu.report;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
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
import java.util.Calendar;
import java.util.List;

import id.co.pegadaian.diarium.R;
import id.co.pegadaian.diarium.adapter.ActivityReportAdapter;
import id.co.pegadaian.diarium.adapter.CustomAdapter;
import id.co.pegadaian.diarium.controller.login.LoginActivity;
import id.co.pegadaian.diarium.controller.home.main_menu.today_activity.CommentActivity;
import id.co.pegadaian.diarium.model.ReportActivityModel;
import id.co.pegadaian.diarium.util.UserSessionManager;
import id.co.pegadaian.diarium.util.element.ProgressDialogHelper;

public class ReportActivity extends AppCompatActivity {

    String[] spinnerTitles;
    String[] typeId;
    int[] spinnerImages;
    Spinner mSpinner;
    private boolean isUserInteracting;
    TextView tvNull;
    private static final String TAG = "ReportActivity";

    Typeface font,fontbold;
    private int mYear,mMonth,mDay;
    private ArrayList<String> mNames = new ArrayList<String>();
    private ArrayList<Integer> mImageUrls = new ArrayList<Integer>();


    UserSessionManager session;
    private ProgressDialogHelper progressDialogHelper;
    LinearLayout rootView;
    ListView listReport;
    private List<ReportActivityModel> listModel;
    private ReportActivityModel model;
    private ActivityReportAdapter adapter;
    TextView tvFrom, tvUntil;
//    final Calendar myCalendarFrom = Calendar.getInstance();
//    final Calendar myCalendarUntil = Calendar.getInstance();
    Calendar cFrom = Calendar.getInstance();
    Calendar cUntil = Calendar.getInstance();


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        font = Typeface.createFromAsset(getApplication().getAssets(),"fonts/Nexa Light.otf");
        fontbold = Typeface.createFromAsset(getApplication().getAssets(),"fonts/Nexa Bold.otf");

        session = new UserSessionManager(ReportActivity.this);
        progressDialogHelper = new ProgressDialogHelper();

        listReport = findViewById(R.id.list_activity);
        TextView a = (TextView) findViewById(R.id.a);
        TextView a1 = (TextView) findViewById(R.id.pickdate2);
        TextView a11 = (TextView) findViewById(R.id.pickdate3);
        TextView a2 = (TextView) findViewById(R.id.b);
        tvNull = (TextView) findViewById(R.id.tvNull);
        tvUntil = (TextView) findViewById(R.id.tvUntil);
        tvFrom= (TextView) findViewById(R.id.tvFrom);

        a.setTypeface(fontbold);
        a1.setTypeface(fontbold);
        a11.setTypeface(fontbold);
        a2.setTypeface(fontbold);
        tvFrom.setTypeface(fontbold);
        tvUntil.setTypeface(fontbold);

        //========================================================================================== SET CURRENT -7 FROM DATE
        cFrom.add(Calendar.DAY_OF_MONTH,-7);
        int dayFrom  = cFrom.get(Calendar.DAY_OF_MONTH);
        int monthFrom = cFrom.get(Calendar.MONTH);
        String paramMonthFrom = null ;
        int yearFrom = cFrom.get(Calendar.YEAR);
        String monthResultFrom = null;
        switch (monthFrom) {
            case Calendar.JANUARY:
                monthResultFrom = "JAN";
                paramMonthFrom = "01";
                break;
            case Calendar.FEBRUARY:
                monthResultFrom = "FEB";
                paramMonthFrom = "02";
                break;
            case Calendar.MARCH:
                monthResultFrom = "MAR";
                paramMonthFrom = "03";
                break;
            case Calendar.APRIL:
                monthResultFrom = "APR";
                paramMonthFrom = "04";
                break;
            case Calendar.MAY:
                monthResultFrom = "MAY";
                paramMonthFrom = "05";
                break;
            case Calendar.JUNE:
                monthResultFrom = "JUN";
                paramMonthFrom = "06";
                break;
            case Calendar.JULY:
                monthResultFrom = "JUL";
                paramMonthFrom = "07";
                break;
            case Calendar.AUGUST:
                monthResultFrom = "AUG";
                paramMonthFrom = "08";
                break;
            case Calendar.SEPTEMBER:
                monthResultFrom = "SEP";
                paramMonthFrom = "09";
                break;
            case Calendar.OCTOBER:
                monthResultFrom = "OCT";
                paramMonthFrom = "10";
                break;
            case Calendar.NOVEMBER:
                monthResultFrom = "NOV";
                paramMonthFrom = "11";
                break;
            case Calendar.DECEMBER:
                monthResultFrom = "DEC";
                paramMonthFrom = "12";
                break;
        }

        String hariFrom = String.valueOf(dayFrom);
        String tahunFrom = String.valueOf(yearFrom);
        String hasilFrom = tahunFrom+paramMonthFrom+hariFrom;
        session.setFormatFrom(hasilFrom);

        tvFrom.setText(dayFrom+" "+monthResultFrom+" "+yearFrom);
        session.setFrom(yearFrom+"-"+paramMonthFrom+"-"+dayFrom);

        //========================================================================================== SET CURRENT UNTIL
        int dayUntil  = cUntil.get(Calendar.DAY_OF_MONTH);
        int monthUntil = cUntil.get(Calendar.MONTH);
        String paramMonthUntil = null ;
        int yearUntil = cUntil.get(Calendar.YEAR);
        String monthResultUntil = null;
        switch (monthUntil) {
            case Calendar.JANUARY:
                monthResultUntil = "JAN";
                paramMonthUntil = "01";
                break;
            case Calendar.FEBRUARY:
                monthResultUntil = "FEB";
                paramMonthUntil = "02";
                break;
            case Calendar.MARCH:
                monthResultUntil = "MAR";
                paramMonthUntil = "03";
                break;
            case Calendar.APRIL:
                monthResultUntil = "APR";
                paramMonthUntil = "04";
                break;
            case Calendar.MAY:
                monthResultUntil = "MAY";
                paramMonthUntil = "05";
                break;
            case Calendar.JUNE:
                monthResultUntil = "JUN";
                paramMonthUntil = "06";
                break;
            case Calendar.JULY:
                monthResultUntil = "JUL";
                paramMonthUntil = "07";
                break;
            case Calendar.AUGUST:
                monthResultUntil = "AUG";
                paramMonthUntil = "08";
                break;
            case Calendar.SEPTEMBER:
                monthResultUntil = "SEP";
                paramMonthUntil = "09";
                break;
            case Calendar.OCTOBER:
                monthResultUntil = "OCT";
                paramMonthUntil = "10";
                break;
            case Calendar.NOVEMBER:
                monthResultUntil = "NOV";
                paramMonthUntil = "11";
                break;
            case Calendar.DECEMBER:
                monthResultUntil = "DEC";
                paramMonthUntil = "12";
                break;
        }

        String hariUntil = String.valueOf(dayUntil);
        String tahunUntil = String.valueOf(yearUntil);
        String hasilUntil = tahunUntil+paramMonthUntil+hariUntil;
        session.setFormatUntil(hasilUntil);
        tvUntil.setText(dayUntil+" "+monthResultUntil+" "+yearUntil);
        session.setUntil(yearUntil+"-"+paramMonthUntil+"-"+dayUntil);

//        spinner option

        final DatePickerDialog.OnDateSetListener dateFrom = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                cFrom.set(Calendar.YEAR, year);
                cFrom.set(Calendar.MONTH, monthOfYear);
                cFrom.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                String monthNumberFrom = null;
                int dayFrom = cFrom.get(Calendar.DAY_OF_MONTH);
                int montFrom = cFrom.get(Calendar.MONTH);
                int yearFrom = cFrom.get(Calendar.YEAR);
                String monthNameFrom = null;
                switch (montFrom) {
                    case Calendar.JANUARY:
                        monthNameFrom = "JAN";
                        monthNumberFrom = "01";
                        break;
                    case Calendar.FEBRUARY:
                        monthNameFrom = "FEB";
                        monthNumberFrom = "02";
                        break;
                    case Calendar.MARCH:
                        monthNameFrom = "MAR";
                        monthNumberFrom = "03";
                        break;
                    case Calendar.APRIL:
                        monthNameFrom = "APR";
                        monthNumberFrom = "04";
                        break;
                    case Calendar.MAY:
                        monthNameFrom = "MAY";
                        monthNumberFrom = "05";
                        break;
                    case Calendar.JUNE:
                        monthNameFrom = "JUN";
                        monthNumberFrom = "06";
                        break;
                    case Calendar.JULY:
                        monthNameFrom = "JUL";
                        monthNumberFrom = "07";
                        break;
                    case Calendar.AUGUST:
                        monthNameFrom = "AUG";
                        monthNumberFrom = "08";
                        break;
                    case Calendar.SEPTEMBER:
                        monthNameFrom = "SEP";
                        monthNumberFrom = "09";
                        break;
                    case Calendar.OCTOBER:
                        monthNameFrom = "OCT";
                        monthNumberFrom = "10";
                        break;
                    case Calendar.NOVEMBER:
                        monthNameFrom = "NOV";
                        monthNumberFrom = "11";
                        break;
                    case Calendar.DECEMBER:
                        monthNameFrom = "DEC";
                        monthNumberFrom = "12";
                        break;
                }
                String hariFrom = String.valueOf(dayFrom);
                String tahunFrom = String.valueOf(yearFrom);
                String hasilFrom = tahunFrom+monthNumberFrom+hariFrom;
//                Toast.makeText(ReportActivity.this, "hasil from : "+hasilFrom, Toast.LENGTH_SHORT).show();
                if (Integer.parseInt(hasilFrom) >= Integer.parseInt(session.getFormatUntil())) {
                    updateLabelFromDate(dayFrom, monthNumberFrom, yearFrom, monthNameFrom, hasilFrom, "0");
                } else {
                    updateLabelFromDate(dayFrom, monthNumberFrom, yearFrom, monthNameFrom, hasilFrom, "1");
                }
            }
        };

        tvFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(ReportActivity.this, dateFrom, cFrom
                        .get(Calendar.YEAR), cFrom.get(Calendar.MONTH),
                        cFrom.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


        final DatePickerDialog.OnDateSetListener dateUntil = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                cUntil.set(Calendar.YEAR, year);
                cUntil.set(Calendar.MONTH, monthOfYear);
                cUntil.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                String paramMonthUntil = null;
                int dayUntil = cUntil.get(Calendar.DAY_OF_MONTH);
                int monthUntil = cUntil.get(Calendar.MONTH);
                int yearUntil = cUntil.get(Calendar.YEAR);
                String monthResultUntil = null;
                switch (monthUntil) {
                    case Calendar.JANUARY:
                        monthResultUntil = "JAN";
                        paramMonthUntil = "01";
                        break;
                    case Calendar.FEBRUARY:
                        monthResultUntil = "FEB";
                        paramMonthUntil = "02";
                        break;
                    case Calendar.MARCH:
                        monthResultUntil = "MAR";
                        paramMonthUntil = "03";
                        break;
                    case Calendar.APRIL:
                        monthResultUntil = "APR";
                        paramMonthUntil = "04";
                        break;
                    case Calendar.MAY:
                        monthResultUntil = "MAY";
                        paramMonthUntil = "05";
                        break;
                    case Calendar.JUNE:
                        monthResultUntil = "JUN";
                        paramMonthUntil = "06";
                        break;
                    case Calendar.JULY:
                        monthResultUntil = "JUL";
                        paramMonthUntil = "07";
                        break;
                    case Calendar.AUGUST:
                        monthResultUntil = "AUG";
                        paramMonthUntil = "08";
                        break;
                    case Calendar.SEPTEMBER:
                        monthResultUntil = "SEP";
                        paramMonthUntil = "09";
                        break;
                    case Calendar.OCTOBER:
                        monthResultUntil = "OCT";
                        paramMonthUntil = "10";
                        break;
                    case Calendar.NOVEMBER:
                        monthResultUntil = "NOV";
                        paramMonthUntil = "11";
                        break;
                    case Calendar.DECEMBER:
                        monthResultUntil = "DEC";
                        paramMonthUntil = "12";
                        break;
                }

                String hariUntil = String.valueOf(dayUntil);
                String tahunUntil = String.valueOf(yearUntil);
                String hasilUntil = tahunUntil+paramMonthUntil+hariUntil;
//                Toast.makeText(ReportActivity.this, "hasil from : "+hasilUntil, Toast.LENGTH_SHORT).show();
                if (Integer.parseInt(hasilUntil) <= Integer.parseInt(session.getFormatFrom())) {
                    updateLabelUntilDate(dayUntil, paramMonthUntil, yearUntil, monthResultUntil, hasilUntil, "0");
                } else {
                    updateLabelUntilDate(dayUntil, paramMonthUntil, yearUntil, monthResultUntil, hasilUntil, "1");
                }

            }
        };

        tvUntil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(ReportActivity.this, dateUntil, cUntil
                        .get(Calendar.YEAR), cUntil.get(Calendar.MONTH),
                        cUntil.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


        mSpinner = (Spinner) findViewById(R.id.spinner);
        spinnerTitles = new String[]{"All","To Do", "In Progress", "Pending", "Done", "Cancel"};
        typeId = new String[]{"05","00", "01", "02", "03", "04"};
        spinnerImages = new int[]{
                R.drawable.report_todo,
                R.drawable.report_todo
                , R.drawable.report_progress
                , R.drawable.report_pending
                , R.drawable.report_done
                , R.drawable.report_cancel};

        CustomAdapter mCustomAdapter = new CustomAdapter(ReportActivity.this, spinnerTitles, spinnerImages, typeId);
        mSpinner.setAdapter(mCustomAdapter);
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (isUserInteracting) {
                    session.setType(typeId[i]);
                    if (session.getType().equals("05")) {
                        getAllReportActivity(session.getType(), session.getFrom(), session.getUntil());
                    } else {
                        getReportActivityDateAndType(session.getType(), session.getFrom(), session.getUntil());
                    }
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                getReportActivityDateAndType(session.getType(), session.getFrom(), session.getUntil());
            }
        });

        session.setType("05");
        if (session.getType().equals("05")) {
            getAllReportActivity(session.getType(), session.getFrom(), session.getUntil());
        } else {
            getReportActivityDateAndType(session.getType(), session.getFrom(), session.getUntil());
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void updateLabelFromDate(int dayFrom, String paramMonthFrom, int yearFrom, String monthResultFrom, String hasilFrom, String flag) {
        tvFrom.setText(dayFrom+" "+monthResultFrom+" "+yearFrom);
        session.setFormatFrom(hasilFrom);
        if (flag.equals("0")) {
            popupAlert("Invalid Date Range", "From date must be smaller than until date!");
        } else {
            session.setFrom(yearFrom+"-"+paramMonthFrom+"-"+dayFrom);
            if (session.getType().equals("05")) {
                getAllReportActivity(session.getType(), session.getFrom(), session.getUntil());
            } else {
                getReportActivityDateAndType(session.getType(), session.getFrom(), session.getUntil());
            }
//            Toast.makeText(ReportActivity.this, "Param = type : "+session.getType()+" from : "+session.getFrom()+" until : "+session.getUntil(), Toast.LENGTH_SHORT).show();
        }
    }

    private void updateLabelUntilDate(int dayUntil, String paramMonthUntil, int yearUntil, String monthResultUntil, String hasilUntil, String flag) {
        tvUntil.setText(dayUntil+" "+monthResultUntil+" "+yearUntil);
        session.setFormatUntil(hasilUntil);
        if (flag.equals("0")) {
            popupAlert("Invalid Date Range", "Until date must greater than from date!");
        } else {
            session.setUntil(yearUntil+"-"+paramMonthUntil+"-"+dayUntil);
            if (session.getType().equals("05")) {
                getAllReportActivity(session.getType(), session.getFrom(), session.getUntil());
            } else {
                getReportActivityDateAndType(session.getType(), session.getFrom(), session.getUntil());
            }

//            Toast.makeText(ReportActivity.this, "Param = type : "+session.getType()+" from : "+session.getFrom()+" until : "+session.getUntil(), Toast.LENGTH_SHORT).show();
        }
    }

    private void popupAlert(String title, String desc) {
        final Dialog dialog = new Dialog(ReportActivity.this);
        dialog.setContentView(R.layout.popup_alert);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        dialog.getWindow().setAttributes(lp);
        dialog.setTitle("Input Code Here");
        Button btnYes =(Button) dialog.findViewById(R.id.btnYes);
        TextView tvTitle =(TextView) dialog.findViewById(R.id.tvTitle);
        TextView tvDesc =(TextView) dialog.findViewById(R.id.tvDate);
        tvTitle.setText(title);
        tvDesc.setText(desc);
        dialog.show();
        dialog.setCancelable(false);
        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    private void getAllReportActivity(String type, String from, String until){
        progressDialogHelper.showProgressDialog(ReportActivity.this, "Getting data...");
        AndroidNetworking.get(session.getServerURL()+"users/activity/from/"+from+"/until/"+until+"/buscd/"+session.getUserBusinessCode())
//        AndroidNetworking.get(session.getServerURL()+"users/activity/from/"+from+"/until/"+until)
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
                        System.out.println(response+"kjwrhbk4jr");
                        try {
                            if(response.getInt("status")==200){
//                                session.setToken(response.getString("token"));
                                listModel = new ArrayList<ReportActivityModel>();
                                JSONArray jsonArray = response.getJSONArray("data");
                                if (jsonArray.length()==0){
                                    tvNull.setVisibility(View.VISIBLE);
                                    listReport.setVisibility(View.GONE);
                                }else {
                                    tvNull.setVisibility(View.GONE);
                                    listReport.setVisibility(View.VISIBLE);
                                    String jumlah = String.valueOf(jsonArray.length());
                                    session.setCountType(jumlah);
                                    for (int a = 0; a < jsonArray.length(); a++) {
                                        JSONObject object = jsonArray.getJSONObject(a);
                                        String begin_date = object.getString("begin_date");
                                        String end_date = object.getString("end_date");
                                        String business_code = object.getString("business_code");
                                        String personal_number = object.getString("personal_number");
                                        String activity_title = object.getString("activity_title");
                                        String activity_start = object.getString("activity_start");
                                        String activity_finish = object.getString("activity_finish");
                                        String change_date = object.getString("change_date");
                                        String change_user = object.getString("change_user");
                                        String activity_id = object.getString("activity_id");
                                        System.out.println("activity id getall(): " + activity_id );
                                        String activity_type = object.getString("activity_type");
                                        String approval_status = object.getString("approval_status");
                                        System.out.println(begin_date+"dl2j3nr3");
                                        model = new ReportActivityModel(begin_date, end_date, business_code, personal_number, activity_title, activity_start, activity_finish, change_date, change_user, activity_id, activity_type, approval_status);
                                        listModel.add(model);
                                    }
                                    adapter = new ActivityReportAdapter(ReportActivity.this, listModel);
                                    listReport.setAdapter(adapter);
                                    listReport.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                            String personal_number_aktivitas = listModel.get(position).getPersonal_number();
                                            String activity_id = listModel.get(position).getActivity_id();
                                            String activity_type = listModel.get(position).getActivity_type();
                                            String activity_title = listModel.get(position).getActivity_title();
                                            String activity_start = listModel.get(position).getActivity_start();
                                            String activity_finish = listModel.get(position).getActivity_finish();
                                            String approval_status = listModel.get(position).getApproval_status();
                                            popupMenuActivity(session.getUserFullName(), personal_number_aktivitas, activity_id, activity_type, activity_title, activity_start, activity_finish, approval_status);
                                        }
                                    });
                                }
                                progressDialogHelper.dismissProgressDialog(ReportActivity.this);
                            }else{
//                                popUpLogin();
                                progressDialogHelper.dismissProgressDialog(ReportActivity.this);
                            }
                        }catch (Exception e){
                            progressDialogHelper.dismissProgressDialog(ReportActivity.this);
                            System.out.println(e);
                        }

                    }
                    @Override
                    public void onError(ANError error) {
                        progressDialogHelper.dismissProgressDialog(ReportActivity.this);
                        System.out.println(error);
                    }
                });
    }

    private void getReportActivityDateAndType(String type, String from, String until){
        progressDialogHelper.showProgressDialog(ReportActivity.this, "Getting data...");
        AndroidNetworking.get(session.getServerURL()+"users/activity/type/"+type+"/from/"+from+"/until/"+until+"/buscd/"+session.getUserBusinessCode())
//        AndroidNetworking.get(session.getServerURL()+"users/activity/from/"+from+"/until/"+until)
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
                        System.out.println(response+"kjwrhbk4jr");
                        try {
                            if(response.getInt("status")==200){
//                                session.setToken(response.getString("token"));
                                listModel = new ArrayList<ReportActivityModel>();
                                JSONArray jsonArray = response.getJSONArray("data");
                                if (jsonArray.length()==0){
                                    tvNull.setVisibility(View.VISIBLE);
                                    listReport.setVisibility(View.GONE);
                                }else {
                                    tvNull.setVisibility(View.GONE);
                                    listReport.setVisibility(View.VISIBLE);
                                    String jumlah = String.valueOf(jsonArray.length());
                                    session.setCountType(jumlah);
                                    for (int a = 0; a < jsonArray.length(); a++) {
                                        JSONObject object = jsonArray.getJSONObject(a);
                                        String begin_date = object.getString("begin_date");
                                        String end_date = object.getString("end_date");
                                        String business_code = object.getString("business_code");
                                        String personal_number = object.getString("personal_number");
                                        String activity_title = object.getString("activity_title");
                                        String activity_start = object.getString("activity_start");
                                        String activity_finish = object.getString("activity_finish");
                                        String change_date = object.getString("change_date");
                                        String change_user = object.getString("change_user");
                                        int activity_id = object.getInt("activity_id");
                                        System.out.println("test activity_id: " + activity_id);
                                        String activity_type = object.getString("activity_type");
                                        String approval_status = object.getString("approval_status");
                                        System.out.println(begin_date+"dl2j3nr3");
                                        model = new ReportActivityModel(begin_date, end_date, business_code, personal_number, activity_title, activity_start, activity_finish, change_date, change_user, String.valueOf(activity_id), activity_type, approval_status);
                                        listModel.add(model);
                                    }
                                    adapter = new ActivityReportAdapter(ReportActivity.this, listModel);
                                    listReport.setAdapter(adapter);
//                                    listReport.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {
//                                        @Override
//                                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                                            String personal_number_aktivitas = listModel.get(position).getPersonal_number();
//                                            String activity_id = listModel.get(position).getActivity_id();
//                                            String activity_type = listModel.get(position).getActivity_type();
//                                            String activity_title = listModel.get(position).getActivity_title();
//                                            String activity_start = listModel.get(position).getActivity_start();
//                                            String activity_finish = listModel.get(position).getActivity_finish();
//                                            String approval_status = listModel.get(position).getApproval_status();
//                                            popupMenuActivity(session.getUserFullName(), personal_number_aktivitas, activity_id, activity_type, activity_title, activity_start, activity_finish, approval_status);
//                                        }
//                                    });
                                }
                                progressDialogHelper.dismissProgressDialog(ReportActivity.this);
                            }else{
//                                popUpLogin();
                                progressDialogHelper.dismissProgressDialog(ReportActivity.this);
                            }
                        }catch (Exception e){
                            progressDialogHelper.dismissProgressDialog(ReportActivity.this);
                            System.out.println(e);
                        }

                    }
                    @Override
                    public void onError(ANError error) {
                        progressDialogHelper.dismissProgressDialog(ReportActivity.this);
                        System.out.println(error);
                    }
                });
    }

    private void popupMenuActivity(final String full_name, final String personal_number_aktivitas, final String activity_id, final String activity_type, final String activity_title, final String activity_start, final String activity_finish, final String approval_status) {
        final Dialog dialog = new Dialog(ReportActivity.this);
        dialog.setContentView(R.layout.layout_activity);
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
        tvTitle.setText("'"+activity_title+"'");
        dialog.show();
        dialog.setCancelable(true);
        btnChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ReportActivity.this, CommentActivity.class);
                i.putExtra("full_name", full_name);
                i.putExtra("personal_number", personal_number_aktivitas);
                i.putExtra("activity_id", activity_id);
                i.putExtra("activity_type", activity_type);
                i.putExtra("activity_title", activity_title);
                i.putExtra("activity_start", activity_start);
                i.putExtra("activity_finish", activity_finish);
                i.putExtra("approval_status", approval_status);
                startActivity(i);
                dialog.dismiss();
            }
        });
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ReportActivity.this);
                builder.setTitle("Confirm");
                builder.setMessage("Are you sure to delete this activity ?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing but close the dialog
                        submitDelete(activity_id);
                        Toast.makeText(ReportActivity.this, "Delete success", Toast.LENGTH_SHORT).show();
//                                                                        submitTodayActivity(activity_id, activity_type, activity_title, activity_start, activity_finish);
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
    }

    private void submitDelete(String id_get) {
        System.out.println("API Delete report activity: " + session.getServerURL()+"users/deleteActivity/actid/"+id_get+"/buscd/"+session.getUserBusinessCode());
        AndroidNetworking.post(session.getServerURL()+"users/deleteActivity/actid/"+id_get+"/buscd/"+session.getUserBusinessCode())
                .addHeaders("Accept","application/json")
                .addHeaders("Content-Type","application/json")
                .addHeaders("Authorization",session.getToken())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // do anything with response
                        System.out.println("response delete report activity: "+response);
                        try {
                            if(response.getInt("status")==200){
                                Toast.makeText(ReportActivity.this, "Success delete activity", Toast.LENGTH_SHORT).show();
                                adapter.notifyDataSetChanged();
                                getAllReportActivity(session.getType(), session.getFrom(), session.getUntil());
                            } else {
                                Toast.makeText(ReportActivity.this, "error", Toast.LENGTH_SHORT).show();
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

    private void popUpLogin() {
        final Dialog dialog = new Dialog(ReportActivity.this);
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
                Intent i = new Intent(ReportActivity.this, LoginActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
            }
        });
    }

    @Override
    public void onUserInteraction() {
        super.onUserInteraction();
        isUserInteracting = true;
    }


    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        getAllReportActivity(session.getType(), session.getFrom(), session.getUntil());
    }
}
