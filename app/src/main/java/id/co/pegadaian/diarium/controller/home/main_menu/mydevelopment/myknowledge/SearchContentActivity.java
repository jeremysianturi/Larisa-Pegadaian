package id.co.pegadaian.diarium.controller.home.main_menu.mydevelopment.myknowledge;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import id.co.pegadaian.diarium.R;
import id.co.pegadaian.diarium.adapter.SearchContentLMSAdapter;
import id.co.pegadaian.diarium.model.SearchTempModel;
import id.co.pegadaian.diarium.util.UserSessionManager;
import id.co.pegadaian.diarium.util.element.ProgressDialogHelper;

public class SearchContentActivity extends AppCompatActivity {

    private EditText mEditText;
    UserSessionManager session;
    private ProgressDialogHelper progressDialogHelper;
    private List<SearchTempModel> listModel;
    private SearchTempModel model;
    private SearchContentLMSAdapter adapter;
    GridView listPartner;
//    Button btnSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_content);
        session = new UserSessionManager(this);
        listPartner = findViewById(R.id.gridview);
        mEditText = findViewById(R.id.editText1);
        mEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    String param = mEditText.getText().toString();
                    getPartner();
                    return true;
                }
                return false;
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Search Content");
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void getPartner(){
        listModel = new ArrayList<SearchTempModel>();
        for (int a = 0; a < 10; a++) {
            model = new SearchTempModel("personal_number"+a,"Materi"+a,"job"+a,"unit"+a,"posisi"+a,"profile"+a);
            listModel.add(model);
        }
        adapter = new SearchContentLMSAdapter(SearchContentActivity.this, listModel);
        listPartner.setAdapter(adapter);
    }
//
//    private void getPartner(String param){
//        AndroidNetworking.get(session.getServerURL()+"users/search/key/"+param+"/buscd/"+session.getUserBusinessCode())
//                .addHeaders("Accept","application/json")
//                .addHeaders("Content-Type","application/json")
//                .addHeaders("Authorization",session.getToken())
//                //.addJSONObjectBody(body)
//                .setPriority(Priority.MEDIUM)
//                .build()
//                .getAsJSONObject(new JSONObjectRequestListener() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        // do anything with response
//                        System.out.println(response+"seradkewjekj");
//                        try {
//                            if(response.getInt("status")==200){
//                                listModel = new ArrayList<SearchTempModel>();
////                                SearchTempModel contacts = new SearchTempModel();
//                                JSONArray jsonArray = response.getJSONArray("data");
//                                for (int a = 0; a < jsonArray.length(); a++) {
//                                    JSONObject object = jsonArray.getJSONObject(a);
//                                    String end_date = object.getString("end_date");
//                                    String business_code = object.getString("business_code");
//                                    String personal_number = object.getString("personal_number");
//                                    String full_name = object.getString("full_name");
//                                    String nickname = object.getString("nickname");
//                                    String born_city = object.getString("born_city");
//                                    String born_date = object.getString("born_date");
//                                    String gender = object.getString("gender");
//                                    String religion = object.getString("religion");
//                                    String language = object.getString("language");
//                                    String national = object.getString("national");
//                                    String tribe = object.getString("tribe");
//                                    String blood_type = object.getString("blood_type");
//                                    String rhesus = object.getString("rhesus");
//                                    String marital_status = object.getString("marital_status");
//                                    String marital_date = object.getString("marital_date");
//                                    String personal_number_reference = object.getString("personal_number_reference");
//                                    String change_date = object.getString("change_date");
//                                    String change_user = object.getString("change_user");
//                                    String begin_date = object.getString("begin_date");
//                                    String job = object.getString("job");
//                                    String unit = object.getString("unit");
//                                    String posisi = object.getString("posisi");
//                                    String profile = object.getString("profile");
//                                    if (!personal_number.equals(session.getUserNIK())) {
//                                        model = new SearchTempModel(personal_number, full_name, job, unit, posisi, profile);
//                                        listModel.add(model);
//                                    }
//                                    listPartner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                                        @Override
//                                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                                            Intent i = new Intent(SearchContentActivity.this, ProfileActivity.class);
//                                            i.putExtra("personal_number",listModel.get(position).getPersonal_number());
//                                            i.putExtra("avatar",listModel.get(position).getProfile());
//                                            i.putExtra("job",listModel.get(position).getUnit());
//                                            startActivity(i);
//                                        }
//                                    });
//                                }
//
//                                adapter = new SearchTempAdapter(SearchContentActivity.this, listModel);
//                                listPartner.setAdapter(adapter);
//
//                            }else{
//                                Toast.makeText(SearchContentActivity.this, "no result for "+param, Toast.LENGTH_SHORT).show();
//                            }
//                        }catch (Exception e){
//                            System.out.println(e);
//                        }
//
//                    }
//                    @Override
//                    public void onError(ANError error) {
//                        System.out.println(error);
//                    }
//                });
//    }

}