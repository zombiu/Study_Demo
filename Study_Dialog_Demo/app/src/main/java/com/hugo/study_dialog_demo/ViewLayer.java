package com.hugo.study_dialog_demo;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.IdRes;
import androidx.annotation.LayoutRes;
import androidx.core.util.Consumer;

class ViewLayer {

    private int layoutId;
    private ViewGroup rootView;
    private View contentView;
    private Consumer<View> initViewConsumer;

    private ViewLayer(Builder builder) {
        this.layoutId = builder.layoutId;
        this.rootView = builder.rootView;
        this.contentView = builder.contentView;
    }

    private void onAttach(ViewGroup rootView) {
        if (layoutId != 0) {
            LayoutInflater inflater = LayoutInflater.from(rootView.getContext());
            contentView = inflater.inflate(layoutId, rootView);
        }
        if (contentView == null) {
            throw new NullPointerException("contentView不能为null");
        }
        rootView.addView(contentView);
        contentView.setVisibility(View.GONE);
        initViewConsumer.accept(contentView);
    }

    private void onDetach() {

    }


    public void show() {

    }


    public static class Builder {

        private int layoutId;
        private ViewGroup rootView;
        private View contentView;
        private Consumer<View> initViewConsumer;

        public Builder(ViewGroup rootView) {
            this.rootView = rootView;
        }

        public Builder setContentView(View view) {
            this.layoutId = 0;
            this.contentView = view;
            return this;
        }

        public Builder setContentView(@LayoutRes int layoutId) {
            this.contentView = null;
            this.layoutId = layoutId;
            return this;
        }

        public Builder initView(Consumer<View> viewConsumer) {
            this.initViewConsumer = viewConsumer;
            return this;
        }


        public ViewLayer create() {
            ViewLayer viewLayer = new ViewLayer(this);
            viewLayer.onAttach(rootView);
            return viewLayer;
        }

        public void show() {
            create().show();
        }


    }

}
