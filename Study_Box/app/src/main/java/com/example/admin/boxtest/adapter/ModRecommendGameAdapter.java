package com.example.admin.boxtest.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.admin.boxtest.entity.NormalMultipleEntity;

import java.util.List;

public class ModRecommendGameAdapter extends BaseQuickAdapter<NormalMultipleEntity,BaseViewHolder> {
    public ModRecommendGameAdapter(int layoutResId, @Nullable List<NormalMultipleEntity> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, NormalMultipleEntity item) {

    }
}
