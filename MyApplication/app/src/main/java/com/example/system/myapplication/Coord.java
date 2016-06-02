package com.example.system.myapplication;

import android.graphics.Color;

/**
 * Created by System on 2016-05-25.
 */
public class Coord {

    private int x;
    private int y;

    public Coord(int a, int b){
        x=a;
        y = b;
    }

    public Coord setCoord(int a, int b){
        x=a;
        y = b;
        return this;
    }

    public int x(){
        return x;
    }

    public int y(){
        return y;
    }
}
