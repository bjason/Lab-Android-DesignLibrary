package com.inthecheesefactory.lab.designlibrary.reference;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.inthecheesefactory.lab.designlibrary.CurrentFragment;
import com.inthecheesefactory.lab.designlibrary.R;
import com.inthecheesefactory.lab.designlibrary.SummaryFragment;

/**
 * Created by WU on 2017/4/12.
 */

public class SimpleFragmentPagerAdapter extends FragmentPagerAdapter {
    final int PAGE_COUNT = 2;
    private String tabTitles[];
    private Context context;

    public SimpleFragmentPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
        tabTitles = new String[]{context.getResources().getString(R.string.txt_current),
                context.getResources().getString(R.string.txt_summary)};
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0)
            return CurrentFragment.newInstance();
        else
            return SummaryFragment.newInstance();
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }
}
