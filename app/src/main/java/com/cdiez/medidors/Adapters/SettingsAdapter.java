package com.cdiez.medidors.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cdiez.medidors.R;

/**
 * Created by Carlos Diez
 * on 19/01/2016.
 */
public class SettingsAdapter extends BaseAdapter{

    private Context mContext;
    private String[] mTitles;
    private int[] mIcons;

    public SettingsAdapter(Context context, String[] titles, int[] icons) {
        mContext = context;
        mTitles = titles;
        mIcons = icons;
    }

    @Override
    public int getCount() {
        return mTitles.length;
    }

    @Override
    public Object getItem(int position) {
        return mTitles[position];
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.settings_item, null);

            holder = new ViewHolder();
            holder.iconImage = (ImageView) convertView.findViewById(R.id.icon);
            holder.titleTextView = (TextView) convertView.findViewById(R.id.title);

            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.titleTextView.setText(mTitles[position]);
        holder.iconImage.setImageResource(mIcons[position]);

        return convertView;
    }

    private static class ViewHolder {
        ImageView iconImage;
        TextView titleTextView;
    }
}