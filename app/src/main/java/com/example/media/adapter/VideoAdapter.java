package com.example.media.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.media.Model.Video;
import com.example.media.PlayVideoActivity;
import com.example.media.R;

import java.util.List;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.VideoViewHolder>{

    private Activity mActivity;
    private List<Video> mListVideo;

    public VideoAdapter(Activity mActivity) {
        this.mActivity = mActivity;
    }

    public void  setData(List<Video> list) {
        this.mListVideo = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public VideoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_video, parent, false);
        return new VideoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoViewHolder holder, int position) {
        Video video = mListVideo.get(position);
        if (video == null) {
            return;
        }

        Glide.with(mActivity).load(video.getThumb()).into(holder.imgVideo);
        holder.layoutItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mActivity, PlayVideoActivity.class);
                intent.putExtra("path_video", video.getPath());
                mActivity.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (mListVideo != null) {
            return mListVideo.size();
        }
        return 0;
    }

    public class VideoViewHolder extends RecyclerView.ViewHolder {
        private RelativeLayout layoutItem;
        private ImageView imgVideo;

        public VideoViewHolder(@NonNull View itemView) {
            super(itemView);

            layoutItem = itemView.findViewById(R.id.layout_item);
            imgVideo = itemView.findViewById(R.id.img_video);
        }
    }
}
