package com.samahmakki.companion;

import android.annotation.SuppressLint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Objects;

import mehdi.sakout.aboutpage.AboutPage;
import mehdi.sakout.aboutpage.Element;

public class About extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        Objects.requireNonNull(actionBar).hide();
        Element adsElement = new Element();
        adsElement.setTitle(getString(R.string.add_advertise));
        View aboutpage = new AboutPage(this)
                .setImage(R.drawable.ab)
                .setDescription(getString(R.string.rafeeq_app)).addItem(adsElement)
                .addItem(new Element().setTitle("Version 1.0"))
                .addEmail("srdev2020@gmail.com")
                .addFacebook("https://www.facebook.com/SRAndroidDevelopment")
                .addTwitter("https://twitter.com/SRDev6")
                .addItem(createcopyright())
                .create();
        setContentView(aboutpage);
    }

    private Element createcopyright() {
        Element copyright = new Element();
        final String copyrightstring = String.format("copyright %d by SRDev", Calendar.getInstance().get(Calendar.YEAR));
        copyright.setTitle(copyrightstring);
        copyright.setIconDrawable(R.mipmap.ic_launcher);
        copyright.setGravity(Gravity.CENTER);
        copyright.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(About.this, copyrightstring, Toast.LENGTH_SHORT).show();
            }
        });
        return copyright;
    }
}


//https://www.linkedin.com/in/dr-reham-mahmoud
//https://www.linkedin.com/in/samahmakki
