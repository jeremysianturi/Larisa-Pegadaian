package id.co.pegadaian.diarium.controller.home.main_menu.myevent.ems.gallery;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.jsibbold.zoomage.ZoomageView;
import com.squareup.picasso.Picasso;

import id.co.pegadaian.diarium.R;
import id.co.pegadaian.diarium.util.UserSessionManager;

public class GalleryDetailActivity extends AppCompatActivity {

    private ImageView imageView;
    private TextView tvTitle, tvDesc, tvDate, tvName;
//    private Button btnEdit, btnDelete;
    private UserSessionManager session;
    private ZoomageView ivGallery;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery_detail);

        session = new UserSessionManager(getApplicationContext());

        ivGallery = findViewById(R.id.ivGallery);
        tvTitle = findViewById(R.id.tvTitle);
        tvDesc = findViewById(R.id.tvDesc);
        tvDate = findViewById(R.id.tvDate);
        tvName = findViewById(R.id.tvName);

        Intent intent = getIntent();
        final String title = intent.getStringExtra("title");
        final String desc = intent.getStringExtra("description");
        final String image = intent.getStringExtra("image");
        final String begin_date = intent.getStringExtra("begin_date");
        final String personal_number = intent.getStringExtra("personal_number");

        tvTitle.setText(title);
        tvDate.setText("taken at : "+begin_date);
        tvName.setText("posted by : "+personal_number);
        tvDesc.setText(desc);
        Picasso.get().load(image).error(R.drawable.placeholder_gallery).into(ivGallery);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Detail Gallery");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //NavUtils.navigateUpFromSameTask(this);
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

//    private void downloadImage(Context context, String url, ImageView image) {
//        Picasso.with(context)
//                .load(url)
//                .error(R.drawable.icon_avatars)
//                .into(image);
//    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
