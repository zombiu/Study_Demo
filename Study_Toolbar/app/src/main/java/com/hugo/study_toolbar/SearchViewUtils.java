package com.hugo.study_toolbar;

import android.util.TypedValue;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.widget.SearchView;

public class SearchViewUtils {

    public static void setSearchViewStyle(SearchView searchView) {
        //去除margin
        LinearLayout searchEditFrame = (LinearLayout) searchView.findViewById(R.id.search_edit_frame); // Get the Linear Layout
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) searchEditFrame.getLayoutParams();
        layoutParams.leftMargin = 0;
        layoutParams.rightMargin = 0;
        searchEditFrame.setLayoutParams(layoutParams);
        //去除padding
        SearchView.SearchAutoComplete textView = (SearchView.SearchAutoComplete) searchView.findViewById(R.id.search_src_text);
        textView.setPadding(0, 0, 0, 0);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
        textView.setHintTextColor(searchView.getContext().getResources().getColor(android.R.color.darker_gray));
//        textView.setHint(searchView.getContext().getString(R.string.search));
        // 去掉下划线
        searchView.findViewById(R.id.search_plate).setBackground(null);
//        ((ImageView) searchView.findViewById(R.id.search_button)).setImageResource(R.drawable.actionbar_search_dark_icon);
        ((ImageView) (searchView.findViewById(R.id.search_close_btn))).setImageResource(R.mipmap.ic_launcher_round);
    }

    /*public static void changeSearchCursor(SearchView searchView) {
        AutoCompleteTextView searchTextView = (AutoCompleteTextView) searchView.findViewById(androidx.appcompat.R.id.search_src_text);
        try {
            Field mCursorDrawableRes = TextView.class.getDeclaredField("mCursorDrawableRes");
            mCursorDrawableRes.setAccessible(true);
            mCursorDrawableRes.set(searchTextView, R.drawable.search_cursor);
        } catch (Exception e) {
        }
    }*/
}
