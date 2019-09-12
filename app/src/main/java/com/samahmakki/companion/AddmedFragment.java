package com.samahmakki.companion;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class AddmedFragment extends Fragment {
    private static final String TAG = "AddmedFragment";
    private ArrayList<String> mNames = new ArrayList<>();
    private ArrayList<String> mImageUrls = new ArrayList<>();
    private RecyclerView recyclerView;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        getImages();

        View view = inflater.inflate(R.layout.fragment_add, container, false);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.shapeRV);

        return view;

    }

    private void getImages() {
        Log.d(TAG, "initImageBitmaps: preparing bitmaps.");

        mImageUrls.add("https://cdn.pixabay.com/photo/2013/07/13/11/44/capsule-158568__340.png");
        mNames.add("Capsule");

        mImageUrls.add("https://cdn.pixabay.com/photo/2016/10/03/18/59/syringe-1712511__340.png");
        mNames.add("Syringe");

        mImageUrls.add("https://cdn.pixabay.com/photo/2016/08/10/04/45/medicine-1582472__340.jpg");
        mNames.add("pills");
        mImageUrls.add("https://image.shutterstock.com/image-photo/overhead-flat-lay-shot-squiggly-260nw-1199827369.jpg");
        mNames.add("Ointment");

        mImageUrls.add("https://cdn.pixabay.com/photo/2012/04/15/20/16/spray-35176__340.png");
        mNames.add("Spray");
        mImageUrls.add("https://cdn.pixabay.com/photo/2015/03/04/16/26/container-659073__340.jpg");
        mNames.add("drops");


        initRecyclerView();

    }


    private void initRecyclerView() {


        Log.d(TAG, "initRecyclerView: init recyclerview");
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);

        recyclerView.setLayoutManager(layoutManager);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(getContext(), mNames, mImageUrls);
        recyclerView.setAdapter(adapter);
    }


}



