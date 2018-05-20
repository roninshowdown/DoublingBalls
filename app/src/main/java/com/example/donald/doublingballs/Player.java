package com.example.donald.doublingballs;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.Log;

enum State {
    LEFT_STAND_STILL, RIGHT_STAND_STILL, LEFT_START_WALK, RIGHT_START_WALK, WALK_LEFT, WALK_RIGHT, SHOOT, DIE
}

enum Direction {
    LEFT, RIGHT
}

public class Player {

    private float xPos;
    private float yPos;
    private float speed = 20f; // TODO Turbotreter

    private Direction direction = Direction.RIGHT;
    private State currentState = State.RIGHT_STAND_STILL;

    private int pictureCount;

    private float playerWidth;
    private float playerHeigth;

    private Bitmap[] leftWalk;
    private Bitmap[] rightWalk;
    private Bitmap[] shooting;
    private Bitmap[] leftDeath;
    private Bitmap[] rightDeath;


    private Bitmap leftStandStill;
    private Bitmap rightStandStill;

    private Bitmap leftStartWalk;
    private Bitmap rightStartWalk;

    public RectF rect = new RectF();
    public RectF rectBitmap = new RectF();

    public Player(Bitmap[] leftWalk, Bitmap[] rightWalk,
                  Bitmap leftStandStill, Bitmap rightStandStill, Bitmap leftStartWalk, Bitmap rightStartWalk, Bitmap[] shooting,
                  Bitmap[] leftDeath, Bitmap[] rightDeath) {
        this.xPos = scaling.getWidth()/3;
        this.yPos = scaling.getHeight() * 283/400;        this.leftWalk = leftWalk;
        this.rightWalk = rightWalk;
        this.leftStandStill = leftStandStill;
        this.rightStandStill = rightStandStill;
        this.leftStartWalk = leftStartWalk;
        this.rightStartWalk = rightStartWalk;
        this.shooting = shooting;
        this.leftDeath = leftDeath;
        this.rightDeath = rightDeath;

        playerWidth = leftStandStill.getWidth();
        playerHeigth = leftStandStill.getHeight();
        Log.d("test", "PLAYER KONSTRUKTOR");

        pictureCount = 0;
    }
    /*
    public void determineStateOnMove() {
        if (direction == Direction.LEFT) {
            switch (getCurrentState()) {
                case RIGHT_STAND_STILL: setCurrentState(State.LEFT_START_WALK);
                    break;
                case LEFT_STAND_STILL:  setCurrentState(State.LEFT_START_WALK);
                    break;
                case RIGHT_START_WALK:  setCurrentState(State.LEFT_START_WALK);
                    break;
                case LEFT_START_WALK:   setCurrentState(State.WALK_LEFT);
                    break;
                case WALK_RIGHT:        setCurrentState(State.LEFT_START_WALK);
                    break;
                case WALK_LEFT:         setCurrentState(State.WALK_LEFT);
                    break;
                case SHOOT:             setCurrentState(State.LEFT_START_WALK);
                    break;
            }
        }
        else if (direction == Direction.RIGHT) {
            switch(getCurrentState()) {
                case RIGHT_STAND_STILL: setCurrentState(State.RIGHT_START_WALK);
                    break;
                case LEFT_STAND_STILL:  setCurrentState(State.RIGHT_START_WALK);
                    break;
                case RIGHT_START_WALK: 	setCurrentState(State.WALK_RIGHT);
                    break;
                case LEFT_START_WALK: 	setCurrentState(State.RIGHT_START_WALK);
                    break;
                case WALK_RIGHT:		setCurrentState(State.WALK_RIGHT);
                    break;
                case WALK_LEFT:			setCurrentState(State.RIGHT_START_WALK);
                    break;
                case SHOOT:				setCurrentState(State.RIGHT_START_WALK);
                    break;
            }
        }
    }
    */

    public void update(Canvas c, float numberOfFrames)  {
        //determineStateOnMove();
        float movedDistance = speed * numberOfFrames;
        if(currentState == State.WALK_LEFT || currentState == State.LEFT_START_WALK) {
            xPos -= movedDistance;
            if (xPos - playerHeigth/4 < 0) { // Walking against a wall
                xPos = playerHeigth/4;
            }
        }
        else if (currentState == State.WALK_RIGHT || currentState == State.RIGHT_START_WALK) {
            xPos += movedDistance;
            if (xPos + playerWidth/4 > c.getWidth()) {
                xPos = c.getWidth()-playerWidth/4;
            }
        }
        rect.set(xPos-playerWidth/6, yPos-playerHeigth*1.9f, xPos+playerWidth/6, yPos-playerHeigth*1.1f);
        rectBitmap.set(xPos-playerWidth/2, yPos-playerHeigth*2, xPos+playerWidth/2, yPos-playerHeigth);
    }

    public void draw(Canvas canvas) {

        canvas.drawRect(rect, new Paint());

        switch(currentState) {
            case LEFT_STAND_STILL:  canvas.drawBitmap(leftStandStill, null, rectBitmap, null);
                                    break;

            case LEFT_START_WALK:   canvas.drawBitmap(leftStartWalk, null, rectBitmap, null);
                                    break;

            case WALK_LEFT:         canvas.drawBitmap(leftWalk[pictureCount], null, rectBitmap, null);
                                    ++pictureCount;
                                    pictureCount %= 10;
                                    break;

            case RIGHT_STAND_STILL: canvas.drawBitmap(rightStandStill, null, rectBitmap, null);
                                    break;

            case RIGHT_START_WALK:  canvas.drawBitmap(rightStartWalk, null, rectBitmap, null);
                                    break;

            case WALK_RIGHT:        canvas.drawBitmap(rightWalk[pictureCount], null, rectBitmap, null);
                                    ++pictureCount;
                                    pictureCount %= 10;
                                    break;
            case SHOOT:             canvas.drawBitmap(shooting[3], null, new RectF(xPos-playerWidth/2, yPos-playerHeigth*2, xPos+playerWidth/2, yPos-playerHeigth - yPos/ 50), null); //TODO ANIMATION FIXEN

                                    break;

            case DIE:
                switch (direction) {
                    case LEFT:
                        canvas.drawBitmap(leftDeath[pictureCount], null, rectBitmap, null);
                        if (pictureCount < 6) pictureCount++;
                        break;

                    case RIGHT:
                        canvas.drawBitmap(rightDeath[pictureCount], null, rectBitmap, null);
                        if (pictureCount < 6) pictureCount++;
                        break;
                }
                break;

        }
    }

    public float getxPos() {
        return xPos;
    }

    public void setxPos(int xPos) {
        this.xPos = xPos;
    }

    public float getyPos() {
        return yPos;
    }

    public void setyPos(int yPos) {
        this.yPos = yPos;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public State getCurrentState() {
        return currentState;
    }

    public void setCurrentState(State currentState) {
        this.currentState = currentState;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public int getPictureCount() {
        return pictureCount;
    }

    public void setPictureCount(int pictureCount) {
        this.pictureCount = pictureCount;
    }

    public float getPlayerWidth() {
        return playerWidth;
    }

    public void setPlayerWidth(int playerWidth) {
        this.playerWidth = playerWidth;
    }

    public float getPlayerHeigth() {
        return playerHeigth;
    }

    public void setPlayerHeigth(int playerHeigth) {
        this.playerHeigth = playerHeigth;
    }
}