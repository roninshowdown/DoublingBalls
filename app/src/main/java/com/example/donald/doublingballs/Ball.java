package com.example.donald.doublingballs;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

enum BallType {
    SMALL,MEDIUM,LARGE
}

public class Ball {

    private double height;
    private double width;
    public float posx;
    public float posy;
    private double accx;
    private double accy;
    private double friction;
    public int points;
    public BallType ballType;
    public GameView bw;
    private double bounce;

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
        this.radius = radius;
        this.friction = friction;
        this.p = p;
        this.points = points;
        bw = v;
        this.width = v.backgroundBitmap.getWidth();
        this.height = v.backgroundBitmap.getHeight();
    }

    public void update() {

        if (bounce != 1) {
            posy -= accy;
            accy -= height/960;      // Geschwindigkeit des Balls in Y Richtung (Schwerkraft) scaled
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
            accy -= height/1440;
            if (accy >= height/48){
                accy -= height/1440;
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
        if (ballType == BallType.LARGE) rect.set(posx-radius*0.75f, posy-radius*0.75f, posx+radius*0.75f, posy+radius*0.75f);
        else rect.set(posx-radius, posy-radius, posx+radius, posy+radius);


    }



    public void draw(Canvas c) {
        c.drawCircle( posx, posy, radius, p);

    }



    public Paint getP() {
        return p;
    }

    public void setP(Paint p) {
        this.p = p;
    }


}