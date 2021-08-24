package id.co.pegadaian.diarium.controller.home.main_menu.mydevelopment.mytraining;


import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import org.json.JSONArray;
import org.json.JSONObject;
import id.co.pegadaian.diarium.R;
import id.co.pegadaian.diarium.util.UserSessionManager;
import id.co.pegadaian.diarium.util.qrcode.Contents;
import id.co.pegadaian.diarium.util.qrcode.QRCodeEncoder;

import static android.content.Context.WINDOW_SERVICE;

/**
 * A simple {@link Fragment} subclass.
 */
public class AbsenFragment extends Fragment {
    private TextView tvName, txt_scan;
    private ImageView img_qr;
    private UserSessionManager session;
    private Typeface font, fontbold;

    public AbsenFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_absen, container, false);
        session = new UserSessionManager(getActivity());
        txt_scan = view.findViewById(R.id.txt_scan);
        tvName = view.findViewById(R.id.tvTitle);
        img_qr = view.findViewById(R.id.img_qr);
        txt_scan.setTypeface(fontbold);
        tvName.setTypeface(fontbold);
        getMateri();
        return view;
    }

    private void getMateri(){
        AndroidNetworking.get("https://testapi.digitalevent.id/lms/api/account?type[]=PARID")
                .addHeaders("Accept","application/json")
                .addHeaders("Content-Type","application/json")
                .addHeaders("Authorization","Bearer "+session.getTokenLdap())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            String isiQR = "https://lmsmain.digitalevent.id/api/attedance/scanqr?parid=2&sesid=3&uid=";
                            String access_token = null;
                            String username = null;
                            for (int a = 0; a < response.length(); a++) {
                                JSONObject object = response.getJSONObject(a);
                                access_token = object.getString("access_token");
                                username = object.getString("username");
                            }
                            generateQRCode(isiQR+access_token, img_qr);
                            tvName.setText(username);
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

    private void generateQRCode(String qrInputText, ImageView myImage) {
        WindowManager manager = (WindowManager) getActivity().getSystemService(WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);
        int width = point.x;
        int height = point.y;
        int smallerDimension = width < height ? width : height;
        smallerDimension = smallerDimension * 3 / 4;
        QRCodeEncoder qrCodeEncoder = new QRCodeEncoder(qrInputText,
                null,
                Contents.Type.TEXT,
                BarcodeFormat.QR_CODE.toString(),
                smallerDimension);
        try {
            Bitmap bitmap = qrCodeEncoder.encodeAsBitmap();
            myImage.setImageBitmap(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }
}