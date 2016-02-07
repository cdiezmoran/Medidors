package com.cdiez.medidors.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cdiez.medidors.Data.Lectura;
import com.cdiez.medidors.Other.ParseConstants;
import com.cdiez.medidors.R;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Carlos Diez
 * on 02/02/2016.
 */
public class HistorialAdapter extends RecyclerView.Adapter<HistorialAdapter.ViewHolder> {

    private Context mContext;
    private List<Lectura> mLecturas;

    public HistorialAdapter(Context context, List<Lectura> lecturas) {
        mContext = context;
        mLecturas = lecturas;
    }

    @Override
    public HistorialAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_historial, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(HistorialAdapter.ViewHolder holder, int position) {
        holder.bindItem(mLecturas.get(position));
    }

    @Override
    public int getItemCount() {
        return mLecturas.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView mFoto;
        TextView mLectura;
        TextView mLecturaAnterior;
        TextView mFecha;
        TextView mPago;

        public ViewHolder(View itemView) {
            super(itemView);

            mFoto = (ImageView) itemView.findViewById(R.id.foto);
            mLectura = (TextView) itemView.findViewById(R.id.lectura);
            mLecturaAnterior = (TextView) itemView.findViewById(R.id.lectura_anterior);
            mFecha = (TextView) itemView.findViewById(R.id.fecha);
            mPago = (TextView) itemView.findViewById(R.id.pago);

        }

        public void bindItem(Lectura lectura) {
            String lecturaText = "Lectura: " + lectura.getLectura();
            String lecturaAnteriorText = "Anterior: " + lectura.getLecturaAnterior();
            String pagoText = "Costo Aprox.: " + lectura.getCosto();

            SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
            Date fechaLectura = lectura.getDate(ParseConstants.KEY_FECHA_LECTURA);

            mLectura.setText(lecturaText);
            mLecturaAnterior.setText(lecturaAnteriorText);
            mPago.setText(pagoText);
            mFecha.setText(dateFormatter.format(fechaLectura));

            Picasso.with(mContext).load(lectura.getImageString()).into(mFoto);
        }
    }
}
