package com.example.donald.doublingballs.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;


import com.example.donald.doublingballs.Sound;
import com.example.donald.doublingballs.activities.ScoreActivity;

public class ShopActivity extends Activity {

    public static boolean music, background, shield, speed, improvedShot;
    private Switch musicSW, backgroundSW, shieldSW, speedSW, improvedShotSW;
    public Sound sound;
    private TextView textViewScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.shop);
        sound = Sound.getInstance(getApplicationContext());

        musicSW = (Switch) findViewById(R.id.music);
        backgroundSW = (Switch) findViewById(R.id.background);
        shieldSW = (Switch) findViewById((R.id.shield));
        speedSW = (Switch) findViewById(R.id.speed);
        improvedShotSW = (Switch)findViewById(R.id.shot);

        textViewScore = (TextView) findViewById(R.id.textViewScore);
        textViewScore.setText(Integer.toString(ScoreActivity.highScore));

        musicSW.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean bChecked) {
                if (bChecked) {
                    if (ScoreActivity.highScore >= 500){
                        sound.playRadiobuttonbeepSound();
                        music = true;
                    }
                    else musicSW.setChecked(false);
                } else {
                    music = false;
                }
            }
        });
        shieldSW.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean bChecked) {
                if (bChecked) {
                    if (ScoreActivity.highScore >= 800){
                        sound.playRadiobuttonbeepSound();
                        shield = true;
                    }
                    else shieldSW.setChecked(false);
                } else {
                    shield = false;
                }
            }
        });
        backgroundSW.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean bChecked) {
                sound.playRadiobuttonbeepSound();
                if (bChecked) {
                    if (ScoreActivity.highScore >= 1300) {
                        sound.playRadiobuttonbeepSound();
                        background = true;
                    }
                    else backgroundSW.setChecked(false);
                } else {
                    background = false;
                }
            }
        });
        speedSW.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean bChecked) {
                if (bChecked) {
                    if (ScoreActivity.highScore >= 2000){
                        sound.playRadiobuttonbeepSound();
                        speed = true;
                    }
                    else speedSW.setChecked(false);
                } else {
                    speed = false;
                }
            }
        });
        improvedShotSW.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean bChecked) {
                if (bChecked) {
                    if (ScoreActivity.highScore >= 3000) {
                        sound.playRadiobuttonbeepSound();
                        improvedShot = true;
                    }
                    else improvedShotSW.setChecked(false);
                } else {
                    improvedShot = false;
                }
            }
        });
    }

    public void onButtonClickBack(View v) {
        if (v.getId() == R.id.back) {
            sound.playButtonSound();
            finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        musicSW.setChecked(music);
        backgroundSW.setChecked(background);
        shieldSW.setChecked(shield);
        speedSW.setChecked(speed);
        improvedShotSW.setChecked(improvedShot);
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}

