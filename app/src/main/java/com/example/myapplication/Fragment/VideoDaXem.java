package com.example.myapplication.Fragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myapplication.Adapter.AdapterLichSu;
import com.example.myapplication.Adapter.AdapterVideo;
import com.example.myapplication.Contect.VideoContect;
import com.example.myapplication.Define;
import com.example.myapplication.InterFace.IonClickVideo;
import com.example.myapplication.R;
import com.example.myapplication.SQLHelper;
import com.example.myapplication.databinding.ActivityVideoDaXemBinding;

import java.util.List;

public class VideoDaXem extends Fragment {
    ActivityVideoDaXemBinding binding;
    List<VideoContect> contects;
    SQLHelper sqlHelper;
    AdapterLichSu adapterLichSu;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    public static VideoDaXem newInstance() {
        Bundle args = new Bundle();
        VideoDaXem fragment = new VideoDaXem ();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.activity_video_da_xem,container,false);
        sqlHelper = new SQLHelper(getContext());
        contects = sqlHelper.getAllProductAdvanced();

        if(contects != null){
            adapterLichSu = new AdapterLichSu(contects,getContext());
            RecyclerView.LayoutManager layoutManager=new GridLayoutManager(getContext(),2,RecyclerView.VERTICAL,false);

            binding.videoDaXem.setAdapter(adapterLichSu);
            binding.videoDaXem.setLayoutManager(layoutManager);
            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
            editor = sharedPreferences.edit();
            adapterLichSu.setIonClickVideo(new IonClickVideo() {
                @Override
                public void onClickItem(VideoContect contect) {
                    editor.putString(Define.file_mp4,contect.getUrl());
                    editor.putString(Define.date_create,contect.getDate());
                    editor.putString(Define.title,contect.getName());
                    editor.commit();
                    Intent intent = new Intent(getContext(),FullScreen.class);
                    startActivity(intent);
                }
            });
        }
        return binding.getRoot();
    }
}
