package com.example.maplocation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;


import android.Manifest;
import android.app.FragmentManager;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback      {

    private static final int REQUEST_LOCATION = 1;
    private FragmentManager fragmentManager;
    private MapFragment mapFragment;
    private UiSettings mUiSettings;
    private GoogleMap googleMap;
    private  static double lat ;
    private static double lng ;
    private FusedLocationProviderClient fusedLocationClient;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fragmentManager = getFragmentManager();
        mapFragment = (MapFragment) fragmentManager.findFragmentById(R.id.googleMap);
        mapFragment.getMapAsync(this);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        LatLng location = new LatLng(lat, lng);
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.title("현재위치");
        markerOptions.snippet("여기");
        markerOptions.position(location);
        googleMap.addMarker(markerOptions);
        mUiSettings = googleMap.getUiSettings();
        mUiSettings.setZoomControlsEnabled(true);
        CircleOptions circle1km = new CircleOptions().center(location) ///원만들기
                .radius(1000)
                .strokeWidth(0) // 선넓
                .fillColor(Color.parseColor("#880000ff"));
        googleMap.addCircle(circle1km);
        //// 위치 허가
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION);
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    REQUEST_LOCATION);
        } else {

            Task<Location> locationResult = LocationServices
                    .getFusedLocationProviderClient(this /** Context */)
                    .getLastLocation();

        }
        googleMap.setMyLocationEnabled(true); // 위치버튼 추가
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location, 16));
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {    // 위치가 있을때
                            lat = location.getLatitude();
                            lng = location.getLongitude();
                            Log.e("TAG", "location sussess");    // 잘 됫으면 로그 출력
                            LatLng location1 = new LatLng(lat, lng);
                            MarkerOptions markerOptions1 = new MarkerOptions();
                            markerOptions1.title("현재위치");
                            markerOptions1.snippet("여기");
                            markerOptions1.position(location1);
                            googleMap.addMarker(markerOptions1);


                        }
                    }
                });

    }

}