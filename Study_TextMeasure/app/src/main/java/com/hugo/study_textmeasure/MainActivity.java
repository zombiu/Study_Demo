package com.hugo.study_textmeasure;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    TextView tv1;
    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
         tv1 = findViewById(R.id.tv1);
        final TextView tv2 = findViewById(R.id.tv2);

        // 获取的是view相对text原点的位置
        final Rect rect = new Rect();
        tv1.getPaint().getTextBounds(tv1.getText(),0,tv1.getText().length(),rect);
        Log.e("-->> ", "tv1 显示范围=" + rect.toString());

        Rect rect2 = new Rect();
        tv2.getPaint().getTextBounds(tv2.getText(),0,tv2.getText().length(),rect2);
        Log.e("-->> ", "tv2 显示范围=" + rect2.toString());

        // FontMetrics对象中的字段值 = 对应线条的 Y 坐标值 - baseline的 Y 坐标值
        final Paint.FontMetrics fontMetrics = tv1.getPaint().getFontMetrics();
        tv1.post(new Runnable() {
            @Override
            public void run() {
                // 作用是作为文字显示的基准线
                int baseline = tv1.getBaseline();
                Log.e("-->> ", "tv1 baseline=" + baseline);
                Log.e("-->> ", "tv1 top=" + fontMetrics.top);

                // fontMetrics中的bottom就是相对于 baseline 0，0的 y轴坐标
                // 为了将数字对齐汉字，这里将第二个textview向下移动5
//              ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) tv2.getLayoutParams();
//              params.bottomMargin = (int) (rect.bottom - rect.bottom);
//              tv2.setLayoutParams(params);
            }
        });


        final Paint.FontMetrics fontMetrics2 = tv2.getPaint().getFontMetrics();
        tv2.post(new Runnable() {
            @Override
            public void run() {
                int baseline = tv1.getBaseline();
                Log.e("-->> ", "tv2 baseline=" + baseline);
                Log.e("-->> ", "tv1 top=" + fontMetrics2.top);
            }
        });
    }
}