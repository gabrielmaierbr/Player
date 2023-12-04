package com.mp3.player;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    TextView textView_SemMusica;
    ArrayList<ModeloDeAudio> listaDeMusica = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recycler_view);
        textView_SemMusica = findViewById(R.id.textView_SemMusica);

        if (checarPermissao() == false)
        {
            pedirPermissao();
            return;
        }

        String[] proejecao = {
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.DURATION
        };

        String selecao = MediaStore.Audio.Media.IS_MUSIC + " != 0";

        Cursor cursor = getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, proejecao, selecao, null, null);
        while(cursor.moveToNext())
        {
            ModeloDeAudio dadosMusica = new ModeloDeAudio(cursor.getString(0), cursor.getString(1), cursor.getString(2));
            if (new File(dadosMusica.getPath()).exists())
            {
                listaDeMusica.add(dadosMusica);
            }
            if (listaDeMusica.size() == 0)
            {
                textView_SemMusica.setVisibility(View.VISIBLE);
            }
            else
            {
                recyclerView.setLayoutManager(new LinearLayoutManager(this));
                recyclerView.setAdapter();
            }
        }
    }

    boolean checarPermissao() {
        int result = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED)
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    boolean pedirPermissao() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE))
        {
            Toast.makeText(MainActivity.this, "Permissão para ler os arquivos é necessária", Toast.LENGTH_SHORT);
        }
        else
        {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 123);
        }
        return false;
    }
}