package com.example.myapplication.Fragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.example.myapplication.R;
import com.example.myapplication.SQLHelper;
import com.example.myapplication.databinding.ActivityPhimHaiBinding;

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

public class PhimHai extends Fragment {
    ActivityPhimHaiBinding binding;
    List<VideoContect> contects;
    VideoContect contect;
    AdapterVideo adapterVideo;
    String url = Define.hotVideo;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    List<VideoContect> videoContects;
    SQLHelper sqlHelper;
    int dem = 0;

    public static PhimHai newInstance() {
        Bundle args = new Bundle();
        PhimHai fragment = new PhimHai ();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.activity_phim_hai,container,false);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        editor = sharedPreferences.edit();
        sqlHelper = new SQLHelper(getContext());
        contects = new ArrayList<>();
        adapterVideo = new AdapterVideo(contects);
        new DogetData(url).execute();
        adapterVideo.setIonClickVideo(new IonClickVideo() {
            @Override
            public void onClickItem(VideoContect contect) {
                videoContects = sqlHelper.getAllProductAdvanced();
                String avatar = contect.getImg();
                String title = contect.getName();
                String id = contect.getId();
                String date = contect.getDate();
                String link = contect.getUrl();


                for(int i=0;i<videoContects.size();i++){
                    if(videoContects.get(i).getId().equalsIgnoreCase(id) == true){
                        dem = dem+1;
                    }
                }
                if (dem == 0){
                    sqlHelper.insertProduct(id,avatar,link,date,title);
                    dem = 0;
                }
                editor.putString(Define.file_mp4,contect.getUrl());
                editor.putString(Define.date_create,contect.getDate());
                editor.putString(Define.title,contect.getName());
                editor.commit();
                getFragmentManager().beginTransaction().replace(R.id.container,new Video()).commit();
            }
        });
        return binding.getRoot();
    }
    class DogetData extends AsyncTask<Void,Void,Void>{
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
                    contect = new VideoContect(title,date,avatar,linkVideo,id);
                    contects.add(contect);
                }
                RecyclerView.LayoutManager layoutManager=new GridLayoutManager(getContext(),2,RecyclerView.VERTICAL,false);


                binding.rvPhimHai.setAdapter(adapterVideo);
                binding.rvPhimHai.setLayoutManager(layoutManager);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
