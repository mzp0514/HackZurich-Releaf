package com.huawei.hackzurich

import android.Manifest.permission.CAMERA
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import com.huawei.hms.maps.HuaweiMap
import com.huawei.hms.maps.OnMapReadyCallback
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import androidx.core.app.ActivityCompat

import android.content.pm.PackageManager
import androidx.core.content.ContextCompat


class MainActivity : FragmentActivity(), OnMapReadyCallback {

    private val REQUEST_CAMERA = 0x01

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        title = getString(R.string.app_title)

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

    override fun onMapReady(p0: HuaweiMap?) {
        TODO("Not yet implemented")
    }

    private suspend fun checkHMS() {
        testHmsCorePresence()
        testAccountByRequestingPushNotificationsToken()
    }

    private suspend fun testAccountByRequestingPushNotificationsToken() {
        val pushToken = withContext(Dispatchers.IO) {
            HmsUtils.getPushNotificationsToken(this@MainActivity)
        }
        check(pushToken.isNotEmpty()) { "Push notifications token retrieved, but empty. Clear app data and try again." }
    }

    private fun testHmsCorePresence() {
        check(HmsUtils.isHmsAvailable(this)) { "Please make sure you have HMS Core installed on the test device." }
    }
}

