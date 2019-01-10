package com.ino.qrmon;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.lang.reflect.Field;

public class QRviewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrview);
        hideSystemUI();

        Intent intent = getIntent();
        TextView textView = findViewById(R.id.qrTitle);
        ImageView imageView = findViewById(R.id.qrmonImage);

        int target = intent.getIntExtra("qrNum", 1);
        String targetText = getResources().getString(R.string.qrmonNum) + String.valueOf(target + 1);
        String filename = "quiz" + String.valueOf(target);

        try {
            Class res = R.drawable.class;
            Field field = res.getField(filename);
            imageView.setImageResource(field.getInt(null));
        }
        catch (Exception e) {
            Log.e("QRview", "Failure to get drawable id.", e);
            return;
        }

        textView.setText(targetText);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            hideSystemUI();
        }
    }

    private void hideSystemUI() {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
            View.SYSTEM_UI_FLAG_FULLSCREEN
            | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }
}
