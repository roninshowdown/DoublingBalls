package com.example.donald.doublingballs.activities;


import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.example.donald.doublingballs.Constants;
import com.example.donald.doublingballs.Sound;

public class GameActivity extends AppCompatActivity {

    MediaPlayer backgroundMusic;
    Bundle savedInstanceState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.savedInstanceState = savedInstanceState;

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        Constants.SCREEN_WIDTH = dm.widthPixels;
        Constants.SCREEN_HEIGHT = dm.heightPixels;


        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.game);
        play(); // MediaPlayer Sound


    }

    public void play(){
        if (backgroundMusic == null){
            if (ShopActivity.music) {
                backgroundMusic = MediaPlayer.create(this, R.raw.gamemusic2);
            }
            else{
                backgroundMusic = MediaPlayer.create(this, R.raw.gamemusic);
            }
                backgroundMusic.setLooping(true);
                backgroundMusic.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        stopPlayer();
                    }
                });


            backgroundMusic.start();
        }
    }

    private void stopPlayer(){
        if (backgroundMusic != null){
            backgroundMusic.release();
            backgroundMusic = null;
        }
    }
    @Override
    protected void onPause(){
        super.onPause();
        finish();
    }
    @Override
    protected void onStop(){
        super.onStop();
        stopPlayer();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        stopPlayer();

    }
}
