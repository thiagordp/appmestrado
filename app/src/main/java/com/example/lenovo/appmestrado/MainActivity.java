package com.example.lenovo.appmestrado;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

public class MainActivity extends AppCompatActivity implements TextToSpeech.OnInitListener {

    private final String TAG = "MAIN_ACT";

    /**
     * Botão que exibe as opções de lembretes
     */
    private ImageButton btnMenu;
    private TextToSpeech tts;
    private String speechString = "";
    private ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(50);
    private View view;
    private Timer timer = new Timer();
    private String contentURL = "";
    private Activity act = this;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);     // Full Screen

        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);                                               // Fix Landscape

        this.view = this.getWindow().getDecorView();
        // view.setBackGroundDrawable(getResources().getDrawable(R.drawable.normal));
        LinearLayout ll = (LinearLayout) findViewById(R.id.linear);
        ll.setBackgroundResource(R.drawable.normal);

        // this.speechString = "No mundo atual, a expansão dos mercados mundiais garante a contribuição de um grupo importante na determinação das diretrizes de desenvolvimento para o futuro.";
        // ControlSpeak controlSpeak = new ControlSpeak(this, this.speechString, this.view);
        // controlSpeak.speak();
        // fala();

        btnMenu = findViewById(R.id.btnMenu);
        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DateFormat sdf = SimpleDateFormat.getDateTimeInstance();
                String currentDateandTime = sdf.format(new Date());

                speechString = "Olá";
                Log.d("TAG", speechString);

                Intent intent = new Intent(getApplicationContext(), NewReminderActivity.class);
                startActivity(intent);
            }
        });
        TimerTask timerTask = new TaskTalk(view, act); // Piscar e olhar pros lados

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(timerTask, 0, 10000);

        if (savedInstanceState == null) {

            Timer timer2 = new Timer();
            TimerTask timerUpdateReminders = new TimerTask() {
                int x = 0;

                @Override
                public void run() {
                    Log.d(TAG, "Olá" + String.valueOf(x++));


                    //////////////////////////////////////////////
                    try {
                        /*  FileInputStream fis = openFileInput(Util.FILE_NAME);
                        byte[] bytes = new byte[fis.available()];
                        int count = fis.read(bytes);
                        Log.d("LEN_CONT", String.valueOf(count));
                        String content = new String(bytes);*/

                        contentURL = "";

                        Thread thread = new Thread() {
                            public void run() {
                                System.out.println("Thread Running");
                                contentURL = getTaskList();
                            }
                        };

                        thread.start();
                        thread.join();

                        Log.d("CONTENT", contentURL);

                        if (contentURL.isEmpty()) {
                            Log.e(TAG, "Empty content.");
                            return;
                        }

                        JSONArray reminders = new JSONArray(contentURL);
                        try {

                            Log.d(TAG, String.valueOf(scheduledExecutorService.shutdownNow()));
                            Log.d(TAG, String.valueOf(scheduledExecutorService.awaitTermination(100, TimeUnit.MILLISECONDS)));
                            Thread.sleep(100);
                            scheduledExecutorService = Executors.newScheduledThreadPool(50);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        setupReminders(reminders);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };
            timer2.scheduleAtFixedRate(timerUpdateReminders, 100, Util.DELAY_1H);
        }
    }

    @Override
    public void onInit(int i) {
        tts.setLanguage(Locale.ROOT);

        tts.speak(speechString, TextToSpeech.QUEUE_FLUSH, null);
    }


    private String getTaskList() {

        //String strURL = Util.URL_SERVER + Util.LIST_RESOURCE + "?" + Util.USER_ID_KEY + "=1";
        String strURL = Util.URL_SERVER + Util.LIST_RESOURCE + Util.AND + Util.USER_ID_KEY + "=1";
        Log.d(TAG, "URL:\t" + strURL);

        try {
            URL url = new URL(strURL);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

            Log.d(TAG, "Verifying answer...");

            if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                String response = Util.convertStreamToString(httpURLConnection.getInputStream());

                Log.d(TAG, "Response:\t\t" + response);
                return response;
            }

        } catch (MalformedURLException e) {
            Log.e(TAG, e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
            e.printStackTrace();
        }

        return new JSONObject().toString();
    }

    private void setupReminders(JSONArray reminders) {

        try {
            for (int i = 0; i < reminders.length(); i++) {
                JSONObject reminder = reminders.getJSONObject(i);

                String name = reminder.getString(Util.NAME_KEY);
                String message = reminder.getString(Util.MESSAGE_KEY);
                String timestamp = reminder.getString(Util.DATE_KEY);
                String type = reminder.getString(Util.TYPE_KEY);

                switch (type) {
                    case Util.TYPE_DAILY:
                        setupDaily(timestamp, reminder);
                        break;
                    case Util.TYPE_UNIQUE:
                        setupUnique(timestamp, reminder);
                        break;
                    case Util.TYPE_WEEKLY:
                        setupWeekly(timestamp, reminder);
                        break;
                }

                Log.d("REMINDER " + i, reminder.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setupDaily(String timestamp, JSONObject reminder) throws Exception {
        Log.d("STATUS", "Setup daily");

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS", Locale.ROOT);
        Date parsedDate = dateFormat.parse(timestamp);
        Timestamp timestampObj = new java.sql.Timestamp(parsedDate.getTime());

        Long currentTime = new Date().getTime();
        Long futureTime = timestampObj.getTime();
        Long delay24h = 24 * 60 * 60 * 1000L;

        while (futureTime < currentTime) {
            futureTime += delay24h;
        }

        long startTime = futureTime - currentTime;
        ReminderTask reminderTask = new ReminderTask(reminder, getApplicationContext(), this, this.view, timer);

        //   final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduledExecutorService.scheduleAtFixedRate(reminderTask, startTime, delay24h, MILLISECONDS);
    }

    private void setupUnique(String timestamp, JSONObject reminder) throws Exception {
        Log.d("STATUS", "Setup unique");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS", Locale.ROOT);
        Date parsedDate = dateFormat.parse(timestamp);
        Timestamp timestampObj = new java.sql.Timestamp(parsedDate.getTime());

        Long currentTime = new Date().getTime();
        Long futureTime = timestampObj.getTime();

        long startTime = futureTime - currentTime;

        if (startTime < 0) {
            //    Toast.makeText(this, "Horário inserido é inválido", Toast.LENGTH_SHORT).show();
            return;
        }

        ReminderTask reminderTask = new ReminderTask(reminder, getApplicationContext(), this, this.view, timer);
        //  final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduledExecutorService.schedule(reminderTask, startTime, MILLISECONDS);
    }

    private void setupWeekly(String timestamp, JSONObject reminder) throws Exception {
        Log.d("STATUS", "Setup weekly");

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS", Locale.ROOT);
        Date parsedDate = dateFormat.parse(timestamp);
        Timestamp timestampObj = new java.sql.Timestamp(parsedDate.getTime());

        Long currentTime = new Date().getTime();
        Long futureTime = timestampObj.getTime();

        while (futureTime < currentTime) {
            futureTime += Util.DELAY_24H * 7;   // Próxima semana
        }

        long startTime = futureTime - currentTime;
        ReminderTask reminderTask = new ReminderTask(reminder, getApplicationContext(), this, view, timer);

        // final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduledExecutorService.scheduleAtFixedRate(reminderTask, startTime, Util.DELAY_24H * 7, MILLISECONDS);
    }
}
