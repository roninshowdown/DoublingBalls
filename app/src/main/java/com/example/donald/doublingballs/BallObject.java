package com.example.donald.doublingballs;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.media.AudioManager;
import android.media.SoundPool;
import android.view.MotionEvent;
import android.view.View;

public class BallObject {

    private double height;
    private double width;
    private double posx;
    private double posy;
    private double accx;
    private double accy;
    private double friction;

    private SoundPool sounds;
    private int bounceSound;
    private int touchSound;

    private double bounce;
    private double defbounce;

    AudioManager audioManager;
    float currentVolume;

    private int radius;
    private Paint p;

    public BallObject(double posx, double posy, double accx, double accy,
                      double bounce, int radius,double friction, Paint p, View v) {
        this.posx = posx;
        this.posy = posy;
        this.accx = accx;
        this.accy = accy;
        this.bounce = bounce;
        this.defbounce = bounce;
        this.radius = radius;
        this.friction = friction;
        this.p = p;


        audioManager = (AudioManager) v.getContext().getSystemService(
                Context.AUDIO_SERVICE);
    }

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

    public int getRadius() {
        return radius;
    }

    public Paint getP() {
        return p;
    }

    public void setPosx(double posx) {
        this.posx = posx;
    }

    public void setPosy(double posy) {
        this.posy = posy;
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
                //playsound();
                accy = (Math.abs(accy) * bounce);
                if (bounce > 0 && bounce != 1)
                    bounce -= 0.01;
                if (bounce == 0.77) bounce = 1;
            }


            if (posy <= (radius)) { // prüft ob Ball an der Decke ist und wechselt Richtung !!!
                //playsound();+
                //Test123
                accy = 0 - Math.abs(accy) * bounce;
            }

            if (posx >= (width - radius)) { // prüft ob Ball rechts an der Wand ist
                //playsound();
                accx = 0 - Math.abs(accx) * bounce;
            }

            if (posx <= (radius)) { // prüft ob Ball links an der Wand ist
                //playsound();
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
                accy = (Math.abs(accy) * bounce);
            }

            if (posy <= (radius)) { // prüft ob Ball an der Decke ist
                //playsound();+
                //Test123
                accy = 0 - Math.abs(accy) * bounce;
            }

            if (posx >= (width - radius)) { // prüft ob Ball rechts an der Wand ist
                //playsound();
                accx = 0 - Math.abs(accx) * bounce;
            }

            if (posx <= (radius)) { // prüft ob Ball links an der Wand ist
                //playsound();
                accx = Math.abs(accx) * bounce;
            }
        }
    }


    public void playsound() {
        currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC) / 15.0f;
        sounds.play(bounceSound, (float) Math.abs(bounce * bounce)
                * currentVolume, (float) Math.abs(bounce * bounce)
                * currentVolume, 0, 0, 1.0f);
    }


    public void draw(Canvas c) {

        height = c.getClipBounds().height();
        width = c.getClipBounds().width();

        c.drawCircle((float) posx, (float) posy, (float) radius, p);
    }

    /*
    public void control(MotionEvent e) {
        currentVolume = audio.getStreamVolume(AudioManager.STREAM_MUSIC) / 15.0f;
        sounds.play(touchSound, currentVolume, currentVolume, 0, 0, 1.5f);
        accx = (e.getX() - posx) / 25;
        accy = (e.getY() - posy) / -25;
        bounce = defbounce;
    }
    */
}