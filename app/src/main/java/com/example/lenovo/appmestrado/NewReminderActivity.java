package com.example.lenovo.appmestrado;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TimePicker;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Locale;

public class NewReminderActivity extends AppCompatActivity {

    private static final String TAG = "NEW_REMINDER_ACT";
    private Button btnSalvar;
    private Button btnListar;
    private TimePicker timePicker;
    private DatePicker datePicker;
    private EditText edtName;
    private EditText edtMessage;
    private RadioButton rbUnica;
    private RadioButton rbSemanal;
    private RadioButton rbDiaria;

    private String contentURL = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);     // Full Screen
        setContentView(R.layout.activity_new_reminder);

        timePicker = (TimePicker) findViewById(R.id.timePicker);
        timePicker.setIs24HourView(true);

        datePicker = (DatePicker) findViewById(R.id.dataPicker);
        edtName = (EditText) findViewById(R.id.edtName);
        edtMessage = (EditText) findViewById(R.id.edtMessage);

        rbDiaria = (RadioButton) findViewById(R.id.rbDiaria);
        rbSemanal = (RadioButton) findViewById(R.id.rbSemanal);
        rbUnica = (RadioButton) findViewById(R.id.rbUnica);

        btnSalvar = (Button) findViewById(R.id.btnSalvar);
        btnSalvar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                // Salvar no arquivo
                //  Toast.makeText(NewReminderActivity.this, "Lembrete salvo com sucesso", Toast.LENGTH_SHORT).show();

              /*  Context context = getApplicationContext();
                try {
                    FileOutputStream fileOutputStream = openFileOutput(Util.FILE_NAME, Context.MODE_PRIVATE);
                    fileOutputStream.write(new String("{\"reminders\":1}").getBytes());
                    fileOutputStream.close();
                    //     Toast.makeText(context, "OK", Toast.LENGTH_SHORT).show();

                } catch (Exception e) {
                    Toast.makeText(context, "Erro:\t" + e.getMessage(), Toast.LENGTH_SHORT).show();
                } finally {
                    finish();
                }
*/
                /////////////////////////////////////////////////////////////////////////
/*
                JSONArray json = new JSONArray();

                try {

                    FileInputStream fis = openFileInput(Util.FILE_NAME);
                    byte[] bytes = new byte[fis.available() + 1];
                    fis.read(bytes);
                    json = new JSONArray(new String(bytes));


                } catch (Exception e) {
                    e.printStackTrace();
                }

                Log.d("JSON_ORIGINAL", json.toString());
*/
                JSONObject reminder = new JSONObject();
                try {
                    String text = edtName.getText().toString();
                    if (text.isEmpty()) {
                        Toast.makeText(NewReminderActivity.this, "Campo NOME não pode ser vazio", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    reminder.put(Util.NAME_KEY, text);

                    int day = datePicker.getDayOfMonth();
                    int month = datePicker.getMonth();
                    int year = datePicker.getYear();
                    int hour;
                    int minute;

                    if (Build.VERSION.SDK_INT < 23) {
                        hour = timePicker.getCurrentHour();
                        minute = timePicker.getCurrentMinute();
                    } else {
                        hour = timePicker.getHour();
                        minute = timePicker.getMinute();
                    }

                  /*  String timestamp = year + "-" + String.format(Locale.ROOT, "%02d", (month + 1)) + "-" + String.format(Locale.ROOT, "%02d", day) + " " +
                            String.format(Locale.ROOT, "%02d", hour) + ":" + String.format(Locale.ROOT, "%02d", minute) + ":00.000";*/
                    String date = year + "-" + String.format(Locale.ROOT, "%02d", (month + 1)) + "-" + String.format(Locale.ROOT, "%02d", day);
                    String time = String.format(Locale.ROOT, "%02d", hour) + ":" + String.format(Locale.ROOT, "%02d", minute) + ":00.000";

                    reminder.put(Util.DATE_KEY, date);
                    reminder.put(Util.TIME_KEY, time);

                    text = edtMessage.getText().toString();

                    if (text.isEmpty()) {
                        Toast.makeText(NewReminderActivity.this, "Campo MENSAGEM não pode ser vazio", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    reminder.put(Util.MESSAGE_KEY, text);

                    if (rbDiaria.isChecked()) {
                        reminder.put(Util.TYPE_KEY, Util.TYPE_DAILY);
                    } else if (rbSemanal.isChecked()) {
                        reminder.put(Util.TYPE_KEY, Util.TYPE_WEEKLY);
                    } else if (rbUnica.isChecked()) {
                        reminder.put(Util.TYPE_KEY, Util.TYPE_UNIQUE);
                    } else {
                        Toast.makeText(NewReminderActivity.this, "Selecione a frequência do lembrete", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    reminder.put("iduser", "1");

                    Log.d("JSON", reminder.toString());

                   /* if (json.length() > 0) {
                        json.put(reminder);

                    } else {
                        json = new JSONArray();
                        json.put(reminder);
                    }

                    //////////////////////////////////////////////////////
                    /////// Salvar no arquivo
                    //////////////////////////////////////////////////////

                    String fileOutputStr = json.toString();
                    FileOutputStream fileOutputStream = openFileOutput(Util.FILE_NAME, Context.MODE_PRIVATE);
                    fileOutputStream.write(fileOutputStr.getBytes());
                    fileOutputStream.close();*/

                    final String fileOutputStr = reminder.toString();
                    Log.d(TAG, "OutString:\t" + fileOutputStr);

                    contentURL = "";

                    Thread thread = new Thread() {
                        public void run() {
                            System.out.println("Thread Running");
                            contentURL = insertNewTask(fileOutputStr);
                        }
                    };

                    thread.start();
                    thread.join();

                    Log.d("CONTENT", contentURL);


                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    finish();
                }
            }
        });

        btnListar = (Button) findViewById(R.id.btnListar);
        btnListar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent act = new Intent(getApplication(), ListRemindersActivity.class);
                startActivity(act);
            }
        });
    }


    private String insertNewTask(String data) {

        //  String strURL = Util.URL_SERVER + Util.NEW_TASK_RESOURCE + "?" + Util.USER_ID_KEY + "=1";
        String strURL = Util.URL_SERVER + Util.NEW_TASK_RESOURCE;

        Log.d(TAG, "URL:\t" + strURL);

        try {
            URL url = new URL(strURL);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setRequestProperty("Content-Type", "application/json");

            byte[] outputBytes = data.getBytes(Charset.defaultCharset());
            OutputStream os = httpURLConnection.getOutputStream();
            os.write(outputBytes);

            Log.d(TAG, "Verifying answer...");

            if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                String response = Util.convertStreamToString(httpURLConnection.getInputStream());

                Log.d(TAG, "Response:\t\t" + response);
                return response;
            } else {
                Log.e(TAG, "Connection error with server. Code: " + httpURLConnection.getResponseCode());
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
}
