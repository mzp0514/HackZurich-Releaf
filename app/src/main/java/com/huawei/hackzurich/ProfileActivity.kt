package com.huawei.hackzurich

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.fragment.app.FragmentActivity

class ProfileActivity : FragmentActivity(), View.OnClickListener {
    private val TAG = "ProfileActivity"

    private val fakeView: View? = null
    private var recycleBlockView: View? = null
    private var profileView: View? = null
    private var messageView: View? = null
    private var scanBlockView: View? = null
    private var marketBlockView: View? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        recycleBlockView = findViewById<LinearLayout>(R.id.nav_bar_block_1)
        scanBlockView = findViewById<LinearLayout>(R.id.nav_bar_block_2)
        marketBlockView = findViewById<LinearLayout>(R.id.nav_bar_block_3)
        profileView = findViewById<ImageView>(R.id.profile_icon)
        messageView = findViewById<ImageView>(R.id.message_icon)

        recycleBlockView?.setOnClickListener(this)
        scanBlockView?.setOnClickListener(this)
        marketBlockView?.setOnClickListener(this)
        profileView?.setOnClickListener(this)
        messageView?.setOnClickListener(this)
    }

    override fun onClick(view: View) {
        if (view == messageView) {
            val intent = Intent(this, MessageActivity::class.java)
            startActivity(intent)
        } else if (view == scanBlockView) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        } else if (view == marketBlockView) {
            val intent = Intent(this, MarketActivity::class.java)
            startActivity(intent)
        }  else if (view.equals(recycleBlockView)) {
            val intent = Intent(this, RecycleActivity::class.java)
            startActivity(intent)
        }
    }
}