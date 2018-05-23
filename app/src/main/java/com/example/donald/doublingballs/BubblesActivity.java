package com.example.donald.doublingballs;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Window;
import android.view.WindowManager;

public class BubblesActivity extends AppCompatActivity {

    MediaPlayer backgroundMusic;
    public static BubblesView bv;
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

        setContentView(R.layout.activity_bubble);

    }

    @Override
    protected void onStart() {
        super.onStart();
        backgroundMusic = MediaPlayer.create(getApplicationContext(), R.raw.gamemusic);
        backgroundMusic.setLooping(true);
        backgroundMusic.start();

        SharedPreferences settings = getSharedPreferences("GAME_DATA", Context.MODE_PRIVATE);
        Score.highScore = settings.getInt("HIGH_SCORE", 0);
        Score.settings = settings;
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt("HIGH_SCORE", Score.highScore);
        editor.commit();
    }

    @Override
    protected void onStop() {
        finish();
        super.onStop();
        backgroundMusic.stop();
    }

    @Override
    protected void onPause() {
        finish();
        super.onPause();
        //bv.surfaceDestroyed(bv.surfaceHolder);
        //onDestroy();
    }

    /**
     * Dispatch onResume() to fragments.  Note that for better inter-operation
     * with older versions of the platform, at the point of this call the
     * fragments attached to the activity are <em>not</em> resumed.  This means
     * that in some cases the previous state may still be saved, not allowing
     * fragment transactions that modify the state.  To correctly interact
     * with fragments in their proper state, you should instead override
     * {@link #onResumeFragments()}.
     */
    @Override
    protected void onResume() {
        super.onResume();
        //onCreate(savedInstanceState);
    }
}
