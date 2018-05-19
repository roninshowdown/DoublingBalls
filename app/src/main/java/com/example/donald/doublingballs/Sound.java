package com.example.donald.doublingballs;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

public class Sound {
    private static SoundPool soundPool;
    private static int hitSound;
    private static int deathSound;
    private static int bounceSound;

    public Sound(Context context){
        soundPool = new SoundPool(2, AudioManager.STREAM_MUSIC,0);
        hitSound = soundPool.load(context, R.raw.hit, 1);
        deathSound = soundPool.load(context, R.raw.deathsound, 1);
        bounceSound = soundPool.load(context, R.raw.bouncingBall, 1);
    }
    public void playHitSound(){
        // play(int soundID, float leftVolume, float rightVolume, int priority, int loop, float rate)
        soundPool.play(hitSound,1,1,1,0,1);
    }
    public void playDeathSound(){
        // play(int soundID, float leftVolume, float rightVolume, int priority, int loop, float rate)
        soundPool.play(deathSound,1,1,1,0,1);
    }
    public void playBounceSound(){
        // play(int soundID, float leftVolume, float rightVolume, int priority, int loop, float rate)
        soundPool.play(bounceSound,1,1,1,0,1);
    }
}
