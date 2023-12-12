package com.mp3.player;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.MediaMetadataRetriever;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.recyclerview.widget.RecyclerView;

public class listaDeMusica extends RecyclerView.Adapter<listaDeMusica.ViewHolder>{

    ArrayList<ModeloDeAudio> listaMusicas;
    Context context;

    public listaDeMusica(ArrayList<ModeloDeAudio> listaDeMusicas, Context context) {
        this.listaMusicas = listaDeMusicas;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recycler_item,parent,false);
        return new listaDeMusica.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(listaDeMusica.ViewHolder holder, int position) {
        ModeloDeAudio songData = listaMusicas.get(holder.getAdapterPosition());

        holder.titleTextView.setText(songData.getTitulo());

        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        mmr.setDataSource(listaMusicas.get(position).getDados());

        byte[] arte = mmr.getEmbeddedPicture();

        if (arte != null && arte.length > 0) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(arte, 0, arte.length);
            holder.iconImageView.setImageBitmap(bitmap);
        } else {
            holder.iconImageView.setImageResource(R.drawable._844724);
        }

        if (PlayerDeMusica.index == holder.getAdapterPosition()){
            holder.titleTextView.setTextColor(Color.parseColor("#FF0000"));
        }else{
            holder.titleTextView.setTextColor(Color.parseColor("#000000"));
        }


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlayerDeMusica.getInstance().reset();
                PlayerDeMusica.index = position;
                Intent intent = new Intent(context, PlayerDeMusicaActivity.class);
                intent.putExtra("LIST", listaMusicas);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return listaMusicas.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView titleTextView;
        ImageView iconImageView;
        public ViewHolder(View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.tituloMus);
            iconImageView = itemView.findViewById(R.id.icoMus);
        }
    }
}
