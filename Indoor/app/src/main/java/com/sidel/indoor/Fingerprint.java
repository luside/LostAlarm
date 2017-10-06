package com.sidel.indoor;

import android.graphics.PointF;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeSet;

/**
 * Created by lsd20 on 06/10/2017.
 */

public class Fingerprint {
    int id;
    PointF location;


    HashMap<String,Integer> dict;

    public Fingerprint(){
        id=0;
    }

    public Fingerprint(HashMap<String,Integer> dict){
        this();
        this.dict = dict;
    }

    public Fingerprint(int id, PointF location){
        this();
        this.location = location;
    }

    public Fingerprint(int id, PointF location, HashMap<String,Integer> dict){
        this(id,location);
        this.dict = dict;

    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public PointF getLocation() {
        return location;
    }

    public void setLocation(PointF location) {
        this.location = location;
    }

    public HashMap<String, Integer> getDict() {
        return dict;
    }

    public void setDict(HashMap<String, Integer> dict) {
        this.dict = dict;
    }

    public float compare(Fingerprint fingerprint) {
        float result = 0f;

        HashMap<String, Integer> fingerprintMeasurements = fingerprint.getDict();
        TreeSet<String> keys = new TreeSet<String>();
        keys.addAll(dict.keySet());
        keys.addAll(fingerprintMeasurements.keySet());

        for (String key : keys) {
            int value = 0;
            Integer fValue = fingerprintMeasurements.get(key);
            Integer mValue = dict.get(key);
            value = (fValue == null) ? -119 : (int) fValue;
            value -= (mValue == null) ? -119 : (int) mValue;
            result += value * value;
        }

        result = (float)Math.sqrt(result);

        return result;
    }

    public Fingerprint getClosestMatch(ArrayList<Fingerprint> fingerprints) {
        //long time = System.currentTimeMillis();
        Fingerprint closest = null;
        float bestScore = -1;

        if(fingerprints != null) {
            for(Fingerprint fingerprint : fingerprints) {
                float score = compare(fingerprint);
                if(bestScore == -1 || bestScore > score) {
                    bestScore = score;
                    closest = fingerprint;
                }
            }
        }
        return closest;
    }
}
