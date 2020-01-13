package com.example.myapplication.Fragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.myapplication.Adapter.AdapterAnime;
import com.example.myapplication.Adapter.AdapterVideo;
import com.example.myapplication.Contect.VideoContect;
import com.example.myapplication.Define;
import com.example.myapplication.InterFace.IonClickVideo;
import com.example.myapplication.R;
import com.example.myapplication.VideoTest;
import com.example.myapplication.databinding.ActivityTrangChuBinding;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public class TrangChu extends Fragment {
     ActivityTrangChuBinding binding;
    List<VideoContect> contects;
    AdapterVideo adapterVideo;
    ProgressBar progressBar;
    AdapterAnime adapterAnime;
    String urlApi = Define.hotVideo;
    public static TrangChu newInstance() {
        Bundle args = new Bundle();
        TrangChu fragment = new TrangChu ();
        fragment.setArguments(args);
        return fragment;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.activity_trang_chu,container,false);
        contects = new ArrayList<>();
        new DogetData(urlApi).execute();
        adapterVideo = new AdapterVideo(contects);
        adapterVideo.setIonClickVideo(new IonClickVideo() {
            @Override
            public void onClickItem(VideoContect contect) {
                Intent intent = new Intent(getContext(), VideoTest.class);
                intent.putExtra("linkVideo",contect.getUrl());
                intent.putExtra("name",contect.getName());
                intent.putExtra("date",contect.getDate());
                startActivity(intent);
            }
        });
        return binding.getRoot();
    }

    class DogetData extends AsyncTask<Void, Void, Void> {
        String result = "";
        String urlApi;

        public DogetData(String url) {
            this.urlApi = url;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                URL url = new URL(urlApi);
                URLConnection connection = url.openConnection();
                InputStream is = connection.getInputStream();
                int byteCharacter;
                while ((byteCharacter = is.read()) != -1) {
                    result += (char) byteCharacter;
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            try {
                JSONArray jsonArray = new JSONArray(result);
                int length = jsonArray.length();
                VideoContect contect;
                for (int i = 0; i < length; i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String title = jsonObject.getString(Define.title);
                    String avatar = jsonObject.getString(Define.avatar);
                    String date = jsonObject.getString(Define.date_create);
                    String linkVideo = jsonObject.getString(Define.file_mp4);
                    contect = new VideoContect(title,date,avatar,linkVideo);
                    contects.add(contect);
                }
                RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(getContext(),RecyclerView.HORIZONTAL,false);
                RecyclerView.LayoutManager layoutManager1 = new LinearLayoutManager(getContext(),RecyclerView.HORIZONTAL,false);

                adapterVideo = new AdapterVideo(contects);
                binding.rvPhimBo.setAdapter(adapterVideo);
                binding.rvPhimBo.setLayoutManager(layoutManager);


                adapterAnime = new AdapterAnime(contects);
                binding.rvAnime.setAdapter(adapterAnime);
                binding.rvAnime.setLayoutManager(layoutManager1);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }



}

