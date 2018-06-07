package com.example.donald.doublingballs;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

import com.example.donald.doublingballs.activities.R;

public class Sound {

    private static Sound instance = null;

    public SoundPool soundPool;
    private int hitSound;
    private int deathSound;
    private int bounceSound;
    private int laserSound;
    private int blubbSound;
    private int walkingSound;
    private int buttonSound;
    private int radiobuttonSound;
    private int highscoreSound;
    private int improvedShotSound;


    public static Sound getInstance(Context c) {
        if (instance == null) instance = new Sound(c);
        return instance;
    }

    private Sound(Context context){
        soundPool = new SoundPool(6, AudioManager.STREAM_MUSIC,0);
        hitSound = soundPool.load(context, R.raw.hit, 1);
        deathSound = soundPool.load(context, R.raw.deathsound, 1);
        bounceSound = soundPool.load(context, R.raw.bouncingball, 1);
        laserSound = soundPool.load(context, R.raw.lasergun, 1);
        blubbSound = soundPool.load(context, R.raw.blubb, 1);
        walkingSound = soundPool.load(context, R.raw.walking, 1);
        buttonSound = soundPool.load(context, R.raw.buttonsound, 1);
        radiobuttonSound = soundPool.load(context, R.raw.radiobuttonbeep, 1);
        highscoreSound = soundPool.load(context, R.raw.newhighscore, 1);
        improvedShotSound = soundPool.load(context, R.raw.bigshot, 1);
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
    public void playLasergunSound(){
        // play(int soundID, float leftVolume, float rightVolume, int priority, int loop, float rate)
        soundPool.play(laserSound,1,1,1,0,1);
    }
    public void playBlubbSound(){
        // play(int soundID, float leftVolume, float rightVolume, int priority, int loop, float rate)
        soundPool.play(blubbSound,1,1,1,0,1);
    }
    public void playWalkingSound(){
        // play(int soundID, float leftVolume, float rightVolume, int priority, int loop, float rate)
         soundPool.play(walkingSound,1,1,1,1,1);
    }
    public void playButtonSound(){
        // play(int soundID, float leftVolume, float rightVolume, int priority, int loop, float rate)
        soundPool.play(buttonSound,1,1,1,0,1);
    }
    public void playRadiobuttonbeepSound(){
        // play(int soundID, float leftVolume, float rightVolume, int priority, int loop, float rate)
        soundPool.play(radiobuttonSound,1,1,1,0,1);
    }
    public void playHighscoreSound(){
        // play(int soundID, float leftVolume, float rightVolume, int priority, int loop, float rate)
        soundPool.play(highscoreSound,1,1,1,0,1);
    }
    public void playBigShotSound(){
        // play(int soundID, float leftVolume, float rightVolume, int priority, int loop, float rate)
        soundPool.play(improvedShotSound,1,1,1,0,1);
    }


}
