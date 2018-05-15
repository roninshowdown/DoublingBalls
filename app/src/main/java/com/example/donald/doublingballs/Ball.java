package com.example.donald.doublingballs;


import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;

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
        dX = 2.5f;
        dY = 5;
    }

    public void draw(Canvas canvas) {
        canvas.drawCircle(xPos, yPos, radius, paint);
    }

    public void update(Canvas c, float numberOfFrames) {

        /*if (xPos >= (c.getWidth()-radius)) {
            xPos = (c.getWidth()-radius);
            dX = -dX;
        }
        else if (xPos <= radius) {
            xPos = radius;
            dX = -dX;
        }

        if (yPos >= (c.getHeight()*0.75f-radius)) {
            yPos = c.getHeight()*0.75f-radius;
            dY = -dY;
        }
        else if (yPos <= radius) {
            yPos = radius;
            dY = -dY;
        }
        xPos += dX*numberOfFrames;
        yPos += dY*numberOfFrames;*/

        yPos -= dY;
        dY -= 0.5;
        xPos += dX;

        if (xPos > 0);
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