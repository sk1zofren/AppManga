package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.FrameLayout;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class Map extends AppCompatActivity implements OnMapReadyCallback  {

    GoogleMap gMap;
    GoogleMap comicMap;
    GoogleMap MIAMap;
    FrameLayout map;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.maps);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        this.gMap = googleMap;
        LatLng mapLouv = new LatLng(50.483055579479846, 4.182554791926987);
        this.gMap.addMarker(new MarkerOptions().position(mapLouv).title("Winter Geek Festival"));
        this.gMap.moveCamera(CameraUpdateFactory.newLatLng(mapLouv));

        this.comicMap = googleMap;
        LatLng mapComic = new LatLng(50.86434772782046, 4.34721656092712);
        this.comicMap.addMarker(new MarkerOptions().position(mapComic).title("Comic Con"));
        this.comicMap.moveCamera(CameraUpdateFactory.newLatLng(mapComic));

        this.MIAMap = googleMap;
        LatLng mapMIA = new LatLng(50.899750541183735, 4.337251640384408);
        this.MIAMap.addMarker(new MarkerOptions().position(mapMIA).title("Made In Asia"));
        this.MIAMap.moveCamera(CameraUpdateFactory.newLatLng(mapMIA));




    }
}