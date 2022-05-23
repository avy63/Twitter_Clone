package com.codepath.apps.restclienttemplate.adapters;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.codepath.apps.restclienttemplate.fragments.FollowFragment;

// Adapter for tab layout/view pager
public class SampleFragmentPagerAdapter extends FragmentPagerAdapter {

    // number of tabs in this PageAdapter
    int PAGE_COUNT;
    // titles of each tab
    private String tabTitles[];
    private Context context;

    public SampleFragmentPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
        PAGE_COUNT = 2;
        tabTitles = new String[] { "Followers", "Following" };
    }

    // returns page count
    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    // returns correct fragment based on activity type and tab position
    @Override
    public Fragment getItem(int position) {
        return FollowFragment.newInstance(position + 1);

    }

    // retrieves the titles of each page
    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        return tabTitles[position];
    }

}
