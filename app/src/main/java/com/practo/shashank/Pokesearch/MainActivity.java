package com.practo.shashank.Pokesearch;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements PokemonInterface {


    TextView abilitiesShow;
    TextView abilitiesLabel;
    Switch asyncSwitch;
    EditText searchText;
    PokeballProgressView pokeball;
    Runnable mRunnable;
    Boolean stop;
    PokemonAsynctask asynctask;
    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        stop = false;
        abilitiesShow = (TextView)this.findViewById(R.id.abilitiesShowText);
        abilitiesLabel = (TextView)this.findViewById(R.id.textView);
        asyncSwitch = (Switch)this.findViewById(R.id.switch1);
        searchText = (EditText) findViewById(R.id.editText);
        pokeball = (PokeballProgressView)findViewById(R.id.pokeballprogress);
        pokeball.setVisibility(View.INVISIBLE);
        //------------------------------------------------------------//

        IntentFilter filter = new IntentFilter(WikiResponse.PARAM_RES);
        filter.addCategory(Intent.CATEGORY_DEFAULT);
        WikiResponse receiver = new WikiResponse();
        registerReceiver(receiver, filter);


        //-----------------------------------------------------------//

        asynctask = new PokemonAsynctask();
        asynctask.delegate = this;
        //----------------------------------------------------------//

        pokeball.setWillNotDraw(false);

        Button b1=(Button)findViewById(R.id.button);
        asyncSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // do something, the isChecked will be
                // true if the switch is in the On position
                if(!asyncSwitch.isActivated()) {
                    asyncSwitch.setActivated(true);
                }else{
                    asyncSwitch.setActivated(false);
                }
            }
        });
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v("EditText", searchText.getText().toString());
                Toast.makeText(MainActivity.this,"Fetching Data",Toast.LENGTH_LONG).show();
                Log.d("Async Switch",""+asyncSwitch.isAccessibilityFocused());

                if(asyncSwitch.isActivated()){
                    abilitiesShow.setVisibility(View.INVISIBLE);
                    pokeball.setVisibility(View.VISIBLE);
                    executeAsyncTask(searchText.getText().toString());
                    stop = false;
                    new Thread(new Runnable() {
                        public void run() {
                            int test = 0;
                            while (!stop) {
                                new Handler(Looper.getMainLooper()).post(new Runnable() { // Tried new Handler(Looper.myLopper()) also
                                    @Override
                                    public void run() {
                                        pokeball.invalidate();
                                    }
                                });

                            }
                        }
                    }).start();
                } else {
                    startFetchService(searchText.getText().toString());
                }
            }
        });
    }

    private void executeAsyncTask(String str) {
        PokemonAsynctask async = new PokemonAsynctask();
        async.delegate = this;
        async.execute(str);
    }

    public void startFetchService(String URI){
        Intent msgIntent = new Intent(this, WikiFetch.class);
        msgIntent.putExtra(WikiFetch.PARAM_URI,URI);
        startService(msgIntent);

    }

    @Override
    public void processFinish(String output) {
        stop = true;
        pokeball.setVisibility(View.INVISIBLE);
        abilitiesShow.setText(output);
        abilitiesShow.setVisibility(View.VISIBLE);
        Log.d("FIN","Fin");

    }

}



