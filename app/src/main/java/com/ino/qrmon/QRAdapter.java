package com.ino.qrmon;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import java.lang.reflect.Field;

public class QRAdapter extends BaseAdapter {

    private final int ENTRY_SIZE = 100;
    private Context mContext;

    // Constructor
    public QRAdapter(Context c) {
        mContext = c;
    }

    public int getCount() {
        return ENTRY_SIZE;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        SharedPreferences pref = mContext.getSharedPreferences("qrDex", 0); // 0 - for private mode;
        boolean found = pref.getBoolean("q" + String.valueOf(position), false);
        ImageView imageView;
        TextView textView;
        String filename = "quiz" + String.valueOf(position);
        int quizId;

        try {
            Class res = R.drawable.class;
            Field field = res.getField(filename);
            quizId = field.getInt(null);
        }
        catch (Exception e) {
            Log.e("QRdex", "Failure to get drawable id.", e);
            return null;
        }

        if(found) {
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(240, 180));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(3, 3, 3, 3);
            imageView.setImageResource(quizId);
            return imageView;
        }
        else {
            textView = new TextView(mContext);
            textView.setLayoutParams(new GridView.LayoutParams(240, 180));
            textView.setPadding(3, 3, 3, 3);
            textView.setBackgroundResource(R.drawable.qrbtn);
            textView.setGravity(Gravity.CENTER);
            textView.setTextSize(24);
            textView.setTextColor(Color.BLACK);
            textView.setText(String.valueOf(position + 1));
            return textView;
        }
    }
}
