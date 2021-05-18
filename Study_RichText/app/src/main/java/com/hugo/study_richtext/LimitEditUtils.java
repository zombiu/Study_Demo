package com.hugo.study_richtext;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.blankj.utilcode.util.ToastUtils;

import java.util.HashMap;
import java.util.Iterator;

/**
 * 限制多个edit text 输入的长度
 */
public class LimitEditUtils {
    private static LimitEditUtils limitEditUtils;
    public static final int MAX_LENGTH = 20;

    private int total_length = 0;

    public HashMap<EditText, LengthTextWatcher> watcherMap = new HashMap<>();

    private LimitEditUtils() {

    }

    public static LimitEditUtils getInstance() {
        if (limitEditUtils == null) {
            synchronized (LimitEditUtils.class) {
                if (limitEditUtils == null) {
                    limitEditUtils = new LimitEditUtils();
                }
            }
        }
        return limitEditUtils;
    }

    public synchronized int getTotalLength() {
        Iterator<LengthTextWatcher> iterator = watcherMap.values().iterator();
        int length = 0;
        while (iterator.hasNext()) {
            LengthTextWatcher next = iterator.next();
            length += next.getLength();
        }
        return length;
    }

    public synchronized void registerWatcher(EditText editText) {
        if (editText == null) {
            return;
        }
        TextWatcher watcher = watcherMap.get(editText);
        if (watcher != null) {
            return;
        }

        LengthTextWatcher newTextWatcher = new LengthTextWatcher(editText);
        editText.addTextChangedListener(newTextWatcher);
        watcherMap.put(editText, newTextWatcher);
    }

    public synchronized void removeWatcher(EditText editText) {
        watcherMap.remove(editText);
    }

    public static class LengthTextWatcher implements TextWatcher {
        private int length = 0;
        private final EditText editText;

        public LengthTextWatcher(EditText editText) {
            this.editText = editText;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (editText == null) {
                return;
            }
            int newLength = s.length();
            int newAllLength = LimitEditUtils.getInstance().getTotalLength() + newLength - length;
            int capacity = newAllLength - MAX_LENGTH;
            // 超出最大长度，截断
            if (capacity > 0) {
                editText.setText(s.toString().substring(0, newLength - capacity));
                ToastUtils.showShort("超出了");
            } else {
                length = newLength;
            }

        }

        public int getLength() {
            return length;
        }
    }
}
