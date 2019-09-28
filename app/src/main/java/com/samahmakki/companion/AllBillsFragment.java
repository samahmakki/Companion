package com.samahmakki.companion;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.samahmakki.companion.R;
import com.samahmakki.companion.data.BillContract;
import com.samahmakki.companion.data.BillContract.BillEntry;
import com.samahmakki.companion.data.BillDbHelper;

import java.util.ArrayList;
import java.util.Objects;

public class AllBillsFragment extends Fragment {

    ArrayList<Bill> BillsItem;
    ListView listView;
    View rootView;
    BillDbHelper mBillHelper;
    BillAdapter adapter;
    BillAdapter2 adapter2;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.all_bills_fragment, container, false);
        readBill();
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(final AdapterView<?> parent, View view, final int position, final long id) {
                mBillHelper = new BillDbHelper(getContext());
                CharSequence[] items = {getResources().getString(R.string.Update),
                        getResources().getString(R.string.Delete)};
                AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
                dialog.setTitle(getResources().getString(R.string.Choose_an_action));
                dialog.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            //update
                            Cursor c = mBillHelper.getData();
                            ArrayList<Integer> arrID = new ArrayList<>();
                            while (c.moveToNext()) {
                                arrID.add(c.getInt(0));
                            }
                            //show update dialog
                        }

                        if (which == 1) {
                            //delete
                            mBillHelper.deleteBill(id);
                            Cursor c = mBillHelper.getData();
                            ArrayList<Integer> arrID = new ArrayList<>();
                            while (c.moveToNext()) {
                                arrID.add(c.getInt(0));
                            }
                            showDialogDelete(arrID.get(position));
                        }
                    }
                });
                dialog.show();
                return true;
            }
        });
        return rootView;
    }

    private void showDialogDelete(final int medicineID) {
        AlertDialog.Builder dialogDelete = new AlertDialog.Builder(Objects.requireNonNull(getContext()));
        dialogDelete.setTitle("Warning !!");
        dialogDelete.setMessage("Are you sure you want delete this?");
        dialogDelete.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    mBillHelper.deleteData(medicineID);
                    Toast.makeText(getContext(), "Deleted Successfully", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Log.e("error", e.getMessage());
                }
                updateMedicineList();
            }
        });
        dialogDelete.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
            }
        });
        dialogDelete.show();
    }

/*
    private void showDialogUpdate(Activity activity, final int position) {
        final Dialog dialog = new Dialog(activity);
        dialog.setContentView(R.layout.bill_list_item);
        dialog.setTitle("Update");
        imgView = dialog.findViewById(R.id.img);
        final EditText edtName = dialog.findViewById(R.id.addet);
        Button btnUpdate = dialog.findViewById(R.id.upbtn);
        //set width of dialog
        int width = (int) (activity.getResources().getDisplayMetrics().widthPixels * 0.95);
        //set height of dialog
        int height = (int) (activity.getResources().getDisplayMetrics().heightPixels * 0.7);
        Objects.requireNonNull(dialog.getWindow()).setLayout(width, height);
        dialog.show();

        //in update dialog open camera to update image
        imgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //check runtime permissions
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (ActivityCompat.checkSelfPermission(Objects.requireNonNull(getContext()), android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                        //permission not granted,request it
                        String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};
                        //show popup for runtime permission
                        requestPermissions(permissions, 888);
                    } else { //permission is already granted
                        pickImageFromGallery();


                    }
                }


            }
        });


        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    AddmedFragment.dbHelper.updateData(
                            edtName.getText().toString().trim(),
                            AddmedFragment.imageViewToByte(imgView), position
                    );
                    dialog.dismiss();
                    Toast.makeText(Objects.requireNonNull(getActivity()).getApplicationContext(), "Updated Succefully", Toast.LENGTH_SHORT).show();
                } catch (Exception error) {
                    Log.e("Update error", error.getMessage());
                }
                updateMedicineList();
            }
        });
    }
*/

    private void updateMedicineList() {
        //get all data from sqlite
        Cursor cursor = mBillHelper.getData();
        BillsItem.clear();
        if (cursor != null) {
            int nameColumnIndex = cursor.getColumnIndex(BillContract.BillEntry.COLUMN_Bill_Name);
            int dateColumnIndex = cursor.getColumnIndex(BillEntry.COLUMN_Bill_DATE);
            int timeColumnIndex = cursor.getColumnIndex(BillEntry.COLUMN_Bill_TIME);
            while (cursor.moveToNext()) {
                // Use that index to extract the String or Int value of the word
                // at the current row the cursor is on.
                String currentName = cursor.getString(nameColumnIndex);
                String currentDate = cursor.getString(dateColumnIndex);
                String currentTime = cursor.getString(timeColumnIndex);
                BillsItem.add(new Bill(currentName, currentDate, currentTime));
            }
        }
        adapter.notifyDataSetChanged();

    }


    private void readBill() {

        BillDbHelper mBillHelper = new BillDbHelper(rootView.getContext());

        SQLiteDatabase db = mBillHelper.getReadableDatabase();
        String[] projection = {
                BillEntry.COLUMN_Bill_Name,
                BillEntry.COLUMN_Bill_DATE,
                BillEntry.COLUMN_Bill_TIME
        };

        Cursor cursor = db.query(
                BillEntry.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null);

        BillsItem = new ArrayList<Bill>();

        adapter = new BillAdapter(rootView.getContext(), BillsItem);
        listView = rootView.findViewById(R.id.bills_listView);
        listView.setAdapter(adapter);


        try {
            // Figure out the index of each column
            int nameColumnIndex = cursor.getColumnIndex(BillContract.BillEntry.COLUMN_Bill_Name);
            int dateColumnIndex = cursor.getColumnIndex(BillEntry.COLUMN_Bill_DATE);
            int timeColumnIndex = cursor.getColumnIndex(BillEntry.COLUMN_Bill_TIME);

            // Iterate through all the returned rows in the cursor
            while (cursor.moveToNext()) {
                // Use that index to extract the String or Int value of the word
                // at the current row the cursor is on.
                String currentName = cursor.getString(nameColumnIndex);
                String currentDate = cursor.getString(dateColumnIndex);
                String currentTime = cursor.getString(timeColumnIndex);

                BillsItem.add(new Bill(currentName, currentDate, currentTime));
            }
        } finally {
            // Always close the cursor when you're done reading from it. This releases all its
            // resources and makes it invalid.
            cursor.close();
        }
    }
}
