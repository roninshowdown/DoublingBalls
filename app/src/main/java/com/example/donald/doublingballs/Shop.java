package com.example.donald.doublingballs;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.Switch;

public class Shop extends Activity {

    public static boolean music, background, shield, speed, improvedShot;
    private Switch musicSW, backgroundSW, shieldSW, speedSW, improvedShotSW;
    public Sound sound;
    private String switchOff = "inaktiv";
    private String switchOn = "aktiv";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.shop);
        sound = new Sound(this);

        musicSW = (Switch) findViewById(R.id.music);
        backgroundSW = (Switch) findViewById(R.id.background);
        shieldSW = (Switch) findViewById((R.id.shield));
        speedSW = (Switch) findViewById(R.id.speed);
        improvedShotSW = (Switch)findViewById(R.id.shot);

        musicSW.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean bChecked) {
                if (bChecked) {
                    music = true;
                } else {
                    music = false;
                }
            }
        });
        backgroundSW.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean bChecked) {
                if (bChecked) {
                    background = true;
                } else {
                    background = false;
                }
            }
        });
        shieldSW.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean bChecked) {
                if (bChecked) {
                    shield = true;
                } else {
                    shield = false;
                }
            }
        });
        speedSW.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean bChecked) {
                if (bChecked) {
                    speed = true;
                } else {
                    speed = false;
                }
            }
        });
        improvedShotSW.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean bChecked) {
                if (bChecked) {
                    improvedShot = true;
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

