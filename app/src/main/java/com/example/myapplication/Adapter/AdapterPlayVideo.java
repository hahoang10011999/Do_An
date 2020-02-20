package com.example.myapplication.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myapplication.Contect.VideoContect;
import com.example.myapplication.InterFace.IonClickVideo;
import com.example.myapplication.R;

import java.util.List;

public class AdapterPlayVideo extends RecyclerView.Adapter<AdapterPlayVideo.ViewHoder> {
    List<VideoContect> contects;
    Context context;
    IonClickVideo ionClickVideo;

    public void setIonClickVideo(IonClickVideo ionClickVideo) {
        this.ionClickVideo = ionClickVideo;
    }

    public AdapterPlayVideo(List<VideoContect> contects) {
        this.contects = contects;
    }

    @NonNull
    @Override
    public AdapterPlayVideo.ViewHoder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.nextvideo,parent,false);
        ViewHoder viewHoder = new ViewHoder(view);
        context = parent.getContext();
        return viewHoder;
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterPlayVideo.ViewHoder holder, int position) {
        final VideoContect contect = contects.get(position);
        Glide.with(context).load(contect.getImg()).into(holder.img);
        holder.name.setText(contect.getName());
        holder.date.setText(contect.getDate());
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ionClickVideo.onClickItem(contect);
            }
        });
    }

    @Override
    public int getItemCount() {
        return contects.size();
    }

    public class ViewHoder extends RecyclerView.ViewHolder {
        ImageView img;
        TextView name,date;
        RelativeLayout layout;
        public ViewHoder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.imgNext);
            name = itemView.findViewById(R.id.nameNext);
            date = itemView.findViewById(R.id.dateNext);
            layout = itemView.findViewById(R.id.layout);
        }
    }
}
