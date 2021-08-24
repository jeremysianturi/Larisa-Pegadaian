package id.co.pegadaian.diarium.controller.home.main_menu.myevent.ems.presence;

import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Display;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;

import id.co.pegadaian.diarium.R;
import id.co.pegadaian.diarium.util.UserSessionManager;
import id.co.pegadaian.diarium.util.qrcode.Contents;
import id.co.pegadaian.diarium.util.qrcode.QRCodeEncoder;

public class PresenceActivity extends AppCompatActivity {
    private TextView tvName, txt_scan;
    private ImageView img_qr;
    private UserSessionManager session;
    private Typeface font, fontbold;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_presence2);
        session = new UserSessionManager(getApplicationContext());
        txt_scan = findViewById(R.id.txt_scan);
        tvName = findViewById(R.id.tvTitle);
//        tvCompany = findViewById(R.id.tvCompany);
//        tvJob = findViewById(R.id.tvJob);
        img_qr = findViewById(R.id.img_qr);
        txt_scan.setTypeface(fontbold);

        tvName.setTypeface(fontbold);
//        tvCompany.setTypeface(fontbold);
//
//        tvJob.setTypeface(fontbold);

        String isiQR = session.getServerURL()+"users/eventEMS/eventid/"+session.getEventId()+"/presence/buscd/"+session.getUserBusinessCode()+"/nik/"+session.getUserNIK();
        generateQRCode(isiQR, img_qr);

        tvName.setText(session.getUserFullName());
//        tvCompany.setText(session.getUserBusinessCode());
//        tvJob.setText(session.getJob());

//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);

//        ActionBar actionBar = getSupportActionBar();
//        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor(session.getEventColoRPrimary())));
//        actionBar.setTitle("My QR Code");
//        actionBar.setHomeAsUpIndicator(R.drawable.back_panah);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Presence");
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void generateQRCode(String qrInputText, ImageView myImage) {
        //Find screen size
        WindowManager manager = (WindowManager) getSystemService(WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);
        int width = point.x;
        int height = point.y;
        int smallerDimension = width < height ? width : height;
        smallerDimension = smallerDimension * 3 / 4;

        //Encode with a QR Code image
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
