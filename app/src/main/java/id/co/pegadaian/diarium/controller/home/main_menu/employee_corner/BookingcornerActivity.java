package id.co.pegadaian.diarium.controller.home.main_menu.employee_corner;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
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
import id.co.pegadaian.diarium.adapter.DetailCornerAdapter;
import id.co.pegadaian.diarium.model.DetailCornerModel;
import id.co.pegadaian.diarium.model.HeaderBuildingModel;
import id.co.pegadaian.diarium.model.HeaderLocationModel;
import id.co.pegadaian.diarium.util.UserSessionManager;
import id.co.pegadaian.diarium.util.element.ProgressDialogHelper;
import in.galaxyofandroid.spinerdialog.SpinnerDialog;

public class BookingcornerActivity extends AppCompatActivity {

    private ProgressDialogHelper progressDialogHelper;
    UserSessionManager session;
    private List<DetailCornerModel> listModel;
    private DetailCornerModel model;
    private DetailCornerAdapter adapter;
    TextView tvNull;
    ListView listActivity;
    Spinner spinnerBuilding, spinnerLocation;
    Calendar cFrom = Calendar.getInstance();
    ArrayList<String> opsi= new ArrayList<>();
    ArrayList<String> opsi2= new ArrayList<>();
    SpinnerDialog spinnerDialogsBuilding,spinnerDialogsFloor;
    TextView tvDate;
    Button btnSearch;
    String hasilFrom, type_booking, booking_code;
//    LinearLayout lvBuilding, lvFloor;
    Typeface font,fontbold;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookingcorner);
        session = new UserSessionManager(this);
        progressDialogHelper = new ProgressDialogHelper();
        Intent intent = getIntent();
        String theme_id = intent.getStringExtra("theme_id");
        String theme_name = intent.getStringExtra("theme_name");
        booking_code = intent.getStringExtra("booking_code");
        type_booking = intent.getStringExtra("type");
        font = Typeface.createFromAsset(getApplication().getAssets(),"fonts/Nexa Light.otf");
        fontbold = Typeface.createFromAsset(getApplication().getAssets(),"fonts/Nexa Bold.otf");
        tvNull = (TextView) findViewById(R.id.tvNull);
        tvDate = (TextView) findViewById(R.id.tvDate);
        spinnerBuilding = (Spinner) findViewById(R.id.spinnerBuilding);
        spinnerLocation = (Spinner) findViewById(R.id.spinnerLocation);
        TextView judul= (TextView) findViewById(R.id.booking_name);
        btnSearch = (Button) findViewById(R.id.btn_search);
        listActivity = findViewById(R.id.list_booking);
        judul.setTypeface(fontbold);
        judul.setText("Booking "+theme_name);
        btnSearch.setTypeface(fontbold);

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
                hasilFrom = tahunFrom+"-"+monthNumberFrom+"-"+hariFrom;
//                Toast.makeText(ReportActivity.this, "hasil from : "+hasilFrom, Toast.LENGTH_SHORT).show();
                updateLabelFromDate(dayFrom, monthNumberFrom, yearFrom, monthNameFrom, hasilFrom);
            }
        };

        tvDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(BookingcornerActivity.this, dateFrom, cFrom
                        .get(Calendar.YEAR), cFrom.get(Calendar.MONTH),
                        cFrom.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        spinnerDialogsBuilding = new SpinnerDialog(BookingcornerActivity.this,opsi,"Choose Place");
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(BookingcornerActivity.this, "Tanggal : "+hasilFrom+" - "+session.getEmpId(), Toast.LENGTH_SHORT).show();
                if (session.getEmpId().equals("")) {
                    Toast.makeText(BookingcornerActivity.this, "Please choose building and location first !", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(hasilFrom)){
                    Toast.makeText(BookingcornerActivity.this, "Please choose date first !", Toast.LENGTH_SHORT).show();
                } else {
                    getDetailCorner(hasilFrom, session.getEmpId());

                }
            }
        });

        getBuilding();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void getDetailCorner(final String date, String ecrid){
        progressDialogHelper.showProgressDialog(BookingcornerActivity.this, "Getting data...");
        AndroidNetworking.get(session.getServerURL()+"users/showdetailbydate/date/"+date+"/ecrid/"+ecrid+"/buscd/"+session.getUserBusinessCode())
//        AndroidNetworking.get(session.getServerURL()+"users/showalldetail?")
                .addHeaders("Accept","application/json")
                .addHeaders("Content-Type","application/json")
                .addHeaders("Authorization",session.getToken())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // do anything with response
                        System.out.println(response+"RESPONDETAILCORNER"+date);
                        try {
                            if(response.getInt("status")==200){
                                listModel = new ArrayList<DetailCornerModel>();
                                JSONArray jsonArray = response.getJSONArray("data");
                                System.out.println("kj3nj"+jsonArray.length());
                                if (jsonArray.length()==0) {
                                    listActivity.setVisibility(View.GONE);
                                    tvNull.setVisibility(View.VISIBLE);
                                    progressDialogHelper.dismissProgressDialog(BookingcornerActivity.this);

                                } else {
                                    listActivity.setVisibility(View.VISIBLE);
                                    tvNull.setVisibility(View.GONE);
                                    for (int a = 0; a < jsonArray.length(); a++) {
                                        JSONObject object = jsonArray.getJSONObject(a);
                                        String begin_date = object.getString("begin_date");
                                        String end_date = object.getString("end_date");
                                        String business_code = object.getString("business_code");
                                        String empcorner_id = object.getString("empcorner_id");
                                        String batch_name = object.getString("batch_name");
                                        String kuota = object.getString("kuota");
                                        String start_batch = object.getString("start_batch");
                                        String end_batch = object.getString("end_batch");
                                        String change_date = object.getString("change_date");
                                        String change_user = object.getString("change_user");
                                        String batch_id = object.getString("batch_id");
                                        String empcorner_date = object.getString("empcorner_date");
                                        String kuota_available = object.getString("kuota_available");
                                        model = new DetailCornerModel(begin_date, end_date, business_code, empcorner_id, batch_name, kuota, start_batch, end_batch, change_date, change_user, batch_id, empcorner_date, kuota_available);
                                        listModel.add(model);
                                    }
                                    adapter = new DetailCornerAdapter(BookingcornerActivity.this, listModel, type_booking, booking_code);
                                    listActivity.setAdapter(adapter);
                                    progressDialogHelper.dismissProgressDialog(BookingcornerActivity.this);
                                }
                            }else{
//                                popUpLogin();
                                progressDialogHelper.dismissProgressDialog(BookingcornerActivity.this);
                            }
                        }catch (Exception e){
                            progressDialogHelper.dismissProgressDialog(BookingcornerActivity.this);
                            System.out.println(e);
                        }

                    }
                    @Override
                    public void onError(ANError error) {
                        progressDialogHelper.dismissProgressDialog(BookingcornerActivity.this);
                        System.out.println(error);
                    }
                });
    }

    private void updateLabelFromDate(int dayFrom, String paramMonthFrom, int yearFrom, String monthResultFrom, String hasilFrom) {
        tvDate.setText(dayFrom+" "+monthResultFrom+" "+yearFrom);
    }

//    private void getEcTheme(){
//        ArrayList<String> list;
//        String result = "";
//        SimpleDateFormat tgl = new SimpleDateFormat("yyyy-MM-dd");
//        String tRes = tgl.format(new Date());
//        AndroidNetworking.get(session.getServerURL()+"users/showalltheme?")
//                .addHeaders("Accept","application/json")
//                .addHeaders("Content-Type","application/json")
//                .addHeaders("Authorization",session.getToken())
//                .setPriority(Priority.MEDIUM)
//                .build()
//                .getAsJSONObject(new JSONObjectRequestListener() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        ArrayList<EmployeeCornerThemeModel> contacts = new ArrayList<>();
//                        System.out.println(response+"hiewuhruTheme");
//                        try {
//                            if(response.getInt("status")==200){
//                                JSONArray jsonArray = response.getJSONArray("data");
//                                if (jsonArray.length()==0) {
//                                } else {
//                                    for (int a = 0; a < jsonArray.length(); a++) {
//                                        JSONObject object = jsonArray.getJSONObject(a);
//                                        String begin_date = object.getString("begin_date");
//                                        String end_date = object.getString("end_date");
//                                        String business_code = object.getString("business_code");
//                                        String theme_id = object.getString("theme_id");
//                                        String theme_name = object.getString("theme_name");
//                                        String image = object.getString("image");
//                                        String change_date = object.getString("change_date");
//                                        String change_user = object.getString("change_user");
//                                        contacts.add(new EmployeeCornerThemeModel(begin_date, end_date, business_code, theme_id, theme_name, image, change_date, change_user));
//
//                                    }
//                                    ArrayAdapter<EmployeeCornerThemeModel> adapter =
//                                            new ArrayAdapter<EmployeeCornerThemeModel>(getApplicationContext(),  android.R.layout.simple_spinner_dropdown_item, contacts);
//                                    adapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item);
//                                    spinnerBuilding.setAdapter(adapter);
//                                    spinnerBuilding.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//                                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                                            String name = contacts.get(position).getTheme_name();
//                                            String id_contact = contacts.get(position).getTheme_id();
//                                            Toast.makeText(getApplicationContext(), "Isi "+name+" - "+id_contact, Toast.LENGTH_SHORT).show();
//                                        }
//                                        public void onNothingSelected(AdapterView<?> parent) {
//                                        }
//                                    });
//                                }
//                            }else{
//                            }
//                        }catch (Exception e){
//                            System.out.println(e);
//                        }
//                    }
//                    @Override
//                    public void onError(ANError error) {
//                        System.out.println(error);
//                    }
//                });
//    }

    private void getBuilding(){
//        progressDialogHelper.showProgressDialog(getActivity(), "Getting data...");
        AndroidNetworking.get(session.getServerURL()+"users/showallheader/buscd/"+session.getUserBusinessCode())
                .addHeaders("Accept","application/json")
                .addHeaders("Content-Type","application/json")
                .addHeaders("Authorization",session.getToken())
                //.addJSONObjectBody(body)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        final ArrayList<HeaderBuildingModel> building = new ArrayList<>();
                        System.out.println(response+"RESPONBUILDING");
                        try {
                            if(response.getInt("status")==200){
                                JSONArray jsonArray = response.getJSONArray("data");
                                if (jsonArray.length()==0) {
                                } else {
                                    building.add(new HeaderBuildingModel("", "", "", "", "", "", "", "Choose Building", "", "", ""));
                                    for (int a = 0; a < jsonArray.length(); a++) {
                                        JSONObject object = jsonArray.getJSONObject(a);
                                        String build_code = object.getString("build_code");
//                                        String begin_date = object.getString("begin_date");
//                                        String end_date = object.getString("end_date");
//                                        String business_code = object.getString("business_code");
//                                        String empcorner_id = object.getString("empcorner_id");
//                                        String empcorner_name = object.getString("empcorner_name");
//                                        String theme_id = object.getString("theme_id");
//                                        String empcorner_desc = object.getString("empcorner_desc");
//                                        String build_code = object.getString("build_code");
//                                        String change_date = object.getString("change_date");
//                                        String change_user = object.getString("change_user");
//                                        String empcorner_loc = object.getString("empcorner_loc");
                                        building.add(new HeaderBuildingModel("", "", "", "", "", "", "", build_code, "", "", ""));
                                    }

                                    ArrayAdapter<HeaderBuildingModel> adapter =
                                            new ArrayAdapter<HeaderBuildingModel>(getApplicationContext(),  android.R.layout.simple_spinner_dropdown_item, building);
                                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                    spinnerBuilding.setAdapter(adapter);
                                    spinnerBuilding.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                            String name = building.get(position).getBuild_code();
                                            String id_contact = building.get(position).getTheme_id();
                                            getLocation(name);
                                        }
                                        public void onNothingSelected(AdapterView<?> parent) {
                                        }
                                    });
                                }
                            }else{
                            }
                        }catch (Exception e){
                            System.out.println(e);
                        }

                    }
                    @Override
                    public void onError(ANError error) {
//                        progressDialogHelper.dismissProgressDialog(getActivity());

                        System.out.println(error);
                    }
                });
    }

    private void getLocation(final String build_code_get){
//        progressDialogHelper.showProgressDialog(getActivity(), "Getting data...");
        AndroidNetworking.get(session.getServerURL()+"users/showallheader/buscd/"+session.getUserBusinessCode())
                .addHeaders("Accept","application/json")
                .addHeaders("Content-Type","application/json")
                .addHeaders("Authorization",session.getToken())
                //.addJSONObjectBody(body)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        final ArrayList<HeaderLocationModel> location = new ArrayList<>();
                        System.out.println(response+"hiewuhruTheme");
                        try {
                            if(response.getInt("status")==200){
                                JSONArray jsonArray = response.getJSONArray("data");
                                if (jsonArray.length()==0) {
                                } else {
                                    location.add(new HeaderLocationModel("", "", "", "", "", "", "", "", "", "", "Choose Location"));
                                    for (int a = 0; a < jsonArray.length(); a++) {
                                        JSONObject object = jsonArray.getJSONObject(a);
                                        JSONArray arrayHeader = object.getJSONArray("header");
                                        for (int b=0; b<arrayHeader.length(); b++) {
                                            JSONObject objHeader = arrayHeader.getJSONObject(b);
                                            String begin_date = objHeader.getString("begin_date");
                                            String end_date = objHeader.getString("end_date");
                                            String business_code = objHeader.getString("business_code");
                                            String empcorner_id = objHeader.getString("empcorner_id");
                                            String empcorner_name = objHeader.getString("empcorner_name");
                                            String theme_id = objHeader.getString("theme_id");
                                            String empcorner_desc = objHeader.getString("empcorner_desc");
                                            String build_code = objHeader.getString("build_code");
                                            String change_date = objHeader.getString("change_date");
                                            String change_user = objHeader.getString("change_user");
                                            String empcorner_loc = objHeader.getString("empcorner_loc");
                                            if (build_code.equals(build_code_get)) {
                                                location.add(new HeaderLocationModel(begin_date, end_date, business_code, empcorner_id, empcorner_name, theme_id, empcorner_desc, build_code, change_date, change_user, empcorner_loc));
                                            }
                                        }
                                    }

                                    ArrayAdapter<HeaderLocationModel> adapter =
                                            new ArrayAdapter<HeaderLocationModel>(getApplicationContext(),  android.R.layout.simple_spinner_dropdown_item, location);
                                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                    spinnerLocation.setAdapter(adapter);
                                    spinnerLocation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                            String empcorner_id = location.get(position).getEmpcorner_id();
                                            session.setEmpId(empcorner_id);

                                        }
                                        public void onNothingSelected(AdapterView<?> parent) {
                                        }
                                    });
                                }
                            }else{
                            }
                        }catch (Exception e){
                            System.out.println(e);
                        }

                    }
                    @Override
                    public void onError(ANError error) {
//                        progressDialogHelper.dismissProgressDialog(getActivity());

                        System.out.println(error);
                    }
                });
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

}
