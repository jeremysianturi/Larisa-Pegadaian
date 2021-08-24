package id.co.pegadaian.diarium.controller.home.main_menu.employee_corner;


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
import id.co.pegadaian.diarium.adapter.HistorybookingAdapter;
import id.co.pegadaian.diarium.model.BookingemployeeCornerModel;
import id.co.pegadaian.diarium.util.UserSessionManager;
import id.co.pegadaian.diarium.util.element.ProgressDialogHelper;

/**
 * A simple {@link Fragment} subclass.
 */
public class ECNotCheckInFragment extends Fragment {

    UserSessionManager session;
    private List<BookingemployeeCornerModel> listModel;
    private BookingemployeeCornerModel model;
    private HistorybookingAdapter adapter;
    private TextView tvNull;
    ListView listBooking;
    private ProgressDialogHelper progressDialogHelper;
    Typeface font,fontbold;
    public ECNotCheckInFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_ecnot_check_in, container, false);
        session = new UserSessionManager(getActivity());
        progressDialogHelper = new ProgressDialogHelper();
        font = Typeface.createFromAsset(getActivity().getAssets(),"fonts/Nexa Light.otf");
        fontbold = Typeface.createFromAsset(getActivity().getAssets(),"fonts/Nexa Bold.otf");
        listBooking = view.findViewById(R.id.list_available);
        tvNull = view.findViewById(R.id.tvNull);
        getMyBooking();
        return view;

    }

    private void getMyBooking(){
        SimpleDateFormat tgl = new SimpleDateFormat("yyyy-MM-dd");
        String tRes = tgl.format(new Date());
        progressDialogHelper.showProgressDialog(getActivity(), "Getting data...");
        AndroidNetworking.get(session.getServerURL()+"users/showallbooking/themeid/"+session.getThemeId()+"/bookstatus/F/buscd/"+session.getUserBusinessCode())
                .addHeaders("Accept","application/json")
                .addHeaders("Content-Type","application/json")
                .addHeaders("Authorization",session.getToken())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println("RESPONAVAILABLE"+response);
                        try {
                            if(response.getInt("status")==200){
                                listModel = new ArrayList<BookingemployeeCornerModel>();
                                JSONArray jsonArray = response.getJSONArray("data");
                                for (int a = 0; a < jsonArray.length(); a++) {
                                    JSONObject object = jsonArray.getJSONObject(a);
                                    String batch_name = null;
                                    String start_batch = null;
                                    String end_batch = null;
                                    String build_code = null;
                                    String empcorner_loc = null;
                                    String theme_id = null;
                                    String theme_name = null;
                                    String full_name = null;
                                    String begin_date = null;
                                    String business_code = null;
                                    String personal_number = null;
                                    String book_code = null;
                                    String empcorner_id = null;
                                    String book_date = null;
                                    String batch_id = null;
                                    String change_date = null;
                                    String change_user = null;
                                    String book_status = null;
                                    build_code = object.getString("build_code");
                                    empcorner_loc = object.getString("empcorner_loc");
                                    JSONArray theme_array = object.getJSONArray("theme");
                                    for (int d=0; d<theme_array.length(); d++) {
                                        JSONObject object_theme = theme_array.getJSONObject(d);
                                        theme_id = object_theme.getString("theme_id");
                                        theme_name = object_theme.getString("theme_name");
                                    }
                                    JSONArray full_name_array = object.getJSONArray("full_name");
                                    for (int e=0; e<theme_array.length(); e++) {
                                        JSONObject object_full_name = full_name_array.getJSONObject(e);
                                        full_name = object_full_name.getString("full_name");
                                    }
                                    JSONArray booking_array = object.getJSONArray("booking");
                                    for (int l=0; l<booking_array.length();l++) {
                                        JSONObject object_booking = booking_array.getJSONObject(l);
                                        begin_date = object_booking.getString("begin_date");
                                        business_code = object_booking.getString("business_code");
                                        personal_number = object_booking.getString("personal_number");
                                        book_code = object_booking.getString("book_code");
                                        empcorner_id = object_booking.getString("empcorner_id");
                                        book_date = object_booking.getString("book_date");
                                        batch_id = object_booking.getString("batch_id");
                                        change_date = object_booking.getString("change_date");
                                        change_user = object_booking.getString("change_user");
                                        book_status = object_booking.getString("book_status");
                                        JSONArray batch_name_array = object_booking.getJSONArray("batch_name");
                                        for (int b=0; b<batch_name_array.length(); b++) {
                                            JSONObject object_batch_name = batch_name_array.getJSONObject(b);
                                            batch_name = object_batch_name.getString("batch_name");
                                            start_batch = object_batch_name.getString("start_batch");
                                            end_batch = object_batch_name.getString("end_batch");
                                            model = new BookingemployeeCornerModel(begin_date, business_code, personal_number, book_code, empcorner_id, book_date, batch_id, change_date, change_user, book_status, batch_name, start_batch, end_batch, build_code, empcorner_loc, theme_id, theme_name, full_name);
                                            listModel.add(model);
                                        }
                                    }
                                }
                                if (listModel.size()==0) {
                                    listBooking.setVisibility(View.GONE);
                                    tvNull.setVisibility(View.VISIBLE);
                                } else {
                                    listBooking.setVisibility(View.VISIBLE);
                                    tvNull.setVisibility(View.GONE);
                                    adapter = new HistorybookingAdapter(getActivity(), listModel);
                                    listBooking.setAdapter(adapter);
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