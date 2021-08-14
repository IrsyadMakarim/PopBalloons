package com.example.popballoons;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;
import java.util.Random;
import java.util.logging.Level;

import com.example.popballoons.HighScoreHelper;

public class MainActivity extends AppCompatActivity implements PopListener{

    ViewGroup contentView;
    private static String TAG;

    private boolean isGameStarted = false;
    private int[] colors = new int[3];
    private int scrWidth;
    private int scrHeight;
    private int level;
    private int userScore;
    private int numberOfPins = 5;
    private int pinsUsed;
    private boolean isGameStopped = true;
    private int balloonsPopped = 0;
    private int balloonsPerLevel = 8;
    private int balloonsLaunched;
    private ArrayList<ImageView> pinImages = new ArrayList<>();
    private ArrayList<Balloon> balloons = new ArrayList<>();
    TextView levelDisplay;
    TextView scoreDisplay;
    TextView highScoreDisplay;
    Button btn;

    Audio audio;
    DatabaseHelper myDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TAG = getClass().getName();

        getWindow().setBackgroundDrawableResource(R.mipmap.backgroundkp);
        setContentView(R.layout.activity_main);

        contentView = (ViewGroup) findViewById(R.id.content_view);
        levelDisplay = (TextView) findViewById(R.id.level_display);
        scoreDisplay = (TextView) findViewById(R.id.score_display);
        highScoreDisplay = (TextView) findViewById(R.id.high_score_display);
        btn = (Button) findViewById(R.id.go_button);

        int newScore = HighScoreHelper.getTopScore(this);
        highScoreDisplay.setText(String.format("%d", newScore));

        pinImages.add((ImageView) findViewById(R.id.pushpin1));
        pinImages.add((ImageView) findViewById(R.id.pushpin2));
        pinImages.add((ImageView) findViewById(R.id.pushpin3));
        pinImages.add((ImageView) findViewById(R.id.pushpin4));
        pinImages.add((ImageView) findViewById(R.id.pushpin5));

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isGameStarted == false){
                    startLevel();
                    isGameStarted = true;
                }
            }
        });
        colors[0] = Color.argb(255, 255, 0, 0);
        colors[1] = Color.argb(255, 0, 255, 0);
        colors[2] = Color.argb(255, 255, 255, 0);

        contentView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                Log.d(TAG, "onToutch");

                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN){
                    setToFullScreen();
                }
                return false;
            }
        });

        audio = new Audio(this);
        audio.prepareMediaPlayer(this);

        myDb = new DatabaseHelper(this);
    }

    @Override
    public void popBalloon(Balloon bal, boolean isTouched) {

        audio.playSound();

        balloonsPopped++;
        balloons.remove(bal);
        contentView.removeView(bal);

        if(isTouched){
            userScore++;
            scoreDisplay.setText(String.format("%d", userScore));
        } else {
            pinsUsed++;
            if (pinsUsed <= pinImages.size()) {
                pinImages.get(pinsUsed - 1).setImageResource(R.drawable.pin_broken);
                Toast.makeText(this, "Ouch!", Toast.LENGTH_SHORT).show();
            }
            if(pinsUsed == numberOfPins){
                gameOver();
            }
        }
        if (balloonsPopped == balloonsPerLevel){
            finishLevel();
        }
    }

    //Function untuk memulai game
    private void startGame(){
        userScore = 0; //Reset user score menjadi 0
        level = 1; //Reset nilai level menjadi 1
        updateGameStats(); //Kita update stat

        audio.playMusic();

        for (ImageView pin: pinImages){
            pin.setImageResource(R.drawable.pin);
        }
    }

    private void checkHighScore(){
        if (HighScoreHelper.isTopScore(this, userScore)){
            HighScoreHelper.setTopScore(this, userScore);
            int newHigh = HighScoreHelper.getTopScore(this);
            highScoreDisplay.setText(String.format("%d", newHigh));
            myDb.insertData(String.valueOf(newHigh));
        }
    }

    private void gameOver(){

        isGameStopped = true;
        isGameStarted = false;
        balloonsPopped = 0;
        pinsUsed = 0;
        level = 0;
        Toast.makeText(this, "Game Over", Toast.LENGTH_LONG).show();
        btn.setText("Play Game");

        audio.pauseMusic();

        for (Balloon bal : balloons){
            bal.setPopped(true);
            contentView.removeView(bal);
        }

        checkHighScore();
    }

    private void startLevel(){
        if (isGameStopped){
            isGameStopped = false;
            startGame();
        }
        updateGameStats();
        new LevelLoop(level).start();
    }

    private void finishLevel(){
        Log.d(TAG, "FINISH LEVEL");

        String message = String.format("Level %d finished!", level);
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        level++;
        balloonsPerLevel++;
        updateGameStats();
        btn.setText(String.format("Start level %d", level));
        isGameStarted = false;

        Log.d(TAG, String.format("balloonsLaunched = %d",
                balloonsLaunched));
        balloonsPopped = 0;

        checkHighScore();
    }

    private void updateGameStats(){
        levelDisplay.setText(String.format("%s", level));
        scoreDisplay.setText(String.format("%s", userScore));
    }

    public void launchBalloon(int xPos){

        int curColor = colors[nextColor()];
        Balloon btemp = new Balloon(MainActivity.this,
                curColor, 100, 1);
        btemp.setY(scrHeight);
        btemp.setX(xPos);

        balloons.add(btemp);

        contentView.addView(btemp);
        btemp.release(scrHeight, 5000);

        Log.d(TAG, "Balloon created");
    }

    protected void onResume(){
        super.onResume();

        audio.playMusic();

        updateGameStats();
        setToFullScreen();

        ViewTreeObserver viewTreeObserver = contentView.getViewTreeObserver();
        if (viewTreeObserver.isAlive()){
            viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    contentView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    scrWidth = contentView.getWidth();
                    scrHeight = contentView.getHeight();
                }
            });
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        audio.pauseMusic();
    }

    private void setToFullScreen(){
        contentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
        |View.SYSTEM_UI_FLAG_FULLSCREEN
        |View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        |View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        |View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
        |View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }

    private static int nextColor(){
        int max = 2;
        int min = 0;
        int retval = 0;

        Random random = new Random();
        retval = random.nextInt((max - min) + 1) + min;

        Log.d(TAG, String.format("retval = %d", retval));
        return retval;
    }

    class LevelLoop extends Thread{
        private int shortDelay = 500;
        private int longDelay = 1_500;
        private int maxDelay;
        private int minDelay;
        private int delay;
        private int looplevel;

        int balloonsLaunched = 0;

        public LevelLoop(int argLevel){
            looplevel = argLevel;
        }

        public void run(){
            while(balloonsLaunched < balloonsPerLevel){

                balloonsLaunched++;
                Random random = new Random(new Date().getTime());
                final int xPosition = random.nextInt(scrWidth - 200);

                maxDelay = Math.max(shortDelay,
                        (longDelay - ((looplevel - 1 )) * 500));
                minDelay = maxDelay / 2;
                delay = random.nextInt(minDelay) + minDelay;

                Log.i(TAG, String.format("Thread delay = %d", delay));

                try {
                    Thread.sleep(delay);
                }
                catch (InterruptedException e){
                    Log.e(TAG, e.getMessage());
                }

                runOnUiThread(new Thread(){
                    public void run(){
                        launchBalloon(xPosition);
                    }
                });
            }
        }
    }
}