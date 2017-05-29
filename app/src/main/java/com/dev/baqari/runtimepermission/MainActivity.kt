package com.dev.baqari.runtimepermission

import android.Manifest
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log

import com.dev.baqari.runtimelib.RuntimePermission
import com.dev.baqari.runtimelib.callbacks.OnDenyPermissions
import com.dev.baqari.runtimelib.callbacks.OnFailure
import com.dev.baqari.runtimelib.callbacks.OnGrantPermissions

class MainActivity : AppCompatActivity() {

    lateinit var permission: RuntimePermission

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        permission = RuntimePermission.builder()
                ?.requestCode(125)
                ?.permission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                ?.permission(Manifest.permission.READ_CONTACTS)
                ?.permission(Manifest.permission.ACCESS_FINE_LOCATION)
                ?.permission(Manifest.permission.READ_PHONE_STATE)
                ?.callBack(object : OnGrantPermissions {
                    override fun get(result: List<String>) {
                        result.forEach { Log.d("granted", " " + it) }
                    }
                }, object : OnDenyPermissions {
                    override fun get(result: List<String>) {
                        result.forEach { Log.d("denied", " " + it) }
                    }
                }, object : OnFailure {
                    override fun fail(e: Exception) {
                        e.printStackTrace()
                    }
                })!!
        permission.request(this)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        permission.onPermissionsResult(requestCode, permissions, grantResults)
    }
}
