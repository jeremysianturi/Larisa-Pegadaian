package id.co.pegadaian.diarium.adapter;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import id.co.pegadaian.diarium.R;
import id.co.pegadaian.diarium.controller.home.main_menu.employee_corner.BookingcornerActivity;
import id.co.pegadaian.diarium.controller.home.main_menu.employee_corner.HistorycornerActivity;
import id.co.pegadaian.diarium.controller.home.main_menu.employee_corner.ScannerFloorActivity;
import id.co.pegadaian.diarium.model.BookingemployeeCornerModel;
import id.co.pegadaian.diarium.util.UserSessionManager;


/**
 * Created by LENOVO on 29/09/2017.
 */

public class HistorybookingAdapter extends BaseAdapter {
    private Context mContext;
    private BookingemployeeCornerModel model;
    private List<BookingemployeeCornerModel> listModel;
    private TextView tvKodeBooking, tvBookDate, tvName, tvTime, tvPlace, tvBatch;
    private ImageView ivObat;
    private LinearLayout pesan;
    Button btnCancel, btnReschedule, btnCheckin;
    UserSessionManager session;
    Dialog dialog;

    public HistorybookingAdapter(Context mContext, List<BookingemployeeCornerModel> listModel) {
        this.mContext = mContext;
        this.listModel = listModel;
        session = new UserSessionManager(mContext);
    }

    @Override
    public int getCount() {
        return listModel.size();
    }

    @Override
    public Object getItem(int position) {
        return listModel.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {
        model = listModel.get(position);
        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            view = inflater.inflate(R.layout.item_list_historybooking, null);
        }

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date value = null;
        try {
            value = formatter.parse(listModel.get(position).getBook_date());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat dateFormatter = new SimpleDateFormat("dd MMM yyyy");
        dateFormatter.setTimeZone(TimeZone.getDefault());
        String dt = dateFormatter.format(value);

        tvKodeBooking = view.findViewById(R.id.kodebooking);
        tvBookDate = view.findViewById(R.id.tvDate);
        tvName = view.findViewById(R.id.tvTitle);
        tvTime = view.findViewById(R.id.tvTime);
        tvPlace = view.findViewById(R.id.tvPlace);
        tvBatch = view.findViewById(R.id.tvBatch);
        tvKodeBooking.setText(listModel.get(position).getBook_code());
        tvBookDate.setText("Date : "+dt);
        tvName.setText("Created By : "+listModel.get(position).getFull_name());
        tvTime.setText("Time : "+listModel.get(position).getStart_batch()+" - "+listModel.get(position).getEnd_batch());
        tvPlace.setText("Place : "+listModel.get(position).getBuild_code()+" - "+listModel.get(position).getEmpcorner_loc());
        tvBatch.setText("Batch : "+listModel.get(position).getBatch_name());
        btnCancel = view.findViewById(R.id.btn_cancel);
        btnReschedule = view.findViewById(R.id.btn_reschedule);
        btnCheckin = view.findViewById(R.id.btn_checkin);
        if (listModel.get(position).getBook_status().equals("B")) {
            btnCancel.setVisibility(View.VISIBLE);
            btnReschedule.setVisibility(View.VISIBLE);
            btnCheckin.setVisibility(View.VISIBLE);
            btnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String book_code = listModel.get(position).getBook_code();
                    String ecrid = listModel.get(position).getEmpcorner_id();
                    String batch_id = listModel.get(position).getBatch_id();
                    String book_date = listModel.get(position).getBook_date();
                    popCancel(book_code, ecrid, batch_id, book_date);
                }
            });
            btnReschedule.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String theme_name = listModel.get(position).getTheme_name();
                    popReschedule(listModel.get(position).getBook_code(), theme_name);
                }
            });

            btnCheckin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SimpleDateFormat s = new SimpleDateFormat("yyyyMMddHHmmss");
                    String format = s.format(new Date());
                    String tgl = listModel.get(position).getBook_date()+listModel.get(position).getStart_batch();
                    String result = tgl.replaceAll("[|?*<\":>+\\[\\]/']", "");
                    System.out.println(format+" CURRENTTIMESTAMP "+result);
                    Intent i = new Intent(mContext, ScannerFloorActivity.class);
                    i.putExtra("title", listModel.get(position).getTheme_name());
                    i.putExtra("theme_id", listModel.get(position).getTheme_id());
                    i.putExtra("book_code", listModel.get(position).getBook_code());
                    i.putExtra("empcorner_id", listModel.get(position).getEmpcorner_id());
                    i.putExtra("batch_id", listModel.get(position).getBatch_id());
                    i.putExtra("date", listModel.get(position).getBook_date());
                    mContext.startActivity(i);
                }
            });
        } else {
            btnCancel.setVisibility(View.INVISIBLE);
            btnReschedule.setVisibility(View.INVISIBLE);
            btnCheckin.setVisibility(View.INVISIBLE);
        }

        return view;
    }

    private void popReschedule(final String booking_code, final String theme_name) {
        dialog = new Dialog(mContext);
        dialog.setContentView(R.layout.layout_confirm_reschedule);
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
                Intent i = new Intent(mContext, BookingcornerActivity.class);
                i.putExtra("type", "re");
                i.putExtra("booking_code", booking_code);
                i.putExtra("theme_name", theme_name);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                mContext.startActivity(i);
                ((Activity)mContext).finish();
                dialog.dismiss();
            }
        });

    }

    private void popCancel(final String book_code, final String ecrid, final String batch_id, final String book_date) {
        dialog = new Dialog(mContext);
        dialog.setContentView(R.layout.layout_confirm_cancel);
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
                cancelEmployeeCorner(book_code, ecrid, batch_id, book_date);
            }
        });
    }

    private void cancelEmployeeCorner(String book_code, String ecrid, String batch_id, String book_date) {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat tgl = new SimpleDateFormat("yyyy-MM-dd");
        String tRes = tgl.format(new Date());

        @SuppressLint("SimpleDateFormat") SimpleDateFormat jam = new SimpleDateFormat("HH:mm");
        String jamRes = jam.format(new Date());

        JSONObject jGroup = new JSONObject();// /sub Object
        try {
            jGroup.put("begin_date", tRes);
//                jGroup.put("end_date", "9999-12-31");
            jGroup.put("business_code", session.getUserBusinessCode());
            jGroup.put("personal_number", session.getUserNIK());
            jGroup.put("book_code", book_code);
            jGroup.put("empcorner_id", ecrid);
            jGroup.put("book_date", book_date);
            jGroup.put("batch_id", batch_id);
            jGroup.put("change_date", tRes);
            jGroup.put("change_user", session.getUserNIK());
            jGroup.put("book_status", "C");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        System.out.println(jGroup + "JSON BOOKING CANCEL");
        AndroidNetworking.post(session.getServerURL()+"users/employeeCornerBooking/ecbcd/"+book_code+"/btcid/"+batch_id+"/buscd/"+session.getUserBusinessCode())
                .addHeaders("Accept","application/json")
                .addHeaders("Content-Type","application/json")
                .addHeaders("Authorization",session.getToken())
                .addJSONObjectBody(jGroup)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // do anything with response
                        System.out.println(response+"BOOKINGCANCEL");
                        try {
                            if(response.getInt("status")==200){
                                Toast.makeText(mContext, "Success Cancel", Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(mContext, HistorycornerActivity.class);
                                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                mContext.startActivity(i);
                                ((Activity)mContext).finish();
                                dialog.dismiss();
                            }else {
                                Toast.makeText(mContext,"Failed!",Toast.LENGTH_SHORT).show();
                            }
                        }catch (Exception e){
                            System.out.println(e);
                        }
                    }
                    @Override
                    public void onError(ANError error) {
                        System.out.println(error);
                    }
                });
    }

}
