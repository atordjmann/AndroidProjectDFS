package com.example.projectdfs;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    List<Source> listSources = new ArrayList<Source>();
    List<String> listSourcesName = new ArrayList<String>();
    List<News> listNews = new ArrayList<News>();
    Spinner spinner;
    int page = 1;
    String source;

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        if (Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        runTask();

    }

    public void runTask () {
        if (isNetworkAvailable()) {

            this.GetSourcesAPI();

            spinner = (Spinner) findViewById(R.id.spinner);
            ArrayAdapter spinnerAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, listSourcesName);
            spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(spinnerAdapter);

            TextView pageTxt = (TextView) findViewById(R.id.pageNbr);
            pageTxt.setText("Page n° " + page);

            source = "google-news-fr";
            this.RemplirNewsData(source, page);

            AdapterView.OnItemSelectedListener elementSelectedListener = new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> spinner, View container, int position, long id) {
                    String sourceName = spinner.getItemAtPosition(position).toString();
                    int index = 0;
                    while (listSources.get(index).getName() != sourceName) {
                        index += 1;
                    }
                    source = listSources.get(index).getId();
                    page = 1;
                    TextView pageTxt = (TextView) findViewById(R.id.pageNbr);
                    pageTxt.setText("Page n° " + page);
                    RemplirNewsData(source, page);
                }

                @Override
                public void onNothingSelected(AdapterView<?> arg0) {
                }
            };
            spinner.setOnItemSelectedListener(elementSelectedListener);


            Button buttonPrev = (Button) findViewById(R.id.buttonprev);
            buttonPrev.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    if(page > 1){
                        page -= 1;
                        RemplirNewsData(source, page);
                        TextView pageTxt = (TextView) findViewById(R.id.pageNbr);
                        pageTxt.setText("Page n° " + page);
                    }
                }

            });
            Button buttonNext = (Button) findViewById(R.id.buttonnext);
            buttonNext.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    try{
                    String myUrl = "https://newsapi.org/v2/everything?apiKey=d31f5fa5f03443dd8a1b9e3fde92ec34&language=fr&sources=" + source + "&page=" + page;
                    URL url = new URL(myUrl);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.connect();
                    int reponseCode = connection.getResponseCode();
                    if (reponseCode == HttpURLConnection.HTTP_OK) {
                        page += 1;
                        RemplirNewsData(source, page);
                        TextView pageTxt = (TextView) findViewById(R.id.pageNbr);
                        pageTxt.setText("Page n° " + page);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                }

            });

        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setCancelable(false);
            builder.setTitle("No Internet connection");
            builder.setMessage("Une connection internet est requise. Veuillez réessayer");
            builder.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    finish();
                }
            });
            builder.setPositiveButton("Réessayer", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();

                    runTask();
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
            Toast.makeText(MainActivity.this, "Network unavailable", Toast.LENGTH_LONG).show();
        }
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
                Toast.makeText(this, "Pas de données à afficher", Toast.LENGTH_LONG).show();
            }

        } catch (Exception e) {
            Toast.makeText(this, "Caught Exception", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
            Log.e("test", e.toString());
        }

    }

    private void RemplirNewsData(final String source, int page) {
        listNews = new ArrayList<News>();
        try {
            String myUrl = "https://newsapi.org/v2/everything?apiKey=d31f5fa5f03443dd8a1b9e3fde92ec34&language=fr&sources=" + source + "&page=" + page;
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
                    SimpleDateFormat format = new SimpleDateFormat(("yyyy-MM-dd'T'HH:mm:ssZ"), Locale.FRENCH);
                    Date dateString = new Date();
                    try {
                        dateString = format.parse(date.replaceAll("Z$", "+0000"));

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }


                    listNews.add(new News(title, auteur == "null" ? "" : auteur, date == "null" ? "" : dateString.toString(), urlToImage, urlArticle, content == "null" ? "" : content));
                }


                ListView newsListView = (ListView) findViewById(R.id.mylistview);
                NewsAdapter adapter = new NewsAdapter(this, listNews);
                newsListView.setAdapter(adapter);
                adapter.notifyDataSetChanged();

                AdapterView.OnItemClickListener setOnActuClickListener = (new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapter, View arg1, int position, long arg3) {
                        News item = (News) adapter.getItemAtPosition(position);
                        Intent intentDetail = new Intent(MainActivity.this, NewsDetailActivity.class);
                        intentDetail.putExtra("EXTRA_TITRE", item.getTitre());
                        intentDetail.putExtra("EXTRA_AUTEUR", item.getAuteur());
                        intentDetail.putExtra("EXTRA_CONTENT", item.getContent());
                        intentDetail.putExtra("EXTRA_DATE", item.getDate());
                        intentDetail.putExtra("EXTRA_IMAGE", item.getImage());
                        intentDetail.putExtra("EXTRA_URL", item.getUrl());
                        intentDetail.putExtra("EXTRA_SOURCE", source);

                        startActivity(intentDetail);
                    }
                });
                newsListView.setOnItemClickListener(setOnActuClickListener);


            } else {
                Toast.makeText(this, "Unable to complete your request", Toast.LENGTH_LONG).show();
            }

        } catch (Exception e) {
            Toast.makeText(this, "Caught Exception", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

}
