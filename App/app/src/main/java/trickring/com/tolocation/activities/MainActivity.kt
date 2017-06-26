package trickring.com.tolocation.activities

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.databinding.DataBindingUtil
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.util.Log
import trickring.com.tolocation.R
import trickring.com.tolocation.databinding.ActivityMainBinding
import trickring.com.tolocation.service.ToLocationService
import trickring.com.tolocation.utils.PermissionHelper

class MainActivity : AppCompatActivity() {

    val TAG: String = MainActivity::class.java.simpleName

    /**
     * パーミッションのリクエストコード
     */
    val PERMISSION_REQUEST_CODE = 100

    /**
     * パーミッションのヘルパークラス
     */
    val permissionHelper = PermissionHelper(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)

        binding.locationSwitchCompat.setOnCheckedChangeListener({ switch, isOn ->
            if (isOn) {
                // Serviceの開始
                if (permissionHelper.isPermitted(android.Manifest.permission.ACCESS_FINE_LOCATION)) {
                    val intent = Intent(this, ToLocationService::class.java)
                    startService(intent)
                } else {
                    permissionHelper.requestPermission(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), PERMISSION_REQUEST_CODE)
                    switch.isChecked = false
                }
            } else {
                // Serviceの停止
                val intent = Intent(this, ToLocationService::class.java)
                stopService(intent)
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.size != 1 || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                if (permissionHelper.isShouldShowRequest(Manifest.permission.ACCESS_FINE_LOCATION)) {
                    // 許可しない
                    AlertDialog.Builder(this)
                            .setTitle("許可しないが選択されました")
                            .setMessage("このアプリでは位置情報が必須になります")
                            .setPositiveButton("再取得", { _, _ ->
                                permissionHelper.requestPermission(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), PERMISSION_REQUEST_CODE)
                            })
                            .setCancelable(false)
                            .create()
                            .show()
                } else {
                    // 許可しない(次回以降は表示しないにチェック)
                    AlertDialog.Builder(this)
                            .setTitle("許可しないが選択されました")
                            .setMessage("このアプリでは位置情報が必須になります。設定画面から権限を変更してください。")
                            .setPositiveButton("設定画面へ", { _, _ ->
                                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                                val uri = Uri.fromParts("package", packageName, null)
                                intent.data = uri
                                startActivity(intent)
                            })
                            .setCancelable(false)
                            .create()
                            .show()
                }
            } else {
                Log.d(TAG, "パーミッション取得成功")
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }
}
