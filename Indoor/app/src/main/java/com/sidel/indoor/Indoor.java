package com.sidel.indoor;

import android.app.Application;

import java.util.ArrayList;

/**
 * Created by lsd20 on 07/10/2017.
 */

public class Indoor extends Application {
    private ArrayList<Fingerprint> fingerprints;

    private DatabaseHandler databaseHandler;



    @Override
    public void onCreate() {
        super.onCreate();
        fingerprints = new ArrayList<Fingerprint>();
        //deleteDatabase(FingerprintDatabaseHandler.DATABASE_NAME);
        databaseHandler = new DatabaseHandler(this);
        loadFingerprintsFromDatabase();
    }

    public void loadFingerprintsFromDatabase() {
        fingerprints = databaseHandler.getAllFingerprints(); // fetch fingerprint data from the database
    }

    public ArrayList<Fingerprint> getFingerprintData() {
        return fingerprints;
    }


    public void addFingerprint(Fingerprint fingerprint) {
        fingerprints.add(fingerprint); // add to fingerprint arraylist
        databaseHandler.addFingerprint(fingerprint); // add to database
    }

    public void deleteAllFingerprints() {
        fingerprints.clear(); // delete all fingerprints from arraylist
        databaseHandler.deleteAllFingerprints(); // delete all fingerprints from database
    }

}
