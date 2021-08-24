package id.co.pegadaian.diarium.controller.home;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.mikhaellopez.circularprogressbar.CircularProgressBar;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import id.co.pegadaian.diarium.R;
import id.co.pegadaian.diarium.adapter.InboxAdapter;
import id.co.pegadaian.diarium.adapter.MenuAdapter;
import id.co.pegadaian.diarium.adapter.MyPostingAdapter;
import id.co.pegadaian.diarium.adapter.SliderAdapterExample;
import id.co.pegadaian.diarium.controller.home.main_menu.posting.EmployeePostingActivity;
import id.co.pegadaian.diarium.controller.login.LoginActivity;
import id.co.pegadaian.diarium.controller.home.main_menu.community.CommunityActivity;
import id.co.pegadaian.diarium.controller.home.main_menu.employee_care.EmployeeCareActivity;
import id.co.pegadaian.diarium.controller.home.main_menu.hr_wiki.HRWikiActivity;
import id.co.pegadaian.diarium.controller.home.main_menu.mytime.checkin.CheckinActivity;
import id.co.pegadaian.diarium.controller.home.main_menu.personal_data.PersonalDataActivity;
import id.co.pegadaian.diarium.controller.home.main_menu.report.ReportActivity;
import id.co.pegadaian.diarium.controller.home.main_menu.search_partner.SearchTempActivity;
import id.co.pegadaian.diarium.controller.home.main_menu.survey.SurveyActivity;
import id.co.pegadaian.diarium.controller.home.main_menu.today_activity.TodayActivity;
import id.co.pegadaian.diarium.controller.profile.ProfileActivity;
import id.co.pegadaian.diarium.model.InboxModel;
import id.co.pegadaian.diarium.model.MyPostingModel;
import id.co.pegadaian.diarium.model.SliderItem;
import id.co.pegadaian.diarium.util.ExpandableHeightGridView;
import id.co.pegadaian.diarium.util.UserSessionManager;
import id.co.pegadaian.diarium.util.qiscus.ui.activity.QiscusChatActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {
    TextView tvName, tvNullPost;
    UserSessionManager session;
    private String namaFile;
    ListView lvPost;
    private List<InboxModel> listModel;
    private InboxModel model;
    private InboxAdapter adapter;
    private List<MyPostingModel> listModelPost;
    private MyPostingModel modelPost;
    private MyPostingAdapter adapterPost;
    EditText etSearch;
    CircularProgressBar circularProgressBar;
    TextView menu_checkin, tvVersion;
    Typeface font,fontbold;
    ImageView ivProfile;
    private ProgressDialog progressDialog;
//    private ProgressDialogHelper progressDialogHelper;
    ExpandableHeightGridView gridView;
    SwipeRefreshLayout swipeRefreshLayout;
    String tgl, statusCheckinFromIntent = "", versionName;



    // banner
    SliderView sliderView;
    private SliderItem bannerModel;
    private List<SliderItem> listModelBanner;



    String[] menu ={
            "",
            "Today Activity",
            "Report Activity",
            "Personal",
            "Social Media",
            "My Profile"
    };

    int[] image={
            R.drawable.ic_checkin,
            R.drawable.ic_today_activity,
            R.drawable.ic_report_activity,
            R.drawable.ic_personal,
            R.drawable.social,
            R.drawable.profilee
            //  R.drawable.ic_today_activity
    };

    public HomeFragment() {
        // Required empty public constructor
    }
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        session = new UserSessionManager(getActivity());
        progressDialog = new ProgressDialog(getActivity());
//        progressDialogHelper = new ProgressDialogHelper();

        System.out.println("Business_code "+session.getUserBusinessCode());
        System.out.println("check token " + session.getToken());

        View view = inflater.inflate(R.layout.fragment_home, container, false);
        font = Typeface.createFromAsset(getActivity().getAssets(),"fonts/Nexa Light.otf");
        fontbold = Typeface.createFromAsset(getActivity().getAssets(),"fonts/Nexa Bold.otf");

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        tgl = sdf.format(new Date());

        getStatCheckin();

        // get version name
        tvVersion = view.findViewById(R.id.tv_version_homefrag);
        try {
            PackageInfo pInfo = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0);
            versionName = pInfo.versionName;
            tvVersion.setText("v " + versionName);
        } catch (PackageManager.NameNotFoundException e) {
            System.out.println("catch di get version name : " + e.getMessage());
        }

        System.out.println("test status checkin/checkout: " + session.getStat());

        if (session.getStat().equals("CO")||session.getStat().equals("OO")) {
            this.menu[0] = "Check In";
        } else {
            this.menu[0] = "Check Out";
        }

        // for banner
        sliderView = view.findViewById(R.id.imageSlider);
        listModelBanner = new ArrayList<SliderItem>();
        String bannerImage = "";
        System.out.println("cek image banner : " + bannerImage);
        bannerModel = new SliderItem(bannerImage);
        listModelBanner.add(bannerModel);
        sliderView.setSliderAdapter(new SliderAdapterExample(getActivity(),listModelBanner));
        sliderView.setScrollTimeInSec(4); //set scroll delay in seconds :
        sliderView.startAutoCycle();

        // for flipping banner automatically
//        sliderView.setIndicatorAnimation(IndicatorAnimationType.WORM);
//        sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
//        sliderView.startAutoCycle();

        TextView more = view.findViewById(R.id.moreinbox);
        gridView = view.findViewById(R.id.gridview);
        ivProfile= view.findViewById(R.id.ivProfile);
        TextView a = view.findViewById(R.id.tvGreeting);
        tvName = view.findViewById(R.id.tvTitle);
//        tvNullInbox = view.findViewById(R.id.tvNullInbox);
        tvNullPost = view.findViewById(R.id.tvNullPost);
        etSearch = view.findViewById(R.id.inputSearch);
        TextView a1 = view.findViewById(R.id.judulsub);
//        TextView b1 = view.findViewById(R.id.judulsub2);
//        TextView more1= view.findViewById(R.id.morepost);
        LinearLayout set = view.findViewById(R.id.profile);
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_homefrag);

        MenuAdapter gridAdapter = new MenuAdapter(getActivity(),image,menu);
        gridView.setAdapter(gridAdapter);
        gridView.setExpanded(true);
        try {
            Picasso.get().load(session.getAvatar()).error(R.drawable.profile).into(ivProfile);
            System.out.println("avatar user: " + session.getAvatar());
        } catch (Exception e){
            Picasso.get().load(R.drawable.profile).into(ivProfile);
        }


        // get status checkin  (ga kepake karna bikin error intent ke fragment)
        try {
            Intent intent = new Intent();
            statusCheckinFromIntent = intent.getStringExtra("checkinstat");
            System.out.println("check value statusCheckinFromIntent : " + statusCheckinFromIntent);

            if (statusCheckinFromIntent.equals(null)){
                System.out.println("masuk ke nunununl");
            } else if (statusCheckinFromIntent.equals("CO")) {
                this.menu[0] = "Check In";
            } else {
                this.menu[0] = "Check Out";
            }
        } catch (Exception e){
            System.out.println("error di intentnya : " + e);
        }
        // get status checkin  (ga kepake karna bikin error intent ke fragment)




        a.setTypeface(fontbold);
        Calendar c = Calendar.getInstance();
        int timeOfDay = c.get(Calendar.HOUR_OF_DAY);

        if(timeOfDay >= 0 && timeOfDay < 12){
            a.setText("Good Morning");
        }else if(timeOfDay >= 12 && timeOfDay < 16){
            a.setText("Good Afternoon");
        }else if(timeOfDay >= 16 && timeOfDay < 21){
            a.setText("Good Evening");
        }else if(timeOfDay >= 21 && timeOfDay < 24){
            a.setText("Good Night");
        }
        tvName.setTypeface(fontbold);
        tvName.setText(session.getUserFullName());
//        a1.setTypeface(fontbold);
//        b1.setTypeface(fontbold);
//        more1.setTypeface(fontbold);
//        more.setTypeface(fontbold);

        showBanner();
        SimpleDateFormat timeStampFormat = new SimpleDateFormat("MM-dd");
        Date myDate = new Date();
        String currentDate = timeStampFormat.format(myDate);
        if (currentDate.equals(session.getBornDate()) && session.getStatusClickBornDate().equals("0")) {
            popupBirthday();
        }

//        more.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent i = new Intent(getActivity(), HomeActivity.class);
//                i.putExtra("key", "moreinbox");
//                startActivity(i);
//            }
//        });

//        more1.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View v) {
//                Intent hariian = new Intent(getActivity(), EmployeePostingActivity.class);
//                startActivity(hariian);
//            }
//        });

        set.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent hariian = new Intent(getActivity(), ProfileActivity.class);
                hariian.putExtra("personal_number", session.getUserNIK());
                hariian.putExtra("avatar", session.getAvatar());
                startActivity(hariian);
            }
        });

        etSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), SearchTempActivity.class);
                startActivity(i);
            }
        });

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                                    long arg3) {
                String clickedText = menu[position];
//                Toast.makeText(getActivity(), "posisi"+clickedText, Toast.LENGTH_SHORT).show();
                switch (clickedText) {
                    case "My Time":
                        Intent a = new Intent(getActivity(), SubMyTimeActivity.class);
                        a.putExtra("code", "MyTime");
                        startActivity(a);
                        break;
                    case "My Performance":
                        Intent b = new Intent(getActivity(), SubMyTimeActivity.class);
                        b.putExtra("code", "MyPerformance");
                        startActivity(b);
                        break;
                    case "My Task":
                        Intent c = new Intent(getActivity(), SubMyTimeActivity.class);
                        c.putExtra("code", "MyTask");
                        startActivity(c);
                        break;
//                    case "My Profile":
//                        Intent intent = new Intent(getActivity(), SubMyTimeActivity.class);
//                        intent.putExtra("code", "MyProfile");
//                        startActivity(intent);
//                        break;
                    case "My Travel":
                        Intent d = new Intent(getActivity(), SubMyTimeActivity.class);
                        d.putExtra("code", "MyTravel");
                        startActivity(d);
                        break;
                    case "My Integrity":
                        Intent e = new Intent(getActivity(), SubMyTimeActivity.class);
                        e.putExtra("code", "MyIntegrity");
                        startActivity(e);
                        break;
                    case "My Development":
                        Intent f = new Intent(getActivity(), SubMyTimeActivity.class);
                        f.putExtra("code", "MyDevelopment");
                        startActivity(f);
                        break;
                    case "FAQ (Chatbot)":
//                        Toast.makeText(getActivity(), "not available", Toast.LENGTH_SHORT).show();
                        Intent h = new Intent(getActivity(), QiscusChatActivity.class);
                        System.out.println(session.getUserNIK()+"@diarium.co.id"+ "NIKNYA");
                        h.putExtra("name","Dexy-Kiwari");
                        h.putExtra("email",session.getUserNIK()+"@diarium.co.id");
                        startActivity(h);
                        break;
                    case "Community":
//                        Toast.makeText(getActivity(), "This menu not available in trial mode", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(getActivity(), CommunityActivity.class);
                        startActivity(i);
                        break;
                    case "Employee Survey":
//                        Toast.makeText(getActivity(), "This menu not available in trial mode", Toast.LENGTH_SHORT).show();
                        Intent j = new Intent(getActivity(), SurveyActivity.class);
                        startActivity(j);
                        break;
                    case "Employee Care":
//                        Toast.makeText(getActivity(), "This menu not available in trial mode", Toast.LENGTH_SHORT).show();
                        Intent k = new Intent(getActivity(), EmployeeCareActivity.class);
                        startActivity(k);
                        break;
                    case "HR Wiki":
//                        Toast.makeText(getActivity(), "This menu not available in trial mode", Toast.LENGTH_SHORT).show();
                        Intent l = new Intent(getActivity(), HRWikiActivity.class);
                        startActivity(l);
                        break;
                    case "Check In":
                        Intent checkin = new Intent(getActivity(), CheckinActivity.class);
                        startActivity(checkin);
                        break;
                    case "Check Out":
                        Intent checkout = new Intent(getActivity(), CheckinActivity.class);
                        startActivity(checkout);
                        break;
                    case "Today Activity":
                        i = new Intent(getActivity(), TodayActivity.class);
                        i.putExtra("personal_number",session.getUserNIK());
                        i.putExtra("name",session.getUserFullName());
                        i.putExtra("status","none");
                        i.putExtra("position",session.getJob());
                        i.putExtra("avatar",session.getAvatar());
                        startActivity(i);
                        break;
                    case "Report Activity":
                        i = new Intent(getActivity(), ReportActivity.class);
                        startActivity(i);
                        break;
                    case "Personal":
                        i = new Intent(getActivity(), PersonalDataActivity.class);
                        startActivity(i);
                        break;
                    case "My Profile":
                        i = new Intent(getActivity(), ProfileActivity.class);
                        i.putExtra("personal_number", session.getUserNIK());
                        i.putExtra("avatar", session.getAvatar());
                        startActivity(i);
                        break;
                    case "Social Media":
                        i = new Intent(getActivity(), EmployeePostingActivity.class);
                        startActivity(i);
                        break;
                }
            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                // checkin checkin/checkout
                getStatCheckin();
                System.out.println("test status checkin/checkout di swipe refresh layout : " + session.getStat());

                if (session.getStat().equals("CO")||session.getStat().equals("OO")) {
                    menu[0] = "Check In";
                } else {
                    menu[0] = "Check Out";
                }

                MenuAdapter gridAdapter = new MenuAdapter(getActivity(),image,menu);
                gridView.setAdapter(gridAdapter);
                gridView.setExpanded(true);
                // checkin checkin/checkout

                showBanner();
                getMyPost();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

//        getInbox();
        getMyPost();

        return view;
    }



    @Override
    public void onResume(){
        super.onResume();

        // checkin checkin/checkout
        getStatCheckin();
        System.out.println("test status checkin/checkout di onResume() : " + session.getStat());

        if (session.getStat().equals("CO")||session.getStat().equals("OO")) {
            menu[0] = "Check In";
        } else {
            menu[0] = "Check Out";
        }

        MenuAdapter gridAdapter = new MenuAdapter(getActivity(),image,menu);
        gridView.setAdapter(gridAdapter);
        gridView.setExpanded(true);
        // checkin checkin/checkout

    }

    private void showBanner(){
        System.out.println("masuk ke show banner");

        String urlShowBanner = session.getServerURL()+"users/banner?begin_date_lte="+tgl+"&end_date_gt="+tgl;
        System.out.println("cek url get banner : " + urlShowBanner);
        AndroidNetworking.get(urlShowBanner)
                .addHeaders("Authorization",session.getToken())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println("response get banner : " + response);
                        // do anything with response
                        try {
                            if(response.getInt("status")==200){

                                listModelBanner = new ArrayList<SliderItem>();
                                String bannerImage = "";
                                JSONArray jsonArray = response.getJSONArray("data");
                                for (int a=0;a<jsonArray.length();a++) {
                                    JSONObject obj_name = jsonArray.getJSONObject(a);

                                    bannerImage = obj_name.getString("image_url");
                                    System.out.println("cek image banner : " + bannerImage);

                                    bannerModel = new SliderItem(bannerImage);
                                    listModelBanner.add(bannerModel);
                                }

                                sliderView.setSliderAdapter(new SliderAdapterExample(getActivity(),listModelBanner));
                                sliderView.setScrollTimeInSec(4); //set scroll delay in seconds :
                                sliderView.startAutoCycle();


                            }else{
                                System.out.println("gagal get banner : " + response.getString("message"));
                            }
                        }catch (Exception e){
                            System.out.println("masuk ke catch get banner : " + e);
                        }
                    }
                    @Override
                    public void onError(ANError error) {
                        System.out.println("masuk ke on error get banner :" + error);
                    }
                });

    }



    private void getStatCheckin(){
        SimpleDateFormat tgl = new SimpleDateFormat("yyyy-MM-dd");
        String tRes = tgl.format(new Date());
        String urlGetStatCheckin = session.getServerURL()+"users/"+session.getUserNIK()+"/statuspresensi/"
                +session.getUserNIK()+"/buscd/"+session.getUserBusinessCode()+"/date/"+tRes;
        System.out.println("check url getStatCheckin : " + urlGetStatCheckin);
        AndroidNetworking.get(urlGetStatCheckin)
                .addHeaders("Accept","application/json")
                .addHeaders("Content-Type","application/json")
                .addHeaders("Authorization",session.getToken())
                //.addJSONObjectBody(body)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println("ebk2j3nj32ePresensi : "+response);
                        // do anything with response
                        try {
                            if(response.getInt("status")==200){
//                                session.setToken(response.getString("token"));
                                JSONArray jsonArray = response.getJSONArray("data");
                                if (jsonArray.length()==0) {

                                    System.out.println("array length nya presence : " + jsonArray.length());
                                    session.setStat("CO");
                                    menu[0] = "Check In";

                                }
                                for (int i=0; i<jsonArray.length(); i++) {
                                    JSONObject obj = jsonArray.getJSONObject(i);
                                    String stat = obj.getString("presence_type");
                                    System.out.println("check sat presence type : " + stat);
                                    session.setStat(stat);
//                                    menu[0] = "Check Out";
                                }
//                                if (session.getStat().equals("CO")||session.getStat().equals("OO")) {
////            Toast.makeText(getActivity(), "Saatnya Checkin", Toast.LENGTH_SHORT).show();
//                                    menu_checkin.setText("Check In");
//                                } else {
////            Toast.makeText(getActivity(), "Saatnya Checkout", Toast.LENGTH_SHORT).show();
//                                    menu_checkin.setText("Check Out");
//                                }

                                System.out.println("test status checkin/checkout: " + session.getStat());

                                if (session.getStat().equals("CO")||session.getStat().equals("OO")) {
                                    menu[0] = "Check In";
                                } else {
                                    menu[0] = "Check Out";
                                }

                                System.out.println(jsonArray.length()+" ql3jen2kelr");
                            }else{
                            }
                            System.out.println("status ya : "+session.getStat());
                        }catch (Exception e){
                            System.out.println(e);
                        }
                    }
                    @Override
                    public void onError(ANError error) {
                        System.out.println("error di getStatCheckin : " + error);
                    }
                });

        System.out.println("check ci/co di getStatCheckin() : " + session.getStat());
    }

    private void popupBirthday() {
        final Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.layout_happy_birthday);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        dialog.getWindow().setAttributes(lp);
        dialog.setTitle("Input Code Here");
        TextView tvGreeting =(TextView) dialog.findViewById(R.id.tvGreeting);
        dialog.show();
        session.setStatusClickBornDate("1");
        dialog.setCancelable(true);
        tvGreeting.setText("Happy Birthday "+session.getUserFullName());
    }

    private void getMyPost(){
        // pindah ke social media icon
    }




    private void getInbox(){
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
//                                    lvInbox.setVisibility(View.GONE);
//                                    tvNullInbox.setVisibility(View.VISIBLE);
                                } else {
//                                    lvInbox.setVisibility(View.VISIBLE);
//                                    tvNullInbox.setVisibility(View.GONE);
                                    for (int a = 0; a < 1; a++) {
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
                                    adapter = new InboxAdapter(getActivity(), listModel);
//                                    lvInbox.setAdapter(adapter);
//                                    lvInbox.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                                        @Override
//                                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                                            Intent i = new Intent(getActivity(), DetailInboxActivity.class);
//                                            i.putExtra("title",listModel.get(position).getTitle());
//                                            i.putExtra("date",listModel.get(position).getChange_date());
//                                            i.putExtra("desc",listModel.get(position).getDescription());
//                                            startActivity(i);
//                                        }
//                                    });
                                }
                            }else{

                                popUpLogin();


//                    pop
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



    class DownloadFile extends AsyncTask<String, Integer, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Create progress dialog
            progressDialog = new ProgressDialog(getActivity());
            // Set your progress dialog Title
            progressDialog.setTitle("Updating Application");
            // Set your progress dialog Message
            progressDialog.setMessage("Please Wait!");
            progressDialog.setIndeterminate(false);
            progressDialog.setMax(100);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            // Show progress dialog
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... Url) {
            try {
                URL url = new URL(Url[0]);
                namaFile = Url[1];
                URLConnection connection = url.openConnection();
                connection.connect();

                // Detect the file lenghth
                int fileLength = connection.getContentLength();

                // Locate storage location
                String filepath = Environment.getExternalStorageDirectory().toString();
                Log.d("isi file path nya", filepath);

                // Download the file
                InputStream input = new BufferedInputStream(url.openStream());

                // Save the downloaded file
                OutputStream output = new FileOutputStream(filepath + "/Download/" + namaFile);

                byte data[] = new byte[1024];
                long total = 0;
                int count;
                while ((count = input.read(data)) != -1) {
                    total += count;
                    // Publish the progress
                    publishProgress((int) (total * 100 / fileLength));
                    output.write(data, 0, count);

                }
                // Close connection
                output.flush();
                output.close();
                input.close();

            } catch (Exception e) {
                // Error Log
                e.printStackTrace();
            }
            return null;
        }



        @Override
        protected void onProgressUpdate(Integer... progress) {
            super.onProgressUpdate(progress);
            // Update the progress dialog
            progressDialog.setCancelable(false);
            progressDialog.setIndeterminate(false);
            progressDialog.setMax(100);
            progressDialog.setProgress(progress[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            progressDialog.dismiss();
            if (result != null) {
                Toast.makeText(getActivity(), "Download error: " + result, Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getActivity(), "File downloaded", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(
                        Uri.fromFile(new File(Environment.getExternalStorageDirectory() + "/Download/" + "sigmabukber.apk")),
                        "application/vnd.android.package-archive");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        }
    }

    private void popUpLogin() {
        final Dialog dialog = new Dialog(getActivity());
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
                Intent i = new Intent(getActivity(), LoginActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
            }
        });
    }


}
