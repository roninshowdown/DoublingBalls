package com.example.donald.doublingballs;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
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
    RectF rect = new RectF();
    RectF rectBitmap = new RectF();

    public Shot(float xPos, float yPos, Bitmap image, Player player) {
        this.xPos = xPos;
        this.yPos = yPos;
        this.image = image;
        shotHeigth = image.getHeight();
        shotWidth = image.getWidth();
        this.player = player;
        speed = 12f;
    }

    public void update(Canvas c, float numberOfFrames){
        float movedDistance = speed * numberOfFrames;
        if(newlyInstantiated) {
            yPos = c.getHeight()-player.getPlayerHeigth()*1.7f;
            newlyInstantiated = false;
        }
        yPos -= movedDistance;

        rect.set(xPos-shotWidth/2,  yPos-shotHeigth/4, xPos+shotWidth/2, yPos+shotHeigth/4);
        rectBitmap.set(xPos-shotWidth/2,  yPos-shotHeigth/2, xPos+shotWidth/2, yPos+shotHeigth/2);
    }
    public void draw(Canvas canvas) {

        //canvas.drawRect(rect, new Paint());
        canvas.drawBitmap(image, null, rectBitmap, null); //TODO ANIMATIONSSQUENZ EINBAUEN
    }

    public boolean outOfRange(Canvas canvas) { return (yPos-shotHeigth/3<0); }

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

    public float getShotWidth() {
        return shotWidth;
    }

    public void setShotWidth(float shotWidth) {
        this.shotWidth = shotWidth;
    }

    public float getShotHeigth() {
        return shotHeigth;
    }

    public void setShotHeigth(float shotHeigth) {
        this.shotHeigth = shotHeigth;
    }
}