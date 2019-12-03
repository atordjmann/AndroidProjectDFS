package com.example.projectdfs;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class NewsActivity extends AppCompatActivity {

    List<News> listNews = new ArrayList<News>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        String source = "google-news-fr";

        this.RemplirNewsData(source);

        ListView newsListView = (ListView) findViewById(R.id.mylistview);
        NewsAdapter adapter = new NewsAdapter(this, listNews);
        newsListView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }

    private void RemplirNewsData(String source){
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
