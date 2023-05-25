package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.FrameLayout;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class Map extends AppCompatActivity implements OnMapReadyCallback {

    GoogleMap gMap;
    FrameLayout map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        // Get the map fragment from the layout
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.maps);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        this.gMap = googleMap;

        // Set the markers for different locations
        LatLng mapLouv = new LatLng(50.483055579479846, 4.182554791926987);
        gMap.addMarker(new MarkerOptions().position(mapLouv).title("Winter Geek Festival"));

        LatLng mapComic = new LatLng(50.86434772782046, 4.34721656092712);
        gMap.addMarker(new MarkerOptions().position(mapComic).title("Comic Con"));

        LatLng mapMIA = new LatLng(50.899750541183735, 4.337251640384408);
        gMap.addMarker(new MarkerOptions().position(mapMIA).title("Made In Asia"));

        // Move the camera to the default location and set zoom level
        gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mapLouv, 10));

        // Set the click listener for info windows of markers
        gMap.setOnInfoWindowClickListener(marker -> {
            String url = "";
            switch (marker.getTitle()) {
                case "Winter Geek Festival":
                    url = "https://www.wintergeekfestival.be/";
                    break;
                case "Comic Con":
                    url = "https://comicconbrussels.com/fr/homepage-fr/";
                    break;
                case "Made In Asia":
                    url = "https://www.madeinasia.be/";
                    break;
            }
            if (!url.isEmpty()) {
                // Open the URL in a browser
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(browserIntent);
            }
        });
    }
}
