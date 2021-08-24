package id.co.pegadaian.diarium.controller.profile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import id.co.pegadaian.diarium.R;

public class FullFotoActivity extends AppCompatActivity {

    ImageView ivProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_full_foto);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");

        ivProfile = findViewById(R.id.ivProfile);


        Intent intent = getIntent();
        if (intent.getStringExtra("from").equals("home fragment")){
            String banner = intent.getStringExtra("banner_image");

            try {
                Picasso.get().load(banner).error(R.drawable.profile).into(ivProfile);
                System.out.println("banner clicked : " + banner);
            } catch (Exception e){
                Picasso.get().load(R.drawable.profile).into(ivProfile);
            }

        } else if (intent.getStringExtra("from").equals("Profile activity")) {
            String avatar = intent.getStringExtra("avatar");
//            Picasso.get().load(avatar).error(R.drawable.profile).into(ivProfile);

            try {
                Picasso.get().load(avatar).error(R.drawable.profile).into(ivProfile);
                System.out.println("avatar user : " + avatar);
            } catch (Exception e){
                Picasso.get().load(R.drawable.profile).into(ivProfile);
            }

        }

    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
