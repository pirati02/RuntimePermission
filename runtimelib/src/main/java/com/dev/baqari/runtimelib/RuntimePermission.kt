package com.dev.baqari.runtimelib


import android.annotation.SuppressLint
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import com.dev.baqari.runtimelib.callbacks.OnDenyPermissions
import com.dev.baqari.runtimelib.callbacks.OnFailure
import com.dev.baqari.runtimelib.callbacks.OnGrantPermissions
import java.util.*

class RuntimePermission {

    class Builder {

        fun requestCode(code: Int): RuntimePermissions {
            requestCode = code
            return runtimePermissions
        }
    }

    class RuntimePermissions {
        fun permission(permission: String): RuntimePermissions {
            allPermissions.put(counter, permission)
            counter++
            return runtimePermissions
        }

        fun callBack(onGrantPermissions: OnGrantPermissions, onDenyPermissions: OnDenyPermissions, onFailure: OnFailure): RuntimePermission {
            grantPermissions = onGrantPermissions
            denyPermissions = onDenyPermissions
            failure = onFailure
            return runtimePermission
        }
    }

    fun onPermissionsResult(code: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == code) {
            grantResults.forEach {
                val permission = permissions[it]
                val grantType = grantResults[it]
                if (grantType == PackageManager.PERMISSION_GRANTED) {
                    grantedPermissions.add(permission)
                } else {
                    deniedPermissions.add(permission)
                }
            }
            grantPermissions!!.get(grantedPermissions)
            denyPermissions!!.get(deniedPermissions)
        }
    }

    fun request(activity: Activity) {
        try {
            val tempArr = arrayOfNulls<String>(allPermissions.size)
            allPermissions.forEach {
                if (ContextCompat.checkSelfPermission(activity, it.value) == PackageManager.PERMISSION_DENIED) {
                    tempArr[it.key] = it.value
                }
            }
            ActivityCompat.requestPermissions(activity, tempArr, requestCode)
        } catch (e: Exception) {
            failure!!.fail(e)
        }

    }

    companion object {
        private var requestCode = 0
        private val runtimePermissions: RuntimePermissions by lazy { RuntimePermissions() }
        private val runtimePermission: RuntimePermission by lazy { RuntimePermission() }
        private val grantedPermissions = ArrayList<String>()
        private val deniedPermissions = ArrayList<String>()
        private var counter = 0
        val builder: Builder by lazy { Builder() }
        private var denyPermissions: OnDenyPermissions? = null
        private var grantPermissions: OnGrantPermissions? = null
        private var failure: OnFailure? = null
        @SuppressLint("UseSparseArrays")
        private val allPermissions = HashMap<Int, String>()

        fun builder(): Builder? {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                return builder
            } else {
                return null
            }
        }
    }
}
