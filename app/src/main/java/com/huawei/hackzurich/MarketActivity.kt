package com.huawei.hackzurich

import android.os.Bundle
import android.view.View
import androidx.fragment.app.FragmentActivity

class MarketActivity : FragmentActivity(), View.OnClickListener {
    private val TAG = "MarketActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_market)
    }

    override fun onClick(v: View?) {
        TODO("Not yet implemented")
    }
}
