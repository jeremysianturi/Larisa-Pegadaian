package id.co.pegadaian.diarium.util.reminder;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import id.co.pegadaian.diarium.R;
import id.co.pegadaian.diarium.controller.home.main_menu.mytime.checkin.CheckinActivity;
import id.co.pegadaian.diarium.model.OfficeCheckinModel;
import id.co.pegadaian.diarium.util.UserSessionManager;

public class ChooseOfficeDialog extends AppCompatDialogFragment {
    Spinner mSpinner;
    UserSessionManager session;
    private OfficeCheckinModel officeModel;
    private ArrayList<String> officeCheckin  = new ArrayList<>();
    private ArrayList<String> officeCheckinId  = new ArrayList<>();
    String siteID;
    CheckinActivity mCheckinActivity;
    LinearLayout lnForthCheckinAct, lnFifthCheckinAct;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        session = new UserSessionManager(getContext());
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.choose_office_dialoh, null);
        AndroidNetworking.initialize(getActivity().getApplicationContext());

        mCheckinActivity = new CheckinActivity();

//        mSpinner = view.findViewById(R.id.spinner_dialog);
        getOffice();

        builder
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        session.setSiteId(siteID);
                        System.out.println("siteIDIDID2" + session.getSiteId());
                        dialog.dismiss();

                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        builder.setView(view);
        AlertDialog dialog = builder.create();
        dialog.show();

//        dialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });

        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                siteID = officeCheckinId.get(position);
//                session.setSiteId(siteID);
//                System.out.println("siteIDIDID" + session.getSiteId());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return dialog;
    }

    private void getOffice(){
        AndroidNetworking.get(session.getServerURL()+"users/presensi/getlocation?personal_number="+session.getLoginUsername())
                .addHeaders("Accept","application/json")
                .addHeaders("Content-Type","application/json")
                .addHeaders("Authorization",session.getToken())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println("Tes Get Office" + response);
                        try {
                            if(response.getInt("status")==200){
//                                session.setToken(response.getString("token"));
                                officeCheckin = new ArrayList<String>();
                                JSONArray jsonArray = response.getJSONArray("data");
                                if(jsonArray.length()>0){
                                    for (int i=0; i<jsonArray.length(); i++) {
                                        JSONObject obj = jsonArray.getJSONObject(i);
                                        String buildingID = obj.getString("building_id");
                                        String buildingName = obj.getString("building_name");

                                        officeModel = new OfficeCheckinModel(buildingName,buildingID);
                                        officeCheckin.add(buildingName);
                                        officeCheckinId.add(buildingID);
                                        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getContext(), R.layout.spinner_item_list, officeCheckin);
                                        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
                                        mSpinner.setAdapter(spinnerArrayAdapter);
                                    }
                                }
//                                spinnerCheckinAdapter = new OfficeCheckinAdapter(CheckinActivity.this,spinnerCheckinList);
                            }
                            else{
                            }

                        } catch (Exception e){
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {

                    }
                });
    }



    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);

    }

}
