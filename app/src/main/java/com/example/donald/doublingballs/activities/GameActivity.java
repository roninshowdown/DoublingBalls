package com.example.donald.doublingballs.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.example.donald.doublingballs.Constants;
import com.example.donald.doublingballs.GameView;
import com.example.donald.doublingballs.R;

public class GameActivity extends AppCompatActivity {

    MediaPlayer backgroundMusic;
    public static GameView bv;
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
    }

    @Override
    protected void onStart() {
        backgroundMusic = MediaPlayer.create(getApplicationContext(), R.raw.gamemusic);
        backgroundMusic.setLooping(true);
        backgroundMusic.start();

        SharedPreferences settings = getSharedPreferences("GAME_DATA", Context.MODE_PRIVATE);
        ScoreActivity.highScore = settings.getInt("HIGH_SCORE", 0);
        ScoreActivity.settings = settings;
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt("HIGH_SCORE", ScoreActivity.highScore);
        editor.commit();
        super.onStart();
    }

    @Override
    protected void onStop() {
        backgroundMusic.stop();
        finish();
        super.onStop();
    }

    @Override
    protected void onPause() {
        //bv.gameLoop.interrupt();
        finish();
        Log.d("onPause()", "isFinishing(): "+Boolean.toString(isFinishing()));
        //bv.surfaceDestroyed(bv.surfaceHolder);
        super.onPause();
        Log.d("onPause()", "running: "+Boolean.toString(bv.gameLoop.running));
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
