package com.hugo.study_toolbar.utils;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.Log;
import android.view.View;

public class ViewToBitmap {

    public static Bitmap saveViewAsBitmap(View view) {
        int width = view.getWidth();
        int height = view.getHeight();
        if (width <= 0 || height <= 0) {
            Log.i("-->>", "size is unknown, maybe wrap_content, will measure");
            int specSize = View.MeasureSpec.makeMeasureSpec(0 /* any */, View.MeasureSpec.UNSPECIFIED);
            view.measure(specSize, specSize);
            width = view.getMeasuredWidth();
            height = view.getMeasuredHeight();
        }
        Log.i("-->>", "view size is w " + width + " h " + height);
        if (width <= 0 || height <= 0) {
            Log.e("-->>", "invalid size, do nothing, return null");
            return null;
        }

        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        if (view.getRight() <= 0 || view.getBottom() <= 0) {
            view.layout(0, 0, width, height);
            view.draw(canvas);
        } else {
            view.layout(view.getLeft(), view.getTop(), view.getRight(), view.getBottom());
            view.draw(canvas);
        }

        return bitmap;
    }

    public static Bitmap saveViewAsBitmap2(View view) {
        view.setDrawingCacheEnabled(true);
        //启用DrawingCache并创建位图
        view.buildDrawingCache() ;
        Bitmap drawingCache = view.getDrawingCache();
        // 延迟2s再关闭 否则 getDrawingCache 可能为空
//        view.setDrawingCacheEnabled(false);
        return Bitmap.createBitmap(drawingCache);
    }
}
