package com.ino.qrmon;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.GridView;

public class QRdexActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrdex);

        GridView gridView = findViewById(R.id.dexContent);
        gridView.setAdapter(new QRAdapter(this));
    }
}
