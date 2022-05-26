package com.example.study_viewmeasure;

import com.blankj.utilcode.util.LogUtils;

import java.math.RoundingMode;
import java.text.DecimalFormat;

public class NumUtils {

    public static void convert(int count) {
        DecimalFormat df = new DecimalFormat("0.00");
        df.setRoundingMode(RoundingMode.DOWN);
        String s = df.format(count / 10000d) + "万";
//        LogUtils.e("--<< " + s);
    }
}

