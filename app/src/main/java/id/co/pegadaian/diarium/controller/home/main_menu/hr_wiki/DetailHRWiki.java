package id.co.pegadaian.diarium.controller.home.main_menu.hr_wiki;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;

import id.co.pegadaian.diarium.R;

public class DetailHRWiki extends AppCompatActivity {
    TextView tvKasus, tvDate, tvSolusi;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_hrwiki);
        Intent intent = getIntent();
        String kasus = intent.getStringExtra("kasus");
        String date = intent.getStringExtra("date");
        String solusi = intent.getStringExtra("solusi");
        tvKasus = findViewById(R.id.tvKasus);
        tvDate = findViewById(R.id.tvDate);
        tvSolusi = findViewById(R.id.tvSolusi);
        tvKasus.setText(kasus);
        tvDate.setText(date);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            tvSolusi.setText(Html.fromHtml(solusi.replace("\\r\\n\\r\\n\t",System.getProperty("line.separator")).replace("\\r\\n\\r\\n",System.getProperty("line.separator")), Html.FROM_HTML_MODE_COMPACT));
        } else {
            tvSolusi.setText(Html.fromHtml(solusi.replace("\\r\\n\\r\\n\t",System.getProperty("line.separator")).replace("\\r\\n\\r\\n",System.getProperty("line.separator"))));
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Detail");
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
