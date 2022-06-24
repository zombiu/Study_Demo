package com.example.study_customview.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.study_customview.R;

import java.util.Collections;
import java.util.List;

public class SampleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<String> items = DataProvider.INSTANCE.getSampleDatas(30);
    private LayoutInflater layoutInflater;

    public SampleAdapter(Context context) {
        layoutInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = layoutInflater.inflate(R.layout.item_test_scroll, parent, false);
        return new SampleHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof SampleHolder) {
            ((SampleHolder) holder).bind(items.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class SampleHolder extends RecyclerView.ViewHolder {
        private TextView tv_title;

        public SampleHolder(@NonNull View itemView) {
            super(itemView);
            tv_title = itemView.findViewById(R.id.tv_title);
        }

        public void bind(String s) {
            tv_title.setText(s);
        }
    }
}
