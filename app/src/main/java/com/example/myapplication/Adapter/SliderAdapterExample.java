package com.example.myapplication.Adapter;

import android.content.Context;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.myapplication.Define;
import com.example.myapplication.R;
import com.smarteist.autoimageslider.SliderViewAdapter;


public class SliderAdapterExample extends SliderViewAdapter<SliderAdapterExample.SliderAdapterVH> {
     Context context;
    int Count;

    public SliderAdapterExample(Context context) {
        this.context = context;
    }

    public void setCount(int count) {
        Count = count;
    }

    @Override
    public SliderAdapterVH onCreateViewHolder(ViewGroup parent) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.slider_item, null);
        return new SliderAdapterVH(inflate);
    }

    @Override
    public void onBindViewHolder(SliderAdapterVH viewHolder, final int position) {
    switch (position){
        case 0:

            viewHolder.Description.setTextColor(Color.WHITE);
            viewHolder.imageGif.setVisibility(View.GONE);
            Glide.with(viewHolder.itemView)
                    .load(Define.slide1)
                    .into(viewHolder.background);
            break;
        case 1:

            viewHolder.Description.setTextColor(Color.WHITE);
            viewHolder.imageGif.setVisibility(View.GONE);
            Glide.with(viewHolder.itemView)
                    .load(Define.slide2)
                    .into(viewHolder.background);
            break;
        case 2:

            viewHolder.Description.setTextColor(Color.WHITE);
            viewHolder.imageGif.setVisibility(View.GONE);
            Glide.with(viewHolder.itemView)
                    .load(Define.slide3)
                    .into(viewHolder.background);
            break;
        case 3:

            viewHolder.Description.setTextColor(Color.WHITE);
            viewHolder.imageGif.setVisibility(View.GONE);
            Glide.with(viewHolder.itemView)
                    .load(Define.slide4)
                    .into(viewHolder.background);
            break;
        case 4:

            viewHolder.Description.setTextColor(Color.WHITE);
            viewHolder.imageGif.setVisibility(View.GONE);
            Glide.with(viewHolder.itemView)
                    .load(Define.slide5)
                    .into(viewHolder.background);
            break;
        default:
            viewHolder.Description.setTextColor(Color.WHITE);
            viewHolder.imageGif.setVisibility(View.VISIBLE);
            Glide.with(viewHolder.itemView)
                    .load(Define.slide1)
                    .into(viewHolder.background);
            break;


    }

    }

    @Override
    public int getCount() {
        return Count;
    }

    class SliderAdapterVH extends SliderViewAdapter.ViewHolder {

        View itemView;
        ImageView background;
        ImageView imageGif;
        TextView Description;

        public SliderAdapterVH(View itemView) {
            super(itemView);
            background = itemView.findViewById(R.id.iv_auto_image_slider);
            imageGif = itemView.findViewById(R.id.iv_gif_container);
            Description = itemView.findViewById(R.id.tv_auto_image_slider);
            this.itemView = itemView;
        }
    }
}
