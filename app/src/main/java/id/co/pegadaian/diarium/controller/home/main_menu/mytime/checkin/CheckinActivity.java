package id.co.pegadaian.diarium.controller.home.main_menu.mytime.checkin;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Struct;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import eltos.simpledialogfragment.form.Check;
import id.co.pegadaian.diarium.R;
import id.co.pegadaian.diarium.adapter.InboxAdapter;
import id.co.pegadaian.diarium.adapter.OfficeCheckinAdapter;
import id.co.pegadaian.diarium.controller.HomeActivity;
import id.co.pegadaian.diarium.controller.home.HomeFragment;
import id.co.pegadaian.diarium.controller.home.main_menu.presence.PresenceList;
import id.co.pegadaian.diarium.controller.home.main_menu.presence.PresenceListApprove;
import id.co.pegadaian.diarium.controller.home.main_menu.presence.PresenceConfirmation;
import id.co.pegadaian.diarium.controller.login.LoginActivity;
import id.co.pegadaian.diarium.controller.splashscreen.SplashscreenActivity;
import id.co.pegadaian.diarium.model.InboxModel;
import id.co.pegadaian.diarium.model.OfficeCheckinModel;
import id.co.pegadaian.diarium.util.UserSessionManager;
import id.co.pegadaian.diarium.util.element.ProgressDialogHelper;

//import id.co.telkomsigma.Diarium.util.element.FMActivity;

public class
CheckinActivity extends AppCompatActivity implements MenuItem.OnMenuItemClickListener,OnMapReadyCallback {

    SimpleDateFormat tgl = new SimpleDateFormat("dd");
    final String tglResult = tgl.format(new Date());

    SimpleDateFormat tahun = new SimpleDateFormat("yyyy");
    final String tahunResult = tahun.format(new Date());

    String expression, health, workfrom, site, siteIDCheck, siteLocationCheck, transportation;
    int healthID, workfromID, transportationID;
    String healthIDtoString,workfromIDtoString,transportationIDtoString;

    ImageView imgUnderPressure,imgComfort,imgHappy, imgSick, imgNotFit,imgHealthy,imgWfs,imgWfh,
            imgWfo,imgOwnTrans, imgOfficeTrans, imgPublicTrans;
    TextView tvUnderPressure, tvComfort, tvHappy, tvSick, tvNotFIt, tvHealthy, tvWfs, tvWfh, tvWfo;

//    CheckinSpinnerLocationModel officeLocModel;
//    List<CheckinSpinnerLocationModel> listOfficeLoc;

    Typeface font,fontbold;
    Button showMenu, showSpinner;
    private GoogleMap mMap;
    TextView tvTime, tvDay, tvDate, tvFeel, tvFeel2;
    UserSessionManager session;
    LinearLayout rootView;
    public static final int ENROLL_REQ_CODE = 12800;
    public static final int AUTH_REQ_CODE = 12801;
    public static final int AUTH_TRUE = 12802;
    String emot = null;
    Location locationVar;
    String checkinType;
    Spinner spinner, spinnerOfficeCheckin;

    private ArrayList<OfficeCheckinModel> spinnerCheckinList;
    private OfficeCheckinAdapter spinnerCheckinAdapter;
    private OfficeCheckinModel officeModel;
    String selectedSpinner;
    private ArrayList<String> officeCheckin  = new ArrayList<>();
    private ArrayList<String> officeCheckinId  = new ArrayList<>();
    private ArrayList<String> officeCheckinlocation  = new ArrayList<>();


    LinearLayout lnUnderPressure, lnComfort, lnHappy, lnSick, lnNotFit, lnHealthy, lnWfs, lnWfh, lnWfo, lnPublicTrans,
            lnOwnTrans, lnOfficeTrans, lnFirst, lnSecond, lnThird , lnForth ,lnFifth;
    String monthResult = null;
    String getDate, getTime;
    private EditText etDescPost;
    private ProgressDialogHelper progressDialogHelper;
    String StDesc;

    //    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkin);

        session = new UserSessionManager(CheckinActivity.this);
        getListOfFakeLocationApps(this);
        font = Typeface.createFromAsset(getApplication().getAssets(),"fonts/Nexa Light.otf");
        fontbold = Typeface.createFromAsset(getApplication().getAssets(),"fonts/Nexa Bold.otf");
//        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
//        mapFragment.getMapAsync(this);
        progressDialogHelper = new ProgressDialogHelper();
//        spinnerOfficeCheckin = findViewById(R.id.spinner_office_checkin);
        showSpinner = findViewById(R.id.btn_show_spinner);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        tvTime = (TextView) findViewById(R.id.tvTime);
        tvDay = (TextView) findViewById(R.id.tvDay);
        tvDate = (TextView) findViewById(R.id.tvDate);
        tvFeel = (TextView) findViewById(R.id.feel);
        tvFeel2 = (TextView) findViewById(R.id.feel2);
//        lnUnderPressure = findViewById(R.id.linear_underPressure);
//        lnComfort = findViewById(R.id.linear_comfort);
//        lnHappy = findViewById(R.id.linear_happy);
        lnSick = findViewById(R.id.linear_sick);
        lnNotFit = findViewById(R.id.linear_notfit);
        lnHealthy = findViewById(R.id.linear_healthy);
        lnWfs = findViewById(R.id.linear_wfs);
        lnWfh = findViewById(R.id.linear_wfh);
        lnWfo = findViewById(R.id.linear_wfo);
        lnPublicTrans = findViewById(R.id.linear_publictrans);
        lnOwnTrans = findViewById(R.id.linear_owntrans);
        lnOfficeTrans = findViewById(R.id.linear_officetrans);
//        lnFirst = findViewById(R.id.linear_first);
        lnSecond = findViewById(R.id.linear_second);
        lnThird = findViewById(R.id.linear_third);
        lnForth = findViewById(R.id.linear_forth);
        lnFifth = findViewById(R.id.linear_fifth);
        etDescPost = findViewById(R.id.desc_post);

//      Pic & Text

//        imgUnderPressure = findViewById(R.id.iv_under_pressure);
//        imgComfort = findViewById(R.id.iv_comfort);
//        imgHappy = findViewById(R.id.iv_happy);
        imgSick = findViewById(R.id.iv_sick);
        imgNotFit = findViewById(R.id.iv_not_fit);
        imgHealthy = findViewById(R.id.iv_healthy);
        imgWfs = findViewById(R.id.iv_wfs);
        imgWfh = findViewById(R.id.iv_wfh);
        imgWfo = findViewById(R.id.iv_wfo);
        imgPublicTrans = findViewById(R.id.iv_publictrans);
        imgOwnTrans = findViewById(R.id.iv_owntrans);
        imgOfficeTrans = findViewById(R.id.iv_officetrans);
//
//        tvUnderPressure = findViewById(R.id.stress);
//        tvComfort = findViewById(R.id.nyaman);
//        tvHappy = findViewById(R.id.nyaman2);
//        tvSick = findViewById(R.id.sick);
//        tvNotFIt = findViewById(R.id.notfit);
//        tvHealthy = findViewById(R.id.healthy);
//        tvWfs = findViewById(R.id.wfs);
//        tvWfh = findViewById(R.id.wfh);
//        tvWfo = findViewById(R.id.wfo);
//      Pic & Text

        tvTime.setTypeface(font);
        tvDay.setTypeface(font);
        tvDate.setTypeface(font);
        tvFeel.setTypeface(font);
        etDescPost.setTypeface(font);

//        Toast.makeText(this, "Status :"+session.getStat(), Toast.LENGTH_SHORT).show();

        //========================================================================================== TIME   // ambil time dipindah ke method onmapready
//        SimpleDateFormat time = new SimpleDateFormat("HH:mm");
//        getTime = time.format(new Date());
//        tvTime.setText(getTime);
        //========================================================================================== END TIME    // ambil time dipindah ke method onmapready

        //========================================================================================== DAY
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        String dayResult = null;
        switch (day) {
            case Calendar.SUNDAY:
                dayResult = "Sunday";
                break;
            case Calendar.MONDAY:
                dayResult = "Monday";
                break;
            case Calendar.TUESDAY:
                dayResult = "Tuesday";
                break;
            case Calendar.WEDNESDAY:
                dayResult = "Wednesday";
                break;
            case Calendar.THURSDAY:
                dayResult = "Thursday";
                break;
            case Calendar.FRIDAY:
                dayResult = "Friday";
                break;
            case Calendar.SATURDAY:
                dayResult = "Saturday";
                break;
        }
        tvDay.setText(dayResult);
        //========================================================================================== END DAY

        //========================================================================================== DATE
        SimpleDateFormat tgl = new SimpleDateFormat("dd");
        final String tglResult = tgl.format(new Date());

        SimpleDateFormat tahun = new SimpleDateFormat("yyyy");
        final String tahunResult = tahun.format(new Date());

        int month = calendar.get(Calendar.MONTH);
        String bulan = null;
        switch (month) {
            case Calendar.JANUARY:
                monthResult = "JANUARY";
                bulan = "January";
                break;
            case Calendar.FEBRUARY:
                monthResult = "FEBRUARY";
                bulan = "February";
                break;
            case Calendar.MARCH:
                monthResult = "MARCH";
                bulan = "March";

                break;
            case Calendar.APRIL:
                monthResult = "APRIL";
                bulan = "April";

                break;
            case Calendar.MAY:
                monthResult = "MAY";
                bulan = "May";

                break;
            case Calendar.JUNE:
                monthResult = "JUNE";
                bulan = "June";

                break;
            case Calendar.JULY:
                monthResult = "JULY";
                bulan = "July";

                break;
            case Calendar.AUGUST:
                monthResult = "AUGUST";
                bulan = "August";

                break;
            case Calendar.SEPTEMBER:
                monthResult = "SEPTEMBER";
                bulan = "September";

                break;
            case Calendar.OCTOBER:
                monthResult = "OCTOBER";
                bulan = "October";

                break;
            case Calendar.NOVEMBER:
                monthResult = "NOVEMBER";
                bulan = "November";

                break;
            case Calendar.DECEMBER:
                monthResult = "DECEMBER";
                bulan = "December";

                break;
        }
        tvDate.setText(tglResult+" "+bulan+" "+tahunResult);

//        ========================================================================================== END DATE
//         Get reference of widgets from XML layout
        spinner = (Spinner) findViewById(R.id.spinner);

//         Initializing a String Array
        String[] plants = new String[]{
                "REGULER",
                "LEMBUR",
        };

//        getOffice();

//         Initializing an ArrayAdapter
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                this, R.layout.spinner_item,plants
        );
        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
        spinner.setAdapter(spinnerArrayAdapter);
        spinner.getSolidColor();
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent,
                                       View view, int pos, long id) {
                checkinType = parent.getItemAtPosition(pos).toString();
                System.out.println("TESCHECKINTYPE" + checkinType);
//                Toast.makeText(CheckinActivity.this, "TIPE : "+checkinType, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });
        if (session.getStat().equals("CO")||session.getStat().equals("OO")) {
            spinner.setEnabled(true);
        } else if (session.getStat().equals("CI")){
            spinner.setSelection(0);
            spinner.setEnabled(false);
        } else if (session.getStat().equals("OI")) {
            spinner.setSelection(1);
            spinner.setEnabled(false);
        }

        LnSick();
        LnNotFit();
        LnHealthy();


        // check automatic time on / off
        try {
            if (Settings.Global.getInt(getContentResolver(), Settings.Global.AUTO_TIME)==0){
                System.out.println("masuk ke ga otomatis");

                AlertDialog alertDialog = new AlertDialog.Builder(CheckinActivity.this).create();
                alertDialog.setTitle("Alert");
                alertDialog.setMessage("Please turn on automatic time zone on your device!");
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
//                                dialog.dismiss();
                                finish();
                            }
                        });
                alertDialog.show();

            } else {
                System.out.println("masukl ke otomatis");
            }
        } catch (Exception e){
            System.out.println("masuk ke catchnya checkin time zone");
        }


        //========================================================================================== CHECK GPS IS ENABLE
        if (session.getStat().equals("CO")||session.getStat().equals("OO")) {
            tvFeel.setText("How do you feel today ?");
            getSupportActionBar().setTitle("Check In");
        } else {
            tvFeel.setText("How do you feel today ?");
            getSupportActionBar().setTitle("Check Out");
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


    }

////  baru
//    private void spinnerListItemSelected(){
//        spinnerCheckinList = new ArrayList<>();
//        spinnerCheckinList.add(new OfficeCheckinModel("Item"));
//        spinnerCheckinList.add(new OfficeCheckinModel("Bahan Baku"));
//
//        spinnerCheckinAdapter = new OfficeCheckinAdapter(CheckinActivity.this,spinnerCheckinList);
//        spinnerOfficeCheckin.setAdapter(spinnerCheckinAdapter);
//    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        if (session.getUserNIK().equals(session.getTempPers())) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_at_checkin, menu);
//        }
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.make_confirmation:
                Intent intent = new Intent(CheckinActivity.this, PresenceList.class);
//                    intent.putExtra("name",session.getUserFullName());
//                    intent.putExtra("email","diarium@telkom.co.id");
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }




    public void LnSick (){
        lnSick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgSick.setImageResource(R.drawable.sick);
                new AlertDialog.Builder(CheckinActivity.this)
                        .setMessage("Sick ?")
                        .setCancelable(false)
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                health = "Sick";
                                healthID = 1;
                                healthIDtoString = String.valueOf(healthID);
                                tvFeel.setText("Please choose your work location");
                                lnSecond.setVisibility(View.GONE);
                                lnThird.setVisibility(View.VISIBLE);

                                LnWfh();
                                LnWfo();
                                LnWfs();
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                imgSick.setImageResource(R.drawable.sick_before);
                            }
                        })
                        .show();
            }
        });
    }

    public void LnNotFit (){
        lnNotFit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgNotFit.setImageResource(R.drawable.notfit);
                new AlertDialog.Builder(CheckinActivity.this)
                        .setMessage("Not Fit ?")
                        .setCancelable(false)
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                health = "Not Fit";
                                healthID = 2;
                                healthIDtoString = String.valueOf(healthID);
                                tvFeel.setText("Please choose your work location");

                                lnSecond.setVisibility(View.GONE);
                                lnThird.setVisibility(View.VISIBLE);

                                LnWfh();
                                LnWfo();
                                LnWfs();
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                imgNotFit.setImageResource(R.drawable.notfit_before);
                            }
                        })
                        .show();
            }
        });
    }

    public void LnHealthy (){
        lnHealthy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgHealthy.setImageResource(R.drawable.healthy);
                new AlertDialog.Builder(CheckinActivity.this)
                        .setMessage("Healthy ?")
                        .setCancelable(false)
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                health = "Healthy";
                                healthID = 3;
                                healthIDtoString = String.valueOf(healthID);
                                tvFeel.setText("Please choose your work location");

                                lnSecond.setVisibility(View.GONE);
                                lnThird.setVisibility(View.VISIBLE);

                                LnWfh();
                                LnWfo();
                                LnWfs();
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                imgHealthy.setImageResource(R.drawable.healthy_before);
                            }
                        })
                        .show();
            }
        });
    }


    public void LnWfs (){

        lnWfs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgWfs.setImageResource(R.drawable.workfromsatelite);
                String Desc = etDescPost.getText().toString();
                workfrom = "Work From Satellite";
                workfromID = 1;
                workfromIDtoString = "1";

                transportation = "";
                transportationIDtoString = "0";

                siteIDCheck = "-";
                siteLocationCheck = "-";

                if (Desc.isEmpty()){
                    Toast.makeText(CheckinActivity.this,"Please fill the Description",Toast.LENGTH_SHORT).show();
                    imgWfs.setImageResource(R.drawable.workfromsatelite_before);
                }else {

                    new AlertDialog.Builder(CheckinActivity.this)
                            .setMessage("Date : " + tglResult + " " + monthResult + " " + tahunResult + "\nTime : "
                                    + getTime+ "\nHealth :" + health + "\nWork Status :" + workfrom)
                            .setCancelable(false)
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    if (session.getStat().equals("CO")||session.getStat().equals("OO")) {
                                        checkIn();
                                    } else {
                                        checkOut();
                                    }

//                                Intent i = new Intent(CheckinActivity.this, FMActivity.class);
//                                i.putExtra(FMActivity.USER_ID,session.getUserFaceCode());
//                                i.putExtra(FMActivity.METHOD,FMActivity.METHOD_FR);
//                                startActivityForResult(i,AUTH_REQ_CODE);
                                }
                            })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    imgWfs.setImageResource(R.drawable.workfromsatelite_before);
                                }
                            })
                            .show();
                }
            }
        });

    }

    public void LnWfh (){
        lnWfh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgWfh.setImageResource(R.drawable.workfromhome);
                String Desc = etDescPost.getText().toString();
                workfrom = "Work From Home";
                workfromID = 2;
                workfromIDtoString = "2";

                transportation = "";
                transportationIDtoString = "0";

                siteIDCheck = "-";
                siteLocationCheck = "-";

                if (Desc.isEmpty()){
                    Toast.makeText(CheckinActivity.this,"Please fill the Description",Toast.LENGTH_SHORT).show();
                    imgWfh.setImageResource(R.drawable.workformhome_before);
                }else {

                    new AlertDialog.Builder(CheckinActivity.this)
                            .setMessage("Date : " + tglResult + " " + monthResult + " " + tahunResult + "\nTime : "
                                    + getTime + "\nHealth :" + health + "\nWork Status :" + workfrom)
                            .setCancelable(false)
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    if (session.getStat().equals("CO")||session.getStat().equals("OO")) {
                                        checkIn();
                                    } else {
                                        checkOut();
                                    }

//                                Intent i = new Intent(CheckinActivity.this, FMActivity.class);
//                                i.putExtra(FMActivity.USER_ID,session.getUserFaceCode());
//                                i.putExtra(FMActivity.METHOD,FMActivity.METHOD_FR);
//                                startActivityForResult(i,AUTH_REQ_CODE);
                                }
                            })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    imgWfh.setImageResource(R.drawable.workformhome_before);
                                }
                            })
                            .show();
                }
            }
        });
    }

    public void LnWfo (){
        lnWfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgWfo.setImageResource(R.drawable.workfromoffice);
                workfrom = "Work From Office";
                workfromID = 3;
                workfromIDtoString = "3";

                new AlertDialog.Builder(CheckinActivity.this)
                        .setMessage("Please choose your office site")
                        .setCancelable(false)
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                workfrom = "Work From Office";
                                lnThird.setVisibility(View.GONE);
                                lnForth.setVisibility(View.VISIBLE);
                                spinnerGetSelected();

                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                imgWfo.setImageResource(R.drawable.workfromoffice_before);
                            }
                        })
                        .show();
            }
        });
    }

    public void spinnerGetSelected(){

        showSpinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialogHelper.showProgressDialog(CheckinActivity.this,"Get office...");
                getOffice();


                System.out.println("check value office checkin : " + officeCheckin);
                if (officeCheckin.toString().equals("[]")){
                    Toast.makeText(CheckinActivity.this,"Your office is empty!",Toast.LENGTH_SHORT).show();
                } else {
                    popupAlert("Please choose your work location");
                }
            }
        });

//        siteIDCheck = session.getSiteId();
//        System.out.println("SiteSiteSite" + siteIDCheck);

//                        spinnerOfficeCheckin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//                            @Override
//                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//
//                                new AlertDialog.Builder(CheckinActivity.this)
//                                        .setMessage("Sure?")
//                                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
//                                            @Override
//                                            public void onClick(DialogInterface dialog, int which) {
//
//                                                siteID = officeCheckinId.get(position);
//                                                System.out.println("TESOFFICEID" + siteID);
//
//                                                lnForth.setVisibility(View.GONE);
//                                                lnFifth.setVisibility(View.VISIBLE);
//
//                                                LnPublicTransportation();
//                                                LnOwnTransportation();
//                                                LnWfhLast();
//                                            }
//                                        })
//                                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                                            @Override
//                                            public void onClick(DialogInterface dialog, int which) {
//
//                                            }
//                                        })
//                                        .show();
//
//                            }
//
//                            @Override
//                            public void onNothingSelected(AdapterView<?> parent) {
//
//                            }
//                        });

    }

    private void popupAlert(String title) {
        final Dialog dialog = new Dialog(CheckinActivity.this);
        dialog.setContentView(R.layout.choose_office_dialoh);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        dialog.getWindow().setAttributes(lp);
        dialog.setTitle("Input Code Here");
        TextView tvTitle = dialog.findViewById(R.id.tv_title_popupalert);
        TextView btnYes = dialog.findViewById(R.id.yes_button);
        spinnerOfficeCheckin = dialog.findViewById(R.id.spinner_dialog12);
//        getOffice();
        tvTitle.setText(title);
//        tvDesc.setText(desc);



        spinnerOfficeCheckin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                siteIDCheck = listOfficeLoc.get(position).getOfficeLocation();
                siteIDCheck = officeCheckinId.get(position);
                siteLocationCheck = officeCheckinlocation.get(position);

                System.out.println("TESOFFICEID" + siteIDCheck);
                System.out.println("TESOFFICE location" + siteLocationCheck);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        dialog.show();
        dialog.setCancelable(false);
        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                popLnFifth();
                tvFeel.setText("Please choose your transportation");
            }
        });
    }

    public void popLnFifth(){
        lnForth.setVisibility(View.GONE);
        lnFifth.setVisibility(View.VISIBLE);
        LnPublicTransportation();
        LnOwnTransportation();
        LnWfhLast();
    }

    public void LnPublicTransportation(){
        lnPublicTrans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgPublicTrans.setImageResource(R.drawable.publictransport);
                String Desc = etDescPost.getText().toString();
                transportation = "Public Transportation";
                transportationID = 1;
                transportationIDtoString = String.valueOf(transportationID);

                if (Desc.isEmpty()){
                    Toast.makeText(CheckinActivity.this,"Please fill the Description",Toast.LENGTH_SHORT).show();
                    imgPublicTrans.setImageResource(R.drawable.publictransport_before);
                }else {

                    new AlertDialog.Builder(CheckinActivity.this)
                            .setMessage("Date : " + tglResult + " " + monthResult + " " + tahunResult + "\nTime : "
                                    + getTime+ "\nHealth :" + health + "\nWork Status :" + workfrom+"\nOfficeId :" + siteIDCheck+ "\nTransportation :" + transportation)
                            .setCancelable(false)
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    if (session.getStat().equals("CO")||session.getStat().equals("OO")) {
                                        checkIn();
                                    } else {
                                        checkOut();
                                    }

//                                Intent i = new Intent(CheckinActivity.this, FMActivity.class);
//                                i.putExtra(FMActivity.USER_ID,session.getUserFaceCode());
//                                i.putExtra(FMActivity.METHOD,FMActivity.METHOD_FR);
//                                startActivityForResult(i,AUTH_REQ_CODE);
                                }
                            })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    imgPublicTrans.setImageResource(R.drawable.publictransport_before);
                                }
                            })
                            .show();
                }

            }
        });
    }

    public void LnOwnTransportation(){
        lnOwnTrans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgOwnTrans.setImageResource(R.drawable.owntransport);
                String Desc = etDescPost.getText().toString();
                transportation = "Own Transportation";
                transportationID = 2;
                transportationIDtoString = String.valueOf(transportationID);

                if (Desc.isEmpty()){
                    Toast.makeText(CheckinActivity.this,"Please fill the Description",Toast.LENGTH_SHORT).show();
                    imgOwnTrans.setImageResource(R.drawable.owntransport_before);
                }else {

                    new AlertDialog.Builder(CheckinActivity.this)
                            .setMessage("Date : " + tglResult + " " + monthResult + " " + tahunResult + "\nTime : "
                                    + getTime+ "\nHealth :" + health + "\nWork Status :" + workfrom+ "\nOfficeId :" + siteIDCheck+ "\nTransportation :" + transportation)
                            .setCancelable(false)
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    if (session.getStat().equals("CO")||session.getStat().equals("OO")) {
                                        checkIn();
                                    } else {
                                        checkOut();
                                    }

//                                Intent i = new Intent(CheckinActivity.this, FMActivity.class);
//                                i.putExtra(FMActivity.USER_ID,session.getUserFaceCode());
//                                i.putExtra(FMActivity.METHOD,FMActivity.METHOD_FR);
//                                startActivityForResult(i,AUTH_REQ_CODE);
                                }
                            })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    imgOwnTrans.setImageResource(R.drawable.owntransport_before);
                                }
                            })
                            .show();
                }

            }
        });
    }

    public void LnWfhLast(){
        lnOfficeTrans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgOfficeTrans.setImageResource(R.drawable.officetransport);
                String Desc = etDescPost.getText().toString();
                transportation = "Office Transportation";
                transportationID = 3;
                transportationIDtoString = String.valueOf(transportationID);

                if (Desc.isEmpty()){
                    Toast.makeText(CheckinActivity.this,"Please fill the Description",Toast.LENGTH_SHORT).show();
                    imgOfficeTrans.setImageResource(R.drawable.officetransport_before);
                }else {

                    new AlertDialog.Builder(CheckinActivity.this)
                            .setMessage("Date : " + tglResult + " " + monthResult + " " + tahunResult + "\nTime : "
                                    + getTime+ "\nHealth :" + health + "\nWork Status :" + workfrom+ "\nOfficeId :" + siteIDCheck + "\nTransportation :" + transportation)
                            .setCancelable(false)
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    if (session.getStat().equals("CO")||session.getStat().equals("OO")) {
                                        checkIn();
                                    } else {
                                        checkOut();
                                    }

//                                Intent i = new Intent(CheckinActivity.this, FMActivity.class);
//                                i.putExtra(FMActivity.USER_ID,session.getUserFaceCode());
//                                i.putExtra(FMActivity.METHOD,FMActivity.METHOD_FR);
//                                startActivityForResult(i,AUTH_REQ_CODE);
                                }
                            })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    imgOfficeTrans.setImageResource(R.drawable.officetransport_before);
                                }
                            })
                            .show();
                }


            }
        });
    }


    @SuppressLint("SimpleDateFormat")
    @Override
    protected void onResume() {
        super.onResume();
        final LocationManager manager = (LocationManager) getSystemService( Context.LOCATION_SERVICE );

        if ( !manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
            popupGPS();
        }

    }

    private void popupGPS() {
        final Dialog dialog = new Dialog(CheckinActivity.this);
        dialog.setContentView(R.layout.layout_enable_gps);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
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
                startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                dialog.dismiss();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AUTH_REQ_CODE) {
            if (session.getFMResult() == Activity.RESULT_OK) {
                //Snackbar.make(rootView,"Your face has successfuly registered",Snackbar.LENGTH_LONG).show();
                // User enrolled successfully
                try {
                    System.out.println("d2j4r4jlrn" + session.getFMResponse());
                    System.out.println("4jnrk34jn34r" + session.getFMMessage());
                    JSONObject dat = new JSONObject(session.getFMMessage());
                    System.out.println(dat + "dljwnk3j4nr");
                    if (dat.getString("result").equals("VERIFIED")) {
                        if (session.getStat().equals("CO") || session.getStat().equals("OO")) {
                            checkIn();
                        } else {
                            checkOut();
                        }
                    } else {
                        new AlertDialog.Builder(CheckinActivity.this)
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
                } catch (Exception e) {
                    System.out.println(e);
                    Toast.makeText(CheckinActivity.this, "Your face is not valid", Toast.LENGTH_SHORT).show();
                }


            } else if (resultCode == Activity.RESULT_CANCELED) {
                //System.out.println(data.getStringExtra(FMActivity.MESSAGE));
//                Toast.makeText(CheckinActivity.this,"Cancel",Toast.LENGTH_SHORT).show();
                // Enrollment cancelled
            } else {
//                Toast.makeText(CheckinActivity.this,"Else",Toast.LENGTH_SHORT).show();
            }
        }

        if (requestCode == AUTH_TRUE){
            if (resultCode == RESULT_OK){
                if (session.getTrue()==1){
                    popLnFifth();
                }
            }
        }

    }

    //    @RequiresApi(api = Build.VERSION_CODES.Q)
    public static List<String> getListOfFakeLocationApps(Context context) {
        List<String> runningApps = getRunningApps(context, false);
        for (int i = runningApps.size() - 1; i >= 0; i--) {
            String app = runningApps.get(i);
            if(!hasAppPermission(context, app, "android.permission.ACCESS_MOCK_LOCATION")){
                runningApps.remove(i);
            }
        }
        return runningApps;
    }

    //    @RequiresApi(api = Build.VERSION_CODES.Q)
    public static List<String> getRunningApps(Context context, boolean includeSystem) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);

        List<String> runningApps = new ArrayList<>();

        List<ActivityManager.RunningAppProcessInfo> runAppsList = activityManager.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo processInfo : runAppsList) {
            for (String pkg : processInfo.pkgList) {
                runningApps.add(pkg);
            }
        }

        try {
            //can throw securityException at api<18 (maybe need "android.permission.GET_TASKS")
            List<ActivityManager.RunningTaskInfo> runningTasks = activityManager.getRunningTasks(1000);
            for (ActivityManager.RunningTaskInfo taskInfo : runningTasks) {
                runningApps.add(taskInfo.topActivity.getPackageName());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        List<ActivityManager.RunningServiceInfo> runningServices = activityManager.getRunningServices(1000);
        for (ActivityManager.RunningServiceInfo serviceInfo : runningServices) {
            runningApps.add(serviceInfo.service.getPackageName());
        }

        runningApps = new ArrayList<>(new HashSet<>(runningApps));

        if(!includeSystem){
            for (int i = runningApps.size() - 1; i >= 0; i--) {
                String app = runningApps.get(i);
                if(isSystemPackage(context, app)){
                    runningApps.remove(i);
                }
            }
        }
        return runningApps;
    }

    public static boolean isSystemPackage(Context context, String app){
        PackageManager packageManager = context.getPackageManager();
        try {
            PackageInfo pkgInfo = packageManager.getPackageInfo(app, 0);
            return (pkgInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean hasAppPermission(Context context, String app, String permission){
        PackageManager packageManager = context.getPackageManager();
        PackageInfo packageInfo;
        try {
            packageInfo = packageManager.getPackageInfo(app, PackageManager.GET_PERMISSIONS);
            if(packageInfo.requestedPermissions!= null){
                for (String requestedPermission : packageInfo.requestedPermissions) {
                    if (requestedPermission.equals(permission)) {
                        return true;
                    }
                }
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
//        progressDialogHelper.showProgressDialog(CheckinActivity.this, "Getting data...");
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap.setMyLocationEnabled(true);
        mMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location location) {
                CameraUpdate center = CameraUpdateFactory.newLatLng(new LatLng(location.getLatitude(), location.getLongitude()));
                CameraUpdate zoom = CameraUpdateFactory.zoomTo(15);
                mMap.clear();
                MarkerOptions mp = new MarkerOptions();
                mp.position(new LatLng(location.getLatitude(), location.getLongitude()));
                
                String lat = String.valueOf(location.getLatitude());
                String lon = String.valueOf(location.getLongitude());
                System.out.println(lat+", "+lon+" dkn3b4jr44");
                session.setLatLon(lat+", "+lon);

                // get date
                SimpleDateFormat tgl = new SimpleDateFormat("yyyy-MM-dd");
                getDate = tgl.format(location.getTime());

                // get time
                SimpleDateFormat wkt = new SimpleDateFormat("HH:mm:ss");
                getTime = wkt.format(location.getTime());

                @SuppressLint("SimpleDateFormat") SimpleDateFormat wkttmpl = new SimpleDateFormat("HH:mm");
                tvTime.setText(wkttmpl.format(location.getTime()));

                System.out.println("check value getDate : " + getDate + "\n" + "check value getTime : " + getTime + "\n" + "check value for tvTime textview : " + new SimpleDateFormat("HH:mm").format(location.getTime()));

                mp.title("Your Location");
                mMap.addMarker(mp);
                mMap.moveCamera(center);
                mMap.animateCamera(zoom);
            }

        });
    }


    public  static File saveImage(Context context, String imageData)  {
        System.out.println(imageData+"kqn3kje");
        final byte[] imgBytesData;
        if (imageData.equals("")||imageData.equals(null)) {
            imgBytesData = android.util.Base64.decode("0",
                    android.util.Base64.DEFAULT);
        } else {
            imgBytesData = android.util.Base64.decode(imageData,
                    android.util.Base64.DEFAULT);
        }

        File file;

        FileOutputStream fileOutputStream;
        try {
            file = File.createTempFile("image", null, context.getCacheDir());
            fileOutputStream = new FileOutputStream(file);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        final BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(
                fileOutputStream);
        try {
            bufferedOutputStream.write(imgBytesData);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                bufferedOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file;
    }

    private void checkIn() {


        progressDialogHelper.showProgressDialog(CheckinActivity.this, "Check In on progress...");

        StDesc = etDescPost.getText().toString();

        SimpleDateFormat tgl = new SimpleDateFormat("yyyy-MM-dd");
        String tRes = tgl.format(new Date());

        SimpleDateFormat jam = new SimpleDateFormat("HH:mm:ss");
        String jamRes = jam.format(new Date());

        String tipe_absen = "CI";

        System.out.println("location testing saat checkin : " + session.getLatLon() );
        System.out.println("TOKEKKK"+session.getToken());

        String urlCheckin = session.getServerURL()+"users/"+session.getUserNIK()+"/newpresence/"+session.getUserNIK()+"/buscd/"+session.getUserBusinessCode();
        System.out.println("url checkin absen : " + urlCheckin);

        AndroidNetworking.upload(urlCheckin)
                .addHeaders("Accept","application/json")
                .addHeaders("Content-Type","application/json")
                .addHeaders("Authorization",session.getToken())
                .addMultipartParameter("begin_date",tRes)
                .addMultipartParameter("end_date","9999-12-31")
                .addMultipartParameter("business_code",session.getUserBusinessCode())
                .addMultipartParameter("personal_number",session.getUserNIK())
                .addMultipartParameter("presence_type",tipe_absen)
                .addMultipartParameter("date",getDate)
                .addMultipartParameter("time",getTime)
                .addMultipartParameter("emoticon",session.getEmot())
                .addMultipartParameter("location",session.getLatLon())
                .addMultipartParameter("change_user",session.getUserNIK())
                .addMultipartParameter("presensi_status","00")
                .addMultipartParameter("description",StDesc)
                .addMultipartParameter("building_id",siteIDCheck)
                .addMultipartParameter("location_office",siteLocationCheck)
                .addMultipartParameter("health_status",healthIDtoString)
                .addMultipartParameter("health_description",health)
                .addMultipartParameter("working_status_location",workfromIDtoString)
                .addMultipartParameter("working_location_description",workfrom)
                .addMultipartParameter("transport_status", transportationIDtoString)
                .addMultipartParameter("transport_description", transportation)
//                .addMultipartFile("evidence",saveImage(CheckinActivity.this, "tyrt"))
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // do anything with response
                        System.out.println(response+"edknwkjrn4Checkin");

                        System.out.println("check locatiio: " + session.getLatLon());

                        System.out.println("PARAM checkin:\n" + "begin_date: " + tRes + "\n"
                                + "end_date: " + "9999-12-31" + "\n"
                                + "business_code: " + session.getUserBusinessCode() + "\n"
                                + "personal_number: " + session.getUserNIK() + "\n"
                                + "presence_type: " + tipe_absen + "\n"
                                + "date: " + tRes + "\n"
                                + "time: " + jamRes + "\n"
                                + "emoticon: " + session.getEmot() + "\n"
                                + "location: " + session.getLatLon() + "\n"
                                + "change_user: " + session.getUserNIK() + "\n"
                                + "presensi_status: " + "00" + "\n"
                                + "description: " + StDesc + "\n"
                                + "building_id: " + siteIDCheck + "\n"
                                + "building_location: " + siteLocationCheck + "\n"
                                + "health_status: " + healthIDtoString + "\n"
                                + "working_status_location: " + workfromIDtoString + "\n"
                                + "health_description: " + health + "\n"
                                + "working_location_description: " + workfrom + "\n"
                                + "transport_status: " + transportationIDtoString + "\n"
                                + "transport_description: " + transportation + "\n"
                        );
                        try {
                            if(response.getInt("status")==200){

                                session.setStat("CO");
                                Toast.makeText(CheckinActivity.this,"Thanks for check in today!",Toast.LENGTH_SHORT).show();
                                finish();

//                                Intent i = new Intent(CheckinActivity.this, HomeFragment.class);
//                                i.putExtra("checkinstat","CO");
//                                startActivity(i);

                            }else if(response.getInt("status")==500){

                                Toast.makeText(CheckinActivity.this,response.getString("message"),Toast.LENGTH_LONG).show();

                            } else {

                                String printError = response.getString("message");
                                Toast.makeText(CheckinActivity.this,printError,Toast.LENGTH_LONG).show();

                            }

                            progressDialogHelper.dismissProgressDialog(CheckinActivity.this);

                        }catch (Exception e){

                            System.out.println("123456HAHA"+e);
                            System.out.println("ERRORDISINI"+ e.getMessage());

                            Toast.makeText(CheckinActivity.this,e.toString(),Toast.LENGTH_LONG).show();
                            progressDialogHelper.dismissProgressDialog(CheckinActivity.this);

                        }
                    }

                    @Override
                    public void onError(ANError error) {

                        System.out.println("onerror di checkin : " + error.getErrorBody());
                        Toast.makeText(CheckinActivity.this,"Something wrong!",Toast.LENGTH_LONG).show();
                        progressDialogHelper.dismissProgressDialog(CheckinActivity.this);

                    }
                });
    }

    private void checkOut() {
        progressDialogHelper.showProgressDialog(CheckinActivity.this, "Check Out on progress...");

        StDesc = etDescPost.getText().toString();

        Log.d("TESIDCHECKOUT", transportationID + " " + workfromID + " " + healthID);
        Log.d("TESIDSTRINGCHECKOUT", transportationIDtoString + " " + workfromIDtoString + " " + healthIDtoString);

        SimpleDateFormat tglPlus = new SimpleDateFormat("yyyy-MM-dd");
        String tResplus = tglPlus.format(new Date());
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(tglPlus.parse(tResplus));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        c.add(Calendar.DATE, -1);
        SimpleDateFormat sdf1 = new SimpleDateFormat("MM-dd-yyyy");
        String output = sdf1.format(c.getTime());

        SimpleDateFormat jam = new SimpleDateFormat("HH:mm:ss");
        String jamRes = jam.format(new Date());
        System.out.println(jamRes+"HASILJAM"+output);

        SimpleDateFormat tgl = new SimpleDateFormat("yyyy-MM-dd");
        String tRes = tgl.format(new Date());
        System.out.println("check tres : " + tRes);

        JSONObject jsonObject = new JSONObject();
        String tipe_absen = "CO";

        String urlCheckout = session.getServerURL()+"users/"+session.getUserNIK()+"/newpresence/+"+session.getUserNIK()+"/buscd/"+session.getUserBusinessCode();
        System.out.println("check url checkout absen : " + urlCheckout);

        AndroidNetworking.upload(urlCheckout)
                .addHeaders("Accept","application/json")
                .addHeaders("Content-Type","application/json")
                .addHeaders("Authorization",session.getToken())
                .addMultipartParameter("begin_date",tRes)
                .addMultipartParameter("end_date","9999-12-31")
                .addMultipartParameter("business_code",session.getUserBusinessCode())
                .addMultipartParameter("personal_number",session.getUserNIK())
                .addMultipartParameter("presence_type",tipe_absen)
                .addMultipartParameter("date",getDate)
                .addMultipartParameter("time",getTime)
                .addMultipartParameter("emoticon",session.getEmot())
                .addMultipartParameter("location",session.getLatLon())
                .addMultipartParameter("change_user",session.getUserNIK())
                .addMultipartParameter("presensi_status","00")
                .addMultipartParameter("description",StDesc)
                .addMultipartParameter("building_id",siteIDCheck)
                .addMultipartParameter("location_office",siteLocationCheck)
                .addMultipartParameter("health_status",healthIDtoString)
                .addMultipartParameter("working_status_location",workfromIDtoString)
                .addMultipartParameter("health_description",health)
                .addMultipartParameter("working_location_description",workfrom)
                .addMultipartParameter("transport_status", transportationIDtoString)
                .addMultipartParameter("transport_description", transportation)
//                .addMultipartFile("evidence",saveImage(CheckinActivity.this, "sdfwe"))
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // do anything with response
                        System.out.println(response+"edknwkjrn4Checkout");
                        System.out.println("PARAM checkout:\n" + "begin_date: " + tRes + "\n"
                                + "end_date: " + "9999-12-31" + "\n"
                                + "business_code: " + session.getUserBusinessCode() + "\n"
                                + "personal_number: " + session.getUserNIK() + "\n"
                                + "presence_type: " + tipe_absen + "\n"
                                + "date: " + tRes + "\n"
                                + "time: " + jamRes + "\n"
                                + "emoticon: " + session.getEmot() + "\n"
                                + "location: " + session.getLatLon() + "\n"
                                + "change_user: " + session.getUserNIK() + "\n"
                                + "presensi_status: " + "00" + "\n"
                                + "description: " + StDesc + "\n"
                                + "building_id: " + siteIDCheck + "\n"
                                + "building_location " + siteLocationCheck + "\n"
                                + "health_status: " + healthIDtoString + "\n"
                                + "working_status_location: " + workfromIDtoString + "\n"
                                + "health_description: " + health + "\n"
                                + "working_location_description: " + workfrom + "\n"
                                + "transport_status: " + transportationIDtoString + "\n"
                                + "transport_description: " + transportation + "\n"
                        );
                        try {
                            if(response.getInt("status")==200){

                                session.setStat("CI");
                                Toast.makeText(CheckinActivity.this, "Success chekout, thanks for today :)", Toast.LENGTH_SHORT).show();
                                finish();
//                                Intent i = new Intent(CheckinActivity.this, HomeFragment.class);
//                                i.putExtra("checkinstat","CI");
//                                startActivity(i);

                            }else if(response.getInt("status")==500){

                                Toast.makeText(CheckinActivity.this,response.getString("message"),Toast.LENGTH_LONG).show();

                            } else {

                                Toast.makeText(CheckinActivity.this,response.getString("message"),Toast.LENGTH_SHORT).show();

                            }

                            progressDialogHelper.dismissProgressDialog(CheckinActivity.this);
                        }catch (Exception e){

                            System.out.println("exception di checkout : " + e);
                            System.out.println("ERRORDISINICHRCKOUT : "+ e.getMessage());
                            Toast.makeText(CheckinActivity.this,e.toString(),Toast.LENGTH_SHORT).show();
                            progressDialogHelper.dismissProgressDialog(CheckinActivity.this);

                        }
                    }
                    @Override
                    public void onError(ANError error) {

                        System.out.println("test on error di on error checkout : " + error.getErrorBody());
                        Toast.makeText(CheckinActivity.this,"Something wrong",Toast.LENGTH_SHORT).show();
                        progressDialogHelper.dismissProgressDialog(CheckinActivity.this);

                    }
                });
    }

    private void getOffice(){
        String urlGetOffice = session.getServerURL()+"users/presensi/getlocation?personal_number="+session.getLoginUsername();
        System.out.println("check url get office : " + urlGetOffice);
        AndroidNetworking.get(urlGetOffice)
                .addHeaders("Accept","application/json")
                .addHeaders("Content-Type","application/json")
                .addHeaders("Authorization",session.getToken())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println("Tes Get Office" + response);
                        try {
                            if(response.getInt("status")==200){
//                                session.setToken(response.getString("token"));
                                officeCheckin = new ArrayList<String>();
                                JSONArray jsonArray = response.getJSONArray("data");
                                if(jsonArray.length()>0){
                                    for (int i=0; i<jsonArray.length(); i++) {
                                        JSONObject obj = jsonArray.getJSONObject(i);
                                        String buildingName = obj.getString("building_name");
                                        String buildingID = obj.getString("building_id");
                                        String location_office = obj.getString("location");

                                        officeModel = new OfficeCheckinModel(buildingName,buildingID);
                                        officeCheckin.add(buildingName);
                                        officeCheckinId.add(buildingID);
                                        officeCheckinlocation.add(location_office);


                                    }
                                    ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(CheckinActivity.this, R.layout.spinner_item_list, officeCheckin);
                                    spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
                                    spinnerOfficeCheckin.setAdapter(spinnerArrayAdapter);

                                }
//                                spinnerCheckinAdapter = new OfficeCheckinAdapter(CheckinActivity.this,spinnerCheckinList);
                            }
                            else{
                                Toast.makeText(CheckinActivity.this,response.getString("message"),Toast.LENGTH_SHORT).show();
                            }


                        } catch (Exception e){
                            Toast.makeText(CheckinActivity.this,"Please retry chosing the office",Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }

                        progressDialogHelper.dismissProgressDialog(CheckinActivity.this);
                    }

                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(CheckinActivity.this,"Something wrong!",Toast.LENGTH_SHORT).show();
                        System.out.println("masuk ke on error get office : " + anError.getErrorBody());
                        progressDialogHelper.dismissProgressDialog(CheckinActivity.this);
                    }
                });
    }

    private void getStatCheckin(){
        SimpleDateFormat tgl = new SimpleDateFormat("yyyy-MM-dd");
        String tRes = tgl.format(new Date());
//        progressDialogHelper.showProgressDialog(CheckinActivity.this, "Getting data...");
        AndroidNetworking.get(session.getServerURL()+"users/"+session.getUserNIK()+"/statuspresensi/"+session.getUserNIK()+"/buscd/"+session.getUserBusinessCode()+"/date/"+tRes)
                .addHeaders("Accept","application/json")
                .addHeaders("Content-Type","application/json")
                .addHeaders("Authorization",session.getToken())
                //.addJSONObjectBody(body)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println("ebk2j3nj32ePresensi"+response);
                        // do anything with response
                        try {
                            if(response.getInt("status")==200){
//                                session.setToken(response.getString("token"));
                                JSONArray jsonArray = response.getJSONArray("data");
                                if (jsonArray.length()==0) {
                                    session.setStat("CO");
                                }
                                for (int i=0; i<jsonArray.length(); i++) {
                                    JSONObject obj = jsonArray.getJSONObject(i);
                                    String stat = obj.getString("presence_type");
                                    session.setStat(stat);
                                }
                                System.out.println(jsonArray.length()+" ql3jen2kelr");
                            }else{
                            }
                            System.out.println("status ya : "+session.getStat());
//                            progressDialogHelper.dismissProgressDialog(CheckinActivity.this);
                        }catch (Exception e){
//                            progressDialogHelper.dismissProgressDialog(CheckinActivity.this);
                            System.out.println(e);
                        }
                    }
                    @Override
                    public void onError(ANError error) {
//                        progressDialogHelper.dismissProgressDialog(CheckinActivity.this);
                        System.out.println(error);
                    }
                });
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        return false;
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }


}
