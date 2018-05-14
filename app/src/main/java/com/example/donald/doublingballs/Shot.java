package com.example.donald.doublingballs;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.RectF;

public class Shot {
    private float xPos;
    private float yPos;
    private float speed;
    private float shotWidth;
    private float shotHeigth;
    private Player player;
    private Bitmap image;
    private boolean newlyInstantiated = true;

    public Shot(float xPos, float yPos, Bitmap image, Player player) {
        this.xPos = xPos;
        this.yPos = yPos;
        this.image = image;
        shotHeigth = image.getHeight();
        shotWidth = image.getWidth();
        this.player = player;
        speed = 7f;
    }

    public void update(Canvas c, float numberOfFrames){
        float movedDistance = speed * numberOfFrames;
        if(newlyInstantiated) {
            yPos = c.getHeight()-player.getPlayerHeigth()*1.7f;
            newlyInstantiated = false;
        }
        yPos -= movedDistance;
    }
    public void draw(Canvas canvas) {
        RectF rect = new RectF(xPos-shotWidth/2,  yPos-shotHeigth,
                                xPos+shotWidth/2, yPos);
        canvas.drawBitmap(image, null, rect, null);
    }

    public boolean outOfRange(Canvas canvas) { return (yPos-shotHeigth<canvas.getHeight()/8); }

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
}