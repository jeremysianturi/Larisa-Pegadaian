package id.co.pegadaian.diarium.controller.home.main_menu.mytime.checkin;

import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import id.co.pegadaian.diarium.R;

public class CheckinTempActivity extends AppCompatActivity implements OnMapReadyCallback {
    BottomSheetBehavior sheetBehavior;
    LinearLayout layoutBottomSheet;
    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkin_temp);
        layoutBottomSheet = (LinearLayout) findViewById(R.id.bottom_sheet);
        sheetBehavior = BottomSheetBehavior.from(layoutBottomSheet);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        /**
         * bottom sheet state change listener
         * we are changing button text when sheet changed state
         * */
        sheetBehavior.setHideable(false);//Important to add
        sheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                switch (newState) {
//                    case BottomSheetBehavior.STATE_HIDDEN:
//                        break;
                    case BottomSheetBehavior.STATE_EXPANDED: {
                    }
                    break;
                    case BottomSheetBehavior.STATE_COLLAPSED: {
                    }
                    break;
                    case BottomSheetBehavior.STATE_DRAGGING:
                        break;
                    case BottomSheetBehavior.STATE_SETTLING:
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });
//        btnBottomSheet.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (sheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
//                    sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
//                    btnBottomSheet.setText("Close sheet");
//                } else {
//                    sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
//                    btnBottomSheet.setText("Expand sheet");
//                }
//            }
//        });

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap.setMyLocationEnabled(true);
        mMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location location) {
                CameraUpdate center = CameraUpdateFactory.newLatLng(new LatLng(location.getLatitude(), location.getLongitude()));
                CameraUpdate zoom = CameraUpdateFactory.zoomTo(15);
                mMap.clear();
                MarkerOptions mp = new MarkerOptions();
                mp.position(new LatLng(location.getLatitude(), location.getLongitude()));
                String lat = String.valueOf(location.getLatitude());
                String lon = String.valueOf(location.getLongitude());
                System.out.println(lat+", "+lon+" dkn3b4jr44");

                mp.title("Your Location");
                mMap.addMarker(mp);
                mMap.moveCamera(center);
                mMap.animateCamera(zoom);
            }

        });}

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}