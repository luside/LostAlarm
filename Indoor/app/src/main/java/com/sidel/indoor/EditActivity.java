package com.sidel.indoor;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.PointF;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeSet;

/**
 * Created by lsd20 on 06/10/2017.
 */

public class EditActivity extends Activity implements View.OnTouchListener {

    public static final int SCAN_DELAY = 1000;
    public static final int SCAN_INTERVAL = 1000;

    protected Map map;
    protected WifiManager wifiManager;
    protected BroadcastReceiver broadcastReceiver;
    protected Indoor application;
    private ApPoint ap;
    private int remainingScan = 0;

    private ProgressDialog progressDialog;

    private HashMap<String, Integer> dict;

    private boolean visible = true;

    private long touchStarted;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("EDIT MODE");
        setContentView(R.layout.activity_main);
        application = (Indoor) getApplication();
        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        map = (Map) findViewById(R.id.map);
        map.setOnTouchListener(this);
    }

    public boolean onTouch(View view, MotionEvent event) {
        view.onTouchEvent(event);
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                touchStarted = event.getEventTime(); // calculate tap start
                break;
            case MotionEvent.ACTION_UP:
                if (event.getEventTime() - touchStarted < 200) {
                    PointF location = new PointF(event.getX(), event.getY());
                    if (ap == null) {
                        ap = map.createNewWifiPointOnMap(location);
                        ap.setActive(true);
                    } else {
                        map.setWifiPointViewPosition(ap, location);
                    }
                    refresh(); // redraw map
                }
                break;
        }
         return true;
    }

    public void onStart() {
        super.onStart();

        broadcastReceiver = new BroadcastReceiver ()
        {
            @Override
            public void onReceive(Context c, Intent intent)
            {
                onReceiveWifiScanResults(wifiManager.getScanResults());

            }
        };

        registerReceiver(broadcastReceiver, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
    }

    protected void onStop()
    {
        unregisterReceiver(broadcastReceiver);
        super.onStop();
    }

    public void onReceiveWifiScanResults(List<ScanResult> results) {
        Log.d("TAG","OnReceive");
        if (remainingScan != 0 && ap != null) {
            remainingScan--;
            HashMap<String, Integer> newdict = new HashMap<String, Integer>();
            for (ScanResult result : results) {
                newdict.put(result.BSSID, result.level);
            }

            TreeSet<String> keys = new TreeSet<String>();
            keys.addAll(dict.keySet());
            keys.addAll(dict.keySet());

            for (String key : keys) {
                Integer value = dict.get(key);
                Integer newvalue = newdict.get(key);

                if (value == null) {
                    dict.put(key, newvalue + (-119 * (3 - 1 - remainingScan)));
                } else if (newvalue == null) {
                    dict.put(key, -119 + value);
                } else {
                    dict.put(key, newvalue + value);
                }
            }
            if (remainingScan > 0) {
                scanNext();
            } else {
                for (String key : dict.keySet()) {
                    int value = (int) dict.get(key) / 3;
                    dict.put(key, value);
                }
                Fingerprint f = new Fingerprint(dict);
                f.setLocation(ap.getLocation());
                map.createNewWifiPointOnMap(f, true);
                application.addFingerprint(f);
            }

        } else {
            Toast.makeText(getApplicationContext(), "Failed to create fingerprint", Toast.LENGTH_SHORT).show();
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(1, 1, 0, "SCAN");
        menu.add(1, 2, 1, "EXIT EDIT MODE");
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case 1:
                if(ap == null) {
                    Toast.makeText(getApplicationContext(), "You must tap on screen first!", Toast.LENGTH_SHORT).show();
                } else {
                    startScan(); // show loading dialog and start wifi scan
                }
                return true;
            case 2:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void startScan() {
        remainingScan = 3;
        dict = new HashMap<String, Integer>();
        progressDialog = ProgressDialog.show(this,"","Scanning...Please Wait",true);
        wifiManager.startScan();
    }


    public void scanNext() {
        Timer timer = new Timer();

        timer.schedule(new TimerTask() {

            @Override
            public void run() {
                wifiManager.startScan();
            }

        }, SCAN_INTERVAL);
    }

    public void refresh() {
        map.invalidate();
    }
}
