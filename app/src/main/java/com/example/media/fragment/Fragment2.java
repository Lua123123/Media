package com.example.media.fragment;

import android.Manifest;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.media.Model.Video;
import com.example.media.R;
import com.example.media.adapter.VideoAdapter;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerFragment;
import com.google.android.youtube.player.YouTubePlayerView;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.normal.TedPermission;

import java.util.ArrayList;
import java.util.List;

public class Fragment2 extends Fragment {   //implements YouTubePlayer.OnInitializedListener
    private static final int REQUEST_VIDEO = 123;
    private RecyclerView rcvVideo;
    private Button btnGetVideo, btnGetVideoUrl;
    private VideoView videoUrl;
    private EditText edtVideoUrl;
    private VideoAdapter videoAdapter;
    private List<Video> mListVideo;
    private YouTubePlayerView youTubePlayerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_2, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rcvVideo = view.findViewById(R.id.rcv_video);
        btnGetVideo = view.findViewById(R.id.btn_get_video);
        btnGetVideoUrl = view.findViewById(R.id.btn_get_video_from_url);
        videoUrl = view.findViewById(R.id.video_url);
        edtVideoUrl = view.findViewById(R.id.edt_video_url);
        edtVideoUrl.setText("http://videocdn.bodybuilding.com/video/mp4/62000/62792m.mp4");
        videoAdapter = new VideoAdapter(Fragment2.this.getActivity());
        GridLayoutManager gridLayoutManager = new GridLayoutManager(Fragment2.this.getActivity(), 2);
        rcvVideo.setLayoutManager(gridLayoutManager);
//        youTubePlayerView = (YouTubePlayerView) view.findViewById(R.id.myYoutube);
//        youTubePlayerView.initialize(API_KEY, (YouTubePlayer.OnInitializedListener) getActivity());

        btnGetVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkPermission();
            }
        });

        btnGetVideoUrl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url_video = edtVideoUrl.getText().toString().trim();
                videoUrl.setVideoPath(url_video);
                videoUrl.setMediaController(new MediaController(getActivity()));
                videoUrl.requestFocus();
                videoUrl.start();
            }
        });
    }

    private void checkPermission() {
        PermissionListener permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                Toast.makeText(Fragment2.this.getActivity(), "Permission Granted", Toast.LENGTH_SHORT).show();
                getAllVideoFromGallery();
            }

            @Override
            public void onPermissionDenied(List<String> deniedPermissions) {
                Toast.makeText(Fragment2.this.getActivity(), "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
            }

        };

        TedPermission.create()
                .setPermissionListener(permissionlistener)
                .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                .setPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .check();
    }

    private void getAllVideoFromGallery() {
        mListVideo = new ArrayList<>();
        Uri uri;
        Cursor cursor;
        int columnIndexData, thumb;

        String absolutePathOfImage = null;
        String thumbnail = null;
        uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;

        String[] projection = {MediaStore.MediaColumns.DATA, MediaStore.Video.Media.BUCKET_DISPLAY_NAME,
        MediaStore.Video.Media._ID, MediaStore.Video.Thumbnails.DATA};

        final String orderBy = MediaStore.Images.Media.DATE_TAKEN;
        cursor = Fragment2.this.getActivity().getContentResolver().query(uri, projection, null, null, orderBy + " DESC");
        columnIndexData = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        thumb = cursor.getColumnIndexOrThrow(MediaStore.Video.Thumbnails.DATA);

        while (cursor.moveToNext()) {
            absolutePathOfImage = cursor.getString(columnIndexData);
            thumbnail = cursor.getString(thumb);

            Video video = new Video();
            video.setPath(absolutePathOfImage);
            video.setThumb(thumbnail);

            mListVideo.add(video);
        }

        videoAdapter.setData(mListVideo);
        rcvVideo.setAdapter(videoAdapter);
    }

}