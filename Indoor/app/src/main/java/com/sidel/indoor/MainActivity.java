package com.sidel.indoor;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;

import java.util.List;

public class MainActivity extends Activity implements View.OnTouchListener{

    protected Map map;
    protected WifiManager wifiManager;
    protected BroadcastReceiver broadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        wifiManager = (WifiManager)getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        map = (Map)findViewById(R.id.map);
        map.setOnTouchListener(this);
    }

    protected void onStart(){
        super.onStart();
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                List<ScanResult> scanresults= wifiManager.getScanResults();
                receiveScanResults(scanresults);
            }
        };
        registerReceiver(broadcastReceiver, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
    }

    protected void receiveScanResults(List<ScanResult> scanResults){

    }

    protected void onStop(){
        super.onStop();
        unregisterReceiver(broadcastReceiver);
    }

    public boolean onTouch(View view, MotionEvent event){
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

    public void startCompassActivity(){
        Intent intent = new Intent(MainActivity.this, CompassActivity.class);
        startActivity(intent);
    }

    public void startEditActivity(){
        Intent intent = new Intent(MainActivity.this, EditActivity.class);
        startActivity(intent);
    }
}
