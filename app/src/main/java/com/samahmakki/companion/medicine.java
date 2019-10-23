package com.samahmakki.companion;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class medicine {
    private String medName;
    private byte[] medimg;
    private int id;
    private String time;
    private String date;
    private String interval;


    public medicine(int id, String medName, byte[] medimg, String date, String time,String interval) {
        this.id = id;
        this.medName = medName;
        this.medimg = medimg;
        this.date = date;
        this.time = time;
        this.interval = interval;


    }


    //Getter

    public int getId(){
        return id;
    }

    public String getMedName() {
        return medName;
    }

    public byte[] getMedimg() {
        return medimg;
    }

    public String getdate() {


        return date;
    }

    public String gettime() {

        return time;
    }
    public String getInterval() {


        return interval;
    }

    //setter
    public void setId(int id){
        this.id = id;
    }

    public void setMedName(String medName) {
        this.medName = medName;
    }

    public void setMedimg(byte[] medimg) {
        this.medimg = medimg;
    }
    public void settime(String time) {
        this.time = time;
    }


    public void setdate(String date) {
        this.date = date;
    }
    public void setInterval(String interval) {
        this.interval = interval;
    }

}
