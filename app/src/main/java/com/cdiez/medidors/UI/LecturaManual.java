package com.cdiez.medidors.UI;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.cdiez.medidors.Adapters.CalcFragmentAdapter;
import com.cdiez.medidors.R;

import butterknife.Bind;
import butterknife.ButterKnife;

public class LecturaManual extends AppCompatActivity {

    @Bind(R.id.toolbar) Toolbar mToolbar;
    @Bind(R.id.container) ViewPager mViewPager;
    @Bind(R.id.tab_layout) TabLayout mTabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lectura_manual);
        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);

        assert getSupportActionBar()!= null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        CalcFragmentAdapter adapter = new CalcFragmentAdapter(getSupportFragmentManager());

        mViewPager.setAdapter(adapter);

        mTabLayout.addTab(mTabLayout.newTab().setText("Lectura"));
        mTabLayout.addTab(mTabLayout.newTab().setText("Consumo"));
        mTabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));
        mTabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
}