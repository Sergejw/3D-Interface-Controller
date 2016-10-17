package com.example.cerg.controler;

import android.content.Context;
import android.net.wifi.p2p.WifiP2pDevice;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class PeersAdapter extends ArrayAdapter<WifiP2pDevice> {

    private List<WifiP2pDevice> peers;
    private Peers activity;

    public PeersAdapter(Context context, int resource, List<WifiP2pDevice> peers) {
        super(context, resource, peers);
        this.peers = peers;
        activity = (Peers) context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.peers_element, null);
        }

        TextView textView = (TextView) v.findViewById(R.id.name);
        textView.setText((peers.get(position).toString()));

        return v;
    }
}
