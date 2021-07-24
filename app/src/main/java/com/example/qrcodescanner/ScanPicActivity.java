package com.example.qrcodescanner;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.zxing.*;
import com.google.zxing.common.HybridBinarizer;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.Reader;

public class ScanPicActivity extends AppCompatActivity {
    private static final int RESULT_LOAD_IMG = 1;
    private Button btnSelectPic;
    private TextView tvResult;
    private ImageView ivQRCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_pic);

        btnSelectPic = (Button)findViewById(R.id.btnSelectPic);
        tvResult = (TextView)findViewById(R.id.tvResult);
        ivQRCode = (ImageView)findViewById(R.id.ivQRCode);

        btnSelectPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickPhoto();
            }
        });
    }

    public void pickPhoto(){
        Intent photoPickerIntent = new Intent(Intent.ACTION_GET_CONTENT);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, RESULT_LOAD_IMG);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMG && resultCode == RESULT_OK){
            try {
                final Uri imageUri = data.getData();
                final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                ivQRCode.setImageBitmap(selectedImage);
                tvResult.setText(bitmapDecodeToText(selectedImage));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(ScanPicActivity.this, "Picture Not Found!", Toast.LENGTH_LONG).show();
            } catch (NotFoundException e){
                e.printStackTrace();
                Toast.makeText(ScanPicActivity.this, "The Picture Can't be Decoded!", Toast.LENGTH_LONG).show();
            }
        }
    }

    public String bitmapDecodeToText(Bitmap bitmap) throws NotFoundException {
        String resultText = null;
        int[] pixels = new int[bitmap.getWidth()*bitmap.getHeight()];
        bitmap.getPixels(pixels, 0, bitmap.getWidth(),0,0, bitmap.getWidth(), bitmap.getHeight());
        LuminanceSource source = new RGBLuminanceSource(bitmap.getWidth(), bitmap.getHeight(), pixels);
        BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(source));

        MultiFormatReader reader = new MultiFormatReader();
        Result result = reader.decode(binaryBitmap);
        resultText = result.getText();
        return resultText;
    }
}