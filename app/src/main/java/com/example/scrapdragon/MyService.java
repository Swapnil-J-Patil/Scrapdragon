package com.example.scrapdragon;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;

import androidx.annotation.Nullable;
public class MyService extends Service {
    private MediaPlayer mediaPlayer;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;

    }
    public void onCreate()
    {
        mediaPlayer=MediaPlayer.create(getApplicationContext(),R.raw.sword);
        mediaPlayer.setLooping(false);
    }
    public void onStart(Intent intent,int startId)
    {
        mediaPlayer.start();
    }
    public void onDestroy()
    {
        mediaPlayer.stop();
    }
}
