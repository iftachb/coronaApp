package com.example.coronaapp;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

public class BluetoothMgr {
    private BluetoothAdapter mBluetoothAdapter;

    private String[] infectedMACAddresses = {"2C:26:17:54:CD:15"};

    public BluetoothMgr() {
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if(mBluetoothAdapter == null) {
            Log.i("Bluetooth", "Device won't support Bluetooth");
        }

    }
    public void enableDiscoverability(Activity activity) {
//        Intent dIntent =  new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
//        dIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
//        activity.startActivity(dIntent);

        // Register for broadcasts when a device is discovered.
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        activity.registerReceiver(receiver, filter);
    }
    public void disableDiscoverability(Activity activity) {
        activity.unregisterReceiver(receiver);
    }

    // Create a BroadcastReceiver for ACTION_FOUND.
    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            Log.i("Bluetooth", "!!!");
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Discovery has found a device. Get the BluetoothDevice
                // object and its info from the Intent.
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                String deviceName = device.getName();
                String deviceHardwareAddress = device.getAddress(); // MAC address
                Log.i("Bluetooth", deviceName +", " +deviceHardwareAddress);

                for (String infected : infectedMACAddresses) {
                    if (deviceHardwareAddress.equals(infected)) {
                        MainManager.getInstance().findBluetoothNearby(deviceName);
                    }
                }
           }
        }
    };
    public void getNearby() {
        if(mBluetoothAdapter == null) {
            // Device won't support Bluetooth
            return;
        }
        if (mBluetoothAdapter.isDiscovering()) {
            // Bluetooth is already in modo discovery mode, we cancel to restart it again
            mBluetoothAdapter.cancelDiscovery();
        }
        mBluetoothAdapter.startDiscovery();

        // Get paired devices.
//        Set<BluetoothDevice> pairedDevices = bAdapter.getBondedDevices();
//        if (pairedDevices.size() > 0) {
//            // There are paired devices. Get the name and address of each paired device.
//            for (BluetoothDevice device : pairedDevices) {
//                String deviceName = device.getName();
//                String deviceHardwareAddress = device.getAddress(); // MAC address
//                Log.i("Bluetooth", deviceName +", " +deviceHardwareAddress);
//            }
//        }
    }


}
