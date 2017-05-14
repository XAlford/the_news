package com.example.ivonneortega.the_news_project.mainActivity.fragments;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Fragment Adapter to manage the swiping between taps in Main Activity
 */
public class FragmentAdapterMainActivity extends FragmentPagerAdapter {


    /**
     * FragmentAdapter constructor
     * @param fm
     */
    public FragmentAdapterMainActivity(FragmentManager fm) {
        super(fm);
    }

    /**
     * Gets the current item
     * @param position is the position we are in right now
     * @return the fragment we are going to display for that position
     */
    @Override
    public Fragment getItem(int position) {
        switch (position)
        {
            case 0:
                return FragmentAllStories.newInstance();
            case 1:
                return FragmentTopStories.newInstance();
            case 2:
                return FragmentSave.newInstance();
            default:
                return null;
        }
    }

    /**
     * @return the number of fragments
     */
    @Override
    public int getCount() {
        return 3;
    }

    /**
     * @param position is the position of the current fragment
     * @return the title for that fragment
     */
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position)
        {
            case 0:
                return "All Stories";
            case 1:
                return "Top Stories";
            case 2:
                return "Save";
            default:
                return null;
        }
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }
}
