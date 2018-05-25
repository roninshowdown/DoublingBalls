package com.example.donald.doublingballs;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.media.AudioManager;

enum BallType {
    SMALL,MEDIUM,LARGE;
}

public class Ball {

    private double height;
    private double width;
    public float posx;
    public float posy;
    private double accx;
    private double accy;
    public double friction;
    public int points;
    public BallType ballType;
    public GameView bw;

    private double bounce;
    //private double defbounce;

    public float radius;
    private Paint p;
    public RectF rect = new RectF();

    public Ball(int points, float posx, float posy, double accx, double accy,
                double bounce, float radius, double friction, BallType ballType, Paint p, GameView v) {
        this.ballType = ballType;
        this.posx = posx;
        this.posy = posy;
        this.accx = accx;
        this.accy = accy;
        this.bounce = bounce;
        //this.defbounce = bounce;
        this.radius = radius;
        this.friction = friction;
        this.p = p;
        this.points = points;
        bw = v;
        this.width = v.backgroundBitmap.getWidth();
        this.height = v.backgroundBitmap.getHeight();

        //audioManager = (AudioManager) v.getContext().getSystemService(Context.AUDIO_SERVICE);

    }

    public void update() {

        if (bounce != 1) {
            posy -= accy;
            accy -= 1.5;      // Geschwindigkeit des Balls in Y Richtung (Schwerkraft)
            posx += accx;

            if (accx > 0)
                accx -= friction; // Friction bestimmt wie schnell der Ball sich nach X-Richtung bewegt
            if (accx < 0)
                accx += friction;

            if (posy >= ((height * 0.74) - radius)) { // prüft ob Ball am Boden ist
                bw.sound.playBounceSound();
                accy = (Math.abs(accy) * bounce);
                if (bounce > 0 && bounce != 1)
                    bounce -= 0.01;
                if (bounce == 0.78) bounce = 1;
            }


            if (posy <= (radius)) { // prüft ob Ball an der Decke ist und wechselt Richtung !!!
                accy = 0 - Math.abs(accy) * bounce;
            }

            if (posx >= (width - radius)) { // prüft ob Ball rechts an der Wand ist
                bw.sound.playBounceSound();
                accx = 0 - Math.abs(accx) * bounce;
            }

            if (posx <= (radius)) { // prüft ob Ball links an der Wand ist
                accx = Math.abs(accx) * bounce;
            }
        }
        else{
            friction = 0; // keine Reibung mehr
            posy -= accy;
            accy -= 1.0;
            if (accy >= 30){
                accy -= 1.0;
            }
            posx += accx;

            if (posy >= ((height * 0.74) - radius)) { // prüft ob Ball am Boden ist
                bw.sound.playBounceSound();
                accy = (Math.abs(accy) * bounce);
            }

            if (posy <= (radius)) { // prüft ob Ball an der Decke ist
                bw.sound.playBounceSound();
                accy = 0 - Math.abs(accy) * bounce;
            }

            if (posx >= (width - radius)) { // prüft ob Ball rechts an der Wand ist
                bw.sound.playBounceSound();
                accx = 0 - Math.abs(accx) * bounce;
            }

            if (posx <= (radius)) { // prüft ob Ball links an der Wand ist
                bw.sound.playBounceSound();
                accx = Math.abs(accx) * bounce;
            }
        }
        if (ballType == BallType.LARGE) rect.set((float)posx-radius*0.75f, (float)posy-radius*0.75f, (float)posx+radius*0.75f, (float)posy+radius*0.75f);
        else rect.set((float)posx-radius, (float)posy-radius, (float)posx+radius, (float)posy+radius);


    }



    public void draw(Canvas c) {

        //height = c.getClipBounds().height();
        //width = c.getClipBounds().width();
        //c.drawRect(rect, new Paint());
        c.drawCircle( posx, posy, radius, p);

    }

/*
    public Ball(double posx, double posy, BallType ballType, BubblesView v) {
        bw = v;
        this.accx = 10;
        this.friction = 0.025;
        this.bounce = 0.8;
        this.posx = 100;
        this.posy = 50;
        this.ballType = ballType;
        if (ballType == BallType.LARGE){
            this.ballType = BallType.LARGE;
            this.points = 100;
            this.accy = 15;
            this.radius = 100;
            Paint p = new Paint();
            p.setARGB(0xFF, 0xFF, 0x00, 0x00); // rot
            this.p = p;
        }
        else if (ballType == BallType.MEDIUM){
            this.ballType = BallType.MEDIUM;
            this.points = 50;
            this.accy = 13;
            this.radius = 50;
            Paint p = new Paint();
            p.setARGB(0xFF, 0xFF, 0xFF, 0x00); // gelb
            this.p = p;
        }
        else if (ballType == BallType.SMALL){
            this.ballType = BallType.SMALL;
            this.points = 20;
            this.accy = 10;
            this.radius = 25;
            Paint p = new Paint();
            p.setARGB(0xFF, 0x00, 0xFF, 0x00); // grün
            this.p = p;
        }
    }
    */

    public double getPosx() {
        return posx;
    }

    public double getPosy() {
        return posy;
    }

    public double getAccx() {
        return accx;
    }

    public double getAccy() {
        return accy;
    }

    public double getBounce() {
        return bounce;
    }


    public Paint getP() {
        return p;
    }


    public void setAccx(double accx) {
        this.accx = accx;
    }

    public void setAccy(double accy) {
        this.accy = accy;
    }

    public void setBounce(double bounce) {
        this.bounce = bounce;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public void setP(Paint p) {
        this.p = p;
    }


}