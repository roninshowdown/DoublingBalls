package com.example.donald.doublingballs;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;


class Bubble {
    private float x, y, speed;
    private Bitmap bubbleBitmap;
    public static final int RADIUS = 10;
    public static final int MAX_SPEED = 10;
    public static final int MIN_SPEED = 1;
    private float amountOfWobble = 0;
    private int test;

    private static final float DOUBLE_PI = (float)(2*3.141529);
    public static final float WOBBLE_RATE = (float)8*DOUBLE_PI;
    public static final float WOBBLE_AMOUNT = (float)0.05;
    private float amountOfShift = 0;
    public static final float SHIFT_RATE = (float)50*DOUBLE_PI;
    public static final float SHIFT_AMOUNT = (float)2;

    public Bubble (float x, float y, float speed, Bitmap bubbleBitmap) {
        this.x = x;
        this.y = y;
        this.speed = Math.max(speed, MIN_SPEED);
        this.bubbleBitmap = bubbleBitmap;
    }

    public void draw(Canvas c) {
        float xDeformation = RADIUS*WOBBLE_AMOUNT*amountOfWobble;
        float yDeformation = RADIUS*WOBBLE_AMOUNT*(1-amountOfWobble);
        float shift = SHIFT_AMOUNT*amountOfShift;

        Rect nullRect = null;
        Paint nullPaint = null;
        c.drawBitmap(bubbleBitmap,
                nullRect,
                new RectF(x-(RADIUS+xDeformation)+shift,
                        y-(RADIUS+yDeformation),
                        x+(RADIUS+xDeformation)+shift,
                        y+(RADIUS+yDeformation)),
                nullPaint);
    }

    public void move(Canvas c, float numberOfFrames) {
        float movedDistance = speed*numberOfFrames;		//Now movement depends on time between frames
        y -= movedDistance;
        amountOfWobble = (float)Math.cos ((y/c.getHeight())*WOBBLE_RATE);
        amountOfShift = (float)Math.sin ((y/c.getHeight())*SHIFT_RATE*movedDistance/MAX_SPEED);
    }

    public boolean outOfRange() {
        return (y+RADIUS < 0);
    }
}