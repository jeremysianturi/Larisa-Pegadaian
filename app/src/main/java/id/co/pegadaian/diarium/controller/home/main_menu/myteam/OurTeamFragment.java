package id.co.pegadaian.diarium.controller.home.main_menu.myteam;


import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

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
import id.co.pegadaian.diarium.adapter.OurTeamAdapter;
import id.co.pegadaian.diarium.model.OurTeamModel;
import id.co.pegadaian.diarium.util.UserSessionManager;
import id.co.pegadaian.diarium.util.element.ProgressDialogHelper;


/**
 * A simple {@link Fragment} subclass.
 */
public class OurTeamFragment extends Fragment {
    UserSessionManager session;
    private List<OurTeamModel> listModel;
    private OurTeamModel model;
    private OurTeamAdapter adapter;
    private ListView listOurTeam;
    private TextView tvNull;
    private ProgressDialogHelper progressDialogHelper;

    Typeface font,fontbold;
    public OurTeamFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_ourteam, container, false);
        session = new UserSessionManager(getActivity());
        progressDialogHelper = new ProgressDialogHelper();
        font = Typeface.createFromAsset(getActivity().getAssets(),"fonts/Nexa Light.otf");
        fontbold = Typeface.createFromAsset(getActivity().getAssets(),"fonts/Nexa Bold.otf");
        listOurTeam = view.findViewById(R.id.listourteam);
        tvNull = (TextView) view.findViewById(R.id.tvNull);
        getOurTeam();
        return view;
    }

    private void getOurTeam(){
        progressDialogHelper.showProgressDialog(getActivity(), "Getting data team...");
        SimpleDateFormat tgl = new SimpleDateFormat("yyyy-MM-dd");
        String tRes = tgl.format(new Date());
        String outTeamUrl = session.getServerURL()+"users/"+session.getUserBusinessCode()+"/ourteam?";
        System.out.println("check value get pur team: " + outTeamUrl);
        AndroidNetworking.get(outTeamUrl)
                .addHeaders("Accept","application/json")
                .addHeaders("Content-Type","application/json")
                .addHeaders("Authorization",session.getToken())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println(response+"TEAMOUR");
                        try {
                            if(response.getInt("status")==200){
                                listModel = new ArrayList<OurTeamModel>();
                                JSONArray jsonArray = response.getJSONArray("data");
                                if (jsonArray.length()==0) {
                                    listOurTeam.setVisibility(View.GONE);
                                    tvNull.setVisibility(View.VISIBLE);
                                } else {
                                    listOurTeam.setVisibility(View.VISIBLE);
                                    tvNull.setVisibility(View.GONE);
                                    for (int a = 0; a < jsonArray.length(); a++) {
                                        JSONObject object = jsonArray.getJSONObject(a);
                                        String personal_number = object.getString("personal_number");
                                        String name = object.getString("name");
                                        String start_date = object.getString("start_date");
                                        String end_date = object.getString("end_date");
                                        String unit = object.getString("unit");
                                        String position = object.getString("position");
                                        String relation = object.getString("relation");
                                        String status = object.getString("status");
                                        String profile = object.getString("profile");
                                        String lead_posisi = object.getString("lead_posisi");
                                        if (!personal_number.equals(session.getUserNIK())) {
                                            model = new OurTeamModel(personal_number, name, start_date, end_date, unit, position, relation, status, profile, lead_posisi);
                                            listModel.add(model);
                                        }

                                    }

                                    adapter = new OurTeamAdapter(getActivity(), listModel);
                                    listOurTeam.setAdapter(adapter);
                                }
                            }else{
                            }
                            progressDialogHelper.dismissProgressDialog(getActivity());
                        }catch (Exception e){
                            System.out.println(e);
                            progressDialogHelper.dismissProgressDialog(getActivity());
                        }
                    }
                    @Override
                    public void onError(ANError error) {
                        System.out.println(error);
                        progressDialogHelper.dismissProgressDialog(getActivity());
                    }
                });
    }

}
