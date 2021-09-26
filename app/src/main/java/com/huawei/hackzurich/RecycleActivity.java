package com.huawei.hackzurich;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

public class RecycleActivity extends FragmentActivity implements View.OnClickListener {

    public static String KEY_MONEY = "KEY_MONEY";

    private int mMoney = 0;

    private View fakeView;
    private View recycleView;
    private View profileView;
    private View messageView;
    private View scanView;
    private View cartView;

    private CheckBox cb1;
    private CheckBox cb2;
    private CheckBox cb3;
    private CheckBox cb4;
    private CheckBox cbTotal;

    private TextView moneyTextView;

    private View buttonGo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycle);

        initViews();
    }

    @Override
    public void onClick(View view) {
        if (view.equals(profileView)) {
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
        } else if (view.equals(cb1) || view.equals(cb2) || view.equals(cb3) || view.equals(cb4)) {
            updateMoney();
            checkCheckBoxTotal();
        } else if (view.equals(cbTotal)) {
            updateCheckBox();
        } else if (view.equals(buttonGo)) {
            Intent intent = new Intent(this, RoutePlanningDemoActivity.class);
            intent.putExtra(KEY_MONEY, mMoney);
            startActivity(intent);
        }
    }

    void initViews() {
//        fakeView = findViewById(R.id.fake_market_ui);
        recycleView = findViewById(R.id.nav_bar_block_1);
        scanView = findViewById(R.id.nav_bar_block_2);
        cartView = findViewById(R.id.nav_bar_block_3);
        messageView = findViewById(R.id.message_icon);
        profileView = findViewById(R.id.profile_icon);
        moneyTextView = findViewById(R.id.total_coin_text);
        buttonGo = findViewById(R.id.button_go);

//        fakeView.setOnClickListener(this);
        recycleView.setOnClickListener(this);
        profileView.setOnClickListener(this);
        cartView.setOnClickListener(this);
        scanView.setOnClickListener(this);
        messageView.setOnClickListener(this);

        cb1 = findViewById(R.id.checkbox_1);
        cb2 = findViewById(R.id.checkbox_2);
        cb3 = findViewById(R.id.checkbox_3);
        cb4 = findViewById(R.id.checkbox_4);
        cbTotal = findViewById(R.id.checkbox_all);

        cb1.setOnClickListener(this);
        cb2.setOnClickListener(this);
        cb3.setOnClickListener(this);
        cb4.setOnClickListener(this);
        cbTotal.setOnClickListener(this);

        buttonGo.setOnClickListener(this);
    }

    private void updateMoney() {
        int money = 0;

        if (cb1.isChecked()) {
            money += 2;
        }

        if (cb2.isChecked()) {
            money += 2;
        }

        if (cb3.isChecked()) {
            money += 3;
        }

        if (cb4.isChecked()) {
            money += 8;
        }

        mMoney = money;
        moneyTextView.setText(" " + new Integer(money).toString() + ".00");
    }

    private void updateCheckBox() {
        if (cbTotal.isChecked()) {
            cb1.setChecked(true);
            cb2.setChecked(true);
            cb3.setChecked(true);
            cb4.setChecked(true);
        } else {
            cb1.setChecked(false);
            cb2.setChecked(false);
            cb3.setChecked(false);
            cb4.setChecked(false);
        }
        updateMoney();
    }

    private void checkCheckBoxTotal() {
        if (cb1.isChecked() && cb2.isChecked() && cb3.isChecked() && cb4.isChecked()) {
            cbTotal.setChecked(true);
        } else {
            cbTotal.setChecked(false);
        }
    }
}