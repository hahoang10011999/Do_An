package com.example.myapplication.Fragment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.example.myapplication.Adapter.AdapterPlayVideo;
import com.example.myapplication.Contect.VideoContect;
import com.example.myapplication.Define;
import com.example.myapplication.InterFace.IonClickVideo;
import com.example.myapplication.PutVideoList;
import com.example.myapplication.R;
import com.example.myapplication.SQL.SQLHelper;
import com.example.myapplication.SQL.SQLHelperList;
import com.example.myapplication.databinding.ActivityFullScreenBinding;

import java.util.List;

public class FullScreen extends AppCompatActivity {
    ActivityFullScreenBinding binding;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Handler handler;
    int timeRun,dem;
    AudioManager audioManager;
    int maxVolume, stepVolume, currentVolume, x, y;
    AdapterPlayVideo adapterPlayVideo;
    SQLHelper sqlHelper;
    List<VideoContect> contects;
    List<VideoContect> videoContects;
    List<VideoContect> list;
    SQLHelperList sqlHelperList;
    PutVideoList putVideoList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_full_screen);



        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        editor = sharedPreferences.edit();
        String link = sharedPreferences.getString(Define.file_mp4, "");

        Uri uri = Uri.parse(link);
        binding.playVideo.setVideoURI(uri);
        binding.playVideo.requestFocus();
        binding.playVideo.start();

        sqlHelper = new SQLHelper(getBaseContext());
        sqlHelperList = new SQLHelperList(getBaseContext());
        putVideoList = new PutVideoList(getBaseContext());
        list = sqlHelperList.getAllProductAdvanced();
//list video
        contects = sqlHelperList.getAllProductAdvanced();
        adapterPlayVideo = new AdapterPlayVideo(contects);
        RecyclerView.LayoutManager layoutManager=new GridLayoutManager(getBaseContext(),1,RecyclerView.VERTICAL,false);
        binding.rvVideo.setAdapter(adapterPlayVideo);
        binding.rvVideo.setLayoutManager(layoutManager);

        adapterPlayVideo.setIonClickVideo(new IonClickVideo() {
            @Override
            public void onClickItem(VideoContect contect) {
                if (list != null) {
                    sqlHelperList.delAllProduct();
                }


                putVideoList.onPutVideo(contects, contect, sqlHelperList);
                putVideoList.onPutVideoHistory(videoContects, contect, sqlHelper, dem);

                editor.putString(Define.file_mp4, contect.getUrl());
                editor.putString(Define.date_create, contect.getDate());
                editor.putString(Define.title, contect.getName());
                editor.commit();

                Intent intent = new Intent(getBaseContext(), FullScreen.class);
                startActivity(intent);
            }
        });

        binding.run.setVisibility(View.GONE);
        binding.imgFastForward.setVisibility(View.GONE);
        binding.imgRewind.setVisibility(View.GONE);
        binding.volum.setVisibility(View.GONE);
        binding.tvChange.setVisibility(View.GONE);
        binding.exitFullScreen.setVisibility(View.INVISIBLE);

//show control 5s
        handler = new Handler();
        ShowControl5s showControl = new ShowControl5s();
        handler.postDelayed(showControl, 5000);
        handler.postDelayed(runnable, 1000);
//return to main screen
        binding.backScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
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
//run video
        binding.run.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.playVideo.start();
                binding.run.setVisibility(View.GONE);
                binding.pause.setVisibility(View.VISIBLE);
            }
        });
//rewin 5s
        binding.rewind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int current = binding.playVideo.getCurrentPosition();
                int time = 5000;
                if (current - time >= 0) {
                    binding.playVideo.seekTo(current - time);
                } else {
                    binding.playVideo.seekTo(0);
                }
            }
        });
//fast for ward 5s
        binding.fastForWard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int current = binding.playVideo.getCurrentPosition();
                int duration = binding.playVideo.getDuration();
                int time = 5000;
                if (current + time < duration) {
                    binding.playVideo.seekTo(current + time);
                }
            }
        });
//full screen
        binding.FullScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.exitFullScreen.setVisibility(View.VISIBLE);
                binding.FullScreen.setVisibility(View.INVISIBLE);
                binding.titleVideo.setVisibility(View.INVISIBLE);
                binding.backScreen.setVisibility(View.GONE);
                binding.listPlayVideo.setVisibility(View.GONE);

                timeRun = binding.playVideo.getCurrentPosition();

                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT);
                RelativeLayout.LayoutParams params1 = (RelativeLayout.LayoutParams) binding.video.getLayoutParams();
                params1.width = params.MATCH_PARENT;
                params1.height = params1.MATCH_PARENT;
                binding.video.setLayoutParams(params);
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

                binding.playVideo.seekTo(timeRun);
                timeRun = binding.playVideo.getCurrentPosition();
            }
        });
//exit full screen
        binding.exitFullScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                binding.exitFullScreen.setVisibility(View.INVISIBLE);
                binding.FullScreen.setVisibility(View.VISIBLE);
                binding.titleVideo.setVisibility(View.VISIBLE);
                binding.listPlayVideo.setVisibility(View.VISIBLE);
                binding.backScreen.setVisibility(View.VISIBLE);


                timeRun = binding.playVideo.getCurrentPosition();
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                RelativeLayout.LayoutParams params1 = (RelativeLayout.LayoutParams) binding.video.getLayoutParams();
                params1.width = params.MATCH_PARENT;
                params1.height = 600;
                binding.video.setLayoutParams(params1);
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                binding.playVideo.seekTo(timeRun);
            }
        });


//control audio and position
        audioManager = (AudioManager) getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
        maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        stepVolume = 100 / maxVolume;
        currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        currentVolume = currentVolume * stepVolume;
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, currentVolume, 0);
        binding.tvChange.setText(String.valueOf(currentVolume));

        binding.playVideo.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getActionMasked()) {
                    case MotionEvent.ACTION_DOWN:
                        x = (int) event.getX();
                        y = (int) event.getY();
                        break;
                    case MotionEvent.ACTION_MOVE:

                        if (Math.abs(event.getX() - x) > 50) {
                            binding.tvChange.setVisibility(View.VISIBLE);
                            if (event.getX() - x > 0 && binding.playVideo.getCurrentPosition() < binding.playVideo.getDuration()) {
                                int time = binding.playVideo.getCurrentPosition() + (int) Math.abs(event.getX() - x);
                                binding.imgFastForward.setVisibility(View.VISIBLE);
                                binding.playVideo.seekTo(time);
                                binding.tvChange.setText(milisecond(time));
                            } else if (event.getX() - x < 0 && binding.playVideo.getCurrentPosition() > 0) {
                                int time = binding.playVideo.getCurrentPosition() - (int) Math.abs(event.getX() - x);
                                binding.imgRewind.setVisibility(View.VISIBLE);
                                binding.playVideo.seekTo(time);
                                binding.tvChange.setText(milisecond(time));
                            }

                        }
                        if (Math.abs(event.getY() - y) > 50) {
                            binding.tvChange.setVisibility(View.VISIBLE);
                            if (event.getY() - y < 0 && currentVolume < 100) {
                                currentVolume++;
                                binding.volum.setVisibility(View.VISIBLE);
                                binding.tvChange.setText(String.valueOf(currentVolume));
                                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, currentVolume / stepVolume, 0);
                            } else if (event.getY() - y > 0 && currentVolume > 0) {
                                currentVolume--;
                                binding.volum.setVisibility(View.VISIBLE);
                                binding.tvChange.setText(String.valueOf(currentVolume));
                                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, currentVolume / stepVolume, 0);
                            }
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        binding.tvChange.setVisibility(View.INVISIBLE);
                        binding.imgRewind.setVisibility(View.GONE);
                        binding.imgFastForward.setVisibility(View.GONE);
                        binding.volum.setVisibility(View.GONE);
                        break;
                }
                binding.videoBtn.setVisibility(View.VISIBLE);
                return true;
            }
        });

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
