package com.example.cerg.controler;

import android.net.wifi.p2p.WifiP2pManager;
import android.util.Log;

/**
 * Created by cerg on 20.01.2016.
 */
public class Listener implements WifiP2pManager.ActionListener {

    @Override
    public void onSuccess() {
        // Code for when the discovery initiation is successful goes here.
        // No services have actually been discovered yet, so this method
        // can often be left blank.  Code for peer discovery goes in the
        // onReceive method, detailed below.
        Log.e("ff", "dfgdfg");
    }

    @Override
    public void onFailure(int reasonCode) {
        // Code for when the discovery initiation fails goes here.
        // Alert the user that something went wrong.
        Log.e("ff", "" + reasonCode);
    }
}
