package com.example.myapplication.Fragment;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import com.example.myapplication.Define;
import com.example.myapplication.R;
import com.example.myapplication.databinding.VideoBinding;
import java.text.SimpleDateFormat;
import java.util.concurrent.TimeUnit;

public class Video extends Fragment  {
    VideoBinding binding;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Handler handler;

    public static Video newInstance() {

        Bundle args = new Bundle();
        Video fragment = new Video();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.video, container, false);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        editor = sharedPreferences.edit();
        String link = sharedPreferences.getString(Define.file_mp4, "");
        String name = sharedPreferences.getString(Define.title, "");


        Uri uri = Uri.parse(link);
        binding.playVideo.setVideoURI(uri);
        binding.playVideo.requestFocus();
        binding.playVideo.start();

        handler = new Handler();
        ShowControl5s showControl5s = new ShowControl5s();
        handler.postDelayed(showControl5s, 3000);

        binding.video.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                binding.videoBtn.setVisibility(View.VISIBLE);
                return true;
            }
        });


        return binding.getRoot();
    }
@SuppressLint("SimpleDateFormat")
private String millisecondsToTimer(int milliseconds) {
    return new SimpleDateFormat("mm:ss").format(milliseconds);
}

    class ShowControl5s implements Runnable {
        public void run() {
            handler.postDelayed(this, 3000);
            binding.videoBtn.setVisibility(View.INVISIBLE);
        }
    }




    public String milisecond(int miliseconds) {
        Long minutes = TimeUnit.MILLISECONDS.toMinutes(miliseconds);
        Long seconds = TimeUnit.MILLISECONDS.toSeconds(miliseconds);
        return minutes + ":" + seconds;
    }

    public void doStart(View view) {
        int duration = binding.playVideo.getDuration();
        int currentPosition = binding.playVideo.getCurrentPosition();
        if (currentPosition == 0) {
            binding.seeBar.setMax(duration);
        }
    }
}
