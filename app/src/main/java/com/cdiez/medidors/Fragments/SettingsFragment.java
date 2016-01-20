package com.cdiez.medidors.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.cdiez.medidors.Adapters.SettingsAdapter;
import com.cdiez.medidors.R;
import com.cdiez.medidors.UI.LoginActivity;
import com.parse.ParseUser;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Carlos Diez
 * on 18/01/2016.
 */
public class SettingsFragment extends Fragment {

    @Bind(R.id.list_view) ListView mListView;

    int[] mIcons = {R.drawable.ic_logout_grey600_24dp};
    String[] mTitles = {"Logout"};

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        ButterKnife.bind(this, view);

        SettingsAdapter adapter = new SettingsAdapter(view.getContext(), mTitles, mIcons);

        mListView.setAdapter(adapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (mTitles[position]) {
                    case "Logout":
                        ParseUser.logOutInBackground();
                        Intent intent = new Intent(view.getContext(), LoginActivity.class);
                        view.getContext().startActivity(intent);
                        break;
                }
            }
        });
        return view;
    }
}
