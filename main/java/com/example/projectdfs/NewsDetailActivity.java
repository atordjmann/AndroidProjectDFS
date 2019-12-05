package com.example.projectdfs;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class NewsDetailActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);
        String sessionTitre = getIntent().getStringExtra("EXTRA_TITRE");
        String sessionAuteur = getIntent().getStringExtra("EXTRA_AUTEUR");
        String sessionDate = getIntent().getStringExtra("EXTRA_DATE");
        String sessionContent = getIntent().getStringExtra("EXTRA_CONTENT");
        String sessionImage = getIntent().getStringExtra("EXTRA_IMAGE");
        String sessionUrl = getIntent().getStringExtra("EXTRA_URL");

        TextView titre = (TextView) findViewById(R.id.detailTitre);
        TextView auteur = (TextView) findViewById(R.id.detailAuteur);
        TextView date = (TextView) findViewById(R.id.detailDate);
        TextView content = (TextView) findViewById(R.id.detailContent);

        titre.setText(sessionTitre == "null"?"pas de titre":sessionTitre);
        auteur.setText(sessionAuteur == "null"?"pas d'auteur":sessionAuteur);
        date.setText(sessionDate=="null"?"pas de date":sessionDate);
        content.setText(sessionContent=="null"?"pas de description":sessionContent);

    }
}
