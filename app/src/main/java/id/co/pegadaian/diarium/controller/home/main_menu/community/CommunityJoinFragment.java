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
import in.galaxyofandroid.spinerdialog.SpinnerDialog;


/**
 * A simple {@link Fragment} subclass.
 */

public class CommunityJoinFragment extends Fragment {
    UserSessionManager session;
    private ProgressDialogHelper progressDialogHelper;
    private List<CommunityModel> listModel;
    private CommunityModel model;
    private CommunityAdapter adapter;
    ListView listCommunity;
    TextView tvNull;
    final List<String> list = new ArrayList<String>();


    TextView option;
    ArrayList<String> opsi= new ArrayList<>();
    SpinnerDialog spinnerDialogs;
//    Spinner spinnerType;
    Typeface font,fontbold;
    public CommunityJoinFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_community_join, container, false);

        session = new UserSessionManager(getActivity());
        progressDialogHelper = new ProgressDialogHelper();

        font = Typeface.createFromAsset(getActivity().getAssets(),"fonts/Nexa Light.otf");
        fontbold = Typeface.createFromAsset(getActivity().getAssets(),"fonts/Nexa Bold.otf");

        listCommunity = view.findViewById(R.id.list_community);
        tvNull = view.findViewById(R.id.tvNull);
//        spinnerType = view.findViewById(R.id.spinnerCommunityType);
//        list.add("Public");
//        list.add("Private");
//        ArrayAdapter<String> adp1 = new ArrayAdapter<String>(getActivity(),
//                android.R.layout.simple_spinner_dropdown_item, list);
//        adp1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spinnerType.setAdapter(adp1);
//        spinnerType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
//        {
//            @Override
//            public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {
//                // TODO Auto-generated method stub
////                Toast.makeText(getActivity(), list.get(position), Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> arg0) {
//                // TODO Auto-generated method stub
//            }
//        });

        getMyCOmmunity();
        return view;

    }

    @Override
    public void onResume(){
        super.onResume();
        getMyCOmmunity();
    }

    private void getMyCOmmunity(){
        progressDialogHelper.showProgressDialog(getActivity(), "Getting data...");
        AndroidNetworking.get(session.getServerURL()+"users/myCommunity/nik/"+session.getUserNIK()+"/buscd/"+session.getUserBusinessCode())
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
                        System.out.println(response+"RESPONJOINCOMMUNITY");
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
                                        String business_code = object.getString("business_code");
                                        String personal_number = object.getString("personal_number");
                                        String community_id = object.getString("community_id");
                                        String community_role = object.getString("community_role");
                                        String aprov = object.getString("aprov");
                                        String change_date = object.getString("change_date");
                                        String change_user = object.getString("change_user");
                                        JSONArray arrayName = object.getJSONArray("community");
                                        for (int b=0;b<arrayName.length(); b++) {
                                            JSONObject objectName = arrayName.getJSONObject(b);
                                            String community_name = objectName.getString("community_name");
                                            String community_desc = objectName.getString("community_desc");
                                            String community_max_person = objectName.getString("community_max_person");
                                            String community_date = objectName.getString("community_date");
                                            model = new CommunityModel(begin_date, end_date, community_id, community_name, community_desc, "01", community_max_person, community_date, change_date, change_user, business_code, personal_number, community_role);
                                            listModel.add(model);
                                        }
                                    }
                                    adapter = new CommunityAdapter(getActivity(), listModel);
                                    listCommunity.setAdapter(adapter);

                                    listCommunity.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                            Intent i = new Intent(getActivity(), CommunityMessageActivity.class);
                                            i.putExtra("community_id", listModel.get(position).getCommunity_id());
                                            i.putExtra("community_name", listModel.get(position).getCommunity_name());
                                            i.putExtra("community_role", listModel.get(position).getRole());
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
