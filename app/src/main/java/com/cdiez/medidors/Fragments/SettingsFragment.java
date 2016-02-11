package com.cdiez.medidors.Fragments;

import android.content.Context;
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
import com.cdiez.medidors.Other.FragmentConstants;
import com.cdiez.medidors.R;
import com.cdiez.medidors.UI.EditLocation;
import com.cdiez.medidors.UI.EditProfile;
import com.cdiez.medidors.UI.EditRecibo;
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

    int[] mIcons = {R.drawable.ic_account_grey600_24dp, R.drawable.ic_receipt_grey600_24dp,
            R.drawable.ic_map_marker_grey600_24dp, R.drawable.ic_logout_grey600_24dp};
    String[] mTitles = {FragmentConstants.SETTINGS_PERFIL, FragmentConstants.SETTINGS_RECIBO,
            FragmentConstants.SETTINGS_LOCATION, FragmentConstants.SETTINGS_LOGOUT};

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
                    case FragmentConstants.SETTINGS_LOGOUT:
                        ParseUser.logOutInBackground();
                        goToActivity(view.getContext(), LoginActivity.class, true);
                        break;

                    case FragmentConstants.SETTINGS_LOCATION:
                        goToActivity(view.getContext(), EditLocation.class, false);
                        break;

                    case FragmentConstants.SETTINGS_RECIBO:
                            goToActivity(view.getContext(), EditRecibo.class, false);
                        break;

                    case FragmentConstants.SETTINGS_PERFIL:
                        goToActivity(view.getContext(), EditProfile.class, false);
                        break;
                }
            }
        });
        return view;
    }

    private void goToActivity(Context context, Class<?> destination, boolean flags) {
        Intent intent = new Intent(context, destination);
        if (flags) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        }
        startActivity(intent);
    }
}
