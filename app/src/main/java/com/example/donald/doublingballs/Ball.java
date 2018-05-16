package com.example.donald.doublingballs;


import android.graphics.Canvas;
import android.graphics.Paint;

public class Ball {

    private float xPos;
    private float yPos;
    private float accX;
    private float accY;
    private float friction = 0.1f;
    private float bounce = 0.8f;

    private int radius;
    private Paint paint;


    Ball(float xPos, float yPos, int radius, Paint paint) {
        this.xPos = xPos;
        this.yPos = yPos;
        this.radius = radius;
        this.paint = paint;
        accX = 2.5f;
        accY = 5;
    }

    public void draw(Canvas canvas) {
        canvas.drawCircle(xPos, yPos, radius, paint);
    }

    public void update(Canvas c, float numberOfFrames) {

        if (accX > 1) accX -= friction;
        if (accY > 3) accY -= bounce;

        if (xPos >= (c.getWidth()-radius)) {
            xPos = (c.getWidth()-radius);
            accX = -accX;
        }
        else if (xPos <= radius) {
            xPos = radius;
            accX = -accX;
        }

        if (yPos >= (c.getHeight()*0.75f-radius)) {
            yPos = c.getHeight()*0.75f-radius;
            accY = -accY;

        }
        else if (yPos <= radius) {
            yPos = radius;
            accY = -accY;
        }
        xPos += accX*numberOfFrames;
        yPos += accY*numberOfFrames;

    }

    /*public boolean checkCollision() {
        if ((Panel.yArrow <= yPos)
                && (((xPos - radius) == Panel.xTouch) || (xPos + radius) == Panel.xTouch)
                || (Math.sqrt((Math.pow((xPos - Panel.xTouch), 2))
                + Math.pow((yPos - Panel.yArrow), 2)) < radius)) {
            GameScreen.vibrator.vibrate(100);
            collided = true;
            Panel.yArrow = -1;
            return collided;

        }
        return false;
    }*/

    public float getxPos() {
        return xPos;
    }

    public void setxPos(float xPos) {
        this.xPos = xPos;
    }

    public float getyPos() {
        return yPos;
    }

    public void setyPos(float yPos) {
        this.yPos = yPos;
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }
}