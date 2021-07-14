package com.example.popballoons;

import android.animation.Animator;
import android.animation.ValueAnimator;

public class BalloonListener implements Animator.AnimatorListener, ValueAnimator.AnimatorUpdateListener{

    Balloon balloon;

    public BalloonListener(Balloon balloon){
        this.balloon = balloon;
    }

    @Override
    public void onAnimationStart(Animator animator) {

    }
    @Override
    public void onAnimationEnd(Animator animator) {

    }

    @Override
    public void onAnimationCancel(Animator animator) {

    }

    @Override
    public void onAnimationRepeat(Animator animator) {

    }

    @Override
    public void onAnimationUpdate(ValueAnimator valueAnimator) {
        balloon.setY((float) valueAnimator.getAnimatedValue());
    }
}
