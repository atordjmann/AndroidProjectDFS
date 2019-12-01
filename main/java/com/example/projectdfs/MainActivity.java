package com.example.projectdfs;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(Build.VERSION.SDK_INT > 9){
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        this.GetSourcesAPI();

        Button btn = (Button)findViewById(R.id.buttonGoogle);
        btn.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(MainActivity.this, NewsActivity.class);
                startActivity(intent);
            }
        });


    }

    private void GetSourcesAPI(){
        try{
            String myUrl = "https://newsapi.org/v2/sources?apiKey=d31f5fa5f03443dd8a1b9e3fde92ec34&language=fr";
            URL url = new URL(myUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            int reponseCode = connection.getResponseCode();
            if(reponseCode == HttpURLConnection.HTTP_OK) {
                InputStream inputStream = connection.getInputStream();
                String result = InputStreamOperations.InputStreamToString(inputStream);
                JSONObject jsonObject = new JSONObject(result);
                JSONArray array = jsonObject.getJSONArray("sources");

                Log.d("result", array.toString());
                TextView mTxtDisplay = (TextView) findViewById(R.id.textHello);
                mTxtDisplay.setText("Response is: " + array.toString());
            }
            else{
                Toast.makeText(this, "Unable to complete your request", Toast.LENGTH_LONG).show();
            }

        } catch(Exception e){
            Toast.makeText(this, "Caught Exception", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
            Log.e("test", e.toString());
        }
    }
}
