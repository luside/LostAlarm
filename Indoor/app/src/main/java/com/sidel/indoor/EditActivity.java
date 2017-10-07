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

import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeSet;
import java.util.concurrent.ExecutionException;

import static com.microsoft.windowsazure.mobileservices.table.query.QueryOperations.val;

/**
 * Created by lsd20 on 06/10/2017.
 */

public class EditActivity extends Activity implements View.OnTouchListener {


    public static final int SCAN_INTERVAL = 1000;

    protected Map map;
    protected WifiManager wifiManager;
    protected BroadcastReceiver broadcastReceiver;
    protected Indoor application;
    private ApPoint ap;
    private int remainingScan = 0;

    private ProgressDialog progressDialog;

    private HashMap<String, Integer> dict;


    private long touchStarted;

    //Azure
    private MobileServiceClient mClient;
    private MobileServiceTable<coordinatesRSSI> mCoordinatesRSSITable;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("EDIT MODE");
        setContentView(R.layout.activity_main);
        application = (Indoor) getApplication();
        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        map = (Map) findViewById(R.id.map);
        map.setOnTouchListener(this);

        //Azure
        try {
            mClient = new MobileServiceClient(
                    "http://lostalarm.azurewebsites.net/",
                    this);
            mCoordinatesRSSITable = mClient.getTable(coordinatesRSSI.class);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

    }

    //Azure synchrosing added
    public coordinatesRSSI addItemInTable(coordinatesRSSI item) throws ExecutionException, InterruptedException {
        coordinatesRSSI entity = mCoordinatesRSSITable.insert(item).get();
        return entity;
    }

    public void checkItemInTable(coordinatesRSSI item) throws ExecutionException, InterruptedException {
        mCoordinatesRSSITable.update(item).get();
    }

    private List<coordinatesRSSI> refreshItemsFromMobileServiceTable() throws ExecutionException, InterruptedException {
        return mCoordinatesRSSITable.where().field("complete").
                eq(val(false)).execute().get();
    }



    //Azure adding ends

    public boolean onTouch(View view, MotionEvent event) {
        view.onTouchEvent(event);
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                touchStarted = event.getEventTime();
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
            public void onReceive(Context context, Intent intent)
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
        Log.d("TAG",String.valueOf(remainingScan));
        if (remainingScan != 0 && ap != null) {
            remainingScan--;
            HashMap<String, Integer> newdict = new HashMap<String, Integer>();
            for (ScanResult result : results) {
                newdict.put(result.BSSID, result.level);
            }

            TreeSet<String> keys = new TreeSet<String>();
            keys.addAll(dict.keySet());
            keys.addAll(newdict.keySet());

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
                progressDialog.dismiss();
            }

        } else {
            progressDialog.dismiss();
            Toast.makeText(getApplicationContext(), "Failed to create fingerprint", Toast.LENGTH_SHORT).show();
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(1, 1, 0, "SCAN");
        menu.add(1, 2, 1, "EXIT EDIT MODE");
        menu.add(1, 3, 2, "UPDATE FINGERPRINTS SET TO AZURE");
        menu.add(1, 4, 3, "GET FINGERPRINTS SET FROM AZURE");
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

            case 3:
                ArrayList<Fingerprint> results = application.getFingerprintData();
                for(Fingerprint result : results){
                    coordinatesRSSI addingItem = new coordinatesRSSI(result.getLocation().x,result.getLocation().y,result.getDict().toString());
                    try {
                        addItemInTable(addingItem);
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                return true;
            case 4:
                ArrayList<coordinatesRSSI> tempRsults  = null;
                try {
                    tempRsults = (ArrayList<coordinatesRSSI>) refreshItemsFromMobileServiceTable();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                ArrayList<Fingerprint> newRsults = new ArrayList<Fingerprint>();
                for(coordinatesRSSI tempRestult:tempRsults){
                    String dirtyResult = tempRestult.getHashString();
                    String pureResult = dirtyResult.substring(1,dirtyResult.length()-1);
                    HashMap<String, Integer> dict = new HashMap<String, Integer>();
                    for(String idLevel : pureResult.split(", ")){
                        String id = idLevel.split("=")[0];
                        String level = idLevel.split("=")[1];
                        dict.put(id, Integer.valueOf(level));
                    }
                    PointF location = new PointF();
                    location.set(tempRestult.getxCoordinates(),tempRestult.getyCoordinates());
                    Fingerprint fingerprint = new Fingerprint(tempRestult.getId(),location, dict);
                    newRsults.add(fingerprint);
                }
                application.setFingerprintData(newRsults);
                return true;

        }
    }

    public void startScan() {
        remainingScan = 3;
        dict = new HashMap<String, Integer>();
        Log.d("TAG","Start to SCAN!!!!!!!!!!!!");
        progressDialog = ProgressDialog.show(this,"","Scanning...Please Wait",true);
        Log.d("TAG","Scanning!!!!!!!!!!!!!!!!!!");
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
