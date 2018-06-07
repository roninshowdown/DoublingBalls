package com.example.donald.doublingballs.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.example.donald.doublingballs.Sound;

public class MainActivity extends Activity {

    public Sound sound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.main);
        sound = Sound.getInstance(getApplicationContext());

        SharedPreferences settings = getSharedPreferences("GAME_DATA", Context.MODE_PRIVATE);
        ScoreActivity.highScore = settings.getInt("HIGH_SCORE", 0);
        ScoreActivity.settings = settings;
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt("HIGH_SCORE", ScoreActivity.highScore);
        editor.apply();
    }

    public void onButtonClickPlay(View v){
        if(v.getId() == R.id.play){
            sound.playButtonSound();
            Intent i = new Intent(MainActivity.this, GameActivity.class);
            startActivity(i);
        }
    }
    public void onButtonClickShop(View v){
        sound.playButtonSound();
        if(v.getId() == R.id.shop){
            Intent i = new Intent(MainActivity.this, ShopActivity.class);
            startActivity(i);
        }
    }
    public void onButtonClickScore(View v){
        sound.playButtonSound();
        if(v.getId() == R.id.score){
            Intent i = new Intent(MainActivity.this, ScoreActivity.class);
            startActivity(i);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}
