package com.cdiez.medidors.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cdiez.medidors.Other.FragmentConstants;
import com.cdiez.medidors.R;

/**
 * Created by Carlos Diez
 * on 1/21/16.
 */
public class InfoAdapter extends RecyclerView.Adapter<InfoAdapter.InfoViewHolder> {

    private String[] mTitles;
    private int[] mIcons;

    public InfoAdapter(String[] titles, int[] icons) {
        mTitles = titles;
        mIcons = icons;
    }

    @Override
    public InfoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.info_item, parent, false);
        return new InfoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(InfoViewHolder holder, int position) {
        final String title = mTitles[position];
        holder.bindItem(title, mIcons[position]);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (title) {
                    case FragmentConstants.INFO_HISTORIAL:

                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mTitles.length;
    }

    public class InfoViewHolder extends RecyclerView.ViewHolder {

        ImageView mIcon;
        TextView mTitle;

        public InfoViewHolder(View itemView) {
            super(itemView);

            mIcon = (ImageView) itemView.findViewById(R.id.icon);
            mTitle = (TextView) itemView.findViewById(R.id.title);
        }

        public void bindItem(String title, int icon) {
            mTitle.setText(title);
            mIcon.setImageResource(icon);
        }
    }
}
