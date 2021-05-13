package com.hugo.study_richtext.test.parser;

import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;

import com.bobomee.android.mentions.text.listener.ParserConverter;

/**
 *
 */
public class Parser implements ParserConverter {

  public Parser() {
  }

  @Override
  public Spanned convert(CharSequence source) {
    if (TextUtils.isEmpty(source)) return new SpannableString("");
    String sourceString = source.toString();
//    sourceString = LinkUtil.replaceUrl(sourceString);

    return Html.fromHtml(sourceString, null, new HtmlTagHandler());
  }
}
