package com.huawei.hackzurich;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecycleActivity extends FragmentActivity implements View.OnClickListener {

    private View fakeView;
    private View recycleView;
    private View profileView;
    private View messageView;
    private View scanView;
    private View cartView;

    private RecyclerView courseRV;

    // Arraylist for storing data
    private ArrayList<ItemModel> ItemModelArrayList;
    
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycle);
        courseRV = findViewById(R.id.idRVCourse);

        // here we have created new array list and added data to it.
        ItemModelArrayList = new ArrayList<>();
        ItemModelArrayList.add(new ItemModel("DSA in Java", 4, R.drawable.clothing));
        ItemModelArrayList.add(new ItemModel("Java Course", 3, R.drawable.clothing));
        ItemModelArrayList.add(new ItemModel("C++ COurse", 4, R.drawable.clothing));
        ItemModelArrayList.add(new ItemModel("DSA in C++", 4, R.drawable.clothing));
        ItemModelArrayList.add(new ItemModel("Kotlin for Android", 4, R.drawable.clothing));
        ItemModelArrayList.add(new ItemModel("Java for Android", 4, R.drawable.clothing));
        ItemModelArrayList.add(new ItemModel("HTML and CSS", 4, R.drawable.clothing));

        // we are initializing our adapter class and passing our arraylist to it.
        ItemAdapter ItemAdapter = new ItemAdapter(this, ItemModelArrayList);

        // below line is for setting a layout manager for our recycler view.
        // here we are creating vertical list so we will provide orientation as vertical
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        // in below two lines we are setting layoutmanager and adapter to our recycler view.
        courseRV.setLayoutManager(linearLayoutManager);
        courseRV.setAdapter(ItemAdapter);
        initViews();
    }

    @Override
    public void onClick(View view) {
        if (view.equals(fakeView)) {
            Intent intent = new Intent(this, RoutePlanningDemoActivity.class);
            startActivity(intent);
       } else if (view.equals(profileView)) {
            Intent intent = new Intent(this, ProfileActivity.class);
            startActivity(intent);
        } else if (view.equals(cartView)) {
            Intent intent = new Intent(this, MarketActivity.class);
            startActivity(intent);
        } else if (view.equals(scanView)) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        } else if (view.equals(messageView)) {
            Intent intent = new Intent(this, MessageActivity.class);
            startActivity(intent);
        }
    }

    void initViews() {
      //  fakeView = findViewById(R.id.fake_ui);
        recycleView = findViewById(R.id.nav_bar_block_1);
        scanView = findViewById(R.id.nav_bar_block_2);
        cartView = findViewById(R.id.nav_bar_block_3);
        messageView = findViewById(R.id.message_icon);
        profileView = findViewById(R.id.profile_icon);

      //  fakeView.setOnClickListener(this);
        recycleView.setOnClickListener(this);
        profileView.setOnClickListener(this);
        cartView.setOnClickListener(this);
        scanView.setOnClickListener(this);
        messageView.setOnClickListener(this);
    }
}