//package id.co.telkomsigma.Diarium.util;
//
//import android.app.DownloadManager;
//import android.app.ProgressDialog;
//import android.content.BroadcastReceiver;
//import android.content.Context;
//import android.content.Intent;
//import android.content.IntentFilter;
//import android.net.Uri;
//import android.os.Environment;
//import android.os.StrictMode;
//import android.widget.Toast;
//
//import java.io.File;
//
//
///**
// * Created by Aang on 16-Nov-16.
// * Dibuat oleh : Gilang Rahman
// * Kelas ini berisi modul - modul untuk fungsi auto update pada aplikasi.
// */
//
//
//public class AutoUpdater {
//
//    private Context context;
//
//    public AutoUpdater(Context ctx){
//        context = ctx;
//
//        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
//        StrictMode.setThreadPolicy(policy);
//        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
//        StrictMode.setVmPolicy(builder.build());
//    }
//
//
//    public void apkDownloader(String url, String title){
////        ProgressDialog pd = new ProgressDialog(context, ProgressDialog.THEME_HOLO_DARK);
////// set indeterminate style
////        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
////// set title and message
////        pd.setTitle("Please wait");
////        pd.setMessage("Loading downloading file...");
////        pd.show();
//
//        final ProgressDialog pd= ProgressDialog.show(context, "Please Wait..", "Downloading data", false, false);
//        String destination = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/";
//        System.out.println(destination+"DestinationANdroidCuy");
//        // System.out.println();
////        String fileName = url.split("/")[url.split("/").length-1];
//        String fileName = "diarium.apk";
//
//        System.out.println(fileName);
//
//        destination += fileName;
//        final Uri uri = Uri.parse("file://" + destination);
//
//        System.out.println(uri + "msadlasmdlasmldmas");
//
//        //Delete update file if exists
//        File file = new File(destination);
//        if (file.exists()){
//            file.delete();//file.delete() - test this, I think sometimes it doesnt work
//        }
//
//        System.out.println(url);
//
//        //get url of app on server
//        //String url = url;
//
//        //set downloadmanager
//        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
//        request.setDescription(title);
//        request.setTitle(title);
//
//        //set destination
//        request.setDestinationUri(uri);
//
//        // get download service and enqueue file
//        final DownloadManager manager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
//        final long downloadId = manager.enqueue(request);
//        System.out.println("masuk1 ");
//        //set BroadcastReceiver to install app when .apk is downloaded
//        BroadcastReceiver onComplete = new BroadcastReceiver() {
//            public void onReceive(Context ctxt, Intent intent) {
//                pd.dismiss();
//                Intent install = new Intent(Intent.ACTION_VIEW);
//                install.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                install.setDataAndType(uri,"application/vnd.android.package-archive");
//                manager.getMimeTypeForDownloadedFile(downloadId);
//                (context).startActivity(install);
//
//                context.unregisterReceiver(this);
//                //(Activity)context.;
//            }
//        };
//        //register receiver for when .apk download is compete
//        context.registerReceiver(onComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
//    }
//
//
//
//}
