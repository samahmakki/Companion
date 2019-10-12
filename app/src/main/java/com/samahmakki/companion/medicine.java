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


    public medicine(int id, String medName, byte[] medimg, String date, String time) {
        this.medName = medName;
        this.medimg = medimg;
        this.date = date;
        this.time = time;
        this.id = id;

    }

    public medicine() {

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
}
