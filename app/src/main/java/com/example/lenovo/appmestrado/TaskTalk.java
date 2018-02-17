package com.example.lenovo.appmestrado;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import java.util.Random;
import java.util.TimerTask;

/**
 * Created by lenovo on 01/02/2018.
 */
public class TaskTalk extends TimerTask {

    private Random random;
    private int i;
    private int len;
    private View view;
    private Activity activity;

    private int eye[] = {R.drawable.normal, R.drawable.normal_closed, R.drawable.normal};

    private int look[] = {R.drawable.normal_front, R.drawable.normal_left, R.drawable.normal_right, R.drawable.normal_down, R.drawable.normal_up};

    public TaskTalk(View view, Activity activity) {
        this.view = view;
        this.activity = activity;

    }

    @Override
    public void run() {
        Log.d("TALK", "Moving Face"        );        /*
         * Piscar
         */
        for (i = 0; i < eye.length; i++) {

            activity.runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    LinearLayout ll = (LinearLayout) view.findViewById(R.id.linear);
                    ll.setBackgroundResource(eye[i % eye.length]);
                }
            });

            try {
                Thread.sleep(500);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        try {
            Thread.sleep(5000);
        } catch (Exception e) {
            e.printStackTrace();
        }

        activity.runOnUiThread(new Runnable() {

            @Override
            public void run() {
                Random random = new Random(System.currentTimeMillis());
                LinearLayout ll = (LinearLayout) view.findViewById(R.id.linear);
                ll.setBackgroundResource(look[random.nextInt(look.length)]);
            }
        });

        try {
            Thread.sleep(2000);
        } catch (Exception e) {
            e.printStackTrace();
        }

        activity.runOnUiThread(new Runnable() {

            @Override
            public void run() {
                Random random = new Random(System.currentTimeMillis());
                LinearLayout ll = (LinearLayout) view.findViewById(R.id.linear);
                ll.setBackgroundResource(look[0]);
            }
        });
    }
}

