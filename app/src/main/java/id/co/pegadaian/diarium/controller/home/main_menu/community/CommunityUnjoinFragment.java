package id.co.pegadaian.diarium.controller.home.main_menu.community;


import android.app.Dialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import id.co.pegadaian.diarium.R;
import id.co.pegadaian.diarium.adapter.CommunityAdapter;
import id.co.pegadaian.diarium.controller.login.LoginActivity;
import id.co.pegadaian.diarium.model.CommunityModel;
import id.co.pegadaian.diarium.util.UserSessionManager;
import id.co.pegadaian.diarium.util.element.ProgressDialogHelper;


/**
 * A simple {@link Fragment} subclass.
 */

public class CommunityUnjoinFragment extends Fragment {
    UserSessionManager session;
    private ProgressDialogHelper progressDialogHelper;
    private List<CommunityModel> listModel;
    private CommunityModel model;
    private CommunityAdapter adapter;
    ListView listCommunity;
    TextView tvNull;

    TextView option;
    ArrayList<String> opsi= new ArrayList<>();
//    SpinnerDialog spinnerDialogs;
    Typeface font,fontbold;
    public CommunityUnjoinFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_community_unjoin, container, false);

        session = new UserSessionManager(getActivity());
        progressDialogHelper = new ProgressDialogHelper();

        font = Typeface.createFromAsset(getActivity().getAssets(),"fonts/Nexa Light.otf");
        fontbold = Typeface.createFromAsset(getActivity().getAssets(),"fonts/Nexa Bold.otf");

        listCommunity = view.findViewById(R.id.list_community);
        tvNull = view.findViewById(R.id.tvNull);

        getAllCommunity();
        return view;

    }

    @Override
    public void onResume(){
        super.onResume();
        getAllCommunity();
    }

    private void getAllCommunity(){
        progressDialogHelper.showProgressDialog(getActivity(), "Getting data...");
        AndroidNetworking.get(session.getServerURL()+"users/community/buscd/"+session.getUserBusinessCode())
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
                        System.out.println(response+"RESPONUNJOINCOMMUNITY");
                        try {
                            if(response.getInt("status")==200){
//                                session.setToken(response.getString("token"));
                                listModel = new ArrayList<CommunityModel>();
                                JSONArray jsonArray = response.getJSONArray("data");
                                if (jsonArray.length()==0) {
                                    tvNull.setVisibility(View.VISIBLE);
                                    listCommunity.setVisibility(View.GONE);
                                } else {
                                    tvNull.setVisibility(View.GONE);
                                    listCommunity.setVisibility(View.VISIBLE);
                                    for (int a = 0; a < jsonArray.length(); a++) {
                                        JSONObject object = jsonArray.getJSONObject(a);
                                        String begin_date = object.getString("begin_date");
                                        String end_date = object.getString("end_date");
                                        String community_id = object.getString("community_id");
                                        String community_name = object.getString("community_name");
                                        String community_desc = object.getString("community_desc");
                                        JSONArray arrayType = object.getJSONArray("community_type");
                                        String community_max_person = object.getString("community_max_person");
                                        String community_date = object.getString("community_date");
                                        String change_date = object.getString("change_date");
                                        String change_user = object.getString("change_user");
                                        String business_code = object.getString("business_code");
                                        String personal_number = object.getString("personal_number");
                                        for (int b=0; b<arrayType.length(); b++) {
                                            JSONObject objectType = arrayType.getJSONObject(b);
                                            String type = objectType.getString("object_id");
                                            model = new CommunityModel(begin_date, end_date, community_id, community_name, community_desc, type, community_max_person, community_date, change_date, change_user, business_code, personal_number, "US");
                                            listModel.add(model);
                                        }
                                    }
                                    adapter = new CommunityAdapter(getActivity(), listModel);
                                    listCommunity.setAdapter(adapter);

                                    listCommunity.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                            Intent i = new Intent(getActivity(), OverviewCommunityActivity.class);
                                            i.putExtra("community_id", listModel.get(position).getCommunity_id());
                                            i.putExtra("community_name", listModel.get(position).getCommunity_name());
                                            i.putExtra("community_type", listModel.get(position).getCommunity_type());
                                            i.putExtra("community_desc", listModel.get(position).getCommunity_desc());
                                            startActivity(i);
                                        }
                                    });
                                }
                                progressDialogHelper.dismissProgressDialog(getActivity());
                            }else{
                                popUpLogin();
                                progressDialogHelper.dismissProgressDialog(getActivity());

                            }
                        }catch (Exception e){
                            progressDialogHelper.dismissProgressDialog(getActivity());

                            System.out.println(e);
                        }

                    }
                    @Override
                    public void onError(ANError error) {
                        progressDialogHelper.dismissProgressDialog(getActivity());

                        System.out.println(error);
                    }
                });
    }

    private void popUpLogin() {
        final Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.layout_session_end);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        dialog.getWindow().setAttributes(lp);
        dialog.setTitle("Input Code Here");
        Button btnYes =(Button) dialog.findViewById(R.id.btnYes);
        dialog.show();
        dialog.setCancelable(false);
        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                session.setLoginState(false);
                Intent i = new Intent(getActivity(), LoginActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
            }
        });
    }

    private void initItem() {
        opsi.add("Public");
        opsi.add("Private");
    }

}
