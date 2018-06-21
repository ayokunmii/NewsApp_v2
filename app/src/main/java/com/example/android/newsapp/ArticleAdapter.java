package com.example.android.newsapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by ayoawotunde on 18/06/2018.
 */

public class ArticleAdapter extends ArrayAdapter<Article> {
    private static final String SEPARATOR = "T";
    private static final String SEPARATOR_B= "Z";
    TextView dateText;
    TextView timeView;
    public ArticleAdapter(@NonNull Context context, ArrayList<Article>story) {
        super(context, 0, story);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null){
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }

        Article article_details = getItem(position);

        TextView headline = (TextView) listItemView.findViewById(R.id.article_headline);
        headline.setText(article_details.getmTitleArticle());


        TextView author = (TextView) listItemView.findViewById(R.id.author);
        author.setText(article_details.getmAuthor());


        ImageView photo = (ImageView) listItemView.findViewById(R.id.article_image);
        if (article_details != null) {
            new DownloadImageTask(photo).execute(article_details.getmPicture());
        }



        dateText = (TextView) listItemView.findViewById(R.id.date);
        dateText.setText(article_details.getmTime());


        // Find the TextView with view ID time
        timeView = (TextView) listItemView.findViewById(R.id.time);
        timeView.setText(article_details.getmTime());

        String originalTime = article_details.getmTime();


        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'kk:mm:ss'Z'", Locale.UK);
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        Date dateformatted = null;
        try {
            dateformatted = simpleDateFormat.parse(originalTime);
        }catch (ParseException e){
            e.printStackTrace();
        }
        String dateReadyToShow = formatDate(dateformatted);
        if (dateReadyToShow.contains(SEPARATOR)) {
            String[] parts = dateReadyToShow.split(SEPARATOR);
            dateText.setText(parts[0]) ;
            timeView.setText(parts[1]);
        }


        return listItemView;
    }

    //taking care of the image url that is presently displayed a string :)
    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;
        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap bmp = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                bmp = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return bmp;
        }
        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }

    //Returns date as Jun 21, 2018 and time as 01:30pm
    private static String formatDate (Date date) {
        SimpleDateFormat newDateFormat = new SimpleDateFormat("MMM dd, yyyy'T'hh:mm aa", Locale.UK);
        newDateFormat.setTimeZone(TimeZone.getDefault());
        String formattedDate = newDateFormat.format(date);
        return formattedDate;
    }




}
