package com.cdiez.medidors.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cdiez.medidors.R;

/**
 * Created by Carlos Diez
 * on 11/02/2016.
 */
public class ConsejosAdapter extends RecyclerView.Adapter<ConsejosAdapter.ViewHolder> {

    private String[] mTitles;
    private String[] mConsejos;

    public ConsejosAdapter(String[] titles, String[] consejos) {
        mTitles = titles;
        mConsejos = consejos;
    }

    @Override
    public ConsejosAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_consejo, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ConsejosAdapter.ViewHolder holder, int position) {
        holder.bindItem(mTitles[position], mConsejos[position]);
    }

    @Override
    public int getItemCount() {
        return mTitles.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView mTitle;
        TextView mConsejo;

        public ViewHolder(View itemView) {
            super(itemView);

            mTitle = (TextView) itemView.findViewById(R.id.title);
            mConsejo = (TextView) itemView.findViewById(R.id.consejo);
        }

        public void bindItem(String title, String consejo) {
            mTitle.setText(title);
            mConsejo.setText(consejo);
        }
    }
}
