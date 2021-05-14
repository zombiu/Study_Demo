package com.hugo.study_recyclerview.search;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.hugo.study_recyclerview.databinding.SearchFeedItemBinding;

import java.util.ArrayList;
import java.util.List;

public class FeedAdapter extends ListAdapter<FeedItem, FeedAdapter.FeedItemViewHolder> {
    private List<FeedItem> items = new ArrayList<>();
    private MediatorLiveData<Boolean> mUpdateMediatorLiveData = new MediatorLiveData<>();

    public FeedAdapter() {
        super(new DiffUtil.ItemCallback<FeedItem>() {
            /**
             * areItemsTheSame用于判断两个item是否代表同一份信息，比如判断id是否一致
             * @param oldItem
             * @param newItem
             * @return
             */
            @Override
            public boolean areItemsTheSame(@NonNull FeedItem oldItem, @NonNull FeedItem newItem) {
                return oldItem.getId() == newItem.getId();
            }

            /**
             * areContentsTheSame用于判断要显示的内容是否完全一致，areItemsTheSame返回true会进入此判断
             * @param oldItem
             * @param newItem
             * @return
             */
            @Override
            public boolean areContentsTheSame(@NonNull FeedItem oldItem, @NonNull FeedItem newItem) {
                return TextUtils.equals(oldItem.getTitle(), newItem.getTitle());
            }
        });
    }

    @NonNull
    @Override
    public FeedItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return FeedItemViewHolder.create(parent);
    }

    @Override
    public void onBindViewHolder(@NonNull FeedItemViewHolder holder, int position) {
        holder.bind(position, getCurrentList().get(position));
    }

    /**
     * 单个item的刷新,跟 refreshItem区别不大
     * @param elementData
     * @param updatePayloadFunction
     * @param <I>
     * @param <R>
     */
    /*public <I, R extends BaseMutableData> void addUpdateMediator(LiveData<I> elementData,
                                                                 final UpdatePayloadFunction<I, R> updatePayloadFunction) {

    }
*/

    public void refreshItem(FeedItem item) {

    }

    @Override
    public void onViewRecycled(@NonNull FeedItemViewHolder holder) {
        super.onViewRecycled(holder);
    }

    /**
     * 当 Item 移出屏幕时，onViewRecycled() 不一定会回调，但 onViewDetachedFromWindow() 肯定会回调。相反，当 Item 移进屏幕内时，另一个方法则会回调。
     * @param holder
     */
    @Override
    public void onViewDetachedFromWindow(@NonNull FeedItemViewHolder holder) {
        super.onViewDetachedFromWindow(holder);

    }

    static class FeedItemViewHolder extends RecyclerView.ViewHolder {
        private SearchFeedItemBinding itemBinding;

        public FeedItemViewHolder(@NonNull SearchFeedItemBinding itemBinding) {
            super(itemBinding.getRoot());
            this.itemBinding = itemBinding;
        }

        public void bind(int position, FeedItem item) {
            itemBinding.titleTv.setText(item.getTitle());
        }

        public static FeedItemViewHolder create(ViewGroup parent) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            return new FeedItemViewHolder(SearchFeedItemBinding.inflate(layoutInflater, parent, false));
        }
    }
}
