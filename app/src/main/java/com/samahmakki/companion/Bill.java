package com.samahmakki.companion;

public class Bill {
    private String mBillName;
    private String mBillDate;
    private String mBillTime;
    private int mReminder;

    public Bill(String billName, String billDate, String billTime) {
        mBillName = billName;
        mBillDate = billDate;
        mBillTime = billTime;
    }

    public Bill(String billName, String billDate, String billTime, int reminder) {
        mBillName = billName;
        mBillDate = billDate;
        mBillTime = billTime;
        mReminder = reminder;
    }

    public String getBillName(){
        return mBillName;
    }

    public int sum(int a ,int b){
        return a + b;
    }


    public String getBillDate(){
        return mBillDate;
    }

    public String getBillTime(){
        return mBillTime;
    }

    public int getReminder(){
        return mReminder;
    }
}
