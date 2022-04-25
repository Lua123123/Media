package com.example.media.fragment;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.media.R;
import com.example.media.service.MyService;
import com.example.media.service.MyServiceUrl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Fragment1 extends Fragment {

    public static final String URL_FILE = "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-1.mp3";
    private Button            btnStartService, btnStopService, btnPlayServiceToUrl, btnStopServiceToUrl;
    private EditText          edt_url;
    private RelativeLayout    layoutBottom;
    private TextView          tvNameSong, txtTimeSong, txtTimeTotal;
    private ImageView         imgPlayOrPause;
    private MyService         myService;
    private SeekBar           skSound;
    private MyServiceUrl      myServiceFromUrl;
    private boolean           isServiceConnection, isServiceConnectionFromUrl;
    MediaPlayer playerSetTime;

    private ServiceConnection mServiceConnectionFromUrl = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinderFromUrl) {
            MyServiceUrl.MyBinderFromUrl myBinderFromUrl = (MyServiceUrl.MyBinderFromUrl) iBinderFromUrl;
            myServiceFromUrl = myBinderFromUrl.getMyServiceFromUrl();
            isServiceConnectionFromUrl = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            myServiceFromUrl = null;
            isServiceConnectionFromUrl = false;
        }
    };

    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            MyService.MyBinder myBinder = (MyService.MyBinder) iBinder;
            myService = myBinder.getMyService();
            isServiceConnection = true;

            handleLayoutMusic();
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            myService = null;
            isServiceConnection = false;
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_1, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        playerSetTime       = MediaPlayer.create(Fragment1.this.getActivity(), R.raw.bathuongcon);

        btnStartService     = view.findViewById(R.id.btn_start_service);
        btnStopService      = view.findViewById(R.id.btn_stop_service);
        btnPlayServiceToUrl = view.findViewById(R.id.btn_play_service_to_url);
        btnStopServiceToUrl = view.findViewById(R.id.btn_stop_service_to_url);
        layoutBottom        = view.findViewById(R.id.layout_bottom);
        tvNameSong          = view.findViewById(R.id.tv_name_song);
        imgPlayOrPause      = view.findViewById(R.id.img_play_or_pause);
        skSound             = view.findViewById(R.id.seek_bar);
        txtTimeSong         = view.findViewById(R.id.textViewTimeSong);
        txtTimeTotal        = view.findViewById(R.id.textViewTimeTotal);
        edt_url             = view.findViewById(R.id.edt_url);

        edt_url.setText(URL_FILE);

        btnStartService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickStartService();
//                SetTimeTotal();
//                UpdateTimeSong();
            }
        });

        btnStopService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickStopService();
            }
        });

        btnPlayServiceToUrl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickPlayServiceFromUrl();
            }
        });

        btnStopServiceToUrl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickStopServiceFromUrl();
            }
        });

        imgPlayOrPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (myService.isPlaying()) {
                    myService.pauseMusic();
                } else {
                    myService.resumeMusic();
                }
                setStatusImageViewPlayOrPause();
            }
        });

        skSound.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                Log.d("iii", "Giá trị" + i);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                playerSetTime.seekTo(skSound.getProgress());
                Log.d("iii", "onStopTrackingTouch");
            }
        });
    }

    private void onClickPlayServiceFromUrl() {
        String urlFile = edt_url.getText().toString().trim();
        Intent intentPlayMyServiceUrl = new Intent(Fragment1.this.getActivity(), MyServiceUrl.class);
        Bundle bundle = new Bundle();
        bundle.putString("Data", urlFile);
        intentPlayMyServiceUrl.putExtras(bundle);
        getActivity().startService(intentPlayMyServiceUrl);
        getActivity().bindService(intentPlayMyServiceUrl, mServiceConnectionFromUrl, Context.BIND_AUTO_CREATE);
    }

    private void onClickStopServiceFromUrl() {
        Intent intentStopMyServiceUrl = new Intent(Fragment1.this.getActivity(), MyServiceUrl.class);
        getActivity().stopService(intentStopMyServiceUrl);

        if (isServiceConnectionFromUrl) {
            getActivity().unbindService(mServiceConnectionFromUrl);
            isServiceConnectionFromUrl = false;
        }
    }

    private void onClickStartService() {
        Intent intentPlayMyService = new Intent(Fragment1.this.getActivity(), MyService.class);
        getActivity().startService(intentPlayMyService);
        getActivity().bindService(intentPlayMyService, mServiceConnection, Context.BIND_AUTO_CREATE);

    }

    private void onClickStopService() {
        Intent intentStopMyService = new Intent(Fragment1.this.getActivity(), MyService.class);
        getActivity().stopService(intentStopMyService);

        if (isServiceConnection) {
            getActivity().unbindService(mServiceConnection);
            isServiceConnection = false;
        }

        layoutBottom.setVisibility(View.GONE);
    }

    private void handleLayoutMusic() {
        layoutBottom.setVisibility(View.VISIBLE);
        tvNameSong.setText("Media Player");

        setStatusImageViewPlayOrPause();
    }

    private void setStatusImageViewPlayOrPause() {
        if (myService == null) {
            return;
        }

        if (myService.isPlaying()) {
            imgPlayOrPause.setImageResource(R.drawable.ic_pause);
        } else {
            imgPlayOrPause.setImageResource(R.drawable.ic_play);
        }
    }

    private void SetTimeTotal(){
        SimpleDateFormat setTimeTotal = new SimpleDateFormat("mm:ss");
        txtTimeTotal.setText(setTimeTotal.format(playerSetTime.getDuration()));
        //gán max skSong = playerSetTime.getDuration()
        skSound.setMax(playerSetTime.getDuration());
    }

    private void UpdateTimeSong(){
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                SimpleDateFormat setTimeSong = new SimpleDateFormat("mm:ss");
                txtTimeSong.setText(setTimeSong.format(playerSetTime.getCurrentPosition()));
                skSound.setProgress(playerSetTime.getCurrentPosition());
                handler.postDelayed(this, 500);
                Log.d("iii", "UpdateTimeSong");
            }
        }, 100);
    }
}