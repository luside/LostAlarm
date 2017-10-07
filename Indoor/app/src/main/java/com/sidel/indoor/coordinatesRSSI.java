package com.sidel.indoor;

/**
 * Represents an item in a ToDo list
 */
public class coordinatesRSSI {

    /**
     * The colums in the table
     */
    //the coordinates
    @com.google.gson.annotations.SerializedName("xCoordinates")
    private float xCoordinates;
    @com.google.gson.annotations.SerializedName("yCoordinates")
    private float yCoordinates;

    //the RSSI of 5 APs
    @com.google.gson.annotations.SerializedName("hashString")
    private String hashString;
    /**
     * Item Id
     */
    @com.google.gson.annotations.SerializedName("fingerPrintId")
    private int mId;

    /**
     * Indicates if the item is completed
     */
    @com.google.gson.annotations.SerializedName("complete")
    private boolean mComplete;

    /**
     * ToDoItem constructor
     */
    public coordinatesRSSI() {

    }



    /**
     * Initializes a new ToDoItem
     */
    public coordinatesRSSI(float x, float y, String hashString) {
        this.setxCoordinates(x);
        this.setyCoordinates(y);
        this.setHashString(hashString);
    }


    //getter and setter for coordinates
    public void setxCoordinates(float x) {
        xCoordinates = x;
    }
    public float getxCoordinates() {
        return xCoordinates;
    }

    public void setyCoordinates(float y) {
        yCoordinates = y;
    }
    public float getyCoordinates() {
        return yCoordinates;
    }


    //getter and setter for ID
    public int getId() {
        return mId;
    }
    public final void setId(int id) {
        mId = id;
    }

    //getter and setter for RSSI from 5 APs
    public void setHashString(String hashString){
        this.hashString = hashString;
    }
    public String getHashString(){
        return hashString;
    }


    /**
     * Indicates if the item is marked as completed
     */
    public boolean isComplete() {
        return mComplete;
    }

    /**
     * Marks the item as completed or incompleted
     */
    public void setComplete(boolean complete) {
        mComplete = complete;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof coordinatesRSSI && ((coordinatesRSSI) o).mId == mId;
    }
}