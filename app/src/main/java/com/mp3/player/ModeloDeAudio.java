package com.mp3.player;

import java.io.Serializable;

public class ModeloDeAudio implements Serializable {
    String artista, album, titulo, duracao, dados;

    public ModeloDeAudio(String artista, String album, String titulo, String duracao, String dados) {
        this.artista = artista;
        this.album = album;
        this.titulo = titulo;
        this.duracao = duracao;
        this.dados = dados;
    }

    public String getArtista() {
        return artista;
    }

    public void setArtista(String artista) {
        this.artista = artista;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getDados() {
        return dados;
    }

    public void setDados(String dados) {
        this.dados = dados;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDuracao() {
        return duracao;
    }

    public void setDuracao(String duracao) {
        this.duracao = duracao;
    }
}