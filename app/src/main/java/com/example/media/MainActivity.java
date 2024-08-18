package com.example.media;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.Manifest;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
            private ListView listView;
           MediaPlayer mediaPlayer;
          private static ArrayList<File> mp3;

           private Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
            listView = findViewById(R.id.listView);
        mp3 = fetchmp3(Environment.getExternalStorageDirectory());
        Dexter.withContext(this)
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {


                       MP3(new View(MainActivity.this));
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {


                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                })
                .check();
    }
           public static ArrayList fetchmp3(File file){
                    ArrayList arrayList = new ArrayList();
                    File[] mp3 = file.listFiles();
                    if(mp3!=null){
                        for (File myfile:
                             mp3) {
                            if(!myfile.isHidden()&& myfile.isDirectory())
                                arrayList.addAll(fetchmp3(myfile));
                            else if(myfile.getName().endsWith(".mp3")&& !myfile.getName().startsWith("."))
                                arrayList.add(myfile);
                            else if(myfile.getName().endsWith(".mp4")&& !myfile.getName().startsWith("."))
                                arrayList.add(myfile);


                        }
                        }
                    return arrayList;
           }


    public void MP3(View v){
        button= findViewById(R.id.button);
        ArrayList<File> m = new ArrayList<>() ;
        for (int i = 0; i < mp3.size(); i++)
            if(mp3.get(i).getName().endsWith(".mp3"))
                m.add(mp3.get(i));

        if (button.getText().equals("MP3")){
            button.setText("MP4");
            String[] items = new String[m.size()];
            for (int i = 0; i < m.size(); i++)
                items[i]= m.get(i).getName();


            ArrayAdapter<String> adapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1,items);
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Uri uri = Uri.parse(m.get(position).toString());

                    Intent intent = new Intent(MainActivity.this, AudioPlayer.class);
                    String current = listView.getItemAtPosition(position).toString();
                    intent.putExtra("uri",uri);
                    intent.putExtra("current",current);
                    startActivity(intent);

                }
            });
        }else {
            button.setText("MP3");
            MP4(v);
        }

    }

    public void MP4(View v){
        ArrayList<File> m = new ArrayList<>() ;
        for (int i = 0; i < mp3.size(); i++)
            if(mp3.get(i).getName().endsWith(".mp4"))
                m.add(mp3.get(i));
        String[] items = new String[m.size()];

        for (int i = 0; i < m.size(); i++)
            items[i]= m.get(i).getName();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1,items);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Uri uri = Uri.parse(m.get(position).toString());
                Intent intent = new Intent(MainActivity.this, video.class);
                String current = listView.getItemAtPosition(position).toString();
                intent.putExtra("uri",uri);
                intent.putExtra("current",current);
                startActivity(intent);

            }
        });
    }
}