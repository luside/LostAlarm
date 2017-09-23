package com.example.wifidemo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = "WiFiDemo";
    WifiManager wifiManager;
    BroadcastReceiver broadcastReceiver;
    TextView textView;
    Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = (TextView)findViewById(R.id.textview);
        button = (Button)findViewById(R.id.button);
        button.setOnClickListener(this);
        wifiManager=(WifiManager)getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        textView.append("\n\nwifi status"+wifiInfo.toString());

       /* List<WifiConfiguration> configurations = wifiManager.getConfiguredNetworks();
        for(WifiConfiguration configuration:configurations){
            textView.append("\n\n"+configuration.toString());
        }*/

        if(broadcastReceiver==null){
            broadcastReceiver = new WifiScanner(this);
        }
        registerReceiver(broadcastReceiver,new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        Log.d(TAG,"onCreate");

    }


    @Override
    protected void onStop(){
        unregisterReceiver(broadcastReceiver);
        super.onStop();
    }

    @Override
    public void onClick(View v){

        Toast.makeText(getApplicationContext(),"All Network Searched", Toast.LENGTH_SHORT).show();
        if(v.getId()==R.id.button){
            Log.d(TAG,"onCreate wifi.startScan()");
            wifiManager.startScan();
        }
    }
}
