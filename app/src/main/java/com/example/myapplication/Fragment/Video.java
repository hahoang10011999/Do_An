package com.example.myapplication.Fragment;

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

import java.util.concurrent.TimeUnit;

public class Video extends Fragment {
    VideoBinding binding;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Handler handler;

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
        binding.run.setVisibility(View.GONE);
        binding.imgFastForward.setVisibility(View.GONE);
        binding.imgRewind.setVisibility(View.GONE);
        binding.volum.setVisibility(View.GONE);
        handler = new Handler();
        ShowControl5s showControl5s = new ShowControl5s();
        handler.postDelayed(showControl5s, 5000);

        binding.video.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                binding.videoBtn.setVisibility(View.VISIBLE);
                return true;
            }
        });
        binding.pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.playVideo.pause();
                binding.pause.setVisibility(View.GONE);
                binding.run.setVisibility(View.VISIBLE);
            }
        });
        binding.run.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.playVideo.start();
                binding.run.setVisibility(View.GONE);
                binding.pause.setVisibility(View.VISIBLE);
            }
        });
        binding.rewind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int current = binding.playVideo.getCurrentPosition();
                int time = 5000;
                if(current - time >=0){
                    binding.playVideo.seekTo(current - time);
                }else{
                    binding.playVideo.seekTo(0);
                }
            }
        });
        binding.fastForWard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int current = binding.playVideo.getCurrentPosition();
                int duration = binding.playVideo.getDuration();
                int time = 5000;
                if(current + time< duration){
                    binding.playVideo.seekTo(current+time);
                }
            }
        });

        handler.postDelayed(runnable, 1000);

        return binding.getRoot();
    }

    class ShowControl5s implements Runnable {
        public void run() {
            handler.postDelayed(this, 3000);
            binding.videoBtn.setVisibility(View.INVISIBLE);
        }
    }


    public String milisecond(int miliseconds) {
        String finalTimerString = "";
        String secondsString = "";

        int hours = (int) (miliseconds / (1000 * 60 * 60));
        int minutes = (int) (miliseconds % (1000 * 60 * 60)) / (1000 * 60);
        int seconds = (int) ((miliseconds % (1000 * 60 * 60)) % (1000 * 60) / 1000);
        if (hours > 0) {
            finalTimerString = hours + ":";
        }

        if (seconds < 10) {
            secondsString = "0" + seconds;
        } else {
            secondsString = "" + seconds;
        }

        finalTimerString = finalTimerString + minutes + ":" + secondsString;

        return finalTimerString;
    }


    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (binding.playVideo != null) {
                int mCurrentPosition = binding.playVideo.getCurrentPosition();
                binding.seeBar.setProgress(mCurrentPosition);
                int timeCurent = binding.playVideo.getCurrentPosition();
                binding.timeRun.setText(milisecond(timeCurent));
                int timeEnd = binding.playVideo.getDuration();
                binding.allTime.setText(milisecond(timeEnd));
                updateSeekBar();
            }
        }
    };

    private void updateSeekBar() {
        handler.postDelayed(runnable, 1000);
        binding.seeBar.setMax(binding.playVideo.getDuration());
    }

}
