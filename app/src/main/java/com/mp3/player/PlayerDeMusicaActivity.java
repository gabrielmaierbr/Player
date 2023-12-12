package com.mp3.player;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class PlayerDeMusicaActivity extends AppCompatActivity {

    TextView txtTitulo, txtArtista, txtAlbum, txtTempoAtual, txtTempoTotal;
    SeekBar barraProg;
    ImageView pausePlay, btnProximo, btnAnterior, icoMusica;
    ArrayList<ModeloDeAudio> listaMusicas;
    ModeloDeAudio musicaAtual;
    MediaPlayer mediaPlayer = PlayerDeMusica.getInstance();

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_player);

        txtTitulo = findViewById(R.id.tituloMusica);
        txtArtista = findViewById(R.id.nomeArtista);
        txtAlbum = findViewById(R.id.nomeAlbum);
        txtTempoAtual = findViewById(R.id.tempoAtual);
        txtTempoTotal = findViewById(R.id.tempoTotal);
        barraProg = findViewById(R.id.barra);
        pausePlay = findViewById(R.id.pause_play);
        btnProximo = findViewById(R.id.proximo);
        btnAnterior = findViewById(R.id.anterior);
        icoMusica = findViewById(R.id.imgMusica);

        txtTitulo.setSelected(true);

        listaMusicas = (ArrayList<ModeloDeAudio>) getIntent().getSerializableExtra("LIST");

        recursosMusica();

        PlayerDeMusicaActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {

                if(mediaPlayer!=null){
                    barraProg.setProgress(mediaPlayer.getCurrentPosition());
                    txtTempoAtual.setText(converterTempoMusica(mediaPlayer.getCurrentPosition()+""));

                    if(mediaPlayer.isPlaying()){
                        pausePlay.setImageResource(R.drawable.baseline_pause_circle_outline_24);
                    }else{
                        pausePlay.setImageResource(R.drawable.baseline_play_circle_outline_24);
                    }
                }
                new Handler().postDelayed(this,100);
            }
        });

        barraProg.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(mediaPlayer!=null && fromUser){
                    mediaPlayer.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


    }

    void recursosMusica(){
        musicaAtual = listaMusicas.get(PlayerDeMusica.index);

        txtTitulo.setText(musicaAtual.getTitulo());
        txtArtista.setText(musicaAtual.getArtista());
        txtAlbum.setText(musicaAtual.getAlbum());

        txtTempoTotal.setText(converterTempoMusica(musicaAtual.getDuracao()));

        pausePlay.setOnClickListener(v-> pausePlay());
        btnProximo.setOnClickListener(v-> playProximaMusica());
        btnAnterior.setOnClickListener(v-> playMusicaAnterior());

        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        mmr.setDataSource(musicaAtual.getDados());
        byte[] arte = mmr.getEmbeddedPicture();

        if (arte != null && arte.length > 0) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(arte, 0, arte.length);
            icoMusica.setImageBitmap(bitmap);
        } else {
            icoMusica.setImageResource(R.drawable._844724);
        }
        playMusica();
    }

    private void playMusica(){

        mediaPlayer.reset();
        try {
            mediaPlayer.setDataSource(musicaAtual.getDados());
            mediaPlayer.prepare();
            mediaPlayer.start();
            barraProg.setProgress(0);
            barraProg.setMax(mediaPlayer.getDuration());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void playProximaMusica(){

        if(PlayerDeMusica.index == listaMusicas.size() - 1)
            return;
        PlayerDeMusica.index += 1;
        mediaPlayer.reset();
        recursosMusica();
    }

    private void playMusicaAnterior(){
        if(PlayerDeMusica.index == 0)
            return;
        PlayerDeMusica.index -= 1;
        mediaPlayer.reset();
        recursosMusica();
    }

    private void pausePlay(){
        if(mediaPlayer.isPlaying())
            mediaPlayer.pause();
        else
            mediaPlayer.start();
    }


    public static String converterTempoMusica(String tempo){
        Long millis = Long.parseLong(tempo);
        return String.format("%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(millis) % TimeUnit.HOURS.toMinutes(1),
                TimeUnit.MILLISECONDS.toSeconds(millis) % TimeUnit.MINUTES.toSeconds(1));
    }
}