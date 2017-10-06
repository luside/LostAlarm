package com.sidel.indoor;

import android.graphics.PointF;

import java.io.FileNotFoundException;
import java.util.HashMap;

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
}
