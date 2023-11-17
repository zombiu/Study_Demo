package com.hugo.study_toolbar.widget;

import android.graphics.Rect;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * 用来设置最左、最右item贴边，中间item平分空白区域
 * 例如 recyclerView宽度320dp  有4列，默认每个item分到的宽度就是80dp
 * item默认宽度是50dp ，也就是说每个item剩余30dp的分配空间， 但是要求第0和第3列 贴边，0和1，1和2，2和3 中间的空白部分均分 ，也就是说中间的空间每个要求是40
 * 50--40--50--40--50--40--50
 * 但是因为 默认每个item分到的宽度就是80dp
 * 剩余30dp的分配空间，要保证每个 item   outRect.left + outRect.right = 30dp的空白空间
 * 50--30 + 10--50--20 + 20--50--10 + 30--50
 * 很有帮助
 *
 * GridLayoutManager 平分后，view 的大小就固定了，而我们修改的是View 的内边距，如果不设置就是view 的原大小
 * 每个item的宽度+outRect.left + outRect.right= 总宽度 都是一样大小
 * 各列的间距相等，即前列的right + 后列的left = 列间距；
 */
public class GridSpaceItemDecoration extends RecyclerView.ItemDecoration {

    private final String TAG = "GridSpaceItemDecoration";

    private int mSpanCount;//横条目数量
    private int mRowSpacing;//行间距
    private int mColumnSpacing;// 列间距

    /**
     * @param spanCount     列数
     * @param rowSpacing    行间距
     * @param columnSpacing 列间距
     */
    public GridSpaceItemDecoration(int spanCount, int rowSpacing, int columnSpacing) {
        this.mSpanCount = spanCount;
        this.mRowSpacing = rowSpacing;
        this.mColumnSpacing = columnSpacing;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view); // 获取view 在adapter中的位置。
        int column = position % mSpanCount; // view 所在的列

        outRect.left = column * mColumnSpacing / mSpanCount; // column * (列间距 * (1f / 列数))
        outRect.right = mColumnSpacing - (column + 1) * mColumnSpacing / mSpanCount; // 列间距 - (column + 1) * (列间距 * (1f /列数))

        Log.e(TAG, "position:" + position
                + "    columnIndex: " + column
                + "    left,right ->" + outRect.left + "," + outRect.right);

        // 如果position > 行数，说明不是在第一行，则不指定行高，其他行的上间距为 top=mRowSpacing
        if (position >= mSpanCount) {
            outRect.top = mRowSpacing; // item top
        }
    }
}



