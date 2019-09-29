package com.samahmakki.companion;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Objects;
import static android.app.Activity.RESULT_OK;

public class KitFragment extends Fragment {

    ListView lstmed;
    MedicineListAdapter adapter = null;
    ArrayList<medicine> list;
    ImageView imgView;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable final Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_kit, container, false);

        android.support.v7.app.ActionBar actionBar = ((AppCompatActivity) Objects.requireNonNull(getActivity())).getSupportActionBar();
        Objects.requireNonNull(actionBar).setTitle("Medication list");


        lstmed = view.findViewById(R.id.lst_med);
        list = new ArrayList<>();
        adapter = new MedicineListAdapter(getContext(), R.layout.item_medicine, list);
        lstmed.setAdapter(adapter);
        //get all data from SQLite
        Cursor cursor = AddmedFragment.dbHelper.getData("SELECT * FROM MEDICINE");
        list.clear();


        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String name = cursor.getString(1);
            byte[] image = cursor.getBlob(2);
            list.add(new medicine(id, name, image));
        }
        adapter.notifyDataSetChanged();

        if (list.size() == 0) {
            //if there is no medicine in table of database which mean listview is empty
            Toast.makeText(getContext(), "No Medicine Found", Toast.LENGTH_SHORT).show();
        }

        lstmed.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int position, long id) {
                CharSequence[] items = { "Update","Delete"};
                 AlertDialog.Builder dialog = new AlertDialog.Builder((getContext()));
                dialog.setTitle("Choose an action");
                dialog.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if (which == 0) { //update
                            Cursor c = AddmedFragment.dbHelper.getData("SELECT id FROM MEDICINE");
                            ArrayList<Integer> arrID = new ArrayList<>();
                            while (c.moveToNext()) {
                                arrID.add(c.getInt(0));
                            }
                            //show update dialog

                            showDialogUpdate(getActivity(), arrID.get(position));
                        }


                        if (which == 1){

                           //delete
                           Cursor c = AddmedFragment.dbHelper.getData("SELECT id FROM MEDICINE");
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
        return view;
    }

    private void showDialogDelete(final int medicineID) {
        AlertDialog.Builder dialogDelete = new AlertDialog.Builder(Objects.requireNonNull(getContext()));
        dialogDelete.setTitle("Warning !!");
        dialogDelete.setMessage("Are you sure you want delete this?");
        dialogDelete.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
           try {
               AddmedFragment.dbHelper.deleteData(medicineID);
               Toast.makeText(getContext(), "Deleted Succefully", Toast.LENGTH_SHORT).show();
           }
           catch (Exception e){
               Log.e("error",e.getMessage());
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

    private void showDialogUpdate(Activity activity, final int position) {
        final Dialog dialog = new Dialog(activity);
        dialog.setContentView(R.layout.update_medicinelist);
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

    private void updateMedicineList() {
        //get all data from sqlite
        Cursor cursor = AddmedFragment.dbHelper.getData("SELECT * FROM MEDICINE");
        list.clear();
        if (cursor != null) {
            while (cursor.moveToNext()) {
                int id = cursor.getInt(0);
                String name = cursor.getString(1);
                byte[] image = cursor.getBlob(2);
                list.add(new medicine(id, name, image));
            }
        }
        adapter.notifyDataSetChanged();

    }

    private void pickImageFromGallery() {
        //intent to pick image
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 888);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {
            case 888: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //permission was granted
                    pickImageFromGallery();

                } else {
                    //permission was denied
                    Toast.makeText(getContext(), "Permission denied..!", Toast.LENGTH_SHORT).show();
                }
            }

        }

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 888 && resultCode == RESULT_OK) {
            //set image to image view
            imgView.setImageURI(data.getData());


        }
    }




}
