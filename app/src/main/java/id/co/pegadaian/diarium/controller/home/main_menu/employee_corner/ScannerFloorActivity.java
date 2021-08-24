package id.co.pegadaian.diarium.controller.home.main_menu.employee_corner;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.zxing.Result;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

import id.co.pegadaian.diarium.util.UserSessionManager;
import me.dm7.barcodescanner.zxing.ZXingScannerView;


public class ScannerFloorActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {
    private ZXingScannerView mScannerView;
    String title, theme_id, book_code, empcorner_id, batch_id, date;
    UserSessionManager session;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        title = intent.getStringExtra("title");
        theme_id = intent.getStringExtra("theme_id");
        book_code = intent.getStringExtra("book_code");
        empcorner_id = intent.getStringExtra("empcorner_id");
        batch_id = intent.getStringExtra("batch_id");
        date = intent.getStringExtra("date");

        mScannerView = new ZXingScannerView(this);
        session = new UserSessionManager(this);
        setContentView(mScannerView);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Scan QR for "+title);
    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this);
        mScannerView.startCamera();
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();
    }

    @Override
    public void handleResult(Result rawResult) {

        Log.v("TAG", rawResult.getText()); // Prints scan results
        Log.v("TAG", rawResult.getBarcodeFormat().toString());
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        String url = rawResult.getText();
        Toast.makeText(ScannerFloorActivity.this, url, Toast.LENGTH_SHORT).show();
        if (url.equals("diarium"+batch_id)) {
            sendBooking(book_code, empcorner_id, batch_id, date);
        } else {
            Toast.makeText(this, "Invalid QR", Toast.LENGTH_SHORT).show();
        }
        AlertDialog alert1 = builder.create();
        alert1.show();
        mScannerView.resumeCameraPreview(this);
    }

    private void sendBooking(String book_code, String empcorner_id, String batch_id, String date) {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat tgl = new SimpleDateFormat("yyyy-MM-dd");
        String tRes = tgl.format(new Date());

        @SuppressLint("SimpleDateFormat") SimpleDateFormat jam = new SimpleDateFormat("HH:mm");
        String jamRes = jam.format(new Date());

//        JSONObject jResult = new JSONObject();// main object
//        JSONArray jArray = new JSONArray();// /ItemDetail jsonArray
        JSONObject jGroup = new JSONObject();// /sub Object
        try {
            jGroup.put("begin_date", tRes);
//                jGroup.put("end_date", "9999-12-31");
            jGroup.put("business_code", session.getUserBusinessCode());
            jGroup.put("personal_number", session.getUserNIK());
            jGroup.put("book_code", book_code);
            jGroup.put("empcorner_id", empcorner_id);
            jGroup.put("book_date", date);
            jGroup.put("batch_id", batch_id);
            jGroup.put("change_date", date);
            jGroup.put("change_user", session.getUserNIK());
            jGroup.put("book_status", "D");
//                jArray.put(jGroup);
            // /itemDetail Name is JsonArray Name
//                jResult.put("", jGroup);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        System.out.println(jGroup + "PARAMBOOKINGEMPLOYEECORNER");
        AndroidNetworking.post(session.getServerURL()+"users/employeeCornerBooking/ecbcd/"+book_code+"/btcid/"+batch_id+"/buscd/"+session.getUserBusinessCode())
                .addHeaders("Accept","application/json")
                .addHeaders("Content-Type","application/json")
                .addHeaders("Authorization",session.getToken())
                .addJSONObjectBody(jGroup)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // do anything with response
                        System.out.println(response+"RESPONBOOKINGEMPLOYEECORNER");
                        try {
                            if(response.getInt("status")==200){
                                Toast.makeText(ScannerFloorActivity.this, "Success Checkin", Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(ScannerFloorActivity.this, HistorycornerActivity.class);
                                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(i);
                                ((Activity)ScannerFloorActivity.this).finish();
                            }else {
                                Toast.makeText(ScannerFloorActivity.this,"Failed!",Toast.LENGTH_SHORT).show();
                            }
                        }catch (Exception e){
                            System.out.println(e);
                        }
                    }
                    @Override
                    public void onError(ANError error) {
                        System.out.println(error);
                    }
                });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }

}

