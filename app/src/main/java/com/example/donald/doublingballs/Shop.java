package com.example.donald.doublingballs;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RadioButton;

public class Shop extends Activity {

    public static boolean music, background, shield, speed, improvedShot;
    public Sound sound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.shop);
        sound = new Sound(this);
    }

    public void onButtonClickBack(View v) {
        if (v.getId() == R.id.back) {
            Intent i = new Intent(Shop.this, MainScreen.class);
            startActivity(i);
            sound.playButtonSound();
        }
    }

    public void onRadioButtonClicked(View v){
        boolean checked = ((RadioButton) v).isChecked();
        sound.playRadiobuttonbeepSound();

        switch(v.getId()) {
            case R.id.music:
                if (checked) music = true;
                else music = false;
                break;
            case R.id.background:
                if (checked) background = true;
                else background = false;
                break;
            case R.id.shield:
                if (checked) shield = true;
                else shield = false;
                break;
            case R.id.speed:
                if (checked) speed = true;
                else speed = false;
                break;
            case R.id.improvedShot:
                if (checked) improvedShot = true;
                else improvedShot = false;
                break;
        }
    }
}

