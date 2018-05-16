package com.example.donald.doublingballs;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import android.content.Context;
import android.graphics.Matrix;
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

    private SurfaceHolder surfaceHolder = null; //Surface to hijack
    private GameLoop gameLoop; //Display refresh thread
    private LinkedList<Bubble> bubbles = new LinkedList<Bubble>(); //Our bubble objects
    private float BUBBLE_FREQUENCY = 0.3f; //Bubble generation rate
    //Certain paint properties and objects
    private Bitmap backgroundBitmap;
    private Bitmap bubbleBitmap;
    private Bitmap shot;
    private Bitmap buttonLeftImage;
    private Bitmap buttonRightImage;
    private Bitmap buttonShootImage;

    private ArrayList<Shot> shots = new ArrayList<Shot>();
    ArrayList<Shot> shotsToBeRemoved = new ArrayList<>();

    private Player player;
    //Donald Ball
    private Ball ball;

    private Paint mPaint;
    private BallObject ballObject;
    private ArrayList<Ball> balls = new ArrayList<>();

    Rect buttonLeft;
    Rect buttonRight;
    Rect buttonShoot;

    private boolean drawBall = true;

    /****
     * Constructor
     * @param context
     * @param attrs
     */
    public BubblesView(Context context, AttributeSet attrs) {
        super(context, attrs);
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

        player = new Player(leftWalk, rightWalk, leftStandStill, rightStandStill, leftStartWalk, rightStartWalk, shooting);
        mPaint = new Paint();
        mPaint.setARGB(0xFF, 0x00, 0x80, 0xFF);
        ball = new Ball(100, 50, 50,mPaint);

        // Ball


        ballObject = new BallObject(100.0, 50.0, 10.0, 25.0, 0.8, 30, mPaint, this);

        shot = BitmapFactory.decodeResource(context.getResources(), R.drawable.shot1);

        buttonLeftImage = BitmapFactory.decodeResource(context.getResources(), R.drawable.leftbutton);
        buttonRightImage = BitmapFactory.decodeResource(context.getResources(), R.drawable.rightbutton);
        buttonShootImage = BitmapFactory.decodeResource(context.getResources(), R.drawable.weapon);
    }

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
        else if (event.getAction() == MotionEvent.ACTION_UP) { // multitouch beachten
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

    /****
     * drawScreen: Paints background and all bubbles
     * @param c: Canvas to be drawn on
     */
    private void drawScreen(Canvas c) {
        //float aspect = (float)c.getHeight() / c.getWidth();
        //Rect srcRect = new Rect(0, (int) (backgroundBitmap.getHeight() - backgroundBitmap.getWidth()*aspect), backgroundBitmap.getWidth(), backgroundBitmap.getHeight());
        backgroundBitmap = Bitmap.createScaledBitmap(backgroundBitmap, c.getWidth(), c.getHeight(), true);
        c.drawBitmap(backgroundBitmap, new Matrix(), null);
        //c.drawBitmap(backgroundBitmap, srcRect, new Rect(0, 0, Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT), null);
		/*for (Bubble bubble : bubbles) {									//Draw bubbles
			bubble.draw(c);
		}*/
        player.draw(c);
        //ballObject.draw(c);
        if (drawBall) ball.draw(c);

        for (Shot shot : shots) {
            shot.draw(c);
        }


        buttonLeft = new Rect(50, c.getHeight()-270, 350, c.getHeight() - 20);
        buttonRight = new Rect( 400,c.getHeight()-270, 700, c.getHeight() - 20);
        buttonShoot = new Rect(c.getWidth()-400, c.getHeight()-270, c.getWidth()-100, c.getHeight()-50);
        /*
        c.drawRect(buttonLeft, paint);
        c.drawRect(buttonRight, paint);
        c.drawRect(buttonShoot, paint);
        */
        c.drawBitmap(buttonLeftImage, null, buttonLeft, null);
        c.drawBitmap(buttonRightImage, null, buttonRight, null);
        c.drawBitmap(buttonShootImage, null, buttonShoot, null);
    }

    /****
     * calculateDisplay: Generates new bubble, moves bubble, removes unused bubbles
     * @param canvas: Canvas to calculate moves for
     * @param numberOfFrames: No. of frames since last call
     */
    private void calculateDisplay(Canvas canvas, float numberOfFrames) {
        //randomlyAddBubbles(canvas.getWidth(), canvas.getHeight(), numberOfFrames);		//Add a new bubble
        //LinkedList<Bubble> bubblesToRemove = new LinkedList<Bubble>();	//Move all bubbles
		/*for (Bubble bubble : bubbles) {
			bubble.move(canvas, numberOfFrames);
			if (bubble.outOfRange())									//and keep display leavers in mind
				bubblesToRemove.add(bubble);
		}

		for (Bubble bubble : bubblesToRemove) {							//Remove all bubbled up
			bubbles.remove(bubble);
		}*/
        //ballObject.update();
        player.update(canvas, numberOfFrames);
        ball.update(canvas, numberOfFrames);

        for (Shot shot : shots) {
            shot.update(canvas, numberOfFrames);
            if(shot.outOfRange(canvas) || areColliding(ball, shot)) {
                shotsToBeRemoved.add(shot);
                drawBall = false;
            }
        }
        for (Shot shot : shotsToBeRemoved) {
            shots.remove(shot);
        }
        //shots.removeAll(shotsToBeRemoved);
        if (areColliding(ball, player)) player.setCurrentState(State.LEFT_STAND_STILL);
    }

    /****
     * randomlyAddBubbles: Adds a bubble at random. Probability rises with the number of frames passed
     * @param screenWidth ...
     * @param screenHeight ...
     * @param numFrames: No. of frames since last call
     */
    public void randomlyAddBubbles(int screenWidth, int screenHeight, float numFrames) {
        //Create a bubble every time the number of frame threshold is exceeded
        if (Math.random()>BUBBLE_FREQUENCY*numFrames)
            return;

        bubbles.add(new Bubble((int)(screenWidth*Math.random()), 				//x pos at random
                screenHeight+Bubble.RADIUS,						//y pos under bottom of screen
                (int)((Bubble.MAX_SPEED-0.1)*Math.random()+0.1),	//This avoids bubbles of speed 0
                bubbleBitmap));
    }

    /*auf Kollision prüfen
    *Alle Ball-Koordinaten mit jedem Schuss und dem Player abgleichen
    * falls hit bei B-S = beide removen
    * falls hit bei B-P = Ball springt weiter, Player verliert leben
    * Besonderheiten
    *  - Ball Radius beachten
    *  - Schuss alle seiten wichtig
    *  */

    public boolean areColliding(Ball b, Shot s) {

        float bX = b.getxPos();
        float bY = b.getyPos();
        float bR = b.getRadius();
        float sX = s.getxPos();
        float sY = s.getyPos();
        float sH = s.getShotHeigth();
        float sW = s.getShotWidth();

        /*float BallLeft = bX-bR;
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

        float dX = bX - sX;
        float dY = bY - sY;
        double distance = Math.sqrt(dX*dX + dY*dY);
        if (b.getyPos() >= s.getyPos()+s.getShotHeigth()/2) {
            if (distance < bR + sH / 2) return true;
        }
        else if (distance <= b.getRadius() + s.getShotWidth()/2) return true;
        return false;
    }

    public boolean areColliding(Ball b, Player p) {
        float dX = b.getxPos()-p.getxPos();
        float dY = b.getyPos()-p.getyPos();
        double distance = Math.sqrt(dX*dX + dY*dY);

        if (b.getyPos() >= p.getyPos()+p.getPlayerHeigth()/2) {
            if (distance <= b.getRadius()+p.getPlayerHeigth()/2) return true;
        }
        else if(distance <= b.getRadius()+p.getPlayerWidth()/2) return true;
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