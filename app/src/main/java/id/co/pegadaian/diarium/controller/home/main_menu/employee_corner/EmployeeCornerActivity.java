package id.co.pegadaian.diarium.controller.home.main_menu.employee_corner;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import id.co.pegadaian.diarium.R;
import id.co.pegadaian.diarium.adapter.EmployeeCornerThemeAdapter;
import id.co.pegadaian.diarium.model.EmployeeCornerThemeModel;
import id.co.pegadaian.diarium.util.UserSessionManager;
import id.co.pegadaian.diarium.util.element.ProgressDialogHelper;

public class EmployeeCornerActivity extends AppCompatActivity {

    String[] nama_corner={
            "Sports Corner",
            "Music Corner",
            "Consultation Corner",
    };
    int[] images = {R.drawable.badminton, R.drawable.band, R.drawable.medical};
    UserSessionManager session;
    private List<EmployeeCornerThemeModel> listModel;
    private EmployeeCornerThemeModel model;
    private EmployeeCornerThemeAdapter adapter;
    private ListView listCorner;
    private TextView tvNull;
    Typeface font,fontbold;
    private ProgressDialogHelper progressDialogHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_corner);
        session = new UserSessionManager(this);
        progressDialogHelper = new ProgressDialogHelper();
        listCorner = findViewById(R.id.list_corner);
        tvNull = (TextView) findViewById(R.id.tvNull);
        font = Typeface.createFromAsset(getApplication().getAssets(),"fonts/Nexa Light.otf");
        fontbold = Typeface.createFromAsset(getApplication().getAssets(),"fonts/Nexa Bold.otf");
        TextView judul = (TextView) findViewById(R.id.choose);
        judul.setTypeface(fontbold);
        getEcTheme();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    private void getEcTheme(){
        SimpleDateFormat tgl = new SimpleDateFormat("yyyy-MM-dd");
        String tRes = tgl.format(new Date());
        progressDialogHelper.showProgressDialog(EmployeeCornerActivity.this, "Getting data...");
        AndroidNetworking.get(session.getServerURL()+"users/showalltheme/buscd/"+session.getUserBusinessCode())
                .addHeaders("Accept","application/json")
                .addHeaders("Content-Type","application/json")
                .addHeaders("Authorization",session.getToken())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println(response+"hiewuhruTheme");
                        try {
                            if(response.getInt("status")==200){
                                listModel = new ArrayList<EmployeeCornerThemeModel>();
                                JSONArray jsonArray = response.getJSONArray("data");
                                if (jsonArray.length()==0) {
                                    listCorner.setVisibility(View.GONE);
                                    tvNull.setVisibility(View.VISIBLE);
                                } else {
                                    listCorner.setVisibility(View.VISIBLE);
                                    tvNull.setVisibility(View.GONE);
                                    for (int a = 0; a < jsonArray.length(); a++) {
                                        JSONObject object = jsonArray.getJSONObject(a);
                                        String begin_date = object.getString("begin_date");
                                        String end_date = object.getString("end_date");
                                        String business_code = object.getString("business_code");
                                        String theme_id = object.getString("theme_id");
                                        String theme_name = object.getString("theme_name");
                                        String image = object.getString("image");
                                        String change_date = object.getString("change_date");
                                        String change_user = object.getString("change_user");
                                        model = new EmployeeCornerThemeModel(begin_date, end_date, business_code, theme_id, theme_name, image, change_date, change_user);
                                        listModel.add(model);
                                    }
                                    adapter = new EmployeeCornerThemeAdapter(EmployeeCornerActivity.this, listModel);
                                    listCorner.setAdapter(adapter);
//                                    listCorner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                                        @Override
//                                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                                            Intent i = new Intent(EmployeeCornerActivity.this, BookingcornerActivity.class);
//                                            i.putExtra("theme_id", listModel.get(position).getTheme_id());
//                                            i.putExtra("theme_name", listModel.get(position).getTheme_name());
//                                            startActivity(i);
//                                        }
//                                    });
                                }
                                progressDialogHelper.dismissProgressDialog(EmployeeCornerActivity.this);
                            }else{
                                progressDialogHelper.dismissProgressDialog(EmployeeCornerActivity.this);
                            }
                        }catch (Exception e){
                            progressDialogHelper.dismissProgressDialog(EmployeeCornerActivity.this);
                            System.out.println(e);
                        }
                    }
                    @Override
                    public void onError(ANError error) {
                        progressDialogHelper.dismissProgressDialog(EmployeeCornerActivity.this);
                        System.out.println(error);
                    }
                });
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_favorite) {
           Intent intent = new Intent(EmployeeCornerActivity.this, HistorycornerActivity.class);
           startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }


}
