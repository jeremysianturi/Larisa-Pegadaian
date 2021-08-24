package id.co.pegadaian.diarium.controller.home.main_menu.community;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import id.co.pegadaian.diarium.R;
import id.co.pegadaian.diarium.util.TimeHelper;

public class DetailCommunityContent extends AppCompatActivity {
    ImageView ivProfile;
    TextView tvName, tvTitle, tvTime, tvDesc;
    TimeHelper timeHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_community_content);
        Intent intent = getIntent();
        timeHelper = new TimeHelper();

        String name = intent.getStringExtra("name");
        String title = intent.getStringExtra("title");
        String desc = intent.getStringExtra("desc");
        String date = intent.getStringExtra("begin_date");
        String ava = intent.getStringExtra("ava");

        tvName = findViewById(R.id.tvName);
        tvTitle = findViewById(R.id.tvTitle);
        tvTime = findViewById(R.id.tvTime);
        tvDesc = findViewById(R.id.tvDesc);
        ivProfile = findViewById(R.id.ivProfile);

        tvName.setText(name);
        tvTitle.setText(title);
        tvTime.setText(timeHelper.getElapsedTime(date));
        tvDesc.setText(desc);
        if (ava.isEmpty()) {
            ivProfile.setImageResource(R.drawable.profile);
        } else{
            Picasso.get().load(ava).error(R.drawable.profile).into(ivProfile);
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Detail Content");
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
}
