package com.ino.qrmon;

import android.content.Context;
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
    private boolean found[] = new boolean[ENTRY_SIZE];
    private Context mContext;

    // Constructor
    public QRAdapter(Context c) {
        mContext = c;
    }

    public int getCount() {
        return found.length;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        found[4] = true;
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

        if (convertView == null) {
            if(found[position]) {
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
        else {
            if(found[position]) {
                imageView = (ImageView) convertView;
                imageView.setImageResource(quizId);
                return imageView;
            }
            else {
                textView = (TextView) convertView;
                textView.setText(String.valueOf(position + 1));
                return textView;
            }
        }
    }
}
