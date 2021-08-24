package id.co.pegadaian.diarium.util;
/*
 * This application created by Muhammad Zaim Milzam
 * 20 January 2020
 */

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.StrictMode;
import android.text.Html;

import androidx.core.app.ActivityCompat;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONObject;

import java.io.File;

import id.co.pegadaian.diarium.BuildConfig;
import okhttp3.OkHttpClient;

public class AutoUpdaterNew {

  private Context context;
  private UserSessionManager session;
  private String title = "Diariuim.apk";
  OkHttpClient client;

  String name,tanngal,url,tanggal,notes;
  int versiNew;

  public AutoUpdaterNew(Context ctx){

    context = ctx;
    session = new UserSessionManager(context);
    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
    StrictMode.setThreadPolicy(policy);

    StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
    StrictMode.setVmPolicy(builder.build());

//
//    CustomTrust customTrust = null;
//    try {
//      customTrust = new CustomTrust(ctx);
//    } catch (IOException e) {
//      e.printStackTrace();
//    }
//    client = customTrust.getClient();
//    /* ===========  ssl Handle =========== */

  }


  public void getData(){
    System.out.println("URL_API "+session.getServerURL()+"appversion/ANDROID/buscd/"+session.getUserBusinessCode());
    try {
      AndroidNetworking.get(session.getServerURL()+"appversion/ANDROID/buscd/"+session.getUserBusinessCode())
              .addHeaders("Accept","application/json")
              .addHeaders("Content-Type","application/json")
              .addHeaders("Authorization",session.getToken())
              .setPriority(Priority.MEDIUM)
              .build()
              .getAsJSONObject(new JSONObjectRequestListener() {
                @Override
                public void onResponse(JSONObject response) {
                  // do anything with response
                  System.out.println("Response_AutoUpdater "+response);
                  try{

                    if(response.getInt("status")==200){

                      System.out.println("response_after_IF"+response);

//                      JSONArray jsonArray = response.getJSONArray("data");
//                      for (int i=0; i<jsonArray.length(); i++) {
//                        JSONObject a = jsonArray.getJSONObject(i);

                        String name = response.getJSONArray("data").getJSONObject(0).getString("version_name");
                        String tanggal = response.getJSONArray("data").getJSONObject(0).getString("date");
                        final String url = response.getJSONArray("data").getJSONObject(0).getString("version_link");
                        String notes = response.getJSONArray("data").getJSONObject(0).getString("version_desc");
                        int versiNew = Integer.parseInt(response.getJSONArray("data").getJSONObject(0).getString("version_code"));

//                        name = nam;
//                        tanggal= tangga;
//                        url = ur;
//                        notes = note;
//                        versiNew = versiNe;

                      System.out.println("url autoupdate :"+url);

                        System.out.println("CekVersiNew"+ versiNew+" "+name);

                      int versi = BuildConfig.VERSION_CODE;

//                      String versiSession = String.valueOf(versiNew);
                      session.setVersiServer(name);
                      System.out.println("sesssionversi " +session.getVersiServer());

//                      System.out.println("cekDataAuto "+notes);

  //                                session.setVersiShow(name);
                      //  JSONArray data = response.getJSONArray("surah");


                      System.out.println("cekVersionDiarium "+versi+"<"+versiNew);
                      if(versi<versiNew){
                        new AlertDialog.Builder(context)
                                .setTitle("Found new update v."+name)
                                .setMessage(Html.fromHtml(""
                                        +"Notes ("+tanggal.substring(0,10)
                                        +") : <br><br>" + notes))
                                .setPositiveButton("Update", new DialogInterface.OnClickListener() {
                                  @Override
                                  public void onClick(DialogInterface dialog, int which) {
                                    final AutoUpdaterNew update = new AutoUpdaterNew(context);
                                    //update.apkDownloader(resultData[3],resultData[1]);

                                    System.out.println("masuk ok");

                                    if (Build.VERSION.SDK_INT >= 23) {
                                      if (context.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                                        //Log.v(TAG,"Permission is granted");
                                        //apkDownloader(url,title);
                                        System.out.println("masuk apk dan urlnya : " +url);
//                                        update.apkDownloader(url,title);
                                        openWebURL(url);
                                      }else{
                                        System.out.println("masuk permission");
                                        // ActivityCompat.requestPermissions((Activity)context, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 101);

                                        if (ActivityCompat.shouldShowRequestPermissionRationale((Activity)context, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                        ) {
                                          showDialogOK("The Permissions are required for this application",
                                                  new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                      switch (which) {
                                                        case DialogInterface.BUTTON_POSITIVE:
                                                          // proceed with logic by disabling the related features or quit the app.
//                                                          update.apkDownloader(url,title);
                                                          openWebURL(url);
                                                          break;
                                                        case DialogInterface.BUTTON_NEGATIVE:
                                                          //checkAndRequestPermissions();
  //                                                                                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
  //                                                                                                Uri.fromParts("package", getPackageName(), null));
  //                                                                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
  //                                                                                        startActivity(intent);
  //                                                                                        break;
                                                      }
                                                    }
                                                  });
                                        }
                                      }

                                    }else{
                                      System.out.println("langsung update");
//                                      update.apkDownloader(url,title);
                                      openWebURL(url);
                                    }

                                  }

                                })
//                                .setNegativeButton("Later", new DialogInterface.OnClickListener() {
//
//                                  @Override
//                                  public void onClick(DialogInterface dialog, int which) {
//                                    //System.exit(1);
//                                  }
//
//                                })
                                .show();
                      }

                    }


                  }catch (Exception e){

                    System.out.println("gagal"+e);
                  }

                }
                @Override
                public void onError(ANError error) {
                  // handle error
                  System.out.println(error);
                }
              });
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void showDialogOK(String message, DialogInterface.OnClickListener okListener) {
    new AlertDialog.Builder(context)
            .setMessage(message)
            .setPositiveButton("OK", okListener)
            .setNegativeButton("SETTINGS", okListener)
            .create()
            .show();
  }

  public void openWebURL( String inURL ) {
    Intent browse = new Intent( Intent.ACTION_VIEW , Uri.parse( inURL ) );
    (context).startActivity(browse);
  }


  public void apkDownloader(String url, String title){

//    ProgressDialog pd = new ProgressDialog(context);
//    pd.setMessage("Tunggu ya Bro/Sis :) lagi Download ...");
//    pd.setTitle("Diarium Super Apps");
//    pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//    pd.setIndeterminate(false);
//    pd.setCancelable(false);
//    pd.show();

    final ProgressDialog pd= ProgressDialog.show(context, "Go to play store ", "Download..", false, false);
    String destination = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/";
    System.out.println(destination+"DestinationANdroidCuy");
    // System.out.println();
//        String fileName = url.split("/")[url.split("/").length-1];
    String fileName = "diarium.apk";

    System.out.println(fileName);

    destination += fileName;
    final Uri uri = Uri.parse("file://" + destination);

    System.out.println(uri + "msadlasmdlasmldmas");

    //Delete update file if exists
    File file = new File(destination);
    if (file.exists()){
      file.delete();//file.delete() - test this, I think sometimes it doesnt work
    }

    System.out.println(url);

    //get url of app on server
    //String url = url;

    //set downloadmanager
    DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
    request.setDescription(title);
    request.setTitle(title);

    //set destination
    request.setDestinationUri(uri);

    // get download service and enqueue file
    final DownloadManager manager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
    final long downloadId = manager.enqueue(request);
    System.out.println("masuk1 ");
    //set BroadcastReceiver to install app when .apk is downloaded
    BroadcastReceiver onComplete = new BroadcastReceiver() {
      public void onReceive(Context ctxt, Intent intent) {
        pd.dismiss();
        Intent install = new Intent(Intent.ACTION_VIEW);
        install.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        install.setDataAndType(uri,"application/vnd.android.package-archive");
        manager.getMimeTypeForDownloadedFile(downloadId);
        (context).startActivity(install);

        context.unregisterReceiver(this);
        //(Activity)context.;
      }
    };
    //register receiver for when .apk download is compete
    context.registerReceiver(onComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
  }

  public void cekServer(){
    getData();
  }

}
