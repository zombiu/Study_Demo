package com.hugo.study_recyclerview.search;

import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class QueryTextBinding implements SearchView.OnQueryTextListener {
    private LiveData<String> keywordLivedata;

    private QueryTextBinding(LiveData<String> keywordLivedata) {
        this.keywordLivedata = keywordLivedata;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        ((MutableLiveData) keywordLivedata).postValue(newText);
        return false;
    }

    public static void bind(SearchView searchView, LiveData<String> keywordLivedata) {
        QueryTextBinding queryTextBinding = new QueryTextBinding(keywordLivedata);
        searchView.setOnQueryTextListener(queryTextBinding);
    }
}
