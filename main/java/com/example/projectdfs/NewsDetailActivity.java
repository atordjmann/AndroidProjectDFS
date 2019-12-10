package com.example.projectdfs;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


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

        TextView titre = (TextView) findViewById(R.id.detailTitre);
        TextView auteur = (TextView) findViewById(R.id.detailAuteur);
        TextView date = (TextView) findViewById(R.id.detailDate);
        TextView content = (TextView) findViewById(R.id.detailContent);

        titre.setText(sessionTitre);
        auteur.setText(sessionAuteur);
        date.setText(sessionDate);
        content.setText(sessionContent);

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
}
