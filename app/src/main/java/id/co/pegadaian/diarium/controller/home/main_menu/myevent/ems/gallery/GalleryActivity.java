package id.co.pegadaian.diarium.controller.home.main_menu.myevent.ems.gallery;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import id.co.pegadaian.diarium.R;
import id.co.pegadaian.diarium.adapter.GalleryAdapter;
import id.co.pegadaian.diarium.controller.home.main_menu.myevent.ems.content.ContentActivity;
import id.co.pegadaian.diarium.model.GalleryModel;
import id.co.pegadaian.diarium.util.UserSessionManager;
import id.co.pegadaian.diarium.util.element.ProgressDialogHelper;

public class GalleryActivity extends AppCompatActivity {
    private static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;
    private static final int CAPTURE_PHOTO = 2;
    private static final int CAPTURE_GALLERY = 3;
    private TextView textView;
    private GridView gridView;
    private GalleryModel model;
    private List<GalleryModel> modelList;
    private GalleryAdapter adapter;
    private UserSessionManager session;
    private ProgressDialogHelper progressDialogHelper;
    private Button button;
    TextView tvNull;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallry);

        progressDialogHelper = new ProgressDialogHelper();

        tvNull = (TextView) findViewById(R.id.tvNull);
        gridView = findViewById(R.id.gv_gallery);
        button = findViewById(R.id.btn_photo);
        session = new UserSessionManager(getApplicationContext());

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(getApplicationContext(), TakePhotoActivity.class);
//                startActivity(intent);

                selectAction();
            }
        });

        getGallery(session.getEventId());

//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);

//        ActionBar actionBar = getSupportActionBar();
//        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor(session.getEventColoRPrimary())));
//        actionBar.setTitle("Gallery");
//        actionBar.setHomeAsUpIndicator(R.drawable.back_panah);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Gallery");
    }


    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    private void selectAction() {
        final CharSequence[] items = {"Capture Photo", "Choose from Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(GalleryActivity.this);
        builder.setTitle("Select Action");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Capture Photo")) {
                    Intent intent = new Intent(getApplicationContext(), TakePhotoActivity.class);
                    intent.putExtra("flag", "1");
                    startActivity(intent);
                    GalleryActivity.this.finish();
                } else if (items[item].equals("Choose from Gallery")) {
                    Intent intent = new Intent(getApplicationContext(), TakePhotoActivity.class);
                    intent.putExtra("flag", "2");
                    startActivity(intent);
                    GalleryActivity.this.finish();
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }


    private void getGallery(String event_id){
        System.out.println("api get list galery: " + session.getServerURL()+"users/eventEMS/"+event_id+"/gallery/buscd/"+session.getUserBusinessCode());
        progressDialogHelper.showProgressDialog(GalleryActivity.this, "Getting data...");
        AndroidNetworking.get(session.getServerURL()+"users/eventEMS/"+event_id+"/gallery/buscd/"+session.getUserBusinessCode())
                .addHeaders("Accept","application/json")
                .addHeaders("Content-Type","application/json")
                .addHeaders("Authorization",session.getToken())
                //.addJSONObjectBody(body)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // do anything with response
                        System.out.println(response+"awdede");
                        try {
                            if(response.getInt("status")==200){
//                                session.setToken(response.getString("token"));
                                modelList = new ArrayList<GalleryModel>();
                                JSONArray jsonArray = response.getJSONArray("data");
                                if (jsonArray.length()==0) {
                                    tvNull.setVisibility(View.VISIBLE);
                                    gridView.setVisibility(View.GONE);
                                } else {
                                    tvNull.setVisibility(View.GONE);
                                    gridView.setVisibility(View.VISIBLE);
                                    for (int a = 0; a < jsonArray.length(); a++) {
                                        JSONObject object = jsonArray.getJSONObject(a);
                                        String begin_date = object.getString("begin_date");
                                        String end_date = object.getString("end_date");
                                        String business_code = object.getString("business_code");
                                        String personal_number = object.getString("personal_number");
                                        String event_id = object.getString("event_id");
                                        String image_id = object.getString("image_id");
                                        String title = object.getString("title");
                                        String description = object.getString("description");
                                        String image = object.getString("image");
                                        String change_date = object.getString("change_date");
                                        String change_user = object.getString("change_user");
                                        System.out.println(begin_date+"dl2j3nr3");

                                        System.out.println("test string image my event: " + image);
                                        model = new GalleryModel(begin_date, end_date, business_code, personal_number, event_id, image_id, title, description, image, change_date, change_user);
                                        modelList.add(model);
                                    }
                                    adapter = new GalleryAdapter(GalleryActivity.this, modelList);
                                    gridView.setAdapter(adapter);
                                }
                                gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                        Intent intent = new Intent(getApplicationContext(), GalleryDetailActivity.class);
                                        intent.putExtra("title", modelList.get(position).getTitle());
                                        intent.putExtra("description", modelList.get(position).getDescription());
                                        intent.putExtra("image", modelList.get(position).getImage());
                                        intent.putExtra("begin_date", modelList.get(position).getBegin_date());
                                        intent.putExtra("personal_number", modelList.get(position).getPersonal_number());
                                        startActivity(intent);
                                    }
                                });
                                progressDialogHelper.dismissProgressDialog(GalleryActivity.this);
                            }else{
//                                popUpLogin();
                                progressDialogHelper.dismissProgressDialog(GalleryActivity.this);
                            }
                        }catch (Exception e){
                            progressDialogHelper.dismissProgressDialog(GalleryActivity.this);
                            System.out.println(e);
                        }

                    }
                    @Override
                    public void onError(ANError error) {
                        progressDialogHelper.dismissProgressDialog(GalleryActivity.this);
                        System.out.println(error);
                    }
                });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getGallery(session.getEventId());
    }
}