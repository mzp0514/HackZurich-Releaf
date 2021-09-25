package com.huawei.hackzurich;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

public class RecycleActivity extends FragmentActivity implements View.OnClickListener {

    private View fakeView;
    private View recycleView;
    private View profileView;
    private View messageView;
    private View scanView;
    private View cartView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycle);

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
        fakeView = findViewById(R.id.fake_ui);
        recycleView = findViewById(R.id.nav_bar_block_1);
        scanView = findViewById(R.id.nav_bar_block_2);
        cartView = findViewById(R.id.nav_bar_block_3);
        messageView = findViewById(R.id.message_icon);
        profileView = findViewById(R.id.profile_icon);

        fakeView.setOnClickListener(this);
        recycleView.setOnClickListener(this);
        profileView.setOnClickListener(this);
        cartView.setOnClickListener(this);
        scanView.setOnClickListener(this);
        messageView.setOnClickListener(this);
    }
}