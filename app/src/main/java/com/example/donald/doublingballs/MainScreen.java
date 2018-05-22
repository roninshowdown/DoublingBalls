package com.example.donald.doublingballs;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainScreen extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display);
    }

    public void onButtonClickPlay(View v){
        if(v.getId() == R.id.play){
            Intent i = new Intent(MainScreen.this, BubblesActivity.class);
            startActivity(i);
        }
    }
    public void onButtonClickShop(View v){
        if(v.getId() == R.id.shop){
            Intent i = new Intent(MainScreen.this, Shop.class);
            startActivity(i);
        }
    }
}
