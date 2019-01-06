package com.ino.qrmon;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class MainActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    private final static int CAMERA_PERMISSION = 0x5;
    private ZXingScannerView mScannerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
        askForPermission();
    }

    public void askForPermission() {
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                    Manifest.permission.CAMERA)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                Toast.makeText(this, "QRmon GO needs camera permission.", Toast.LENGTH_SHORT).show();
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.CAMERA},
                        CAMERA_PERMISSION);
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.CAMERA},
                        CAMERA_PERMISSION);
                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case CAMERA_PERMISSION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Permission Granted.", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(this, "Permission NOT Granted.", Toast.LENGTH_SHORT).show();
                }
                break;
            }
            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    public void QrScan(View view) {
        mScannerView = new ZXingScannerView(this); //initialize scanner view
        setContentView(mScannerView);
        mScannerView.setResultHandler(this); //register ourselves as a handler for scan results
        mScannerView.startCamera(); //start camera
    }
    @Override
    public void onPause() {
        super.onPause();
        if(mScannerView != null) {
            mScannerView.stopCamera();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if(mScannerView != null) {
            mScannerView.startCamera();
        }
    }

    @Override
    public void handleResult(Result rawResult) {
        String code;
        onPause();
        // Do something with the result here
        Log.d("QR_SCAN_RESULT", rawResult.getText());
        code = "quiz_" + rawResult.getText();

        ImageView image = new ImageView(this);
        //image.findViewById(R.drawable.main_logo);
        image.setImageResource(getResources().getIdentifier(code, "drawable", "com.ex.qrmongo"));
        //show the scanner result into dialog box
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //builder.setIcon(R.mipmap.icon);
        builder.setTitle("QRmon 발견!");
        builder.setView(image);
        builder.setPositiveButton("닫기", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                setContentView(R.layout.activity_main);
            }
        });
        AlertDialog alert1 = builder.create();
        alert1.show();
        //if you would like to resume scanning, call mScannerView.resumeCameraPreview(this);
        onResume();
        mScannerView.resumeCameraPreview(this);
    }
}
