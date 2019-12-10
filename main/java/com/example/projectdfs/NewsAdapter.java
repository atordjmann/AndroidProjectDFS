package com.example.projectdfs;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;
import java.util.List;

public class NewsAdapter extends BaseAdapter {
    List<News> news;

    LayoutInflater inflater;

    private class ViewHolder{
        TextView tvTitre;
        TextView tvAuteur;
        TextView tvDate;
        ImageView image;
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
            if(position%2 == 0) {
                convertView = inflater.inflate(R.layout.news_item, null);
                holder.tvTitre = (TextView) convertView.findViewById(R.id.txtTitre);
                holder.tvAuteur = (TextView) convertView.findViewById(R.id.txtAuteur);
                holder.tvDate = (TextView) convertView.findViewById(R.id.txtDate);
                holder.image = (ImageView) convertView.findViewById(R.id.imageView);
            }
            else{
                convertView = inflater.inflate(R.layout.news_item2, null);
                holder.tvTitre = (TextView) convertView.findViewById(R.id.txtTitre);
                holder.tvAuteur = (TextView) convertView.findViewById(R.id.txtAuteur);
                holder.tvDate = (TextView) convertView.findViewById(R.id.txtDate);
                holder.image = (ImageView) convertView.findViewById(R.id.imageView);
            }

            convertView.setTag(holder);
        } else {
            Log.v("test", "convertView is not null");
            holder = (ViewHolder) convertView.getTag();
        }
        News actu = news.get(position);
        holder.tvTitre.setText(actu.getTitre());
        holder.tvAuteur.setText(actu.getAuteur());
        holder.tvDate.setText(actu.getDate());
        new DownloadImageTask(holder.image).execute(actu.getImage());
        return convertView;

    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap>{
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
