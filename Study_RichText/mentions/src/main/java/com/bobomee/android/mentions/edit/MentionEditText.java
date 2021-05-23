/*
 * Copyright 2016 Andy
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.bobomee.android.mentions.edit;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Color;
import android.nfc.Tag;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatEditText;

import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.LogUtils;
import com.bobomee.android.mentions.edit.listener.InsertData;
import com.bobomee.android.mentions.edit.listener.LinkTouchMethod;
import com.bobomee.android.mentions.edit.listener.MentionInputConnection;
import com.bobomee.android.mentions.edit.listener.MentionTextWatcher;
import com.bobomee.android.mentions.edit.listener.SpanClickHelper;
import com.bobomee.android.mentions.edit.util.ClipboardHelper;
import com.bobomee.android.mentions.edit.util.FormatRangeManager;
import com.bobomee.android.mentions.edit.util.RangeManager;
import com.bobomee.android.mentions.model.FormatRange;
import com.bobomee.android.mentions.model.Range;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 */
public class MentionEditText extends AppCompatEditText {
    private Runnable mAction;

    private boolean mIsSelected;
    private ClipboardManager mClipboardManager;
    private MentionInputConnection mentionInputConnection;
    private GestureDetector gestureDetector;
    private MotionEvent upMotionEvent;

    public MentionEditText(Context context) {
        super(context);
        init();
    }

    public MentionEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MentionEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    /**
     * 监听删除输入事件
     *
     * @param outAttrs
     * @return
     */
    @Override
    public InputConnection onCreateInputConnection(EditorInfo outAttrs) {
        mentionInputConnection = new MentionInputConnection(super.onCreateInputConnection(outAttrs), true, this);
        return mentionInputConnection;
    }

    @Override
    public void setText(final CharSequence text, BufferType type) {
        super.setText(text, type);
        //hack, put the cursor at the end of text after calling setText() method
        if (mAction == null) {
            mAction = new Runnable() {
                @Override
                public void run() {
                    setSelection(getText().length());
                }
            };
        }
        post(mAction);
    }

    @Override
    protected void onSelectionChanged(int selStart, int selEnd) {
        super.onSelectionChanged(selStart, selEnd);
        LogUtils.e("-->> selStart=" + selStart + ",selEnd=" + selEnd);
        //avoid infinite recursion after calling setSelection()
        if (null != mRangeManager && !mRangeManager.isEqual(selStart, selEnd)) {
            //if user cancel a selection of mention string, reset the state of 'mIsSelected'
            Range closestRange = mRangeManager.getRangeOfClosestMentionString(selStart, selEnd);
            LogUtils.e("-->>" + GsonUtils.toJson(closestRange));
            if (closestRange != null && closestRange.getTo() == selEnd) {
                mIsSelected = false;
            }

            Range nearbyRange = mRangeManager.getRangeOfNearbyMentionString(selStart, selEnd);
            //if there is no mention string nearby the cursor, just skip
            if (null != nearbyRange) {
                //forbid cursor located in the mention string.
                // 禁止游标移动到字符串中
                if (selStart == selEnd) {
                    setSelection(nearbyRange.getAnchorPosition(selStart));
                } else {
                    /*if (selEnd < nearbyRange.getTo()) {
                        setSelection(selStart, nearbyRange.getTo());
                    }
                    if (selStart > nearbyRange.getFrom()) {
                        setSelection(nearbyRange.getFrom(), selEnd);
                    }*/
                    if (selStart >= nearbyRange.getFrom() && selEnd < nearbyRange.getTo()) {
                        setSelection(nearbyRange.getFrom(), nearbyRange.getTo());
                    } else if (selEnd < nearbyRange.getTo()) {
                        setSelection(selStart, nearbyRange.getTo());
                    } else if (selStart > nearbyRange.getFrom()) {
                        setSelection(nearbyRange.getFrom(), selEnd);
                    }
                }
            }
        }
    }


    public void insert(InsertData insertData) {
        if (null != insertData) {
            CharSequence charSequence = insertData.charSequence();
            Editable editable = getText();
            final int start = getSelectionStart();
            final int oldEnd = getSelectionEnd();
            final int end = start + charSequence.length();

            // 如果是选中状态
            if (start != oldEnd) {
                replaceSelection(insertData);
                return;
            }

            LogUtils.e("-->>start=" + start + ",end=" + oldEnd);
            editable.insert(start, charSequence);
            // 加一个空格
//            editable.insert(end, " ");
            FormatRange.FormatData format = insertData.formatData();
            final FormatRange range = new FormatRange(start, end);
            range.setInsertData(insertData);
            range.setConvert(format);
            range.setRangeCharSequence(charSequence);
            mRangeManager.add(range);

            //设置部分文字点击事件
            ClickableSpan clickableSpan = new ClickableSpan() {
                @Override
                public void onClick(View widget) {
                    LogUtils.e("-->>点击了话题");
                    widget.post(new Runnable() {
                        @Override
                        public void run() {
                            // 这里不能使用 start 和 end，因为选中再进行输入操作时，有可能会改变range的的范围
                            setSelection(range.getFrom(), range.getTo());
                            LogUtils.e("-->>选中 start=" + start + ",end=" + end);
                        }
                    });
                }

                @Override
                public void updateDrawState(@NonNull TextPaint ds) {
                    ds.setUnderlineText(false);
                }
            };
            editable.setSpan(clickableSpan, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            int color = insertData.color();
            editable.setSpan(new ForegroundColorSpan(color), start, end,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        }
    }

    private void replaceSelection(InsertData insertData) {
        CharSequence charSequence = insertData.charSequence();
        Editable editable = getText();
        final int start = getSelectionStart();
        final int oldEnd = getSelectionEnd();
        final int end = start + charSequence.length();
        // 先删除 后插入
        editable.delete(start, oldEnd);
        editable.insert(start, charSequence);
        LogUtils.e("-->>" + editable);

        FormatRange.FormatData format = insertData.formatData();
        FormatRange range = new FormatRange(start, end);
        range.setInsertData(insertData);
        range.setConvert(format);
        range.setRangeCharSequence(charSequence);
        mRangeManager.add(range);

        //设置部分文字点击事件
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                widget.post(new Runnable() {
                    @Override
                    public void run() {
                        setSelection(start, end);
                        LogUtils.e("-->>选中");
                    }
                });
            }

            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                ds.setUnderlineText(false);
            }
        };
        editable.setSpan(clickableSpan, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        int color = insertData.color();
        editable.setSpan(new ForegroundColorSpan(color), start, end,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        /*int oldSelRange = oldEnd - start;
        int newSelRange = charSequence.length();
        // 对所有range 进行偏移
        if (newSelRange != oldSelRange) {
            int offset = newSelRange - oldSelRange;
            ArrayList<Range> ranges = (ArrayList<Range>) mRangeManager.get();
            for (Range element : ranges) {
                if (element.getFrom() >= end) {
                    element.setOffset(offset);
                }
            }
        }*/

        setSelection(start, end);
    }

    public void insert(CharSequence charSequence) {
        insert(new Default(charSequence));
    }

    class Default implements InsertData {

        private final CharSequence charSequence;

        public Default(CharSequence charSequence) {
            this.charSequence = charSequence;
        }

        @Override
        public CharSequence charSequence() {
            return charSequence;
        }

        @Override
        public FormatRange.FormatData formatData() {
            return new DEFAULT();
        }

        @Override
        public int color() {
            return Color.RED;
        }

        class DEFAULT implements FormatRange.FormatData {
            @Override
            public CharSequence formatCharSequence() {
                return charSequence;
            }
        }
    }

    public CharSequence getFormatCharSequence() {
        String text = getText().toString();
        return mRangeManager.getFormatCharSequence(text);
    }

    public void clear() {
        mRangeManager.clear();
        setText("");
    }

    protected FormatRangeManager mRangeManager;

    private void init() {
        mClipboardManager = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
        mRangeManager = new FormatRangeManager();
        //disable suggestion
        addTextChangedListener(new MentionTextWatcher(this));

        // 长按监听 去除复制的格式
        setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                LogUtils.e("-->>edittext 长按监听");
                String clipText = ClipboardHelper.getInstance(getContext()).getClipText(getContext());
                if (!TextUtils.isEmpty(clipText)) {
                    // 保存无格式的 text
                    ClipData simple_text = ClipData.newPlainText("simple_text", clipText);
                    mClipboardManager.setPrimaryClip(simple_text);
                }
                // 返回true，将拦截长按复制、粘贴功能
                return false;
            }
        });

        // 点击话题选中后，点击一次edittext不响应点击事件 ，再点一次才会响应
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                LogUtils.e("-->>onClick");
//                setSelected(false);
                // 重置索引保护
                if (mentionInputConnection != null) {
                    mentionInputConnection.resetLastIndex();
                }

                if (upMotionEvent != null) {
                    SpanClickHelper.spanClickHandle(MentionEditText.this, upMotionEvent);
                    upMotionEvent = null;
                }
            }
        });

        // 无法实现需求，因为onTouch的up事件返回true时，不会进入onTouchEvent方法，无法取消长按的监听，所以就触发了onLongClick
//        setOnTouchListener(new LinkTouchMethod());

        gestureDetector = new GestureDetector(getContext(), new GestureDetector.OnGestureListener() {
            @Override
            public boolean onDown(MotionEvent e) {
                return false;
            }

            @Override
            public void onShowPress(MotionEvent e) {
            }

            @Override
            public boolean onSingleTapUp(MotionEvent e) {
//                upMotionEvent = e;
                LogUtils.e("-->>点击up " + e.toString());
                return false;
            }

            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                return false;
            }

            @Override
            public void onLongPress(MotionEvent e) {
            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                return false;
            }
        });

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getActionMasked();
        if (action == MotionEvent.ACTION_UP) {
            this.upMotionEvent = event;
        }
        /*if (gestureDetector != null) {
            gestureDetector.onTouchEvent(event);
        }*/
        return super.onTouchEvent(event);
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////

    public RangeManager getRangeManager() {
        return mRangeManager;
    }

    @Override
    public boolean isSelected() {
        return mIsSelected;
    }

    @Override
    public void setSelected(boolean selected) {
        mIsSelected = selected;
    }

    public void restore(String content, List<Range> ranges) {
        SpannableStringBuilder builder = new SpannableStringBuilder(content);
        Collections.sort(ranges);
        for (Range range : ranges) {
            String matchTag = range.getInsertData().charSequence().toString();
            String substring = content.substring(range.getFrom(), range.getTo());
            if (TextUtils.equals(matchTag, substring)) {
                LogUtils.e("-->> " + range.getInsertData().charSequence() + " , start=" + range.getFrom() + ", end=" + range.getTo());
                mRangeManager.add(range);
                // 设置颜色
                int color = range.getInsertData().color();
                builder.setSpan(new ForegroundColorSpan(color), range.getFrom(), range.getTo(),
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
        setText(builder);
    }

    public void restore(String content) {
        SpannableStringBuilder builder = new SpannableStringBuilder(content);
        ArrayList<? extends Range> ranges = mRangeManager.get();
        Collections.sort(ranges);
        for (Range range : ranges) {
            String matchTag = range.getInsertData().charSequence().toString();
            String substring = content.substring(range.getFrom(), range.getTo());
            if (TextUtils.equals(matchTag, substring)) {
                LogUtils.e("-->> " + range.getInsertData().charSequence() + " , start=" + range.getFrom() + ", end=" + range.getTo());
                mRangeManager.add(range);
                // 设置颜色
                int color = range.getInsertData().color();
                builder.setSpan(new ForegroundColorSpan(color), range.getFrom(), range.getTo(),
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
        setText(builder);
    }
}
