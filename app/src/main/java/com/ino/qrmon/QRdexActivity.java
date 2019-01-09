package com.ino.qrmon;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.GridView;

public class QRdexActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        BaseAdapter gridAdapter = new QRAdapter(this);
        Intent intent = getIntent();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrdex);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);

        GridView gridView = findViewById(R.id.dexContent);

        if(intent.getBooleanExtra("update", false)) {
            gridAdapter.notifyDataSetChanged();
            gridView.invalidateViews();
        }
        gridView.setAdapter(gridAdapter);
    }
}
