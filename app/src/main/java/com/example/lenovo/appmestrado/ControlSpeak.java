package com.example.lenovo.appmestrado;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by lenovo on 30/01/2018.
 */
public class ControlSpeak {
    private Activity activity;
    private String message;
    private View view;
    private int[] imgs = {R.drawable.talk_half_open, R.drawable.talk_open};
    private Timer timer;

    public ControlSpeak(Activity activity, String message, View view, Timer timer) {
        this.activity = activity;
        this.message = message;
        this.timer = timer;
        this.view = view;
    }

    public void speak() {
        final int lenSyllabus = processFile(message);
        final Random random = new Random(System.currentTimeMillis());

        //     final Timer timer = new Timer();
        // TaskTalk taskTalk = new TaskTalk(random, 0, lenSyllabus, view, activity);

        TimerTask timerTask = new TimerTask() {
            int i = 0;

            @Override
            public void run() {
                Log.d("OLA", "OLA " + i);

                if (i < lenSyllabus) {

                    activity.runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            LinearLayout ll = (LinearLayout) view.findViewById(R.id.linear);
                            ll.setBackgroundResource(imgs[i % imgs.length]);
                        }
                    });

                    i++;
                } else {
                    timer.cancel();
                    timer.purge();

                    timer = new Timer();
                    TaskTalk taskTalk = new TaskTalk(view, activity);
                    timer.scheduleAtFixedRate(taskTalk, 2000, 10000);

                    activity.runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            LinearLayout ll = (LinearLayout) view.findViewById(R.id.linear);
                            ll.setBackgroundResource(R.drawable.normal);
                        }
                    });
                }
            }
        };
        timer = new Timer();
        timer.scheduleAtFixedRate(timerTask, 0, 200);

    }

    private int processFile(String fileName) {
        String line = null;
        int wordsNumber = 0, sentencesNumber = 0, syllabusNumber = 0;
        System.out.println("Opening first file \"" + fileName + "\"");

        try {
            System.out.println("Processing text...");

            line = this.message;
            line = line.replaceAll("\\s+", " ");
            line = line.replaceAll(",\\.!\\?", "");
            Log.d("SPEAK", line);
            String[] wordsArray = line.split(" ");

            // Counting number of sentences
            for (int i = 0; i < wordsArray.length; i++) {
                //Counting number of syllabus
                if (wordsArray[i].length() > 2)
                    syllabusNumber += (wordsArray[i].length() / 2);
                else
                    syllabusNumber++;
            }
            // Counting number of words
            if (!line.isEmpty()) {
                wordsNumber += wordsArray.length;
            }

            Log.d("SPEAK", "Report generated.");
            Log.d("SPEAK", "numero de palavras: " + wordsNumber + "\nnumero de sentencas: " + sentencesNumber + "\nnumero de silabas: " + syllabusNumber);

        } catch (Exception e) {
            System.out.println("Exception occurred");
        }

        return syllabusNumber;
    }
}
