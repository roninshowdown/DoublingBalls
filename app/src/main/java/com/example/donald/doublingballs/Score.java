package com.example.donald.doublingballs;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

public class Score extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        TextView currentscoreLabel = (TextView) findViewById(R.id.currentscoreLabel);
        TextView highscoreLabel = (TextView) findViewById(R.id.highscoreLabel);

        setContentView(R.layout.score);
    }
}
