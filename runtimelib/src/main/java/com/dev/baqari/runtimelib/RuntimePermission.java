package com.dev.baqari.runtimelib;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.dev.baqari.runtimelib.callbacks.OnDenyPermissions;
import com.dev.baqari.runtimelib.callbacks.OnFailure;
import com.dev.baqari.runtimelib.callbacks.OnGrantPermissions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RuntimePermission {
    private static int requestCode = 0;
    private static RuntimePermissions runtimePermissions = new RuntimePermissions();
    private static RuntimePermission runtimePermission = new RuntimePermission();
    private static List<String> grantedPermissions = new ArrayList<>();
    private static List<String> deniedPermissions = new ArrayList<>();
    private static int counter = 0;
    private static Builder builder;
    private static OnDenyPermissions denyPermissions;
    private static OnGrantPermissions grantPermissions;
    private static OnFailure failure;
    @SuppressLint("UseSparseArrays")
    private static Map<Integer, String> allPermissions = new
            HashMap<>();

    public static Builder builder() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (builder == null)
                builder = new Builder();
        }
        return builder;
    }

    public static class Builder {

        public RuntimePermissions requestCode(int code) {
            requestCode = code;
            return runtimePermissions;
        }
    }

    public static final class RuntimePermissions {
        public RuntimePermissions permission(String permission) {
            allPermissions.put(counter, permission);
            counter++;
            return runtimePermissions;
        }

        public RuntimePermission callBack(OnGrantPermissions onGrantPermissions, OnDenyPermissions onDenyPermissions, OnFailure onFailure) {
            grantPermissions = onGrantPermissions;
            denyPermissions = onDenyPermissions;
            failure = onFailure;
            return runtimePermission;
        }
    }

    public void onPermissionsResult(int code, String[] permissions, int[] grantResults) {
        if (requestCode == code) {
            for (int i = 0; i < grantResults.length; i++) {
                int grantType = grantResults[i];
                String permission = permissions[i];
                if (grantType == PackageManager.PERMISSION_GRANTED) {
                    grantedPermissions.add(permission);
                } else {
                    deniedPermissions.add(permission);
                }
            }
            grantPermissions.get(grantedPermissions);
            denyPermissions.get(deniedPermissions);
        }
    }

    public void request(Activity activity) {
        try {
            String[] tempArr = new String[allPermissions.size()];
            for (Map.Entry<Integer, String> e : allPermissions.entrySet()) {
                Integer key = e.getKey();
                String value = e.getValue();
                if (ContextCompat.checkSelfPermission(activity, value) == PackageManager.PERMISSION_DENIED) {
                    tempArr[key] = value;
                }
            }
            ActivityCompat.requestPermissions(activity, tempArr, requestCode);
        } catch (Exception e) {
            failure.fail(e);
        }
    }
}
