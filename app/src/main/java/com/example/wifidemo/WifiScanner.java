package com.example.wifidemo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;
import android.widget.Toast;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by lsd20 on 20/09/2017.
 */


public class WifiScanner extends BroadcastReceiver {

    private static final String TAG = "WifiScanReceiver";
    MainActivity main;

    public WifiScanner(MainActivity main){
        super();
        this.main = main;
    }

    @Override
    public void onReceive(Context context, Intent intent){

        List<ScanResult> results = main.wifiManager.getScanResults();
        Log.d(TAG,results.toString());
        ScanResult bestsignal = null;
        for(ScanResult result:results){
            main.textView.append("\n"+result.BSSID+"\t\t"+result.level);
            if(bestsignal==null|| WifiManager.compareSignalLevel(bestsignal.level,result.level)<0){
                bestsignal=result;
            }
        }
        if(bestsignal==null){
            Log.d(TAG,"Bestsignal is null");
        }
        String message = String.format("%s networks found.%s is the strongest.",results.size(),bestsignal.SSID);
        Toast.makeText(main, message, Toast.LENGTH_SHORT).show();
        Log.d(TAG,"onReceive() message :" + message);
    }

}
