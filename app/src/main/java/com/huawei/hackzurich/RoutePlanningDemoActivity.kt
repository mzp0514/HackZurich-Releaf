/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 *  2020.1.3-Changed modify the import classes type and add some map display demos.
 *                  Huawei Technologies Co., Ltd.
 *
 */
package com.huawei.hackzurich

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.LocationManager
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.huawei.hms.maps.CameraUpdateFactory
import com.huawei.hms.maps.HuaweiMap
import com.huawei.hms.maps.OnMapReadyCallback
import com.huawei.hms.maps.SupportMapFragment
import com.huawei.hms.maps.model.*
import com.huawei.hackzurich.utils.NetworkRequestManager
import com.huawei.hackzurich.utils.NetworkRequestManager.OnNetworkListener
import kotlinx.android.synthetic.main.bottom_sheet.view.*
import kotlinx.android.synthetic.main.bottom_sheet2.view.*
import org.json.JSONException
import org.json.JSONObject
import java.util.*
import kotlin.collections.ArrayList

/**
 * Route Planning
 */
@SuppressLint("LongLogTag")
class RoutePlanningDemoActivity : AppCompatActivity(), OnMapReadyCallback {
    companion object {
        private const val TAG = "RoutePlanningDemoActivity"
        const val REQUEST_CODE = 0X01
        private val PERMISION = arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)
        private const val ZOOM_DELTA = 2.0f
    }


    private var mSupportMapFragment: SupportMapFragment? = null
    private var hMap: HuaweiMap? = null
//    private var mMarkerOrigin: Marker? = null
//    private var mMarkerDestination: Marker? = null

    private var markers: MutableList<Marker> = ArrayList()

    private var latLngs: MutableList<LatLng> = ArrayList()

    private val mPolylines: MutableList<Polyline> = ArrayList()
    private val mPaths: MutableList<List<LatLng>> = ArrayList()
    private var mLatLngBounds: LatLngBounds? = null

    private val mHandler: Handler = object : Handler() {
        @SuppressLint("HandlerLeak")
        override fun handleMessage(msg: Message) {
            when (msg.what) {
                0 -> renderRoute(mPaths, mLatLngBounds)
                1 -> {
                    val bundle = msg.data
                    val errorMsg = bundle.getString("errorMsg")
                    Toast.makeText(this@RoutePlanningDemoActivity, errorMsg, Toast.LENGTH_SHORT).show()
                }
                else -> {
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_route_planning_demo)
        if (!hasLocationPermission()) {
            ActivityCompat.requestPermissions(this, PERMISION, REQUEST_CODE)
        }
        val fragment = supportFragmentManager.findFragmentById(R.id.mapfragment_routeplanningdemo)
        if (fragment is SupportMapFragment) {
            mSupportMapFragment = fragment
            mSupportMapFragment?.getMapAsync(this)
        }

        latLngs.add(LatLng(47.39038965520367, 8.51583849802119)) // technopark
        latLngs.add(LatLng(47.402177626411635, 8.47259129868391)) // Vergarverk Biogas Zurich Biowaste
        latLngs.add(LatLng(47.397993427656424, 8.481548334262188)) // Recyclinghof Werdhölzli

    }

    private fun isGPSOpen(context: Context): Boolean {
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        val network = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        return gps || network
    }

    private fun hasLocationPermission(): Boolean {
        for (permission in PERMISION) {
            if (ActivityCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                return false
            }
        }
        return true
    }

    override fun onMapReady(huaweiMap: HuaweiMap) {
        hMap = huaweiMap
        if (isGPSOpen(this)) {
            hMap?.isMyLocationEnabled = true
            hMap?.uiSettings?.isMyLocationButtonEnabled = true
        } else {
            hMap?.isMyLocationEnabled = false
            hMap?.uiSettings?.isMyLocationButtonEnabled = false
        }
        hMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(latLngs[0], 13f))
        getWalkingRouteResult(latLngs[0], latLngs[1])
        getWalkingRouteResult(latLngs[1], latLngs[2])

        var dialog = BottomSheetDialog(this)
        var bottomSheet = layoutInflater.inflate(R.layout.bottom_sheet, null)

        bottomSheet.buttonSubmit.setOnClickListener {
            dialog.dismiss()
            var dialog2 = BottomSheetDialog(this)
            var bottomSheet2 = layoutInflater.inflate(R.layout.bottom_sheet2, null)
            bottomSheet2.buttonOk.setOnClickListener {
                dialog2.dismiss()
            }
            dialog2.setContentView(bottomSheet2)
            dialog2.show()
        }

        dialog.setContentView(bottomSheet)
        dialog.show()
    }

    fun getWalkingRouteResult(latLng1: LatLng, latLng2: LatLng) {
        NetworkRequestManager.getWalkingRoutePlanningResult(latLng1, latLng2,
                object : OnNetworkListener {
                    override fun requestSuccess(result: String?) {
                        generateRoute(result)
                    }

                    override fun requestFail(errorMsg: String?) {
                        val msg = Message.obtain()
                        val bundle = Bundle()
                        bundle.putString("errorMsg", errorMsg)
                        msg.what = 1
                        msg.data = bundle
                        mHandler.sendMessage(msg)
                    }
                })
    }

//    fun getBicyclingRouteResult(view: View?, latLng1: LatLng, latLng2: LatLng) {
//        removePolylines()
//        NetworkRequestManager.getBicyclingRoutePlanningResult(latLng1, latLng2,
//                object : OnNetworkListener {
//                    override fun requestSuccess(result: String?) {
//                        generateRoute(result)
//                    }
//
//                    override fun requestFail(errorMsg: String?) {
//                        val msg = Message.obtain()
//                        val bundle = Bundle()
//                        bundle.putString("errorMsg", errorMsg)
//                        if (errorMsg != null) {
//                            Log.d(TAG, errorMsg)
//                        }
//                        msg.what = 1
//                        msg.data = bundle
//                        mHandler.sendMessage(msg)
//                    }
//                })
//    }
//
//    fun getDrivingRouteResult(view: View?, latLng1: LatLng, latLng2: LatLng) {
//        removePolylines()
//        NetworkRequestManager.getDrivingRoutePlanningResult(latLng1, latLng2,
//                object : OnNetworkListener {
//                    override fun requestSuccess(result: String?) {
//                        generateRoute(result)
//                    }
//
//                    override fun requestFail(errorMsg: String?) {
//                        val msg = Message.obtain()
//                        val bundle = Bundle()
//                        bundle.putString("errorMsg", errorMsg)
//                        msg.what = 1
//                        msg.data = bundle
//                        mHandler.sendMessage(msg)
//                    }
//                })
//    }



    private fun generateRoute(json: String?) {
        try {
            val jsonObject = JSONObject(json)
            val routes = jsonObject.optJSONArray("routes")
            if (null == routes || routes.length() == 0) {
                return
            }
            val route = routes.getJSONObject(0)

            // get route bounds
            val bounds = route.optJSONObject("bounds")
            if (null != bounds && bounds.has("southwest") && bounds.has("northeast")) {
                val southwest = bounds.optJSONObject("southwest")
                val northeast = bounds.optJSONObject("northeast")
                val sw = LatLng(southwest.optDouble("lat"), southwest.optDouble("lng"))
                val ne = LatLng(northeast.optDouble("lat"), northeast.optDouble("lng"))
                mLatLngBounds = LatLngBounds(sw, ne)
            }

            // get paths
            val paths = route.optJSONArray("paths")
            for (i in 0 until paths.length()) {
                val path = paths.optJSONObject(i)
                val mPath: MutableList<LatLng> = ArrayList()
                val steps = path.optJSONArray("steps")
                for (j in 0 until steps.length()) {
                    val step = steps.optJSONObject(j)
                    val polyline = step.optJSONArray("polyline")
                    for (k in 0 until polyline.length()) {
                        if (j > 0 && k == 0) {
                            continue
                        }
                        val line = polyline.getJSONObject(k)
                        val lat = line.optDouble("lat")
                        val lng = line.optDouble("lng")
                        val latLng = LatLng(lat, lng)
                        mPath.add(latLng)
                    }
                }
                mPaths.add(i, mPath)
            }
            mHandler.sendEmptyMessage(0)
        } catch (e: JSONException) {
            Log.e(TAG, "JSONException$e")
        }
    }

    /**
     * Render the route planning result
     *
     * @param paths
     * @param latLngBounds
     */
    private fun renderRoute(paths: List<List<LatLng>>?, latLngBounds: LatLngBounds?) {
        if (null == paths || paths.isEmpty() || paths[0].isEmpty()) {
            return
        }
        for (i in paths.indices) {
            val path = paths[i]
            val options = PolylineOptions().color(Color.BLUE).width(5f)
            for (latLng in path) {
                options.add(latLng)
            }
            val polyline = hMap?.addPolyline(options)
            if (polyline != null) {
                mPolylines.add(i, polyline)
            }
        }
        addMarker(paths[0][0])
        addMarker(paths[0][paths[0].size - 1])
        if (null != latLngBounds) {
            val cameraUpdate = CameraUpdateFactory.newLatLngBounds(latLngBounds, 5)
            hMap?.moveCamera(cameraUpdate)
        } else {
            hMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(paths[0][0], 25f))
        }
    }

//    fun setOrigin(view: View?) {
//        val originLatStr = edtOriginLat.text.toString().trim()
//        val originLngStr = edtOriginLng.text.toString().trim()
//        if (originLatStr.isNotEmpty() && originLngStr.isNotEmpty()) {
//            try {
//                latLng1 = LatLng(originLatStr.toDouble(), originLngStr.toDouble())
//                removePolylines()
//                addOriginMarker(latLng1)
//                hMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng1, 13f))
//                mMarkerOrigin?.showInfoWindow()
//            } catch (e: IllegalArgumentException) {
//                Log.e(TAG, "IllegalArgumentException $e")
//                Toast.makeText(this, "IllegalArgumentException", Toast.LENGTH_SHORT).show()
//            } catch (e: NullPointerException) {
//                Log.e(TAG, "NullPointerException $e")
//                Toast.makeText(this, "NullPointerException", Toast.LENGTH_SHORT).show()
//            } catch (e: NumberFormatException) {
//                Log.e(TAG, "NumberFormatException $e")
//                Toast.makeText(this, "NumberFormatException", Toast.LENGTH_SHORT).show()
//            }
//        }
//    }

//    fun setDestination(view: View?) {
//        val destinationLatStr = edtDestinationLat.text.toString().trim()
//        val destinationLngStr = edtDestinationLng.text.toString().trim()
//        if (destinationLatStr.isNotEmpty() && destinationLngStr.isNotEmpty()) {
//            try {
//                latLng2 = LatLng(destinationLatStr.toDouble(), destinationLngStr.toDouble())
//                removePolylines()
//                addDestinationMarker(latLng2)
//                hMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng2, 13f))
//                mMarkerDestination?.showInfoWindow()
//            } catch (e: IllegalArgumentException) {
//                Log.e(TAG, "IllegalArgumentException $e")
//                Toast.makeText(this, "IllegalArgumentException", Toast.LENGTH_SHORT).show()
//            } catch (e: NullPointerException) {
//                Log.e(TAG, "NullPointerException $e")
//                Toast.makeText(this, "NullPointerException", Toast.LENGTH_SHORT).show()
//            } catch (e: NumberFormatException) {
//                Log.e(TAG, "NumberFormatException $e")
//                Toast.makeText(this, "NumberFormatException", Toast.LENGTH_SHORT).show()
//            }
//        }
//    }

    private fun addMarker(latLng: LatLng) {
      hMap?.addMarker(MarkerOptions().position(latLng)
                .anchor(0.5f, 0.9f) // .anchorMarker(0.5f, 0.9f)
                .title("Origin")
                .snippet(latLng.toString()))
    }


    private fun removePolylines() {
        for (polyline in mPolylines) {
            polyline.remove()
        }
        mPolylines.clear()
        mPaths.clear()
        mLatLngBounds = null
    }
}