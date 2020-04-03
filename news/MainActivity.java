package com.example.news;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = MainActivity.class.getName();

    /**
     * URL for news data from the Guardian dataset
     */
    private static final String GUARDIAN_REQUEST_URL =
            "https://content.guardianapis.com/search?api-key=c53d5b47-655f-4fb2-84db-a9f7e5aa653e";

    /**
     * Adapter for the list of news
     */
    private NewsAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Finds a reference to the ListView in the layout
        ListView newsListView = findViewById(R.id.list);

        // Creates a new adapter that takes an empty list of news as an input
        mAdapter = new NewsAdapter(this, new ArrayList<News>());

        // Sets the adapter on the ListView so the list can be populated in the user interface
        newsListView.setAdapter(mAdapter);


        // Sets an item click listener on the ListView, which sends an intent to a web browser
        // to open a website with more information about the selected article.
        newsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // Find the current article that was clicked on
                News currentNews = mAdapter.getItem(position);

                // Convert the String URL into a URI object (to pass into the Intent constructor)
                Uri newsUri = Uri.parse(currentNews.getUrl());

                // Create a new intent to view the news URI
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, newsUri);

                // Send the intent to launch a new activity
                startActivity(websiteIntent);
            }
        });

        // Start the AsyncTask to fetch the earthquake data
        NewsAsyncTask task = new NewsAsyncTask();
        task.execute(GUARDIAN_REQUEST_URL);
    }

    /**
     * {@link AsyncTask} to perform the network request on a background thread, and then update the UI with the list of news in the response.
     */
    @SuppressLint("StaticFieldLeak")
    private class NewsAsyncTask extends AsyncTask<String, Void, List<News>> {
        /**
         * Runs a background thread and performs the network request.
         */
        @Override
        protected List<News> doInBackground(String... urls) {

            if (urls.length < 1 || urls[0] == null) {
                return null;
            }

            return QueryUtils.fetchNewsData(urls[0]);
        }

        /**
         * Runs the main UI thread after the background work has been completed. This method receives as input, the return value from the doInBackground() method.
         */
        @Override
        protected void onPostExecute(List<News> data) {
            // Clears the adapter of previous news data
            mAdapter.clear();

            // If there is a valid list of {@link News}, then add them to the adapter's
            if (data != null && !data.isEmpty()) {
                mAdapter.addAll(data);
            }
        }

    }

    }

