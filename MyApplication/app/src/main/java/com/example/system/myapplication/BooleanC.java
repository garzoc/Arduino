package com.example.system.myapplication;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

/**
 * @authors Emanuel Mellblom, John Sundling
 * Create boolean variables for use in other classes
 */
    public class BooleanC {
        private boolean bool;
        private int integer;
        private Drawable drawable;
        private Bitmap bitmap;


        public BooleanC(boolean bool){
            this.bool = bool;
            this.integer = 0;
            this.drawable = null;
            this.bitmap = null;
        }

        public void setBool(boolean bool){
            this.bool = bool;
        }

        public boolean getBool(){
            return this.bool;
        }

        public void setInt(int integer){
            this.integer = integer;
        }

        public int getInt(){
            return this.integer;
        }

        public void setDrawable(Drawable drawable){
            this.drawable = drawable;
        }

        public Drawable getDrawable(){
            return this.drawable;
        }

        public Bitmap getBitmap(){
            return this.bitmap;
        }

        public void setBitmap(Bitmap bitmap){
            this.bitmap = bitmap;
        }
    }


