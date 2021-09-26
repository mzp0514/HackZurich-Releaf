package com.huawei.hackzurich

import android.Manifest
import android.app.AlertDialog
import android.content.ActivityNotFoundException
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import com.huawei.hackzurich.modelcreator.WasteSortingDetector

class MarketActivity : FragmentActivity(), View.OnClickListener {
    private val REQUEST_IMAGE_CAPTURE = 1
    private val REQUEST_CAMERA = 0x01

    private val fakeView: View? = null
    private var recycleBlockView: View? = null
    private var profileView: View? = null
    private var messageView: View? = null
    private var scanBlockView: View? = null
//    private var marketBlockView: View? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_market)

        recycleBlockView = findViewById<LinearLayout>(R.id.nav_bar_block_1)
        scanBlockView = findViewById<LinearLayout>(R.id.nav_bar_block_2)
//        marketBlockView = findViewById<LinearLayout>(R.id.nav_bar_block_3)
        profileView = findViewById<ImageView>(R.id.profile_icon)
        messageView = findViewById<ImageView>(R.id.message_icon)

        recycleBlockView?.setOnClickListener(this)
        scanBlockView?.setOnClickListener(this)
//        marketBlockView?.setOnClickListener(this)
        profileView?.setOnClickListener(this)
        messageView?.setOnClickListener(this)
    }

    override fun onClick(view: View) {
       if (view == profileView) {
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        } else if (view == scanBlockView) {
           takePhoto()
       } else if (view == messageView) {
            val intent = Intent(this, MessageActivity::class.java)
            startActivity(intent)
        }  else if (view.equals(recycleBlockView)) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }


    private fun takePhoto() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
            !== PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this, arrayOf(
                    Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ), REQUEST_CAMERA
            )
        }
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        try {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
        } catch (e: ActivityNotFoundException) {
            // display error state to the user
        }
    }

    private fun activateAlertDialogBuilder(ans: WasteSortingDetector.Prediction) {
        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setTitle("Category")
        alertDialogBuilder.setMessage(ans.label)
        alertDialogBuilder.setPositiveButton("Yes", { dialogInterface: DialogInterface, i: Int -> })
        alertDialogBuilder.setNegativeButton("Cancel", { dialogInterface: DialogInterface, i: Int -> })
        alertDialogBuilder.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap
            var wasteSorting = WasteSortingDetector(this)
            var ans = wasteSorting.detect(imageBitmap)
            activateAlertDialogBuilder(ans)
        }
    }

}
