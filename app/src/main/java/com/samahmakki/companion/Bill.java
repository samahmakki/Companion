package com.samahmakki.companion;

public class Bill {
    private String mBillName;
    private String mBillDate;
    private String mBillTime;

    public Bill(String billName, String billDate, String billTime) {
        mBillName = billName;
        mBillDate = billDate;
        mBillTime = billTime;
    }

    public String getBillName(){
        return mBillName;
    }

    public String getBillDate(){
        return mBillDate;
    }

    public String getBillTime(){
        return mBillTime;
    }
}
