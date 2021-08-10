package com.example.popballoons;

import android.app.Activity;
import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.util.Log;

public class Audio {

    private static String TAG;
    private int soundId;
    private MediaPlayer mplayer;
    private float volume;
    private SoundPool soundPool;
    private boolean isLoaded;

    public Audio(Activity activity){

        AudioManager audioManager = (AudioManager) activity.getSystemService(Context.AUDIO_SERVICE);

        float actVolume = (float) audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);

        float maxVolume = (float) audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);

        volume = actVolume / maxVolume;

        activity.setVolumeControlStream(AudioManager.STREAM_MUSIC);

        AudioAttributes audioAtrrib = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build();

        soundPool = new SoundPool.Builder()
                .setAudioAttributes(audioAtrrib)
                .setMaxStreams(6)
                .build();

        soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener(){
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status){
                Log.d(TAG, "Soundpool is loaded");
                isLoaded = true;
            }
        });
        soundId = soundPool.load(activity, R.raw.pop, 1);
    }

    public void playSound(){
        if (isLoaded){
            soundPool.play(soundId, volume, volume, 1, 0, 1f);
        }
        Log.d(TAG,"Playsound");
    }

    public void prepareMediaPlayer(Context ctx) {
        mplayer = MediaPlayer.create(ctx.getApplicationContext(),
                R.raw.rcm);
        mplayer.setVolume(05.f, 0.5f);
        mplayer.setLooping(true);
    }

    public void playMusic() {
        mplayer.start();
    }

    public void stopMusic() {
        mplayer.stop();
    }

    public void pauseMusic() {
        mplayer.pause();
    }
}


