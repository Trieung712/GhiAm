package com.test.ghiam;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AppCompatButton buttonA = findViewById(R.id.btnrecord);
        AppCompatButton buttonB = findViewById(R.id.btnReadFile);

        buttonA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Chuyển đến Activity A
                Intent intent = new Intent(MainActivity.this, Record.class);
                startActivity(intent);
            }
        });

        buttonB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Chuyển đến Activity B
                Intent intent = new Intent(MainActivity.this, ReadFile.class);
                startActivity(intent);
            }
        });
    }
}