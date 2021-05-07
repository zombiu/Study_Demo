package com.bobomee.android.mentions.edit.listener;

import android.util.Log;
import android.view.KeyEvent;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputConnectionWrapper;

import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.LogUtils;
import com.bobomee.android.mentions.edit.MentionEditText;
import com.bobomee.android.mentions.edit.util.RangeManager;
import com.bobomee.android.mentions.model.Range;


public class MentionInputConnection extends InputConnectionWrapper {
    private final MentionEditText mEditText;
    private final RangeManager mRangeManager;

    public MentionInputConnection(InputConnection target, boolean mutable, MentionEditText editText) {
        super(target, mutable);
        this.mEditText = editText;
        this.mRangeManager = editText.getRangeManager();
    }

    /**
     * 删除时，需要判断 #tag# 是否是选中状态，选中的tag直接删除，未选中的tag需要先选中
     *
     * @param event
     * @return
     */
    @Override
    public boolean sendKeyEvent(KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_DEL) {
            if (null != mRangeManager) {
                // selectionStart表示在选择过程中不变的光标位置
                // selectionEnd表示在选择过程中移动的位置
                int selectionStart = mEditText.getSelectionStart();
                int selectionEnd = mEditText.getSelectionEnd();
                LogUtils.e("-->>sendKeyEvent selectionStart=" + selectionStart + " , selectionEnd=" + selectionEnd);
                Range closestRange = mRangeManager.getRangeOfClosestMentionString(selectionStart, selectionEnd);
                if (closestRange == null) {
                    mEditText.setSelected(false);
                    return super.sendKeyEvent(event);
                }
                Log.e("-->>", "选中的range " + GsonUtils.toJson(closestRange));
                //if mention string has been selected or the cursor is at the beginning of mention string, just use default action(delete)
                if (mEditText.isSelected() || selectionStart == closestRange.getFrom()) {
                    mEditText.setSelected(false);
                    // 删除选中的 #tag#
                    return super.sendKeyEvent(event);
                } else {
                    //select the mention string
                    mEditText.setSelected(true);
                    mRangeManager.setLastSelectedRange(closestRange);
                    setSelection(closestRange.getTo(), closestRange.getFrom());
                }
                return true;
            }
        }
        return super.sendKeyEvent(event);
    }

    @Override
    public boolean deleteSurroundingText(int beforeLength, int afterLength) {
        if (beforeLength == 1 && afterLength == 0) {
            return sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DEL)) && sendKeyEvent(
                    new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_DEL));
        }
        return super.deleteSurroundingText(beforeLength, afterLength);
    }
}