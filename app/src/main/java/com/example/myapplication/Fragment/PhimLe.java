package com.example.myapplication.Fragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myapplication.Adapter.AdapterVideo;
import com.example.myapplication.Contect.VideoContect;
import com.example.myapplication.Define;
import com.example.myapplication.InterFace.IonClickVideo;
import com.example.myapplication.PutVideoList;
import com.example.myapplication.R;
import com.example.myapplication.SQL.SQLHelper;
import com.example.myapplication.SQL.SQLHelperList;
import com.example.myapplication.databinding.ActivityPhimLeBinding;

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

public class PhimLe extends Fragment {
    ActivityPhimLeBinding binding;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    List<VideoContect> contects;
    AdapterVideo adapterVideo;
    String urlPL = Define.phimLe;
    List<VideoContect> videoContects;
    List<VideoContect> list;
    SQLHelper sqlHelper;
    SQLHelperList sqlHelperList;
    int dem = 0;
    PutVideoList putVideoList;

    public static PhimLe newInstance() {
        Bundle args = new Bundle();
        PhimLe fragment = new PhimLe();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.activity_phim_le, container, false);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        editor = sharedPreferences.edit();
        sqlHelper = new SQLHelper(getContext());
        sqlHelperList = new SQLHelperList(getContext());
        contects = new ArrayList<>();
        list = sqlHelperList.getAllProductAdvanced();

        adapterVideo = new AdapterVideo(contects);
        new PhimLe.DogetData(urlPL).execute();
        putVideoList = new PutVideoList(getContext());
        adapterVideo.setIonClickVideo(new IonClickVideo() {
            @Override
            public void onClickItem(VideoContect contect) {
                if (list != null) {
                    sqlHelperList.delAllProduct();
                }
                if (list != null) {
                    sqlHelperList.delAllProduct();
                }


                putVideoList.onPutVideo(contects, contect, sqlHelperList);
                putVideoList.onPutVideoHistory(videoContects, contect, sqlHelper, dem);

                editor.putString(Define.file_mp4, contect.getUrl());
                editor.putString(Define.date_create, contect.getDate());
                editor.putString(Define.title, contect.getName());
                editor.putString(Define.id,contect.getId());
                editor.putString(Define.avatar,contect.getImg());
                editor.commit();

                Intent intent = new Intent(getContext(), FullScreen.class);
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
                    String id = jsonObject.getString(Define.id);
                    contect = new VideoContect(title, date, avatar, linkVideo, id);
                    contects.add(contect);
                }
                RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 2, RecyclerView.VERTICAL, false);


                binding.rvPhimLe.setAdapter(adapterVideo);
                binding.rvPhimLe.setLayoutManager(layoutManager);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
