package com.getyasa.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

import com.getyasa.R;

/**
 * Created by maxim.vasilkov@gmail.com on 22/12/15.
 */
public class SplashActivity extends AppCompatActivity {

    final int time = 1600;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        setContentView(R.layout.splash_activity);

        ImageView img = (ImageView) findViewById(R.id.yaicon1);
        RotateAnimation rotateAnimation = new RotateAnimation(0, 720,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setDuration(time);
        img.startAnimation(rotateAnimation);
        new Handler().postDelayed(new Runnable() {
            public void run() {
                handler.sendEmptyMessage(0);
            }
        }, time);
    }

    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            Intent intent = new Intent(SplashActivity.this, ShapesActivity.class);
            overridePendingTransition(R.anim.appear_bottom_right_in, R.anim.disappear_top_left_out);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            return false;
        }
    });

}
