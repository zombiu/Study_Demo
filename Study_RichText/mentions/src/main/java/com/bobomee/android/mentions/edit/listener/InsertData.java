package com.bobomee.android.mentions.edit.listener;

import com.bobomee.android.mentions.model.FormatRange;


public interface InsertData {

    CharSequence charSequence();

    FormatRange.FormatData formatData();

    int color();
}
