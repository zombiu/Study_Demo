package com.bobomee.android.mentions.text.listener;

import android.text.Spanned;

public interface ParserConverter {

    Spanned convert(CharSequence source);
}
