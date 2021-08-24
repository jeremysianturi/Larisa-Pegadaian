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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import id.co.pegadaian.diarium.R;
import id.co.pegadaian.diarium.controller.home.main_menu.employee_corner.HistorycornerActivity;
import id.co.pegadaian.diarium.model.DetailCornerModel;
import id.co.pegadaian.diarium.util.UserSessionManager;

public class DetailCornerAdapter extends BaseAdapter {
    private Context mContext;
    private DetailCornerModel model;
    private List<DetailCornerModel> listModel;
    private TextView tvTitle, tvBatchStart, tvBatchEnd, tvDate, tvDesc, tvKuota;
    private ImageView ivObat;
    private Button btnBooking;
    private LinearLayout pesan;
    UserSessionManager session;
    String generatedPostId;
    Dialog dialog;
    String type, book_code_get;
    public DetailCornerAdapter(Context mContext, List<DetailCornerModel> listModel, String tipe, String book_code_get) {
        this.mContext = mContext;
        this.listModel = listModel;
        this.type = tipe;
        this.book_code_get = book_code_get;
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
            view = inflater.inflate(R.layout.item_detail_corner, null);
        }
        tvTitle = view.findViewById(R.id.tvThemeName);
        tvKuota = view.findViewById(R.id.kapasitas);
        tvBatchStart = view.findViewById(R.id.datestart);
        tvBatchEnd = view.findViewById(R.id.dateend);
        tvDate = view.findViewById(R.id.date);
        btnBooking = view.findViewById(R.id.btn_book);
        tvTitle.setText(model.getBatch_name());
        tvKuota.setText("slot available : "+model.getKuota_available());
        tvBatchStart.setText(model.getStart_batch());
        tvBatchEnd.setText(model.getEnd_batch());
        tvDate.setText("Date : "+model.getEmpcorner_date());
        btnBooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Integer.valueOf(listModel.get(position).getKuota_available())<=0) {
                    Toast.makeText(mContext, "Quota not available for this session!", Toast.LENGTH_SHORT).show();
                } else {
//                Toast.makeText(mContext, "corner id : "+listModel.get(position).getEmpcorner_id()+" - "+"batch id : "+listModel.get(position).getBatch_id()+" - "+"Batch name : "+listModel.get(position).getBatch_name()+" - ", Toast.LENGTH_SHORT).show();
                    generatePostId(listModel.get(position).getEmpcorner_id(), listModel.get(position).getBatch_id(), listModel.get(position).getEmpcorner_date());
                }
            }
        });
        return view;
    }


    private void generatePostId(final String ecrid, final String batchid, final String date) {
        SimpleDateFormat tgl = new SimpleDateFormat("yyyy-MM-dd");
        String tRes = tgl.format(new Date());
//        progressDialogHelper.showProgressDialog(CheckinActivity.this, "Getting data...");
        AndroidNetworking.get(session.getServerURL() + "users/generateIDBooking?")
                .addHeaders("Accept", "application/json")
                .addHeaders("Content-Type", "application/json")
                .addHeaders("Authorization", session.getToken())
                //.addJSONObjectBody(body)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println("RESPONBOOKINGID" + response);
                        // do anything with response
                        try {
                            if (response.getInt("status") == 200) {
                                generatedPostId = response.getJSONObject("data").getString("id");
                                popUpBooking(generatedPostId,ecrid, batchid, date);
//                                session.setGeneratedPostId(id);
                            } else {
//                                popUpLogin();
                            }
                            System.out.println("status ya : " + session.getStat());
//                            progressDialogHelper.dismissProgressDialog(CheckinActivity.this);
                        } catch (Exception e) {
//                            progressDialogHelper.dismissProgressDialog(CheckinActivity.this);
                            System.out.println(e);
                        }
                    }

                    @Override
                    public void onError(ANError error) {
//                        progressDialogHelper.dismissProgressDialog(CheckinActivity.this);
                        System.out.println(error);
                    }
                });
    }

    private void popUpBooking(final String generateEmpId, final String ecrid, final String batchid, final String date) {
        dialog = new Dialog(mContext);
        dialog.setContentView(R.layout.layout_confirm_booking);
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
                if (type.equals("re")) {
                    sendBooking(book_code_get, ecrid, batchid, date);
                } else {
                    sendBooking(generateEmpId, ecrid, batchid, date);
                }
            }
        });

    }

    private void sendBooking(String ecbc, String ecrid, String batch, String date) {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat tgl = new SimpleDateFormat("yyyy-MM-dd");
        String tRes = tgl.format(new Date());

        @SuppressLint("SimpleDateFormat") SimpleDateFormat jam = new SimpleDateFormat("HH:mm");
        String jamRes = jam.format(new Date());

//        JSONObject jResult = new JSONObject();// main object
//        JSONArray jArray = new JSONArray();// /ItemDetail jsonArray
            JSONObject jGroup = new JSONObject();// /sub Object
            try {
                jGroup.put("begin_date", tRes);
//                jGroup.put("end_date", "9999-12-31");
                jGroup.put("business_code", session.getUserBusinessCode());
                jGroup.put("personal_number", session.getUserNIK());
                jGroup.put("book_code", ecbc);
                jGroup.put("empcorner_id", ecrid);
                jGroup.put("book_date", date);
                jGroup.put("batch_id", batch);
                jGroup.put("change_date", date);
                jGroup.put("change_user", session.getUserNIK());
                jGroup.put("book_status", "B");
//                jArray.put(jGroup);
                // /itemDetail Name is JsonArray Name
//                jResult.put("", jGroup);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        System.out.println(jGroup + "PARAMBOOKINGEMPLOYEECORNER");
        AndroidNetworking.post(session.getServerURL()+"users/employeeCornerBooking/ecbcd/"+ecbc+"/btcid/"+batch+"/buscd/"+session.getUserBusinessCode())
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
                        System.out.println(response+"RESPONBOOKINGEMPLOYEECORNER");
                        try {
                            if(response.getInt("status")==200){
                                Toast.makeText(mContext, "Success Booking", Toast.LENGTH_SHORT).show();
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