package com.example.popballoons;

import android.animation.ValueAnimator;
import android.content.Context;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;

import androidx.appcompat.widget.AppCompatImageView;

public class Balloon extends AppCompatImageView implements View.OnTouchListener{

    private ValueAnimator animator;
    private BalloonListener listener;
    private boolean isPopped;

    private PopListener mainactivity;
    private final String TAG = getClass().getName();

    public Balloon(Context context){
        super(context);
    }

    public Balloon(Context context, int color, int height, int level){
        super(context);

        mainactivity = (PopListener) context;
        listener = new BalloonListener(this);
        setOnTouchListener(this);

        setImageResource(R.mipmap.balon);
        setColorFilter(color);
        int width = height / 2;

        int dpHeight = pixelsToDp(height, context);
        int dpWidth = pixelsToDp(width, context);

        ViewGroup.LayoutParams params =
                new ViewGroup.LayoutParams(dpWidth, dpHeight);
        setLayoutParams(params);
    }

    public static int pixelsToDp(int px, Context context){
        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, px,
                context.getResources().getDisplayMetrics()
        );
    }

    public void release(int scrHeight, int duration){
        animator = new ValueAnimator();
        animator.setDuration(duration);
        animator.setFloatValues(scrHeight, 0f);
        animator.setInterpolator(new LinearInterpolator());
        animator.setTarget(this);
        animator.addListener(listener);
        animator.addUpdateListener(listener);
        animator.start();
    }

    @Override
    public boolean onTouch(View view, MotionEvent event){
        Log.d(TAG, "TOUCHED");
        if(!isPopped){
            mainactivity.popBalloon(this, true);
            isPopped = true;
            animator.cancel();
        }
        return true;
    }

    public void pop(boolean isTouched){
        mainactivity.popBalloon(this, isTouched);
    }

    public boolean isPopped(){
        return isPopped;
    }

    public void setPopped(boolean b){
        isPopped = true;
    }
}
