package org.cacticouncil.menu;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;


public class TabPagerAdapter extends FragmentPagerAdapter
{
    public enum Tab
    {
        About,
        Noms,
        Sips
    };

    public static final String ARG_TAB_INDEX = "TabIndex";

    private final Context mContext;

    public TabPagerAdapter(Context context, FragmentManager fm)
    {
        super(fm, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position)
    {
        Fragment frag = null;
        if (position == 0)
            frag = new FragmentAbout();
        else
        {
            frag = new FragmentItems();
            Bundle bundle = new Bundle();
            if (position == 1)
                bundle.putSerializable(ARG_TAB_INDEX, Tab.Noms);
            else if (position == 2)
                bundle.putSerializable(ARG_TAB_INDEX, Tab.Sips);
            frag.setArguments(bundle);
        }
        return frag;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position)
    {
        return Tab.values()[position].name();
    }

    @Override
    public int getCount()
    {
        return 3; // 3 total pages (tabs)
    }
}