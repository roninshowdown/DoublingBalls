package com.example.donald.doublingballs.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.donald.doublingballs.Sound;

public class ScoreActivity extends Activity {

    ImageButton shareButton;
    public static SharedPreferences settings;
    public int currentScore = 0;
    public static int highScore = 0;
    private Sound sound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sound = new Sound(this);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.score);


        TextView currentscoreLabel = (TextView) findViewById(R.id.currentscoreLabel);
        TextView highscoreLabel1 = (TextView) findViewById(R.id.highscoreLabel1);


        currentScore = getIntent().getIntExtra("SCORE", 0);
        Log.d("currentScore: ", Integer.toString(currentScore));
        currentscoreLabel.setText(currentScore + "");

        // saved Scores

        //SharedPreferences settings = getSharedPreferences("GAME_DATA", Context.MODE_PRIVATE);
        //ScoreActivity.highScore = settings.getInt("HIGH_SCORE", 0);

        if(currentScore > highScore) {
            this.highScore = currentScore;
            highscoreLabel1.setText(currentScore+"");
            SharedPreferences.Editor editor = settings.edit();
            editor.putInt("HIGH_SCORE", currentScore);
            editor.commit();
        }
        else{
            highscoreLabel1.setText(highScore+"");
        }
            // Spielstand sichern
            shareButton = (ImageButton) findViewById(R.id.share);
            shareButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sound.playButtonSound();
                    Intent i = new Intent(Intent.ACTION_SEND);
                    i.setType("text/plain");
                    String shareBody = "Mein HighScore in der App Doubling Balls beträgt " + highScore + " Punkte. Versuche das erstmal zu toppen!";
                    String shareSub = "Doubling Balls Highscore";
                    i.putExtra(Intent.EXTRA_SUBJECT, shareBody);
                    i.putExtra(Intent.EXTRA_TEXT, shareBody);
                    startActivity(Intent.createChooser(i,"Highscore teilen"));
                }
            });
        }


    public void onButtonClickBack(View v) {
        if (v.getId() == R.id.back) {
            sound.playButtonSound();
            finish();
        }
    }

    public static void loadHighScore() {

    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}

