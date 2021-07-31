package com.example.coronaapp;

import android.app.Activity;

import java.util.Vector;

public class MainManager {
    private Communication communication;
    private Vector<Event> events;
    private LocationMgr location;
    private BluetoothMgr bluetoothMgr;

    private MainManager() {
        communication = new Communication();
        events = new Vector();
        location = new LocationMgr();
        bluetoothMgr = new BluetoothMgr();
    }
    private static MainManager instance;


    public static MainManager getInstance() {
        if (instance == null) {
            instance = new MainManager();
        }
        return instance;
    }

    public void registerToEvents(Event event) {
        events.add(event);
    }

    public void unregisterToEvents(Event event) {
        events.remove(event);
    }

    public void getByCountry(String Country, String from, String to) {
        communication.getByCountry(Country, from, to);
    }

    public void reportCovid(String status, int sum) {
        for (Event event: events) {
            event.reportCovid(status, sum);
        }
    }

    public void setLocationManager(android.location.LocationManager locationManager, Activity activity) {
        location.init(locationManager, activity);
    }
    public String getLocalCountry() {
        return location.geCountry();
    }

    public void bluetoothEnableDiscoverability(Activity activity) {
        bluetoothMgr.enableDiscoverability(activity);
    }

    public void bluetoothDisableDiscoverability(Activity activity) {
        bluetoothMgr.disableDiscoverability(activity);
    }

    public void getBluetoothNearby() {
        bluetoothMgr.getNearby();
    }
    public void findBluetoothNearby(String name) {
        for (Event event: events) {
            event.findBluetoothNearby(name);
        }
    }
}
