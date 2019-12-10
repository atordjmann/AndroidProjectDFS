package com.example.projectdfs;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.InputStream;


public class NewsDetailActivity extends AppCompatActivity {

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);
        String sessionTitre = getIntent().getStringExtra("EXTRA_TITRE");
        String sessionAuteur = getIntent().getStringExtra("EXTRA_AUTEUR");
        String sessionDate = getIntent().getStringExtra("EXTRA_DATE");
        String sessionContent = getIntent().getStringExtra("EXTRA_CONTENT");
        String sessionImage = getIntent().getStringExtra("EXTRA_IMAGE");
        final String sessionUrl = getIntent().getStringExtra("EXTRA_URL");
        String sessionSource = getIntent().getStringExtra("EXTRA_SOURCE");

        TextView titre = (TextView) findViewById(R.id.detailTitre);
        TextView auteur = (TextView) findViewById(R.id.detailAuteur);
        TextView date = (TextView) findViewById(R.id.detailDate);
        TextView content = (TextView) findViewById(R.id.detailContent);
        TextView source = (TextView) findViewById(R.id.detailSource);
        new DownloadImageTask((ImageView) findViewById(R.id.imageView2)).execute(sessionImage);

        titre.setText(sessionTitre);
        auteur.setText(sessionAuteur);
        date.setText(sessionDate);
        content.setText(sessionContent);
        source.setText(sessionSource);

        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(NewsDetailActivity.this, WebViewActivity.class);
                intent.putExtra("EXTRA_URL", sessionUrl);
                startActivity(intent);
            }

        });

    }
    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage){
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls ){
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try{
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            }catch(Exception e){
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result){
            bmImage.setImageBitmap(result);
        }
    }
}
