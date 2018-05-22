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
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

public class Score extends Activity {

    Button shareButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.score);


        TextView currentscoreLabel = (TextView) findViewById(R.id.currentscoreLabel);
        TextView highscoreLabel1 = (TextView) findViewById(R.id.highscoreLabel1);


        int currentscore = getIntent().getIntExtra("SCORE", 0);
        Log.d("currentScore: ", Integer.toString(currentscore));
        currentscoreLabel.setText(currentscore + "");

        // saved Scores
        SharedPreferences settings = getSharedPreferences("GAME_DATA", Context.MODE_PRIVATE);
        final int highscore = settings.getInt("HIGH_SCORE", 0);

        if(currentscore > highscore) {
            highscoreLabel1.setText("HighScore : " + currentscore);
        }

            // Spielstand sichern

        SharedPreferences.Editor editor = settings.edit();
        editor.putInt("HIGH_SCORE", currentscore);
        editor.commit();


            shareButton = (Button) findViewById(R.id.share);
            shareButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(Intent.ACTION_SEND);
                    i.setType("text/plain");
                    String shareBody = "Highscore Doubling Balls: " + highscore + " Punkte !";
                    String shareSub = "Doubling Balls Highscore";
                    i.putExtra(Intent.EXTRA_SUBJECT, shareBody);
                    i.putExtra(Intent.EXTRA_TEXT, shareBody);
                    startActivity(Intent.createChooser(i,"Highscore teilen"));
                }
            });
        }


    public void tryAgain(View view){
        startActivity(new Intent(getApplicationContext(), BubblesActivity.class));
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
    @Override
    protected void onResume() {
        super.onResume();
    }
}

