package com.example.donald.doublingballs;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.RectF;

import com.example.donald.doublingballs.activities.ShopActivity;

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
    private RectF rectBitmap = new RectF();

    public Shot(float xPos, float yPos, Bitmap image, Player player) {
        this.xPos = xPos;
        this.yPos = yPos;
        this.image = image;
        shotHeigth = image.getHeight();
        shotWidth = image.getWidth();
        this.player = player;
        speed = Constants.SCREEN_HEIGHT / 120f;
    }

    public void update(Canvas c, float numberOfFrames){
        float movedDistance = speed * numberOfFrames;
        if(newlyInstantiated) {
            yPos = c.getHeight()-player.getPlayerHeigth()*1.7f;
            newlyInstantiated = false;
        }
        yPos -= movedDistance;

        if (ShopActivity.improvedShot){
            rect.set(xPos-shotWidth,  yPos-shotHeigth*1.3f, xPos+shotWidth, yPos+shotHeigth*0.7f);
            rectBitmap.set(xPos-shotWidth,  yPos-shotHeigth*1.3f, xPos+shotWidth, yPos+shotHeigth*0.7f);
        }
        else {
            rect.set(xPos-shotWidth/2,  yPos-shotHeigth/4, xPos+shotWidth/2, yPos+shotHeigth/4);
            rectBitmap.set(xPos-shotWidth/2,  yPos-shotHeigth/2, xPos+shotWidth/2, yPos+shotHeigth/2);
        }
    }
    public void draw(Canvas canvas) {

        //canvas.drawRect(rect, new Paint());
        canvas.drawBitmap(image, null, rectBitmap, null); //TODO ANIMATIONSSQUENZ EINBAUEN
    }

    public boolean outOfRange() { return (yPos-shotHeigth/3<0); }

}