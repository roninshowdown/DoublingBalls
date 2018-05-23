package com.example.donald.doublingballs;

import java.util.HashSet;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.os.Vibrator;
import android.os.VibrationEffect;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.SurfaceHolder;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

enum GAME {
    PENDING, START, OVER
}

public class BubblesView extends SurfaceView implements SurfaceHolder.Callback {

    private GAME gameMode = GAME.PENDING;

    // Score

    public boolean highscoreSound = true;
    public boolean scoreActivity = true;

    // Colors
    public Paint red;
    public Paint yellow;
    public Paint green;

    //GameContent
    public int life;  // Leben des Spielers
    public int ammo = 3; // Munition des Spielers
    public double difficulty_factor = 1; // Im Laufe des Spiels wird die Ballspawn-Rate erhöht.
    public int reachedScore = 0; // final Score

    // Texte
    private double bonus_score = 0;

    // Zeit
    private Thread timeThread;
    private volatile boolean runningTimeThread=false;    // access to elementary data types (not double or long) are atomic and should be volatile to synchronize content
    private volatile double elapsedTime = 0.0;

    synchronized private void resetElapsedTime() { elapsedTime = 0.0;}
    synchronized private double getElapsedTime() { return elapsedTime; }
    synchronized private void increaseElapsedTime(double increment) { elapsedTime += increment; }

    public SurfaceHolder surfaceHolder = null; //Surface to hijack
    private GameLoop gameLoop; //Display refresh thread

    public Bitmap backgroundBitmap;
    public Bitmap shot;
    private Bitmap buttonLeftImage;
    private Bitmap buttonRightImage;
    private Bitmap buttonShootImage;

    public boolean leftButtonHeldDown = false;
    public boolean rightButtonHeldDown = false;
    public boolean shootButtonHeldDown = false;


    private Bitmap death;
    private Bitmap life1;
    private Bitmap life2;
    private Bitmap life3;
    private Bitmap life4;

    private Bitmap ammo0;
    private Bitmap ammo1;
    private Bitmap ammo2;
    private Bitmap ammo3;

    private Bitmap gameOverImage;
    private Bitmap gameOverHighScoreImage;
    private Bitmap einleitung;

    public HashSet<Shot> shots = new HashSet<>();
    HashSet<Shot> shotsToBeRemoved = new HashSet<>();
    HashSet<Shot> shotsToBeAdded = new HashSet<>();

    private HashSet<BallObject> ballObjects = new HashSet<>();
    HashSet<BallObject> ballObjectsToBeRemoved = new HashSet<>();
    HashSet<BallObject> ballObjectsToBeAdded = new HashSet<>();

    public Player player;

    private Paint mPaint;

    Rect buttonLeft;
    Rect buttonRight;
    Rect buttonShoot;

    int leftButtonPointerID = -1;
    int rightButtonPointerID = -1;
    int shootButtonPointerID = -1;

    public Sound sound;
    Vibrator vibrator;



    /****
     * Constructor
     * @param context
     * @param attrs
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    public BubblesView(Context context, AttributeSet attrs) {
        super(context, attrs);

        getHolder().addCallback((Callback) this);	//Register this class as callback handler for the surface

        if (Shop.background) backgroundBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.background3);
        else backgroundBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.background2);

        BubblesActivity.bv = this;

        sound = new Sound(context);

        vibrator = context.getSystemService(Vibrator.class);


        Bitmap[] leftWalk = new Bitmap[10];
        leftWalk[0]	= BitmapFactory.decodeResource(context.getResources(), R.drawable.leftwalk1);
        leftWalk[1]	= BitmapFactory.decodeResource(context.getResources(), R.drawable.leftwalk2);
        leftWalk[2]	= BitmapFactory.decodeResource(context.getResources(), R.drawable.leftwalk3);
        leftWalk[3]	= BitmapFactory.decodeResource(context.getResources(), R.drawable.leftwalk4);
        leftWalk[4]	= BitmapFactory.decodeResource(context.getResources(), R.drawable.leftwalk5);
        leftWalk[5]	= BitmapFactory.decodeResource(context.getResources(), R.drawable.leftwalk6);
        leftWalk[6]	= BitmapFactory.decodeResource(context.getResources(), R.drawable.leftwalk7);
        leftWalk[7]	= BitmapFactory.decodeResource(context.getResources(), R.drawable.leftwalk8);
        leftWalk[8]	= BitmapFactory.decodeResource(context.getResources(), R.drawable.leftwalk9);
        leftWalk[9]	= BitmapFactory.decodeResource(context.getResources(), R.drawable.leftwalk10);

        Bitmap[] rightWalk = new Bitmap[10];
        rightWalk[0] = BitmapFactory.decodeResource(context.getResources(), R.drawable.rightwalk1);
        rightWalk[1] = BitmapFactory.decodeResource(context.getResources(), R.drawable.rightwalk2);
        rightWalk[2] = BitmapFactory.decodeResource(context.getResources(), R.drawable.rightwalk3);
        rightWalk[3] = BitmapFactory.decodeResource(context.getResources(), R.drawable.rightwalk4);
        rightWalk[4] = BitmapFactory.decodeResource(context.getResources(), R.drawable.rightwalk5);
        rightWalk[5] = BitmapFactory.decodeResource(context.getResources(), R.drawable.rightwalk6);
        rightWalk[6] = BitmapFactory.decodeResource(context.getResources(), R.drawable.rightwalk7);
        rightWalk[7] = BitmapFactory.decodeResource(context.getResources(), R.drawable.rightwalk8);
        rightWalk[8] = BitmapFactory.decodeResource(context.getResources(), R.drawable.rightwalk9);
        rightWalk[9] = BitmapFactory.decodeResource(context.getResources(), R.drawable.rightwalk10);

        Bitmap leftStandStill = BitmapFactory.decodeResource(context.getResources(), R.drawable.leftstandstill1);
        Bitmap rightStandStill = BitmapFactory.decodeResource(context.getResources(), R.drawable.rightstandstill1);

        Bitmap leftStartWalk = BitmapFactory.decodeResource(context.getResources(), R.drawable.leftstart1);
        Bitmap rightStartWalk = BitmapFactory.decodeResource(context.getResources(), R.drawable.rightstart1);

        Bitmap[] shooting = new Bitmap[4];
        shooting[0] = BitmapFactory.decodeResource(context.getResources(), R.drawable.shoot1); // TODO quatsch
        shooting[1] = BitmapFactory.decodeResource(context.getResources(), R.drawable.shoot1);
        shooting[2] = BitmapFactory.decodeResource(context.getResources(), R.drawable.shoot1);
        shooting[3] = BitmapFactory.decodeResource(context.getResources(), R.drawable.shoot1);

        Bitmap[] leftDeath = new Bitmap[7];
        leftDeath[0] = BitmapFactory.decodeResource(context.getResources(), R.drawable.leftdeath1);
        leftDeath[1] = BitmapFactory.decodeResource(context.getResources(), R.drawable.leftdeath1);
        leftDeath[2] = BitmapFactory.decodeResource(context.getResources(), R.drawable.leftdeath1);
        leftDeath[3] = BitmapFactory.decodeResource(context.getResources(), R.drawable.leftdeath2);
        leftDeath[4] = BitmapFactory.decodeResource(context.getResources(), R.drawable.leftdeath2);
        leftDeath[5] = BitmapFactory.decodeResource(context.getResources(), R.drawable.leftdeath2);
        leftDeath[6] = BitmapFactory.decodeResource(context.getResources(), R.drawable.leftdeath3);

        Bitmap[] rightDeath = new Bitmap[7];
        rightDeath[0] = BitmapFactory.decodeResource(context.getResources(), R.drawable.rightdeath1);
        rightDeath[1] = BitmapFactory.decodeResource(context.getResources(), R.drawable.rightdeath1);
        rightDeath[2] = BitmapFactory.decodeResource(context.getResources(), R.drawable.rightdeath1);
        rightDeath[3] = BitmapFactory.decodeResource(context.getResources(), R.drawable.rightdeath2);
        rightDeath[4] = BitmapFactory.decodeResource(context.getResources(), R.drawable.rightdeath2);
        rightDeath[5] = BitmapFactory.decodeResource(context.getResources(), R.drawable.rightdeath2);
        rightDeath[6] = BitmapFactory.decodeResource(context.getResources(), R.drawable.rightdeath3);

        player = new Player(leftWalk, rightWalk, leftStandStill, rightStandStill, leftStartWalk, rightStartWalk, shooting, backgroundBitmap, leftDeath, rightDeath);

        if (Shop.improvedShot) shot = BitmapFactory.decodeResource(context.getResources(), R.drawable.improvedshot);
        else shot = BitmapFactory.decodeResource(context.getResources(), R.drawable.shot);

        buttonLeftImage = BitmapFactory.decodeResource(context.getResources(), R.drawable.leftbutton);
        buttonRightImage = BitmapFactory.decodeResource(context.getResources(), R.drawable.rightbutton);
        buttonShootImage = BitmapFactory.decodeResource(context.getResources(), R.drawable.weapon);

        // Life

        death = BitmapFactory.decodeResource(context.getResources(), R.drawable.death);
        life1 = BitmapFactory.decodeResource(context.getResources(), R.drawable.life1);
        life2 = BitmapFactory.decodeResource(context.getResources(), R.drawable.life2);
        life3 = BitmapFactory.decodeResource(context.getResources(), R.drawable.life3);
        life4 = BitmapFactory.decodeResource(context.getResources(), R.drawable.life4);

        // Ammo

        ammo0 = BitmapFactory.decodeResource(context.getResources(), R.drawable.ammo0);
        ammo1 = BitmapFactory.decodeResource(context.getResources(), R.drawable.ammo1);
        ammo2 = BitmapFactory.decodeResource(context.getResources(), R.drawable.ammo2);
        ammo3 = BitmapFactory.decodeResource(context.getResources(), R.drawable.ammo3);

        gameOverImage = BitmapFactory.decodeResource(context.getResources(), R.drawable.gameover);
        gameOverHighScoreImage = BitmapFactory.decodeResource(context.getResources(), R.drawable.gameoverwithhighscore);

        einleitung = BitmapFactory.decodeResource(context.getResources(), R.drawable.einleitung);

        // BallObjects
        mPaint = new Paint();
        mPaint.setARGB(0xFF, 0x00, 0x80, 0xFF);


        // Colors
        red = new Paint();
        red.setARGB(0xFF, 0xFF, 0x00, 0x00); // rot
        yellow = new Paint();
        yellow.setARGB(0xFF, 0xFF, 0xFF, 0x00); // gelb
        green = new Paint();
        green.setARGB(0xFF, 0x00, 0xFF, 0x00); // grün


        if (Shop.shield) life = 4;
        else life = 3;
    }

    /*
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            float xPressed = event.getX();
            float yPressed = event.getY();
            //while(event.getAction() == MotionEvent.ACTION_BUTTON_PRESS) Log.d("test", "working");
            if (xPressed <= buttonLeft.right && xPressed >= buttonLeft.left && yPressed <= buttonLeft.bottom && yPressed >= buttonLeft.top) {
                player.setCurrentState(State.WALK_LEFT);
                player.setDirection(Direction.LEFT);
            }
            else if (xPressed <= buttonRight.right && xPressed >= buttonRight.left && yPressed <= buttonRight.bottom && yPressed >= buttonRight.top) {
                player.setCurrentState(State.WALK_RIGHT);
                player.setDirection(Direction.RIGHT);
            }
            else if (xPressed <= buttonShoot.right && xPressed >= buttonShoot.left && yPressed <= buttonShoot.bottom && yPressed >= buttonShoot.top) {
                if (shots.toArray().length < 3) {
                    shots.add(new Shot(player.getxPos(), 0, shot, player));
                    player.setCurrentState(State.SHOOT);
                }

            }
            return true;
        }
        else if (event.getAction() == MotionEvent.ACTION_POINTER_UP) {
            switch(player.getCurrentState()) {
                case RIGHT_STAND_STILL: player.setCurrentState(State.RIGHT_STAND_STILL);
                    break;
                case LEFT_STAND_STILL:	player.setCurrentState(State.LEFT_STAND_STILL);
                    break;
                case RIGHT_START_WALK: 	player.setCurrentState(State.RIGHT_STAND_STILL);
                    break;
                case LEFT_START_WALK: 	player.setCurrentState(State.LEFT_STAND_STILL);
                    break;
                case WALK_RIGHT:		player.setCurrentState(State.RIGHT_STAND_STILL);
                    break;
                case WALK_LEFT:			player.setCurrentState(State.LEFT_STAND_STILL);
                    break;
                case SHOOT:				if (player.getDirection() == Direction.LEFT) player.setCurrentState(State.LEFT_STAND_STILL);
                else player.setCurrentState(State.RIGHT_STAND_STILL);

                    break;
            }
            return true;
        }
        return false;
    }
    */

    //ignoring 3 finger touch events; the game is meant to be played with two
    public boolean onTouchEvent(MotionEvent event) {

        if (gameMode == GAME.OVER){ // Bei Gameover kommt man durch Klick ins Score Fenster
            if (scoreActivity){
                scoreActivity = false;
                Intent i = new Intent(getContext(), Score.class);
                i.putExtra("SCORE", reachedScore);
                Log.d("Score: ", Integer.toString(reachedScore));
                getContext().startActivity(i);

            }


        }

        if(gameMode == GAME.PENDING) {
            gameMode = GAME.START;
            gameLoop.startTimeThread(); // startet die Zeit nach dem ersten Button-Klick, if Abfrage sorgt dafür das nur ein Zeit-Thread existiert.
            // Erster Ball
            ballObjects.add(new BallObject(50,backgroundBitmap.getWidth() / 17.06,backgroundBitmap.getHeight() / 3.42857, 10, 30.0 , 0.8, 50, 0.025, BallTypes.MEDIUM, yellow, this)); // medium Ball
        }

        int activePointerID;
        int activePointerIndex;

        switch(event.getActionMasked()) {

            case (MotionEvent.ACTION_DOWN):

                activePointerIndex = event.getActionIndex();
                activePointerID = event.getPointerId(activePointerIndex);


                int xPos = (int) event.getX(activePointerIndex);
                int yPos = (int) event.getY(activePointerIndex);

                //ckeck if the initial touch is in a button
                if (buttonLeft.contains(xPos, yPos)) {

                    player.setCurrentState(State.WALK_LEFT);
                    player.setDirection(Direction.LEFT);

                    leftButtonPointerID = activePointerID;
                    Log.d("test", "leftID:::activeID    "+Integer.toString(leftButtonPointerID)+":::"+Integer.toString(activePointerID));

                    if (!leftButtonHeldDown) {
                        leftButtonHeldDown = true;
                        //sound.playWalkingSound();
                    }

                } else if (buttonRight.contains(xPos, yPos)) {

                    player.setCurrentState(State.WALK_RIGHT);
                    player.setDirection(Direction.RIGHT);

                    rightButtonPointerID = activePointerID;

                    if (!rightButtonHeldDown) {
                        rightButtonHeldDown = true;
                        //sound.playWalkingSound();
                    }

                } else if (buttonShoot.contains(xPos, yPos)) {

                    if (shots.toArray().length < 3) {

                        player.setCurrentState(State.SHOOT);
                        shotsToBeAdded.add(new Shot(player.getxPos(), 0, shot, player));

                        if (Shop.improvedShot) sound.playBigShotSound();
                        else sound.playLasergunSound();

                        shootButtonPointerID = activePointerID;
                        shootButtonHeldDown = true;
                    }
                }
            break;

            case (MotionEvent.ACTION_POINTER_DOWN):

                if (event.getPointerCount() > 2) break;
                activePointerIndex = event.getActionIndex();
                activePointerID = event.getPointerId(activePointerIndex);


                //check if second touch is in a button and set it as the current state, as it is the most recent interaction

                int xPosPD = (int)event.getX(activePointerIndex);
                int yPosPD = (int)event.getY(activePointerIndex);

                if (buttonLeft.contains(xPosPD, yPosPD)) {

                    player.setCurrentState(State.WALK_LEFT);
                    player.setDirection(Direction.LEFT);

                    leftButtonPointerID = activePointerID;
                   // Log.d("test", "leftID:::activeID    "+Integer.toString(leftButtonPointerID)+":::"+Integer.toString(activePointerID));
                    if (!leftButtonHeldDown) {
                        leftButtonHeldDown = true;
                        //sound.playWalkingSound();
                    }
                }
                else if (buttonRight.contains(xPosPD, yPosPD)) {

                    player.setCurrentState(State.WALK_RIGHT);
                    player.setDirection(Direction.RIGHT);

                    rightButtonPointerID = activePointerID;
                    //Log.d("test", "rightID:::activeID   "+Integer.toString(rightButtonPointerID)+":::"+Integer.toString(activePointerID));
                    if (!rightButtonHeldDown) {
                        rightButtonHeldDown = true;
                        //sound.playWalkingSound();
                    }
                }
                else if (buttonShoot.contains(xPosPD, yPosPD)) {

                    if (shots.toArray().length < 3) {

                        player.setCurrentState(State.SHOOT);
                        shotsToBeAdded.add(new Shot(player.getxPos(), 0, shot, player));

                        if (Shop.improvedShot) sound.playBigShotSound();
                        else sound.playLasergunSound();

                        shootButtonPointerID = activePointerID;
                      //  Log.d("test", "shootID:::activeID   "+Integer.toString(shootButtonPointerID)+":::"+Integer.toString(activePointerID));
                        shootButtonHeldDown = true;
                    }
                }
            break;

            case (MotionEvent.ACTION_MOVE):

                if (event.getPointerCount() > 2) break;

                activePointerIndex = event.getActionIndex();
                activePointerID = event.getPointerId(activePointerIndex);

                int currentXPos = (int) event.getX(activePointerIndex);
                int currentYPos = (int) event.getY(activePointerIndex);


                Log.d("test", "leftID:::activeID    " + Integer.toString(leftButtonPointerID) + ":::" + Integer.toString(activePointerID));
                //Log.d("test", "rightID:::activeID" + Integer.toString(rightButtonPointerID) + ":::" + Integer.toString(activePointerID));
                //Log.d("test", "shootID:::activeID" + Integer.toString(shootButtonPointerID) + ":::" + Integer.toString(activePointerID));

                //check if a touch dragged out of a button

                if (leftButtonPointerID == activePointerID && !buttonLeft.contains(currentXPos, currentYPos)) {


                    leftButtonHeldDown = false;
                    //if (shootButtonHeldDown) player.setCurrentState(State.SHOOT);
                    //else if (rightButtonHeldDown) player.setCurrentState(State.WALK_RIGHT);
                    determineStateOnActionUp();

                } else if (rightButtonPointerID == activePointerID && !buttonRight.contains(currentXPos, currentYPos)) {

                    rightButtonHeldDown = false;
                    //if (shootButtonHeldDown) player.setCurrentState(State.SHOOT);
                    //else if (leftButtonHeldDown) player.setCurrentState(State.WALK_LEFT);
                    determineStateOnActionUp();

                } else if (shootButtonPointerID == activePointerID && !buttonShoot.contains(currentXPos, currentYPos)) {

                    shootButtonHeldDown = false;
                    if (leftButtonHeldDown) player.setCurrentState(State.WALK_LEFT);
                    else if (rightButtonHeldDown) player.setCurrentState(State.WALK_RIGHT);
                    else determineStateOnActionUp();
                }


                //check if touch dragged into a button and set it as the current state, as it is the most recent interaction
                if (buttonLeft.contains(currentXPos, currentYPos)) {

                    player.setCurrentState(State.WALK_LEFT);
                    player.setDirection(Direction.LEFT);

                    leftButtonPointerID = activePointerID;
                    if (!leftButtonHeldDown) {
                        leftButtonHeldDown = true;
                        //sound.playWalkingSound();
                    }

                } else if (buttonRight.contains(currentXPos, currentYPos)) {

                    player.setCurrentState(State.WALK_RIGHT);
                    player.setDirection(Direction.RIGHT);

                    rightButtonPointerID = activePointerID;
                    if (!rightButtonHeldDown) {
                        rightButtonHeldDown = true;
                        //sound.playWalkingSound();
                    }

                } else if (buttonShoot.contains(currentXPos, currentYPos)) {

                    if (shots.toArray().length < 3) {
                        //no new Shot added; Action_Move triggers to often, resulting in permanent shooting
                        player.setCurrentState(State.SHOOT);
                        //if (!shootButtonHeldDown) shots.add(new Shot(player.getxPos(), 0, shot, player)); //TODO schuss beim reindraggen

                        shootButtonPointerID = activePointerID;
                        shootButtonHeldDown = true;
                    }
                }
            break;

            case (MotionEvent.ACTION_POINTER_UP):

                if (event.getPointerCount() > 2) break;

                activePointerIndex = event.getActionIndex();
                activePointerID = event.getPointerId(activePointerIndex);

                int xPosPU = (int)event.getX(activePointerIndex);
                int yPosPU = (int)event.getY(activePointerIndex);

                //check if suplementary touch events ended
                if (buttonLeft.contains(xPosPU, yPosPU)) {

                    leftButtonHeldDown = false;
                    if (rightButtonHeldDown) player.setCurrentState(State.WALK_RIGHT);
                    else if (shootButtonHeldDown) player.setCurrentState(State.SHOOT);
                    //if initial touch is not inside a button
                    else determineStateOnActionUp();
                }
                else if (buttonRight.contains(xPosPU, yPosPU)) {

                    rightButtonHeldDown = false;
                    if (leftButtonHeldDown) player.setCurrentState(State.WALK_LEFT);
                    else if (shootButtonHeldDown) player.setCurrentState(State.SHOOT);
                    //if initial touch is not inside a button
                    else determineStateOnActionUp();
                }
                else if (buttonShoot.contains(xPosPU, yPosPU)) {

                    shootButtonHeldDown = false;
                    if (leftButtonHeldDown) player.setCurrentState(State.WALK_LEFT);
                    else if (rightButtonHeldDown) player.setCurrentState(State.WALK_RIGHT);
                    //if initial touch is not inside a button
                    else determineStateOnActionUp();
                }
            break;

            case (MotionEvent.ACTION_UP):

                //initial touch released: default determination of state; no conflicts possible
                determineStateOnActionUp();
            break;

        }
        return true;
    }

    public void determineStateOnActionUp() {

        switch (player.getCurrentState()) {

            case RIGHT_STAND_STILL:
                player.setCurrentState(State.RIGHT_STAND_STILL);
                break;
            case LEFT_STAND_STILL:
                player.setCurrentState(State.LEFT_STAND_STILL);
                break;
            case RIGHT_START_WALK:
                player.setCurrentState(State.RIGHT_STAND_STILL);
                break;
            case LEFT_START_WALK:
                player.setCurrentState(State.LEFT_STAND_STILL);
                break;
            case WALK_RIGHT:
                player.setCurrentState(State.RIGHT_STAND_STILL);
                rightButtonHeldDown = false;
                //Sound.soundPool.pause(Sound.walkingSound);
                break;
            case WALK_LEFT:
                player.setCurrentState(State.LEFT_STAND_STILL);
                leftButtonHeldDown = false;
                //Sound.soundPool.pause(Sound.walkingSound);
                break;
            case SHOOT:
                shootButtonHeldDown = false;
                if (player.getDirection() == Direction.LEFT)
                    player.setCurrentState(State.LEFT_STAND_STILL);
                else player.setCurrentState(State.RIGHT_STAND_STILL);
                break;
        }
    }

    /****
     * drawScreen: Paints background and all bubbles
     * @param c: Canvas to be drawn on
     */
    private void drawScreen(Canvas c) {

        backgroundBitmap = Bitmap.createScaledBitmap(backgroundBitmap, c.getWidth(), c.getHeight(), true);
        // 2560
        // 1440
        c.drawBitmap(backgroundBitmap, new Matrix(), null);

        player.draw(c);

        for (BallObject ballObject : ballObjects) {
            ballObject.draw(c);
        }

        for (Shot shot : shots) {
            shot.draw(c);
        }

        buttonLeft = new Rect(backgroundBitmap.getWidth() * 5/200, backgroundBitmap.getHeight() * 82/100, backgroundBitmap.getWidth()*27/200, backgroundBitmap.getHeight() * 985/1000);
        buttonRight = new Rect( backgroundBitmap.getWidth()* 32/200, backgroundBitmap.getHeight() * 82/100, backgroundBitmap.getWidth()*54/200, backgroundBitmap.getHeight() * 985/1000);
        buttonShoot = new Rect(backgroundBitmap.getWidth()* 85/100, backgroundBitmap.getHeight()*82/100, backgroundBitmap.getWidth()*95/100, backgroundBitmap.getHeight()*97/100);
        /*
        c.drawRect(buttonLeft, paint);
        c.drawRect(buttonRight, paint);
        c.drawRect(buttonShoot, paint);
        */
        c.drawBitmap(buttonLeftImage, null, buttonLeft, null);
        c.drawBitmap(buttonRightImage, null, buttonRight, null);
        c.drawBitmap(buttonShootImage, null, buttonShoot, null);

        // Draw Time
        Paint timePaint = timePaint = new Paint();
        timePaint.setColor(Color.WHITE);
        timePaint.setTextSize(80);
        String timeText = String.format("%.0f", getElapsedTime() + bonus_score);
        c.drawText(timeText, backgroundBitmap.getWidth() * 9/10, backgroundBitmap.getHeight() * 1/15, timePaint);

        // Draw Life
        switch (life){
            case 0:
                c.drawBitmap(death, backgroundBitmap.getWidth() * 19/60, backgroundBitmap.getHeight() * 80/100  , mPaint);
                break;
            case 1:
                c.drawBitmap(life4, backgroundBitmap.getWidth() * 19/60, backgroundBitmap.getHeight() * 80/100  , mPaint);
                break;
            case 2:
                c.drawBitmap(life3, backgroundBitmap.getWidth() * 19/60, backgroundBitmap.getHeight() * 80/100  , mPaint);
                break;
            case 3:
                c.drawBitmap(life2, backgroundBitmap.getWidth() * 19/60, backgroundBitmap.getHeight() * 80/100 , mPaint);
                break;
            case 4:
                c.drawBitmap(life1, backgroundBitmap.getWidth() * 19/60, backgroundBitmap.getHeight() * 80/100 , mPaint);
                break;
        }

        // Draw Ammo
        switch (ammo){
            case 0:
                c.drawBitmap(ammo0, backgroundBitmap.getWidth() * 72/100, backgroundBitmap.getHeight() * 81/100  , mPaint);
                break;
            case 1:
                c.drawBitmap(ammo1, backgroundBitmap.getWidth() * 72/100, backgroundBitmap.getHeight() * 81/100  , mPaint);
                break;
            case 2:
                c.drawBitmap(ammo2, backgroundBitmap.getWidth() * 72/100, backgroundBitmap.getHeight() * 81/100  , mPaint);
                break;
            case 3:
                c.drawBitmap(ammo3, backgroundBitmap.getWidth() * 72/100, backgroundBitmap.getHeight() * 81/100  , mPaint);
                break;
        }

        if (gameMode == GAME.PENDING) {

            //RectF einleitungRect = new RectF(0,0, backgroundBitmap.getWidth(), backgroundBitmap.getHeight());
            //c.drawBitmap(einleitung, null, einleitungRect, null);

            //c.drawBitmap(einleitung, -c.getWidth()/3, 0, null);

            einleitung = Bitmap.createScaledBitmap(einleitung, backgroundBitmap.getWidth(), backgroundBitmap.getHeight(), true);
            c.drawBitmap(einleitung, new Matrix(), null);
        }

        //draw GAMEOVER screen
        if (gameMode == GAME.OVER) {
            runningTimeThread = false; // stoppt die Zeit


            reachedScore = (int) (getElapsedTime() + bonus_score + 1); // + 1 wegen int cast -> Rundungsfehler

            if (reachedScore > Score.highScore) {
                if (highscoreSound){
                    sound.playHighscoreSound();
                    highscoreSound = false;
                }
                RectF gameOverRect = new RectF(backgroundBitmap.getWidth() * 2f / 10, 0f, backgroundBitmap.getWidth() * 8f / 10, backgroundBitmap.getHeight() * 4f / 10);
                c.drawBitmap(gameOverHighScoreImage, null, gameOverRect, null);
                c.drawText(timeText, backgroundBitmap.getWidth() * 52/100, backgroundBitmap.getHeight() * 123/400, timePaint);
            }
            else {
                RectF gameOverRect = new RectF(backgroundBitmap.getWidth() * 2f / 10, 0f, backgroundBitmap.getWidth() * 8f / 10, backgroundBitmap.getHeight() * 4f / 10);
                c.drawBitmap(gameOverImage, null, gameOverRect, null);
                c.drawText(timeText, backgroundBitmap.getWidth() * 52/100, backgroundBitmap.getHeight() * 123/400, timePaint);
            }
        }
    }


    private void calculateDisplay(Canvas canvas, float numberOfFrames) {

        player.update(canvas, numberOfFrames);


        if (gameMode == GAME.START) {
            randomlyAddBallObjects(backgroundBitmap.getWidth(),backgroundBitmap.getHeight());
        }

        if (gameMode == GAME.START || gameMode == GAME.OVER) {
            for (BallObject ballObject : ballObjects) {
                ballObject.update();
            }
        }

        for (Shot shot : shots) {
            shot.update(canvas, numberOfFrames);
            if(shot.outOfRange(canvas)) {
                shotsToBeRemoved.add(shot);
            }
        }

        for (BallObject ballObject : ballObjects) {
            if (areColliding(ballObject, player)){

                ballObjectsToBeRemoved.add(ballObject);
                if(ballObject.ballTypes == BallTypes.LARGE)
                { // große Bälle ziehen 2 Leben ab
                    if (life > 1) life-=2;
                    else life = 0;
                }
                else if (life > 0) life--;
            }
            for (Shot shot : shots) {
                if (areColliding(ballObject, shot)) {
                    bonus_score += ballObject.points;
                    shotsToBeRemoved.add(shot);
                    ballObjectsToBeRemoved.add(ballObject); //TODO BEI GLEICHZEITIGER KOLLISION MIT SPIELER & SCHUSS WIR BALL ZWEIMAL IN DIE LiSTE GSCHRIEBEN, EVTL. EQUALS/HASH ÜBERSCHREIBEN

                    if (ballObject.ballTypes == BallTypes.LARGE){ // Large Balls

                        // Bei der Ballerzeugung könnte man ballObjects.getAccy + 30 für den Parameter accy eintragen
                        // damit man die Schusskraft mit der Schwerkraft des Balls verrechnet: Bewusst nicht gemacht, weil das Spiel sonst zu schwer wird für den Spieler
                        // Die Bälle sollen immer weit genug vom aktuellen Ball wegspringen!


                        ballObjectsToBeAdded.add(new BallObject(50, ballObject.getPosx(), ballObject.getPosy(), 10, 30.0 , 0.8, 50, 0.025, BallTypes.MEDIUM, yellow, this)); // Medium Ball
                        ballObjectsToBeAdded.add(new BallObject(50, ballObject.getPosx(), ballObject.getPosy(), -10,30.0, 0.8, 50, 0.025, BallTypes.MEDIUM, yellow, this)); // Medium Ball
                    }
                    else if (ballObject.ballTypes == BallTypes.MEDIUM) { // Medium Balls

                        ballObjectsToBeAdded.add(new BallObject(20, ballObject.getPosx(),ballObject.getPosy(), 10, 30.0, 0.8, 25, 0.025, BallTypes.SMALL, green, this)); // small Ball
                        ballObjectsToBeAdded.add(new BallObject(20, ballObject.getPosx(), ballObject.getPosy(), -10, 30.0, 0.8, 25, 0.025, BallTypes.SMALL, green, this)); // small Ball

                    }
                    else{
                        // Small Balls lassen keine neuen Bälle spawnen
                    }
                }
            }
        }

        ballObjects.addAll(ballObjectsToBeAdded);
        shots.addAll(shotsToBeAdded);

        for (Shot shot : shotsToBeRemoved) {
            shots.remove(shot);
        }
        ammo = 3- shots.size();

        for (BallObject ballObject : ballObjectsToBeRemoved) {
            ballObjects.remove(ballObject);
        }

        // Listen leeren

        ballObjectsToBeAdded.clear();
        ballObjectsToBeRemoved.clear();
        shotsToBeRemoved.clear();
        shotsToBeAdded.clear();

        if (life == 0) {
            gameMode = GAME.OVER;
            player.setCurrentState(State.DIE);
        }

    }

    public void randomlyAddBallObjects(int screenWidth, int screenHeight) {

        if (elapsedTime > 20 && elapsedTime < 40 ) {
                difficulty_factor = 2;
        }
        else if (elapsedTime > 40 && elapsedTime < 60){
                difficulty_factor = 3;
        }
        else if (elapsedTime > 60 && elapsedTime < 100){
                difficulty_factor = 4;
        }
        else if (elapsedTime > 100 && elapsedTime < 150){
                difficulty_factor = 5;
        }
        else if (elapsedTime > 150 && elapsedTime < 200){
            difficulty_factor = 6;
        }
        else if (elapsedTime > 200 && elapsedTime < 300){
            difficulty_factor = 7;
        }
        difficulty_factor += 0.005; // Schwierigkeit wird nach 300 Sekunden einfach langsam linear erhöht

        if ((int) (Math.random() * 500
        ) > difficulty_factor) // nur wenn die Zahl <= factor ist wird ein Ball gespawnt
            return;
        //backgroundBitmap.getWidth() / 17.05 = x
        //backgroundBitmap.getHeight() / 3.42857 = y
        if(difficulty_factor < 7) {
            double probability = Math.random();
            // Lässt Balle mit unterschiedlichen Wahrscheinlichkeiten (L = 20%, M = 30 %, S = 50% ) spawnen
            if (probability <= 0.1) {
                ballObjects.add(new BallObject(100, 150, backgroundBitmap.getHeight() / 3.42857, 10, 30.0, 0.8, 100, 0.025, BallTypes.LARGE, red, this)); // large Ball
            } else if (probability > 0.1 && probability < 0.3) {
                ballObjects.add(new BallObject(50, 75, backgroundBitmap.getHeight() / 3.42857, 10, 30.0, 0.8, 50, 0.025, BallTypes.MEDIUM, yellow, this)); // medium Ball            }
            } else if (probability >= 0.3 && probability <= 1) {
                ballObjects.add(new BallObject(20, 32.5, backgroundBitmap.getHeight() / 3.42857, 10, 30.0, 0.8, 25, 0.025, BallTypes.SMALL, green, this)); // small Ball
            }
        }
        else {
            double probability = Math.random();
            int spawndirection = (int) (Math.random() * 2 ); // ab difficulty factor von 10 werden auch rechts Bälle spawnen

            switch (spawndirection) {
                case 0:
                    if (probability <= 0.1) { // left spawn
                        ballObjects.add(new BallObject(100, 150,backgroundBitmap.getHeight() / 3.42857, 10, 30.0, 0.8, 100, 0.025, BallTypes.LARGE, red, this)); // large Ball
                    } else if (probability > 0.1 && probability < 0.3) {
                        ballObjects.add(new BallObject(50,75,backgroundBitmap.getHeight() / 3.42857, 10, 30.0 , 0.8, 50, 0.025, BallTypes.MEDIUM, yellow, this)); // medium Ball
                    } else if (probability >= 0.3 && probability <= 1) {
                        ballObjects.add(new BallObject(20, 32.5,backgroundBitmap.getHeight() / 3.42857, 10, 30.0, 0.8, 25, 0.025, BallTypes.SMALL, green, this)); // small Ball
                    }
                    break;
                case 1:
                    if (probability <= 0.1) { // right spawn
                        ballObjects.add(new BallObject(100, screenWidth-150,backgroundBitmap.getHeight() / 3.42857, -10, 30.0, 0.8, 100, 0.025, BallTypes.LARGE, red, this)); // large Ball
                    } else if (probability > 0.1 && probability < 0.3) {
                        ballObjects.add(new BallObject(50,screenWidth-75,backgroundBitmap.getHeight() / 3.42857, -10, 30.0 , 0.8, 50, 0.025, BallTypes.MEDIUM, yellow, this)); // medium Ball
                    } else if (probability >= 0.3 && probability <= 1) {
                        ballObjects.add(new BallObject(20, screenWidth-32.5,backgroundBitmap.getHeight() / 3.42857, -10, 30.0, 0.8, 25, 0.025, BallTypes.SMALL, green, this)); // small Ball

                    }
                    break;
                }
            }

    }

    public boolean areColliding(BallObject b, Shot s) {
        if (b.rect.intersect(s.rect)) {
            sound.playBlubbSound();
            return true;
        }
        return false;
    }

    public boolean areColliding(BallObject b, Player p) {
        if (gameMode != GAME.START) return false;
        if(b.rect.intersect(p.rect)) {
            sound.playHitSound();
            if (life == 1) sound.playDeathSound(); // Nach Collision wird erst das Leben abgezogen
            if (Build.VERSION.SDK_INT >= 26)vibrator.vibrate(VibrationEffect.createOneShot(150, VibrationEffect.DEFAULT_AMPLITUDE));
            else vibrator.vibrate(150);
            return true;
        }
        return false;
    }


    /****
     * Private display loop thread
     */
    private class GameLoop extends Thread {
        private long msPerFrame = 1000/30;	//Frame rate
        public boolean running = true;		//Control flag for start / stop mechanism
        private int fpsSamples[] = new int[50];
        private int samplePos = 0;
        private int samplesSum = 0;
        /****
         * run is the standard routine called, when a thread is started via the start() method
         */
        public void run() {
            Canvas canvas = null;
            long thisFrameTime;
            long lastFrameTime = System.currentTimeMillis();
            float framesSinceLastFrame;
            final SurfaceHolder surfaceHolder = BubblesView.this.surfaceHolder;

            while (running) {
                try {
                    canvas = surfaceHolder.lockCanvas();	//Get the canvas exclusively
                    if(canvas == null) continue;
                    synchronized (surfaceHolder) {			//Must be executed exclusively
                        drawScreen(canvas);					//Draw bubbles
                    }
                    thisFrameTime = System.currentTimeMillis();		//Calculate the exact no. of frames since last loop
                    framesSinceLastFrame = (float)(thisFrameTime - lastFrameTime)/msPerFrame;

                    float fps = 1000.0f / ((float)samplesSum / fpsSamples.length);
                    canvas.drawText(String.format("FPS: %f", fps), 10, 10, new Paint());

                    calculateDisplay(canvas, framesSinceLastFrame);	//update positions of bubbles
                    Log.d("test", Float.toString(framesSinceLastFrame));

                    thisFrameTime = System.currentTimeMillis();
                    int timeDelta = (int) (thisFrameTime - lastFrameTime);

                    samplesSum -= fpsSamples[samplePos];
                    fpsSamples[samplePos++] = timeDelta;
                    samplesSum += timeDelta;
                    samplePos %= fpsSamples.length;

                    lastFrameTime = thisFrameTime;

                    if(timeDelta<msPerFrame)
                        sleep(msPerFrame - timeDelta);

                }catch(InterruptedException e){

                }finally {
                    if (canvas != null)
                        surfaceHolder.unlockCanvasAndPost(canvas);
                }
            }
        }
        public  void startTimeThread() {
            if(runningTimeThread) return;
            runningTimeThread = true;
            resetElapsedTime();
            timeThread = new Thread(new Runnable() {
                public void run() {
                    while (runningTimeThread) {
                        increaseElapsedTime(0.05);

                        try {
                            Thread.sleep(10);
                        } catch (InterruptedException ex) {

                            runningTimeThread=false;
                        }
                    }
                }});
            timeThread.start();

        }

        public void stopTimeThread() {
            synchronized (this) {				//Must be executed exclusively
                if(timeThread != null) {
                    try {
                        timeThread.join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                timeThread = null;
            }
        }

    }


/****
 * Interfcae implementation
 */

    /****
     * Called when display is up
     */
    public void surfaceCreated(SurfaceHolder holder) {
        surfaceHolder = holder;
        synchronized (this) {				//Must be executed exclusively
            if (gameLoop == null) {
                gameLoop = new GameLoop();	//Start animation here
                gameLoop.start();
            }
        }
    }

    /****
     * Not used
     */
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        //Nothing to do
    }

    /****
     * Called before display will be brought down
     */
    public void surfaceDestroyed(SurfaceHolder holder) {
        synchronized (this) {				//Must be executed exclusively
            if(gameLoop != null) {
                gameLoop.running = false;
                try {
                    gameLoop.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            gameLoop = null;
        }
    }
}