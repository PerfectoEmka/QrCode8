package com.example.qrcode8;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.qrcode8.databinding.ActivityMainBinding;
import com.google.zxing.BarcodeFormat;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanIntentResult;
import com.journeyapps.barcodescanner.ScanOptions;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private ActivityResultLauncher<ScanOptions> barCodeLauncher = registerForActivityResult(new ScanContract(),
            new ActivityResultCallback<ScanIntentResult>() {
                @Override
                public void onActivityResult(ScanIntentResult result) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(result.getContents()));
                    intent.setPackage("com.instagram.android");

                    try {
                        startActivity(intent);
                    } catch (ActivityNotFoundException e) {
                        startActivity(new Intent(Intent.ACTION_VIEW,
                                Uri.parse(result.getContents())));
                    }
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        generateQr();

        binding.btnScan.setOnClickListener(v -> {
            ScanOptions scanOptions = new ScanOptions();
            scanOptions.setDesiredBarcodeFormats(ScanOptions.QR_CODE);
            scanOptions.setPrompt("Сканировать");
            scanOptions.setCameraId(0);
            scanOptions.setBeepEnabled(true);
            barCodeLauncher.launch(scanOptions);
        });
    }


    private void generateQr(){
        try {
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.encodeBitmap("https://instagram.com/boris.raw?utm_medium=copy_link"
                    , BarcodeFormat.QR_CODE
                    , 200
                    , 200);
            binding.qrCodeIV.setImageBitmap(bitmap);
        }catch (Exception e){
            Log.e("TAG", "generateQr: ", e.fillInStackTrace());
        }
    }
}