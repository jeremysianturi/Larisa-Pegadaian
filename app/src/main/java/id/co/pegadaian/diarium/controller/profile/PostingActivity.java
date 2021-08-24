package id.co.pegadaian.diarium.controller.profile;

import android.os.Bundle;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import id.co.pegadaian.diarium.R;
import id.co.pegadaian.diarium.adapter.UpdateAdapter;

public class PostingActivity extends AppCompatActivity {


    String[] nama ={
            "Iriana Indrawari",
            "Iriana Indrawari",
            "Iriana Indrawari",
    };
    String[] jabatan ={
            "Head Of ICT Solutions",
            "Head Of ICT Solutions",
            "Head Of ICT Solutions",
    };
    String[] day ={
            "2 days ago",
            "3 days ago",
            "4 days ago",
    };
    String[] konten ={
            "Melakukan kunjungan ke Bank BRI",
            "Sharing Session Presales",
            "Melakukan pengujian aplikasi",
    };
    String[] like={
            "5 Likes",
            "10 Likes",
            "2 Likes",
    };
    String[] komen ={
            "0 Comments",
            "2 Comments",
            "3 COmments",
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posting);

        ListView list = findViewById(R.id.list_post);

        UpdateAdapter gridAdapter = new UpdateAdapter(PostingActivity.this,nama,jabatan,day,konten,like,komen);
        list.setAdapter(gridAdapter);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

}
