package id.co.pegadaian.diarium.controller.home.main_menu.search_partner;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import id.co.pegadaian.diarium.R;
import id.co.pegadaian.diarium.adapter.SearchAdapter;
import id.co.pegadaian.diarium.model.SearchModel;
import id.co.pegadaian.diarium.util.UserSessionManager;

public class SearchActivity extends AppCompatActivity {
    private ListView lv;
    EditText inputSearch;
    UserSessionManager session;
    private List<SearchModel> listModel;
    private SearchModel model;
    private SearchAdapter adapterSearch;
    ArrayList<HashMap<String, String>> productList;
    String products[] = {"Dell Inspiron", "HTC One X", "HTC Wildfire S", "HTC Sense", "HTC Sensation XE",
            "iPhone 4S", "Samsung Galaxy Note 800",
            "Samsung Galaxy S3", "MacBook Air", "Mac Mini", "MacBook Pro"};
    String[] str;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        session = new UserSessionManager(this);
        lv = findViewById(R.id.list_view);
        lv.setTextFilterEnabled(true);
        inputSearch = findViewById(R.id.inputSearch);

        inputSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                // When user changed the Text
                adapterSearch.getFilter().filter(cs);
//                                        adapterSearch.getFilter().filter(cs);
            }
            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
                // TODO Auto-generated method stub
            }
            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
            }
        });

        getInbox();
    }

    private void getInbox(){
        AndroidNetworking.get(session.getServerURL()+"users/search/key/a/buscd/1000")
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
                        System.out.println(response+"seradkewjekj");
                        try {
                            if(response.getInt("status")==200){
//                                session.setToken(response.getString("token"));
                                listModel = new ArrayList<SearchModel>();
                                JSONArray jsonArray = response.getJSONArray("data");
                                for (int a = 0; a < jsonArray.length(); a++) {
                                    JSONObject object = jsonArray.getJSONObject(a);
                                    String personal_number = object.getString("personal_number");
                                    String full_name = object.getString("full_name");
                                    String profile = object.getString("profile");
                                    String job = object.getString("job");
                                    String posisi = object.getString("posisi");
                                    model = new SearchModel(personal_number,full_name,profile,job+" "+posisi);
                                    listModel.add(model);
                                }
                                adapterSearch = new SearchAdapter(SearchActivity.this, listModel);
                                lv.setAdapter(adapterSearch);

                            }else{
//                                popUpLogin();
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
}