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

    public Shot(float xPos, float yPos, Bitmap image, Player player) {
        this.xPos = xPos;
        this.yPos = yPos;
        this.image = image;
        shotHeigth = image.getHeight();
        shotWidth = image.getWidth();
        this.player = player;
        speed = 7f;
    }

    public void update(Canvas c, float numberOfframes){
        float movedDistance = speed * numberOfframes;
        yPos += movedDistance;
    }
    //TODO FLUGRICHTUNG/SPAWN VON SHOTS
    public void draw(Canvas canvas) {
        RectF rect = new RectF(xPos-shotWidth/2, canvas.getHeight()+yPos+shotHeigth/2, player.getxPos()+shotWidth/2, canvas.getHeight()+yPos);
        canvas.drawBitmap(image, null, rect, null);
    }

    public boolean outOfRange() {
        return yPos <0;
    }
}