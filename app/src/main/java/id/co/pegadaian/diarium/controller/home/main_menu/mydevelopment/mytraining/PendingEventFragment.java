package id.co.pegadaian.diarium.controller.home.main_menu.mydevelopment.mytraining;


import android.app.Dialog;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import id.co.pegadaian.diarium.R;
import id.co.pegadaian.diarium.adapter.TrainingTrainerAdapter;
import id.co.pegadaian.diarium.model.TrainingTrainerModel;
import id.co.pegadaian.diarium.util.UserSessionManager;
import id.co.pegadaian.diarium.util.element.ProgressDialogHelper;

/**
 * A simple {@link Fragment} subclass.
 */
public class PendingEventFragment extends Fragment {


    private ProgressDialogHelper progressDialogHelper;
    UserSessionManager session;
    private List<TrainingTrainerModel> listModelTrainer;
    private TrainingTrainerModel modelTrainer;
    private TrainingTrainerAdapter adapterTrainer;
    TextView tvNull;
    ListView listActivity;
    Typeface font,fontbold;
    Dialog dialog;

    public PendingEventFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pending_event, container, false);
        progressDialogHelper = new ProgressDialogHelper();
        session = new UserSessionManager(getActivity());
        font = Typeface.createFromAsset(getActivity().getAssets(),"fonts/Nexa Light.otf");
        fontbold = Typeface.createFromAsset(getActivity().getAssets(),"fonts/Nexa Bold.otf");
        listActivity = view.findViewById(R.id.listOnGoing);
        tvNull = view.findViewById(R.id.tvNull);
        getEventTrainer();
        return view;
    }


    private void getEventTrainer(){
        System.out.println("NOTAPPROVEEVENT");
        progressDialogHelper.showProgressDialog(getActivity(), "Getting data...");
        AndroidNetworking.get("https://testapi.digitalevent.id/lms/api/myevent/trainer?tr_id=2&stat_relat=ST03")
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
                        System.out.println("RESPONTRAININGONGOING"+response);
                        try {
                            listModelTrainer = new ArrayList<TrainingTrainerModel>();
                            JSONArray jsonArray = response.getJSONArray("data");
                            System.out.println(jsonArray.length()+"PANJANGDATA");
                            if (jsonArray.length()==0) {
                                tvNull.setVisibility(View.VISIBLE);
                                listActivity.setVisibility(View.GONE);
                            } else {
                                tvNull.setVisibility(View.GONE);
                                listActivity.setVisibility(View.VISIBLE);
                                for (int a = 0; a < jsonArray.length(); a++) {
                                    JSONObject object = jsonArray.getJSONObject(a);
                                    String BEGDA = object.getString("BEGDA");
                                    String ENDDA = object.getString("ENDDA");
                                    String BUSCD = object.getString("BUSCD");
                                    String PERNR = object.getString("PERNR");
                                    String TRAID = object.getString("TRAID");
                                    String TRNAM = object.getString("TRNAM");
                                    String STTAR = object.getString("STTAR");
                                    String BUSC1 = object.getString("BUSC1");
                                    String CHGDT = object.getString("CHGDT");
                                    String CHUSR = object.getString("CHUSR");
                                    String trainer_name = object.getString("trainer_name");
                                    String schedule_id  = object.getString("schedule_id");
                                    String schedule_name = object.getString("schedule_name");
                                    String session_id = object.getString("session_id");
                                    String topic = object.getString("topic");
                                    String begin_time = object.getString("begin_time");
                                    String end_time  = object.getString("end_time");
                                    String day_number = object.getString("day_number");
                                    String batch_name = object.getString("batch_name");
                                    String batch = object.getString("batch");
                                    String session_name = object.getString("session_name");
                                    String event_id = object.getString("event_id");
                                    String event_name = object.getString("event_name");
                                    String event_status = object.getString("event_status");
                                    String situation_code = object.getString("situation_code");
                                    String situation_name = object.getString("situation_name");
                                    String event_stat_id = object.getString("event_stat_id");
                                    modelTrainer = new TrainingTrainerModel(BEGDA, ENDDA, BUSCD, PERNR, TRAID, TRNAM, STTAR, BUSC1, CHGDT, CHUSR, trainer_name, schedule_id, schedule_name, session_id, topic, begin_time, end_time, day_number, batch_name, batch, session_name, event_id, event_name, event_status, situation_code,situation_name, event_stat_id);
                                    listModelTrainer.add(modelTrainer);
                                }
                                adapterTrainer = new TrainingTrainerAdapter(getActivity(), listModelTrainer);
                                listActivity.setAdapter(adapterTrainer);
                                listActivity.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                                        popupApprove();
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


    private void popupApprove() {
        dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.confirm_approve_eventlms);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        dialog.getWindow().setAttributes(lp);
        dialog.setTitle("Input Code Here");
        Button btnYes =(Button) dialog.findViewById(R.id.btnYes);
        Button btnNo =(Button) dialog.findViewById(R.id.btnNo);
        dialog.show();
        dialog.setCancelable(false);
        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Approved Success", Toast.LENGTH_SHORT).show();
            }
        });

    }

}
