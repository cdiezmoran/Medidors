package com.cdiez.medidors.Fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cdiez.medidors.Adapters.InfoAdapter;
import com.cdiez.medidors.Other.FragmentConstants;
import com.cdiez.medidors.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Carlos Diez
 * on 18/01/2016.
 */
public class InfoFragment extends Fragment {

    @Bind(R.id.recycler_view) RecyclerView mRecyclerView;

    int[] mIcons = {R.drawable.ic_history_black_48dp};
    String[] mTitles = {FragmentConstants.INFO_HISTORIAL};

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_info, container, false);
        ButterKnife.bind(this, view);

        mRecyclerView.setHasFixedSize(true);

        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
        mRecyclerView.setLayoutManager(layoutManager);

        InfoAdapter adapter = new InfoAdapter(mTitles, mIcons);
        mRecyclerView.setAdapter(adapter);

        return view;
    }
}
