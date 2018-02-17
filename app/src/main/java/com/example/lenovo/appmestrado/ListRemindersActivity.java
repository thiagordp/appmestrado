package com.example.lenovo.appmestrado;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;

public class ListRemindersActivity extends AppCompatActivity {

    private final String TAG = "LIST_REMINDERS_ACT";

    private String dataURL = "";
    private Button btnApagar;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_reminders);

        // this.json = new JSONObject();
        // this.view = getWindow().getDecorView().getRootView();

        listView = findViewById(R.id.lvReminders);


        btnApagar = findViewById(R.id.btnDeleteAll);
        btnApagar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    Thread thread = new Thread() {
                        public void run() {
                            System.out.println("Thread Running");

                            removeUserTask();
                        }
                    };

                    thread.start();
                    thread.join();

                    fillList();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        fillList();

    }

    private String removeUserTask() {

        // String strURL = Util.URL_SERVER + Util.DELETE_USER_TASKS + "?" + Util.USER_ID_KEY + "=1";
        String strURL = Util.URL_SERVER + Util.DELETE_USER_TASKS;


        Log.d(TAG, "URL:\t" + strURL);

        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("iduser", "1");
            String data = jsonObject.toString();

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
            }

        } catch (MalformedURLException e) {
            Log.e(TAG, e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
            e.printStackTrace();
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
            e.printStackTrace();
        }

        return new JSONObject().toString();
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

    private void fillList() {
        // Ler arquivo e pegar JSON Array
        try {
           /* FileInputStream fis = openFileInput(Util.FILE_NAME);
            byte[] content = new byte[fis.available()];
            fis.read(content);
            String data = new String(content);*/
            dataURL = "";

            Thread thread = new Thread() {
                public void run() {
                    System.out.println("Thread Running");
                    dataURL = getTaskList();
                }
            };

            thread.start();
            thread.join();

            JSONArray jsonArray = new JSONArray(dataURL);
            int lenArray = jsonArray.length();

            if (lenArray == 0) {
                listView.setAdapter(null);
                Toast.makeText(this, "Nenhum lembrete encontrado", Toast.LENGTH_SHORT).show();
                return;
            }

            String[] nomes = new String[lenArray];
            String[] mensagens = new String[lenArray];
            String[] date = new String[lenArray];
            String[] type = new String[lenArray];

            // Preencher vetores
            for (int i = 0; i < lenArray; i++) {
                JSONObject tmpJSONObj = jsonArray.getJSONObject(i);

                nomes[i] = tmpJSONObj.getString(Util.NAME_KEY);
                mensagens[i] = tmpJSONObj.getString(Util.MESSAGE_KEY);
                date[i] = tmpJSONObj.getString(Util.DATE_KEY);
                type[i] = tmpJSONObj.getString(Util.TYPE_KEY);
            }

            ReminderList reminderList = new ReminderList(this, nomes, date, mensagens, type);
            listView.setAdapter(null);
            listView.setAdapter(reminderList);

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
