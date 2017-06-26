package trickring.com.tolocation.utils

import android.app.Activity
import android.content.pm.PackageManager
import android.support.v4.app.ActivityCompat
import android.support.v4.content.PermissionChecker

/**
 * パーミッション Helperクラス
 */
class PermissionHelper(val activity: Activity) {

    /**
     * パーミッションの有無を判定
     *
     * @param permission パーミッション
     * @return true:パーミッションがある false:パーミッションがない
     */
    fun isPermitted(permission: String): Boolean {
        return PermissionChecker.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_GRANTED
    }

    /**
     * パーミッションダイアログの再表示判定
     *
     * @param permission パーミッション
     * @return true:表示する false:表示しない
     */
    fun isShouldShowRequest(permission: String): Boolean {
        return ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)
    }

    /**
     * パーミッションを要求する
     *
     * @param permissions パーミッション
     * @param requestCode リクエストコード
     */
    fun requestPermission(permissions: Array<String>, requestCode: Int) {
        ActivityCompat.requestPermissions(activity, permissions, requestCode)
    }
}
