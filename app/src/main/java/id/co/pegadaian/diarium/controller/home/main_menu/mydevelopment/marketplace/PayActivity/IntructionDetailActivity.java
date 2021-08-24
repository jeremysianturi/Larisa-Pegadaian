package id.co.pegadaian.diarium.controller.home.main_menu.mydevelopment.marketplace.PayActivity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import id.co.pegadaian.diarium.R;

public class IntructionDetailActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_intruction_detail);



    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    getSupportActionBar().setTitle("Detail Instruction");


  }

  @Override
  public boolean onSupportNavigateUp() {
    finish();
    return true;
  }
}
