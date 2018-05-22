package com.example.donald.doublingballs;

import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Window;
import android.view.WindowManager;

public class BubblesActivity extends AppCompatActivity {

    MediaPlayer backgroundMusic;
    public static BubblesView bv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        Constants.SCREEN_WIDTH = dm.widthPixels;
        Constants.SCREEN_HEIGHT = dm.heightPixels;

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_bubble);

        backgroundMusic = MediaPlayer.create(getApplicationContext(), R.raw.gamemusic);
        backgroundMusic.setLooping(true);
        backgroundMusic.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        backgroundMusic.stop();
        //bv.surfaceDestroyed(bv.surfaceHolder);
        finish();
    }


}
