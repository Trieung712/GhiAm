package com.test.ghiam;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.ContextWrapper;
import android.content.pm.PackageManager;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Toast;
import android.os.SystemClock;
import android.widget.Chronometer;


import java.io.File;
import java.io.IOException;

public class Record extends AppCompatActivity {
    private static int MICROPHONE_PERMISSION_CODE = 200;
    MediaRecorder mediaRecorder;
    private Chronometer chronometer;
    private long recordingStartTime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);
        if (isMicrophonePresent()){
            getMicrophonePermission();
        }
        chronometer = findViewById(R.id.chronometer);

    }

    public void btnRecordPress(View v){
        try {
            mediaRecorder = new MediaRecorder();
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            mediaRecorder.setOutputFile(getRecordFilePath());
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            mediaRecorder.prepare();
            mediaRecorder.start();

            // Lưu thời gian bắt đầu ghi âm
            recordingStartTime = SystemClock.elapsedRealtime();

            // Bắt đầu đếm thời gian
            chronometer.setBase(recordingStartTime);
            chronometer.start();
            Toast.makeText(this, "Started Record",Toast.LENGTH_LONG).show();
        }
        catch (Exception e){
            e.printStackTrace();
        }



    }
    public void btnStopPress(View v){
        if (mediaRecorder != null) {
            mediaRecorder.stop();
            mediaRecorder.release();
            mediaRecorder = null;
            // Dừng đếm thời gian và đặt lại về 0
            chronometer.stop();
            chronometer.setBase(SystemClock.elapsedRealtime());
            Toast.makeText(this, "Stopped Record", Toast.LENGTH_LONG).show();
        }
    }

    private boolean isMicrophonePresent(){
        if(this.getPackageManager().hasSystemFeature(PackageManager.FEATURE_MICROPHONE)){
            return true;
        }
        else {
            return false;
        }
    }
    private void getMicrophonePermission (){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.RECORD_AUDIO},MICROPHONE_PERMISSION_CODE);
        }
    }

    private String getRecordFilePath(){
        ContextWrapper contextWrapper = new ContextWrapper(getApplicationContext());
        File musicDirectory = contextWrapper.getExternalFilesDir(Environment.DIRECTORY_MUSIC);

        // Tạo tên file mới dựa trên số thứ tự
        String fileName = generateUniqueFileName(musicDirectory);

        File file = new File(musicDirectory, fileName + ".3gp");
        return file.getPath();
    }

    private String generateUniqueFileName(File directory) {
        String baseFileName = "testRecord";
        int count = 1;

        File newFile;
        do {
            String fileName = baseFileName + count;
            newFile = new File(directory, fileName + ".3gp");
            count++;
        } while (newFile.exists());

        return baseFileName + (count - 1);
    }
}