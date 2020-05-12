package com.example.myapplication.Fragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myapplication.Adapter.AdapterAnime;
import com.example.myapplication.Adapter.AdapterVideo;
import com.example.myapplication.Adapter.SliderAdapterExample;
import com.example.myapplication.Contect.SliderContect;
import com.example.myapplication.Contect.VideoContect;
import com.example.myapplication.Define;
import com.example.myapplication.InterFace.IonClickVideo;
import com.example.myapplication.PutVideoList;
import com.example.myapplication.R;
import com.example.myapplication.SQL.SQLHelper;
import com.example.myapplication.SQL.SQLHelperList;
import com.example.myapplication.databinding.ActivityTrangChuBinding;
import com.smarteist.autoimageslider.IndicatorAnimations;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

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
    List<VideoContect> contects1;
    List<VideoContect> videoContects;
    List<VideoContect> list;
    List<SliderContect> sliderContects;
    AdapterVideo adapterVideo;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    AdapterAnime adapterAnime;
    String urlApi = Define.hotVideo;
    String urlPL = Define.phimLe;
    SQLHelper sqlHelper;
    SQLHelperList sqlHelperList;
    int dem = 0;
    PutVideoList putVideoList;
    SliderAdapterExample sliderAdapterExample;

    public static TrangChu newInstance() {
        Bundle args = new Bundle();
        TrangChu fragment = new TrangChu();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.activity_trang_chu, container, false);
        contects = new ArrayList<>();
        contects1 = new ArrayList<>();
        sliderContects = new ArrayList<>() ;
        new DogetData(urlApi).execute();
        new DogetData1(urlPL).execute();

        putVideoList = new PutVideoList(getContext());

        sqlHelper = new SQLHelper(getContext());
        sqlHelperList = new SQLHelperList(getContext());
        list = sqlHelperList.getAllProductAdvanced();
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        editor = sharedPreferences.edit();

        sliderAdapterExample = new SliderAdapterExample( getContext());
        sliderAdapterExample.setCount(5);
        binding.imageSlider.setSliderAdapter(sliderAdapterExample);
        binding.imageSlider.setIndicatorAnimation(IndicatorAnimations.FILL);
        binding.imageSlider.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        binding.imageSlider.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
        binding.imageSlider.setIndicatorSelectedColor(Color.WHITE);
        binding.imageSlider.setIndicatorUnselectedColor(Color.GRAY);
        binding.imageSlider.setScrollTimeInSec(3);
        binding.imageSlider.startAutoCycle();


        adapterAnime = new AdapterAnime(contects1);
        adapterVideo = new AdapterVideo(contects);

        adapterVideo.setIonClickVideo(new IonClickVideo() {
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
                editor.putString(Define.id,contect.getId());
                editor.putString(Define.avatar,contect.getImg());
                editor.commit();

                Intent intent = new Intent(getContext(), FullScreen.class);
                startActivity(intent);

            }
        });
        adapterAnime.setIonClickVideo(new IonClickVideo() {
            @Override
            public void onClickItem(VideoContect contect) {

                if (list != null) {
                    sqlHelperList.delAllProduct();
                }
                putVideoList.onPutVideo(contects1, contect, sqlHelperList);
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


    public class DogetData extends AsyncTask<Void, Void, Void> {
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
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false);
                    binding.rvPhimBo.setAdapter(adapterVideo);
                    binding.rvPhimBo.setLayoutManager(layoutManager);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
    public class DogetData1 extends AsyncTask<Void, Void, Void> {
        String result = "";
        String urlPL;

        public DogetData1(String url) {
            this.urlPL = url;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                URL url = new URL(urlPL);
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
                    contects1.add(contect);
                    RecyclerView.LayoutManager layoutManager1 = new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false);
                    binding.rvAnime.setAdapter(adapterAnime);
                    binding.rvAnime.setLayoutManager(layoutManager1);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}

