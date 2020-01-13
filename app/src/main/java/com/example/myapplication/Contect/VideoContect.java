package com.example.myapplication.Contect;

public class VideoContect {
    String name,date,img,url;

    public VideoContect(String name, String date, String img,String url) {
        this.name = name;
        this.date = date;
        this.img = img;
        this.url = url;
    }

    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}
