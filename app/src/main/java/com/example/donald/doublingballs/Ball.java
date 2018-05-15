package com.example.donald.doublingballs;


import android.graphics.Canvas;
import android.graphics.Paint;

public class Ball {

    private float xPos;
    private float yPos;
    private float dX;
    private float dY;

    private int radius;
    private Paint paint;


    Ball(float xPos, float yPos, int radius, Paint paint) {
        this.xPos = xPos;
        this.yPos = yPos;
        this.radius = radius;
        this.paint = paint;
        dX = 5;
        dY = 10;
    }

    public void draw(Canvas canvas) {
        canvas.drawCircle(xPos, yPos, radius, paint);
    }

    public void update(Canvas c, float numberOfFrames) {
        xPos += dX*numberOfFrames;
        yPos += dY*numberOfFrames;

        if (xPos >= (c.getWidth()-radius)) {
            xPos = (c.getWidth()-radius);
            dX = -dX;
        }
        if (xPos <= radius) {
            xPos = radius;
            dX = -dX;
        }

        if (yPos >= (c.getHeight()-radius)) {
            yPos = c.getHeight()-radius;
            dY = -dY;
        }
        if (yPos <= radius)
            yPos = radius;
            dY = -dY;
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

}