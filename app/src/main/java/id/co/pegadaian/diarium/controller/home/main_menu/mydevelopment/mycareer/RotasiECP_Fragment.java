package id.co.pegadaian.diarium.controller.home.main_menu.mydevelopment.mycareer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import id.co.pegadaian.diarium.R;
import id.co.pegadaian.diarium.adapter.RotasiECP_Adapter;
import id.co.pegadaian.diarium.model.RotasiECPModel;
import id.co.pegadaian.diarium.util.UserSessionManager;
import id.co.pegadaian.diarium.util.element.ProgressDialogHelper;

public class RotasiECP_Fragment extends Fragment {

  private List<RotasiECPModel> listModel;
  private RotasiECPModel model;
  private ProgressDialogHelper progressDialogHelper;
  private UserSessionManager session;
  private ListView listrotasi;
  private RotasiECP_Adapter adapter;

  public RotasiECP_Fragment() {
    // Required empty public constructor
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    View view = inflater.inflate(R.layout.fragment_rotasi, container, false);

    session = new UserSessionManager(getActivity());
    progressDialogHelper = new ProgressDialogHelper();

    listModel = new ArrayList<RotasiECPModel>();
    listrotasi = view.findViewById(R.id.list_rotasi_ecp);

//    model = new RotasiECPModel("","","","","");
//    listModel.add(model);
//
//    adapter = new RotasiECP_Adapter(getActivity(),listModel);
//    listrotasi.setAdapter(adapter);


    getRotasiECP();
    return view;
  }

  private void getRotasiECP() {
    System.out.println("mnas");

    progressDialogHelper.showProgressDialog(Objects.requireNonNull(getActivity()),"Load data ...");
    AndroidNetworking.get(session.getServerMyCareer()+"api/qualifications/data-ecp-promosi-rotasi?personnel_number=173122013&business_code=1200&code_level_movement=2")
            .addHeaders("Accept","application/json")
            .addHeaders("Content-Type","application/json")
            .addHeaders("Authorization","Bearer "+session.getTokenLdap())
            .setPriority(Priority.MEDIUM)
            .build()
            .getAsJSONObject(new JSONObjectRequestListener() {
              @Override
              public void onResponse(JSONObject response) {
                System.out.println("response_rotasi"+response);
                try{
                  listModel = new ArrayList<RotasiECPModel>();
                  JSONArray jsonArray = response.getJSONArray("data");
                  for (int i = 0; i<jsonArray.length(); i++){
                    JSONObject object = jsonArray.getJSONObject(i);
                    String aplicans = object.getString("interested");
                    String rank = object.getString("ranking");
                    String score = object.getString("score");
                    String band = object.getString ("band_position");
                    String title = object.getString("position_name");

                    int no = i+1;

                    model = new RotasiECPModel(aplicans,band,rank,score,title,no);
                    listModel.add(model);
                  }

                  adapter = new RotasiECP_Adapter(getContext(),listModel);
                  listrotasi.setAdapter(adapter);

                  progressDialogHelper.dismissProgressDialog(getActivity());
                } catch (JSONException e) {
                  e.printStackTrace();
                }

              }

              @Override
              public void onError(ANError anError) {
                progressDialogHelper.dismissProgressDialog(getActivity());
                System.out.println("ERROR__ "+anError);
                System.out.println("EROOR___ "+anError.getResponse());
              }
            });
  }


}
