package com.mp3.player;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    TextView txtSemMusica;
    Button btnSobreNos;
    ArrayList<ModeloDeAudio> songsList = new ArrayList<>();

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recycler_view);
        txtSemMusica = findViewById(R.id.textView_SemMusica);
        btnSobreNos = findViewById(R.id.btnSobre);

        if(checarPermissao() == false){
            pedirPermissao();
            return;
        }

        String[] projecao = {
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.ALBUM,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.Media.DATA
        };

        String selecao = MediaStore.Audio.Media.IS_MUSIC;

        Cursor cursor = getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, projecao, selecao,null,null);

        while(cursor.moveToNext()){
            ModeloDeAudio dadosMusica = new ModeloDeAudio(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4));
            if(new File(dadosMusica.getDados()).exists()) {
                songsList.add(dadosMusica);
            }
        }

        if(songsList.size()==0){
            txtSemMusica.setVisibility(View.VISIBLE);
        }else{
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(new listaDeMusica(songsList,getApplicationContext()));
        }

    }

    boolean checarPermissao(){
        int result1 = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_MEDIA_AUDIO);
        int result2 = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE);
        if(result1 == PackageManager.PERMISSION_GRANTED || result2 == PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 123);
            return true;
        }else{
            return false;
        }
    }

    void pedirPermissao() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Permissão Necessária");
        builder.setMessage("Este aplicativo precisa de permissão para acessar suas músicas. Por favor, conceda a permissão.");

        builder.setPositiveButton("Conceder Permissão", (dialog, which) -> {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.READ_MEDIA_AUDIO, Manifest.permission.READ_EXTERNAL_STORAGE},
                    123);
        });

        builder.setNegativeButton("Cancelar", (dialog, which) -> {

            Toast.makeText(MainActivity.this, "Permissão negada. O aplicativo pode não funcionar corretamente.", Toast.LENGTH_SHORT).show();
        });

        builder.show();
    }




    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 123) {
            boolean allPermissionsGranted = true;
            for (int result : grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    allPermissionsGranted = false;
                    break;
                }
            }

            if (allPermissionsGranted) {
                //Chama RecyclerView - para quem qui-se adicionar!
            } else {

                Toast.makeText(this, "Permissões necessárias não foram concedidas", Toast.LENGTH_SHORT).show();
            }
        }
    }



    @Override
    protected void onResume() {
        super.onResume();
        if(recyclerView!=null){
            recyclerView.setAdapter(new listaDeMusica(songsList,getApplicationContext()));
        }
    }

    public void alertSobre(View v)
    {
        AlertDialog.Builder sobre = new AlertDialog.Builder(this);
        sobre.setTitle("Equipe");
        sobre.setMessage("Membros:\n - Rafael Neri\n - Davi Oliveira\n - Danilo Dias\n - Igor\n - Gabriel Maier\n\nProfessor: Victor Moak");
        sobre.setPositiveButton("OK", null);
        sobre.create().show();
    }

    public void telaLogin(View view){
        Intent login = new Intent(MainActivity.this, login.class);
        startActivity(login);
    }
}