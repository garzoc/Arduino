package com.example.system.myapplication;

/**
 * @author John Sundling
 * @cntributer Emanuel Mellblom
 * A class that handles the buffer for mapping
 */

public class BufferHandler {
    private int collectorIndex;
    private int storageIndex;
    private int size;
    final private int memory;
    private String[] buffer;

    public BufferHandler(){
        memory = 20;
        this.collectorIndex = 0;
        this.storageIndex = 0;
        this.size = 0;
        this.buffer = new String[memory];
    }

    public void push(String string){
        if((storageIndex+1)%this.memory!=collectorIndex){
            this.size++;
            this.buffer[storageIndex]=string;
            this.storageIndex++;
            this.storageIndex=this.storageIndex%this.memory;
        }
    }

    public  String poll(){
        if(storageIndex!=collectorIndex){
            this.size--;
            String tmp = this.buffer[collectorIndex];
           this.collectorIndex++;
            this.collectorIndex=this.collectorIndex%this.memory;
            return tmp;
        }
        return "";
    }

    public String peek() {
        if (storageIndex != collectorIndex) {
            String tmp = this.buffer[collectorIndex];
            return tmp;
        }
        return "";
    }

    public int size(){
        return this.size;
    }
}
