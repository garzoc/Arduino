package com.example.system.myapplication;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * @authors John Sundling, Emanuel Mellblom.
 * This class handles the transfer of image data from the raspberry Pi to the app
 * It also handles rendering the bitmap to a drawable in the app.
 */

public class DataHandeler extends AppCompatActivity {
    static int collectorIndex;
    static int storageIndex;
    final  int size;
    static int memory;
    static byte[][] inData;
    static private boolean decoderBusy;
    static List<Byte> bytelist  = new ArrayList<Byte>();
    //static BooleanC threadBusy = new BooleanC(false);
    static BooleanC test = new BooleanC(false);
    char[] endSequence={'9', '6', '0', '3', '1', '7', '2', '3', '1', '4'};
    int endSequenceIndex=0;
    ImageView raspImg;
    public static int packetSize = 2048;

    public DataHandeler(int size, ImageView image){
        this.size = size;
        memory = 0;
        this.collectorIndex = 0;
        this. storageIndex = 0;
        this.inData = new byte[size][size];
        this.decoderBusy = false;
        this.raspImg = image;
    }

    public boolean push(byte[] data){
        if((storageIndex+1)%size != collectorIndex){
            this.inData[storageIndex] = data;
            storageIndex++;
            storageIndex = storageIndex%size;
            return  true;
        }
        return false;
    }

    public byte[] poll(){
        if(storageIndex!=collectorIndex){
            byte[] temp = inData[collectorIndex];
            this.collectorIndex++;
            this.collectorIndex = collectorIndex%size;
            return temp;
        }
        return null;
    }

    public byte[] peek(){
        if(storageIndex!=collectorIndex){
            return inData[collectorIndex];
        }
        return null;
    }

    public boolean toBoolean(int orginal){
        return orginal==1?true:false;
    }

    public boolean parseSequence(byte input) {
        if((this.endSequence[this.endSequenceIndex])== (input)){
            boolean endSequenceFound=endSequenceIndex==(9)?toBoolean((endSequenceIndex-=10)+2):false;
            this.endSequenceIndex++;
            return endSequenceFound; //6
        }else{
            this.endSequenceIndex=0;
            return false;
        }
    }

    int count = 0;
    public void decodeBuffer(){
        if(!decoderBusy) {
            if(bytelist.size()>0){
                bytelist.clear();
            }
            decoderBusy = true;
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    while(peek()!=null){
                        byte[] temp= poll();
                        for(int i = 0; i<packetSize; i++){
                            int tempInt=temp[i];
                            count++;
                            if(!parseSequence(temp[i])) {
                                bytelist.add((byte) tempInt);
                            }else{
                                int size = bytelist.size();
                                if(size>=7){
                                    for (int j = (size - 1); j > size - 10; j--){
                                        bytelist.remove(j);
                                    }
                                    displayImages(bytelist);
                                }
                                endSequenceIndex =0;
                                break;
                            }
                        }
                        if(peek()==null) {
                            try {
                                Thread.sleep(500);
                            }catch (InterruptedException ie){
                                System.out.println(ie);
                            }
                        }
                        if(test.getBool()) {
                            break;
                        }
                    }
                    decoderBusy = false;
                }
            }, 0);
        }
    }

    Bitmap im;
    BitmapFactory.Options factory = new BitmapFactory.Options();

    public void displayImages(List<Byte> list){
        factory.inSampleSize = 1;
        factory.inPreferQualityOverSpeed=false;
        factory.inDither = false;
        final byte[] imageList = constructByteArray(list);

        if(im != null) {
            if (im.isRecycled() != true) {
                im.recycle();
            }
        }

        final BooleanC theimage = new BooleanC(false);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    im = BitmapFactory.decodeByteArray(imageList, 0, imageList.length, factory);
                    factory.inBitmap = im;
                    theimage.setDrawable(new BitmapDrawable(Resources.getSystem(), im));
                    theimage.setBool(true);
                }
            }).start();
        while(true) {
            if(theimage.getBool()) {
                VideoDisplay.setImages(theimage.getDrawable());
            break;
            }
        }
        }


    public byte[] constructByteArray(List<Byte> list){
        byte[] array = new byte[list.size()];
        for(int i = 0; i<array.length;i++){
           array[i] = list.get(i);
        }
        return array;
    }
}