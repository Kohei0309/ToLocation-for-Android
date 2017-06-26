package trickring.com.tolocation.service

import android.app.Service
import android.content.Context
import android.content.Intent
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import trickring.com.tolocation.activities.MainActivity


/**
 * ロケーションのServiceクラス
 */
class ToLocationService : Service(), LocationListener {

    val TAG: String = ToLocationService::class.java.simpleName

    /**
     * LocationListener最小通知タイマー（ミリ秒）
     */
    val LOCATION_NOTICE_MIN_TIMER: Long = 3L

    /**
     * LocationListener最小通知距離（メートル）
     */
    val LOCATION_NOTICE_MIN_DISTANCE: Float = 10F

    /**
     * 位置情報 マネージャークラス
     */
    lateinit var locationManager: LocationManager

    /**
     * 位置情報
     */
    lateinit var location: Location

    override fun onBind(p0: Intent?): IBinder? {
        Log.d(TAG, "onBind")
        return null
    }

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "onCreate")
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(TAG, "onStartCommand")
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Log.d(TAG, "onStartCommand : 位置情報が無効です")
            return START_NOT_STICKY
        }
        // 位置情報取得を開始
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, LOCATION_NOTICE_MIN_TIMER, LOCATION_NOTICE_MIN_DISTANCE, this)
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        Log.d(TAG, "onDestroy")
        locationManager.removeUpdates(this)
        super.onDestroy()
    }

    override fun onLocationChanged(location: Location?) {
        if (location != null) {
            Log.d(TAG, "位置情報 : " + location)
            this.location = location
        } else {
            Log.d(TAG, "位置情報 : 取得失敗")
        }
    }

    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
        Log.d(TAG, "onStatusChanged")
    }

    override fun onProviderEnabled(provider: String?) {
        Log.d(TAG, "onProviderEnabled")
    }

    override fun onProviderDisabled(provider: String?) {
        Log.d(TAG, "onProviderDisabled")
    }

    private fun startIntent() {
        val dialogIntent = Intent(this, MainActivity::class.java)
        dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(dialogIntent)
    }
}
