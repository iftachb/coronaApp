package com.example.coronaapp;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import android.location.Location;

import androidx.core.app.ActivityCompat;

import java.io.IOException;
import java.util.List;
import java.util.Locale;


public class LocationMgr implements LocationListener {
    private final int REQUEST_LOCATION_PERMISSION = 1;

    private LocationManager locationManager;
    private Location location;
    private Geocoder geocoder;
    private List<Address> addresses;

    public void init(LocationManager locationManager, Activity activity) {
        this.locationManager = locationManager;
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestLocationPermission(activity);
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);

        geocoder = new Geocoder(activity, Locale.getDefault());
    }
    private void requestLocationPermission(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            String permissions[] = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_BACKGROUND_LOCATION};

            ActivityCompat.requestPermissions(
                    activity,
                    permissions,
                    REQUEST_LOCATION_PERMISSION
            );
        } else {
            String permissions[] = {Manifest.permission.ACCESS_FINE_LOCATION};
            ActivityCompat.requestPermissions(
                    activity,
                    permissions,
                    REQUEST_LOCATION_PERMISSION
            );
        }
    }

    public String geCountry() {
        if (location == null) {
            Log.e("location", "cannot have location");
            return "";
        }

        try {
            addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            String city = addresses.get(0).getLocality();
            String state = addresses.get(0).getAdminArea();
            String country = addresses.get(0).getCountryName();
            return country;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    @Override
    public void onLocationChanged(Location location) {
        this.location = location;
    }

    @Override
    public void onProviderDisabled(String provider) {
        Log.d("Latitude","disable");
    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.d("Latitude","enable");
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.d("Latitude","status");
    }
}


