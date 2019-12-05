package com.example.projectdfs;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class NewsAdapter extends BaseAdapter {
    List<News> news;

    LayoutInflater inflater;

    private class ViewHolder{
        TextView tvTitre;
        TextView tvAuteur;
        TextView tvDate;
    }

    public NewsAdapter(Context context, List<News> objects){
        inflater = LayoutInflater.from(context);
        this.news = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        ViewHolder holder;
        if(convertView == null){
            Log.v("test", "convertView is null");
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.news_item, null);
            holder.tvTitre = (TextView) convertView.findViewById(R.id.txtTitre);
            holder.tvAuteur = (TextView) convertView.findViewById(R.id.txtAuteur);
            holder.tvDate = (TextView) convertView.findViewById(R.id.txtDate);

            convertView.setTag(holder);
        } else {
            Log.v("test", "convertView is not null");
            holder = (ViewHolder) convertView.getTag();
        }
        News actu = news.get(position);
        holder.tvTitre.setText(actu.getTitre());
        holder.tvAuteur.setText(actu.getAuteur());
        holder.tvDate.setText(actu.getDate());
        return convertView;

    }

    @Override
    public int getCount(){
        return news.size();
    }

    @Override
    public News getItem(int position){
        return news.get(position);
    }

    @Override
    public long getItemId(int position){
        return position;
    }
}
