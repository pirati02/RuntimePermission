package com.dev.baqari.runtimepermission;

import android.Manifest;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.dev.baqari.runtimelib.RuntimePermission;
import com.dev.baqari.runtimelib.callbacks.OnDenyPermissions;
import com.dev.baqari.runtimelib.callbacks.OnFailure;
import com.dev.baqari.runtimelib.callbacks.OnGrantPermissions;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    RuntimePermission permission;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        permission = RuntimePermission.builder()
                .requestCode(125)
                .permission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .permission(Manifest.permission.READ_CONTACTS)
                .permission(Manifest.permission.ACCESS_FINE_LOCATION)
                .permission(Manifest.permission.READ_PHONE_STATE)
                .callBack(new OnGrantPermissions() {
                    @Override
                    public void get(List<String> result) {
                        for (String item : result) {
                            Log.d("GRANTED PERMISSIon", " " + item);
                        }
                    }
                }, new OnDenyPermissions() {
                    @Override
                    public void get(List<String> result) {
                        for (String item : result) {
                            Log.d("DENIED PERMISSIon", " " + item);
                        }
                    }
                }, new OnFailure() {
                    @Override
                    public void fail(Exception e) {
                        e.printStackTrace();
                    }
                });
        permission.request(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        permission.onPermissionsResult(requestCode, permissions, grantResults);
    }
}
