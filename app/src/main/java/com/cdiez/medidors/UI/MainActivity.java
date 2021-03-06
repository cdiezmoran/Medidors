package com.cdiez.medidors.UI;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.cdiez.medidors.Adapters.MainFragmentAdapter;
import com.cdiez.medidors.R;
import com.parse.ParseUser;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @Bind(R.id.container) ViewPager mViewPager;
    @Bind(R.id.tab_layout) TabLayout mTabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        if (ParseUser.getCurrentUser() == null) {
            Intent intent = new Intent(this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }

        MainFragmentAdapter adapter = new MainFragmentAdapter(getSupportFragmentManager());

        mViewPager.setAdapter(adapter);

        final Drawable[] drawables = {ContextCompat.getDrawable(this, R.drawable.ic_information_outline_white_24dp),
                ContextCompat.getDrawable(this, R.drawable.ic_account_circle_white_24dp),
                ContextCompat.getDrawable(this, R.drawable.ic_settings_white_24dp)};

        for (Drawable drawable : drawables) {
            drawable.setColorFilter(ContextCompat.getColor(this, R.color.colorAccent), PorterDuff.Mode.SRC_ATOP);
            drawable.setAlpha(100);
        }

        mTabLayout.addTab(mTabLayout.newTab().setIcon(drawables[0]));
        mTabLayout.addTab(mTabLayout.newTab().setIcon(drawables[1]));
        mTabLayout.addTab(mTabLayout.newTab().setIcon(drawables[2]));
        mTabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        mTabLayout.setSelectedTabIndicatorColor(ContextCompat.getColor(this, R.color.colorAccent));

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));
        mTabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                drawables[tab.getPosition()].setAlpha(255);
                tab.setIcon(drawables[tab.getPosition()]);
                mViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                drawables[tab.getPosition()].setAlpha(100);
                tab.setIcon(drawables[tab.getPosition()]);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        mViewPager.setCurrentItem(1, false);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }
}