package com.hugo.study_richtext;

import android.text.InputFilter;
import android.widget.EditText;

public class InputUtils {

    public static void setMaxLength(EditText editText) {
        editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(20)});
    }
}
