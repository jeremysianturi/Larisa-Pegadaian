package id.co.pegadaian.diarium.controller.home.main_menu.myevent.ems.news;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

import id.co.pegadaian.diarium.R;

public class DetailNewsActivity extends AppCompatActivity {
    String title, desc, image, date;
    TextView tvTitle, tvDesc, tvDate;
    ImageView ivNews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_news);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvDesc = (TextView) findViewById(R.id.tvDesc);
        tvDate = (TextView) findViewById(R.id.tvDate);
        ivNews = (ImageView) findViewById(R.id.ivNews);

        Intent intent = getIntent();
        title = intent.getStringExtra("title");
        desc = intent.getStringExtra("desc");
        image = intent.getStringExtra("image");
        date = intent.getStringExtra("date");

        tvTitle.setText(title);
        tvDesc.setText(desc);
        tvDate.setText(date);
        Picasso.get().load(image).error(R.drawable.placeholder_gallery).into(ivNews);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Detail News");
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
