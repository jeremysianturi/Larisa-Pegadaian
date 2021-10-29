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

import id.co.pegadaian.diarium.R;
import id.co.pegadaian.diarium.adapter.Adapter_ecp_promosi;
import id.co.pegadaian.diarium.model.PromosiEcpModel;
//import id.co.pegadaian.diarium.util.ServiceGeneratorCollege;
import id.co.pegadaian.diarium.util.UserSessionManager;
import id.co.pegadaian.diarium.util.element.ProgressDialogHelper;

public class PromosiECP_Fragment extends Fragment {

  private List<PromosiEcpModel> listModel;
  private PromosiEcpModel model;
  private ProgressDialogHelper progressDialogHelper;
  private UserSessionManager session;
  private ListView listpromosi;
  private Adapter_ecp_promosi adapter;
//  private ServiceGeneratorCollege serviceGeneratorCollege;


  public PromosiECP_Fragment() {
    // Required empty public constructor
  }


  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    View view = inflater.inflate(R.layout.fragment_promosi_ecp_, container, false);

    listModel = new ArrayList<PromosiEcpModel>();
    progressDialogHelper = new ProgressDialogHelper();
    session = new UserSessionManager(getActivity());
    listpromosi = view.findViewById(R.id.list_promosi_ecp);


//    model = new PromosiEcpModel("","","");
//    listModel.add(model);

//    adapter = new Adapter_ecp_promosi(getActivity(),listModel);
//    listpromosi.setAdapter(adapter);

//    getUnsafeOkHttpClient();
    getPromosiECP();
    return view;
  }



  private void getPromosiECP() {
    System.out.println("Bearer "+session.getTokenLdap());
    AndroidNetworking.get(session.getServerMyCareer()+"api/qualifications/data-ecp-promosi-rotasi?personnel_number=173122013&business_code=1200&code_level_movement=1")
            .addHeaders("Accept","application/json")
            .addHeaders("Content-Type","application/json")
            .addHeaders("Authorization","Bearer "+session.getTokenLdap())
            .setPriority(Priority.MEDIUM)
            .build()
            .getAsJSONObject(new JSONObjectRequestListener() {
              @Override
              public void onResponse(JSONObject response) {
                System.out.println("response_promosi " +response);

                try{
                  listModel = new ArrayList<PromosiEcpModel>();
                  JSONArray jsonArray = response.getJSONArray("data");
                    for (int i = 0; i<jsonArray.length(); i++){
                      JSONObject object = jsonArray.getJSONObject(i);
                      String aplicans = object.getString("interested");
                      String rank = object.getString("ranking");
                      String score = object.getString("score");
                      String band = object.getString ("band_position");
                      String title = object.getString("position_name");

                      model = new PromosiEcpModel(aplicans,band,rank,score,title);
                      listModel.add(model);

                    }
                    adapter = new Adapter_ecp_promosi(getActivity(),listModel);
                    listpromosi.setAdapter(adapter);
                } catch (JSONException e) {
                  e.printStackTrace();
                }
              }
              @Override
              public void onError(ANError anError) {
                System.out.println("EROORPROMOSIECP "+anError);

              }
            });
  }
}
