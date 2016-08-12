package com.practo.shashank.Pokesearch;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by shashank on 11/08/16.
 */
public class PokemonAsynctask extends AsyncTask<String,String,String> {

    public PokemonInterface delegate = null;



    /**
     * Before starting background thread
     * */
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        System.out.println("Starting download");
    }

    @Override
    protected String doInBackground(String... msg) {

        String result = "";
        URL url;
       // Log.d("MSG",msg);
        HttpURLConnection urlConnection = null;
        try {
            url = new URL("http://pokeapi.co/api/v2/pokemon/"+msg[0]);

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

        return result;
    }

    /**
     * After completing background task
     * **/
    @Override
    protected void onPostExecute(String result) {
        ArrayList<String> show = new ArrayList();
        try {

            JSONObject json = new JSONObject(result);


            if (json.has("abilities")){
                JSONArray temp = json.getJSONArray("abilities");
                for (int i = 0; i < temp.length(); i++) {
                    try {
                        JSONObject row = temp.getJSONObject(i);
                        show.add(row.getJSONObject("ability").get("name").toString());
                    }catch (JSONException e){
                        Log.e("Bcast","Abilities not Jsonable");
                    }
                }
            }else {
                show.add("No Abilities");
            }



        } catch (JSONException e) {
            Log.e("Bcast","cant jsonify response");
            Log.e("BCast","Response "+result);
        }

        delegate.processFinish(show.toString());
    }
}
