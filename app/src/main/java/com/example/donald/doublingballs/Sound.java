package com.example.donald.doublingballs;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

public class Sound {
    public static SoundPool soundPool;
    private static int hitSound;
    private static int deathSound;
    private static int bounceSound;
    private static int laserSound;
    private static int blubbSound;
    public static int walkingSound;
    private static int buttonSound;
    private static int backgroundSound;
    private static int radiobuttonSound;
    private static int highscoreSound;

    public Sound(Context context){
        soundPool = new SoundPool(2, AudioManager.STREAM_MUSIC,0);
        hitSound = soundPool.load(context, R.raw.hit, 1);
        deathSound = soundPool.load(context, R.raw.deathsound, 1);
        bounceSound = soundPool.load(context, R.raw.bouncingball, 1);
        laserSound = soundPool.load(context, R.raw.lasergun, 1);
        blubbSound = soundPool.load(context, R.raw.blubb, 1);
        walkingSound = soundPool.load(context, R.raw.walking, 1);
        buttonSound = soundPool.load(context, R.raw.buttonsound, 1);
        radiobuttonSound = soundPool.load(context, R.raw.radiobuttonbeep, 1);
        highscoreSound = soundPool.load(context, R.raw.newhighscore, 1);
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


}
