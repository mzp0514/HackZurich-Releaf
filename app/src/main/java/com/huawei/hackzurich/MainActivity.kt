package com.huawei.hackzurich

import android.Manifest.permission.CAMERA
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import androidx.core.app.ActivityCompat

import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.provider.MediaStore
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import com.huawei.hackzurich.modelcreator.WasteSortingDetector

class MainActivity : FragmentActivity(), View.OnClickListener {

    private val REQUEST_CAMERA = 0x01
    private var recycleBlockView : View ?= null
    private var scanBlockView : View ?= null
    private var cartBlockView : View ?= null
    private var profileView : View ?= null
    private var messageView : View ?= null
    private val REQUEST_IMAGE_CAPTURE = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        title = getString(R.string.app_title)

//        val cameraPreview = window.decorView.findViewById<CameraPreview>(R.id.camera_view)
//        cameraPreview.cameraPreview

//        actionBar!!.hide();


//        val checkStatusTextView = findViewById<TextView>(R.id.main_check)
//
//        lifecycle.coroutineScope.launchWhenCreated {
//            try {
//                checkHMS()
//                checkStatusTextView.text = getString(R.string.checking_setup_result_ok)
//            } catch (checkException: Exception) {
//                checkStatusTextView.text =
//                    getString(R.string.checking_setup_result_fail, checkException.message)
//            }
//        }
        initView()

        if (ContextCompat.checkSelfPermission(this, CAMERA)
            !== PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this, arrayOf(
                    CAMERA,
                    WRITE_EXTERNAL_STORAGE
                ), REQUEST_CAMERA
            )
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap
            var wasteSorting = WasteSortingDetector(this)
            var ans = wasteSorting.detect(imageBitmap)
            println(ans)
        }
    }


    override fun onClick(p0: View?) {
        if (p0 === recycleBlockView) {
            val intent = Intent(this, RecycleActivity::class.java)
            startActivity(intent);
        } else if (p0 === scanBlockView) {
            takePhoto()
        }
        else if (p0 === cartBlockView) {
            val intent = Intent(this, CartActivity::class.java)
            startActivity(intent);
        } else if (p0 === profileView) {
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent);
        } else if (p0 === messageView) {
            val intent = Intent(this, MessageActivity::class.java)
            startActivity(intent);
        }
    }

    private fun takePhoto() {

        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        try {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
        } catch (e: ActivityNotFoundException) {
            // display error state to the user
        }
    }

    private fun initView() {
        recycleBlockView = findViewById<LinearLayout>(R.id.nav_bar_block_1)
        scanBlockView = findViewById<LinearLayout>(R.id.nav_bar_block_2)
        cartBlockView = findViewById<LinearLayout>(R.id.nav_bar_block_3)
        profileView = findViewById<ImageView>(R.id.profile_icon)
        messageView = findViewById<ImageView>(R.id.message_icon)

        recycleBlockView?.setOnClickListener(this)
        scanBlockView?.setOnClickListener(this)
        cartBlockView?.setOnClickListener(this)
        profileView?.setOnClickListener(this)
        messageView?.setOnClickListener(this)

    }

//    private suspend fun checkHMS() {
//        testHmsCorePresence()
//        testAccountByRequestingPushNotificationsToken()
//    }
//
//    private suspend fun testAccountByRequestingPushNotificationsToken() {
//        val pushToken = withContext(Dispatchers.IO) {
//            HmsUtils.getPushNotificationsToken(this@MainActivity)
//        }
//        check(pushToken.isNotEmpty()) { "Push notifications token retrieved, but empty. Clear app data and try again." }
//    }
//
//    private fun testHmsCorePresence() {
//        check(HmsUtils.isHmsAvailable(this)) { "Please make sure you have HMS Core installed on the test device." }
//    }
}

