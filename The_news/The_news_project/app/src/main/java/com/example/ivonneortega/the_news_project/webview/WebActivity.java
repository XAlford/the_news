package com.example.ivonneortega.the_news_project.webview;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.ivonneortega.the_news_project.R;
import com.example.ivonneortega.the_news_project.detailView.CollectionDemoActivity;

public class WebActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Create a new WebView to take up the entire activity screen.
        WebView webView = new WebView(this);
        setContentView(webView);

        //Get the current article's URL from the starting intent.
        String url = getIntent().getStringExtra(CollectionDemoActivity.URL);

        //Set up the WebView with e new client, load the retrieved URL.
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl(url);
    }
}
