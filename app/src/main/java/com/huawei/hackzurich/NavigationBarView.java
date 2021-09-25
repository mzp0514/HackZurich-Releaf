package com.huawei.hackzurich;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huawei.hackzurich.R;

public class NavigationBarView extends LinearLayout {
    private static int SCAN_PAGE = 0;
    private static int BUY_PAGE = 0;
    private static int PROFILE_PAGE = 0;


    private int currentPage = SCAN_PAGE;
    private Context mContext;

    public NavigationBarView (Context context) {
        super(context);
        mContext = context;
        initView();
    }

    public NavigationBarView (Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initView();
    }


    public void initView() {
        LayoutInflater.from(mContext).inflate(R.layout.navigation_bar_view, this, true);

    }


}
