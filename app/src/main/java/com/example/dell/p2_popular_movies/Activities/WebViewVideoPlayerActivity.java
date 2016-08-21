package com.example.dell.p2_popular_movies.Activities;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.example.dell.p2_popular_movies.Fragments.MovieDetailsActivityFragment;
import com.example.dell.p2_popular_movies.R;
import com.example.dell.p2_popular_movies.Utilites.utility;

/**
 * Created by DELL on 8/13/2016.
 */
public class WebViewVideoPlayerActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_webview);
        String videoId = getIntent().getStringExtra(MovieDetailsActivityFragment.MOVIE_ID);
        final WebView video = (WebView) findViewById(R.id.videoView);
        video.getSettings().setJavaScriptEnabled(true);
        video.getSettings().setPluginState(WebSettings.PluginState.ON);

        video.setWebChromeClient(new WebChromeClient() {
        });

//youtube video url

        final String mimeType = "text/html";
        final String encoding = "UTF-8";
        String html = utility.getVideoHTML(videoId);
        video.loadDataWithBaseURL("", html, mimeType, encoding, "");
    }

}

