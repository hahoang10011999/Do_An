package com.example.myapplication.Fragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myapplication.Adapter.AdapterLichSu;
import com.example.myapplication.Adapter.AdapterVideo;
import com.example.myapplication.Contect.VideoContect;
import com.example.myapplication.R;
import com.example.myapplication.SQLHelper;
import com.example.myapplication.databinding.ActivityVideoDaXemBinding;

import java.util.List;

public class VideoDaXem extends Fragment {
    ActivityVideoDaXemBinding binding;
    List<VideoContect> contects;
    SQLHelper sqlHelper;
    AdapterLichSu adapterLichSu;

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
        }
        return binding.getRoot();
    }
}
