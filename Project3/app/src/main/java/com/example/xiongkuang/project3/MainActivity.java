package com.example.xiongkuang.project3;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button googleButton;
    private Button amazonButton;
    private Button githubButton;
    private WebView mWebView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        googleButton = (Button)findViewById(R.id.google_button);
        amazonButton = (Button)findViewById(R.id.amazon_button);
        githubButton = (Button)findViewById(R.id.github_button);
        mWebView = (WebView)findViewById(R.id.webView);

        googleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mWebView.getSettings().setLoadsImagesAutomatically(true);
                mWebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
                mWebView.getSettings().setJavaScriptEnabled(true);
                mWebView.loadUrl("https://www.google.com");

            }
        });

        amazonButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mWebView.getSettings().setLoadsImagesAutomatically(true);
                mWebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
                mWebView.getSettings().setJavaScriptEnabled(true);
                mWebView.loadUrl("http://www.amazon.com");

            }
        });

        githubButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mWebView.getSettings().setLoadsImagesAutomatically(true);
                mWebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
                mWebView.getSettings().setJavaScriptEnabled(true);
                mWebView.loadUrl("https://github.com/");

            }
        });


        mWebView.getSettings().setLoadsImagesAutomatically(true);
        mWebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.loadUrl("https://github.com/");
        mWebView.loadUrl("https://www.google.com");


    }
//
//    public void onGoogleClicked(View view){
//
//    }
//
//    public void onAmazonClicked(View view){
//
//    }
//
//    public void onYoutubeClicked(View view){
//
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
