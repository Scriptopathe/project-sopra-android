package com.example.dorian.sopraandroid.model;

import org.jdom2.Element;

/**
 * Created by amandine on 12/01/2016.
 */
public class Booking {
    private String day;
    private String startTime;
    private String endTime;

    public Booking (Element booking) {
        System.out.println("booking dans booking :  ROOT -->" + booking.toString());
        this.day = booking.getChildText("day");
        this.startTime = booking.getChildText("startTime");
        this.endTime = booking.getChildText("endTime");
        System.out.println("fini booking");
    }

    public String getDay(){
        return this.day;
    }

    public String getStartTime(){
        return this.startTime;
    }

    public String getEndTime(){
        return this.endTime;
    }
}