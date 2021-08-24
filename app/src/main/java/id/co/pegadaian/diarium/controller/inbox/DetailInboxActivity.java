package id.co.pegadaian.diarium.controller.inbox;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import id.co.pegadaian.diarium.R;

public class DetailInboxActivity extends AppCompatActivity {
    TextView tvTitle, tvDate, tvDesc;
    Typeface font,fontbold;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_inbox);
        font = Typeface.createFromAsset(getApplication().getAssets(),"fonts/Nexa Light.otf");
        fontbold = Typeface.createFromAsset(getApplication().getAssets(),"fonts/Nexa Bold.otf");
        tvTitle = findViewById(R.id.tvTitle);
        tvDate = findViewById(R.id.tvDate);
        tvDesc = findViewById(R.id.tvDesc);
        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        String date = intent.getStringExtra("date");
        String desc = intent.getStringExtra("desc");
        tvTitle.setText(title);
        tvDate.setText(covertTimeToText(date));
        tvDesc.setText(desc);
        getSupportActionBar().setTitle(title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public String covertTimeToText(String dataDate) {

        String convTime = null;

        String prefix = "";
        String suffix = "Ago";

        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            Date pasTime = dateFormat.parse(dataDate);

            Date nowTime = new Date();

            long dateDiff = nowTime.getTime() - pasTime.getTime();

            long second = TimeUnit.MILLISECONDS.toSeconds(dateDiff);
            long minute = TimeUnit.MILLISECONDS.toMinutes(dateDiff);
            long hour   = TimeUnit.MILLISECONDS.toHours(dateDiff);
            long day  = TimeUnit.MILLISECONDS.toDays(dateDiff);

            if (second < 60) {
                convTime = second+" Seconds "+suffix;
            } else if (minute < 60) {
                convTime = minute+" Minutes "+suffix;
            } else if (hour < 24) {
                convTime = hour+" Hours "+suffix;
            } else if (day >= 7) {
                if (day > 30) {
                    convTime = (day / 30)+" Months "+suffix;
                } else if (day > 360) {
                    convTime = (day / 360)+" Years "+suffix;
                } else {
                    convTime = (day / 7) + " Week "+suffix;
                }
            } else if (day < 7) {
                convTime = day+" Days "+suffix;
            }

        } catch (ParseException e) {
            e.printStackTrace();
            Log.e("ConvTimeE", e.getMessage());
        }
        return convTime;
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
}
