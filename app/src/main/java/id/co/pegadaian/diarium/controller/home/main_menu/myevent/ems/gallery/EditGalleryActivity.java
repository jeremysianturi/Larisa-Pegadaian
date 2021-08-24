package id.co.pegadaian.diarium.controller.home.main_menu.myevent.ems.gallery;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.jsibbold.zoomage.ZoomageView;

import id.co.pegadaian.diarium.R;
import id.co.pegadaian.diarium.util.UserSessionManager;

public class EditGalleryActivity extends AppCompatActivity {
    Button btnSave;
    EditText et_caption;

    private TextView txtDate, tvBy;
    private UserSessionManager session;
    private ZoomageView demoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_gallery);
        btnSave = (Button) findViewById(R.id.btnSave);
        et_caption = (EditText) findViewById(R.id.et_capt);
        demoView = findViewById(R.id.demoView);
        txtDate = findViewById(R.id.txt_gall_date);
        tvBy = findViewById(R.id.tvBy);
        session = new UserSessionManager(getApplicationContext());

        Intent intent = getIntent();
        final String image_id = intent.getStringExtra("image_id");
        final String title = intent.getStringExtra("title");
        final String description = intent.getStringExtra("description");
        final String image = intent.getStringExtra("image");
        final String change_date = intent.getStringExtra("change_date");
        et_caption.setText(description);
//        btnSave.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String caption = et_caption.getText().toString();
//                saveCapt(caption, id_foto);
//            }
//        });

//        downloadImage(getApplicationContext(), link, demoView);
//        String tahun_start = dates.substring(0,4);
//        String bulan_start = dates.substring(5,7);
//        String hari_start = dates.substring(8,11);
//        String val_bulan_start = null;
//        switch (bulan_start) {
//            case "01":
//                val_bulan_start = "Jan";
//                break;
//            case "02":
//                val_bulan_start = "Feb";
//                break;
//            case "03":
//                val_bulan_start = "Mar";
//                break;
//            case "04":
//                val_bulan_start = "Apr";
//                break;
//            case "05":
//                val_bulan_start = "Mar";
//                break;
//            case "06":
//                val_bulan_start = "Jun";
//                break;
//            case "07":
//                val_bulan_start = "Jul";
//                break;
//            case "08":
//                val_bulan_start = "Agu";
//                break;
//            case "09":
//                val_bulan_start = "Sep";
//                break;
//            case "10":
//                val_bulan_start = "Okt";
//                break;
//            case "11":
//                val_bulan_start = "Nov";
//                break;
//            case "12":
//                val_bulan_start = "Des";
//                break;
//        }
//        String tanggal_hasil_start = hari_start+" "+val_bulan_start+" "+tahun_start;
        txtDate.setText("Taken at : "+change_date);
        tvBy.setText("Title : "+title);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

//        ActionBar actionBar = getSupportActionBar();
//        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor(session.getEventColoRPrimary())));
//        actionBar.setTitle("Edit Caption");
//        actionBar.setHomeAsUpIndicator(R.drawable.back_panah);
    }

//    private void downloadImage(Context context, String url, ImageView image) {
//        Picasso.with(context)
//                .load(url)
//                .error(R.drawable.icon_avatars)
//                .into(image);
//    }

//    private void saveCapt(String userid, String gambarid) {
//        AndroidNetworking.get(ConstantUtils.URL.EDIT_CAPTION + "{caption}"+"/"+"{gambarid}")
//                .addPathParameter("caption", userid)
//                .addPathParameter("gambarid", gambarid)
//                .setTag("image")
//                .setPriority(Priority.LOW)
//                .build()
//                .getAsJSONObject(new JSONObjectRequestListener() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        System.out.println("djndwj"+response);
//                        try {
//                            String code = response.getString(ConstantUtils.DELETE_IMAGE.CODE);
//                            if (code.equals("T")) {
//                                Toast.makeText(EditGalleryActivity.this, "Edit succes", Toast.LENGTH_SHORT).show();
//                                Intent intent = new Intent(getApplicationContext(), GalleryActivity.class);
//                                startActivity(intent);
//                            } else {
//                                Toast.makeText(EditGalleryActivity.this, "Edit failed", Toast.LENGTH_SHORT).show();
//                                finish();
//                            }
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//
//                    @Override
//                    public void onError(ANError anError) {
//                        System.out.println("bahasa2 ");
//                    }
//                });
//    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
