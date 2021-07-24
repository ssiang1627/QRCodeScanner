package com.example.qrcodescanner;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import org.jetbrains.annotations.NotNull;

public class PermissionHelper{
    private Context context;
    private static final int CAMERA_REQUEST_CODE = 100;
    private static final String TAG = "PermissionHelper";

    public PermissionHelper(Context context)
    {
        this.context = context;
    }

    public void requestPermission() {
        Log.d(TAG, "requestPermission");
        Activity activity = (Activity) context;
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CAMERA}, CAMERA_REQUEST_CODE);
        }
    }
}

