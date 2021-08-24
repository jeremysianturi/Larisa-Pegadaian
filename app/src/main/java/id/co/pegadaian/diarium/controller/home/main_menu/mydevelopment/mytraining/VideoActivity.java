package id.co.pegadaian.diarium.controller.home.main_menu.mydevelopment.mytraining;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import id.co.pegadaian.diarium.R;
import id.co.pegadaian.diarium.util.UserSessionManager;

public class VideoActivity extends AppCompatActivity {
    WebView wb;
    UserSessionManager session;
    private class HelloWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            return false;
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        Intent intent = getIntent();
        String url = intent.getStringExtra("url");
        session = new UserSessionManager(this);
        wb= findViewById(R.id.webView1);
        wb.getSettings().setJavaScriptEnabled(true);
        wb.getSettings().setLoadWithOverviewMode(true);
        wb.getSettings().setUseWideViewPort(true);
        wb.getSettings().setPluginState(WebSettings.PluginState.ON);
        wb.setWebViewClient(new VideoActivity.HelloWebViewClient());
        wb.loadUrl(url);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Video");
    }
    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
}
