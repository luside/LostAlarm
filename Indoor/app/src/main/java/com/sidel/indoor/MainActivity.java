package com.sidel.indoor;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.PointF;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeSet;

public class MainActivity extends Activity implements View.OnTouchListener {

    protected Map map;
    protected WifiManager wifiManager;
    protected BroadcastReceiver broadcastReceiver;
    protected Indoor application;
    private ApPoint user;
    private static Handler updatehandler = new Handler();

    public static final int SCAN_DELAY = 1000; // delay for the first scan (milliseconds)
    public static final int SCAN_INTERVAL = 1000; // interval between scans (milliseconds)

    private class Redraw implements Runnable {
        public void run() {
            refresh();
        }
    }

    private Redraw redraw;
    private HashMap<String, Integer> dict;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        application = (Indoor) getApplication();
        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        map = (Map) findViewById(R.id.map);
        map.setOnTouchListener(this);
        user = map.createNewWifiPointOnMap(new PointF(0,0));
        user.setActive(true);
        dict = new HashMap<String, Integer>();
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {

            @Override
            public void run() {
         wifiManager.startScan();

            }

        }, SCAN_DELAY, SCAN_INTERVAL);
    }

    protected void onStart() {
        super.onStart();
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                List<ScanResult> scanresults = wifiManager.getScanResults();
                receiveScanResults(scanresults);
            }
        };
        registerReceiver(broadcastReceiver, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
    }

    protected void receiveScanResults(final List<ScanResult> results) {
        Indoor application = (Indoor) getApplication();
        final ArrayList<Fingerprint> fingerprints = application.getFingerprintData();


        if (results.size() > 0 && fingerprints.size() > 0) {
            Thread t = new Thread() {
                public void run() {

                    HashMap<String, Integer> newdict = new HashMap<String, Integer>();
                    for (ScanResult result : results) {
                        newdict.put(result.BSSID, result.level);
                    }

                    TreeSet<String> keys = new TreeSet<String>();
                    keys.addAll(dict.keySet());
                    keys.addAll(newdict.keySet());

                    for (String key: keys){
                        Integer newvalue = newdict.get(key);
                        Integer value = dict.get(key);
                        if(value==null){
                            dict.put(key,value);
                        }else if(newvalue==null){
                            dict.remove(key);
                        }else{
                            newvalue = (int)(value*0.4+newvalue*0.6);
                            dict.put(key,newvalue);
                        }
                    }

                    Fingerprint f = new Fingerprint(newdict);

                    Fingerprint closestMatch = f.getClosestMatch(fingerprints);
                    user.setFingerprint(closestMatch);

                    // need to refresh map through updateHandler since only UI thread is allowed to touch its views
                    updatehandler.post(redraw);
                }
            };
            t.start();


        }

    }

    protected void onStop() {
        super.onStop();
        unregisterReceiver(broadcastReceiver);
    }

    public boolean onTouch(View view, MotionEvent event) {
        view.onTouchEvent(event);
        return true;
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(1, 1, 0, "EDIT");
        menu.add(1, 2, 1, "COMPASS");
        menu.add(1, 3, 2, "EXIT");
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 1:
                startEditActivity();
                return true;
            case 2:
                startCompassActivity();
                return true;
            case 3:
                moveTaskToBack(true);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void refresh() {
        map.invalidate();
    }

    public void startCompassActivity() {
        Intent intent = new Intent(MainActivity.this, CompassActivity.class);
        startActivity(intent);
    }

    public void startEditActivity() {
        Intent intent = new Intent(MainActivity.this, EditActivity.class);
        startActivity(intent);
    }
}
