package com.samahmakki.companion.data;

import android.provider.BaseColumns;

public final class BillContract{
    //constructor
    private BillContract() {
    }

    public static final class BillEntry implements BaseColumns{
        public static final String TABLE_NAME = "BILLS";

        public final static  String _Id = BaseColumns._ID;
        public final static  String COLUMN_Bill_Name = "bill_name";
        public final static  String COLUMN_Bill_TIME = "Time";
        public final static  String COLUMN_Bill_DATE = "date";
        public final static  String COLUMN_Bill_REMINDER = "reminder";
    }
}
