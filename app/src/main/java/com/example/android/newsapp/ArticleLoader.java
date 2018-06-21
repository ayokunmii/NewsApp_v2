package com.example.android.newsapp;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import java.util.List;

/**
 * Created by ayoawotunde on 19/06/2018.
 */

public class ArticleLoader extends AsyncTaskLoader<List<Article>> {
    private String mUrl;
    public ArticleLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }
    @Override
    protected void onStartLoading() {
        Log.i("Loader", "onStartLoading -");
        forceLoad();
    }
    @Override
    public List<Article> loadInBackground() {
        Log.i("Loader", "onLoadInBackground - -");
        if (mUrl == null) {
            return null;
        }

        // Perform the network request, parse the response, and extract articles.
        List<Article> articles = Utils.fetchArticles(mUrl);
        Log.i("Loader", "fetchDataaa -");
        return articles;
    }
}
