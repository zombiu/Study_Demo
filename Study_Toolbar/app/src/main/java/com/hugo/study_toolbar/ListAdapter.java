package com.hugo.study_toolbar;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ListAdapter extends BaseAdapter {
    private List<Drawable> mDrawableList = new ArrayList<>();
    private int mLength = 0;
    private LayoutInflater mInflater;
    private Context mContext;
    private ListView mListView;
    private Animation animation;
    private int mFirstTop, mFirstPosition;
    private boolean isScrollDown;

    public ListAdapter(Context context, ListView listView, List<Drawable> drawables, int length) {
        mDrawableList.addAll(drawables);
        mLength = length;
        mInflater = LayoutInflater.from(context);
        mContext = context;
        mListView = listView;
        animation = AnimationUtils.loadAnimation(mContext, R.anim.bottom_in_anim);
        mListView.setOnScrollListener(mOnScrollListener);
    }

    AbsListView.OnScrollListener mOnScrollListener = new AbsListView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {

        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            View firstChild = view.getChildAt(0);
            if (firstChild == null) return;
            int top = firstChild.getTop();
            /**
             * firstVisibleItem > mFirstPosition表示向下滑动一整个Item
             * mFirstTop > top表示在当前这个item中滑动
             */
            isScrollDown = firstVisibleItem > mFirstPosition || mFirstTop > top;
            mFirstTop = top;
            mFirstPosition = firstVisibleItem;
        }
    };

    @Override
    public int getCount() {
        return mLength;
    }

    @Override
    public Object getItem(int position) {
        return mDrawableList.get(position % mDrawableList.size());
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;

        if (convertView == null) {

            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.item_layout, null);
            holder.mImageView = (ImageView) convertView.findViewById(R.id.img);
            holder.mTextView = (TextView) convertView.findViewById(R.id.text);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        //清除当前显示区域中所有item的动画
        for (int i=0;i<mListView.getChildCount();i++){
            View view = mListView.getChildAt(i);
            view.clearAnimation();
        }
        //然后给当前item添加上动画
        if (isScrollDown) {
            convertView.startAnimation(animation);
        }
        convertView.setTag(holder);

        holder.mImageView.setImageDrawable(mDrawableList.get(position % mDrawableList.size()));
        holder.mTextView.setText(position + "");

        return convertView;
    }

    public class ViewHolder {
        public ImageView mImageView;
        public TextView mTextView;
    }
}
