package com.example.donald.doublingballs;

import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;

public class BubblesActivity extends AppCompatActivity {

    //public MediaPlayer mediaPlayerBackground = MediaPlayer.create(getApplicationContext(), R.raw.gamemusic);

    private boolean moveLeftHeldDown = false;
    private boolean moveRightHeldDown = false;
    private boolean shootHeldDown = false;

/*
    @Override
    protected void onPause(){
        super.onPause();
        mediaPlayerBackground.release();
    }
    */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        Constants.SCREEN_WIDTH = dm.widthPixels;
        Constants.SCREEN_HEIGHT = dm.heightPixels;
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_bubble);


        //mediaPlayerBackground.setLooping(true);
        //mediaPlayerBackground.start();


/*
        final ImageButton moveLeft = findViewById(R.id.moveLeft);
        moveLeft.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent ev) {
                float xPressed = ev.getX();
                float yPressed = ev.getY();

                switch(ev.getAction() & MotionEvent.ACTION_MASK) {
                    case (MotionEvent.ACTION_DOWN):
                        bw.player.setCurrentState(State.WALK_LEFT);
                        bw.player.setDirection(Direction.LEFT);
                        moveLeftHeldDown = true;
                        break;
                    case (MotionEvent.ACTION_UP):
                        bw.determineStateOnActionUp();
                        moveLeftHeldDown = false;
                        break;
                    case (MotionEvent.ACTION_MOVE):
                        break;
                }
                return true;

            }
        });

        final ImageButton moveRight = findViewById(R.id.moveRight);
        moveRight.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent ev) {
                switch(ev.getAction() & MotionEvent.ACTION_MASK) {
                    case (MotionEvent.ACTION_DOWN):
                        bw.player.setCurrentState(State.WALK_RIGHT);
                        bw.player.setDirection(Direction.RIGHT);
                        moveRightHeldDown = true;
                        break;
                    case (MotionEvent.ACTION_UP):
                        bw.determineStateOnActionUp();
                        moveRightHeldDown = false;
                }
                return true;

            }
        });

        final ImageButton shoot = findViewById(R.id.shoot);
        shoot.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent ev) {
                switch(ev.getAction() & MotionEvent.ACTION_MASK) {
                    case (MotionEvent.ACTION_DOWN):
                        bw.player.setCurrentState(State.SHOOT);
                        if (bw.shots.toArray().length < 3) {
                            bw.shots.add(new Shot(bw.player.getxPos(), 0, bw.shot, bw.player));
                            if(bw.ammo > 0) bw.ammo--;
                        }
                        if (moveRightHeldDown) bw.player.setCurrentState(State.WALK_RIGHT);
                        else if (moveLeftHeldDown) bw.player.setCurrentState((State.WALK_LEFT));
                        break;
                    case (MotionEvent.ACTION_UP):
                        bw.determineStateOnActionUp();
                }
                return true;

            }
        });
        */

    }
}
