package com.example.cerg.controler;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.net.wifi.WpsInfo;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

public class Peers extends Activity implements  WifiP2pManager.PeerListListener,
                                                WifiP2pManager.ActionListener,
                                                AdapterView.OnItemClickListener,
                                                WifiP2pManager.ConnectionInfoListener {

    private WifiP2pManager mManager;
    private WifiP2pManager.Channel mChannel;
    private BroadcastReceiver receiver;
    private final IntentFilter intentFilter = new IntentFilter();
    private final List<WifiP2pDevice> peers = new ArrayList<>();
    private ListView lv;
    private PeersAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.peers);
        setUpIntentFilter();

        adapter = new PeersAdapter(this, R.layout.peers_element, peers);
        lv = (ListView) findViewById(R.id.peers_list_view);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(this);

        mManager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
        mChannel = mManager.initialize(this, getMainLooper(), null);
        mManager.discoverPeers(mChannel, this);
        receiver = new Receiver(mManager, mChannel, this);
    }

    @Override
    public void onPeersAvailable(WifiP2pDeviceList availablePeers) {
        peers.clear();
        peers.addAll(availablePeers.getDeviceList());


        Log.e("Neue Peers: ", "" + peers.size());

        for (WifiP2pDevice device : peers)
            Log.e("Name: ", device.deviceName);

        adapter.notifyDataSetChanged();

    }

    public void connect(int position) {
        // Picking the first device found on the network.
        WifiP2pDevice device = peers.get(position);

        WifiP2pConfig config = new WifiP2pConfig();
        config.deviceAddress = device.deviceAddress;
        config.wps.setup = WpsInfo.PBC;

        mManager.connect(mChannel, config, new WifiP2pManager.ActionListener() {

            @Override
            public void onSuccess() {
                // WiFiDirectBroadcastReceiver will notify us. Ignore for now.
                Log.i("fs", "Verbunden");
            }

            @Override
            public void onFailure(int reason) {
                Toast.makeText(Peers.this, "Connect failed. Retry.",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     *
     */
    private void setUpIntentFilter() {
        //  Indicates a change in the Wi-Fi P2P status.
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        // Indicates a change in the list of available peers.
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        // Indicates the state of Wi-Fi P2P connectivity has changed.
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        // Indicates this device's details have changed.
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);
    }

    /**
     *  Meldet den WiFi Direct Epfaenger an, wenn Activity aktiv/sichtbar wird.
     *  INFO: Lifecycle State
     */
    @Override
    public void onResume() {
        super.onResume();
        receiver = new Receiver(mManager, mChannel, this);
        registerReceiver(receiver, intentFilter);
    }

    /**
     *  Meldet den WiFi Direct Empfaenger ab, wenn Activity angehalten wird.
     *  INFO: Lifecycle State
     */
    @Override
    public void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
    }

    /**
     *  Schreibt wenn Initiirung erfolgreich.
     */
    @Override
    public void onSuccess() {
        Log.i("Peers", "Suche nach Peers erfolgreich initiiert.");
    }

    /**
     *  Schreibt wenn Initiirung fehlerhaft.
     */
    @Override
    public void onFailure(int reasonCode) {
        Log.e("Peers", "Konnte keine Suche nach Peers initiierten." +
              "Fehlercode:" + reasonCode);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        connect(position);
    }

    @Override
    public void onConnectionInfoAvailable(final WifiP2pInfo info) {

        // InetAddress from WifiP2pInfo struct.
        //InetAddress groupOwnerAddress = info.groupOwnerAddress.getHostAddress();

        // After the group negotiation, we can determine the group owner.
        if (info.groupFormed && info.isGroupOwner) {
            // Do whatever tasks are specific to the group owner.
            // One common case is creating a server thread and accepting
            // incoming connections.
        } else if (info.groupFormed) {
            // The other device acts as the client. In this case,
            // you'll want to create a client thread that connects to the group
            // owner.
        }
        Log.e("INFA: ", info.toString());
    }
}
