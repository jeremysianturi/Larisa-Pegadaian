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
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import id.co.pegadaian.diarium.R;
import id.co.pegadaian.diarium.adapter.Promosi_SuccessionCPAdapter;
import id.co.pegadaian.diarium.model.PromosiSCPModel;
import id.co.pegadaian.diarium.util.UserSessionManager;
import id.co.pegadaian.diarium.util.element.ProgressDialogHelper;

public class Promosi_SCP_Fragment extends Fragment {

  private List<PromosiSCPModel> listModel;
  private PromosiSCPModel model;
  private ProgressDialogHelper progressDialogHelper;
  private UserSessionManager session;
  private ListView listpromosi;
  private Promosi_SuccessionCPAdapter adapter;


  public Promosi_SCP_Fragment() {
    // Required empty public constructor
  }


  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    View view = inflater.inflate(R.layout.fragment_promosi_ecp_, container, false);

    progressDialogHelper = new ProgressDialogHelper();
    session = new UserSessionManager(getActivity());
    listpromosi = view.findViewById(R.id.list_promosi_ecp);

//    model = new PromosiSCPModel("","","");
//    listModel.add(model);

//    adapter = new Promosi_SuccessionCPAdapter(getActivity(),listModel);
//    listpromosi.setAdapter(adapter);

    getDataPromotionSuccession();

    return view;
  }

  private void getDataPromotionSuccession() {

    AndroidNetworking.get(session.getServerMyCareer()+"api/qualifications/data-suc-promosi-rotasi?personnel_number=173122013&business_code=1200&code_level_movement=1")
            .addHeaders("Accept","application/json")
            .addHeaders("Content-Type","application/json")
            .addHeaders("Authorization","Bearer "+session.getTokenMyCareer())
            .setPriority(Priority.MEDIUM)
            .build()
            .getAsJSONObject(new JSONObjectRequestListener() {
              @Override
              public void onResponse(JSONObject response) {
                System.out.println("promosi_sucsesssion"+response);

                try {
                  if (response.getString("data")!=null){
                    listModel = new ArrayList<PromosiSCPModel>();
                    JSONArray array = response.getJSONArray("data");
                    for (int i = 0; i<array.length();i++) {
                      JSONObject object = array.getJSONObject(i);

                      String object_identifier = object.getString("object_identifier");
                      String personel_number = object.getString("personel_number");
                      String personel_name = object.getString("personel_name");
                      String band_position = object.getString("band_position");
                      String class_position = object.getString("class_position");
                      String code_level_movement = object.getString("code_level_movement");
                      String level_movement = object.getString("level_movement");
                      String position_code = object.getString("position_code");
                      String position_name = object.getString("position_name");
                      String score = object.getString("score");
                      int number = i + 1;

                      model = new PromosiSCPModel(object_identifier, personel_number, personel_name, band_position, class_position, code_level_movement, level_movement, position_code, position_name, score, number);
                      listModel.add(model);
                    }

                    adapter = new Promosi_SuccessionCPAdapter(getActivity(),listModel);
                    listpromosi.setAdapter(adapter);

                  }


                } catch (Exception e) {
                  e.printStackTrace();
                }


              }

              @Override
              public void onError(ANError anError) {

              }
            });
  }
}
