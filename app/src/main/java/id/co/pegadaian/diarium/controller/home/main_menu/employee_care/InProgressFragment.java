package id.co.pegadaian.diarium.controller.home.main_menu.employee_care;


import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
import id.co.pegadaian.diarium.adapter.EmployeeCareAdapter;
import id.co.pegadaian.diarium.model.EmployeeCareModel;
import id.co.pegadaian.diarium.util.UserSessionManager;
import id.co.pegadaian.diarium.util.element.ProgressDialogHelper;

/**
 * A simple {@link Fragment} subclass.
 */
public class InProgressFragment extends Fragment {


    UserSessionManager session;
    private List<EmployeeCareModel> listModel;
    private EmployeeCareModel model;
    private EmployeeCareAdapter adapter;
    private TextView tvNull;
    ListView listBooking;
    private ProgressDialogHelper progressDialogHelper;
    Typeface font,fontbold;

    public InProgressFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_in_progress, container, false);
        session = new UserSessionManager(getActivity());
        progressDialogHelper = new ProgressDialogHelper();
        font = Typeface.createFromAsset(getActivity().getAssets(),"fonts/Nexa Light.otf");
        fontbold = Typeface.createFromAsset(getActivity().getAssets(),"fonts/Nexa Bold.otf");
        listBooking = view.findViewById(R.id.list_available);
        tvNull = view.findViewById(R.id.tvNull);
        getCareData();
        return view;

    }

    @Override
    public void onResume(){
        super.onResume();
        getCareData();
    }

    private void getCareData(){
        SimpleDateFormat tgl = new SimpleDateFormat("yyyy-MM-dd");
        String tRes = tgl.format(new Date());
        progressDialogHelper.showProgressDialog(getActivity(), "Getting data...");
        AndroidNetworking.get(session.getServerURL()+"users/employeeCare/getAllEmployeeCare/ecrst/02/buscd/"+session.getUserBusinessCode())
                .addHeaders("Accept","application/json")
                .addHeaders("Content-Type","application/json")
                .addHeaders("Authorization",session.getToken())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println("RESPONEMPCARE"+response);
                        try {
                            if(response.getInt("status")==200){
                                listModel = new ArrayList<EmployeeCareModel>();
                                JSONArray jsonArray = response.getJSONArray("data");
                                for (int a = jsonArray.length()-1; a >= 0; a--) {
                                    JSONObject object = jsonArray.getJSONObject(a);
                                    String begin_date = object.getString("begin_date");
                                    String end_date = object.getString("end_date");
                                    String business_code = object.getString("business_code");
                                    String personal_number = object.getString("personal_number");
                                    String ticket_number = object.getString("ticket_number");
                                    String problem_type = object.getString("problem_type");
                                    String problem_desc = object.getString("problem_desc");
                                    String image = object.getString("image");
                                    String problem_status = object.getString("problem_status");
                                    String change_date = object.getString("change_date");
                                    String change_user = object.getString("change_user");
                                    String name = null;
                                    JSONArray arrayName = object.getJSONArray("change_user_name");
                                    for (int j=0;j<arrayName.length();j++) {
                                        JSONObject objName = arrayName.getJSONObject(j);
                                        name = objName.getString("full_name");
                                    }
                                    if (problem_status.equals("02")) {
                                        model = new EmployeeCareModel(begin_date, end_date, business_code, personal_number, ticket_number, problem_type, problem_desc, image, problem_status, change_date, change_user, name);
                                        listModel.add(model);
                                    }

                                }
                                if (listModel.size()==0) {
                                    listBooking.setVisibility(View.GONE);
                                    tvNull.setVisibility(View.VISIBLE);
                                } else {
                                    listBooking.setVisibility(View.VISIBLE);
                                    tvNull.setVisibility(View.GONE);
                                    adapter = new EmployeeCareAdapter(getActivity(), listModel);
                                    listBooking.setAdapter(adapter);
                                    listBooking.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                            Intent i = new Intent(getActivity(), DetailEmployeeCareActivity.class);
                                            i.putExtra("problem_type",listModel.get(position).getProblem_type());
                                            i.putExtra("problem_desc",listModel.get(position).getProblem_desc());
                                            i.putExtra("image",listModel.get(position).getImage());
                                            i.putExtra("personal_number",listModel.get(position).getPersonal_number());
                                            i.putExtra("change_date",listModel.get(position).getChange_date());
                                            i.putExtra("ticket",listModel.get(position).getTicket_number());
                                            i.putExtra("status","y");
                                            i.putExtra("name",listModel.get(position).getName());
                                            startActivity(i);
                                        }
                                    });
                                }
                                progressDialogHelper.dismissProgressDialog(getActivity());
                            }else{
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

}
