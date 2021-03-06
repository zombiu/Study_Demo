package com.hugo.study_richtext;

import android.text.InputFilter;
import android.text.Spanned;

import com.blankj.utilcode.util.LogUtils;

/**
 * public abstract CharSequence filter (
 *     CharSequence source,  //输入的文字
 *     int start,  //输入的文字 开始位置
 *     int end,  //输入的文字 结束位置
 *     Spanned dest, //当前显示的内容
 *     int dstart,  //当前显示的内容  开始位置
 *     int dend //当前显示的内容  结束位置
 * );
 * <p>
 * 作者：兰兰笑笑生
 * 链接：https://juejin.cn/post/6844903727812329485
 * 来源：掘金
 * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
 */
public class TextLengthFilter implements InputFilter {
    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        LogUtils.e("-->>source=" + source + ",start=" + start + ",end=" + end);
        LogUtils.e("-->>dest=" + dest + ",dstart=" + dstart + ",dend=" + dend);
        // 剩余的文本长度
        int keep = LimitEditUtils.MAX_LENGTH - (dest.length() - (dend - dstart));
        // 没有剩余
        if (keep <= 0) {
            return "";
        } else if (keep >= end - start) {
            // 剩余长度 大于 要添加的文本长度
            return null;
        } else {
            keep += start;
            if (Character.isHighSurrogate(source.charAt(keep - 1))) {
                --keep;
                if (keep == start) {
                    return "";
                }
            }
            return source.subSequence(start, keep);
        }
    }
}
