package id.co.pegadaian.diarium.controller.home.main_menu.mydevelopment.marketplace.PayActivity.settting;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import id.co.pegadaian.diarium.R;

public class PaymentSettingActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_payment_setting);



    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    getSupportActionBar().setTitle(" Payment Method");
  }

  @Override
  public boolean onSupportNavigateUp() {
    finish();
    return true;
  }
}
