package id.co.pegadaian.diarium.controller.home.main_menu.presence;

import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import id.co.pegadaian.diarium.R;

public class PresenceActivity extends AppCompatActivity {


    Typeface font,fontbold;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_presence);
        font = Typeface.createFromAsset(getAssets(), "fonts/Nexa Light.otf");
        fontbold = Typeface.createFromAsset(getAssets(), "fonts/Nexa Bold.otf");

        TextView txtTitle = findViewById(R.id.presence);
        txtTitle.setTypeface(fontbold);
        TextView name = findViewById(R.id.tvTitle);
        name.setTypeface(fontbold);
        TextView niik = findViewById(R.id.nik);
        niik.setTypeface(fontbold);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
}
