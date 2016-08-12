package com.practo.shashank.Pokesearch;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class WikiResponse extends BroadcastReceiver {

    public static final String PARAM_RES = "WIKIRES";

    public WikiResponse() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String text = intent.getStringExtra(WikiFetch.PARAM_RES);
        Activity activity = (Activity) context;
        TextView viewtext = (TextView) activity.findViewById(R.id.abilitiesShowText);

        try {

            JSONObject json = new JSONObject(text);
            ArrayList<String> show = new ArrayList();

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

            viewtext.setText(show.toString());

        } catch (JSONException e) {
            Log.e("Bcast","cant jsonify response");
            Log.e("BCast","Response "+text);
        }
    }
}