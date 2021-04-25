package com.globalhiddenodds.whois.presentation.permission

import android.app.Activity
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.util.ArrayList
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LevelPermission @Inject constructor(){

    fun requestPermission(
        activity: Activity, requestCode: Int,
        vararg permissions: String): Boolean {
        var granted = true
        val permissionsNeeded = ArrayList<String>()

        permissions.forEach { s ->
            val permissionCheck = ContextCompat.checkSelfPermission(activity, s)
            val hasPermission = permissionCheck == PackageManager.PERMISSION_GRANTED
            granted = granted and hasPermission
            when {
                !hasPermission -> permissionsNeeded.add(s)
            }
        }

        return when {
            granted -> true
            else -> {
                ActivityCompat.requestPermissions(activity,
                    permissionsNeeded.toTypedArray(),
                    requestCode)
                false
            }
        }
    }

    fun permissionGranted(
        requestCode: Int, permissionCode: Int, grantResults: IntArray) =
        if (requestCode == permissionCode) {
            grantResults.isNotEmpty() && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED
        } else false

}