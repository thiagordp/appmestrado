package com.example.lenovo.appmestrado;

import android.app.Activity;
import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;

import org.json.JSONObject;

import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by lenovo on 11/01/2018.
 */

public class ReminderTask extends TimerTask implements TextToSpeech.OnInitListener {

    private JSONObject message;
    private TextToSpeech tts;
    private String speechString;
    private Context context;
    private Activity activity;
    private View view;
    private Timer timer;

    public ReminderTask(JSONObject message, Context context, Activity activity, View view, Timer timer) {
        this.message = message;
        this.context = context;
        this.view = view;
        this.activity = activity;
        this.timer = timer;
    }

    private void fala() {
        tts = new TextToSpeech(context, this);
    }

    @Override
    public void onInit(int i) {
        if (tts == null) {
            tts = new TextToSpeech(context, this);
        }
        tts.setLanguage(Locale.ROOT);

        Log.d("TAG", "Before speech");
        tts.speak(speechString, TextToSpeech.QUEUE_FLUSH, null);
        Log.d("TAG", "After speech");
    }


    @Override
    public void run() {
        Log.d("TASK", "olá\t" + message.toString());
        this.speechString = "Problema ao decodificar mensagem original";

        try {
            this.speechString = message.getString(Util.MESSAGE_KEY);
            String time = message.getString(Util.DATE_KEY);
/*
            Timestamp timestamp = Timestamp.valueOf(time);
            if (timestamp.getTime() < (new Date().getTime() - 60 * 1000)) {

                return;
            }*/


        } catch (Exception e) {
            e.printStackTrace();
        }

        ControlSpeak controlSpeak = new ControlSpeak(activity, speechString, view, timer);
        controlSpeak.speak();
        fala();
    }
}
