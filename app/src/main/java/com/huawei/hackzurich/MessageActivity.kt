package com.huawei.hackzurich

import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.fragment.app.FragmentActivity

class MessageActivity : FragmentActivity(), View.OnClickListener {
    private val TAG = "MessageActivity"

    private val fakeView: View? = null
    private var recycleBlockView: View? = null
    private var profileView: View? = null
    private var messageView: View? = null
    private var scanBlockView: View? = null
    private var marketBlockView: View? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_message)

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

        if (Build.VERSION.SDK_INT >= 21) {
            val decorView = window.decorView
            val option = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE)
            decorView.systemUiVisibility = option
            window.statusBarColor = Color.BLACK
        }
    }

    override fun onClick(view: View) {
        if (view == profileView) {
            val intent = Intent(this, ProfileActivity::class.java)
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