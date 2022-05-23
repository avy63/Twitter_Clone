package com.codepath.apps.restclienttemplate.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.adapters.SampleFragmentPagerAdapter;
import com.github.florent37.materialviewpager.MaterialViewPager;
import com.google.android.material.tabs.TabLayout;

import static android.view.View.GONE;

public class ProfileDetailActivity extends AppCompatActivity {

    private MaterialViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_detail);
        mViewPager = (MaterialViewPager) findViewById(R.id.materialViewPager);



        ViewPager viewPager = mViewPager.getViewPager();
        viewPager.setAdapter(new SampleFragmentPagerAdapter(getSupportFragmentManager(),
               ProfileDetailActivity.this));
        // Give the TabLayout the ViewPager
        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs_detail);
        tabLayout.setupWithViewPager(viewPager);

        mViewPager.getPagerTitleStrip().setViewPager(mViewPager.getViewPager());
        mViewPager.getToolbar().setVisibility(GONE);
        mViewPager.getPagerTitleStrip().setTextColor(Color.WHITE);


        ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#1DA1F2")));
        actionBar.setTitle("Twitter");


    }
}
