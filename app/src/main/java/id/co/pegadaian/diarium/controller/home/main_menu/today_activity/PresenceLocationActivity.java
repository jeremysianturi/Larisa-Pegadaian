package id.co.pegadaian.diarium.controller.home.main_menu.today_activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import id.co.pegadaian.diarium.R;

public class PresenceLocationActivity extends AppCompatActivity implements MenuItem.OnMenuItemClickListener, OnMapReadyCallback {
    private GoogleMap mMap;
    String lokasi, tipe;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_presence_location);
        Intent intent = getIntent();
        lokasi = intent.getStringExtra("location");
        tipe = intent.getStringExtra("tipe");
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        return false;
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        mMap = googleMap;
//        progressDialogHelper.showProgressDialog(CheckinActivity.this, "Getting data...");
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap.setMyLocationEnabled(true);
        mMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location location) {
                String[] latlong =  lokasi.split(",");
                double latitude = Double.parseDouble(latlong[0]);
                double longitude = Double.parseDouble(latlong[1]);
                MarkerOptions mp = new MarkerOptions();
                CameraUpdate center = CameraUpdateFactory.newLatLng(new LatLng(latitude, longitude));
                CameraUpdate zoom = CameraUpdateFactory.zoomTo(15);
                MarkerOptions marker = new MarkerOptions().position(new LatLng(latitude, longitude)).title("Your Location "+tipe);
//                mMap.addMarker(mp);
                mMap.moveCamera(center);
                mMap.animateCamera(zoom);
                googleMap.addMarker(marker);
            }

        });

    }
}
