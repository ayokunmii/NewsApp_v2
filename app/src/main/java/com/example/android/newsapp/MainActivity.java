package com.example.android.newsapp;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Article>> {
    private static final int EARTHQUAKE_LOADER_ID = 1;
    private static final String JSON_URL = "https://content.guardianapis.com/search?from-date=2018-01-01&order-by=newest&show-tags=contributor&show-fields=thumbnail&page-size=50&q=metoo&api-key=ee7fcfa8-a253-432e-9e44-80655700e71a";
    ListView ArticleListView;
    TextView empty;
    private ArticleAdapter articleAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //initialise listview
        ArticleListView = (ListView) findViewById(R.id.list);
        //initialise arrayadapter
        articleAdapter = new ArticleAdapter(this, new ArrayList<Article>());
        //set the adapter to populate the screen with listview content
        ArticleListView.setAdapter(articleAdapter);
        //initialise textview responsible for displaying network and connection comment
        empty = (TextView) findViewById(R.id.empty_state);
        //set listview to display this textview when it's empty
        ArticleListView.setEmptyView(empty);

        ArticleListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Article currentArticle = articleAdapter.getItem(i);
                Intent openArticle = new Intent(Intent.ACTION_VIEW).setData(Uri.parse(currentArticle.getmWeb()));
                startActivity(openArticle);
            }
        });


        // Get a reference to the ConnectivityManager to check state of network connectivity
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        // Get details on the currently active default data network
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        // If there is a network connection, fetch data
        if (networkInfo != null && networkInfo.isConnected()) {
            // Get a reference to the LoaderManager, in order to interact with loaders.
            LoaderManager loaderManager = getLoaderManager();

            // Initialize the loader. Pass in the int ID constant defined above and pass in null for
            // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
            // because this activity implements the LoaderCallbacks interface).
            loaderManager.initLoader(EARTHQUAKE_LOADER_ID, null, this);
        } else {
            // Otherwise, display error
            // First, hide loading indicator so error message will be visible
            View loadingIndicator = findViewById(R.id.progress_bar);
            loadingIndicator.setVisibility(View.GONE);
            // Update empty state with no connection error message
            empty.setText(R.string.no_internet_connection);
        }
    }

    //Loader activities
    @Override
    public Loader<List<Article>> onCreateLoader(int i, Bundle bundle) {
        Log.i("Loader", "onCreateLoader: ");
        // Create a new loader for the given URL
        return new ArticleLoader(this, JSON_URL);

    }


    @Override
    public void onLoadFinished(Loader<List<Article>> loader, List<Article> stories) {
        empty.setText(R.string.no_article);
        ProgressBar loading = (ProgressBar) findViewById(R.id.progress_bar);
        loading.setVisibility(View.GONE);

        Log.i("Loader", "onFinishedLoader -");
        // Clear the adapter of previous earthquake data
        articleAdapter.clear();

        // If there is a valid list of Articles, then add them to the adapter's
        // data set. This will trigger the ListView to update.
        if (stories != null && !stories.isEmpty()) {
            //comment out to see app when there is no article to display
            articleAdapter.addAll(stories);
        }


    }

    @Override
    public void onLoaderReset(Loader<List<Article>> loader) {
        Log.i("Loader", "onLoaderReset -");
        articleAdapter.clear();
    }
}
