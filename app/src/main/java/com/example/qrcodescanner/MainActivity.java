package com.example.qrcodescanner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private Button btnQRScan;
    private TextView tvToolbarTitle;
    private Toolbar toolbar;
    private Button btnScanPic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnQRScan = findViewById(R.id.btnQRScan);
        btnScanPic = findViewById(R.id.btnScanPic);
        tvToolbarTitle = findViewById(R.id.tvToolbarTitle);
        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        tvToolbarTitle.setText("中國信託");

        btnQRScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startQRScanActivity();
            }
        });

        btnScanPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startScanPicActivity();
            }
        });
    }

    public void startQRScanActivity(){
        Intent qrIntent = new Intent(MainActivity.this, QRScanActivity.class );
        MainActivity.this.startActivity(qrIntent);
    }

    public void startScanPicActivity(){
        Intent scanPicIntent = new Intent(MainActivity.this, ScanPicActivity.class);
        MainActivity.this.startActivity(scanPicIntent);
    }
}