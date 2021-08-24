package id.co.pegadaian.diarium.controller.home.main_menu.community;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import id.co.pegadaian.diarium.R;

public class AcceptCommunityActivity extends AppCompatActivity {


    Typeface font,fontbold;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accept_community);

        font = Typeface.createFromAsset(getApplication().getAssets(),"fonts/Nexa Light.otf");
        fontbold = Typeface.createFromAsset(getApplication().getAssets(),"fonts/Nexa Bold.otf");

        TextView a =  findViewById(R.id.approve);
        a.setTypeface(fontbold);
        TextView b =  findViewById(R.id.decline);
        b.setTypeface(fontbold);
        TextView c =  findViewById(R.id.nama_komunitas);
        c.setTypeface(fontbold);

        LinearLayout community = (LinearLayout) findViewById(R.id.accept);
        community.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent message_community = new Intent(AcceptCommunityActivity.this ,CommunityMessageActivity.class);
                startActivity(message_community);
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
}
