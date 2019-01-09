package com.ino.qrmon;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

public class QRdexActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        BaseAdapter gridAdapter = new QRAdapter(this);
        Intent intent = getIntent();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrdex);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);

        GridView gridView = findViewById(R.id.dexContent);
        TextView textView = findViewById(R.id.dexTitle);

        textView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                AlertDialog resetAlert;
                AlertDialog.Builder builder = new AlertDialog.Builder(QRdexActivity.this);
                TextView resetPop = new TextView(QRdexActivity.this);

                resetPop.setText(R.string.qrdex_reset);
                resetPop.setTextSize(24);
                resetPop.setPadding(15, 5, 15, 5);
                resetPop.setGravity(Gravity.CENTER);

                builder.setTitle("주의!");
                builder.setView(resetPop);
                builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        SharedPreferences pref;
                        SharedPreferences.Editor editor;

                        pref = getApplicationContext().getSharedPreferences("qrDex", 0); // 0 - for private mode
                        editor = pref.edit();

                        for(int j = 0; j < 100; j++)
                            editor.putBoolean("q" + String.valueOf(j), false);
                        editor.apply();
                        dialogInterface.dismiss();
                        Intent intent = new Intent( QRdexActivity.this, MainActivity.class);
                        startActivity(intent);
                    }
                });
                builder.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                lp.width = 240;
                lp.height = 180;
                lp.gravity = Gravity.CENTER;
                lp.x = 0;
                lp.y = 0;

                resetAlert = builder.create();
                resetAlert.getWindow().setAttributes(lp);
                resetAlert.show();
                return true;
            }
        });

        if(intent.getBooleanExtra("update", false)) {
            gridAdapter.notifyDataSetChanged();
            gridView.invalidateViews();
        }
        gridView.setAdapter(gridAdapter);
    }
}
