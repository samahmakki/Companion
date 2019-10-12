package com.samahmakki.companion;

public class medicine {
    private String medName;
    private byte[] medimg;
    private int id;


    public medicine(int id, String medName, byte[] medimg) {
        this.medName = medName;
        this.medimg = medimg;

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
