package com.test.ghiam;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class ReadFile extends AppCompatActivity {
    private Dialog popupDialog;
    private SeekBar seekBar;
    private Button btnPause;
    private Button btnStop;
    private boolean isPlaying = false;
    private ListView listView;
    private ArrayList<File> recordedFiles;
    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_file);

        listView = findViewById(R.id.listView);

        // Lấy danh sách các file ghi âm
        recordedFiles = getRecordedFiles();

        // Hiển thị danh sách file trong ListView
        showRecordedFiles();

        // Xử lý sự kiện khi người dùng chọn một file
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               // playRecordedFile(position);
                showPopup(recordedFiles.get(position));

            }
        });
    }
    private void showPopup(final File selectedFile) {
        popupDialog = new Dialog(this);
        popupDialog.setContentView(R.layout.popup_layout);

        TextView tvFileName = popupDialog.findViewById(R.id.tvFileName);

        btnPause = popupDialog.findViewById(R.id.btnPause);
        btnStop = popupDialog.findViewById(R.id.btnStop);

        tvFileName.setText(selectedFile.getName());

        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(selectedFile.getPath());
            mediaPlayer.prepare();
            mediaPlayer.start();
            isPlaying = true;

        } catch (IOException e) {
            e.printStackTrace();
        }

        btnPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPlaying) {
                    mediaPlayer.pause();
                    isPlaying = false;
                    btnPause.setText("Continue");
                } else {
                    mediaPlayer.start();
                    isPlaying = true;
                    btnPause.setText("Pause");
                }
            }
        });

        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.stop();
                }
                popupDialog.dismiss();
            }
        });

        popupDialog.show();
    }
    private ArrayList<File> getRecordedFiles() {
        ArrayList<File> fileList = new ArrayList<>();
        File musicDirectory = new File(getExternalFilesDir(Environment.DIRECTORY_MUSIC).getPath());


        File[] files = musicDirectory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isFile() && file.getName().endsWith(".3gp")) {
                    fileList.add(file);
                }
            }
            Collections.sort(fileList, new Comparator<File>() {
                public int compare(File f1, File f2) {
                    return Long.compare(f2.lastModified(), f1.lastModified());
                }
            });
        }
        return fileList;
    }

    private void showRecordedFiles() {
        // Tạo một mảng string chứa tên file để hiển thị trong ListView
        ArrayList<String> fileNames = new ArrayList<>();
        for (File file : recordedFiles) {
            fileNames.add(file.getName());
        }

        // Sử dụng ArrayAdapter để hiển thị danh sách file trong ListView
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, fileNames);
        listView.setAdapter(arrayAdapter);
    }

    private void playRecordedFile(int position) {
        // Phát lại file được chọn bằng MediaPlayer
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }

        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(recordedFiles.get(position).getPath());
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Phương thức để mở RecordListActivity
    public static void start(Context context) {
        Intent intent = new Intent(context, ReadFile.class);
        context.startActivity(intent);
    }
}