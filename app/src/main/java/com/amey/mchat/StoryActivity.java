package com.amey.mchat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.amey.mchat.Adapters.StoryPageTransition;
import com.amey.mchat.fragments.HomeFragment;
import com.amey.mchat.fragments.StoryFragment;
import com.bumptech.glide.Glide;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class StoryActivity extends FragmentActivity {

    private ViewPager2 viewPager;
    private FragmentStateAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story);


    // Instantiate a ViewPager2 and a PagerAdapter.
    viewPager = findViewById(R.id.pager);
        viewPager.setPageTransformer(new StoryPageTransition());
    pagerAdapter = new StorySlidePagerAdapter(this);
        viewPager.setAdapter(pagerAdapter);
}
private class StorySlidePagerAdapter extends FragmentStateAdapter {
    public StorySlidePagerAdapter(FragmentActivity fa){
        super(fa);
    }

    @Override
    public Fragment createFragment(int position) {
        return new StoryFragment(HomeFragment.list.get(position));
    }

    @Override
    public int getItemCount() {
        return HomeFragment.list.size();
    }
}

}