package id.co.pegadaian.diarium.controller.home.main_menu.myevent.ems.content;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import id.co.pegadaian.diarium.R;
import id.co.pegadaian.diarium.util.UserSessionManager;

public class DetailContentActivity extends AppCompatActivity {
    TextView tvTitle, tvDesc;
    Button btnDownload;
//    private PDFView pdfView;
    UserSessionManager session;

    WebView wv;
    class WebViewClass extends WebViewClient {
        final  WebView wv;

        WebViewClass(WebView webView) {
            this.wv = webView;
        }

        public void onPageFinished(WebView view, String url) {
            wv.setVisibility(View.VISIBLE);
            view.clearCache(true);
        }

        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            wv.loadData("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>" + "<center>" + "error"+ ".</center>", "text/html", "UTF-8");
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_content);
        session = new UserSessionManager(this);

        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        String description = intent.getStringExtra("description");
        String file_content = intent.getStringExtra("file_content");
        tvTitle = findViewById(R.id.tvTitle);
        tvDesc = findViewById(R.id.tvDate);
//        pdfView = findViewById(R.id.pdfView);

        tvTitle.setText(title);
        tvDesc.setText(description);
        wv=(WebView)findViewById(R.id.webView1);
        wv.setWebViewClient(new WebViewClient());
        wv.getSettings().setLoadsImagesAutomatically(true);
        wv.getSettings().setJavaScriptEnabled(true);
        wv.setScrollBarStyle(View.VISIBLE);
        wv.getSettings().setBuiltInZoomControls(true);
        wv.getSettings().setSupportZoom(true);
        wv.getSettings().setLoadWithOverviewMode(true);
        wv.getSettings().setUseWideViewPort(true);
        wv.getSettings().setAllowContentAccess(true);
        wv.loadUrl(file_content);
        wv.setVisibility(View.INVISIBLE);
        wv.setWebViewClient(new WebViewClass(wv));
//        btnDownload.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                DownloadFile(file_content);
//            }
//        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Detail Content");
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

//    class RetrievePDFStream extends AsyncTask<String, Void, InputStream> {
//
//        @Override
//        protected InputStream doInBackground(String... strings) {
//            InputStream inputStream = null;
//            try {
//                URL url = new URL(strings[0]);
//                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
//                if (urlConnection.getResponseCode() == 200) {
//                    inputStream = new BufferedInputStream(urlConnection.getInputStream());
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            return inputStream;
//        }
//
//        @Override
//        protected void onPostExecute(InputStream inputStream) {
//            pdfView.fromStream(inputStream).load();
//        }
//    }

//    private void DownloadFile(String linkDownload) {
//        Toast.makeText(getApplicationContext(), "Downloading file..", Toast.LENGTH_SHORT).show();
//        String destination = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/";
//        final String fileName = linkDownload.split("/")[linkDownload.split("/").length - 1];
//
//        destination += fileName;
//        final Uri uri = Uri.parse("file://" + destination);
//
//
//        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(linkDownload));
//        request.setDescription("Download " + fileName);
//        request.setTitle(fileName);
//
//        //set destination
//        request.setDestinationUri(uri);
//
//        // get download service and enqueue file
//        final DownloadManager manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
//        final long downloadId = manager.enqueue(request);
//
//        BroadcastReceiver onComplete = new BroadcastReceiver() {
//            public void onReceive(Context ctxt, Intent intent) {
//
//                Toast.makeText(getApplicationContext(), "File downloaded", Toast.LENGTH_SHORT).show();
//                NotificationCompat.Builder mBuilder =
//                        new NotificationCompat.Builder(getApplicationContext())
//                                .setSmallIcon(R.mipmap.ic_launcher)
//                                .setContentTitle(fileName)
//                                .setContentText("Download completed");
//
//
//                NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//                notificationManager.notify(682736, mBuilder.build());
//            }
//        };
//        //register receiver for when .apk download is compete
//        registerReceiver(onComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
//    }
}
