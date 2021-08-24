package id.co.pegadaian.diarium.controller.home.main_menu.mydevelopment.myknowledge;


import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import id.co.pegadaian.diarium.R;
import id.co.pegadaian.diarium.adapter.KnowledgeAdapter;
import id.co.pegadaian.diarium.controller.home.main_menu.mydevelopment.mytraining.VideoActivity;
import id.co.pegadaian.diarium.model.KnowledgeModel;
import id.co.pegadaian.diarium.util.UserSessionManager;
import id.co.pegadaian.diarium.util.element.ProgressDialogHelper;

/**
 * A simple {@link Fragment} subclass.
 */
public class PopularContentFragment extends Fragment {
    private ProgressDialogHelper progressDialogHelper;
    UserSessionManager session;
    private List<KnowledgeModel> listModel;
    private KnowledgeModel model;
    private KnowledgeAdapter adapter;
    TextView tvNull;
    GridView gridView;
    Typeface font,fontbold;


    public PopularContentFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_popular_content, container, false);
        progressDialogHelper = new ProgressDialogHelper();
        session = new UserSessionManager(getActivity());
        font = Typeface.createFromAsset(getActivity().getAssets(),"fonts/Nexa Light.otf");
        fontbold = Typeface.createFromAsset(getActivity().getAssets(),"fonts/Nexa Bold.otf");
        gridView = view.findViewById(R.id.gridview);
        tvNull = view.findViewById(R.id.tvNull);
        getTodayActivityList();
        return view;
    }

    private void getTodayActivityList(){
        System.out.println("MASUKTRAINING");
        progressDialogHelper.showProgressDialog(getActivity(), "Getting data...");
        AndroidNetworking.get("https://testapi.digitalevent.id/lms/api/materi?order[BEGDA]=asc")
                .addHeaders("Accept","application/json")
                .addHeaders("Content-Type","application/json")
                .addHeaders("Authorization","Bearer "+session.getTokenLdap())
                //.addJSONObjectBody(body)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // do anything with response
                        System.out.println("RESPONTRAINING"+response);
                        try {
                            listModel = new ArrayList<KnowledgeModel>();
                            JSONArray jsonArray = response.getJSONArray("data");
                            System.out.println(jsonArray.length()+"PANJANGDATA");
                            if (jsonArray.length()==0) {
                                tvNull.setVisibility(View.VISIBLE);
                                gridView.setVisibility(View.GONE);
                            } else {
                                tvNull.setVisibility(View.GONE);
                                gridView.setVisibility(View.VISIBLE);
                                for (int a = 0; a < jsonArray.length(); a++) {
                                    JSONObject object = jsonArray.getJSONObject(a);
                                    String begin_date = object.getString("begin_date");
                                    String end_date = object.getString("end_date");
                                    String materi_id = object.getString("materi_id");
                                    String materi_name = object.getString("materi_name");
                                    String selling_price = object.getString("selling_price");
                                    String purchase_price = object.getString("purchase_price");
                                    String change_date = object.getString("change_date");
                                    String change_user = object.getString("change_user");
                                    String business_code = object.getJSONObject("business_code").getString("business_code");
                                    String company_name = object.getJSONObject("business_code").getString("company_name");
                                    String address = object.getString("address");
                                    model = new KnowledgeModel(begin_date,end_date,materi_id,materi_name,"",selling_price,purchase_price,change_date,change_user,business_code,company_name,address);
                                    listModel.add(model);
                                }
                                adapter = new KnowledgeAdapter(getActivity(), listModel);
                                gridView.setAdapter(adapter);
                                gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                                        if (listModel.get(position).getAddress().substring(listModel.get(position).getAddress().length()-3).equals("pdf")) {

                                            Intent i = new Intent(Intent.ACTION_VIEW);
                                            i.setData(Uri.parse("https://testapi.digitalevent.id/lms/api/materistream?materi_id="+listModel.get(position).getMateri_id()+"&token="+session.getTokenLdap()));
                                            startActivity(i);
                                        } else {
                                            Intent a = new Intent(getActivity(), VideoActivity.class);
                                            a.putExtra("url","https://testapi.digitalevent.id/lms/api/materistream?materi_id="+listModel.get(position).getMateri_id()+"&token="+session.getTokenLdap());
                                            startActivity(a);
                                        }
                                    }
                                });
                            }
                            progressDialogHelper.dismissProgressDialog(getActivity());
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
