package id.co.pegadaian.diarium.controller.home.main_menu.mytime.checkin;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;

import id.co.pegadaian.diarium.R;
import id.co.pegadaian.diarium.util.UserSessionManager;

public class FormKesehatanActivity extends AppCompatActivity {

  private UserSessionManager session;
  private WebView wb;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_form_kesehatan);
    session = new UserSessionManager(this);

    wb = findViewById(R.id.webview_form_kesehatan);
    String url = "https://healthtracking.digitalevent.id/check";

    wb.getSettings().setJavaScriptEnabled(true);
    wb.getSettings().setLoadWithOverviewMode(true);
    wb.getSettings().setUseWideViewPort(true);
    wb.getSettings().setPluginState(WebSettings.PluginState.ON);
//    wb.setWebViewClient(new OneSheetActivity.HelloWebViewClient());
//    wb.loadUrl(url,extraHeaders);
    wb.loadUrl(url);

    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    getSupportActionBar().setTitle("Form Kesehatan");

  }
  @Override
  public boolean onSupportNavigateUp() {
    onBackPressed();
    return true;
  }
}
