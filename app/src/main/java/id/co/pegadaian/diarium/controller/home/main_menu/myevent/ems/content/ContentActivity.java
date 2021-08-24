package id.co.pegadaian.diarium.controller.home.main_menu.myevent.ems.content;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import id.co.pegadaian.diarium.R;
import id.co.pegadaian.diarium.adapter.MateriAdapter;
import id.co.pegadaian.diarium.model.MateriModel;
import id.co.pegadaian.diarium.util.UserSessionManager;
import id.co.pegadaian.diarium.util.element.ProgressDialogHelper;

public class ContentActivity extends AppCompatActivity {
    private ListView listView;
    private MateriModel model;
    private List<MateriModel> listModel;
    private MateriAdapter adapter;
    private Typeface font, fontbold;
    private UserSessionManager session;
    private EditText etSearch;
    private ProgressDialogHelper progressDialogHelper;
    private ProgressDialog progressDialog;
    private String namaFile;
    TextView tvNull;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);
        session = new UserSessionManager(getApplicationContext());
        progressDialogHelper = new ProgressDialogHelper();
        listView = (ListView) findViewById(R.id.lv_materi);
        tvNull = (TextView) findViewById(R.id.tvNull);

        getContent(session.getEventId());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Content");
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void getContent(String event_id){
        progressDialogHelper.showProgressDialog(ContentActivity.this, "Getting data...");
        AndroidNetworking.get(session.getServerURL()+"users/eventEMS/"+event_id+"/content/buscd/"+session.getUserBusinessCode())
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
                                listModel = new ArrayList<MateriModel>();
                                JSONArray jsonArray = response.getJSONArray("data");
                                if (jsonArray.length()==0) {
                                    tvNull.setVisibility(View.VISIBLE);
                                    listView.setVisibility(View.GONE);
                                } else {
                                    tvNull.setVisibility(View.GONE);
                                    listView.setVisibility(View.VISIBLE);
                                    for (int a = 0; a < jsonArray.length(); a++) {
                                        JSONObject object = jsonArray.getJSONObject(a);
                                        String begin_date = object.getString("begin_date");
                                        String end_date = object.getString("end_date");
                                        String business_code = object.getString("business_code");
                                        String personal_number = object.getString("personal_number");
                                        String event_id = object.getString("event_id");
                                        String content_id = object.getString("content_id");
                                        String title = object.getString("title");
                                        String description = object.getString("description");
                                        String file_content = object.getString("file_content");
                                        String change_date = object.getString("change_date");
                                        String change_user = object.getString("change_user");
                                        System.out.println(begin_date+"dl2j3nr3");
                                        model = new MateriModel(begin_date, end_date, business_code, personal_number, event_id, content_id, title, description, file_content, change_date, change_user);
                                        listModel.add(model);
                                    }
                                    adapter = new MateriAdapter(ContentActivity.this, listModel);
                                    listView.setAdapter(adapter);
                                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(listModel.get(position).getFile_content()));
                                            startActivity(browserIntent);

//                                            Intent i = new Intent(ContentActivity.this, DetailContentActivity.class);
//                                            i.putExtra("title",listModel.get(position).getTitle());
//                                            i.putExtra("description",listModel.get(position).getDescription());
//                                            i.putExtra("file_content",listModel.get(position).getFile_content());
//                                            startActivity(i);
                                        }
                                    });
                                }
                                progressDialogHelper.dismissProgressDialog(ContentActivity.this);
                            }else{
//                                popUpLogin();
                                progressDialogHelper.dismissProgressDialog(ContentActivity.this);
                            }
                        }catch (Exception e){
                            progressDialogHelper.dismissProgressDialog(ContentActivity.this);
                            System.out.println(e);
                        }

                    }
                    @Override
                    public void onError(ANError error) {
                        progressDialogHelper.dismissProgressDialog(ContentActivity.this);
                        System.out.println(error);
                    }
                });
    }

    public void showFile(String name) {

        Environment.getExternalStorageState();
        String storage = Environment.getExternalStorageDirectory().toString() + "/Download";
        //File pdfFile = new File(storage + "/Rapim/" + name);
        File pdfFile = new File(storage + "/" +name);
        Uri path = Uri.fromFile(pdfFile);
        Intent pdfIntent = new Intent(Intent.ACTION_VIEW);
        pdfIntent.setDataAndType(path, "application/pdf");
        pdfIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(pdfIntent);

    }

    private class DownloadFile extends AsyncTask<String, Integer, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Create progress dialog
            progressDialog = new ProgressDialog(ContentActivity.this);
            // Set your progress dialog Title
            progressDialog.setTitle("Download Materi");
            // Set your progress dialog Message
            progressDialog.setMessage("Please Wait!");
            progressDialog.setIndeterminate(false);
            progressDialog.setMax(100);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            // Show progress dialog
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... Url) {
            try {
                URL url = new URL(Url[0]);
                namaFile = Url[1];
                URLConnection connection = url.openConnection();
                connection.connect();

                // Detect the file lenghth
                int fileLength = connection.getContentLength();

                // Locate storage location
                String filepath = Environment.getExternalStorageDirectory().toString();
                Log.d("isi file path nya", filepath);

                // Download the file
                InputStream input = new BufferedInputStream(url.openStream());

                // Save the downloaded file
                OutputStream output = new FileOutputStream(filepath +"/Download/"+ namaFile);
                //OutputStream output = new FileOutputStream(filepath + "/Rapim/" + namaFile);

                byte data[] = new byte[1024];
                long total = 0;
                int count;
                while ((count = input.read(data)) != -1) {
                    total += count;
                    // Publish the progress
                    publishProgress((int) (total * 100 / fileLength));
                    output.write(data, 0, count);

                }
                // Close connection
                output.flush();
                output.close();
                input.close();

            } catch (Exception e) {
                // Error Log
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            super.onProgressUpdate(progress);
            // Update the progress dialog
            progressDialog.setProgress(progress[0]);
            // Dismiss the progress dialog
            if (progressDialog.getProgress() == 100) {
                progressDialog.dismiss();
                showFile(namaFile);
            }
        }
    }
}
