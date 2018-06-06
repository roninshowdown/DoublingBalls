package com.example.donald.doublingballs;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.RectF;

import com.example.donald.doublingballs.activities.ShopActivity;

enum State {
    LEFT_STAND_STILL, RIGHT_STAND_STILL, LEFT_START_WALK, RIGHT_START_WALK, WALK_LEFT, WALK_RIGHT, SHOOT, DIE
}

enum Direction {
    LEFT, RIGHT
}

public class Player {

    private float xPos;
    private float yPos;
    private float speed;

    private Direction direction = Direction.RIGHT;
    private State currentState = State.RIGHT_STAND_STILL;

    private int pictureCount = 0;
    private int dieCounter = 0;

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
                  Bitmap leftStandStill, Bitmap rightStandStill, Bitmap leftStartWalk, Bitmap rightStartWalk, Bitmap[] shooting, Bitmap[] leftDeath, Bitmap[] rightDeath) {
        this.xPos = Constants.SCREEN_WIDTH / 2;
        this.yPos = Constants.SCREEN_HEIGHT;
        this.leftWalk = leftWalk;
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

        if (ShopActivity.speed) speed = Constants.SCREEN_HEIGHT / 36f;
        else speed = Constants.SCREEN_HEIGHT / 72f;
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
            if (xPos - playerWidth/4 < 0) { // Walking against a wall
                xPos = playerWidth/4;
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

        //canvas.drawRect(rect, new Paint());
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
            case SHOOT:             canvas.drawBitmap(shooting[3], null, new RectF(xPos-playerWidth/2, yPos-playerHeigth*2, xPos+playerWidth/2, yPos-playerHeigth - yPos/ 50), null); //TODO ANIMATIONSEQUENZ EINBAUEN

                                    break;

            case DIE:
                dieCounter %= 7;
                switch (direction) {
                    case LEFT:
                        canvas.drawBitmap(leftDeath[dieCounter], null, rectBitmap, null);
                        if (dieCounter < 6) dieCounter++;
                        break;

                    case RIGHT:
                        canvas.drawBitmap(rightDeath[dieCounter], null, rectBitmap, null);
                        if (dieCounter < 6) dieCounter++;
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