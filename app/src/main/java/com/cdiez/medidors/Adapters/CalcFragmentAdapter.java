package com.cdiez.medidors.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.cdiez.medidors.Fragments.ConsumoFragment;
import com.cdiez.medidors.Fragments.LecturaFragment;

/**
 * Created by Carlos Diez
 * on 13/02/2016.
 */
public class CalcFragmentAdapter extends FragmentPagerAdapter {

    public CalcFragmentAdapter(FragmentManager fm){
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                return new LecturaFragment();
            case 1:
                return new ConsumoFragment();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }
}
