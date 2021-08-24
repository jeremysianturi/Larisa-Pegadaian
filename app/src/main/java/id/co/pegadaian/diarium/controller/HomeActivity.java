package id.co.pegadaian.diarium.controller;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.DownloadManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.StrictMode;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.android.material.tabs.TabLayout;

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
import java.util.Calendar;
import java.util.Date;

import androidx.viewpager.widget.ViewPager;
import id.co.pegadaian.diarium.R;
import id.co.pegadaian.diarium.adapter.ViewPagerAdapter;
import id.co.pegadaian.diarium.controller.friend.FriendsFragment;
import id.co.pegadaian.diarium.controller.home.HomeFragment;
import id.co.pegadaian.diarium.controller.home.main_menu.MoreFragment;
import id.co.pegadaian.diarium.controller.inbox.InboxFragment;
import id.co.pegadaian.diarium.controller.login.LoginActivity;
import id.co.pegadaian.diarium.controller.notification.NotifikasiFragment;
import id.co.pegadaian.diarium.temp.MainActivity;
import id.co.pegadaian.diarium.util.AutoUpdaterNew;
import id.co.pegadaian.diarium.util.MyNotificationPublisher;
import id.co.pegadaian.diarium.util.UserSessionManager;
import id.co.pegadaian.diarium.util.element.ProgressDialogHelper;

public class HomeActivity extends AppCompatActivity {

  private boolean doubleBackToExitPressedOnce = false;
  private Fragment fragment;
  private FragmentManager fragmentManager;
  private ProgressDialogHelper progressDialogHelper;
  UserSessionManager session;
  private ProgressDialog progressDialog;

  // buat view pager
  ViewPagerAdapter viewPagerAdapter;
  ViewPager viewPager;
  TabLayout tabLayout;
  // buat view pager


  public static final String NOTIFICATION_CHANNEL_ID = "10001" ;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
//    setContentView(R.layout.activity_home);
    setContentView(R.layout.activity_home_with_view_pager);

    // buat view pager
    tabLayout = (TabLayout) findViewById(R.id.tabs_home);
    viewPager = (ViewPager) findViewById(R.id.viewpager_home);
    // buat view pager

    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
    StrictMode.setThreadPolicy(policy);

    StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
    StrictMode.setVmPolicy(builder.build());
//    atualizaApp = new AutoUpdater(this);

    progressDialogHelper = new ProgressDialogHelper();
    session = new UserSessionManager(HomeActivity.this);
    progressDialog = new ProgressDialog(this);

    //  AUTO UPDATE       BATAS SUCI BROOOOOOOOOOOO
//    AutoUpdaterNew au = new AutoUpdaterNew(this);
//    au.cekServer();
    //   AUTO UPDATE      Batas Suci BROOOOOOOOOOOO


    getStatCheckin();

    // token
    System.out.println("token value : " + session.getToken());

    // bottom navigation

    // buat view pager
    viewPagerAdapter = new ViewPagerAdapter(HomeActivity.this.getSupportFragmentManager());
    viewPagerAdapter.addFragments(new HomeFragment(),"Home");
    viewPagerAdapter.addFragments(new InboxFragment(),"Inbox");
    viewPagerAdapter.addFragments(new FriendsFragment(),"Friends");
    viewPagerAdapter.addFragments(new NotifikasiFragment(),"Notification");
    viewPagerAdapter.addFragments(new MoreFragment(),"More");
    viewPager.setAdapter(viewPagerAdapter);
    viewPager.setOffscreenPageLimit(5);
    tabLayout.setupWithViewPager(viewPager);
    setupTabLayout();   // set image icon on view pager

    // auto update
//    getVersion();

  }

  private void setupTabLayout () {
    tabLayout.getTabAt(0).setIcon(R.drawable.home_viewpager);
    tabLayout.getTabAt(1).setIcon(R.drawable.inbox_viewpager);
    tabLayout.getTabAt(2).setIcon(R.drawable.friend_viewpager);
    tabLayout.getTabAt(3).setIcon(R.drawable.notification_viewpager);
    tabLayout.getTabAt(4).setIcon(R.drawable.menu_viewpager);
  }



  @Override
  protected void onResume() {
    super.onResume();
    ConnectivityManager mgr = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
    NetworkInfo netInfo = mgr.getActiveNetworkInfo();

    if (netInfo != null) {
      if (netInfo.isConnected()) {
        System.out.println("internet gw kenceng coy");
      }else {
        Toast.makeText(this, "Connection problem", Toast.LENGTH_SHORT).show();
      }
    } else {
      Toast.makeText(this, "Connection problem", Toast.LENGTH_SHORT).show();
      //No internet
    }

    // auto update
//    getVersion();

    getStatCheckin();
  }


  private void getStatCheckin(){
    SimpleDateFormat tgl = new SimpleDateFormat("yyyy-MM-dd");
    String tRes = tgl.format(new Date());
    String urlGetStatCheckin = session.getServerURL()+"users/"+session.getUserNIK()+"/statuspresensi/"+session.getUserNIK()+"/buscd/"+session.getUserBusinessCode()+"/date/"+tRes;
    System.out.println("check url get stat checkin di home activity : " + urlGetStatCheckin);
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
                System.out.println("ebk2j3nj32eeeePresensi"+response);
                // do anything with response
                try {
                  if(response.getInt("status")==200){
                    JSONArray jsonArray = response.getJSONArray("data");
                    if (jsonArray.length()==0) {
                      session.setStat("CO");
                    }
                    for (int i=0; i<jsonArray.length(); i++) {
                      JSONObject obj = jsonArray.getJSONObject(i);
                      String stat = obj.getString("presence_type");
                      session.setStat(stat);
                    }
                    System.out.println(jsonArray.length()+" ql3jeeeeen2kelr");

                  }else{
                    Toast.makeText(HomeActivity.this,response.getString("message"),Toast.LENGTH_SHORT).show();

                    if (response.getInt("status")==401){
                      popUpLogin();
                    }
                  }
                  System.out.println("status ya : "+session.getStat());
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
    final Dialog dialog = new Dialog(HomeActivity.this);
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
        Intent i = new Intent(HomeActivity.this, LoginActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
      }
    });
  }


  @Override
  public void onBackPressed() {
    if (doubleBackToExitPressedOnce) {
      Intent a = new Intent(Intent.ACTION_MAIN);
      a.addCategory(Intent.CATEGORY_HOME);
      a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
      startActivity(a);
    }

    this.doubleBackToExitPressedOnce = true;
    Toast.makeText(this, "Klik lagi untuk keluar", Toast.LENGTH_SHORT).show();

    new Handler().postDelayed(new Runnable() {
      @Override
      public void run() {
        doubleBackToExitPressedOnce = false;
      }
    }, 2000);

  }



  // CHECKIN AUTOUPDATE
  private void getVersion(){
    AndroidNetworking.get(session.getServerURL()+"appversion/ANDROID/buscd/"+session.getUserBusinessCode())
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
                    PackageInfo pInfo = HomeActivity.this.getPackageManager().getPackageInfo(HomeActivity.this.getPackageName(), 0);
                    int version_aplikasi = pInfo.versionCode;

                    JSONArray jsonArray = response.getJSONArray("data");

                    for (int i=0;i<jsonArray.length();i++) {
                      JSONObject object = jsonArray.getJSONObject(i);

                      String version_db = object.getString("version_code");
                      String url = object.getString("version_link");
//                                    String type = object.getString("type");

                      System.out.println(version_aplikasi+"VERSIAPLIKASI");
                      System.out.println(version_db+"VERSIDB");

                      if (Integer.parseInt(version_db)>version_aplikasi) {
                        popupUpdate("playstore", url);
                      }
                    }
                  }else{
                    Toast.makeText(HomeActivity.this, "error to get update", Toast.LENGTH_SHORT).show();
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

  private void popupUpdate(String type, String url) {
    final Dialog dialog = new Dialog(HomeActivity.this);
    dialog.setContentView(R.layout.layout_popup_update);
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
        if (type.equals("playstore")) {
          Intent i = new Intent(Intent.ACTION_VIEW);
          i.setData(Uri.parse(url));
          startActivity(i);
        } else {
          System.out.println("masuk ke gagal update berarti ya di home activity");
        }
      }
    });
  }
  // CHECKIN AUTOUPDATE


}
