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
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

public class QRdexActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        BaseAdapter gridAdapter = new QRAdapter(this);
        final Intent intent = getIntent();
        SharedPreferences pref;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrdex);
        hideSystemUI();

        GridView gridView = findViewById(R.id.dexContent);
        TextView textView = findViewById(R.id.dexReset);
        TextView title = findViewById(R.id.dexTitle);

        pref = getApplicationContext().getSharedPreferences("qrDex", 0); // 0 - for private mode
        String titleText = " (" + String.valueOf(pref.getInt("QRmonCount", -1) + "/100)");
        titleText = getString(R.string.title_qrdex) + titleText;
        title.setText(titleText);

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

                        editor.putInt("QRmonCount", 0);
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
                resetAlert.getWindow().getDecorView().setSystemUiVisibility(
                        View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
                resetAlert.show();
                return true;
            }
        });

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                SharedPreferences pref;
                SharedPreferences.Editor editor;

                pref = getApplicationContext().getSharedPreferences("qrDex", 0); // 0 - for private mode

                if(pref.getBoolean("q" + String.valueOf(position), false)) {
                    Intent intent1 = new Intent(getApplicationContext(), QRviewActivity.class);
                    intent1.putExtra("qrNum", position);
                    startActivity(intent1);
                }
                else
                    Toast.makeText(getApplicationContext(), "아직 발견하지 않은 QRmon 입니다!", Toast.LENGTH_LONG).show();
            }
        });

        if(intent.getBooleanExtra("update", false)) {
            gridAdapter.notifyDataSetChanged();
            gridView.invalidateViews();
        }
        gridView.setAdapter(gridAdapter);
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
