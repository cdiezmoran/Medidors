package com.cdiez.medidors.Adapters;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;

import com.cdiez.medidors.Fragments.InfoFragment;
import com.cdiez.medidors.Fragments.SettingsFragment;
import com.cdiez.medidors.Fragments.UserFragment;

/**
 * Created by Carlos Diez
 * on 18/01/2016.
 */
public class MainFragmentAdapter extends FragmentPagerAdapter{

    public MainFragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                return new InfoFragment();
            case 1:
                return new UserFragment();
            case 2:
                return new SettingsFragment();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 3;
    }
}