package com.example.donald.doublingballs;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Matrix;
import android.support.v4.graphics.BitmapCompat;
import android.support.v4.view.MotionEventCompat;
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

/****
 * bubblesView: Manages display handling of app. Implements SurfaceHolder.Callback to access certain display properties
 */
public class BubblesView extends SurfaceView implements SurfaceHolder.Callback {


    private volatile boolean gamestart = false;

    // Sound
    private boolean playSound;

    //GameContent
    public int life = 3;  // Leben des Spielers
    public int ammo = 3; // Munition des Spielers
    public double difficulty_factor = 1; // Im Laufe des Spiels wird die Ballspawn-Rate erhöht.

    // Texte
    private double bonus_score = 0;

    // Zeit
    private Thread timeThread;
    private volatile boolean runningTimeThread=false;    // access to elementary data types (not double or long) are atomic and should be volatile to synchronize content
    private volatile double elapsedTime = 0.0;

    synchronized private void resetElapsedTime() { elapsedTime = 0.0;}
    synchronized private double getElapsedTime() { return elapsedTime; }
    synchronized private void increaseElapsedTime(double increment) { elapsedTime += increment; }



    private SurfaceHolder surfaceHolder = null; //Surface to hijack
    private GameLoop gameLoop; //Display refresh thread
    private LinkedList<Bubble> bubbles = new LinkedList<Bubble>(); //Our bubble objects
    private float BUBBLE_FREQUENCY = 0.3f; //Bubble generation rate
    //Certain paint properties and objects

    private Bitmap backgroundBitmap;
    private Bitmap bubbleBitmap;
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

    public HashSet<Shot> shots = new HashSet<>();
    private HashSet<BallObject> ballObjects = new HashSet<>();
    HashSet<Shot> shotsToBeRemoved = new HashSet<>();
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

    /****
     * Constructor
     * @param context
     * @param attrs
     */
    public BubblesView(Context context, AttributeSet attrs) {
        super(context, attrs);

        sound = new Sound(context);

        getHolder().addCallback((Callback) this);	//Register this class as callback handler for the surface
        backgroundBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.background2);
        bubbleBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.bubble);

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
        shooting[0] = BitmapFactory.decodeResource(context.getResources(), R.drawable.shoot1);
        shooting[1] = BitmapFactory.decodeResource(context.getResources(), R.drawable.shoot2);
        shooting[2] = BitmapFactory.decodeResource(context.getResources(), R.drawable.shoot3);
        shooting[3] = BitmapFactory.decodeResource(context.getResources(), R.drawable.shoot4);

        player = new Player(leftWalk, rightWalk, leftStandStill, rightStandStill, leftStartWalk, rightStartWalk, shooting, backgroundBitmap);

        shot = BitmapFactory.decodeResource(context.getResources(), R.drawable.shot1);

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


        // BallObjects
        mPaint = new Paint();
        mPaint.setARGB(0xFF, 0x00, 0x80, 0xFF);

        // StartBall

        ballObjects.add(new BallObject(100, 50.0, 10, 10, 0.8, 100, 0.025, BallTypes.LARGE, mPaint, this)); // medium Ball
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

        int activePointerID;
        int activePointerIndex;


        if(!gamestart) {
            gameLoop.startTimeThread(); // startet die Zeit nach dem ersten Button-Klick, if Abfrage sorgt dafür das nur ein Zeit-Thread existiert.
            gamestart = true;
        }

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

                    leftButtonHeldDown = true;

                } else if (buttonRight.contains(xPos, yPos)) {

                    player.setCurrentState(State.WALK_RIGHT);
                    player.setDirection(Direction.RIGHT);

                    rightButtonPointerID = activePointerID;
                    rightButtonHeldDown = true;

                } else if (buttonShoot.contains(xPos, yPos)) {

                    if (shots.toArray().length < 3) {

                        player.setCurrentState(State.SHOOT);
                        shots.add(new Shot(player.getxPos(), 0, shot, player));
                        sound.playLasergunSound();
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
                    leftButtonHeldDown = true;
                }
                else if (buttonRight.contains(xPosPD, yPosPD)) {

                    player.setCurrentState(State.WALK_RIGHT);
                    player.setDirection(Direction.RIGHT);

                    rightButtonPointerID = activePointerID;
                    //Log.d("test", "rightID:::activeID   "+Integer.toString(rightButtonPointerID)+":::"+Integer.toString(activePointerID));
                    rightButtonHeldDown = true;
                }
                else if (buttonShoot.contains(xPosPD, yPosPD)) {

                    if (shots.toArray().length < 3) {

                        player.setCurrentState(State.SHOOT);
                        shots.add(new Shot(player.getxPos(), 0, shot, player));
                        sound.playLasergunSound();
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


                    //leftButtonHeldDown = false;
                    //if (shootButtonHeldDown) player.setCurrentState(State.SHOOT);
                    //else if (rightButtonHeldDown) player.setCurrentState(State.WALK_RIGHT);
                    determineStateOnActionUp();

                } else if (rightButtonPointerID == activePointerID && !buttonRight.contains(currentXPos, currentYPos)) {

                    //rightButtonHeldDown = false;
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
                    leftButtonHeldDown = true;

                } else if (buttonRight.contains(currentXPos, currentYPos)) {

                    player.setCurrentState(State.WALK_RIGHT);
                    player.setDirection(Direction.RIGHT);

                    rightButtonPointerID = activePointerID;
                    rightButtonHeldDown = true;

                } /*else if (buttonShoot.contains(currentXPos, currentYPos)) {

                    if (shots.toArray().length < 3) {
                        //no new Shot added; Action_Move triggers to often, resulting in permanent shooting
                        player.setCurrentState(State.SHOOT);
                        //if (!shootButtonHeldDown) shots.add(new Shot(player.getxPos(), 0, shot, player)); //TODO schuss beim reindraggen

                        shootButtonPointerID = activePointerID;
                        shootButtonHeldDown = true;
                    }
                }*/
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

        }return true;
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
                break;
            case WALK_LEFT:
                player.setCurrentState(State.LEFT_STAND_STILL);
                leftButtonHeldDown = false;
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

        randomlyAddBallObjects(backgroundBitmap.getWidth(),backgroundBitmap.getHeight());

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
    }


    /****
     * calculateDisplay: Generates new bubble, moves bubble, removes unused bubbles
     * @param canvas: Canvas to calculate moves for
     * @param numberOfFrames: No. of frames since last call
     */
    private void calculateDisplay(Canvas canvas, float numberOfFrames) {

        player.update(canvas, numberOfFrames);

        for (BallObject ballObject : ballObjects){
            ballObject.update();
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

                    shotsToBeRemoved.add(shot);
                    ballObjectsToBeRemoved.add(ballObject); // wird 2 mal hinzugefügt? TODO

                    if (ballObject.ballTypes == BallTypes.LARGE){ // Large Balls
                        bonus_score += 100;

                        // Bei der Ballerzeugung könnte man ballObjects.getAccy + 30 für den Parameter accy eintragen
                        // damit man die Schusskraft mit der Schwerkraft des Balls verrechnet: Bewusst nicht gemacht, weil das Spiel sonst zu schwer wird für den Spieler
                        // Die Bälle sollen immer weit genug vom aktuellen Ball wegspringen!

                        ballObjectsToBeAdded.add(new BallObject(ballObject.getPosx(), ballObject.getPosy(), 10, 30.0 , 0.8, 50, 0.025, BallTypes.MEDIUM, mPaint, this)); // small Ball
                        ballObjectsToBeAdded.add(new BallObject(ballObject.getPosx(), ballObject.getPosy(), -10,30.0, 0.8, 50, 0.025, BallTypes.MEDIUM, mPaint, this)); // small Ball
                    }
                    if (ballObject.ballTypes == BallTypes.MEDIUM) { // Medium Balls
                        bonus_score += 50;
                        ballObjectsToBeAdded.add(new BallObject(ballObject.getPosx(),ballObject.getPosy(), 10, 30.0, 0.8, 25, 0.025, BallTypes.SMALL, mPaint, this)); // small Ball
                        ballObjectsToBeAdded.add(new BallObject(ballObject.getPosx(), ballObject.getPosy(), -10, 30.0, 0.8, 25, 0.025, BallTypes.SMALL, mPaint, this)); // small Ball
                    }
                    else{
                        // Small Balls
                        bonus_score += 20;
                    }
                }
            }
        }

        ballObjects.addAll(ballObjectsToBeAdded);

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

    }

    /****
     * randomlyAddBubbles: Adds a bubble at random. Probability rises with the number of frames passed
     * @param screenWidth ...
     * @param screenHeight ...
     */
    /*
    public void randomlyAddBubbles(int screenWidth, int screenHeight, float numFrames) {
        //Create a bubble every time the number of frame threshold is exceeded
        if (Math.random()>BUBBLE_FREQUENCY*numFrames)
            return;

        bubbles.add(new Bubble((int)(screenWidth*Math.random()), 				//x pos at random
                screenHeight+Bubble.RADIUS,						//y pos under bottom of screen
                (int)((Bubble.MAX_SPEED-0.1)*Math.random()+0.1),	//This avoids bubbles of speed 0
                bubbleBitmap));
    }
    */
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

        if ((int) (Math.random() * 1000) > difficulty_factor) // nur wenn die Zahl <= factor ist wird ein Ball gespawnt
            return;
        /*
        bubbles.add(new Bubble((int)(screenWidth*Math.random()), 				//x pos at random
                screenHeight+Bubble.RADIUS,						//y pos under bottom of screen
                (int)((Bubble.MAX_SPEED-0.1)*Math.random()+0.1),	//This avoids bubbles of speed 0
                bubbleBitmap));
                */

        double probability = Math.random();
        // Lässt Balle mit unterschiedlichen Wahrscheinlichkeiten (L = 20%, M = 30 %, S = 50% ) spawnen
        if (probability <= 0.1) {
            ballObjects.add(new BallObject(100, 50.0, 10, 15.0, 0.8, 100, 0.025, BallTypes.LARGE, mPaint, this)); // large Ball
        }
        else if (probability > 0.1 && probability < 0.3 ) {
            ballObjects.add(new BallObject(100, 50.0, 10, 13.0, 0.8, 50, 0.025, BallTypes.MEDIUM, mPaint, this)); // medium Ball

        }
        else if(probability >= 0.3 && probability <= 1){
            ballObjects.add(new BallObject(100, 50.0, 10, 10.0, 0.8, 25, 0.025, BallTypes.SMALL, mPaint, this)); // small Ball
        }

    }

    public boolean areColliding(BallObject b, Shot s) {

       /* float bX = b.getxPos();
        float bY = b.getyPos();
        float bR = b.getRadius();
        float sX = s.getxPos();
        float sY = s.getyPos();
        float sH = s.getShotHeigth();
        float sW = s.getShotWidth();

        float BallLeft = bX-bR;
        float BallRight = bX+bR;

        float BallTop = bY-bR;
        float BallBottom = bY+bR;

        float BallXHalfLeft = bX-0.5f*bR;
        float BallXHalfRight = bX+0.5f*bR;

        float BallYHalfLeftTop = bY-0.5f*bR;
        float BallYHalfLeftBottom = bY+0.5f*bR;

        float ShotLeft = sX-0.5f*sW;
        float ShotRight = sX+0.5f*sW;
        float ShotTop = sY-0.5f*sH;
        float ShotBottom = sY+0.5f*sH;

        if ((BallLeft == ShotRight || BallRight == ShotLeft) && bY == sY || //prüft ob der Ball links oder rechts getroffen wurde
             BallBottom == ShotTop && bX == sX ||                           //prüft ob der Ball unten getroffen wurde
                 (BallXHalfLeft == sX || BallXHalfRight == sX) && BallYHalfLeftBottom == ShotTop ||
                 ) {}*/

        if (b.rect.intersect(s.rect)){
            sound.playBlubbSound();
            return true;
        }
        return false;
        /*
        double dX = b.getPosx() - s.getxPos();
        double dY = b.getPosy() - s.getyPos();
        double distance = Math.sqrt(dX*dX + dY*dY);
        if (b.getPosy() >= s.getyPos()+s.getShotHeigth()/2) {
            if (distance < b.getRadius() + s.getShotHeigth() / 2) return true;
        }
        else if (distance <= b.getRadius() + s.getShotWidth()/2) return true;
        return false;*/
    }

    public boolean areColliding(BallObject b, Player p) {
        if(b.rect.intersect(p.rect)) {
            sound.playHitSound();
            return true;
        }
        return false;
        /*
        double dX = b.getPosx()-p.getxPos();
        double dY = b.getPosy()-p.getyPos();
        double distance = Math.sqrt(dX*dX + dY*dY);

        if (b.getPosy() >= p.getyPos()+p.getPlayerHeigth()/2) {
            if (distance <= b.getRadius()+p.getPlayerHeigth()/2) return true;
        }
        else if(distance <= b.getRadius()+p.getPlayerWidth()/2) return true;
        return false;*/
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
//			boolean retry = true;
//			if (gameLoop != null) {			//Stop the loop
//				gameLoop.running = false;
//				while (retry) {
//					try {
//						gameLoop.join();	//Catch the thread
//						retry = false;
//					} catch (InterruptedException e) {
//					}
//				}
//			}
            gameLoop = null;
        }
    }
}