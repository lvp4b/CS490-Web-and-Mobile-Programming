package com.github.lvp4b.icp_11;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;


import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

public class LocationActivity extends AppCompatActivity implements OnMapReadyCallback {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        final Geocoder geocoder = new Geocoder(this);

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat
                .checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            //show message or ask permissions from the user.
            Toast.makeText(this, "We need location permissions", Toast.LENGTH_SHORT).show();
            return;
        }

        googleMap.moveCamera(CameraUpdateFactory.zoomTo(7));

        StringBuilder userAddress = new StringBuilder();
        final LocationManager userCurrentLocation = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        userCurrentLocation.requestSingleUpdate(LocationManager.GPS_PROVIDER, new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();

                // Getting the address of the user based on latitude and longitude.
                try {
                    List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
                    if (addresses.isEmpty()) {
                        Toast.makeText(LocationActivity.this, "Could not determine your address", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    String city = addresses.get(0).getLocality();
                    String state = addresses.get(0).getAdminArea();
                    String country = addresses.get(0).getCountryName();
                    String postalCode = addresses.get(0).getPostalCode();
                    String knownName = addresses.get(0).getFeatureName();

                    userAddress.setLength(0);
                    userAddress.append(knownName + ", " + city + ", " + state + ", " + country + " " + postalCode).append("\t");
                    Toast.makeText(LocationActivity.this, userAddress, Toast.LENGTH_SHORT).show();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

                // Setting our image as the marker icon.
                LatLng userCurrentLocationCoordinates = new LatLng(latitude, longitude);
                googleMap.clear();
                googleMap.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude))
                        .title("Your current address.").snippet(userAddress.toString())
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_maps)));

                // Setting the zoom level of the map.
                googleMap.moveCamera(CameraUpdateFactory.newLatLng(userCurrentLocationCoordinates));
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            @Override
            public void onProviderEnabled(String provider) {
            }

            @Override
            public void onProviderDisabled(String provider) {
            }
        }, null);
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
    }
}
