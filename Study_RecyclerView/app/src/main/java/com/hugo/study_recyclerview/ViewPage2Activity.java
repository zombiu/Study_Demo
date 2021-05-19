package com.hugo.study_recyclerview;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import android.os.Bundle;
import android.view.View;

import com.hugo.study_recyclerview.databinding.ActivityViewPage2Binding;

public class ViewPage2Activity extends AppCompatActivity {
    private ActivityViewPage2Binding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityViewPage2Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        ScreenSlidePagerAdapter pagerAdapter = new ScreenSlidePagerAdapter(this);
        binding.pager.setAdapter(pagerAdapter);

        binding.callTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.pager.setCurrentItem(1);
                ScreenSlidePageFragment myFragment = (ScreenSlidePageFragment) getSupportFragmentManager().findFragmentByTag("f" + pagerAdapter.getItemId(1));
                myFragment.refresh();
            }
        });
    }

    private class ScreenSlidePagerAdapter extends FragmentStateAdapter {
        public ScreenSlidePagerAdapter(FragmentActivity fa) {
            super(fa);
        }

        @Override
        public Fragment createFragment(int position) {
            return new ScreenSlidePageFragment();
        }

        @Override
        public int getItemCount() {
            return 2;
        }
    }
}