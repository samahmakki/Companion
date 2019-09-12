package com.samahmakki.companion;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

public class MainActivity extends AppCompatActivity {

    LinearLayout medicationLayout;
    LinearLayout billsLayout;
    LinearLayout magnifierLayout;
    LinearLayout activitiesLayout;
    LinearLayout flashlightLayout;

<<<<<<< Updated upstream
    @SuppressLint("RestrictedApi")
=======
>>>>>>> Stashed changes
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        medicationLayout = findViewById(R.id.medication);
        billsLayout = findViewById(R.id.bills);
        magnifierLayout = findViewById(R.id.magnifier);
        activitiesLayout = findViewById(R.id.activities);
        flashlightLayout = findViewById(R.id.flashlight);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.custom_actionbar);

        medicationLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent medicationIntent = new Intent(MainActivity.this, MedicationActivity.class);
                startActivity(medicationIntent);
            }
        });

        billsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent billsIntent = new Intent(MainActivity.this, BillsActivity.class);
                startActivity(billsIntent);
            }
        });

        magnifierLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent magnifierIntent = new Intent(MainActivity.this, MagnifierActivity.class);
                startActivity(magnifierIntent);
            }
        });

        activitiesLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent activitiesIntent = new Intent(MainActivity.this, ActivitiesActivity.class);
                startActivity(activitiesIntent);
            }
        });


        flashlightLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent flashlightIntent = new Intent(MainActivity.this, flashlightActivity.class);
                startActivity(flashlightIntent);
            }
        });
    }
}
