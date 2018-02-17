package com.example.lenovo.appmestrado;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by lenovo on 15/02/2018.
 */

public class ListAsyncTask extends AsyncTask<String, Void, JSONObject> {

    private final String TAG = "LIST_ASYNC_TASK";

    private Context context;
    private View view;

    public ListAsyncTask(Context context, View view) {
        this.context = context;
        this.view = view;
    }

    @Override
    protected JSONObject doInBackground(String... params) {

        for (String strURL : params) {
            Log.d(TAG, "URL:\t" + strURL);

            try {
                URL url = new URL(strURL);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

                Log.d(TAG, "Verifying answer...");

                if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    String response = Util.convertStreamToString(httpURLConnection.getInputStream());

                    Log.d(TAG, "Response:\t\t" + response);
                    JSONObject json = new JSONObject(response);
                    Log.d(TAG, "JSON:\t\t" + json.toString());

                    return json;
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
        }

        return null;
    }
}
