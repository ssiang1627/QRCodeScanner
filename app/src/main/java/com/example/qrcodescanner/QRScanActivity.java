package com.example.qrcodescanner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.Result;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Text;

import java.security.Permission;
import java.util.List;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class QRScanActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {
    private ZXingScannerView scannerView;
    private TextView tvResult1;
    private TextView tvResult2;
    private TextView tvResult3;
    private Handler pauseHandler = new Handler();
    private Button btnNextStep;
    private int permissionDeniedTimes = 0;
    private static final int CAMERA_REQUEST_CODE = 100;
    private PermissionHelper permissionHelper = new PermissionHelper(QRScanActivity.this);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrscan);
        TextView tvToolbarTitle = findViewById(R.id.tvToolbarTitle);
        Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        tvToolbarTitle.setText("台電電費");

        scannerView = (ZXingScannerView)findViewById(R.id.zxscan);
        tvResult1 = (TextView)findViewById(R.id.tvResult1);
        tvResult2 = (TextView)findViewById(R.id.tvResult2);
        tvResult3 = (TextView)findViewById(R.id.tvResult3);
        btnNextStep = (Button)findViewById(R.id.btnNextStep);

        btnNextStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvResult1.setText("");
                tvResult2.setText("");
                tvResult3.setText("");
                scannerView.resumeCameraPreview(QRScanActivity.this);
            }
        });

        View.OnClickListener tvOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView tv = (TextView) v;
                tv.setText("");
                scannerView.resumeCameraPreview(QRScanActivity.this);
            }
        };

        tvResult1.setOnClickListener(tvOnClickListener);
        tvResult2.setOnClickListener(tvOnClickListener);
        tvResult3.setOnClickListener(tvOnClickListener);

        requestPermission();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void requestPermission(){
            permissionHelper.requestPermission();
//        if (ContextCompat.checkSelfPermission(QRScanActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED){
//            ActivityCompat.requestPermissions(QRScanActivity.this, new String[]{Manifest.permission.CAMERA}, CAMERA_REQUEST_CODE);
//        }else{
//            scannerView.setResultHandler(QRScanActivity.this);
//            scannerView.startCamera();
//        }
//        Dexter.withActivity(this)
//                .withPermission(Manifest.permission.CAMERA)
//                .withListener(new PermissionListener() {
//                    @Override
//                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
//                        scannerView.setResultHandler(QRScanActivity.this);
//                        scannerView.startCamera();
//                    }
//
//                    @Override
//                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
//                        Toast.makeText(QRScanActivity.this,"You must accept this permission", Toast.LENGTH_SHORT).show();
//                    }
//
//                    @Override
//                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
//
//                    }
//                })
//                .check();
    }

    @Override
    protected void onDestroy() {
        scannerView.stopCamera();
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        scannerView.setResultHandler(this);
        scannerView.startCamera();
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        scannerView.stopCamera();
    }

    @Override
    public void handleResult(Result rawResult) {
        if (tvResult1.getText() == ""){
            tvResult1.setText(rawResult.getText());
        }else if (tvResult2.getText() == ""){
            tvResult2.setText(rawResult.getText());
        }else if (tvResult3.getText() == ""){
            tvResult3.setText(rawResult.getText());
            Toast.makeText(QRScanActivity.this,"請點選重置以重新掃描", Toast.LENGTH_SHORT).show();

        }

        if (tvResult1.getText() == "" || tvResult2.getText() == "" || tvResult3.getText() == ""){
            pauseHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    scannerView.resumeCameraPreview(QRScanActivity.this);
                }
            },500);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull @NotNull String[] permissions, @NonNull @NotNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_REQUEST_CODE){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                scannerView.setResultHandler(QRScanActivity.this);
                scannerView.startCamera();
            } else {
                permissionDeniedTimes +=1;
                if (permissionDeniedTimes <=1){
                    Toast.makeText(QRScanActivity.this,"You must accept this permission", Toast.LENGTH_SHORT).show();
                    requestPermission();
                }else {
                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package", getPackageName(), null);
                    intent.setData(uri);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    startActivity(intent);
                }
                Log.d("permissionDeniedTimes","times:" + permissionDeniedTimes);

            }
        }
    }
}