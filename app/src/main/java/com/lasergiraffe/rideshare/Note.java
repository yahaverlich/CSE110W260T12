package com.lasergiraffe.rideshare;

import android.view.View;

import com.parse.*;



@ParseClassName("notes")
public class Note extends ParseObject implements View.OnClickListener{

    private String id;
    private String title;
    private String toDest;
    private String fromDest;
    private String name;
    private String phone;
    private String details;
    private String time;
    private String date;
    private String price;
    private String username;
    private int capacity;
    private int currNumRiders;
    private boolean isDriver;


    public Note(){
        //empty constructor
    }


    public Note(String noteId, String noteTitle, String noteName, String notePhone,
                String noteDetails, int noteCapacity, int noteCurrNumRiders, String destTo,
                String destFrom, String priceTotal, String theTime, String theDate, String username,
                boolean isItADriver) {
        id = noteId;
        title = noteTitle;
        name = noteName;
        phone = notePhone;
        details = noteDetails;
        capacity = noteCapacity;
        currNumRiders = noteCurrNumRiders;
        toDest = destTo;
        fromDest = destFrom;
        price = priceTotal;
        time = theTime;
        date = theDate;
        this.username = username;
        isDriver = isItADriver;

    }

    @Override
    // toString is the function for the content of the action bar
    public String toString() {
        return fromDest + "  -->  " + toDest;
    }

    //GETTERS
    public String getId() { return id; }
    public String getTitle() { return toString(); }
    public String getDetails() { return details; }
    public String getName() { return name; }
    public String getPhone() { return phone; }
    public int getCapacity() { return capacity; }
    public int getCurrNumRiders() { return currNumRiders; }
    public String getPrice() { return price; }
    public String getToDest() { return toDest; }
    public String getFromDest() { return fromDest; }
    public String getTime(){ return time; }
    public String getDate(){ return date; }
    public String getUsername(){ return username; }
    public boolean getDriver() { return isDriver; }

    //SETTERS
    public void setId(String id) { this.id = id; }
    public void setTitle() { this.title = toString(); }
    public void setDetails(String details) { this.details = details; }
    public void setName( String name) { this.name = name; }
    public void setPhone( String phone ) { this.phone = phone;}
    public void setCapacity( int capacity ){ this.capacity=capacity; }
    public void setCurrNumRiders( int currNumRiders ){ this.currNumRiders = currNumRiders; }
    public void setPrice(String price) {  this.price = price; }
    public void setToDest(String toDest) {  this.toDest = toDest; }
    public void setFromDest(String fromDest) {  this.fromDest = fromDest; }
    public void setTime(String time){ this.time = time; }
    public void setDate(String date){ this.date = date; }
    public void setUsername(String username){ this.username = username; }
    public void setDriver(boolean isDriver){ this.isDriver = isDriver; }



    @Override
    public void onClick(View v) {
        System.out.println("hi");
    }
}