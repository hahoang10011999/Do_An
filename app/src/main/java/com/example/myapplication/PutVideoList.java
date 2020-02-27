package com.example.myapplication;

import android.content.Context;

import com.example.myapplication.Contect.VideoContect;
import com.example.myapplication.SQL.SQLHelper;
import com.example.myapplication.SQL.SQLHelperList;

import java.util.List;

public class PutVideoList {
    Context context;

    public PutVideoList(Context context) {
        this.context = context;
    }


    public void onPutVideo(List<VideoContect> contects, VideoContect contect, SQLHelperList sqlHelperList){

        for (int i = 0; i < contects.size(); i++) {

            if (contects.get(i).getId() != contect.getId()) {

                String avatar1 = contects.get(i).getImg();
                String title1 = contects.get(i).getName();
                String id1 = contects.get(i).getId();
                String date1 = contects.get(i).getDate();
                String link1 = contects.get(i).getUrl();
                sqlHelperList.insertProduct(id1, avatar1, link1, date1, title1);
            }

        }
    }
    public void onPutVideoHistory(List<VideoContect> videoContects, VideoContect contect, SQLHelper sqlHelper, int dem){
        videoContects = sqlHelper.getAllProductAdvanced();
        String avatar = contect.getImg();
        String title = contect.getName();
        String id = contect.getId();
        String date = contect.getDate();
        String link = contect.getUrl();

        for (int i = 0; i < videoContects.size(); i++) {
            if (videoContects.get(i).getId().equalsIgnoreCase(id) == true) {
                dem = dem + 1;
            }
        }
        if (dem == 0) {
            sqlHelper.insertProduct(id, avatar, link, date, title);
            dem = 0;
        }
    }
}
