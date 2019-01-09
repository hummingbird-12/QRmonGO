package com.ino.qrmon;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.zxing.Result;

import java.lang.reflect.Field;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class QRscanActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    private ZXingScannerView mScannerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
            //if you would like to resume scanning, call mScannerView.resumeCameraPreview(this);
            mScannerView.resumeCameraPreview(this);
        }
    }

    @Override
    public void handleResult(Result rawResult) {
        String code;
        int quizId;

        SharedPreferences pref;
        SharedPreferences.Editor editor;

        onPause();

        // Do something with the result here
        Log.d("QR_SCAN_RESULT", rawResult.getText());

        code = "quiz" + rawResult.getText();
        try {
            Class res = R.drawable.class;
            Field field = res.getField(code);
            quizId = field.getInt(null);
        }
        catch (Exception e) {
            Log.e("QRdex", "Failure to get drawable id: " + code, e);
            Toast.makeText(this, "QRmon이 아니에요!", Toast.LENGTH_LONG).show();
            onResume();
            return;
        }

        pref = getApplicationContext().getSharedPreferences("qrDex", 0); // 0 - for private mode
        editor = pref.edit();
        editor.putBoolean("q" + rawResult.getText(), true);
        editor.apply();

        ImageView image = new ImageView(this);
        image.setImageResource(quizId);

        //show the scanner result into dialog box
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("QRmon 발견!");
        builder.setView(image);

        image.setMaxWidth(480);

        builder.setPositiveButton("닫기", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Intent intent = new Intent( QRscanActivity.this, QRdexActivity.class);
                intent.putExtra("update", true);
                startActivity(intent);
            }
        });

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.width = 240;
        lp.height = 180;

        AlertDialog quizAlert = builder.create();
        quizAlert.getWindow().setAttributes(lp);
        quizAlert.show();
    }
}
