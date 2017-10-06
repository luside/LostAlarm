package com.sidel.indoor;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
}
