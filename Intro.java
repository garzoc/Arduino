package com.example.system.myapplication;

import android.content.Intent;
import android.os.Bundle;
import com.github.paolorotolo.appintro.AppIntro;

/**
 * @author Emanuel Mellblom
 * Class for setting up the App Intro
 */

public class Intro extends AppIntro {
    @Override
    public void init(Bundle savedInstanceState) {
        //Here we are adding the slides
        addSlide(SampleSlide.newInstance(R.layout.slide));
        addSlide(SampleSlide.newInstance(R.layout.slide2));
        addSlide(SampleSlide.newInstance(R.layout.slide3));
        addSlide(SampleSlide.newInstance(R.layout.slide4));
        addSlide(SampleSlide.newInstance(R.layout.slide5));
        addSlide(SampleSlide.newInstance(R.layout.slide6));
        addSlide(SampleSlide.newInstance(R.layout.slide7));
        addSlide(SampleSlide.newInstance(R.layout.slide8));

        //Hide the actionbar at the top
        getSupportActionBar().hide();

        // Hide Skip/Done button
        showSkipButton(true);
        showStatusBar(true);

        //Vibrate on click
        setVibrate(true);
        setVibrateIntensity(30);

        //This is different transition animations
        //setDepthAnimation();
        //setFadeAnimation();
        //setZoomAnimation();
        setFlowAnimation();
        //setSlideOverAnimation();
        //setDepthAnimation();
    }

    @Override
    public void onSkipPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void onNextPressed() {
        // Do something when users tap on Next button.
    }

    @Override
    public void onDonePressed() {
        // Do something when users tap on Done button.
        finish();
    }

    @Override
    public void onSlideChanged() {
        // Do something when slide is changed
    }
}