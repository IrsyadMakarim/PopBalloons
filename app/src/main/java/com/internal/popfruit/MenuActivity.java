package com.internal.popfruit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class MenuActivity extends AppCompatActivity {

    private static String TAG;
    String score;

    ViewGroup menuView;
    Button btnPlay, btnScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        TAG = getClass().getName();

        btnPlay = (Button) findViewById(R.id.menu_play);
        btnScore = (Button) findViewById(R.id.menu_score);

        getWindow().setBackgroundDrawableResource(R.mipmap.menubg);
        menuView = (ViewGroup) findViewById(R.id.menu_view);

        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        btnScore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), LeaderboardActivity.class);
                score = String.valueOf(HighScoreHelper.getTopScore(view.getContext()));
                intent.putExtra("score_key", score);
                startActivity(intent);
            }
        });

        menuView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                Log.d(TAG, "onToutch");

                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN){
                    setToFullScreen();
                }
                return false;
            }
        });
    }

    private void setToFullScreen(){
        menuView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                |View.SYSTEM_UI_FLAG_FULLSCREEN
                |View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                |View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                |View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                |View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }

    protected void onResume(){
        super.onResume();

        setToFullScreen();
    }
}