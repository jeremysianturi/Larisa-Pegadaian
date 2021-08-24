package id.co.pegadaian.diarium.controller.home.main_menu.onehseet;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;
import java.util.Map;

import id.co.pegadaian.diarium.R;
import id.co.pegadaian.diarium.util.UserSessionManager;

public class    OneSheetActivity extends AppCompatActivity {
    WebView wb;
    UserSessionManager session;
    private class HelloWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            return false;
        }
    }
    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        session = new UserSessionManager(this);
        setContentView(R.layout.activity_one_sheet);
        wb= findViewById(R.id.webView1);
        String url = "https://diariumapp.digitalevent.id/onesheets?nik=10101010&buscd=1000";

        Map <String, String> extraHeaders = new HashMap<String, String>();
        extraHeaders.put("Authorization",session.getToken());
        extraHeaders.put("Content-Type","application/json");
        extraHeaders.put("Accept","application/json");

        wb.getSettings().setJavaScriptEnabled(true);
        wb.getSettings().setLoadWithOverviewMode(true);
        wb.getSettings().setUseWideViewPort(true);
        wb.getSettings().setPluginState(WebSettings.PluginState.ON);
        wb.setWebViewClient(new HelloWebViewClient());
        wb.loadUrl(url,extraHeaders);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("OneSheet");
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
