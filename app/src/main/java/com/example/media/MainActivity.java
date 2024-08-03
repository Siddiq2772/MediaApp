package com.example.media;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.Manifest;
import android.os.Environment;
import android.os.Parcelable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

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
           public static MediaPlayer mediaPlayer;
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

        Dexter.withContext(this)
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        ArrayList<File> mp3 = fetchmp3(Environment.getExternalStorageDirectory());
                        String[] items = new String[mp3.size()];
                        for (int i = 0; i < mp3.size(); i++) {
                            items[i]= mp3.get(i).getName().replace(".mp3","");
                        }
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1,items);
                        listView.setAdapter(adapter);
                        int position = listView.getSelectedItemPosition();
                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    Uri uri = Uri.parse(mp3.get(position).toString());

                                    Intent intent = new Intent(MainActivity.this, AudioPlayer.class);
                                    String current = listView.getItemAtPosition(position).toString();
                                    intent.putExtra("uri",uri);
                                    intent.putExtra("current",current);
                                    startActivity(intent);

                            }
                        });
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
           public ArrayList<File> fetchmp3(File file){
                    ArrayList arrayList = new ArrayList();
                    File[] mp3 = file.listFiles();
                    if(mp3!=null){
                        for (File myfile:
                             mp3) {
                            if(!myfile.isHidden()&& myfile.isDirectory())
                                arrayList.addAll(fetchmp3(myfile));
                            else if(myfile.getName().endsWith(".mp3")&& !myfile.getName().startsWith("."))
                                arrayList.add(myfile);
                        }
                    }
                    return arrayList;
           }
}