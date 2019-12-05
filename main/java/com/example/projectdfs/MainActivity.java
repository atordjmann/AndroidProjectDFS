package com.example.projectdfs;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
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

    List<Source> listSources = new ArrayList<Source>();
    List<String> listSourcesName = new ArrayList<String>();
    List<News> listNews = new ArrayList<News>();
    Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        if(Build.VERSION.SDK_INT > 9){
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        this.GetSourcesAPI();

        spinner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter spinnerAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, listSourcesName );
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);

        String source = "google-news-fr";
        this.RemplirNewsData(source);

        AdapterView.OnItemSelectedListener elementSelectedListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> spinner, View container, int position, long id) {
                String sourceName = spinner.getItemAtPosition(position).toString();
                int index = 0;
                while(listSources.get(index).getName() != sourceName){
                    index +=1;
                }
                String sourceId = listSources.get(index).getId();
                RemplirNewsData(sourceId);
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        };
        spinner.setOnItemSelectedListener(elementSelectedListener);
    }

    private void GetSourcesAPI() {
        try {
            String myUrl = "https://newsapi.org/v2/sources?apiKey=d31f5fa5f03443dd8a1b9e3fde92ec34&language=fr";
            URL url = new URL(myUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            int reponseCode = connection.getResponseCode();
            if (reponseCode == HttpURLConnection.HTTP_OK) {
                InputStream inputStream = connection.getInputStream();
                String result = InputStreamOperations.InputStreamToString(inputStream);
                JSONObject jsonObject = new JSONObject(result);
                JSONArray array = jsonObject.getJSONArray("sources");
                for (int i = 0; i < array.length(); i++) {
                    JSONObject elmt = array.getJSONObject(i);
                    String id = elmt.getString("id");
                    String name = elmt.getString("name");
                    String description = elmt.getString("description");
                    String urlSource = elmt.getString("url");
                    String category = elmt.getString("category");

                    listSources.add(new Source(id, name, description, urlSource, category));
                    listSourcesName.add(name);
                }

                //TextView mTxtDisplay = (TextView) findViewById(R.id.textHello);
                //mTxtDisplay.setText("Response is: " + array.toString());
            } else {
                Toast.makeText(this, "Unable to complete your request", Toast.LENGTH_LONG).show();
            }

        } catch (Exception e) {
            Toast.makeText(this, "Caught Exception", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
            Log.e("test", e.toString());
        }
    }

        private void RemplirNewsData (String source){
        listNews = new ArrayList<News>();
            try {
                String myUrl = "https://newsapi.org/v2/everything?apiKey=d31f5fa5f03443dd8a1b9e3fde92ec34&language=fr&sources=" + source;
                URL url = new URL(myUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                int reponseCode = connection.getResponseCode();
                if (reponseCode == HttpURLConnection.HTTP_OK) {
                    InputStream inputStream = connection.getInputStream();
                    String result = InputStreamOperations.InputStreamToString(inputStream);
                    JSONObject jsonObject = new JSONObject(result);
                    JSONArray array = jsonObject.getJSONArray("articles");
                    for (int i = 0; i < array.length(); i++) {
                        String title = array.getJSONObject(i).getString("title");
                        String auteur = array.getJSONObject(i).getString("author");
                        String date = array.getJSONObject(i).getString("publishedAt");
                        String urlArticle = array.getJSONObject(i).getString("url");
                        String urlToImage = array.getJSONObject(i).getString("urlToImage");
                        String content = array.getJSONObject(i).getString("content");


                        listNews.add(new News(title, auteur, date, urlToImage, urlArticle, content));
                    }
                    Log.i("data", listNews.toString());

                    ListView newsListView = (ListView) findViewById(R.id.mylistview);
                    NewsAdapter adapter = new NewsAdapter(this, listNews);
                    newsListView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();

                    AdapterView.OnItemClickListener setOnActuClickListener = ( new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapter, View arg1, int position, long arg3) {
                            String item = adapter.getItemAtPosition(position).toString();
                            Log.i("detail", item);
                        }
                    }  );
                    newsListView.setOnItemClickListener(setOnActuClickListener);


                } else {
                    Toast.makeText(this, "Unable to complete your request", Toast.LENGTH_LONG).show();
                }

            } catch (Exception e) {
                Toast.makeText(this, "Caught Exception", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
                Log.e("test", e.toString());
            }
        }

}
