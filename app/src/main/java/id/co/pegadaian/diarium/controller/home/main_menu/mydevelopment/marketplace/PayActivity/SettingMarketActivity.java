package id.co.pegadaian.diarium.controller.home.main_menu.mydevelopment.marketplace.PayActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import id.co.pegadaian.diarium.R;
import id.co.pegadaian.diarium.controller.home.main_menu.mydevelopment.marketplace.PayActivity.settting.ChangePinActivity;
import id.co.pegadaian.diarium.controller.home.main_menu.mydevelopment.marketplace.PayActivity.settting.ForgotPinActivity;
import id.co.pegadaian.diarium.controller.home.main_menu.mydevelopment.marketplace.PayActivity.settting.PaymentSettingActivity;

public class SettingMarketActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_seeting_market);


    LinearLayout changepin = findViewById(R.id.layout_changepin);
    changepin.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent i = new Intent(SettingMarketActivity.this, ChangePinActivity.class);
        startActivity(i);
      }
    });


    LinearLayout forgotpin = findViewById(R.id.layout_forgotpin);
    forgotpin.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent i = new Intent(SettingMarketActivity.this, ForgotPinActivity.class);
        startActivity(i);
      }
    });

    LinearLayout payment = findViewById(R.id.layout_paymentmethod);
    payment.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent i = new Intent(SettingMarketActivity.this, PaymentSettingActivity.class);
        startActivity(i);
      }
    });


    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    getSupportActionBar().setTitle("Setting");
  }

  @Override
  public boolean onSupportNavigateUp() {
    finish();
    return true;
  }
}
