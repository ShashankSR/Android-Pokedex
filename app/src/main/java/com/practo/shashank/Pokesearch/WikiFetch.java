package com.practo.shashank.Pokesearch;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class WikiFetch extends IntentService {

    public static final String PARAM_URI = "URI";
    public static final String PARAM_RES = "RES";

    public WikiFetch() {
        super("WikiFetch");
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        // Normally we would do some work here, like download a file.
        // For our sample, we just sleep for 5 seconds.
        String msg = intent.getStringExtra(WikiFetch.PARAM_URI);
        String result = "";
        URL url;
        HttpURLConnection urlConnection = null;
        try {
            url = new URL("http://pokeapi.co/api/v2/pokemon/"+msg.toLowerCase());

            urlConnection = (HttpURLConnection) url
                    .openConnection();

            InputStream in = urlConnection.getInputStream();
            BufferedReader buf = new BufferedReader(new InputStreamReader(in));
            String line = null;

            StringBuilder responseData = new StringBuilder();
            while ((line = buf.readLine()) != null) {
                responseData.append(line);
            }
            result = responseData.toString();
        } catch (Exception e) {
            Log.e("Error","Error in Fetching data");
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
        if(result != null) {
            Intent broadcastIntent = new Intent();
            broadcastIntent.setAction(WikiResponse.PARAM_RES);
            broadcastIntent.addCategory(Intent.CATEGORY_DEFAULT);
            broadcastIntent.putExtra(PARAM_RES, result);
            sendBroadcast(broadcastIntent);
        }
    }
}
