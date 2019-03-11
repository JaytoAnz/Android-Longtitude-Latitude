package com.codenusa.app_poslink.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.codenusa.app_poslink.R;
import com.codenusa.app_poslink.model.UploadLaporan;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class LaporanAdapter extends RecyclerView.Adapter<LaporanAdapter.LaporanViewHolder> {

    private Context mCtx;
    private List<UploadLaporan> dataList;

    public LaporanAdapter(List<UploadLaporan> dataList, Context mCtx) {
        this.mCtx = mCtx;
        this.dataList = dataList;
    }

    public class LaporanViewHolder extends RecyclerView.ViewHolder {

        private Context mCtx;
        private List<UploadLaporan> dataList;
        private TextView textJudul, textDesk, textTgl, textKonfir;
        private ImageView foto_lap;

        public LaporanViewHolder(View itemView, Context mCtx, List<UploadLaporan> dataList) {
            super(itemView);
            this.mCtx = mCtx;
            this.dataList = dataList;

            textJudul = (TextView) itemView.findViewById(R.id.textJudul);
            textDesk = (TextView) itemView.findViewById(R.id.textDeskripsi);
            textTgl = (TextView) itemView.findViewById(R.id.textTgl);
            textKonfir = (TextView) itemView.findViewById(R.id.textKonfirmasi);

            foto_lap = (ImageView) itemView.findViewById(R.id.foto_lap);

        }
    }

    @Override
    public LaporanViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.list_laporan, parent, false);
        return new LaporanViewHolder(view, mCtx, dataList);
    }

    @Override
    public void onBindViewHolder(LaporanViewHolder holder, int position) {
        holder.textJudul.setText(dataList.get(position).getJudul());
        holder.textDesk.setText(dataList.get(position).getDeskripsi());
        holder.textKonfir.setText(dataList.get(position).getStatus());

        //loading the image
        Glide.with(mCtx)
                .load("http://poslink.pmiicyberkom.org/_api/" + dataList.get(position).getFoto())                     // Set image url
                .diskCacheStrategy(DiskCacheStrategy.ALL)   // Cache for image
                .into(holder.foto_lap);

        String createDate = dataList.get(position).getTgl();
        SimpleDateFormat df1 = new SimpleDateFormat("yyy-MM-dd");
        SimpleDateFormat df2 = new SimpleDateFormat("EEE, d MMM yyyy");
        Date date = null;

        try {
            date = df1.parse(createDate);
            holder.textTgl.setText("" + df2.format(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }
}
