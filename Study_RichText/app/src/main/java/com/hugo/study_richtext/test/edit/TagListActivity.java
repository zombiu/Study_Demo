package com.hugo.study_richtext.test.edit;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hugo.study_richtext.R;
import com.hugo.study_richtext.databinding.TagItemBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * Resume:
 *
 * @author 汪波
 * @version 1.0
 * @see
 * @since 2017/4/3 汪波 first commit
 */
public class TagListActivity extends AppCompatActivity {

    RecyclerView recycler;
    private TagListActivity mTagListActivity;

    public static final String RESULT_TAG = "RESULT_TAG";
    private TagAdapter mTagAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.common_recycler);
        mTagListActivity = this;
        initView();
    }

    private void initView() {
        recycler = findViewById(R.id.recycler);

        List<Tag> tags = provideData();

        recycler.setLayoutManager(new LinearLayoutManager(mTagListActivity));
        mTagAdapter = new TagAdapter(tags);
        recycler.setAdapter(mTagAdapter);
    }

    private List<Tag> provideData() {
        List<Tag> result = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            Tag tag = new Tag("tag" + i, "id" + i);
            tag.setTagUrl("http://www.baidu.com/");
            result.add(tag);
        }
        return result;
    }

    private class TagAdapter extends RecyclerView.Adapter<TagViewHolder> {

        List<Tag> data;
        public TagAdapter(@Nullable List<Tag> data) {
           this.data = data;
        }

        @NonNull
        @Override
        public TagViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            TagItemBinding binding = TagItemBinding.inflate(getLayoutInflater());
            return new TagViewHolder(binding);
        }

        @Override
        public void onBindViewHolder(@NonNull TagViewHolder holder, int position) {
            holder.bind(position,data.get(position));
        }

        @Override
        public int getItemCount() {
            return data.size();
        }
    }

     class TagViewHolder extends RecyclerView.ViewHolder {
        private TagItemBinding binding;
        public TagViewHolder(@NonNull TagItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(final int position, final Tag tag) {
            binding.tagName.setText(tag.getTagLable());
            binding.atgUrl.setText(tag.getTagUrl());
            binding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setResult(tag);
                }
            });
        }
    }

    /*private class TagItem {
        private TextView tagName;
        private TextView atgUrl;

        @Override
        public int getLayoutResId() {
            return R.layout.tag_item;
        }

        @Override
        public void bindViews(View root) {
            initView(root);
        }

        @Override
        public void setViews(Tag tag) {

        }

        @Override
        public void handleData(Tag tag, int position) {
            tagName.setText(tag.getTagLable());
            atgUrl.setText(tag.getTagUrl());
        }

        private void initView(View view) {
            tagName = (TextView) view.findViewById(R.id.tag_name);
            atgUrl = (TextView) view.findViewById(R.id.atg_url);
        }
    }*/

    public static Intent getIntent(Activity activity) {
        return new Intent(activity, TagListActivity.class);
    }

    private void setResult(Tag tag) {
        Intent intent = getIntent();
        intent.putExtra(RESULT_TAG, tag);
        setResult(RESULT_OK, intent);
        finish();
    }
}


