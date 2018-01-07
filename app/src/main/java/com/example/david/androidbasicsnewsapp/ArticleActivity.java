package com.example.david.androidbasicsnewsapp;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

// this implementation is heavily based on https://github.com/udacity/ud843-QuakeReport/tree/lesson-four
public class ArticleActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<List<Article>> {

    private static final String LOG_TAG = ArticleActivity.class.getName();

    // Not essential but based on the implementation in EarthquakeActivity
    private static final int ARTICLE_LOADER_ID = 1;

    private ArticleAdapter adapter;

    private TextView emptyStateTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.article_activity);

        ListView articleListView = (ListView) findViewById(R.id.list);

        emptyStateTextView = (TextView) findViewById(R.id.empty_view);
        articleListView.setEmptyView(emptyStateTextView);

        adapter = new ArticleAdapter(this, new ArrayList<Article>());

        articleListView.setAdapter(adapter);

        articleListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Article currentArticle = adapter.getItem(position);
                Uri articleUri = Uri.parse(currentArticle.getWebUrl());
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, articleUri);
                startActivity(websiteIntent);
            }
        });

        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            LoaderManager loaderManager = getLoaderManager();
            loaderManager.initLoader(ARTICLE_LOADER_ID, null, this);
        } else {
            View loadingIndicator = findViewById(R.id.loading_indicator);
            loadingIndicator.setVisibility(View.GONE);
            emptyStateTextView.setText(R.string.no_internet_connection);
        }
    }

    @Override
    public Loader<List<Article>> onCreateLoader(int i, Bundle bundle) {
        Uri baseUri = Uri.parse(getString(R.string.guardian_api_url));
        Uri.Builder uriBuilder = baseUri.buildUpon();
        uriBuilder.appendQueryParameter(getString(R.string.query_param), getString(R.string.default_query));
        uriBuilder.appendQueryParameter(getString(R.string.from_date_param), getString(R.string.default_from_date));
        uriBuilder.appendQueryParameter(getString(R.string.order_by_param), getString(R.string.default_order_by));
        uriBuilder.appendQueryParameter(getString(R.string.api_key_param), getString(R.string.default_api_key));
        uriBuilder.appendQueryParameter(getString(R.string.show_tags_param), getString(R.string.default_contributor));
        uriBuilder.appendQueryParameter(getString(R.string.page_size_param), getString(R.string.default_page_size));
        return new ArticleLoader(this, uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(Loader<List<Article>> loader, List<Article> articles) {

        View loadingIndicator = findViewById(R.id.loading_indicator);
        loadingIndicator.setVisibility(View.GONE);

        emptyStateTextView.setText(R.string.no_articles);

        adapter.clear();

        if (articles != null && !articles.isEmpty()) {
            adapter.addAll(articles);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Article>> loader) {
        adapter.clear();
    }
}
