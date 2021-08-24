package id.co.pegadaian.diarium.controller.home.main_menu.mydevelopment.marketplace.PayActivity.settting;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.alimuzaffar.lib.pin.PinEntryEditText;

import id.co.pegadaian.diarium.R;

public class ForgotPinActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_forgot_pin);


    PinEntryEditText pin = findViewById(R.id.pin);
    if (pin != null) {
      pin.setOnPinEnteredListener(new PinEntryEditText.OnPinEnteredListener() {
        @Override
        public void onPinEntered(CharSequence str) {
          if (str.toString().equals("123456")) {
            Toast.makeText(ForgotPinActivity.this, "SUCCESS", Toast.LENGTH_SHORT).show();
          } else {
            Toast.makeText(ForgotPinActivity.this, "FAIL", Toast.LENGTH_SHORT).show();
            pin.setText(null);
          }
        }
      });
    }

    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    getSupportActionBar().setTitle("Forgot Pin");
  }

  @Override
  public boolean onSupportNavigateUp() {
    finish();
    return true;
  }
}
