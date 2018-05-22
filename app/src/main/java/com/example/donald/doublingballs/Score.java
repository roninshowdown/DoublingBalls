package com.example.donald.doublingballs;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

public class Score extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.score);


        TextView currentscoreLabel = (TextView) findViewById(R.id.currentscoreLabel);
        TextView highscoreLabel = (TextView) findViewById(R.id.highscoreLabel);

        int currentscore = getIntent().getIntExtra("SCORE", 0);
        Log.d("currentScore: ", Integer.toString(currentscore));
        currentscoreLabel.setText(currentscore + "");

        SharedPreferences settings = getSharedPreferences("GAME_DATA", Context.MODE_PRIVATE);
        int highscore = settings.getInt("HIGH_SCORE", 0);

        if(currentscore > highscore){
            highscoreLabel.setText("Highscore: " + currentscore);

            // Spielstand sichern

            SharedPreferences.Editor editor = settings.edit();
            editor.putInt("HIGH_SCORE", currentscore);
            editor.commit();
        }
        else{
            highscoreLabel.setText("High Score : " + highscore);
        }

    }

    public void tryAgain(View view){
        startActivity(new Intent(getApplicationContext(), BubblesActivity.class));
    }
}
