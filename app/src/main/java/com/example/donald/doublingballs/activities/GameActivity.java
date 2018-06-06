package com.example.donald.doublingballs.activities;


import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.example.donald.doublingballs.Sound;

public class GameActivity extends AppCompatActivity {

    MediaPlayer backgroundMusic = new MediaPlayer();
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

        if (ShopActivity.music) {
            backgroundMusic = MediaPlayer.create(getApplicationContext(), R.raw.gamemusic2 );
        }
        else{
            backgroundMusic = MediaPlayer.create(getApplicationContext(), R.raw.gamemusic);
        }
        backgroundMusic.setLooping(true);
        backgroundMusic.start();

    }

    @Override
    protected void onPause() {
        super.onPause();

        backgroundMusic.stop();
        backgroundMusic.release();
        backgroundMusic = null;

        //Sound.getInstance(this).soundPool = null;

        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        /*
        backgroundMusic.stop();
        backgroundMusic.release();
        backgroundMusic = null;

        Sound.soundPool.release();
        Sound.soundPool = null;

        finish();
        Log.d("onPause()", "isFinishing(): "+Boolean.toString(isFinishing()));
        //bv.surfaceDestroyed(bv.surfaceHolder);
        Log.d("onPause()", "running: "+Boolean.toString(bv.gameLoop.running));
        super.onPause();
        //onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //onCreate(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        /*
        if(bv.gameLoop != null) {
            bv.gameLoop.running = false;
            try {
                bv.gameLoop.interrupt();
                bv.gameLoop.join(500);
                if (bv.gameLoop.isAlive()) {
                    Log.e("surfaceDestroyed: ", "thread bugged as fuck");
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        bv.gameLoop = null;
        */
        super.onDestroy();
    }
}
